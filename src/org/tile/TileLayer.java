package org.tile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Properties;

/**
 * 图层分装类
 */
public class TileLayer {
    /*
     * 图层名称
     **/
    private String name;
    private int x, y;
    private int width, height;
    private int offsetx, offsety;
    private float opacity = 1.0F;
    private boolean visible;
    private int[][] data;
    /**
     * 用于编码的数据层。目前它可以使用“base64”和“CSV”
     */
    private String encoding;
    /**
     * data数据的压缩格式支持"gzip" 和 "zlib"
     */
    private String compression;
    private Properties properties;

    public void initAttributes(Node node) {
        init(node);
    }

    private void init(Node node) {
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
        opacity = (float) TileUtils.getDoubleAttribute(nodeMap, "opacity", 1.0);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node item = nodeList.item(i);
            if ("properties".equalsIgnoreCase(item.getNodeName())) {
                properties = TileUtils.getProperties(item, properties);
            } else if ("data".equalsIgnoreCase(item.getNodeName())) {
                encoding = TileUtils.getAttributeValue(item, "encoding");
                compression = TileUtils.getAttributeValue(item, "compression");
                data = new int[height][width];
                // 填充数组
                String mapTemp[] = node.getTextContent().split(",", 0);
                for (int y = 0, count = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        data[y][x] = Integer.parseInt(mapTemp[count++].trim());
                    }
                }
            }
        }
    }

    public void paint(Graphics2D g2d, Tileset[] tilesets, int mapTileWidth, int mapTileHeight) {
        int dx = 0, dy = 0, sx = 0, sy = 0;
        for (int y = 0, x; y < data.length; y++) {
            for (x = 0; x < data[y].length; x++) {
                int id = data[y][x];
                if (id == 0) {
                    continue;
                }
                Tileset tileset = null;
                for (int i = 0, count = 0; i < tilesets.length; i++) {
                    count += tilesets[i].getTilecount();
                    if (id < count) {
                        tileset = tilesets[i];
                        id += tileset.getTilecount();
                        id -= count;
                        id--;
                        dx = x * mapTileWidth;
                        dy = y * mapTileHeight;
                        sx = id % tileset.getColumns() * tileset.getTilewidth();
                        sy = id / tileset.getColumns() * tileset.getTileheight();
                        g2d.drawImage(tileset.getTilesetimg(),
                                dx, dy, dx + mapTileWidth, dy + mapTileHeight,
                                sx, sy, sx + tileset.getTilewidth(), sy + tileset.getTileheight(),
                                null);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"");
        builder.append(name);
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
        builder.append("\",\"opacity\":\"");
        builder.append(opacity);
        builder.append("\",\"visible\":\"");
        builder.append(visible);
        builder.append("\",\"data\":\"");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                builder.append(data[i][j]);
                builder.append(",");
            }
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append("\",\"encoding\":\"");
        builder.append(encoding);
        builder.append("\",\"compression\":\"");
        builder.append(compression);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\"} ");
        return builder.toString();
    }

}
