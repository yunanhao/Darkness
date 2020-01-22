package demo.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class ImageFactory {
    // 创建可重复利用的StringBuilder对象用于存储验证码
    public static final StringBuilder CHAR_CODE = new StringBuilder();
    // 创建随机字符数据库(可读取配置文件)
    private final String CHAR_DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789abcdefghjkmnopqrstuvwxyz";
    // 构建随机对象
    private final Random RANDOM = new Random();
    // 设置验证图像的高度(可读取配置文件)
    private final int IMAGE_HEIGHT = 128;
    // 设置需要显示的字符数量(可读取配置文件)
    private final int CHAR_COUNT = 4;
    // 创建a r g b颜色的大小常量
    private final int COLOR_SIZE = 256;
    // 根据高度和字符数量计算宽度
    private final int IMAGE_WIDTH = IMAGE_HEIGHT * CHAR_COUNT;
    // 根据宽度和字符数量计算字符间隔 IMAGE_WIDTH / CHAR_COUNT;
    private final int CHAR_GAP = IMAGE_HEIGHT;
    // 根据宽度计算干扰线条的数量(可自定义)
    private final int LINE_COUNT = IMAGE_WIDTH >> 4;
    // 创建随机字符数据库长度的参数
    private final int LENGTH = CHAR_DATA.length();
    // 创建字体常量 避免在service中重复创建和销毁对象所导致的效率降低
    private final Font FONT = new Font("幼圆", Font.BOLD, IMAGE_HEIGHT);
    // 干扰线条的属性
    private final BasicStroke STROKE = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
    // 验证码图像的Image对象
    private final BufferedImage CHAR_IMAGE = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
            BufferedImage.TYPE_4BYTE_ABGR);

    private ImageFactory() {
    }

    public static ImageFactory getInstance() {
        return Instance.newInstance;
    }

    /**
     * 获取一个窗体 主要用于展示验证码 没啥用
     */
    public static final JFrame getFrame(final ImageFactory imageFactory) {
        final JFrame frame = new JFrame();
        final JPanel contentPane = new JPanel(new BorderLayout(), true);
        contentPane.setSize(528, 250);
        final JPanel panel = new JPanel(null, true) {
            static final long serialVersionUID = 1L;

            @Override
            public void update(final Graphics g) {
                super.update(g);
                System.out.println("update");
            }

            @Override
            public void paint(final Graphics g) {
                g.clearRect(0, 0, 512, 150);
                g.drawImage(imageFactory.getImage(), 0, 0, null);
                g.drawString(ImageFactory.CHAR_CODE.toString(), 0, 150);
                System.out.println("paint");
            }
        };
        panel.setSize(512, 150);
        final JPanel northPanel = new JPanel();
        northPanel.setBackground(new Color(0x0099ff));
        northPanel.add(new JLabel("验证码-->"));
        final JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(0xffff00));
        southPanel.setSize(512, 50);
        southPanel.add(new Button("click1"));
        southPanel.add(new Button("click2"));
        southPanel.add(new Button("click3"));
        southPanel.add(new Button("click4"));
        contentPane.add(northPanel, "North");
        contentPane.add(panel, "Center");
        contentPane.add(southPanel, "South");
        frame.setContentPane(contentPane);
        frame.setSize(new Dimension(contentPane.getWidth(), contentPane.getHeight()));
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                System.out.println(e);
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        panel.repaint();
                        break;
                    case MouseEvent.BUTTON3:
                        System.exit(0);
                        break;
                }
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    /**
     * 水平翻转图像
     *
     * @param bufferedimage 目标图像
     * @return 新图片
     */
    public static final BufferedImage flipImage(final BufferedImage bufferedimage) {
        final int w = bufferedimage.getWidth();
        final int h = bufferedimage.getHeight();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, bufferedimage.getColorModel().getTransparency())).createGraphics())
                .drawImage(bufferedimage, 0, 0, w, h, w, 0, 0, h, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 变更图像为指定大小
     *
     * @param bufferedimage 目标图像
     * @param w             宽
     * @param h             高
     * @return 新图片
     */
    public static final BufferedImage resizeImage(final BufferedImage bufferedimage, final int w, final int h) {
        final int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
                .setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.drawImage(bufferedimage, 0, 0, w, h, 0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
                null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 旋转图片为指定角度
     *
     * @param bufferedimage 目标图像
     * @param degree        旋转角度
     * @return 新图片
     */
    public static final BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
        final int w = bufferedimage.getWidth();
        final int h = bufferedimage.getHeight();
        final int type = bufferedimage.getColorModel().getTransparency();
        final BufferedImage img = new BufferedImage(w, h, type);
        final Graphics2D graphics2d = img.createGraphics();
        // graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 旋转图片
     *
     * @param originalimage 原始图像
     * @param theta         旋转角度
     * @param x             旋转中心的横坐标
     * @param y             旋转中心的纵坐标
     * @return image 改变之后的图像
     */
    public static final BufferedImage rotateImage(final Image originalimage, final double theta, final double x,
                                                  final double y) {
        final BufferedImage image = new BufferedImage(originalimage.getWidth(null), originalimage.getHeight(null),
                BufferedImage.TYPE_4BYTE_ABGR_PRE);
        final Graphics2D image_g2d = image.createGraphics();
        image_g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        image_g2d.rotate(theta, x, y);
        image_g2d.drawImage(originalimage, 0, 0, null);
        image_g2d.dispose();
        return image;
    }

    /**
     * 以图片的中心为原点旋转图片
     *
     * @param originalimage 原始图像
     * @param theta         旋转角度(弧度)
     * @return image 改变之后的图像
     */
    public static final BufferedImage rotateImage(final Image originalimage, final double theta) {
        final int x = originalimage.getWidth(null) >> 1, y = originalimage.getHeight(null) >> 1,
                size = (int) Math.hypot(x << 1, y << 1) >> 1;
        final BufferedImage image = new BufferedImage(size << 1, size << 1, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        final Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(size - x, size - y);
        g.translate(x, y);
        g.rotate(theta);
        g.translate(-x, -y);
        g.drawImage(originalimage, 0, 0, null);
        g.dispose();
        return image;
    }

    /**
     * 将图片素材切分成小图块
     *
     * @param tilecounts
     */
    public static final BufferedImage[] splitImage(final int tile_w, final int tile_h, final int tilecount,
                                                   final Image... image) {
        final BufferedImage[] images = new BufferedImage[tilecount + 1];
        images[0] = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0, length = image.length; i < length; i++) {
            final int column = image[i].getWidth(null) / tile_w;
            final int row = image[i].getHeight(null) / tile_h;
            for (int y = 0, count = 1; y < row; y++) {
                for (int x = 0; x < column; x++, count++) {
                    images[count] = new BufferedImage(tile_w, tile_h, BufferedImage.TYPE_4BYTE_ABGR_PRE);
                    final Graphics2D g = images[count].createGraphics();
                    g.drawImage(image[i], 0, 0, tile_w, tile_h, tile_w * x, tile_h * y, tile_w * x + tile_w,
                            tile_h * y + tile_h, null);
                    g.dispose();
                }
            }
        }
        return images;
    }

    /**
     * 矩形地图绘制
     *
     * @param row
     * @param column
     * @param tile_w
     * @param tile_h
     */
    public static final void drawMap2D(final Graphics g, final int row, final int column, final int tile_w,
                                       final int tile_h) {
        g.setColor(new Color(0x0099ff));
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < column; x++) {
                g.draw3DRect(x * tile_w, y * tile_h, tile_w, tile_h, true);
            }
        }
    }

    /**
     * 45度交错地图绘制
     */
    public static final void drawMap45(final Graphics2D g, final int row, final int column, final int tile_w,
                                       final int tile_h) {
        g.setColor(new Color(0x0099ff));
        int dx, dy;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < column; x++) {
                if (y % 2 != 0) {
                    dx = x * tile_w;
                    dy = y * tile_h / 2;
                } else {
                    dx = -tile_w / 2 + x * tile_w;
                    dy = -tile_h + y * tile_h / 2;
                }
                g.draw3DRect(dx, dy, tile_w, tile_h, true);
            }
        }
    }

    /**
     * 将源图像由方变圆
     *
     * @param image 源图像
     */
    public static final BufferedImage getRoundImage(final Image image) {
        return getRoundImage(image, image.getWidth(null), image.getHeight(null), 0, 0, image.getWidth(null),
                image.getHeight(null));
    }

    /**
     * 获取指定大小圆的图像
     *
     * @param image  源图像
     * @param width  结果图像的宽度
     * @param height 结果图像的高度
     * @param startX 源图像的起始横坐标
     * @param startY 源图像的起始纵坐标
     * @param endX   源图像的结束横坐标
     * @param endY   源图像的结束纵坐标
     */
    public static final BufferedImage getRoundImage(final Image image, final double width, final double height,
                                                    final int startX, final int startY, final int endX, final int endY) {
        final Ellipse2D.Double round = new Ellipse2D.Double(0, 0, width, height);
        final BufferedImage roundImage = new BufferedImage((int) width, (int) height,
                BufferedImage.TYPE_4BYTE_ABGR_PRE);
        final Graphics2D g2d = roundImage.createGraphics();
        g2d.setClip(round);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, (int) width, (int) height, startX, startY, endX, endY, null);
        g2d.dispose();
        return roundImage;
    }

    /**
     * 获取一张可指定大小的边框图像
     *
     * @param source 用于放大的.9png源图像
     * @param width  要获取的图像的宽度
     * @param height 要获取的图像的高度
     * @param span_L 源图像左边距
     * @param span_U 源图像上边距
     * @param span_R 源图像右边距
     * @param span_D 源图像下边距
     */
    public static final BufferedImage getNinePNG(final Image source, final int width, final int height,
                                                 final int span_L, final int span_U, final int span_R, final int span_D) {
        return getNinePNG(source, new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE), span_L, span_U,
                span_R, span_D);
    }

    /**
     * 获取一张可以缩放(最好用于放大)图像
     *
     * @param source 用于放大的.9png源图像
     * @param target 用来返回绘制完成的图像
     * @param span_L 源图像左边距
     * @param span_U 源图像上边距
     * @param span_R 源图像右边距
     * @param span_D 源图像下边距
     */
    public static final BufferedImage getNinePNG(final Image source, final Image target, final int span_L,
                                                 final int span_U, final int span_R, final int span_D) {
        final Graphics g = target.getGraphics();
        final int target_w = target.getWidth(null);
        final int target_h = target.getHeight(null);
        final int image_w = source.getWidth(null);
        final int image_h = source.getHeight(null);
        // 左上角
        g.drawImage(source, 0, 0, span_L, span_U, 0, 0, span_L, span_U, null);
        // 上边
        g.drawImage(source, span_L, 0, target_w - span_R, 0 + span_U, span_L, 0, image_w - span_R, span_U, null);
        // 右上角
        g.drawImage(source, target_w - span_R, 0, target_w, 0 + span_U, image_w - span_R, 0, image_w, span_U, null);
        // 左边
        g.drawImage(source, 0, span_U, span_L, 0 + target_h - span_D, 0, span_U, span_L, image_h - span_D, null);
        // 中间
        g.drawImage(source, span_L, span_U, target_w - span_R, target_h - span_D, span_L, span_U, image_w - span_R,
                image_h - span_D, null);
        // 右边
        g.drawImage(source, target_w - span_R, span_U, target_w, target_h - span_D, image_w - span_R, span_U, image_w,
                image_h - span_D, null);
        // 左下角
        g.drawImage(source, 0, target_h - span_D, span_L, target_h, 0, image_h - span_D, span_L, image_h, null);
        // 下边
        g.drawImage(source, span_L, target_h - span_D, 0 + target_w - span_R, target_h, span_L, image_h - span_D,
                image_w - span_R, image_h, null);
        // 右下角
        g.drawImage(source, target_w - span_R, target_h - span_D, target_w, target_h, image_w - span_R,
                image_h - span_D, image_w, image_h, null);
        g.dispose();
        return (BufferedImage) target;
    }

    /**
     * 测试验证码窗体的入口
     */
    public static void main(final String[] args) {
        final ImageFactory imageFactory = ImageFactory.getInstance();
        ImageFactory.getFrame(imageFactory).setVisible(true);
    }

    /**
     * 返回一个具有验证码的图像
     */
    public BufferedImage getImage() {
        // 删除之前存储的内容
        ImageFactory.CHAR_CODE.delete(0, ImageFactory.CHAR_CODE.length());
        // 验证码图像的Image对象的画笔对象(只有Graphics2D拥有rotate方法)
        Graphics2D g = CHAR_IMAGE.createGraphics();// 将画笔对象赋值便于作画
        // 清除上次残留的图像以免对本次的验证码产生干扰
        g.clearRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        // 设置随机得到的背景颜色填充整个画布 为使其不干扰用户识别验证码降低一半透明度
        g.setColor(new Color(RANDOM.nextInt(COLOR_SIZE >> 1), RANDOM.nextInt(COLOR_SIZE), RANDOM.nextInt(COLOR_SIZE),
                RANDOM.nextInt(COLOR_SIZE)));
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        for (int i = 0; i < CHAR_COUNT; i++) {// 根据要求显示的字符数量进行循环绘制
            final char c = CHAR_DATA.charAt(RANDOM.nextInt(LENGTH));
            ImageFactory.CHAR_CODE.append(c);
            g.setFont(FONT);// 设置字体
            // 旋转画笔方向便于作出形状奇怪的验证码
            g.rotate(Math.toRadians(RANDOM.nextInt(120) - 60), (CHAR_GAP >> 1) + i * CHAR_GAP, IMAGE_HEIGHT >> 1);
            g.translate(RANDOM.nextInt(32) - 16, RANDOM.nextInt(32) - 16);
            // 设置画笔的颜色从而绘制不同颜色的验证码
            g.setColor(new Color(RANDOM.nextInt(COLOR_SIZE), RANDOM.nextInt(COLOR_SIZE), RANDOM.nextInt(COLOR_SIZE)));
            // 绘制字符 每次都向后移动一个间隔
            g.drawString(String.valueOf(c), i * CHAR_GAP, IMAGE_HEIGHT - 16);
            for (int j = 0, count = RANDOM.nextInt(4) + 4; j < count; j++) {
                g.fillOval(RANDOM.nextInt(IMAGE_WIDTH), RANDOM.nextInt(IMAGE_HEIGHT), RANDOM.nextInt(16) + 4,
                        RANDOM.nextInt(16) + 4);
            }
            // 销毁画笔对象并重新赋值 避免之前的操作产生干扰
            g.dispose();
            g = CHAR_IMAGE.createGraphics();
        }
        System.out.println(ImageFactory.CHAR_CODE);
        g.setStroke(STROKE);// 设置干扰线条的Stroke属性
        for (int i = 0; i < LINE_COUNT; i++) {// 循环绘制干扰线条
            g.setColor(new Color(RANDOM.nextInt(COLOR_SIZE >> 1), RANDOM.nextInt(COLOR_SIZE),
                    RANDOM.nextInt(COLOR_SIZE), RANDOM.nextInt(COLOR_SIZE)));
            g.drawLine(RANDOM.nextInt(IMAGE_WIDTH), RANDOM.nextInt(IMAGE_HEIGHT), RANDOM.nextInt(IMAGE_WIDTH),
                    RANDOM.nextInt(IMAGE_HEIGHT));
        }
        return CHAR_IMAGE;
    }

    private static final class Instance {
        private static final ImageFactory newInstance = new ImageFactory();
    }
}
