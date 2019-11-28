package org.tmx;

public class Tileset {
    String name;
    int firstgid;
    String source;
    int tilewidth, tileheight;
    int spacing, margin;
    int tilecount;

    /**
     * @param name
     * @param firstgid
     * @param source
     * @param tilewidth
     * @param tileheight
     * @param spacing
     * @param margin
     * @param tilecount
     */
    public Tileset(final String name, final String firstgid, final String source,
                   final String tilewidth, final String tileheight, final String spacing,
                   final String margin, final String tilecount) {
        super();
        this.name = name;
        // this.firstgid = firstgid;
        // this.source = source;
        // this.tilewidth = tilewidth;
        // this.tileheight = tileheight;
        // this.spacing = spacing;
        // this.margin = margin;
        // this.tilecount = tilecount;
    }

}
