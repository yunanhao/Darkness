package org.tile;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Properties;

public class ObjectGroup {
    String name, draworder;
    int x, y;
    int width, height;
    int offsetx, offsety;
    int color;
    boolean visible;
    float opacity;
    Properties properties;
    TileObject[] object;

    public ObjectGroup(Node node) {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        name = TileUtils.getAttributeValue(nodeMap, "name");
        draworder = TileUtils.getAttributeValue(nodeMap, "draworder");
        x = TileUtils.getIntegerAttribute(nodeMap, "x", 0);
        y = TileUtils.getIntegerAttribute(nodeMap, "y", 0);
        width = TileUtils.getIntegerAttribute(nodeMap, "width", 0);
        height = TileUtils.getIntegerAttribute(nodeMap, "height", 0);
        offsetx = TileUtils.getIntegerAttribute(nodeMap, "offsetx", 0);
        offsety = TileUtils.getIntegerAttribute(nodeMap, "offsety", 0);
        color = TileUtils.getColor(TileUtils.getAttributeValue(nodeMap, "color"));
        opacity = (float) TileUtils.getDoubleAttribute(nodeMap, "opacity", 0);
        visible = !"0".equalsIgnoreCase(TileUtils.getAttributeValue(nodeMap, "color"));
        NodeList nodeList = node.getChildNodes();
        Node item;
        String v;
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            item = nodeList.item(i);
            v = item.getNodeName();
            if ("properties".equalsIgnoreCase(v)) {
                properties = TileUtils.getProperties(item, properties);
            } else if ("object".equalsIgnoreCase(v)) {
                // TODO
            }
        }
        Element element = (Element) node;
        nodeList = element.getElementsByTagName("object");
        len = nodeList.getLength();
        object = new TileObject[len];
        for (int i = 0; i < len; i++) {
            object[i] = new TileObject(nodeList.item(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"");
        builder.append(name);
        builder.append("\",\"draworder\":\"");
        builder.append(draworder);
        builder.append("\",\"x\":\"");
        builder.append(x);
        builder.append("\",\"y\":\"");
        builder.append(y);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"offsetx\":\"");
        builder.append(offsetx);
        builder.append("\",\"offsety\":\"");
        builder.append(offsety);
        builder.append("\",\"color\":\"");
        builder.append(color);
        builder.append("\",\"visible\":\"");
        builder.append(visible);
        builder.append("\",\"opacity\":\"");
        builder.append(opacity);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\",\"object\":");
        builder.append(Arrays.toString(object));
        builder.append("} ");
        return builder.toString();
    }

}
