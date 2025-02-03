package com.tm.common.config;

public class FileDirConfig {
    public static String TEMP_DOWNLOAD_FILES_PATH = System.getProperty("user.home") + "/.tm/data/ci/autotest/temp/download-files";
    public static String TEMP_UPLOAD_FILES_PATH = System.getProperty("user.home") + "/.tm/data/ci/autotest/temp/upload-files";
    public static String COVERAGE_JAVA_SOURCE_CODE_PATH = System.getProperty("user.home") + "/.tm/data/ci/autotest/coverage-java-source-code";
}
