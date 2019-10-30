package org.tile;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.Arrays;
import java.util.Properties;

public class TileMap {
    public static final int ORIENTATION_ORTHOGONAL = 1;
    public static final int ORIENTATION_ISOMETRIC = 2;
    public static final int ORIENTATION_STAGGERED = 8;
    public static final int ORIENTATION_HEXAGONAL = 4;
    /**
     * Shifted (used for iso and hex).
     */
    public static final int ORIENTATION_SHIFTED = 5;
    /**
     * 地图的背景色
     * (since 0.9, optional, may include alpha value
     * since 0.15 in the form #AARRGGBB)
     */
    private int backgroundcolor;
    private int width, height;
    private int tilewidth, tileheight;
    private Tileset[] tilesets;
    private TileLayer[] layers;
    private ObjectGroup[] objectLayers;
    private ImageLayer[] imageLayers;
    private String filename;
    private Properties properties;
    /**
     * Map orientation. Tiled supports
     * "orthogonal="isometric="staggered" (since 0.9)
     * and "hexagonal" (since 0.11).
     * (orthogonal | isometric | staggered | hexagonal | shifted)
     */
    private int orientation = ORIENTATION_ORTHOGONAL;

    /**
     * The order in which tiles on tile layers are rendered.
     * Valid values are right-down (the default), right-up, left-down
     * and left-up. In all cases, the map is drawn row-by-row.
     * (since 0.10, but only supported for orthogonal maps at the moment)
     */
    private String renderorder;
    /**
     * Stores the next available ID for new objects. This number
     * is stored to prevent reuse of the same ID after objects
     * have been removed. (since 0.11)
     */
    private int nextobjectid;
    /**
     * Only for hexagonal maps. Determines the width or height
     * (depending on the staggered axis) of the tile's edge, in pixels.
     */
    private int hexsidelength;
    /**
     * 对于staggered和hexagonal地图, 决定交错轴("x" or "y"). (since 0.11)
     */
    private int staggeraxis;
    private int staggerindex;// 对于staggered和hexagonal地图, determines whether the "even" or
    // "odd" indexes along the staggered axis are shifted. (since
    // 0.11)
    private String version;// The TMX format version, generally 1.0.

    public TileMap(Node node) {
        initAttributes(node);
    }

