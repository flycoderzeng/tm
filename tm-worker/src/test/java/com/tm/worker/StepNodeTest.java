package com.tm.worker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.common.entities.autotest.enumerate.StepNodeTypeDefineEnum;
import com.tm.worker.core.control.GenericController;
import com.tm.worker.core.logic.Action;
import com.tm.worker.core.logic.SetUp;
import com.tm.worker.core.logic.TearDown;
import com.tm.worker.core.node.RootNode;
import com.tm.worker.core.node.StepNode;
import com.tm.worker.core.node.StepNodeBase;
import com.tm.worker.core.node.function.extractor.JsonMultiExtractorNode;
import com.tm.worker.core.node.function.extractor.XmlMultiExtractorNode;
import com.tm.worker.core.node.function.time.GetDateNode;
import com.tm.worker.core.node.function.randomizer.GetRandomIntNode;
import com.tm.worker.core.node.function.time.GetTimestampNode;
import com.tm.worker.core.protocol.http.HttpSampler;
import com.tm.worker.utils.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StepNodeTest {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    public static String steps = "[{\"type\":\"root\",\"level\":1,\"define\":{\"userDefinedVariables\":[{\"name\":\"count\",\"type\":\"string\",\"value\":\"5\",\"key\":\"16264464083865944800957332\"},{\"name\":\"hello\",\"type\":\"string\",\"value\":\"world\",\"key\":\"16264464083861011192954593\"},{\"name\":\"world\",\"type\":\"string\",\"value\":\"很大\",\"key\":\"16264464083866836220069976\"},{\"name\":\"cardno\",\"type\":\"string\",\"value\":\"1234\",\"key\":\"1626446408386576002745554\"},{\"name\":\"openDate\",\"type\":\"string\",\"value\":\"nidemama\",\"key\":\"1627054586275413370963444\"},{\"name\":\"name\",\"type\":\"string\",\"value\":\"sss\",\"key\":\"163604037725023243797550\"},{\"name\":\"time\",\"type\":\"string\",\"value\":\"\",\"key\":\"16376757291292517360443974\"},{\"name\":\"wwwww\",\"type\":\"string\",\"value\":\"10\",\"key\":\"16395762189579225585890257\"}],\"name\":\"自动化用例222333333嗡嗡嗡p\",\"comments\":\"天若有情天亦老33333\",\"enabled\":true,\"cookies\":[{\"name\":\"session\",\"value\":\"${count}\",\"description\":\"\",\"type\":\"text\",\"domain\":\"localhost\",\"path\":\"/\",\"key\":\"162696286149242795457962\"}]},\"title\":\"自动化用例222333333嗡嗡嗡p\",\"key\":\"1\",\"isLeaf\":false,\"children\":[{\"type\":\"setUp\",\"level\":2,\"define\":{\"name\":\"初始化环境22223333555\",\"comments\":\"yes\",\"enabled\":true},\"children\":[{\"type\":\"调用平台API(__getRandomInt)\",\"isLeaf\":true,\"title\":\"生成随机卡号\",\"level\":3,\"key\":\"16269470019755069564457561\",\"children\":[],\"define\":{\"name\":\"生成随机卡号\",\"comments\":\"\",\"parametricList\":[{\"name\":\"count\",\"value\":\"${count}\"},{\"name\":\"result\",\"value\":\"${cardno}\"},{\"name\":\"prefix\",\"value\":\"5555\"},{\"name\":\"suffix\",\"value\":\"9988\"},{\"name\":\"result_1\",\"value\":\"${hello}\"},{\"name\":\"result_2\",\"value\":\"${jiji}\"}],\"platformApiId\":10001,\"enabled\":true}},{\"type\":\"http\",\"isLeaf\":true,\"title\":\"send http\",\"level\":3,\"key\":\"1626666936683817622592626\",\"children\":[],\"define\":{\"name\":\"send http\",\"comments\":\"\",\"enabled\":true,\"requestType\":\"GET\",\"url\":\"http://127.0.0.1:9081/api/v1/group/1\",\"params\":[{\"name\":\"appp\",\"value\":\"${cardno}\",\"description\":\"\",\"key\":\"16268511695607158130981751\"},{\"name\":\"b\",\"value\":\"${hello}\",\"description\":\"\",\"key\":\"16268511695606113605560904\"},{\"name\":\"c\",\"value\":\"333\",\"description\":\"\",\"key\":\"16268511695606026122276009\"},{\"name\":\"d\",\"value\":\"3333\",\"description\":\"\",\"key\":\"16268511695604161355812089\"}],\"checkErrorList\":[{\"name\":\"\",\"value\":\"${jiji}\",\"description\":\"\",\"type\":\"text\",\"domain\":\"\",\"path\":\"\",\"relationOperator\":\"1\",\"assertLevel\":\"error\",\"key\":\"16271321385735175909933678\",\"extractorType\":\"1\"},{\"name\":\"\",\"value\":\"\",\"description\":\"\",\"type\":\"text\",\"domain\":\"\",\"extractorType\":\"1\",\"path\":\"\",\"relationOperator\":\"1\",\"assertLevel\":\"error\",\"key\":\"1628692891091983902857215\"}],\"responseExtractorList\":[{\"name\":\"\",\"value\":\"\",\"description\":\"\",\"type\":\"text\",\"domain\":\"\",\"path\":\"\",\"relationOperator\":\"1\",\"assertLevel\":\"error\",\"key\":\"16271333781843780210168909\",\"extractorType\":\"1\"},{\"name\":\"\",\"value\":\"\",\"description\":\"\",\"type\":\"text\",\"domain\":\"\",\"extractorType\":\"1\",\"path\":\"\",\"relationOperator\":\"1\",\"assertLevel\":\"error\",\"key\":\"16271334466699022111013561\"}],\"headers\":[{\"name\":\"Content-Type\",\"value\":\"application/json\",\"description\":\"\",\"type\":\"text\",\"key\":\"16271119047197502726250735\"}],\"bodyType\":\"raw\",\"rawType\":\"xml\",\"formData\":[{\"name\":\"jiji\",\"value\":\"积极\",\"description\":\"上证综指\",\"type\":\"text\",\"key\":\"16267035080795692454468420\"}],\"formUrlencoded\":[{\"name\":\"hello\",\"value\":\"jiij\",\"description\":\"几把\",\"type\":\"text\",\"key\":\"16286926086089662281873696\"}],\"content\":\"{\\\"a\\\": 12}\"}},{\"type\":\"调用平台API(__jsonMultiExtractor)\",\"isLeaf\":true,\"title\":\"响应提取\",\"level\":3,\"key\":\"16286929814363809296911126\",\"children\":[],\"define\":{\"name\":\"响应提取\",\"comments\":\"\",\"parametricList\":[{\"name\":\"path1\",\"value\":\"$.code\"},{\"name\":\"result1\",\"value\":\"${hello}\"},{\"name\":\"result2\",\"value\":\"${hello}\"},{\"name\":\"content\",\"value\":\"{\\\"cardNo\\\": \\\"1234\\\"}\"},{\"name\":\"path_1\",\"value\":\"$.cardNo\"},{\"name\":\"result_1\",\"value\":\"${cardno}\"}],\"platformApiId\":10004,\"enabled\":true},\"disabled\":false}],\"isLeaf\":false,\"title\":\"初始化环境22223333555\",\"key\":\"2\"},{\"type\":\"action\",\"level\":2,\"define\":{\"name\":\"嘎嘎嘎\",\"comments\":\"\",\"enabled\":true},\"children\":[{\"type\":\"if\",\"isLeaf\":false,\"title\":\"如果\",\"level\":3,\"key\":\"16265855863589761624245010\",\"children\":[{\"type\":\"while\",\"isLeaf\":false,\"title\":\"while循环\",\"level\":3,\"key\":\"16265888595149600585817390\",\"children\":[{\"type\":\"调用平台API(__operationExpression)\",\"isLeaf\":true,\"title\":\"__operationExpression\",\"level\":4,\"key\":\"16395762602186251001632134\",\"children\":[],\"define\":{\"name\":\"__operationExpression\",\"comments\":\"\",\"parametricList\":[{\"name\":\"expression_0\",\"value\":\"${wwwww} - 1\"},{\"name\":\"result_0\",\"value\":\"${wwwww}\"}],\"platformApiId\":10016,\"enabled\":true}}],\"define\":{\"name\":\"while循环\",\"comments\":\"\",\"condition\":\"${wwwww} > 0\",\"enabled\":true}}],\"define\":{\"name\":\"如果\",\"comments\":\"\",\"condition\":\"${hello} > 2\",\"enabled\":true}},{\"type\":\"loop\",\"isLeaf\":false,\"title\":\"固定循环多少次2\",\"level\":3,\"key\":\"16265943369008350478662281\",\"children\":[{\"type\":\"调用平台API(__getDate)\",\"isLeaf\":true,\"title\":\"获取时间\",\"level\":3,\"key\":\"16368574626888175064180772\",\"children\":[],\"define\":{\"name\":\"获取时间\",\"comments\":\"\",\"parametricList\":[{\"name\":\"result\",\"value\":\"${openDate}\"},{\"name\":\"offset\",\"value\":\"-1\"}],\"platformApiId\":10002,\"enabled\":true}}],\"define\":{\"name\":\"固定循环多少次2\",\"comments\":\"\",\"condition\":\"2\",\"enabled\":true}},{\"type\":\"调用平台API(__getTimestamp)\",\"isLeaf\":true,\"title\":\"获取时间戳\",\"level\":3,\"key\":\"16376757438553598590566380\",\"children\":[],\"define\":{\"name\":\"获取时间戳\",\"comments\":\"\",\"parametricList\":[{\"name\":\"result\",\"value\":\"${time}\"},{\"name\":\"type\",\"value\":\"1\"}],\"platformApiId\":10007,\"enabled\":true}},{\"type\":\"jdbc request\",\"isLeaf\":true,\"title\":\"jdbc request\",\"level\":3,\"key\":\"16408545417715058943442028\",\"children\":[],\"define\":{\"name\":\"执行数据库操作\",\"comments\":\"\",\"enabled\":true,\"dbName\":\"test\",\"resultSetVariableName\":\"\",\"content\":\"select * from city limit 1\",\"checkErrorList\":[],\"responseExtractorList\":[]}}],\"isLeaf\":false,\"title\":\"嘎嘎嘎\",\"key\":\"3\"},{\"type\":\"teardown\",\"level\":2,\"define\":{\"name\":\"清理环境22332222\",\"comments\":\"必然会执行的逻辑，相当于finally\",\"enabled\":true},\"children\":[],\"isLeaf\":false,\"title\":\"清理环境22332222\",\"key\":\"4\"}]}]";

    public StepNode caseStepTree;

    public static void main(String[] args) {
        Map[] stepNodes = gson.fromJson(steps, HashMap[].class);
        //System.out.println(stepNode);
        Map node = stepNodes[0];
        StepNodeTest stepNodeTest = new StepNodeTest();
        stepNodeTest.change(node, null, true);
        System.out.println("end");
    }

    public void change(Map node, StepNode parentNode, boolean first) {
        List<Map> children = null;
        if(node.containsKey("children")) {
            children = (List<Map>) node.remove("children");
        }
        Map define = null;
        if(node.containsKey("define")) {
            define = (Map) node.remove("define");
        }

        StepNode stepNode = BeanUtils.mapToBean(StepNode.class, node);
        if(first) {
            caseStepTree = stepNode;
        }

        stepNode.setDefine(getDefine(node, define));
        stepNode.setChildren(new ArrayList<>());
        stepNode.setParent(parentNode);
        if(parentNode != null) {
            parentNode.getChildren().add(stepNode);
        }
        if(children != null && !children.isEmpty()) {
            for (Map child : children) {
                change(child, stepNode, false);
            }
        }
    }

    public static StepNodeBase getDefine(Map node, Map defineMap) {
        String type = "root";
        if(node.containsKey("type")) {
            type = node.get("type").toString();
        }
        Pattern pattern = Pattern.compile("调用平台API\\((.*)\\)");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()) {
            type = matcher.group(1);
        }
        StepNodeBase define;
        StepNodeTypeDefineEnum nodeTypeDefineEnum = StepNodeTypeDefineEnum.get(type);
        if(nodeTypeDefineEnum == null) {
            System.out.println(type);
        }

        switch (nodeTypeDefineEnum) {
            case ROOT:
                define = BeanUtils.mapToBean(RootNode.class, defineMap);
                return define;
            case SETUP:
                define = BeanUtils.mapToBean(SetUp.class, defineMap);
                return define;
            case ACTION:
                define = BeanUtils.mapToBean(Action.class, defineMap);
                return define;
            case TEARDOWN:
                define = BeanUtils.mapToBean(TearDown.class, defineMap);
                return define;
            case HTTP:
                define = BeanUtils.mapToBean(HttpSampler.class, defineMap);
                return define;
            case IF:
            case LOOP:
            case WHILE:
                define = BeanUtils.mapToBean(GenericController.class, defineMap);
                return define;
            case __getDate:
                define = BeanUtils.mapToBean(GetDateNode.class, defineMap);
                return define;
            case __getRandomInt:
                define = BeanUtils.mapToBean(GetRandomIntNode.class, defineMap);
                return define;
            case __getTimestamp:
                define = BeanUtils.mapToBean(GetTimestampNode.class, defineMap);
                return define;
            case __jsonMultiExtractor:
                define = BeanUtils.mapToBean(JsonMultiExtractorNode.class, defineMap);
                return define;
            case __xmlMultiExtractor:
                define = BeanUtils.mapToBean(XmlMultiExtractorNode.class, defineMap);
                return define;
            default:
                return null;
        }
    }
}
