package org.tmx;

import java.awt.*;
import java.awt.geom.Area;
import java.util.HashMap;

public class TileObjectGroup {
    String name;
    int x, y;
    int width, height;
    float opacity;
    boolean visible;
    HashMap<String, TileObject> objects;

    /**
     * @param name
     */
    public TileObjectGroup(final String name) {
        super();
        this.name = name;
        objects = new HashMap<>();
    }

    /**
     * 对象层的单个对象的封装类
     */
    class TileObject {
        String id;
        String gid;
        String name;
        String type;
        int x, y;
        int width, height;
        Area area;

        /**
         * @param id
         * @param x
         * @param y
         * @param width
         * @param height
         * @param type
         */
        public TileObject(final String id, final int x, final int y,
                          final int width, final int height, final String type) {
            super();
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = type;
        }

        /**
         * @param id
         * @param x
         * @param y
         * @param points
         */
        public TileObject(final String id, final int x, final int y,
                          final String points) {
            super();
            this.id = id;
            this.x = x;
            this.y = y;
            final String[] strings = points.split("[, ]");
            final Polygon polygon = new Polygon();
            for (int k = 0; k < strings.length; k++) {
                final int pointX = Integer.parseInt(strings[k++]) + x;
                final int pointY = Integer.parseInt(strings[k]) + y;
                polygon.addPoint(pointX, pointY);
            }
        }

    }

}
