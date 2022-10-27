package com.tm.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FlowTest {

    public static void main(String[] args) throws Exception {
        String flow = "{\"linkDataArray\": [\n"
            + "    { \"from\": -1, \"to\": 0 },\n"
            + "    { \"from\": -2, \"to\": 0 },\n"
            + "    { \"from\": -2, \"to\": 3 },\n"
            + "    { \"from\": -3, \"to\": 3 },\n"
            + "    { \"from\":  0, \"to\": 1 },\n"
            + "    { \"from\":  1, \"to\": 2 },\n"
            + "    { \"from\":  1, \"to\": 3 },\n"
            + "    { \"from\":  0, \"to\": 5 },\n"
            + "    { \"from\":  5, \"to\": 3 },\n"
            + "    { \"from\":  3, \"to\": 2 },\n"
            + "\n"
            + "\n"
            + "    { \"from\":  3, \"to\": 6 },\n"
            + "\n"
            + "    { \"from\":  2, \"to\": 100 },\n"
            + "    { \"from\":  6, \"to\": 101 },\n"
            + "\n"
            + "    { \"from\":  0, \"to\": 200 },\n"
            + "    { \"from\":  3, \"to\": 201 },\n"
            + "    { \"from\":  2, \"to\": 202 }\n"
            + "  ]\n"
            + "}";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        FlowDefine define = gson.fromJson(flow, FlowDefine.class);
        List<LinkDefine> linkDefines = define.getLinkDataArray();
        System.out.println(linkDefines);

        Map<String, LinkDefine> linkDefineMap = new HashMap<>();
        for (LinkDefine linkDefine : linkDefines) {
            linkDefineMap.put(linkDefine.getFrom()+"-"+linkDefine.getTo(), linkDefine);
        }
        if(linkDefineMap.size() != linkDefines.size()) {
            throw new Exception("存在重复的边");
        }

        Map<Integer, String> map = new HashMap<>();
        map.put(-1, "A");
        map.put(-2, "B");
        map.put(-3, "G");
        map.put(0, "C");
        map.put(1, "D");
        map.put(2, "I");
        map.put(3, "H");
        map.put(5, "F");
        map.put(6, "K");
        map.put(100, "M");
        map.put(101, "L");
        map.put(200, "E");
        map.put(201, "J");
        map.put(202, "N");



        Map<Integer, NodeDefine> nodeDefineHashMap = new HashMap<>();
        for (LinkDefine linkDefine : linkDefines) {
            if(!nodeDefineHashMap.containsKey(linkDefine.getFrom())) {
                NodeDefine nodeDefine = new NodeDefine();
                nodeDefine.setId(linkDefine.getFrom());
                nodeDefine.setLevel(1);
                nodeDefineHashMap.put(nodeDefine.getId(), nodeDefine);
            }
            if(!nodeDefineHashMap.containsKey(linkDefine.getTo())) {
                NodeDefine nodeDefine = new NodeDefine();
                nodeDefine.setId(linkDefine.getTo());
                nodeDefine.setLevel(1);
                nodeDefineHashMap.put(nodeDefine.getId(), nodeDefine);
            }
        }
        System.out.println(nodeDefineHashMap);

        List<NodeDefine> startNodeList = new ArrayList<>();
        List<NodeDefine> nodeDefineList = new ArrayList<>(nodeDefineHashMap.values());
        for (NodeDefine nodeDefine : nodeDefineList) {
            int to = 0;
            for (LinkDefine linkDefine : linkDefines) {
                if(linkDefine.getTo().equals(nodeDefine.getId())) {
                    to++;
                }
                if(linkDefine.getFrom().equals(nodeDefine.getId())) {
                    nodeDefine.getAdjacencyNodeList().add(nodeDefineHashMap.get(linkDefine.getTo()));
                    nodeDefine.getAdjacencyLinkList().add(linkDefine);
                }
            }
            if(to < 1) {
                startNodeList.add(nodeDefine);
            }
        }
        nodeDefineList.forEach(node -> System.out.println(node.getId() + ": " + node.getAdjacencyNodeList().size()));
        System.out.println("---start nodes----");
        startNodeList.forEach(node -> System.out.println(node.getId()));
        System.out.println("---------------------");


        Stack<NodeDefine> stack = new Stack<>();
        for (NodeDefine startNode : startNodeList) {
            System.out.println(startNode.getId() + " adjacency links is : " + startNode.getAdjacencyLinkList().size());
            startNode.setVisited(true);
            startNode.setLevel(1);
            if(startNode.getAdjacencyLinkList().isEmpty()) {
                System.out.println(startNode.getId() + " is a alone node");
                continue;
            }
            stack.push(startNode);
            NodeDefine currNode;
            for(;;) {
                if(stack.isEmpty()) {
                    break;
                }
                currNode = stack.peek();
                if(currNode.getLevel().equals(1) && currNode.getAdjacencyLinkList().isEmpty()) {
                    stack.pop();
                    break;
                }else if(currNode.getAdjacencyLinkList().isEmpty()) {
                    stack.pop();
                    continue;
                }else if(!currNode.getAdjacencyLinkList().isEmpty()) {
                    int hasVisited = 0;
                    for (LinkDefine linkDefine : currNode.getAdjacencyLinkList()) {
                        NodeDefine toNode = nodeDefineHashMap.get(linkDefine.getTo());
                        if(toNode.getVisited() && (toNode.getLevel()-1) >= currNode.getLevel()) {
                            linkDefine.setVisited(true);
                            hasVisited++;
                            continue;
                        }
                        if(!linkDefine.getVisited() || (linkDefine.getVisited() && toNode.getLevel() <= currNode.getLevel())) {
                            linkDefine.setVisited(true);
                            toNode.setVisited(true);
                            toNode.setLevel(currNode.getLevel()+1);
                            if(stack.search(toNode) > 0) {
                                throw new Exception("存在环");
                            }
                            stack.push(toNode);
                            break;
                        } else {
                            hasVisited++;
                        }
                    }
                    if(hasVisited == currNode.getAdjacencyLinkList().size()) {
                        stack.pop();
                        continue;
                    }
                }
            }
        }
        System.out.println("---------------------");

        int maxLevel = 1;
        for (NodeDefine nodeDefine : nodeDefineList) {
            System.out.println("node id: " + nodeDefine.getId() + ", level: " + nodeDefine.getLevel());
            if(nodeDefine.getLevel() > maxLevel) {
                maxLevel = nodeDefine.getLevel();
            }
        }

        System.out.println("---------------------");

        TaskDefine[] taskDefines = new TaskDefine[maxLevel];
        for (int i = 0; i < taskDefines.length; i++) {
            taskDefines[i] = new TaskDefine();
        }
        for (NodeDefine nodeDefine : nodeDefineList) {
            taskDefines[nodeDefine.getLevel()-1].getNodeList().add(nodeDefine);
        }

        for (int i = 0; i < taskDefines.length; i++) {
            String nodeStr = "";
            for (NodeDefine nodeDefine : taskDefines[i].getNodeList()) {
                nodeStr += " " + map.get(nodeDefine.getId());
            }
            System.out.println("level: " + (i+1) + ", node: " + nodeStr);
        }
    }

    public static class TaskDefine {
        private List<NodeDefine> nodeList = new ArrayList<>();

        public List<NodeDefine> getNodeList() {
            return nodeList;
        }

        public void setNodeList(List<NodeDefine> nodeList) {
            this.nodeList = nodeList;
        }
    }

    public static class FlowDefine {
        private List<LinkDefine> linkDataArray;

        public List<LinkDefine> getLinkDataArray() {
            return linkDataArray;
        }

        public void setLinkDataArray(List<LinkDefine> linkDataArray) {
            this.linkDataArray = linkDataArray;
        }

        @Override
        public String toString() {
            return "FlowDefine{" +
                "linkDataArray=" + linkDataArray +
                '}';
        }
    }

    public static class LinkDefine {
        private Integer from;
        private Integer to;
        private Boolean visited = false;

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        public Boolean getVisited() {
            return visited;
        }

        public void setVisited(Boolean visited) {
            this.visited = visited;
        }

        @Override
        public String toString() {
            return "LinkDefine{" +
                "from=" + from +
                ", to=" + to +
                ", visited=" + visited +
                '}';
        }
    }

    public static class NodeDefine {
        private Integer id;
        private String name;
        private Integer level = 1;
        private Boolean visited = false;
        private List<NodeDefine> adjacencyNodeList = new ArrayList<>();
        private List<LinkDefine> adjacencyLinkList = new ArrayList<>();

        public List<LinkDefine> getAdjacencyLinkList() {
            return adjacencyLinkList;
        }

        public void setAdjacencyLinkList(List<LinkDefine> adjacencyLinkList) {
            this.adjacencyLinkList = adjacencyLinkList;
        }

        public List<NodeDefine> getAdjacencyNodeList() {
            return adjacencyNodeList;
        }

        public void setAdjacencyNodeList(List<NodeDefine> adjacencyNodeList) {
            this.adjacencyNodeList = adjacencyNodeList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Boolean getVisited() {
            return visited;
        }

        public void setVisited(Boolean visited) {
            this.visited = visited;
        }

        @Override
        public boolean equals(Object obj) {
            return this.id.equals(((NodeDefine)obj).getId());
        }

        @Override
        public String toString() {
            return "NodeDefine{" +
                "id=" + id +
                ", level=" + level +
                ", visited=" + visited +
                ", adjacencyNodeList=" + adjacencyNodeList +
                '}';
        }
    }
}
