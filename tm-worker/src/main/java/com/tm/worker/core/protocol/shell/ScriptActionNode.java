package com.tm.worker.core.protocol.shell;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.tm.common.base.model.AutoScript;
import com.tm.worker.core.exception.TMException;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.threads.AutoTestContext;
import com.tm.worker.core.threads.AutoTestContextService;
import com.tm.worker.core.variable.AutoTestVariables;
import com.tm.worker.utils.ExpressionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class ScriptActionNode extends StepNodeBase {
    private String content;
    private String scriptResultVariableName;

    private String WINDOWS_TEMP_SCRIPT_PATH = System.getProperty("user.home") + "/data/ci/autotest/temp/scripts";
    private String LINUX_TEMP_SCRIPT_PATH = "/data/ci/autotest/temp/scripts";

    private String SHELL_RESULT_FLAG = "RESULT=";

    private String interpreterPath;

    private Integer scriptId;

    private static String OS = System.getProperty("os.name").toLowerCase();

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行shell script步骤，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(content) && scriptId == null) {
            throw new TMException("执行脚本和脚本内容 不能同时为空");
        }
        content = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
        if(StringUtils.isBlank(content) && scriptId == null) {
            throw new TMException("执行脚本和脚本内容 不能同时为空");
        }

        scriptResultVariableName = ExpressionUtils.replaceExpression(scriptResultVariableName, caseVariables.getVariables());

        String tempScriptPath = makeTempScript(context);
        execTempScript(caseVariables, tempScriptPath);
    }

    private void execTempScript(AutoTestVariables caseVariables, String tempScriptPath) throws InterruptedException, IOException {
        Process process;
        String cmd = "";
        String cd;
        if (OS.contains("windows")) {
            cd = "cd /d" + WINDOWS_TEMP_SCRIPT_PATH;
        }else{
            cd = "cd " + LINUX_TEMP_SCRIPT_PATH;
        }
        List<String> cmds = new ArrayList<>();
        if (OS.contains("windows")) {
            cmds.add("cmd.exe");
            cmds.add("/c");
        } else {
            if(tempScriptPath.endsWith(".py")) {
                cmds.add("python");
            }else{
                cmds.add("sh");
                cmds.add("-c");
            }
        }

        if (StringUtils.isNoneBlank(interpreterPath)) {
            cmd += interpreterPath + " " + tempScriptPath;
        } else {
            if(tempScriptPath.endsWith(".py")) {
                cmd = "python ";
            }else{
                cmd = "sh -x ";
            }
            cmd += tempScriptPath;
        }

        if(scriptId != null && StringUtils.isNoneBlank(content)) {
            cmd += " " + content;
        }
        cmds.add(cd + "&&" + cmd);

        log.info(StringUtils.join(cmds, " "));
        process = RuntimeUtil.exec(cmds.toArray(String[]::new));
        process.waitFor(600, TimeUnit.SECONDS);
        log.info("shell exit code: {}", process.exitValue());
        addResultInfoLine("shell exit code: " + process.exitValue());

        StringBuilder stdoutBuilder = new StringBuilder();
        BufferedReader stdoutBufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = stdoutBufferedReader.readLine()) != null) {
            if(line.startsWith(SHELL_RESULT_FLAG) && StringUtils.isNoneBlank(scriptResultVariableName)) {
                caseVariables.put(scriptResultVariableName, line.substring(SHELL_RESULT_FLAG.length()));
            }
            stdoutBuilder.append(line).append("\n");
        }
        log.info(stdoutBuilder.toString());
        addResultInfoLine(stdoutBuilder.toString());

        StringBuilder stderrBuilder = new StringBuilder();
        String charsetName;
        if (OS.contains("windows")) {
            charsetName = "GBK";
        } else {
            charsetName = "UTF8";
        }
        BufferedReader stderrBufferedReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), charsetName));
        while ((line = stderrBufferedReader.readLine()) != null) {
            stderrBuilder.append(line).append("\n");
        }
        log.info(stderrBuilder.toString());
        addResultInfoLine(stderrBuilder.toString());

        if(process.exitValue() != 0) {
            throw new TMException("脚本退出码不等于0");
        }
    }

    private String makeTempScript(AutoTestContext context) {
        String scriptContent;
        String suffix = "";
        if(scriptId != null) {
            AutoScript autoScript = context.getTaskService().findAutoScript(scriptId);
            if(autoScript == null) {
                throw new TMException("未找到脚本: " + scriptId);
            }
            if(autoScript.getType() == 1) {
                suffix = ".sh";
            }
            if(autoScript.getType() == 2 || autoScript.getType() == 3) {
                suffix = ".py";
            }
            scriptContent = autoScript.getContent();
        }else{
            scriptContent = content;
        }
        if(!OS.contains("windows")) {
            scriptContent = scriptContent.replace("\r\n", "\n");
        }
        if(StringUtils.isBlank(scriptContent)) {
            throw new TMException("脚本内容为空");
        }
        String tempScriptName = System.currentTimeMillis() + "_" + context.getPlanTask().getPlanExecuteResultId()
                + "_" + context.getCaseTask().getAutoCase().getId() + "_" + context.getCaseTask().getGroupNo();
        String tempScriptPath;
        if(OS.contains("windows")) {
            FileUtil.mkdir(WINDOWS_TEMP_SCRIPT_PATH);
            tempScriptPath = WINDOWS_TEMP_SCRIPT_PATH + File.separator + tempScriptName + suffix;
        }else{
            FileUtil.mkdir(LINUX_TEMP_SCRIPT_PATH);
            tempScriptPath = LINUX_TEMP_SCRIPT_PATH + File.separator + tempScriptName + suffix;
        }
        log.info(tempScriptPath);
        File file = FileUtil.writeUtf8String(scriptContent, tempScriptPath);
        if(!file.exists()) {
            throw new TMException("创建临时脚本文件失败");
        }
        return tempScriptPath;
    }
}
