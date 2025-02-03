package com.tm.web.utils;

import com.tm.web.jacoco.JacocoCoverageResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoverageUtils {
    public static JacocoCoverageResult getCodeCoverageResult(String codeCoverageResultXmlPath, List<String> classURIList) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(codeCoverageResultXmlPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setValidating(false);
        dbFactory.setNamespaceAware(true);
        dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
        dbFactory.setFeature("http://xml.org/sax/features/validation", false);
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        JacocoCoverageResult jacocoCoverageResult = parseFileCoverage(doc, classURIList);
        JacocoCoverageResult.Counter counter1 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.INSTRUCTION, 0, 0);
        JacocoCoverageResult.Counter counter2 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.BRANCH, 0, 0);
        JacocoCoverageResult.Counter counter3 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.LINE, 0, 0);
        JacocoCoverageResult.Counter counter4 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.COMPLEXITY, 0, 0);
        JacocoCoverageResult.Counter counter5 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.METHOD, 0, 0);
        JacocoCoverageResult.Counter counter6 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.CLASS, 0, 0);
        for (JacocoCoverageResult.PackageCoverageResult packageCoverageResult : jacocoCoverageResult.getPackageCoverageResults()) {
            for (JacocoCoverageResult.Counter counter : packageCoverageResult.getCounters()) {
                if(counter.getType().equals(JacocoCoverageResult.CounterType.INSTRUCTION)) {
                    counter1.setMissed(counter1.getMissed() + counter.getMissed());
                    counter1.setCovered(counter1.getCovered() + counter.getCovered());
                } else if(counter.getType().equals(JacocoCoverageResult.CounterType.BRANCH)) {
                    counter2.setMissed(counter2.getMissed() + counter.getMissed());
                    counter2.setCovered(counter2.getCovered() + counter.getCovered());
                } else if(counter.getType().equals(JacocoCoverageResult.CounterType.LINE)) {
                    counter3.setMissed(counter3.getMissed() + counter.getMissed());
                    counter3.setCovered(counter3.getCovered() + counter.getCovered());
                } else if(counter.getType().equals(JacocoCoverageResult.CounterType.COMPLEXITY)) {
                    counter4.setMissed(counter4.getMissed() + counter.getMissed());
                    counter4.setCovered(counter4.getCovered() + counter.getCovered());
                } else if(counter.getType().equals(JacocoCoverageResult.CounterType.METHOD)) {
                    counter5.setMissed(counter5.getMissed() + counter.getMissed());
                    counter5.setCovered(counter5.getCovered() + counter.getCovered());
                } else if(counter.getType().equals(JacocoCoverageResult.CounterType.CLASS)) {
                    counter6.setMissed(counter6.getMissed() + counter.getMissed());
                    counter6.setCovered(counter6.getCovered() + counter.getCovered());
                }
            }
        }
        jacocoCoverageResult.getCounters().add(counter1);
        jacocoCoverageResult.getCounters().add(counter2);
        jacocoCoverageResult.getCounters().add(counter3);
        jacocoCoverageResult.getCounters().add(counter4);
        jacocoCoverageResult.getCounters().add(counter5);
        jacocoCoverageResult.getCounters().add(counter6);

        return jacocoCoverageResult;
    }

    public static JacocoCoverageResult parseFileCoverage(Document doc, List<String> classURIList) {
        JacocoCoverageResult jacocoCoverageResult = new JacocoCoverageResult(doc.getDocumentElement().getAttribute("name"));
        jacocoCoverageResult.setCounters(new ArrayList<>());
        jacocoCoverageResult.setPackageCoverageResults(new ArrayList<>());

        NodeList packageList = doc.getElementsByTagName("package");
        List<String> packageURIList = new ArrayList<>();
        for (String classURI : classURIList) {
            packageURIList.add(classURI.substring(0, classURI.lastIndexOf("/")));
        }

        for (int i = 0; i < packageList.getLength(); i++) {
            Node packageNode = packageList.item(i);
            if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
                Element packageElement = (Element) packageNode;
                String packageName = packageElement.getAttribute("name");
                if(!classURIList.isEmpty() && !packageURIList.contains(packageName)) {
                    continue;
                }
                JacocoCoverageResult.PackageCoverageResult packageCoverageResult = new JacocoCoverageResult.PackageCoverageResult(packageName);
                packageCoverageResult.setCounters(new ArrayList<>());
                packageCoverageResult.setClassCoverageResults(new ArrayList<>());

                jacocoCoverageResult.getPackageCoverageResults().add(packageCoverageResult);

                NodeList classNodeList = packageElement.getElementsByTagName("class");
                for (int j = 0; j < classNodeList.getLength(); j++) {
                    Node classNode = classNodeList.item(j);
                    if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element classElement = (Element) classNode;
                        String className = classElement.getAttribute("name");
                        if(!classURIList.isEmpty() && !classURIList.contains(className+".java")) {
                            continue;
                        }
                        JacocoCoverageResult.ClassCoverageResult classCoverageResult = new JacocoCoverageResult.ClassCoverageResult();
                        classCoverageResult.setCounters(new ArrayList<>());
                        classCoverageResult.setMethodCoverageResults(new ArrayList<>());
                        classCoverageResult.setLineCoverageResults(new ArrayList<>());
                        packageCoverageResult.getClassCoverageResults().add(classCoverageResult);

                        classCoverageResult.setName(className);
                        String sourceFileName = classElement.getAttribute("sourcefilename");
                        classCoverageResult.setSourceFileName(sourceFileName);

                        fillLineCoverageResult(packageElement, sourceFileName, classCoverageResult);

                        NodeList childNodes = classElement.getChildNodes();
                        for (int k = 0; k < childNodes.getLength(); k++) {
                            Node childNode = childNodes.item(k);
                            if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("counter")) {
                                fillCounters((Element) childNode, classCoverageResult.getCounters(), classCoverageResult);
                            }else if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("method")) {
                                Element methodElement = (Element) childNode;
                                JacocoCoverageResult.MethodCoverageResult methodCoverageResult = getMethodCoverageResult(methodElement);
                                NodeList methodCounterList = methodElement.getElementsByTagName("counter");
                                for (int m = 0; m < methodCounterList.getLength(); m++) {
                                    Node counterNode = methodCounterList.item(m);
                                    if (counterNode.getNodeType() == Node.ELEMENT_NODE) {
                                        fillCounters((Element) counterNode, methodCoverageResult.getCounters(), classCoverageResult);
                                    }
                                }
                                classCoverageResult.getMethodCoverageResults().add(methodCoverageResult);
                            }
                        }
                    }
                }
                JacocoCoverageResult.Counter counter1 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.INSTRUCTION, 0, 0);
                JacocoCoverageResult.Counter counter2 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.BRANCH, 0, 0);
                JacocoCoverageResult.Counter counter3 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.LINE, 0, 0);
                JacocoCoverageResult.Counter counter4 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.COMPLEXITY, 0, 0);
                JacocoCoverageResult.Counter counter5 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.METHOD, 0, 0);
                JacocoCoverageResult.Counter counter6 = new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.CLASS, 0, 0);

                for (JacocoCoverageResult.ClassCoverageResult classCoverageResult : packageCoverageResult.getClassCoverageResults()) {
                    for (JacocoCoverageResult.Counter counter : classCoverageResult.getCounters()) {
                        if(counter.getType() == JacocoCoverageResult.CounterType.INSTRUCTION) {
                            counter1.setMissed(counter1.getMissed() + counter.getMissed());
                            counter1.setCovered(counter1.getCovered() + counter.getCovered());
                        }else if(counter.getType() == JacocoCoverageResult.CounterType.BRANCH) {
                            counter2.setMissed(counter2.getMissed() + counter.getMissed());
                            counter2.setCovered(counter2.getCovered() + counter.getCovered());
                        }else if(counter.getType() == JacocoCoverageResult.CounterType.LINE) {
                            counter3.setMissed(counter3.getMissed() + counter.getMissed());
                            counter3.setCovered(counter3.getCovered() + counter.getCovered());
                        }else if(counter.getType() == JacocoCoverageResult.CounterType.COMPLEXITY) {
                            counter4.setMissed(counter4.getMissed() + counter.getMissed());
                            counter4.setCovered(counter4.getCovered() + counter.getCovered());
                        }else if(counter.getType() == JacocoCoverageResult.CounterType.METHOD) {
                            counter5.setMissed(counter5.getMissed() + counter.getMissed());
                            counter5.setCovered(counter5.getCovered() + counter.getCovered());
                        }else if(counter.getType() == JacocoCoverageResult.CounterType.CLASS) {
                            counter6.setMissed(counter6.getMissed() + counter.getMissed());
                            counter6.setCovered(counter6.getCovered() + counter.getCovered());
                        }
                    }
                }
                packageCoverageResult.getCounters().add(counter1);
                packageCoverageResult.getCounters().add(counter2);
                packageCoverageResult.getCounters().add(counter3);
                packageCoverageResult.getCounters().add(counter4);
                packageCoverageResult.getCounters().add(counter5);
                packageCoverageResult.getCounters().add(counter6);
            }
        }

        return jacocoCoverageResult;
    }

    public static void fillCounters(Element childNode, List<JacocoCoverageResult.Counter> counters, JacocoCoverageResult.ClassCoverageResult classCoverageResult) {
        String type = childNode.getAttribute("type");
        int missed = Integer.parseInt(childNode.getAttribute("missed"));
        int covered = Integer.parseInt(childNode.getAttribute("covered"));
        counters.add(new JacocoCoverageResult.Counter(JacocoCoverageResult.CounterType.get(type), missed, covered));
    }

    public static void fillLineCoverageResult(Element packageElement, String sourceFileName,
                                              JacocoCoverageResult.ClassCoverageResult classCoverageResult) {
        NodeList sourceFileNodeList = packageElement.getElementsByTagName("sourcefile");
        for (int m = 0; m < sourceFileNodeList.getLength(); m++) {
            Node sourceFileNode = sourceFileNodeList.item(m);
            if (sourceFileNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sourceFileElement = (Element) sourceFileNode;
                String fileName = sourceFileElement.getAttribute("name");
                if(!sourceFileName.equals(fileName)) {
                    continue;
                }
                NodeList lineList = sourceFileElement.getElementsByTagName("line");
                for (int n = 0; n < lineList.getLength(); n++) {
                    Node lineNode = lineList.item(n);
                    if (lineNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element lineElement = (Element) lineNode;
                        int nr = Integer.parseInt(lineElement.getAttribute("nr"));
                        int mi = Integer.parseInt(lineElement.getAttribute("mi"));
                        int ci = Integer.parseInt(lineElement.getAttribute("ci"));
                        int mb = Integer.parseInt(lineElement.getAttribute("mb"));
                        int cb = Integer.parseInt(lineElement.getAttribute("cb"));
                        classCoverageResult.getLineCoverageResults().add(new JacocoCoverageResult.LineCoverageResult(nr, mi, ci, mb, cb));
                    }
                }
            }
        }
    }

    public static JacocoCoverageResult.MethodCoverageResult getMethodCoverageResult(Element methodElement) {
        String name = methodElement.getAttribute("name");
        String desc = methodElement.getAttribute("desc");
        String line = methodElement.getAttribute("line");

        JacocoCoverageResult.MethodCoverageResult methodCoverageResult = new JacocoCoverageResult.MethodCoverageResult();
        methodCoverageResult.setName(name);
        methodCoverageResult.setDesc(desc);
        methodCoverageResult.setLine(line);
        methodCoverageResult.setCounters(new ArrayList<>());
        return methodCoverageResult;
    }

    public static String extractDirectoryName(String gitUrl) {
        // 统一处理URL格式
        String path = gitUrl.replace(".git", ""); // 去掉.git后缀
        path = path.replace("https://", "") // 去掉https://
                .replace("git@", "") // 去掉git@
                .replace(":", "/"); // 将SSH格式中的冒号替换为斜杠

        // 提取最后一部分
        String[] parts = path.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1]; // 返回最后一部分
        } else {
            throw new IllegalArgumentException("Invalid Git URL format: " + gitUrl);
        }
    }
}
