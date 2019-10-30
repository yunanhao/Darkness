package com.mona.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public final class SwingFrames {
    public static int location_x, location_y;

    /**
     * 获取一个窗体
     */
    public static final JFrame getFrame(final int width, final int height) {
        final JFrame frame = new JFrame();
        final JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(width, height));
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    /**
     * 确认是否退出窗体
     */
    public static final void exitGame(final Component component) {
        if (JOptionPane.showConfirmDialog(component, "您确定退出程序么?", "退出",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0)
            System.exit(0);
    }

    /**
     * 设置背景 必须在ContentPane确定不变之后调用
     */
    public static final void setBackgroundImage(final JFrame frame,
                                                final String path) {
        final ImageIcon icon = new ImageIcon(path);
        final JLabel backgroundlabel = new JLabel(icon);
        backgroundlabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        ((JPanel) frame.getContentPane()).setOpaque(false);
        frame.getLayeredPane().add(backgroundlabel, new Integer(Integer.MIN_VALUE));
    }

    /**
     * 设置背景 必须在ContentPane确定不变之后调用
     */
    public static final void setBackgroundImage(final JFrame frame,
                                                final Image image) {
        final ImageIcon icon = new ImageIcon(image);
        final JLabel backgroundlabel = new JLabel(icon);
        backgroundlabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        ((JPanel) frame.getContentPane()).setOpaque(false);
        frame.getLayeredPane().add(backgroundlabel, new Integer(Integer.MIN_VALUE));
    }

    /**
     * 窗口居中显示
     */
    public static final void setFrameCenter(final Window frame) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screen = toolkit.getScreenSize();
        final int Width = screen.width - frame.getWidth() >> 1;
        final int Heigth = screen.height - frame.getHeight() >> 1;
        frame.setLocation(Width, Heigth);
    }

    /**
     * 设置窗体可以根据某个组件拖动
     */
    public static final void setMoveable(final JFrame frame,
                                         final JComponent component) {
        // 拖动窗口
        component.addMouseListener(new MouseAdapter() {
            // 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
            @Override
            public void mousePressed(final MouseEvent e) {
                // 当鼠标按下的时候获得窗口当前的位置
                SwingFrames.location_x = e.getX();
                SwingFrames.location_y = e.getY();
            }
        });
        component.addMouseMotionListener(new MouseMotionAdapter() {
            // 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
            @Override
            public void mouseDragged(final MouseEvent e) {
                // 当鼠标拖动时获取窗口当前位置
                final int x = frame.getX();
                final int y = frame.getY();
                // 设置窗口的位置
                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
                frame.setLocation(x + e.getX() - SwingFrames.location_x, y + e.getY()
                        - SwingFrames.location_y);
            }
        });
    }

    /**
     * 创建硬件适配的缓冲图像，为了能显示得更快速
     */
    public static BufferedImage createCompatibleImage(final int width,
                                                      final int height) {
        final GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice device = env.getDefaultScreenDevice();
        final GraphicsConfiguration gc = device.getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    /**
     * 绘制45度交错地图
     */
    public static final void paintMap(final Graphics2D g, final int tile_w,
                                      final int tile_h, final byte[][] mapArray) {
        for (int y = 0, row = mapArray.length; y < row; y++)
            for (int x = 0, column = mapArray[y].length; x < column; x++)
                if (y % 2 != 0) g.draw3DRect(x * tile_w, y * tile_h / 2, tile_w,
                        tile_h, true);
                else g.draw3DRect(-tile_w / 2 + x * tile_w, -tile_h + y * tile_h / 2,
                        tile_w, tile_h, false);
    }

    /**
     * 绘制45度交错地图
     */
    public static final void paintMap(final Graphics2D g,
                                      final byte[][] mapArray, final int tile_w, final int tile_h,
                                      final BufferedImage... images) {
        int dx, dy;
        for (int y = 0, row = mapArray.length; y < row; y++)
            for (int x = 0, column = mapArray[y].length; x < column; x++) {
                if (y % 2 != 0) {
                    dx = x * tile_w;
                    dy = y * tile_h / 2;
                } else {
                    dx = -tile_w / 2 + x * tile_w;
                    dy = -tile_h + y * tile_h / 2;
                }
                g.drawImage(images[0], dx, dy, dx + tile_w, dy + tile_h, 0, 0, 1, 1,
                        null);
                g.draw3DRect(dx, dy, tile_w, tile_h, true);
            }
    }
}
