package org.tile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Properties;

public class ImageLayer {
    int x, y;
    int color;
    String name;
    float opacity;
    boolean visible;
    int width, height;
    int offsetx, offsety;
    Properties properties;

    public ImageLayer(Node node) {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        name = TileUtils.getAttributeValue(nodeMap, "name");
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
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            item = nodeList.item(i);
            if ("properties".equalsIgnoreCase(item.getNodeName())) {
                properties = TileUtils.getProperties(item, properties);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"x\":\"");
        builder.append(x);
        builder.append("\",\"y\":\"");
        builder.append(y);
        builder.append("\",\"color\":\"");
        builder.append(color);
        builder.append("\",\"name\":\"");
        builder.append(name);
        builder.append("\",\"opacity\":\"");
        builder.append(opacity);
        builder.append("\",\"visible\":\"");
        builder.append(visible);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"offsetx\":\"");
        builder.append(offsetx);
        builder.append("\",\"offsety\":\"");
        builder.append(offsety);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\"} ");
        return builder.toString();
    }


}
