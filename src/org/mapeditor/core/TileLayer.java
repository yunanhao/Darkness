package org.mapeditor.core;

import java.awt.*;
import java.util.Properties;

public class TileLayer extends Layer {
    public String encoding;
    public String compression;
    public String value;

    public int[][] data;
    public Properties properties;

    public TileLayer() {
    }

    public Tile getTileAt(int x, int y) {
        // TODO 自动生成的方法存根
        return null;
    }

    public Rectangle getBounds() {
        // TODO 自动生成的方法存根
        return null;
    }

}