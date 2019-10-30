package org.tile;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TileUtils {
    // 1.创建DocumentBuilder工厂
    private static DocumentBuilderFactory documentBuilderFactory;
    // 2.创建DocumentBuilder对象
    private static DocumentBuilder documentBuilder;

    static {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setExpandEntityReferences(false);
        if (documentBuilder == null) {
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                documentBuilderFactory.setValidating(false);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAttributeValue(NamedNodeMap nodeMap, String key) {
        Node node = nodeMap.getNamedItem(key);
        if (node != null) return node.getNodeValue();
        return null;
    }

    public static String getAttributeValue(Node node, String attribname) {
        final NamedNodeMap attributes = node.getAttributes();
        String value = null;
        if (attributes != null) {
            Node attribute = attributes.getNamedItem(attribname);
            if (attribute != null) {
                value = attribute.getNodeValue();
            }
        }
        return value;
    }

    public static int getColor(String string) {
        if (string == null || string.trim().length() == 0 || "null".equalsIgnoreCase(string)) return 0xFF808080;
        if (string.contains("#")) {
            string = string.substring(string.indexOf('#') + 1);
        }
        return Integer.parseInt(string, 16);
    }

    public static double getDoubleAttribute(NamedNodeMap nodeMap, String key, double defaultValue) {
        if (nodeMap == null) return defaultValue;
        Node node = nodeMap.getNamedItem(key);
        if (node != null) {
            key = node.getNodeValue();
            if (key != null && key.trim().length() != 0) return Double.parseDouble(key.trim());
        }
        return defaultValue;
    }

    public static double getDoubleAttribute(Node node, String attribname, double def) {
        final String attr = getAttributeValue(node, attribname);
        if (attr != null)
            return Double.parseDouble(attr);
        else return def;
    }

    public static int getIntegerAttribute(NamedNodeMap nodeMap, String key, int defaultValue) {
        if (nodeMap == null) return defaultValue;
        Node node = nodeMap.getNamedItem(key);
        if (node != null) {
            key = node.getNodeValue();
            if (key != null && key.trim().length() != 0) return Integer.parseInt(key.trim());
        }
        return defaultValue;
    }

    public static int getIntegerAttribute(Node node, String attribname, int def) {
        final String attr = getAttributeValue(node, attribname);
        if (attr != null)
            return Integer.parseInt(attr);
        else return def;
    }

    public static int getOrientation(String string) {
        switch (string) {
            case "orthogonal":
                return TileMap.ORIENTATION_ORTHOGONAL;
            case "isometric":
                return TileMap.ORIENTATION_ISOMETRIC;
            case "staggered":
                return TileMap.ORIENTATION_STAGGERED;
            case "hexagonal":
                return TileMap.ORIENTATION_HEXAGONAL;
            case "shifted":
                return TileMap.ORIENTATION_SHIFTED;
            default:
                return 0;
        }
    }

    public static Properties getProperties(Node node, Properties properties) {
        if (node == null || !node.hasChildNodes()) return properties;
        NodeList list = node.getChildNodes();
        Node item;
        NamedNodeMap map;
        if (properties == null) {
            properties = new Properties();
        }
        for (int i = 0, len = list.getLength(); i < len; i++) {
            item = list.item(i);
            if ("property".equalsIgnoreCase(item.getNodeName())) {
                map = item.getAttributes();
                properties.setProperty(map.getNamedItem("name").getNodeValue(),
                        map.getNamedItem("value").getNodeValue());
            }
        }
        return properties;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0 || "null".equalsIgnoreCase(s);
    }

    /**
     * 加载地图文件
     *
     * @throws IOException
     * @throws SAXException
     */
    public static TileMap loadTMXFile(InputStream mapFlie) throws SAXException, IOException {
        // 3.DocumentBuilder对象的parse方法得到Document对象
        Document document = documentBuilder.parse(mapFlie);
        // 4.获取根元素
        Element root = document.getDocumentElement();
        TileMap tileMap = new TileMap(root);
        return tileMap;
    }
}
