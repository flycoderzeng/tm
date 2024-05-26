package com.tm.mockagent.utils;

import com.tm.mockagent.entities.model.MockAgentArgs;
import org.apache.commons.lang3.StringUtils;

public class AgentUtils {
    private AgentUtils() {}

    public static MockAgentArgs getAgentInfo(String agentArgs) {
        MockAgentArgs info = new MockAgentArgs();
        if(StringUtils.isBlank(agentArgs)) {
            return info;
        }
        final String[] args = agentArgs.split("\\+");
        for (String arg : args) {
            final String[] kv = arg.split("=");
            if (kv.length == 2) {
                switch (kv[0]) {
                    case "applicationName":
                        info.setApplicationName(kv[1]);
                        break;
                    case "ip":
                        info.setIp(kv[1]);
                        break;
                    case "port":
                        if (StringUtils.isNumeric(kv[1])) {
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
                        if (StringUtils.isNumeric(kv[1])) {
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
