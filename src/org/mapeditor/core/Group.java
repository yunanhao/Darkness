package org.mapeditor.core;

import java.util.ArrayList;
import java.util.List;

public class Group extends Layer {
    protected List<TileLayer> layer;
    protected List<ObjectGroup> objectgroup;
    protected List<ImageLayer> imagelayer;
    protected List<Group> group;

    public List<TileLayer> getLayer() {
        if (layer == null) {
            layer = new ArrayList<TileLayer>();
        }
        return layer;
    }

    public List<ObjectGroup> getObjectgroup() {
        if (objectgroup == null) {
            objectgroup = new ArrayList<ObjectGroup>();
        }
        return objectgroup;
    }

    public List<ImageLayer> getImagelayer() {
        if (imagelayer == null) {
            imagelayer = new ArrayList<ImageLayer>();
        }
        return imagelayer;
    }

    public List<Group> getGroup() {
        if (group == null) {
            group = new ArrayList<Group>();
        }
        return group;
    }

}
