package com.tm.web;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
@Slf4j
public class JsonUtil {
    public static final Gson gson = new Gson();
    public static String getJsonPath(String json, String path) {
        Map<String, Object> map = gson.fromJson(json, HashMap.class);
        Object currObject = map;
        if(path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] nodes = path.split("/");
        if(nodes.length < 1) {
            return null;
        }
        for (int i = 0; i < nodes.length; i++) {
            String currNode = nodes[i];
            if(i == 0 && !map.containsKey(currNode)) {
                log.error("key {} 不存在", currNode);
                return null;
            }
            if(StringUtils.isBlank(currNode)) {
                log.error("json path中有空值, {}", path);
                return null;
            }
            if(StringUtils.isNumeric(currNode)) {
                if(!(currObject instanceof List)) {
                    log.error("不存在 键 {}", currNode);
                    return null;
                }else {
                    List list = (List) currObject;
                    Integer ind = Integer.valueOf(currNode);
                    if(ind >= list.size()) {
                        log.error("键 {} 越界", currNode);
                        return null;
                    }else{
                        currObject = list.get(ind);
                    }
                }
            }else{
                if(!(currObject instanceof Map)) {
                    log.error("不存在 键 {}", currNode);
                    return null;
                }else {
                    Map currMap = (Map) currObject;
                    if(!currMap.containsKey(currNode)) {
                        log.error("不存在 键 {}", currNode);
                        return null;
                    } else {
                        currObject = currMap.get(currNode);
                    }
                }
            }
            if(i == nodes.length - 1) {
                return gson.toJson(currObject);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String s = "{\"a\": 1}";
        String path = "/a";
        System.out.println(getJsonPath(s, path));
        int[] nums = {1,2,3};
    }
}
