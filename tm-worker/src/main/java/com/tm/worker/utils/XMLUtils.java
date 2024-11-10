package com.tm.worker.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Slf4j
public final class XMLUtils {
    private XMLUtils() {}

    public static Document parseXmlString(String xmlContent) {
        SAXReader reader = new SAXReader();
        // 禁用外部实体的方法
        try {
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (SAXException e) {
            log.error("解析xml字符串错误，", e);
            return null;
        }
        InputSource source = new InputSource(new StringReader(xmlContent));
        Document document;
        try {
            document = reader.read(source);
        } catch (DocumentException e) {
            log.error("解析xml字符串错误，", e);
            return null;
        }

        return document;
    }


    /**
     * 根据xpath获取内容
     * 获取groupId标签下的文本：//dependency/groupId/text()
     * 获取dependency标签id属性值：//dependency/@id
     * @param document
     * @param xpath
     * @return
     */
    public static List<Node> selectNodeList(Document document, String xpath) {
        List<Node> list = document.selectNodes(xpath);
        return list;
    }

    public static String formatXml(String xml){
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer serializer= factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new StringReader(xml)));
            StringWriter stringWriter = new StringWriter();
            StreamResult res = new StreamResult(stringWriter);
            serializer.transform(xmlSource, res);
            return stringWriter.toString();
        } catch (Exception e) {
            return xml;
        }
    }
}
