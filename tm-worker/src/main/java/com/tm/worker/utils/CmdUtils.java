package com.tm.worker.utils;

import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CmdUtils {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String WINDOWS_RANDOM_STRING_EXE_PATH = System.getProperty("user.home") + "/data/ci/autotest/tools/random_string.exe";
    private static final String LINUX_RANDOM_STRING_EXE_PATH = "/data/ci/autotest/tools/random_string";

    private CmdUtils() {}

    public static String randomStringByRegex(String regex) {
        try {
            if(OS.contains("windows")) {
                return new ProcessExecutor().command(WINDOWS_RANDOM_STRING_EXE_PATH, regex)
                        .readOutput(true).execute()
                        .outputUTF8().trim();
            }else{
                return new ProcessExecutor().command(LINUX_RANDOM_STRING_EXE_PATH, regex)
                        .readOutput(true).execute()
                        .outputUTF8().trim();
            }
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
