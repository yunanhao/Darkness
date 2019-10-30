package org.mapeditor.core;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Properties;

public class Tile {
    protected int id;
    protected String type;
    protected String terrain;
    protected float probability;
    protected Properties properties;
    protected Animation animation;
    protected List<ObjectGroup> objectgroup;

    protected BufferedImage image;
    protected String source;

    public Tile() {
        this(-1);
    }

    public Tile(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        type = value;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String value) {
        terrain = value;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float value) {
        probability = value;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties value) {
        properties = value;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation value) {
        animation = value;
    }

    public List<ObjectGroup> getObjectgroup() {
        return objectgroup;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getWidth() {
        if (image != null) {
            return image.getWidth();
        }
        return 0;
    }

    public int getHeight() {
        if (image != null) {
            return image.getHeight();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\":");
        builder.append(id);
        builder.append(",\"type\":\"");
        builder.append(type);
        builder.append("\",\"terrain\":\"");
        builder.append(terrain);
        builder.append("\",\"probability\":\"");
        builder.append(probability);
        builder.append("\",\"properties\":");
        builder.append(properties);
        builder.append(",\"animation\":");
        builder.append(animation);
        builder.append(",\"objectgroup\":");
        builder.append(objectgroup);
        builder.append("} \n");
        return builder.toString();
    }

}
