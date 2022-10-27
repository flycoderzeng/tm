package com.tm.worker;

import com.tm.worker.utils.XpathUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.List;

public class XmlPathTest {
    public static void main(String[] args) throws DocumentException {
        String xml = "<dependency id=\"11\"><groupId>com.yahoo.elide</groupId><artifactId>elide-core</artifactId><version>6.0.3</version><version>6.0.4</version></dependency>";
        String s = XpathUtils.formatXml(xml);
        System.out.println(s);
        String xpath = "//dependency/groupId/text()";
        Document doc = XpathUtils.parseXmlString(s);
        List<Node> list = XpathUtils.selectNodeList(doc, xpath);
        for (Node node : list) {
            System.out.println(node.getStringValue());
        }

        xpath = "//dependency/@id";
        list = XpathUtils.selectNodeList(doc, xpath);
        for (Node node : list) {
            System.out.println(node.getStringValue());
        }

        xpath = "//dependency/version";
        list = XpathUtils.selectNodeList(doc, xpath);
        for (Node node : list) {
            System.out.println("+++: " + node.getStringValue());
        }
    }
}
