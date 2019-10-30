package org.mapeditor.core;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TileSet {
    public Map<Integer, Tile> tiles;
    public BufferedImage tileSetImage;
    protected int firstgid;
    protected String name;
    protected int tilewidth;
    protected int tileheight;
    protected int spacing;
    protected int margin;
    protected int tilecount;
    protected int columns;
    protected int offsetx;
    protected int offsety;
    protected int backgroundcolor;
    protected Properties properties;
    protected String source;

    public TileSet() {
        tiles = new HashMap<>();
    }

    public void setBackgroundcolor(String parseInt) {
        backgroundcolor = Integer.parseInt(parseInt.substring(1), 16);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getFirstgid() {
        return firstgid;
    }

    public void setFirstgid(int value) {
        firstgid = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        source = value;
    }

    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int value) {
        tilewidth = value;
    }

    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int value) {
        tileheight = value;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int value) {
        spacing = value;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int value) {
        margin = value;
    }

    public int getTilecount() {
        return tilecount;
    }

    public void setTilecount(int value) {
        tilecount = value;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int value) {
        columns = value;
    }

    /**
     * 通过id查找指定Tile
     */
    public Tile getTile(int id) {
        if (id < tilecount) {
            Tile tile = tiles.get(id);
            if (tile == null) {
                tile = new Tile(id);
                tiles.put(tile.getId(), tile);
            }
            if (columns == 0 || tile.getImage() != null) {
                return tile;
            }
            int temp = tilecount / columns;
            if (tileSetImage == null) {
                return tile;
            }
            for (int y = 0; y < temp; y++) {
                for (int x = 0; x < columns; x++) {
                    if (id == x + y * columns) {
                        x = margin + (tilewidth + spacing) * x;
                        y = margin + (tileheight + spacing) * y;
                        tile.setImage(tileSetImage.getSubimage(x, y, tilewidth, tileheight));
                        return tile;
                    }
                }
            }
        }
        return null;
    }
}
