package org.tile;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Tile {
    public static final List<Tile> TILES = new ArrayList<>();
    private int gid;
    private int id;
    private int columns;
    private int width, height;
    private float probability;
    private Properties properties;
    private BufferedImage tilesetimg;

    public Tile(int id, int gid, Tileset tileset) {
        this.id = id;
        this.gid = gid;
        columns = tileset.getColumns();
        width = tileset.getTilewidth();
        height = tileset.getTileheight();
        tilesetimg = tileset.getTilesetimg();
    }

    public Tile(int gid) {
        this.gid = gid;
        TILES.add(this);
    }

    public static void addTile(Tile tile) {
        if (tile == null) return;
        for (Tile t : TILES) {
            if (tile.equals(t)) return;
        }
        TILES.add(tile);
    }

    public static Tile getTile(int gid) {
        for (Tile tile : TILES) {
            if (tile.gid == gid) return tile;
        }
        return new Tile(gid);
    }

    public void init(Node node) {
        NodeList nodeList = node.getChildNodes();
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            if ("properties".equalsIgnoreCase(nodeList.item(i).getNodeName())) {
                properties = TileUtils.getProperties(nodeList.item(i), properties);
            }
        }
    }

    public void paint(Graphics2D g2d, int mapTileWidth, int mapTileHeight, int dx, int dy) {
        dx *= mapTileWidth;
        dy *= mapTileHeight;
        int sx = id % columns * width;
        int sy = id / columns * height;
        g2d.drawImage(tilesetimg,
                dx, dy, dx + mapTileWidth, dy + mapTileHeight,
                sx, sy, sx + width, sy + height,
                null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"gid\":\"");
        builder.append(gid);
        builder.append("\",\"id\":\"");
        builder.append(id);
        builder.append("\",\"width\":\"");
        builder.append(width);
        builder.append("\",\"height\":\"");
        builder.append(height);
        builder.append("\",\"probability\":\"");
        builder.append(probability);
        builder.append("\",\"properties\":\"");
        builder.append(properties);
        builder.append("\"} ");
        return builder.toString();
    }

}
