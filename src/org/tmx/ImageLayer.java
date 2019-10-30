package org.tmx;

public class ImageLayer {
    // The name of the image layer.
    String name;
    // The x|y position of the image layer in pixels.
    int x, y;
    // : The width|height of the image layer in tiles. Meaningless.
    int width, height;
    // : The opacity of the layer as a value from 0 to 1. Defaults to 1.
    int opacity;
    // : Whether the layer is shown (1) or hidden (0). Defaults to 1.
    int visible;
}
