package org.tile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Properties;

/**
 * 自动地形
 */
public class TileTerrain {
    //地形的名字
    private String name;
    //用于展示的地形图标图块
    private int tile;
    private Properties properties;

    public TileTerrain(Node node) {
        init(node);
    }

    public void init(Node node) {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        name = TileUtils.getAttributeValue(nodeMap, "name");
        tile = TileUtils.getIntegerAttribute(nodeMap, "org/tile", 0);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node item = nodeList.item(i);
            if ("properties".equalsIgnoreCase(item.getNodeName())) {
                properties = TileUtils.getProperties(item, properties);
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tile
     */
    public int getTile() {
        return tile;
    }

    /**
     * @param tile the tile to set
     */
    public void setTile(int tile) {
        this.tile = tile;
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (properties == null ? 0 : properties.hashCode());
        result = prime * result + tile;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TileTerrain)) return false;
        TileTerrain other = (TileTerrain) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (properties == null) {
            if (other.properties != null) return false;
        } else if (!properties.equals(other.properties)) return false;
        return tile == other.tile;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"");
        builder.append(name);
        builder.append("\",\"tile\":\"");
        builder.append(tile);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\"} ");
        return builder.toString();
    }

}
