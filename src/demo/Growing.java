package demo;

import demo.util.GeometryUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Growing extends JPanel {
    private final Timer timer;
    private final boolean stopped = false;
    // 修改type的值使用不同的绘制方式，1为compatible image, 2为swing的back-buffer
    int type = 1;
    private List<Point2D> ps = new ArrayList<Point2D>();
    private BufferedImage bufImg;

    public Growing() {
        ps.add(new Point2D.Double(0, 0));
        ps.add(new Point2D.Double(800, 0));

        timer = new Timer(500, e -> {
            grow();
            repaint();
        });

        timer.start();
    }

    // 创建硬件适配的缓冲图像，为了能显示得更快速
    public static BufferedImage createCompatibleImage(int w, int h, int type) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        return gc.createCompatibleImage(w, h, type);
    }

    private static void createGuiAndShow() {
        JFrame frame = new JFrame("Growing");
        frame.getContentPane().add(new Growing());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createGuiAndShow());
    }

    public void grow() {
        if (stopped) {
            return;
        }

        List<Point2D> temp = new ArrayList<Point2D>();
        temp.add(ps.get(0));

        for (int i = 0; i < ps.size() - 1; ++i) {
            Point2D p0 = ps.get(i);
            Point2D p4 = ps.get(i + 1);
            double len = GeometryUtil.distanceOfPoints(p0, p4);

            if (len < 0.01) {
                // 当线条长度小于1时，就停止再增长
                System.out.println(ps.size());
                timer.stop();
                return;
            }

            Point2D p1 = GeometryUtil.extentPoint(p0, p4, len / 3);
            Point2D p3 = GeometryUtil.extentPoint(p0, p4, len * 2 / 3);
            Point2D p2 = GeometryUtil.rotate(p3.getX(), p3.getY(), p1.getX(), p1.getY(), 60);

            temp.add(p1);
            temp.add(p2);
            temp.add(p3);
            temp.add(p4);
        }

        ps = null;
        ps = temp;
        temp = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // 改变窗口的大小，可以看到直接对intermediate image操作比直接对swing back-buffer操作快很多.
        // 所以有很多绘制操作时，使用triple buffer是很有必要的(因为Swing已经默认使用了双缓冲).

        if (type == 1) {
            // [[[1]]]: 操作 compatible image 速度非常快
            renderWithBuf(g2d, getWidth(), getHeight());
        } else {
            // [[[2]]]: 操作Swing的 back-buffer 速度非常慢
            render(g2d, getWidth(), getHeight());
        }
    }

    protected void renderWithBuf(Graphics2D g2d, int w, int h) {
        if (bufImg == null || bufImg.getWidth() != w || bufImg.getHeight() != h) {
            bufImg = createCompatibleImage(w, h, Transparency.OPAQUE);
            // bufImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D gg = bufImg.createGraphics();
        render(gg, w, h);
        gg.dispose();

        g2d.drawImage(bufImg, 0, 0, null);
    }

    protected void render(Graphics2D g2d, int w, int h) {
        g2d.setBackground(Color.BLACK);
        g2d.clearRect(0, 0, w, h);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(0, h - 20);

        g2d.setColor(Color.WHITE);
        for (int i = 0; i < ps.size() - 1; ++i) {
            Point2D sp = ps.get(i);
            Point2D ep = ps.get(i + 1);
            g2d.drawLine((int) sp.getX(), -(int) sp.getY(), (int) ep.getX(), -(int) ep.getY());
        }
    }
}
