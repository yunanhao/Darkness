package org.mapeditor.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectGroup extends Layer implements Cloneable, Iterable<MapObject> {
    protected List<MapObject> objects;
    protected String color;
    protected String draworder;

    public ObjectGroup() {
        super();
    }

    public ObjectGroup(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        if (objects == null) {
            return true;
        }
        return objects.isEmpty();
    }

    public void addObject(MapObject o) {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.add(o);
        o.setObjectGroup(this);
    }

    public void removeObject(MapObject o) {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.remove(o);
        o.setObjectGroup(null);
    }

    @Override
    public Iterator<MapObject> iterator() {
        return objects.iterator();
    }

    public List<MapObject> getObjects() {
        if (objects == null) {
            objects = new ArrayList<MapObject>();
        }
        return objects;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String value) {
        color = value;
    }

    public String getDraworder() {
        return draworder;
    }

    public void setDraworder(String value) {
        draworder = value;
    }

}
