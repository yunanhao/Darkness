package org.mapeditor.core;

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@XmlType
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "map")
public class TileMap implements Iterable<Layer> {
    public Properties properties;
    public List<Layer> layers;
    public List<TileSet> tileset;
    @XmlElement(name = "layer")
    public List<TileLayer> tileLayers;
    protected int width;
    protected int height;
    protected int tilewidth;
    protected int tileheight;
    protected int hexsidelength;
    protected int nextobjectid;
    protected int backgroundcolor;
    protected String orientation;
    protected String renderorder;
    protected String staggeraxis;
    protected String staggerindex;
    protected String version;
    protected String tiledversion;
    @XmlElement(name = "imagelayer")
    protected List<ImageLayer> imageLayers;
    @XmlElement(name = "objectgroup")
    protected List<ObjectGroup> objectGroups;
    @XmlElement(name = "object")
    protected List<Group> group;

    protected String filename;

    public TileMap() {
        tileset = new ArrayList<>();
        tileLayers = new ArrayList<>();
    }

    public TileMap(int width, int height) {
        this();
        this.width = width;
        this.height = height;
    }

    public Tile getTileByGId(int gid) {
        int id;
        TileSet tileSet;
        Tile tile = null;
        for (int i = 0, size = tileset.size(); i < size; i++) {
            tileSet = tileset.get(i);
            if ((id = gid - tileSet.getFirstgid()) >= 0) {
                if (id < tileSet.getTilecount()) {
                    return tileSet.getTile(id);
                }
            }
        }
        return tile;
    }

    public List<TileSet> getTileSets() {
        return tileset;
    }

    public List<TileLayer> getTileLayers() {
        return tileLayers;
    }

    public List<ImageLayer> getImagelayer() {
        return imageLayers;
    }

    public List<ObjectGroup> getObjectgroup() {
        return objectGroups;
    }

    public List<Group> getGroup() {
        return group;
    }

    @XmlAttribute(name = "version")
    public String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    public void setVersion(String value) {
        version = value;
    }

    @XmlAttribute(name = "tiledversion")
    public String getTiledversion() {
        return tiledversion;
    }

    public void setTiledversion(String value) {
        tiledversion = value;
    }

    @XmlAttribute(name = "orientation")
    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String value) {
        orientation = value;
    }

    @XmlAttribute(name = "renderorder")
    public String getRenderorder() {
        return renderorder;
    }

    public void setRenderorder(String value) {
        renderorder = value;
    }

    @XmlAttribute(name = "width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        width = value;
    }

    @XmlAttribute(name = "height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        height = value;
    }

    @XmlAttribute(name = "tilewidth")
    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int value) {
        tilewidth = value;
    }

    @XmlAttribute(name = "tileheight")
    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int value) {
        tileheight = value;
    }

    @XmlAttribute(name = "hexsidelength")
    public int getHexsidelength() {
        return hexsidelength;
    }

    public void setHexsidelength(Integer value) {
        hexsidelength = value;
    }

    @XmlAttribute(name = "staggeraxis")
    public String getStaggeraxis() {
        return staggeraxis;
    }

    public void setStaggeraxis(String value) {
        staggeraxis = value;
    }

    @XmlAttribute(name = "staggerindex")
    public String getStaggerindex() {
        return staggerindex;
    }

    public void setStaggerindex(String value) {
        staggerindex = value;
    }

    @XmlAttribute(name = "backgroundcolor")
    public int getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(int value) {
        backgroundcolor = value;
    }

    @XmlAttribute(name = "nextobjectid")
    public int getNextobjectid() {
        return nextobjectid;
    }

    public void setNextobjectid(int value) {
        nextobjectid = value;
    }

    @XmlElement(name = "properties")
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties value) {
        properties = value;
    }

    public List<Layer> getLayers() {
        if (layers == null) {
            layers = new ArrayList<>();
        }
        return layers;
    }

    public int getLayerCount() {
        return getLayers().size();
    }

    public Rectangle getBounds() {
        return new Rectangle(width, height);
    }

    @Override
    public Iterator<Layer> iterator() {
        return getLayers().iterator();
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void addTileset(TileSet tileset) {
        // Sanity check
        final int tilesetIndex = getTileSets().indexOf(tileset);
        if (tileset == null || tilesetIndex > -1) {
            return;
        }

        Tile t = tileset.getTile(0);

        if (t != null) {
            int tw = t.getWidth();
            int th = t.getHeight();
            if (tw != tilewidth && tileheight == 0) {
                tilewidth = tw;
                tileheight = th;
            }
        }

        this.tileset.add(tileset);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public int getTileHeightMax() {
        int maxHeight = tileheight;

        for (TileSet tileset : this.tileset) {
            int height = tileset.getTileheight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }

        return maxHeight;
    }

    public void swapTileSets(int index0, int index1) {
        if (index0 == index1 || tileset == null) {
            return;
        }
        TileSet set = tileset.get(index0);
        tileset.set(index0, tileset.get(index1));
        tileset.set(index1, set);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"filename\":\"");
        builder.append(filename);
        builder.append("\",\"width\":");
        builder.append(width);
        builder.append(",\"height\":");
        builder.append(height);
        builder.append(",\"tilewidth\":");
        builder.append(tilewidth);
        builder.append(",\"tileheight\":");
        builder.append(tileheight);
        builder.append(",\"hexsidelength\":");
        builder.append(hexsidelength);
        builder.append(",\"nextobjectid\":");
        builder.append(nextobjectid);
        builder.append(",\"backgroundcolor\":\"");
        builder.append(backgroundcolor);
        builder.append("\",\"version\":\"");
        builder.append(version);
        builder.append("\",\"tiledversion\":\"");
        builder.append(tiledversion);
        builder.append("\",\"orientation\":\"");
        builder.append(orientation);
        builder.append("\",\"renderorder\":\"");
        builder.append(renderorder);
        builder.append("\",\"staggeraxis\":\"");
        builder.append(staggeraxis);
        builder.append("\",\"staggerindex\":\"");
        builder.append(staggerindex);
        builder.append("\",\n\"tileset\":");
        builder.append(tileset);
        builder.append(",\n\"tileLayers\":");
        builder.append(tileLayers);
        builder.append(",\n\"objectGroups\":");
        builder.append(objectGroups);
        builder.append(",\n\"imageLayers\":");
        builder.append(imageLayers);
        builder.append(",\n\"group\":");
        builder.append(group);
        builder.append(",\n\"properties\":");
        builder.append(properties);
        builder.append("} \n");
        return builder.toString();
    }

}
