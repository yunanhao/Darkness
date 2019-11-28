package demo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 截图程序演示 按确定键截图
 */
public class Screenshot {
    public static void main(final String[] args) throws Exception {
        final Deom d = new Deom();
        // 全屏显示
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(d);
    }

}

class Deom extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Point sp = new Point();
    Point ep = new Point();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    BufferedImage img = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_RGB);

    public Deom() throws Exception {

        final Robot r = new Robot();
        img = r.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                sp = e.getPoint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                ep = e.getPoint();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                try {
                    saveImg();
                } catch (final IOException e1) {

                    e1.printStackTrace();
                }

                System.exit(0);
            }

        });
    }

    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        // 设置透明度
        final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(ac);

        g2d.fill3DRect((int) Math.min(sp.getX(), ep.getX()), (int) Math.min(sp.getY(), ep.getY()), (int) Math.abs(sp.getX()
                        - ep.getX()) + 1, // 防止值为零
                (int) Math.abs(sp.getY() - ep.getY()) + 1, false);
        g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    public void saveImg() throws IOException {
        final BufferedImage saveImg = img.getSubimage((int) Math.min(sp.getX(), ep.getX()), (int) Math.min(sp.getY(), ep
                        .getY()), (int) Math.abs(sp.getX() - ep.getX()) + 1, // 防止值为零
                (int) Math.abs(sp.getY() - ep.getY()) + 1);
        final SimpleDateFormat sd = new SimpleDateFormat("yyyymmddhhss");
        final String name = sd.format(new Date());

        final File path = FileSystemView.getFileSystemView().getHomeDirectory();
        final File filepath = new File(path + File.separator + name + ".jpg");
        ImageIO.write(saveImg, "jpg", filepath);

    }
}