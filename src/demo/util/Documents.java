package demo.util;

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
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文档的工具 意义并不大
 */
public final class Documents {

    public static final String label_head = "<\\?xml(\\s+([A-z]\\w*)=('|\").*?\\3)*\\?>";
    public static final String label_all = "<([A-z]\\w*)(\\s+([A-z]\\w*)=('|\").*?\\4)*(/>|>(.*?)</\\1>)";
    public static final String label_single = "<([A-z]\\w*)(\\s+([A-z]\\w*)=('|\").*?\\4)*/>";
    public static final String label_double = "<([A-z]\\w*)(\\s+([A-z]\\w*)=('|\").*?\\4)*>(.*?)</\\1>";
    public static final Pattern head = Pattern.compile(
            "<\\s*\\?xml\\s*(\\s+[a-zA-Z][a-zA-Z0-9]*\\s*=(['|\"]{1})\\S*\\2\\s*)*\\s*\\?>");
    public static final Pattern label = Pattern.compile(Documents.label_all);

    /**
     * (推荐使用)加载地图文件 初始化成员变量
     */
    public static final HashMap<String, Object> loadMapFile(final InputStream mapFlie)
            throws ParserConfigurationException, SAXException, IOException {
        // 1.创建DocumentBuilder工厂
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        // 2.创建DocumentBuilder对象
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // 3.DocumentBuilder对象的parse方法得到Document对象
        final Document document = documentBuilder.parse(mapFlie);
        // 4.获取根元素
        final HashMap<String, Object> map = new HashMap<>();
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
            final String[] mapTemp = layerElement.getTextContent().split(",", 0);
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
            imagePath[i] = ((Element) tileElement.getElementsByTagName("image").item(0)).getAttribute("source");
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
                if (objectElement.hasAttribute("width") && objectElement.hasAttribute("height")) {
                    final int w = Integer.parseInt(objectElement.getAttribute("width"));
                    final int h = Integer.parseInt(objectElement.getAttribute("height"));
                    if (objectElement.hasChildNodes()) {
                        areas.add(new Area(new Ellipse2D.Float(x, y, w, h)));
                    } else {
                        areas.add(new Area(new Rectangle(x, y, w, h)));
                    }
                } else {
                    // 多边形
                    final Element polygonElement = (Element) objectElement.getElementsByTagName("polygon").item(0);
                    if (polygonElement != null) {
                        final Polygon polygon = new Polygon();
                        final String string = polygonElement.getAttribute("points");
                        final String[] strings = string.split("[, ]");
                        for (int k = 0; k < strings.length; k++) {
                            final int pointX = Integer.parseInt(strings[k++]) + x;
                            final int pointY = Integer.parseInt(strings[k]) + y;
                            polygon.addPoint(pointX, pointY);
                        }
                        areas.add(new Area(polygon));
                    } else {
                        // 线段围成的图形
                        final Element polylineElement = (Element) objectElement.getElementsByTagName("polyline").item(0);
                        if (polylineElement != null) {
                            final String string = polylineElement.getAttribute("points");
                            final String[] strings = string.split("[, ]");
                            int pointX = x, pointY = y, pointXnext, pointYnext;
                            for (int k = 0; k < strings.length; k++) {
                                if (k + 1 > strings.length) {
                                    break;
                                }
                                pointXnext = Integer.parseInt(strings[k]) + x;
                                pointYnext = Integer.parseInt(strings[++k]) + y;
                                if (k < 2) {
                                    areas.add(new Area(new Line2D.Float(pointX, pointY, pointXnext, pointYnext)));
                                } else {
                                    if (k + 2 > strings.length) {
                                        break;
                                    }
                                    pointX = pointXnext;
                                    pointY = pointYnext;
                                    pointXnext = Integer.parseInt(strings[++k]) + x;
                                    pointYnext = Integer.parseInt(strings[++k]) + y;
                                    areas.add(new Area(new Line2D.Float(pointX, pointY, pointXnext, pointYnext)));
                                }
                            }
                        }
                    }
                }
            }
            map.put("objectgroup->" + name, areas);
        }
        System.out.println(map);
        return map;
    }

    /**
     * 检查xml格式(没啥用)
     */
    @Deprecated
    public static final void check(final String article, int count, final HashMap<String, String> attributes) {
        // 去除所有 </标签名称> 格式的结束标签
        final String article_new = article.replaceAll("</\\S*>", "");
        // 以 < 或者 > 分割字符串
        final String[] sections = article_new.split("<|>");
        // 每段节点
        for (int i = 0; i < sections.length; i++) {
            // 得到所有有数据的字符串
            if (!sections[i].matches("\\s*")) {
                // 以空格划分字符串获取包含单个元素名称、元素属性或者元素内容的子串
                final String[] attribute = sections[i].split(" ");
                String prefix = "";
                for (int k = 0; k < attribute.length; k++) {
                    // 以正则获取单个元素属性的键值对的组合
                    if (attribute[k].matches("([a-z]+)\\s*=\\s*\"([^\"]+)\"[/?|\\??]?")) {
                        final String[] key_value = attribute[k].replaceAll("\"|/|\\?", "").split("=");
                        // 将单个元素属性的键值对的组合分开并存入HashMap
                        final String key = prefix + "_" + key_value[0];
                        final String value = key_value[1];
                        attributes.put(key, value);
                    } else if (attribute[k].matches("[0-9|,]+")) {
                        // 将 Array+count作为键地图数组作为值存入HashMap
                        attributes.put("Array" + count++, attribute[k]);
                    } else {
                        prefix = attribute[k].replaceAll("[^\\w]", "");
                    }
                }
            }
        }
    }

    /**
     * 以IO流读取指定的XML文件getXmlDataMap
     *
     * @param xmlFile 要解析的地图xml文件的所有内容
     * @return attributes 返回该xml属性和文本的名称与内容的键值对
     */
    @Deprecated
    public static HashMap<String, String> getXmlDataMap(final File xmlFile) {
        final StringBuilder article = new StringBuilder();
        try {
            final FileInputStream in = new FileInputStream(xmlFile);
            final InputStreamReader in_read = new InputStreamReader(in);
            final BufferedReader buffread = new BufferedReader(in_read);
            for (String string; (string = buffread.readLine()) != null; article.append(string)) {
            }
            buffread.close();
            in_read.close();
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        // 计数器count
        int count = 0;
        // 声明一个HashMap<String, String>的变量attributes
        final HashMap<String, String> attributes = new HashMap<>();
        // 去除所有 </标签名称> 格式的结束标签
        final String article_new = article.toString().replaceAll("<\\s*\\S*>\\s*", "");
        // 以 < 或者 > 分割字符串
        final String[] sections = article_new.split("<|>");
        // 每段节点
        for (int i = 0; i < sections.length; i++) {
            // 得到所有有数据的字符串
            if (!sections[i].matches("\\s*")) {
                // 以空格划分字符串获取包含单个元素名称、元素属性或者元素内容的子串
                final String[] attribute = sections[i].split(" ");
                String prefix = "";
                for (int k = 0; k < attribute.length; k++) {
                    // 以正则获取单个元素属性的键值对的组合
                    if (attribute[k].matches("([a-z]+)\\s*=\\s*\"([^\"]+)\"[/?|\\??]?")) {
                        final String[] key_value = attribute[k].replaceAll("\"|/|\\?", "").split("=");
                        // 将单个元素属性的键值对的组合分开并存入HashMap
                        final String key = prefix + "_" + key_value[0];
                        final String value = key_value[1];
                        attributes.put(key, value);
                    } else if (attribute[k].matches("[0-9|,]+")) {
                        // 地图数组作为值存入HashMap
                        attributes.put("Array" + count++, attribute[k]);
                    } else {
                        prefix = attribute[k].replaceAll("[^\\w]", "");
                    }
                }

            }
        }
        return attributes;
    }

    /**
     * IO获取地图数组
     */
    @Deprecated
    public static int[][] ioToArray(final File file, final int column, final int row) {
        String array = "";
        final byte[] buffin = new byte[1024];
        try {
            final FileInputStream in = new FileInputStream(file);
            for (int length = 0; (length = in.read(buffin)) != -1; ) {
                array += new String(buffin, 0, length);
            }
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        final String[] s = array.replaceAll("\\s*", "").split(",");
        final int[][] map = new int[row][column];
        for (int j = 0, count = 0; j < map.length; j++) {
            for (int k = 0; k < map[j].length; k++, count++) {
                map[j][k] = Integer.parseInt(s[count]);
            }
        }
        return map;
    }

    /**
     * @param file 读取的文件
     * @return attributes 返回该xml属性和文本的名称与内容的键值对
     * @author Administrator
     * @category 通过io读取
     */
    @Deprecated
    public static HashMap<String, String> loadXml(final File file) {
        final StringBuilder article = new StringBuilder();
        try {
            final FileInputStream in = new FileInputStream(file);
            final InputStreamReader in_read = new InputStreamReader(in);
            final BufferedReader buffread = new BufferedReader(in_read);
            for (String section; (section = buffread.readLine()) != null; article.append(section)) {
            }
            buffread.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        // 声明一个HashMap<String, String>的变量attributes
        final HashMap<String, String> attributes = new HashMap<>();
        Pattern.compile("<\\s*\\?xml\\s*(\\s+[a-zA-Z][a-zA-Z0-9]*\\s*=(['|\"]{1})\\S*\\2\\s*)*\\s*\\?>");

        final String pattern = "<\\s*([a-zA-Z][a-zA-Z0-9]*)(\\s+[a-zA-Z]\\w*=(['|\"])\\S*\\3)*\\s*((>(.*?)<\\s*/\\s*\\1\\s*>)|(/\\s*>))";
        final Pattern label = Pattern.compile(pattern);
        final Stack<String> stack = new Stack<>();
        final ArrayList<String> list = new ArrayList<String>();
        Matcher matcher = label.matcher(article);
        stack.push(article.toString());
        while (!stack.isEmpty()) {
            matcher = label.matcher(stack.pop());
            while (matcher.find()) {
                final String s = stack.push(matcher.group().replaceFirst(pattern, "$6"));
                if (!s.matches("\\s*")) {
                    list.add(s.trim());
                }
            }
        }
        try {
            final FileOutputStream fos = new FileOutputStream(new File("resource/map/readme.txt"), false);
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            final BufferedWriter writer = new BufferedWriter(osw);
            for (final String string : list) {
                writer.write(string.replaceAll("\\d", ""));
            }
            writer.newLine();
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    public static HashMap<String, String> loadXml(final String file) throws IOException {
        final StringBuilder article = new StringBuilder(8192);
        final BufferedReader buffread = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        for (String section; (section = buffread.readLine()) != null; article.append(section)) {
        }
        buffread.close();
        // 声明一个HashMap<String, String>的变量attributes
        final HashMap<String, String> attributes = new HashMap<String, String>();
        final Stack<String> stack = new Stack<String>();
        final ArrayList<String> list = new ArrayList<String>();
        Matcher matcher = Documents.label.matcher(article);
        stack.push(article.toString());
        while (!stack.isEmpty()) {
            matcher = Documents.label.matcher(stack.pop());
            while (matcher.find()) {
                final String s = stack.push(matcher.group().replaceFirst(Documents.label_all, "$6"));
                if (!s.matches("\\s*")) {
                    list.add(s.trim());
                }
            }
        }
        return attributes;
    }

    /**
     * @param xmlFile 需要读取的固定格式XML文件
     * @return 包含文件相关数据的键值对
     * @throws ParserConfigurationException
     * @throws SAXException
     * @author Administrator
     * @category 用DOM读取xml文件
     */
    public static HashMap<String, String> loadXmlFile(File xmlFile) throws ParserConfigurationException, SAXException {
        final HashMap<String, String> attributes = new HashMap<>();
        try {
            if (xmlFile == null) {
                xmlFile = new File("resource/map/adventure.tmx");
            }
            // 1.创建DocumentBuilder工厂
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2.创建DocumentBuilder对象
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3.DocumentBuilder对象的parse方法得到Document对象
            final Document document = documentBuilder.parse(new BufferedInputStream(new FileInputStream(xmlFile)));
            // 4.获取根元素
            final Element element = document.getDocumentElement();
            // 5.NodeList集合
            final NodeList nodeList = element.getChildNodes();

            attributes.put("width", element.getAttribute("width"));
            attributes.put("height", element.getAttribute("height"));
            attributes.put("tilewidth", element.getAttribute("tilewidth"));
            attributes.put("tileheight", element.getAttribute("tileheight"));
            // 6.遍历查找元素
            for (int i = 0; i < nodeList.getLength(); i++) {
                // System.out.println(nodeList.item(i).getNodeName());
                final String text = nodeList.item(i).getTextContent().replaceAll("\\s*", "");
                if (nodeList.item(i).getNodeName().equalsIgnoreCase("layer")) {
                    attributes.put(((Element) nodeList.item(i)).getAttribute("name"), text);
                } else if (nodeList.item(i).getNodeName().equalsIgnoreCase("tileset")) {
                    attributes.put(((Element) nodeList.item(i)).getAttribute("name"), text);
                } else if (nodeList.item(i).getNodeName().equalsIgnoreCase("imagelayer")) {
                    attributes.put(((Element) nodeList.item(i)).getAttribute("name"), text);
                } else if (nodeList.item(i).getNodeName().equalsIgnoreCase("objectgroup")) {
                    attributes.put(((Element) nodeList.item(i)).getAttribute("name"), text);
                } else {
                    // System.out.println("XML文件 未知元素 [" + i + "]:" +
                    // nodeList.item(i)+"\n");
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    public static void main(final String[] args) throws Exception {
        final long SCbefore = System.currentTimeMillis();
        Documents.scannerToArray(new File("resource/map/scanner.txt"), 20, 22);
        final long SCnow = System.currentTimeMillis();
        final String scanner = "Scanner-timecost:\t" + (SCnow - SCbefore) + "毫秒(ms)";
        System.out.println(scanner);

        final long iobefore = System.currentTimeMillis();
        Documents.ioToArray(new File("resource/map/scanner.txt"), 20, 22);
        final long ionow = System.currentTimeMillis();
        final String iotosc = "IOthanScanner-timecost:\t" + (ionow - iobefore) + "毫秒(ms)";
        System.out.println(iotosc);

        final long TXTbefore = System.currentTimeMillis();
        Documents.readText(new File("resource/map/adventure.txt"));
        final long TXTnow = System.currentTimeMillis();
        final String iotxt = "IOtxtreader-timecost:\t" + (TXTnow - TXTbefore) + "毫秒(ms)";
        System.out.println(iotxt);

        final long xmliobefore = System.currentTimeMillis();
        Documents.getXmlDataMap(new File("src/vivi/map.xml"));
        final long xmlionow = System.currentTimeMillis();
        final String ioxml = "IOxmlreader-timecost:\t" + (xmlionow - xmliobefore) + "毫秒(ms)";
        System.out.println(ioxml);

        final long Xmlbefore = System.currentTimeMillis();
        Documents.loadXmlFile(new File("src/vivi/map.xml"));
        final long Xmlnow = System.currentTimeMillis();
        final String dom = "DOMxmlreader-timecost:\t" + (Xmlnow - Xmlbefore) + "毫秒(ms)";
        System.out.println(dom);

        final long Saxbefore = System.currentTimeMillis();

        final long Saxnow = System.currentTimeMillis();
        final String sax = "Saxxmlreader-timecost:\t" + (Saxnow - Saxbefore) + "毫秒(ms)";
        System.out.println(sax);

        final long before = System.currentTimeMillis();
        try {
            final FileOutputStream fos = new FileOutputStream(new File("resource/map/data.txt"), true);
            final OutputStreamWriter osw = new OutputStreamWriter(fos);
            final BufferedWriter writer = new BufferedWriter(osw);
            writer.newLine();
            writer.write(scanner);
            writer.newLine();
            writer.write(iotosc);
            writer.newLine();
            writer.write(iotxt);
            writer.newLine();
            writer.write(ioxml);
            writer.newLine();
            writer.write(dom);
            writer.newLine();
            final long now = System.currentTimeMillis();
            writer.write("写入耗时:" + (now - before) + "毫秒(ms)");
            writer.newLine();
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        Documents.loadXml(new File("src/vivi/map.xml"));
        // &lt; < 小于
        // &gt; > 大于
        // &amp; & 和号
        // &apos; ' 单引号
        // &quot; " 引号
        final StringBuilder article = new StringBuilder(8192);
        final BufferedReader buffread = new BufferedReader(new InputStreamReader(new FileInputStream(
                "src\\vivi\\map.xml")));
        for (String section; (section = buffread.readLine()) != null; article.append(section)) {
        }
        buffread.close();
        Documents.loadXml("src\\vivi\\map.xml");
        final int[][] map = {{00, 01, 02, 03, 04, 05, 06, 07, 8, 9}, {10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, {20,
                21, 22, 23, 24, 25, 26, 27, 28, 29}, {30, 31, 32, 33, 34, 35, 36, 37, 38, 39}, {40, 41, 42, 43, 44, 45, 46,
                47, 48, 49},};
        int[] gm;
        int length = 0;
        int index = 0;
        final int mapx = map.length, mapy = map[0].length;
        // 计算一维数组长度
        for (int i = 0; i < mapx; i++) {
            length += map[i].length;
        }
        // 复制元素
        gm = new int[length];
        for (int i = 0; i < mapx; i++) {
            for (int j = 0; j < mapy; j++) {
                gm[index++] = map[i][j];
            }
        }
        for (int x, y, z = 0; z < index; z++) {
            y = z / 10;
            x = z % 10;
            System.out.print("y=" + y + "--x=" + x);
            System.out.print("old:" + map[y][x]);
            System.out.println("|new:" + gm[y * map[0].length + x]);
        }
        for (int i = mapx - 1; i > 0; i--) {
            map[i] = map[i - 1];
            for (int j = 0; j < mapy; j++) {
                System.out.print("A " + i + j + "=" + map[i][j] + "\t");
            }
            System.out.println();
        }
        for (int i = 0; i < mapx - 1; i++) {
            final int[] temp = map[i + 1];
            map[i] = temp;
            for (int j = 0; j < mapy; j++) {
                System.out.print("B " + i + j + "=" + map[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Properties读取配置文件
     */
    public static void readConfig(File cfgFile) {
        try {
            final InputStream inputStream = new FileInputStream(cfgFile);
            // InputStream inputStream =
            // this.getClass().getClassLoader().getResourceAsStream("src/fightgame/Config.properties");
            final Properties properties = new Properties();
            properties.load(inputStream);
            properties.setProperty("name", "123");
            properties.store(new FileWriter("src/vivi/Config.properties"), "it is 注释");
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 要读取的特定格式的*.txt文件
     *
     * @return attributes 返还一个键值对组
     */
    public static HashMap<String, String> readText(final File textFile) {
        String article = "";
        final byte[] buffin = new byte[1024];
        final HashMap<String, String> attributes = new HashMap<>();
        try {
            final FileInputStream in = new FileInputStream(textFile);
            for (int length = 0; (length = in.read(buffin)) != -1; ) {
                article += new String(buffin, 0, length);
            }
            in.close();
            final String[] section = article.split("\\[.*\\]", -1);

            for (int i = 0; i < section.length; i++) {
                if (i == 1) {
                    final String[] header = section[i].split("\\s", 0);
                    for (int j = 0; j < header.length; j++) {
                        if (header[j].startsWith("width")) {
                            attributes.put("column", header[j].substring(6));
                        }
                        if (header[j].startsWith("height")) {
                            attributes.put("row", header[j].substring(7));
                        }
                        if (header[j].startsWith("tilewidth")) {
                            attributes.put("tilewidth", header[j].substring(10));
                        }
                        if (header[j].startsWith("tileheight")) {
                            attributes.put("tileheight", header[j].substring(11));
                        }
                    }
                } else if (i == 2) {
                    final String[] tilesets = section[i].replaceAll("\\s", "").split("tileset", 0);
                    for (int j = 0; j < tilesets.length; j++) {
                        if (tilesets[j].startsWith("=")) {
                            // TODO
                        }
                    }
                } else if (i == 3) {
                    attributes.put("array" + (i - 3), section[i].substring(section[i].indexOf("data=") + 7).replaceAll("\\s",
                            ""));
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    /**
     * Scanner读取文件
     */
    public static int[][] scannerToArray(final File file, final int column, final int row) {
        final int[][] arr = new int[row][column];
        try {
            final Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",");
            while (scanner.hasNextInt()) {
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[i].length; j++) {
                        arr[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
