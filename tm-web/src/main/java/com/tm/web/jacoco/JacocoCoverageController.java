package com.tm.web.jacoco;

import com.alibaba.fastjson2.JSON;
import com.tm.common.base.mapper.CoverageInfoMapper;
import com.tm.common.base.model.CoverageInfo;
import com.tm.common.config.FileDirConfig;
import com.tm.common.entities.base.BaseResponse;
import com.tm.common.entities.base.IdBody;
import com.tm.common.utils.ResultUtils;
import com.tm.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
            if(System.getProperty("os.name").toLowerCase().contains("windows")) {
                path = Paths.get(FileDirConfig.WINDOWS_TEMP_UPLOAD_FILES_PATH + File.separator + Objects.requireNonNull(originalFilename));
            }else{
                path = Paths.get(FileDirConfig.LINUX_TEMP_UPLOAD_FILES_PATH + File.separator + Objects.requireNonNull(originalFilename));
            }
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
                put("counters", counters);
            }});
        }
        return ResultUtils.success(listMaps);
    }
}
