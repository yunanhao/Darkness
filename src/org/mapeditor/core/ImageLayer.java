package org.mapeditor.core;

public class ImageLayer extends Layer {

    public int imgwidth;
    public int imgheight;
    public String imgsource;
    public String imgtrans;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\":\"");
        builder.append(name);
        builder.append("\",\"x\":");
        builder.append(x);
        builder.append(",\"y\":");
        builder.append(y);
        builder.append(",\"width\":");
        builder.append(width);
        builder.append(",\"height\":");
        builder.append(height);
        builder.append(",\"opacity\":");
        builder.append(opacity);
        builder.append(",\"visible\":");
        builder.append(visible);
        builder.append(",\"properties\":");
        builder.append(properties);
        builder.append("} \n");
        return builder.toString();
    }

}
