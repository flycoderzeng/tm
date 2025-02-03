package com.tm.web.jacoco;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.tm.common.base.mapper.CoverageInfoMapper;
import com.tm.common.base.model.CoverageInfo;
import com.tm.common.config.FileDirConfig;
import com.tm.common.entities.autotest.request.OpenClassBody;
import com.tm.common.entities.autotest.request.OpenPackageBody;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.zeroturnaround.exec.ProcessExecutor;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.tm.web.utils.CoverageUtils.extractDirectoryName;
import static com.tm.web.utils.CoverageUtils.getCodeCoverageResult;

@Slf4j
@RestController
@RequestMapping(value = "/coverage")
public class JacocoCoverageController extends BaseController {
    @Autowired
    private CoverageInfoMapper coverageInfoMapper;

    @PostMapping(value = "/uploadWithCoverageInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse uploadFileWithJson(
            @RequestParam("file") MultipartFile file,
            @RequestParam("coverageInfo") String info) {

        if (file.isEmpty()) {
            return ResultUtils.error("文件为空，请选择一个文件上传。");
        }
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResultUtils.error("文件名为空，请选择一个文件上传。");
            }
            if (!originalFilename.endsWith(".xml")) {
                return ResultUtils.error("请选择一个xml文件上传。");
            }
            CoverageInfo coverageInfo = JSON.parseObject(info, CoverageInfo.class);
            Path path;
            FileUtil.mkdir(FileDirConfig.TEMP_UPLOAD_FILES_PATH);
            path = Paths.get(FileDirConfig.TEMP_UPLOAD_FILES_PATH + File.separator + Objects.requireNonNull(originalFilename));
            Files.write(path, bytes);
            List<String> classURIList = new ArrayList<>();
            JacocoCoverageResult codeCoverageResult = getCodeCoverageResult(path.toFile().getAbsolutePath(), classURIList);
            coverageInfo.setMissedInstructions(codeCoverageResult.getMissedInstructions());
            coverageInfo.setCoveredInstructions(codeCoverageResult.getCoveredInstructions());
            coverageInfo.setMissedBranches(codeCoverageResult.getMissedBranches());
            coverageInfo.setCoveredBranches(codeCoverageResult.getCoveredBranches());
            coverageInfo.setMissedCxty(codeCoverageResult.getMissedCxty());
            coverageInfo.setCoveredCxty(codeCoverageResult.getCoveredCxty());
            coverageInfo.setMissedClasses(codeCoverageResult.getMissedClasses());
            coverageInfo.setCoveredClasses(codeCoverageResult.getCoveredClasses());
            coverageInfo.setMissedMethods(codeCoverageResult.getMissedMethods());
            coverageInfo.setCoveredMethods(codeCoverageResult.getCoveredMethods());
            coverageInfo.setMissedLines(codeCoverageResult.getMissedLines());
            coverageInfo.setCoveredLines(codeCoverageResult.getCoveredLines());
            coverageInfo.setAddUser(getLoginUser().getAddUser());
            coverageInfo.setAddTime(new Date());
            coverageInfo.setCoverageInfo(JSON.toJSONString(codeCoverageResult).getBytes());
            coverageInfoMapper.insertBySelective(coverageInfo);
            return ResultUtils.success();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.error("", e);
            return ResultUtils.error(e.getMessage());
        }
    }

    @PostMapping(value = "/getCoverageInfo", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getCoverageInfo(@RequestBody IdBody body) {
        return ResultUtils.success(coverageInfoMapper.selectByPrimaryId(body.getId()));
    }

    @PostMapping(value = "/getPackageLevelCoverageInfo", produces = {"application/json;charset=UTF-8"})
    public BaseResponse getPackageLevelCoverageInfo(@RequestBody IdBody body) {
        CoverageInfo coverageInfo = coverageInfoMapper.selectByPrimaryId2(body.getId());
        String info = new String(coverageInfo.getCoverageInfo());
        JacocoCoverageResult jacocoCoverageResult = JSON.parseObject(info, JacocoCoverageResult.class);
        List<JacocoCoverageResult.PackageCoverageResult> packageCoverageResults = jacocoCoverageResult.getPackageCoverageResults();
        List<Map<String, Object>> listMaps = new ArrayList<>();
        for (JacocoCoverageResult.PackageCoverageResult packageCoverageResult : packageCoverageResults) {
            String packageName = packageCoverageResult.getName().replace("/", ".");
            List<JacocoCoverageResult.Counter> counters = packageCoverageResult.getCounters();
            listMaps.add(new HashMap<>() {{
                put("name", packageName);
                put("type", "package");
                put("counters", counters);
            }});
        }
        return ResultUtils.success(listMaps);
    }

    @PostMapping(value = "/openPackageLevelCoverageInfo", produces = {"application/json;charset=UTF-8"})
    public BaseResponse openPackageLevelCoverageInfo(@RequestBody OpenPackageBody body) {
        CoverageInfo coverageInfo = coverageInfoMapper.selectByPrimaryId2(body.getId());
        String info = new String(coverageInfo.getCoverageInfo());
        JacocoCoverageResult jacocoCoverageResult = JSON.parseObject(info, JacocoCoverageResult.class);
        List<JacocoCoverageResult.PackageCoverageResult> packageCoverageResults = jacocoCoverageResult.getPackageCoverageResults();
        List<Map<String, Object>> listMaps = new ArrayList<>();
        for (JacocoCoverageResult.PackageCoverageResult packageCoverageResult : packageCoverageResults) {
            String packageName = packageCoverageResult.getName().replace("/", ".");
            if(StringUtils.equals(packageName, body.getPackageName())) {
                return ResultUtils.success(packageCoverageResult.getClassCoverageResults());
            }
        }
        return ResultUtils.success(listMaps);
    }

    @PostMapping(value = "/openClassCoverageInfo", produces = {"application/json;charset=UTF-8"})
    public BaseResponse openClassCoverageInfo(@RequestBody OpenClassBody body) throws Exception {
        CoverageInfo coverageInfo = coverageInfoMapper.selectByPrimaryId(body.getId());
        String directoryName = extractDirectoryName(coverageInfo.getGitUrl());
        String dirPath = FileDirConfig.COVERAGE_JAVA_SOURCE_CODE_PATH + File.separator + directoryName + File.separator + coverageInfo.getBranch();
        FileUtil.mkdir(dirPath);
        boolean dirEmpty = FileUtil.isDirEmpty(new File(dirPath));
        String output = "";
        if(dirEmpty) {
            if (OS.contains("windows")) {
                byte[] bytes = new ProcessExecutor().command("cmd", "/c", "git clone --depth=1 " + coverageInfo.getGitUrl() + " " + dirPath)
                        .readOutput(true).execute().output();
                output = new String(bytes, "GBK");
            } else {
                output = new ProcessExecutor().command("/bin/bash", "-c", "git clone --depth=1 " + coverageInfo.getGitUrl() + " " + dirPath)
                        .readOutput(true).execute()
                        .outputUTF8();
            }
            log.info(output);
        }else{
            if (OS.contains("windows")) {
                byte[] bytes = new ProcessExecutor().command("cmd", "/c", "cd /d " + dirPath + " && git pull origin " + coverageInfo.getBranch())
                        .readOutput(true).execute().output();
                output = new String(bytes, "GBK");
            } else {
                output = new ProcessExecutor().command("/bin/bash", "-c", "cd " + dirPath + " && git pull origin " + coverageInfo.getBranch())
                        .readOutput(true).execute()
                        .outputUTF8();
            }
            log.info(output);
        }
        List<File> files = FileUtil.loopFiles(dirPath, pathname -> pathname.getAbsolutePath().contains(body.getPackageName().replace(".", File.separator) + File.separator + body.getSourceFileName()));
        if(!files.isEmpty()) {
            return ResultUtils.success(new String(FileUtil.readBytes(files.get(0))));
        }
        return ResultUtils.success();
    }
}
