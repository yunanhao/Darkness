package org.tmx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TMXReader {
    // 1.创建DocumentBuilder工厂
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
            .newInstance();
    // 2.创建DocumentBuilder对象
    private static DocumentBuilder documentBuilder;

    /**
     * 加载地图文件
     */
    public static final HashMap<String, Object> loadTMXFile(
            final InputStream mapFlie) throws ParserConfigurationException, SAXException, IOException {
        if (TMXReader.documentBuilder == null) {
            TMXReader.documentBuilder = TMXReader.documentBuilderFactory
                    .newDocumentBuilder();
            TMXReader.documentBuilderFactory.setValidating(false);
        }
        // 3.DocumentBuilder对象的parse方法得到Document对象
        final Document document = TMXReader.documentBuilder.parse(mapFlie);
        final HashMap<String, Object> map = new HashMap<>();
        // 4.获取根元素
        final Element root = document.getDocumentElement();
        final int width = Integer.parseInt(root.getAttribute("width"));
        final int height = Integer.parseInt(root.getAttribute("height"));
        map.put("width", width);
        map.put("height", height);
        map.put("tilewidth", Integer.parseInt(root.getAttribute("tilewidth")));
        map.put("tileheight", Integer.parseInt(root.getAttribute("tileheight")));
        map.put("orientation", root.getAttribute("orientation"));
        map.put("backgroundcolor", root.getAttribute("backgroundcolor"));
        map.put("version", root.getAttribute("version"));
        map.put("renderorder", root.getAttribute("renderorder"));
        map.put("nextobjectid", root.getAttribute("nextobjectid"));
        // 5.NodeList集合
        final NodeList layer = root.getElementsByTagName("layer");
        // 6.遍历查找元素
        for (int i = 0, len = layer.getLength(); i < len; i++) {
            final Element layerElement = (Element) layer.item(i);
            final int[][] map_array = new int[height][width];
            // 填充数组
            final String mapTemp[] = layerElement.getTextContent().split(",");
            for (int y = 0, count = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    map_array[y][x] = Integer.parseInt(mapTemp[count++].trim());
                }
            }
            map.put("layer->" + layerElement.getAttribute("name"), map_array);
        }
        // 获取素材的tileset集合 包含图块图像信息
        final NodeList tileset = root.getElementsByTagName("tileset");
        final String[] imagePath = new String[tileset.getLength()];
        int tileTotal = 0;
        for (int i = 0, len = tileset.getLength(); i < len; i++) {
            final Element tileElement = (Element) tileset.item(i);
            tileTotal += Integer.parseInt(tileElement.getAttribute("tilecount"));
            imagePath[i] = ((Element) tileElement.getElementsByTagName("image").item(
                    0)).getAttribute("source");
        }
        map.put("tileset->tileTotal", tileTotal);
        map.put("tileset->imagePath", imagePath);
        // 获取对象层
        final NodeList objectLayerList = root.getElementsByTagName("objectgroup");
        for (int i = 0, len = objectLayerList.getLength(); i < len; i++) {
            final Element objectLayer = (Element) objectLayerList.item(i);
            final String name = objectLayer.getAttribute("name");
            final NodeList objectList = objectLayer.getElementsByTagName("object");
            final Area areas = new Area();
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
                        areas.add(new Area(new Ellipse2D.Float(x, y, w, h)));
                    } else {
                        areas.add(new Area(new Rectangle(x, y, w, h)));
                    }
                } else {
                    // 多边形
                    final Element polygonElement = (Element) objectElement
                            .getElementsByTagName("polygon").item(0);
                    if (polygonElement != null) {
                        final Polygon polygon = new Polygon();
                        final String string = polygonElement.getAttribute("points");
                        final String[] points = string.split("[, ]");
                        for (int k = 0; k < points.length; k++) {
                            final int pointX = Integer.parseInt(points[k++]) + x;
                            final int pointY = Integer.parseInt(points[k]) + y;
                            polygon.addPoint(pointX, pointY);
                        }
                        areas.add(new Area(polygon));
                    } else {
                        // 线段围成的图形
                        final Element polylineElement = (Element) objectElement
                                .getElementsByTagName("polyline").item(0);
                        if (polylineElement != null) {
                            final String string = polylineElement.getAttribute("points");
                            final String[] points = string.split("[, ]");
                            final Polygon polyline = new Polygon();
                            for (int k = 0; k < points.length; k++) {
                                final int pointX = Integer.parseInt(points[k++]) + x;
                                final int pointY = Integer.parseInt(points[k]) + y;
                                polyline.addPoint(pointX, pointY);
                            }
                            areas.subtract(new Area(polyline));
                        }
                    }
                }
            }
            map.put("objectgroup->" + name, areas);
        }
        return map;
    }
}
