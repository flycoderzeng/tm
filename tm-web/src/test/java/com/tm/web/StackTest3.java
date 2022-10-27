package com.tm.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

public class StackTest3 {
    public static Boolean CONDITIONAL = false;
    public static String __TRUE = "__TRUE";
    public static String __FALSE = "__FALSE";
    public static List<String> OPERATOR_LIST = Arrays.asList("=, <>, >, >=, <, <=, AND, OR, NOT, (, )".split(", "));
    public static List<String> LOGIC_OPERATOR_LIST = Arrays.asList("AND, OR, NOT".split(", "));
    public static List<String> RELATION_OPERATOR_LIST = Arrays.asList("=, <>, >, >=, <, <=".split(", "));
    public static Map<String, String> map = new HashMap<>();

    public static Stack<String> operatorStack = new Stack<>();
    public static Stack<String> operandStack = new Stack<>();
    public static Queue<String> queue = new LinkedBlockingDeque<>();

    public static Map<String, Integer> priorityMap = new HashMap<>();

    //public static String c = "IF [VerifyType] = 1 AND NOT ( [Username] = \"Hello2\" OR [Age] > 18 ) THEN [Mobile] = \"空\";";
    public static String c = "IF [VerifyType] = 1 AND NOT [Username] = \"Hello2\" AND NOT [Age] > 18 THEN [Mobile] = \"空\";";
    //public static String c = "[VerifyType] = 1 AND [Mobile] <> \"空\";";
    //public static String c = "IF [VerifyType] = 1 THEN [Mobile] <> \"空\";";
    //public static String c = "IF [VerifyType] = 2 THEN [Mail] <> \"空\";";
    public static void main(String[] args) throws Exception {
        priorityMap.put("(", 3);
        priorityMap.put(")", 0);
        priorityMap.put("=", 2);
        priorityMap.put("<>", 2);
        priorityMap.put(">", 2);
        priorityMap.put(">=", 2);
        priorityMap.put("<", 2);
        priorityMap.put("<=", 2);
        priorityMap.put("AND", 1);
        priorityMap.put("OR", 1);
        priorityMap.put("NOT", 1);


        map.put("Username", "Hello");
        map.put("VerifyType", "1");
        map.put("Mobile", "空");
        map.put("Age", "18");
        System.out.println(map);
        if(!c.endsWith(";")) {
            throw new Exception("表达式必须以分号结束");
        }
        c = c.substring(0, c.length()-1) + " ;";
        System.out.println(c);
        String[] list = c.split("\\s");
        queue.addAll(Arrays.asList(list));
        String str;
        while ((str = queue.poll()) != null) {
            if(str.equals("IF")) {
                CONDITIONAL = true;
                continue;
            }
            if(str.equals("THEN") || str.equals(";")) {
                if(!operatorStack.isEmpty()) {
                    cal(str, true);
                }
                // 说明IF或约束表达式 结束 检查操作数栈的最终结果值是不是为TRUE
                String result = operandStack.peek();
                if(!result.equals(__TRUE) && str.equals(";")) {
                    throw new Exception("用例不符合约束表达式");
                } else if (!result.equals(__TRUE) && str.equals("THEN")) {
                    throw new Exception("用例不符合约束表达式IF部分");
                } else if(result.equals(__TRUE) && str.equals(";")) {
                    break;
                } else {
                    operandStack.clear();
                    continue;
                }
            } else if (OPERATOR_LIST.indexOf(str) != -1) {
                if(operatorStack.isEmpty()) {
                    operatorStack.add(str);
                    continue;
                }
                cal(str, false);
                continue;
            } else if (str.startsWith("[")) {
                if(!str.endsWith("]")) {
                    throw new Exception("表达式错误: " + str);
                }
                String key = str.substring(1, str.length()-1);
                if(!map.containsKey(key)) {
                    throw new Exception(key + " 参数不存在");
                }
                String o = map.get(key);
                if(o == null) {
                    throw new Exception(key + " 参数值是空");
                }
                operandStack.add(o);
                continue;
            } else {
                str = str.replace("\"", "");
                operandStack.add(str);
                continue;
            }
        }
    }

