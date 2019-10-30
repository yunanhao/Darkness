package org.tile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Properties;

public class TileObject {
    /**
     * id是对象的唯一标识。每个对象被放置在地图上获取一个唯一的ID，
     * 即使对象被删除，也不会有对象得到相同的ID
     */
    private int id;
    private int gid;
    private int x, y;
    private int width, height;
    private int[] points;
    private String name;
    private String type;
    private String shape;
    private Properties properties;
    /**
     * 按顺时针方向旋转的对象（默认为0）. (since 0.10)
     */
    private float rotation;
    private boolean visible;

    public TileObject(Node node) {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        gid = TileUtils.getIntegerAttribute(nodeMap, "gid", 0);
        id = TileUtils.getIntegerAttribute(nodeMap, "id", 0);
        x = TileUtils.getIntegerAttribute(nodeMap, "x", 0);
        y = TileUtils.getIntegerAttribute(nodeMap, "y", 0);
        width = TileUtils.getIntegerAttribute(nodeMap, "width", 0);
        height = TileUtils.getIntegerAttribute(nodeMap, "height", 0);
        rotation = (float) TileUtils.getDoubleAttribute(nodeMap, "rotation", 0);
        name = TileUtils.getAttributeValue(nodeMap, "name");
        type = TileUtils.getAttributeValue(nodeMap, "type");
        shape = "rect";
        visible = !"0".equalsIgnoreCase(TileUtils.getAttributeValue(nodeMap, "color"));
        NodeList nodeList = node.getChildNodes();
        Node item;
        String v;
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            item = nodeList.item(i);
            v = item.getNodeName();
            if ("properties".equalsIgnoreCase(v)) {
                properties = TileUtils.getProperties(item, properties);
            } else if ("ellipse".equalsIgnoreCase(v)) {
                shape = "ellipse";
            } else if ("polyline".equalsIgnoreCase(v)) {
                initPoints(item, "polyline");
            } else if ("polygon".equalsIgnoreCase(v)) {
                initPoints(item, "polygon");
            }
        }
    }

    public void initPoints(Node item, String type) {
        shape = type;
        NamedNodeMap map = item.getAttributes();
        String[] s = map.getNamedItem("points").getNodeValue().trim().split("[, ]");
        points = new int[s.length];
        for (int j = 0; j < points.length; j++) {
            if ((j & 1) == 0) {
                points[j] = Integer.parseInt(s[j].trim()) + x;
            } else {
                points[j] = Integer.parseInt(s[j].trim()) + y;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":\"");
        builder.append(id);
        builder.append("\",\"gid\":\"");
        builder.append(gid);
        builder.append("\",\"x\":\"");
        builder.append(x);
        builder.append("\",\"y\":\"");
        builder.append(y);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"points\":");
        builder.append(Arrays.toString(points));
        builder.append(",\"name\":\"");
        builder.append(name);
        builder.append("\",\"type\":\"");
        builder.append(type);
        builder.append("\",\"shape\":\"");
        builder.append(shape);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\",\"rotation\":\"");
        builder.append(rotation);
        builder.append("\",\"visible\":\"");
        builder.append(visible);
        builder.append("\"} ");
        return builder.toString();
    }

}
