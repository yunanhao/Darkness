package org.tmx;

public class TileLayer {
    String name;
    int x, y;
    int width, height;
    float opacity;
    boolean visible;
    int[][] data;
    String encoding;
    int type;//tilelayer|objectgroup|imagelayer

    /**
     * @param name
     * @param width
     * @param height
     * @param data
     * @param encoding
     */
    public TileLayer(final String name, final String width, final String height,
                     final String data, final String encoding) {
        super();
        this.name = name;
        if (width != null) {
            this.width = Integer.parseInt(width);
        }
        if (height != null) {
            this.height = Integer.parseInt(height);
        }
        final String mapTemp[] = data.replaceAll("\\s*", "").split(",", 0);
        this.data = new int[this.height][this.width];
        for (int y = 0, count = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.data[y][x] = Integer.parseInt(mapTemp[count++]);
            }
        }
        this.encoding = encoding;
    }

    public void s() {

    }
}