    public static void cal(String operator, boolean end) throws Exception {
//        System.out.println("操作数栈: " + operandStack);
//        System.out.println("操作符栈: " + operatorStack);
        // 取出操作栈顶的操作符A
        // 比较新的操作符和操作符A的优先级   如果大于0，则将新的操作符入操作符栈，如果小于或等于，则弹出操作符栈顶的操作符B
        // 如果操作符B是NOT 则弹出一个操作数 做取反操作，将结果压入操作数栈
        // 如果操作符B不是NOT 则弹出两个操作数 做对应操作，将结果压入操作数栈
        for(;;) {
            if((operatorStack.isEmpty() && !end) || operator.equals("NOT")) {
                operatorStack.add(operator);
                break;
            }
            if(operatorStack.isEmpty() && end) {
                break;
            }
            String op2 = !operatorStack.isEmpty() ? operatorStack.peek() : "";
            Integer priorityResult = comparePriority(operator, op2);
            //System.out.println("判断操作符优先级，op1: " + operator + ", op2: " + op2 + ", result: " + priorityResult);
            if (priorityResult > 0) {
                operatorStack.add(operator);
                break;
            } else {
                if (operator.equals(")") && op2.equals("(")) {
                    operatorStack.pop();
                    break;
                } else if (op2.equals("(")) {
                    operatorStack.add(operator);
                    break;
                }
                op2 = operatorStack.pop();
                String left;
                String right;
                right = operandStack.pop();
                if (!op2.equals("NOT")) {
                    left = operandStack.pop();
                } else {
                    left = right;
                }
                Boolean result = getExecuteResult(op2, left, right);
                operandStack.push(changeBooleanToString(result));
            }
        }
    }

    /**
     * 比较操作符优先级 括号 大于 关系操作 大于 逻辑操作符
     * @param op1
     * @param op2
     * @return
     * 大于0
     * 等于0
     * 小于0
     */
    public static Integer comparePriority(String op1, String op2) {
        if(op1 == null || op2 == null || op1.isEmpty() || op2.isEmpty() || op1.equals("THEN") || op1.equals(";")) {
            return -1;
        }
        if(op1.equals("(")) {
            return 1;
        }
        return priorityMap.get(op1) - priorityMap.get(op2);
    }

    public static boolean changeStringBoolean(String src) {
        if(src == null) {
            return false;
        }
        if(src.equals(__TRUE)) {
            return true;
        }
        return false;
    }

    public static String changeBooleanToString(Boolean src) {
        if(src == null) {
            return __FALSE;
        }
        if(src.equals(true)) {
            return __TRUE;
        }
        return __FALSE;
    }

    public static boolean getExecuteResult(String op, String left, String right) throws Exception {
        if(LOGIC_OPERATOR_LIST.indexOf(op) != -1) {
            return getLogicExecuteResult(op, changeStringBoolean(left), changeStringBoolean(right));
        }
        if(RELATION_OPERATOR_LIST.indexOf(op) != -1) {
            return getRelationExecuteResult(op, left, right);
        }
        return false;
    }

    public static boolean getLogicExecuteResult(String op, Boolean left, Boolean right) throws Exception {
        System.out.println("进行逻辑运算 op: " + op + ", left: " + left + ", right: " + right);
        switch (op) {
            case "AND":
                return left && right;
            case "OR":
                return left || right;
            case "NOT":
                return !left;
            default:
                throw new Exception("不支持的逻辑操作符");

        }
    }

    public static boolean getRelationExecuteResult(String op, String left, String right) throws Exception {
        System.out.println("进行关系运算 op: " + op + ", left: " + left + ", right: " + right);
        // 0-字符串 1-数字
        int leftType = 0;
        int rightType = 0;
        Double x1 = new Double(0);
        Double x2 = new Double(0);
        try {
            x1 = Double.valueOf(left);
            leftType = 1;
        } catch (Exception e) {

        }
        try {
            x2 = Double.valueOf(right);
            rightType = 1;
        } catch (Exception e) {

        }
        if (leftType != rightType) {
            throw new Exception("类型不一致");
        }
        switch (op) {
            case "=":
                if(leftType == 0) {
                    return left.equals(right);
                }else{
                    return x1.equals(x2);
                }
            case "<>":
                if(leftType == 0) {
                    return !left.equals(right);
                }else {
                    return !x1.equals(x2);
                }
            case "<":
                if(leftType == 0) {
                    return left.compareTo(right) < 0 ? true : false;
                } else {
                    return x1 < x2;
                }
            case "<=":
                if(leftType == 0) {
                    return left.compareTo(right) <= 0 ? true : false;
                } else {
                    return x1 <= x2;
                }
            case ">":
                if(leftType == 0) {
                    return left.compareTo(right) > 0 ? true : false;
                } else {
                    return x1 > x2;
                }
            case ">=":
                if(leftType == 0) {
                    return left.compareTo(right) >= 0 ? true : false;
                } else {
                    return x1 >= x2;
                }
            default:
                throw new Exception("不支持的操作符");
        }
    }
}
