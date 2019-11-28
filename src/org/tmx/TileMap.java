package org.tmx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 每个*.tmx文件都对应一个此类的实例
 */
public class TileMap {
    // 1.创建DocumentBuilder工厂
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
            .newInstance();
    // 2.创建DocumentBuilder对象
    private static DocumentBuilder documentBuilder;

    static {
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilderFactory.setValidating(false);
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    String version;
    // (orthogonal | isometric | staggered | hexagonal | shifted)
    String orientation;
    // (right-down | right-up | left-down | left-up)
    String renderorder;
    String backgroundcolor;
    int width, height;
    int tilewidth, tileheight;
    HashMap<String, String> properties;
    HashMap<String, Tileset> tileset;
    HashMap<String, TileLayer> tileLayer;
    HashMap<String, ImageLayer> imagelayer;
    HashMap<String, TileObjectGroup> tileObjectGroup;
    int tileTotal;

    /**
     * 加载地图文件
     */
    public static final TileMap loadTMXFile(final InputStream mapFlie) throws SAXException, IOException {
        // 3.DocumentBuilder对象的parse方法得到Document对象
        final Document document = documentBuilder.parse(mapFlie);
        // 4.获取根元素
        final Element root = document.getDocumentElement();
        final TileMap tileMap = new TileMap();
        tileMap.orientation = root.getAttribute("orientation");
        tileMap.version = root.getAttribute("version");
        tileMap.backgroundcolor = root.getAttribute("backgroundcolor");
        tileMap.width = Integer.parseInt(root.getAttribute("width"));
        tileMap.height = Integer.parseInt(root.getAttribute("height"));
        tileMap.tilewidth = Integer.parseInt(root.getAttribute("tilewidth"));
        tileMap.tileheight = Integer.parseInt(root.getAttribute("tileheight"));
        // 5.NodeList集合
        final NodeList layer = root.getElementsByTagName("layer");
        // 6.遍历查找元素
        for (int i = 0, len = layer.getLength(); i < len; i++) {
            final Element layerElement = (Element) layer.item(i);
            final String name = layerElement.getAttribute("name");
            final Element dataElement = (Element) layerElement.getElementsByTagName(
                    "data").item(0);
            tileMap.tileLayer.put(
                    name,
                    new TileLayer(name, layerElement.getAttribute("width"), layerElement
                            .getAttribute("height"), dataElement.getTextContent(), dataElement
                            .getAttribute("encoding")));
        }
        // 获取素材的tileset集合 包含图块图像信息
        final NodeList tilesetList = root.getElementsByTagName("tileset");
        for (int i = 0, len = tilesetList.getLength(); i < len; i++) {
            final Element tileElement = (Element) tilesetList.item(i);
            tileMap.tileset.put(
                    tileElement.getAttribute("name"),
                    new Tileset(tileElement.getAttribute("name"), tileElement
                            .getAttribute("firstgid"), ((Element) tileElement
                            .getElementsByTagName("image").item(0)).getAttribute("source"),
                            tileElement.getAttribute("tilewidth"), tileElement
                            .getAttribute("tileheight"), tileElement.getAttribute("spacing"),
                            tileElement.getAttribute("margin"), tileElement
                            .getAttribute("tilecount")));
            tileMap.tileTotal += Integer.parseInt(tileElement
                    .getAttribute("tilecount"));
        }
        // 获取对象层
        final NodeList objectLayerList = root.getElementsByTagName("objectgroup");
        for (int i = 0, len = objectLayerList.getLength(); i < len; i++) {
            final Element objectLayer = (Element) objectLayerList.item(i);
            final NodeList objectList = objectLayer.getElementsByTagName("object");
            final TileObjectGroup objectGroup = new TileObjectGroup(
                    objectLayer.getAttribute("name"));
            for (int j = 0, length = objectList.getLength(); j < length; j++) {
                final Element objectElement = (Element) objectList.item(j);
                final int x = Integer.parseInt(objectElement.getAttribute("x"));
                final int y = Integer.parseInt(objectElement.getAttribute("y"));
                // 如果拥有宽高属性则视为矩形或者椭圆
                if (objectElement.hasAttribute("width")
                        && objectElement.hasAttribute("height")) {
                    final int w = Integer.parseInt(objectElement.getAttribute("width"));
                    final int h = Integer.parseInt(objectElement.getAttribute("height"));
                    if (objectElement.hasChildNodes()) {
                        objectGroup.objects
                                .put(
                                        objectElement.getAttribute("id"),
                                        objectGroup.new TileObject(objectElement.getAttribute("id"), x, y, w, h, "ellipse"));
                    } else {
                        objectGroup.objects
                                .put(
                                        objectElement.getAttribute("id"),
                                        objectGroup.new TileObject(objectElement.getAttribute("id"), x, y, w, h, "rectangle"));
                    }
                } else {
                    // 线段围成的图形
                    final Element polylineElement = (Element) objectElement
                            .getElementsByTagName("polyline").item(0);
                    if (polylineElement != null) {
                        final Polygon polyline = new Polygon();
                        final String string = polylineElement.getAttribute("points");
                        // TODO
                        final String[] strings = string.split("[, ]");
                        for (int k = 0; k < strings.length; k++) {
                            final int pointX = Integer.parseInt(strings[k++]) + x;
                            final int pointY = Integer.parseInt(strings[k]) + y;
                            polyline.addPoint(pointX, pointY);
                        }
                    }
                    // 多边形
                    final Element polygonElement = (Element) objectElement
                            .getElementsByTagName("polygon").item(0);
                    if (polygonElement != null) {
                        objectGroup.objects
                                .put(
                                        objectElement.getAttribute("id"),
                                        objectGroup.new TileObject(objectElement.getAttribute("id"), x, y, polygonElement
                                                .getAttribute("points")));
                    }
                }
            }
            tileMap.tileObjectGroup
                    .put(objectLayer.getAttribute("name"), objectGroup);
        }
        return tileMap;
    }

    public static void main(final String[] args) throws ParserConfigurationException, SAXException, IOException {
        TMXReader.loadTMXFile(new FileInputStream(
                "G:\\[素材天下]\\RMVA素材\\Tile/GameMap.tmx"));
    }
}
