package com.tm.worker.core.protocol.shell;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class ScriptActionNode extends StepNodeBase {
    private String content;
    private String scriptResultVariableName;

    private String WINDOWS_TEMP_SCRIPT_PATH = "D:\\data\\ci\\autotest\\temp\\scripts";
    private String LINUX_TEMP_SCRIPT_PATH = "/data/ci/autotest/temp/scripts";

    private String SHELL_RESULT_FLAG = "RESULT=";

    private static String OS = System.getProperty("os.name").toLowerCase();

    @Override
    public void run() throws Exception {
        super.run();
        log.info("执行shell script步骤，{}", getName());
        AutoTestContext context = AutoTestContextService.getContext();
        AutoTestVariables caseVariables = context.getCaseVariables();
        if(StringUtils.isBlank(content)) {
            addResultInfoLine("shell脚本内容为空");
            return ;
        }
        content = ExpressionUtils.replaceExpression(content, caseVariables.getVariables());
        if(StringUtils.isBlank(content)) {
            addResultInfoLine("shell脚本内容为空");
            return ;
        }

        scriptResultVariableName = ExpressionUtils.replaceExpression(scriptResultVariableName, caseVariables.getVariables());

        String tempScriptPath = makeTempScript(context);
        execTempScript(caseVariables, tempScriptPath);
    }

    private void execTempScript(AutoTestVariables caseVariables, String tempScriptPath) throws InterruptedException, IOException {
        Process process;
        if(OS.indexOf("windows") > -1) {
            process = RuntimeUtil.exec("cmd.exe /c " + tempScriptPath);
        }else{
            process = RuntimeUtil.exec("sh -x " + tempScriptPath);
        }
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
        BufferedReader stderrBufferedReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), "GBK"));
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
        String tempScriptName = System.currentTimeMillis() + "_" + context.getPlanTask().getPlanExecuteResultId()
                + "_" + context.getCaseTask().getAutoCase().getId() + "_" + context.getCaseTask().getGroupNo();
        String tempScriptPath;
        if(OS.indexOf("windows") > -1) {
            tempScriptPath = WINDOWS_TEMP_SCRIPT_PATH + File.separator + tempScriptName + ".bat";
        }else{
            tempScriptPath = LINUX_TEMP_SCRIPT_PATH + File.separator + tempScriptName + ".sh";
        }
        log.info(tempScriptPath);
        File file = FileUtil.writeUtf8String(content, tempScriptPath);
        if(!file.exists()) {
            throw new TMException("创建临时脚本文件失败");
        }
        return tempScriptPath;
    }
}
