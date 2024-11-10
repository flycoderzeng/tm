package com.tm.ma;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.tm.ma.jdbc.JDBCConfig;
import com.tm.ma.jdbc.JdbcDataSource;
import com.tm.ma.utils.JDBCUtils;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info(JSON.toJSONString(args));
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("命令行工具", options);
            System.exit(1);
        }
        String functionNo = cmd.getOptionValue("function");
        if ("1".equals(functionNo)) {
            getToken(cmd, formatter, options);
        }else if("2".equals(functionNo)) {
            execSql(cmd, formatter, options);
        }else if("3".equals(functionNo)) {
            findLog(cmd, formatter, options);
        }else if("4".equals(functionNo)) {
            showResource(cmd, formatter, options);
        }
    }

    private static void showResource(CommandLine commandLine, HelpFormatter formatter, Options options) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("===========> 进程资源使用情况 <===========").append(System.lineSeparator());
        String cmd = "top -b -n 1 -o %MEM|head -60";
        stringBuilder.append(new ProcessExecutor().command("/bin/bash", "-c", cmd)
                .readOutput(true).execute()
                .outputUTF8());
        cmd = "free -h";
        stringBuilder.append("===========> 内存使用情况 <===========").append(System.lineSeparator());
        stringBuilder.append(new ProcessExecutor().command("/bin/bash", "-c", cmd)
                .readOutput(true).execute()
                .outputUTF8());
        cmd = "df -h";
        stringBuilder.append("===========> 磁盘使用情况 <===========").append(System.lineSeparator());
        stringBuilder.append(new ProcessExecutor().command("/bin/bash", "-c", cmd)
                .readOutput(true).execute()
                .outputUTF8());
        System.out.println(stringBuilder);
    }

    private static void findLog(CommandLine commandLine, HelpFormatter formatter, Options options) throws Exception {
        boolean isListLogFile = commandLine.hasOption("list-log-files");
        String logKeyword = commandLine.getOptionValue("log-keyword");
        if(StringUtils.isBlank(logKeyword)) {
            logKeyword = "ERROR";
        }
        String lastLines = commandLine.getOptionValue("last-lines");
        if(StringUtils.isBlank(lastLines)) {
            lastLines = "1000";
        }
        if(!StrUtil.isNumeric(lastLines)) {
            System.out.println("last-lines 必须是 整数.");
            formatter.printHelp("命令行工具", options);
            System.exit(1);
        }

        String lastMinutes = commandLine.getOptionValue("last-minutes");
        if(StringUtils.isBlank(lastMinutes)) {
            lastMinutes = "60";
        }
        if(!StrUtil.isNumeric(lastMinutes)) {
            System.out.println("last-minutes 必须是 整数.");
            formatter.printHelp("命令行工具", options);
            System.exit(1);
        }
        String cmd = "df -h|grep rootfs|awk '{print $NF}'|grep -oE \"/.*rootfs\"|tail -1";
        String logDir = new ProcessExecutor().command("/bin/bash", "-c", cmd)
                .readOutput(true).execute()
                .outputUTF8();
        logger.info(logDir);
        if(StringUtils.isBlank(logDir)) {
            logDir = "/mnt/log";
        }else{
            logDir = logDir.trim();
            logDir += "/mnt/log";
        }
        cmd = "find " + logDir + " -name \"*.log\" -type f -mtime 0";
        logger.info(cmd);
        if(isListLogFile) {
            String logFilePathList = new ProcessExecutor().command("/bin/bash", "-c", cmd)
                    .readOutput(true).execute()
                    .outputUTF8();
            System.out.println(logFilePathList);
            System.exit(0);
        }
        String logFilePathInput = commandLine.getOptionValue("log-file-path");
        String finalLastLines = lastLines;
        String finalLogKeyword = logKeyword;
        long finalLastMinutes = Long.parseLong(lastMinutes);
        if(StringUtils.isNotBlank(logFilePathInput)) {
            processLogFile(logFilePathInput, finalLastLines, finalLogKeyword, finalLastMinutes);
        }else{
            new ProcessExecutor().command("/bin/bash", "-c", cmd)
                    .redirectOutput(new LogOutputStream() {
                        @Override
                        protected void processLine(String logFilePath) {
                            processLogFile(logFilePath, finalLastLines, finalLogKeyword, finalLastMinutes);
                        }
                    }).execute();
        }
    }

    public static long extractTimestamp(String log) {
        String pattern = "\\[?\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(.\\d{1,3})?\\]?";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(log);
        if (matcher.find()) {
            String group = matcher.group(0);
            int beginIndex = 0;
            int endIndex = group.length();
            if(group.startsWith("[")) {
                beginIndex = 1;
            }
            if(group.endsWith("]")) {
                endIndex = group.length()-1;
            }
            String timestampStr = group.substring(beginIndex, endIndex);
            String dateFormatter = "yyyy-MM-dd HH:mm:ss";
            if(timestampStr.length()-4 == dateFormatter.length()) {
                dateFormatter = "yyyy-MM-dd HH:mm:ss" + timestampStr.charAt(dateFormatter.length()) + "SSS";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter);
            DateTime dateTime = DateUtil.parse(timestampStr, formatter);
            return dateTime.getTime();
        }
        return -1;
    }

    private static void processLogFile(String logFilePath, String finalLastLines, String finalLogKeyword, long finalLastMinutes) {
        final String tailCmd = "tail -" + finalLastLines + " " + logFilePath;
        final long lastMinutes = finalLastMinutes * 60 * 1000;
        final long currentMinutes = System.currentTimeMillis();
        try {
            String logContent = new ProcessExecutor().command("/bin/bash", "-c", tailCmd)
                    .readOutput(true).execute()
                    .outputUTF8();
            StringBuilder stringBuilder = new StringBuilder();
            try (Stream<String> stream = Arrays.stream(logContent.split("\n"))) {
                final boolean[] isOpen = {false};
                stream.filter(line -> {
                    if (line.contains(finalLogKeyword) && (currentMinutes - extractTimestamp(line)) < lastMinutes) {
                        isOpen[0] = true;
                    }
                    if (isOpen[0] && !line.contains(finalLogKeyword)
                            && ReUtil.isMatch("\\[?\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(,\\d{1,3})?\\]?.*",
                            line.substring(0, Math.min(line.length(), 25)))) {
                        isOpen[0] = false;
                    }
                    return isOpen[0];
                }).map(line -> line + System.lineSeparator()).forEach(stringBuilder::append);
                if(!stringBuilder.isEmpty()) {
                    System.out.printf("===========> %s <===========\n", logFilePath);
                    System.out.print(stringBuilder);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private static void execSql(CommandLine cmd, HelpFormatter formatter, Options options) throws Exception {
        String sql = cmd.getOptionValue("sql");
        String jdbcUrl = cmd.getOptionValue("jdbc-url");
        String dbUsername = cmd.getOptionValue("db-username");
        String dbPassword = cmd.getOptionValue("db-password");
        if(StringUtils.isBlank(sql) || StringUtils.isBlank(jdbcUrl)
                || StringUtils.isBlank(dbUsername)
                || StringUtils.isBlank(dbPassword)) {
            System.out.println("please input [jdbc url] [db username] [db password] [sql].");
            formatter.printHelp("命令行工具", options);
            System.exit(1);
        }
        logger.info("====>sql: {}", sql);
        JDBCConfig jdbcConfig = new JDBCConfig();
        jdbcConfig.setJdbcUrl(jdbcUrl);
        jdbcConfig.setUsername(dbUsername);
        jdbcConfig.setPassword(dbPassword);
        JdbcDataSource dataSource = new JdbcDataSource(jdbcConfig);
        long start = System.currentTimeMillis();
        Connection conn = dataSource.getConnection();
        long end = System.currentTimeMillis();
        logger.error("====>获取连接耗时: {}ms", end - start);
        String sqlExecTimes = cmd.getOptionValue("sql-exec-times");
        int sqlExecTimeInt = 1;
        if(StringUtils.isNoneBlank(sqlExecTimes) && StringUtils.isNumeric(sqlExecTimes)) {
            sqlExecTimeInt = Integer.parseInt(sqlExecTimes);
            if(sqlExecTimeInt > 100000) {
                sqlExecTimeInt = 1;
            }
        }
        if(sql.toLowerCase().startsWith("select")) {
            List<Map<String, Object>> maps = null;
            start = System.currentTimeMillis();
            for (int i = 0; i < sqlExecTimeInt; i++) {
                maps = JDBCUtils.doQuery(conn, sql);
            }
            logger.info(JSON.toJSONString(maps));
            end = System.currentTimeMillis();
            logger.info("====>执行{}次select耗时: {}ms", sqlExecTimeInt, end - start);
        }else if(sql.toLowerCase().startsWith("insert") || sql.toLowerCase().startsWith("update")) {
            start = System.currentTimeMillis();
            for (int i = 0; i < sqlExecTimeInt; i++) {
                JDBCUtils.doUpdate(conn, sql);
            }
            end = System.currentTimeMillis();
            logger.info("====>执行{}次insert or update耗时: {}ms", sqlExecTimeInt, end - start);
        }else{
            logger.error("不支持的sql语句, 只能是select 和 insert 语句");
            System.exit(1);
        }
    }

    private static void getToken(CommandLine cmd, HelpFormatter formatter, Options options) {
        String platformIp = cmd.getOptionValue("platform-ip");
        String platformPort = cmd.getOptionValue("platform-port");
        String userId = cmd.getOptionValue("uid");
        String password = cmd.getOptionValue("pwd");
        String portType = cmd.getOptionValue("port-type");
        if(StringUtils.isBlank(platformIp) || StringUtils.isBlank(platformPort)
                || StringUtils.isBlank(userId) || StringUtils.isBlank(password) || StringUtils.isBlank(portType)) {
            System.out.println("please input [platform ip] [platform port] [user id or app id] [password or app secret] [1 admin port or 2 user port].");
            formatter.printHelp("命令行工具", options);
            System.exit(1);
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        Option funcOption = new Option("func", "function", true, "function no,1: get token,2: olym jdbc test,3: find log,4: show system resource");
        funcOption.setRequired(true);
        options.addOption(funcOption);

        Option platformIpOption = new Option("sip", "platform-ip", true, "platform ip");
        platformIpOption.setRequired(false);
        options.addOption(platformIpOption);

        Option platformPortOption = new Option("sport", "platform-port", true, "platform port");
        platformPortOption.setRequired(false);
        options.addOption(platformPortOption);

        Option uidOption = new Option("U", "uid", true, "user id");
        uidOption.setRequired(false);
        options.addOption(uidOption);

        Option pwdOption = new Option("P", "pwd", true, "password");
        pwdOption.setRequired(false);
        options.addOption(pwdOption);

        Option portTypeOption = new Option("pt", "port-type", true, "port type, 1: admin port, 2: user port");
        portTypeOption.setRequired(false);
        options.addOption(portTypeOption);

        Option clientIdOption = new Option("cid", "client-id", true, "client id");
        clientIdOption.setRequired(false);
        options.addOption(clientIdOption);

        Option clientSecretOption = new Option("csec", "client-secret", true, "client secret");
        clientSecretOption.setRequired(false);
        options.addOption(clientSecretOption);

        Option jdbcUrlOption = new Option("ju", "jdbc-url", true, "jdbc url");
        jdbcUrlOption.setRequired(false);
        options.addOption(jdbcUrlOption);

        Option dbUsernameOption = new Option("du", "db-username", true, "数据库用户名");
        dbUsernameOption.setRequired(false);
        options.addOption(dbUsernameOption);

        Option dbPwdOption = new Option("dpwd", "db-password", true, "数据库密码");
        dbPwdOption.setRequired(false);
        options.addOption(dbPwdOption);

        Option sqlOption = new Option("sql", "sql", true, "执行的SQL语句, 只能是insert select update");
        sqlOption.setRequired(false);
        options.addOption(sqlOption);

        Option sqlExecTimesOption = new Option("set", "sql-exec-times", true, "SQL执行次数, 比如 100, 1000");
        sqlExecTimesOption.setRequired(false);
        options.addOption(sqlOption);

        Option logKeywordOption = new Option("lkw", "log-keyword", true, "查找的日志内容的关键字, 默认 ERROR");
        logKeywordOption.setRequired(false);
        options.addOption(logKeywordOption);

        Option lastLinesOption = new Option("lls", "last-lines", true, "最后多少行, 默认1000");
        lastLinesOption.setRequired(false);
        options.addOption(lastLinesOption);

        Option lastMinitusOption = new Option("lms", "last-minutes", true, "最后多少分钟内的日志, 默认60分钟");
        lastMinitusOption.setRequired(false);
        options.addOption(lastMinitusOption);

        Option listLogFileOption = new Option("llf", "list-log-files", false, "列出所有日志文件");
        listLogFileOption.setRequired(false);
        options.addOption(listLogFileOption);

        Option logFilePathOption = new Option("lfp", "log-file-path", true, "指定日志文件");
        logFilePathOption.setRequired(false);
        options.addOption(logFilePathOption);

        return options;
    }
}