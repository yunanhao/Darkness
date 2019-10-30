package org.mapeditor.core;

import java.util.Properties;

public abstract class Layer implements Cloneable {
    protected String name;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int offsetx;
    protected int offsety;
    protected double opacity;
    protected boolean visible;
    protected boolean locked;
    protected Properties properties;

    public Layer() {
    }

    public Layer(int width, int height) {
        this(0, 0, width, height);
    }

    public Layer(int x, int y, int width, int height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOffsetx() {
        return offsetx;
    }

    public void setOffsetx(int offsetx) {
        this.offsetx = offsetx;
    }

    public int getOffsety() {
        return offsety;
    }

    public void setOffsety(int offsety) {
        this.offsety = offsety;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

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
        builder.append(",\"offsetx\":");
        builder.append(offsetx);
        builder.append(",\"offsety\":");
        builder.append(offsety);
        builder.append(",\"properties\":");
        builder.append(properties);
        builder.append("}");
        return builder.toString();
    }

}
