package org.mapeditor.core;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlAttribute;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MapObject implements Cloneable {
    protected Ellipse ellipse;
    protected Polygon polygon;
    protected Polyline polyline;
    protected Text text;
    protected int id;
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected double rotation;
    protected int gid;
    protected boolean visible;
    private ObjectGroup objectGroup;
    private String imageSource;
    private String name;
    private String type;
    private Image image;
    private Image scaledImage;
    private Tile tile;
    private Properties properties;

    public MapObject() {
        super();
        name = "Object";
        type = "";
        imageSource = "";
    }

    public MapObject(double x, double y, double width, double height, double rotation) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    @Override
    public MapObject clone() throws CloneNotSupportedException {
        MapObject clone = (MapObject) super.clone();
        clone.properties = (Properties) properties.clone();
        return clone;
    }

    public ObjectGroup getObjectGroup() {
        return objectGroup;
    }

    public void setObjectGroup(ObjectGroup objectGroup) {
        this.objectGroup = objectGroup;
    }

    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void setBounds(Rectangle2D.Double bounds) {
        x = bounds.getX();
        y = bounds.getY();
        width = bounds.getWidth();
        height = bounds.getHeight();
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String source) {
        if (imageSource.equals(source)) {
            return;
        }

        imageSource = source;

        // Attempt to read the image
        if (imageSource.length() > 0) {
            try {
                image = ImageIO.read(new File(imageSource));
            } catch (IOException e) {
                image = null;
            }
        } else {
            image = null;
        }

        scaledImage = null;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Image getImage(double zoom) {
        if (image == null) {
            return null;
        }

        final int zoomedWidth = (int) (getWidth() * zoom);
        final int zoomedHeight = (int) (getHeight() * zoom);

        if (scaledImage == null || scaledImage.getWidth(null) != zoomedWidth || scaledImage.getHeight(
                null) != zoomedHeight) {
            scaledImage = image.getScaledInstance(zoomedWidth, zoomedHeight, Image.SCALE_SMOOTH);
        }

        return scaledImage;
    }

    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    @Override
    public String toString() {
        return type + " (" + getX() + "," + getY() + ")";
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties value) {
        properties = value;
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    public void setEllipse(Ellipse value) {
        ellipse = value;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon value) {
        polygon = value;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline value) {
        polyline = value;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text value) {
        text = value;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image value) {
        image = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        type = value;
    }

    public double getX() {
        return x;
    }

    public void setX(double value) {
        x = value;
    }

    public double getY() {
        return y;
    }

    public void setY(double value) {
        y = value;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double value) {
        width = value;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double value) {
        height = value;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double value) {
        rotation = value;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int value) {
        gid = value;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean value) {
        visible = value;
    }

    public static class Ellipse {

    }

    public static class Polygon {

        @XmlAttribute(name = "points")
        protected String points;

        /**
         * 获取points属性的值。
         *
         * @return possible object is {@link String }
         */
        public String getPoints() {
            return points;
        }

        /**
         * 设置points属性的值。
         *
         * @param value allowed object is {@link String }
         */
        public void setPoints(String value) {
            points = value;
        }

    }

    public static class Polyline {

        @XmlAttribute(name = "points")
        protected String points;

        /**
         * 获取points属性的值。
         *
         * @return possible object is {@link String }
         */
        public String getPoints() {
            return points;
        }

        /**
         * 设置points属性的值。
         *
         * @param value allowed object is {@link String }
         */
        public void setPoints(String value) {
            points = value;
        }

    }

    public static class Text {
        protected String value;
        protected String fontfamily;
        protected Integer pixelsize;
        protected Boolean wrap;
        protected String color;
        protected Boolean bold;
        protected Boolean italic;
        protected Boolean underline;
        protected Boolean strikeout;
        protected Boolean kerning;
        protected int halign;
        protected int valign;

    }

}
