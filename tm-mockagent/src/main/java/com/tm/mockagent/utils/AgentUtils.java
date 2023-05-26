package com.tm.mockagent.utils;

import com.tm.mockagent.entities.model.MockAgentArgsInfo;
import org.apache.commons.lang3.StringUtils;

public class AgentUtils {
    private AgentUtils() {}

    public static MockAgentArgsInfo getAgentInfo(String agentArgs) {
        MockAgentArgsInfo info = new MockAgentArgsInfo();
        if(StringUtils.isBlank(agentArgs)) {
            return info;
        }
        final String[] args = agentArgs.split("\\+");
        if(args == null || args.length == 0) {
            return info;
        }
        for (int i = 0; i < args.length; i++) {
            final String[] kv = args[i].split("=");
            if(kv.length == 2) {
                switch (kv[0]) {
                    case "applicationName":
                        info.setApplicationName(kv[1]);
                        break;
                    case "ip":
                        info.setIp(kv[1]);
                        break;
                    case "port":
                        if(StringUtils.isNumeric(kv[1])) {
                            info.setPort(Integer.parseInt(kv[1]));
                        }
                        break;
                    case "name":
                        info.setName(kv[1]);
                        break;
                    case "description":
                        info.setDescription(kv[1]);
                        break;
                    case "mockServerIp":
                        info.setMockServerIp(kv[1]);
                        break;
                    case "mockServerPort":
                        if(StringUtils.isNumeric(kv[1])) {
                            info.setMockServerPort(Integer.parseInt(kv[1]));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return info;
    }
}