    public void initAttributes(Node node) {
        if (node == null) return;
        NamedNodeMap nodeMap = node.getAttributes();
        if (nodeMap == null) return;
        width = TileUtils.getIntegerAttribute(nodeMap, "width", 0);
        height = TileUtils.getIntegerAttribute(nodeMap, "height", 0);
        tilewidth = TileUtils.getIntegerAttribute(nodeMap, "tilewidth", 0);
        tileheight = TileUtils.getIntegerAttribute(nodeMap, "tileheight", 0);
        setOrientation(TileUtils.getAttributeValue(nodeMap, "orientation"));
        backgroundcolor = TileUtils.getColor(TileUtils.getAttributeValue(nodeMap, "backgroundcolor"));
        version = TileUtils.getAttributeValue(nodeMap, "version");
        renderorder = TileUtils.getAttributeValue(nodeMap, "renderorder");
        nextobjectid = TileUtils.getIntegerAttribute(nodeMap, "nextobjectid", 0);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; i++) {
            Node item = nodeList.item(i);
            if ("properties".equalsIgnoreCase(item.getNodeName())) {
                properties = TileUtils.getProperties(item, properties);
            } else if ("layer".equalsIgnoreCase(item.getNodeName())) {
            } else if ("tileset".equalsIgnoreCase(item.getNodeName())) {
            } else if ("objectgroup".equalsIgnoreCase(item.getNodeName())) {
            } else if ("imagelayer".equalsIgnoreCase(item.getNodeName())) {
            }
        }
        Element element = (Element) node;
        initTileLayer(element.getElementsByTagName("layer"));
        // 获取素材的tileset集合 包含图块图像信息
        initTileset(element.getElementsByTagName("tileset"));
        // 获取对象层
        initTileObjectLayer(element.getElementsByTagName("objectgroup"));
        // 获取图像层
        initTileImageLayer(element.getElementsByTagName("imagelayer"));
    }

    public void initTileLayer(NodeList layerList) {
        int len = layerList.getLength();
        layers = new TileLayer[len];
        for (int i = 0; i < len; i++) {
            layers[i] = new TileLayer();
            layers[i].initAttributes(layerList.item(i));
        }
    }

    public void initTileset(NodeList tilesetList) {
        int len = tilesetList.getLength();
        tilesets = new Tileset[len];
        for (int i = 0; i < len; i++) {
            tilesets[i] = new Tileset(tilesetList.item(i));
        }
    }

    public void initTileObjectLayer(NodeList tileObjectList) {
        int len = tileObjectList.getLength();
        objectLayers = new ObjectGroup[len];
        for (int i = 0; i < len; i++) {
            objectLayers[i] = new ObjectGroup(tileObjectList.item(i));
        }
    }

    private void initTileImageLayer(NodeList tileImageList) {
        int len = tileImageList.getLength();
        imageLayers = new ImageLayer[len];
        for (int i = 0; i < len; i++) {
            imageLayers[i] = new ImageLayer(tileImageList.item(i));
        }
    }

    public void setOrientation(String o) {
        if ("isometric".equalsIgnoreCase(o)) {
            orientation = ORIENTATION_ISOMETRIC;
        } else if ("orthogonal".equalsIgnoreCase(o)) {
            orientation = ORIENTATION_ORTHOGONAL;
        } else if ("hexagonal".equalsIgnoreCase(o)) {
            orientation = ORIENTATION_HEXAGONAL;
        } else if ("shifted".equalsIgnoreCase(o)) {
            orientation = ORIENTATION_SHIFTED;
        } else {
            System.out.println("Unknown orientation '" + o + "'");
        }
    }

    /**
     * global ID
     */
    public Tile getTileByGid(int gid) {
        //TODO
        Tile tile = null;
        for (int i = 0, pre, count = 1; i < tilesets.length; i++) {
            pre = count;
            count += tilesets[i].getTilecount();
            if (gid < count) {
                gid -= pre;//至此gid已经由全局id变成局部id
            }
        }
        return tile;
    }

    public void paint(Graphics2D g2d) {
        for (int k = 0; k < layers.length; k++) {
            layers[k].paint(g2d, tilesets, tilewidth, tileheight);
        }
        g2d.setColor(Color.red);
        //TODO
//		g2d.fillRect(TMXViewer.pointX-8, TMXViewer.pointY-8, 16, 16);
    }

    /**
     * @return the backgroundcolor
     */
    public int getBackgroundcolor() {
        return backgroundcolor;
    }

    /**
     * @param backgroundcolor the backgroundcolor to set
     */
    public void setBackgroundcolor(int backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
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
     * @return the tilesets
     */
    public Tileset[] getTilesets() {
        return tilesets;
    }

    /**
     * @param tilesets the tilesets to set
     */
    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }

    /**
     * @return the layers
     */
    public TileLayer[] getLayers() {
        return layers;
    }

    /**
     * @param layers the layers to set
     */
    public void setLayers(TileLayer[] layers) {
        this.layers = layers;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
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
     * @return the orientation
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the renderorder
     */
    public String getRenderorder() {
        return renderorder;
    }

    /**
     * @param renderorder the renderorder to set
     */
    public void setRenderorder(String renderorder) {
        this.renderorder = renderorder;
    }

    /**
     * @return the nextobjectid
     */
    public int getNextobjectid() {
        return nextobjectid;
    }

    /**
     * @param nextobjectid the nextobjectid to set
     */
    public void setNextobjectid(int nextobjectid) {
        this.nextobjectid = nextobjectid;
    }

    /**
     * @return the hexsidelength
     */
    public int getHexsidelength() {
        return hexsidelength;
    }

    /**
     * @param hexsidelength the hexsidelength to set
     */
    public void setHexsidelength(int hexsidelength) {
        this.hexsidelength = hexsidelength;
    }

    /**
     * @return the staggeraxis
     */
    public int getStaggeraxis() {
        return staggeraxis;
    }

    /**
     * @param staggeraxis the staggeraxis to set
     */
    public void setStaggeraxis(int staggeraxis) {
        this.staggeraxis = staggeraxis;
    }

    /**
     * @return the staggerindex
     */
    public int getStaggerindex() {
        return staggerindex;
    }

    /**
     * @param staggerindex the staggerindex to set
     */
    public void setStaggerindex(int staggerindex) {
        this.staggerindex = staggerindex;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"backgroundcolor\":\"");
        builder.append(backgroundcolor);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"tilewidth\":\"");
        builder.append(tilewidth);
        builder.append("\",\"tileheight\":\"");
        builder.append(tileheight);
        builder.append("\",\"tilesets\":");
        builder.append(Arrays.toString(tilesets));
        builder.append(",\"layers\":");
        builder.append(Arrays.toString(layers));
        builder.append(",\"objectLayers\":");
        builder.append(Arrays.toString(objectLayers));
        builder.append(",\"imageLayers\":");
        builder.append(Arrays.toString(imageLayers));
        builder.append(",\"filename\":\"");
        builder.append(filename);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\",\"orientation\":\"");
        builder.append(orientation);
        builder.append("\",\"renderorder\":\"");
        builder.append(renderorder);
        builder.append("\",\"nextobjectid\":\"");
        builder.append(nextobjectid);
        builder.append("\",\"hexsidelength\":\"");
        builder.append(hexsidelength);
        builder.append("\",\"staggeraxis\":\"");
        builder.append(staggeraxis);
        builder.append("\",\"staggerindex\":\"");
        builder.append(staggerindex);
        builder.append("\",\"version\":\"");
        builder.append(version);
        builder.append("\",\"Tile\":");
        builder.append(Tile.TILES);
        builder.append("} ");
        return builder.toString();
    }

}
