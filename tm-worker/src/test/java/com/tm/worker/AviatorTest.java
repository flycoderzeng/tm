package com.tm.worker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.*;
import com.tm.worker.core.function.extractor.JsonExtractor;
import com.tm.worker.core.function.random.GetRandomInt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AviatorTest {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        AviatorEvaluator.addFunction(new GetRandomInt());
        AviatorEvaluator.addFunction(new GetDateTest());
        AviatorEvaluator.addFunction(new JsonExtractor());

        Map<String, Object> env = new HashMap<>();
        env.put("count", "1");
        env.put("__response", "{\"a\":1}");
        Map<String, Object> map = new HashMap<>();
        map.put("result", 1);
        env.put("map", map);
        System.out.println(AviatorEvaluator.execute("__getRandomInt(count+1)", env));
        System.out.println(AviatorEvaluator.execute("m = GetDateTest('yyyyMMdd');m.result;", env));
        System.out.println(AviatorEvaluator.execute("m = __jsonExtractor('$.a', __response)", env));
    }

    public static class GetDateTest extends AbstractFunction {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            String format = FunctionUtils.getStringValue(arg1, env);
            SimpleDateFormat f = new SimpleDateFormat(format);
            Map<String, Object> map = new HashMap<>();
            map.put("result", f.format(new Date()));
            map.put("result_1", new Date().getTime());

            return new AviatorJavaType("map");
        }

        /**
         * 返回方法名
         */
        @Override
        public String getName() {
            return "GetDateTest";
        }
    }
}
