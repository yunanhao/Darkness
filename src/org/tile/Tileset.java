package org.tile;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/*
 * 图块对象 拼合地图时的素材
 * **/
public class Tileset {
    private String name;
    private int firstgid;
    private int tilecount;
    private int columns;
    private int tilewidth, tileheight;
    private int tileoffsetx, tileoffsety;
    private int margin, spacing;
    //图块的图像素材
    private String source;
    /**
     * 用于嵌入图像，在与数据子元素相结合的。
     * 有效值是文件扩展名如PNG，GIF，JPG，BMP，等（自0.9）
     */
    private String format;
    private int id, x, y, width, height, trans;

    private BufferedImage tilesetimg;
    private Properties properties;
    private TileTerrain terraintypes[];

    public Tileset(Node node) {
        try {
            init(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Node node) throws IOException {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        name = TileUtils.getAttributeValue(nodeMap, "name");
        firstgid = TileUtils.getIntegerAttribute(nodeMap, "firstgid", 0);
        tilecount = TileUtils.getIntegerAttribute(nodeMap, "tilecount", 0);
        margin = TileUtils.getIntegerAttribute(nodeMap, "margin", 0);
        spacing = TileUtils.getIntegerAttribute(nodeMap, "spacing", 0);
        tilewidth = TileUtils.getIntegerAttribute(nodeMap, "tilewidth", 0);
        tileheight = TileUtils.getIntegerAttribute(nodeMap, "tileheight", 0);
        NodeList nodeList = node.getChildNodes();
        Node item;
        String name;
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            item = nodeList.item(i);
            name = item.getNodeName();
            if ("tileoffset".equalsIgnoreCase(name)) {
                tileoffsetx = TileUtils.getIntegerAttribute(item, "x", 0);
                tileoffsety = TileUtils.getIntegerAttribute(item, "y", 0);
            } else if ("properties".equalsIgnoreCase(name)) {
                properties = TileUtils.getProperties(item, properties);
            } else if ("terraintypes".equalsIgnoreCase(name)) {
                initTerrain(item);
            } else if ("image".equalsIgnoreCase(name)) {
                NamedNodeMap imgMap = item.getAttributes();
                if (imgMap == null) return;
                source = TileUtils.getAttributeValue(imgMap, "source");
                format = TileUtils.getAttributeValue(imgMap, "format");
                id = TileUtils.getIntegerAttribute(imgMap, "id", 0);
                x = TileUtils.getIntegerAttribute(imgMap, "x", 0);
                y = TileUtils.getIntegerAttribute(imgMap, "y", 0);
                width = TileUtils.getIntegerAttribute(imgMap, "width", 0);
                height = TileUtils.getIntegerAttribute(imgMap, "height", 0);
                trans = TileUtils.getColor(TileUtils.getAttributeValue(imgMap, "trans"));
                System.out.println("I:\\Pictures\\[素材天下]\\RMVA素材/Tile/" + source);
                tilesetimg = ImageIO.read(new File("I:\\Pictures\\[素材天下]\\RMVA素材/Tile/" + source));
            } else if ("org/tile".equalsIgnoreCase(name)) {
                if (item.hasChildNodes()) {
                    Tile.getTile(TileUtils.getIntegerAttribute(item, "id", -firstgid) + firstgid).init(item);
                }
            }
        }
        columns = TileUtils.getIntegerAttribute(nodeMap, "columns", width / tilewidth);
    }

    public void initTerrain(Node item) {
        NodeList list = item.getChildNodes();
        int length = 0;
        for (int j = 0; j < list.getLength(); j++) {
            if ("terrain".equalsIgnoreCase(list.item(j).getNodeName())) {
                length++;
            }
        }
        terraintypes = new TileTerrain[length];
        int count = 0;
        for (int j = 0; j < list.getLength(); j++) {
            if ("terrain".equalsIgnoreCase(list.item(j).getNodeName())) {
                terraintypes[count++] = new TileTerrain(list.item(j));
                if (count >= length) {
                    break;
                }
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
     * @return the firstgid
     */
    public int getFirstgid() {
        return firstgid;
    }

    /**
     * @param firstgid the firstgid to set
     */
    public void setFirstgid(int firstgid) {
        this.firstgid = firstgid;
    }

    /**
     * @return the tilecount
     */
    public int getTilecount() {
        return tilecount;
    }

    /**
     * @param tilecount the tilecount to set
     */
    public void setTilecount(int tilecount) {
        this.tilecount = tilecount;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * @return the tilewidth
     */
    public int getTilewidth() {
        return tilewidth;
    }

    /**
     * @param tilewidth the tilewidth to set
     */
    public void setTilewidth(int tilewidth) {
        this.tilewidth = tilewidth;
    }

    /**
     * @return the tileheight
     */
    public int getTileheight() {
        return tileheight;
    }

    /**
     * @param tileheight the tileheight to set
     */
    public void setTileheight(int tileheight) {
        this.tileheight = tileheight;
    }

    /**
     * @return the tileoffsetx
     */
    public int getTileoffsetx() {
        return tileoffsetx;
    }

    /**
     * @param tileoffsetx the tileoffsetx to set
     */
    public void setTileoffsetx(int tileoffsetx) {
        this.tileoffsetx = tileoffsetx;
    }

    /**
     * @return the tileoffsety
     */
    public int getTileoffsety() {
        return tileoffsety;
    }

    /**
     * @param tileoffsety the tileoffsety to set
     */
    public void setTileoffsety(int tileoffsety) {
        this.tileoffsety = tileoffsety;
    }

    /**
     * @return the margin
     */
    public int getMargin() {
        return margin;
    }

    /**
     * @param margin the margin to set
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }

    /**
     * @return the spacing
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * @param spacing the spacing to set
     */
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the trans
     */
    public int getTrans() {
        return trans;
    }

    /**
     * @param trans the trans to set
     */
    public void setTrans(int trans) {
        this.trans = trans;
    }

    /**
     * @return the tilesetimg
     */
    public BufferedImage getTilesetimg() {
        return tilesetimg;
    }

    /**
     * @param tilesetimg the tilesetimg to set
     */
    public void setTilesetimg(BufferedImage tilesetimg) {
        this.tilesetimg = tilesetimg;
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

    /**
     * @return the terraintypes
     */
    public TileTerrain[] getTerraintypes() {
        return terraintypes;
    }

    /**
     * @param terraintypes the terraintypes to set
     */
    public void setTerraintypes(TileTerrain[] terraintypes) {
        this.terraintypes = terraintypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + columns;
        result = prime * result + firstgid;
        result = prime * result + (format == null ? 0 : format.hashCode());
        result = prime * result + height;
        result = prime * result + id;
        result = prime * result + margin;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (properties == null ? 0 : properties.hashCode());
        result = prime * result + (source == null ? 0 : source.hashCode());
        result = prime * result + spacing;
        result = prime * result + Arrays.hashCode(terraintypes);
        result = prime * result + tilecount;
        result = prime * result + tileheight;
        result = prime * result + tileoffsetx;
        result = prime * result + tileoffsety;
        result = prime * result + (tilesetimg == null ? 0 : tilesetimg.hashCode());
        result = prime * result + tilewidth;
        result = prime * result + trans;
        result = prime * result + width;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Tileset)) return false;
        Tileset other = (Tileset) obj;
        if (columns != other.columns) return false;
        if (firstgid != other.firstgid) return false;
        if (format == null) {
            if (other.format != null) return false;
        } else if (!format.equals(other.format)) return false;
        if (height != other.height) return false;
        if (id != other.id) return false;
        if (margin != other.margin) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (properties == null) {
            if (other.properties != null) return false;
        } else if (!properties.equals(other.properties)) return false;
        if (source == null) {
            if (other.source != null) return false;
        } else if (!source.equals(other.source)) return false;
        if (spacing != other.spacing) return false;
        if (!Arrays.equals(terraintypes, other.terraintypes)) return false;
        if (tilecount != other.tilecount) return false;
        if (tileheight != other.tileheight) return false;
        if (tileoffsetx != other.tileoffsetx) return false;
        if (tileoffsety != other.tileoffsety) return false;
        if (tilesetimg == null) {
            if (other.tilesetimg != null) return false;
        } else if (!tilesetimg.equals(other.tilesetimg)) return false;
        if (tilewidth != other.tilewidth) return false;
        if (trans != other.trans) return false;
        if (width != other.width) return false;
        if (x != other.x) return false;
        return y == other.y;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"");
        builder.append(name);
        builder.append("\",\"firstgid\":\"");
        builder.append(firstgid);
        builder.append("\",\"tilecount\":\"");
        builder.append(tilecount);
        builder.append("\",\"columns\":\"");
        builder.append(columns);
        builder.append("\",\"tilewidth\":\"");
        builder.append(tilewidth);
        builder.append("\",\"tileheight\":\"");
        builder.append(tileheight);
        builder.append("\",\"tileoffsetx\":\"");
        builder.append(tileoffsetx);
        builder.append("\",\"tileoffsety\":\"");
        builder.append(tileoffsety);
        builder.append("\",\"margin\":\"");
        builder.append(margin);
        builder.append("\",\"spacing\":\"");
        builder.append(spacing);
        builder.append("\",\"source\":\"");
        builder.append(source);
        builder.append("\",\"format\":\"");
        builder.append(format);
        builder.append("\",\"id\":\"");
        builder.append(id);
        builder.append("\",\"x\":\"");
        builder.append(x);
        builder.append("\",\"y\":\"");
        builder.append(y);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"trans\":\"");
        builder.append(trans);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\",\"terraintypes\":");
        builder.append(Arrays.toString(terraintypes));
        builder.append("} ");
        return builder.toString();
    }

}
