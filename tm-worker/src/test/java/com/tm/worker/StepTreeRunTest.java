package com.tm.worker;

import com.google.gson.Gson;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.LinkedList;
import java.util.Stack;

public class StepTreeRunTest {
    public static volatile boolean running = true;
    public static void main(String[] args) {
        StepNode stepNode = new StepNode("root");
        stepNode.setStepId(1);
        StepNode stepNode1 = new StepNode("setUp");
        stepNode1.setStepId(2);
        stepNode.setChildren(new StepNode[3]);
        stepNode.getChildren()[0] = stepNode1;

        StepNode stepNode2 = new StepNode("action");
        stepNode2.setStepId(3);
        stepNode.getChildren()[1] = stepNode2;

        StepNode stepNode4 = new StepNode("http");
        stepNode4.setStepId(5);

        stepNode2.setChildren(new StepNode[2]);

        stepNode2.getChildren()[0] = stepNode4;

        StepNode stepNode5 = new StepNode("while");
        stepNode5.setStepId(6);

        stepNode2.getChildren()[1] = stepNode5;

        StepNode stepNode6 = new StepNode("http");
        stepNode6.setStepId(7);
        stepNode5.setChildren(new StepNode[1]);
        stepNode5.getChildren()[0] = stepNode6;

        StepNode stepNode3 = new StepNode("tearDown");
        stepNode3.setStepId(4);
        stepNode.getChildren()[2] = stepNode3;

        StepNode stepNode7 = new StepNode("http");
        stepNode7.setStepId(8);
        stepNode3.setChildren(new StepNode[1]);
        stepNode3.getChildren()[0] = stepNode7;

        System.out.println(new Gson().toJson(stepNode));

        run2(stepNode);
    }


    public static void run2(StepNode rootNode) {
        Stack<StepNode> stack = new Stack<>();
        LinkedList<StepNode> linkedList = new LinkedList<>();
        linkedList.add(rootNode);
        for(;;) {
            if(linkedList.isEmpty() && !stack.isEmpty()) {
                StepNode temp = stack.pop();
                System.out.println("run " + temp.getType() + " end--");
                continue;
            }
            if(linkedList.isEmpty() && stack.isEmpty()) {
                break;
            }
            StepNode currStepNode = linkedList.get(0);
            if(currStepNode.isEnabled()) {
                stack.add(currStepNode);
                currStepNode.run();
                linkedList.remove(0);
            }else{
                continue;
            }
            if(currStepNode.getChildren() != null && currStepNode.getChildren().length > 0) {
                if(((currStepNode.getType().equals("while")
                        || currStepNode.getType().equals("loop"))
                        && !currStepNode.isBreakLoop()) || !(currStepNode.getType().equals("while")
                        || currStepNode.getType().equals("loop"))) {
                    for (int i = currStepNode.getChildren().length - 1; i >= 0; i--) {
                        linkedList.add(0, currStepNode.getChildren()[i]);
                    }
                }else{
                    StepNode temp = stack.pop();
                    System.out.println("run " + temp.getType() + ", id: " + temp.getStepId() + " end--");
                }
            }else{
                StepNode temp = stack.pop();
                System.out.println("run " + temp.getType() + ", id: " + temp.getStepId() + " end--");
            }
            if(stack.size() > 0) {
                StepNode node = stack.pop();
                boolean ended = true;
                if(node.getChildren() != null && node.getChildren().length > 0 && !linkedList.isEmpty()) {
                    StepNode first = linkedList.get(0);
                    for (int i = 0; i < node.getChildren().length; i++) {
                        if(first.getStepId().equals(node.getChildren()[i].getStepId())) {
                            ended = false;
                            break;
                        }
                    }
                }
                if(!ended) {
                    stack.add(node);
                }else {
                    if ((node.getType().equals("while") || node.getType().equals("loop")) && !node.isBreakLoop()) {
                        linkedList.add(0, node);
                    }else{
                        System.out.println("run " + node.getType() + ", id: " + node.getStepId() + " end--");
                    }
                }
            }
        }
    }

    public static void run(StepNode node) {
        if(!running || !node.enabled) {
            return ;
        }
        node.run();
        // 如果是循环逻辑
        if(!node.getType().equals("while") && !node.getType().equals("loop")) {
            node.setBreakLoop(true);
            if(node.children != null && node.children.length > 0) {
                for (int i = 0; i < node.children.length; i++) {
                    run(node.children[i]);
                }
            }
        }else {
            while (!node.isBreakLoop()) {
                if (node.children != null && node.children.length > 0) {
                    for (int i = 0; i < node.children.length; i++) {
                        run(node.children[i]);
                    }
                }
                node.run();
            }
        }

        System.out.println(node.getType() + " end return");
    }

    @Data
    public static final class StepNode {
        private Integer stepId;
        private String type;
        private Long startTimestamp;
        private Long endTimestamp;
        private boolean enabled = true;
        private boolean breakLoop = false;

        public StepNode(String type) {
            this.type = type;
        }

        public void run() {
            setStartTimestamp(System.currentTimeMillis());
            System.out.println("run " + type + ", id: " + stepId);
            if(type.equals("while")) {
                int i = RandomUtils.nextInt(1, 20);
                System.out.println("random int result is: " + i);
                if (i < 6) {
                    setBreakLoop(true);
                }
            }
            setEndTimestamp(System.currentTimeMillis());
        }

        public StepNode[] children;
    }
}
