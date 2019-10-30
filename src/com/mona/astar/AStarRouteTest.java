package com.mona.astar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:Java的A*寻径实现
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * <p>
 * License: http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 *
 * @author chenpeng
 * @version 0.1
 * @email：ceponline@yahoo.com.cn
 */
@SuppressWarnings("serial")
public class AStarRouteTest extends Panel {
    final Random random;
    // 地图描述
    int[][] map = new int[20][20];
    // 无法移动区域
    Set<Integer> HIT;
    int CS = 16;
    // 起始坐标
    Point start = new Point(0, 0);
    // 目的坐标
    Point target = new Point(13, 13);
    /**
     * 从起点到目标的路径
     */
    Point[] path;
    AStarRoute aStar;
    BufferedImage offscreen;

    public AStarRouteTest() {
        random = new Random();
        HIT = new HashSet<>();
        HIT.add(1);
        // 注入地图描述及障碍物描述
        final Dimension dimension = new Dimension(map[0].length * CS, map.length
                * CS);
        setPreferredSize(dimension);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(final MouseEvent e) {

            }

            @Override
            public void mousePressed(final MouseEvent e) {

            }

            @Override
            public void mouseExited(final MouseEvent e) {

            }

            @Override
            public void mouseEntered(final MouseEvent e) {

            }

            @Override
            public void mouseClicked(final MouseEvent e) {

                switch (e.getButton()) {
                    case 1:
                        start.x = e.getX() / CS;
                        start.y = e.getY() / CS;
                        break;
                    case 2:
                        for (int i = 0; i < map.length; i++) {
                            Arrays.fill(map[i], 0);
                            for (int j = 0; j < map[i].length; j++) {
                                if (random.nextInt(3) == 2) {
                                    map[i][j] = 1;
                                }
                            }
                        }
                        break;
                    case 3:
                        target.x = e.getX() / CS;
                        target.y = e.getY() / CS;
                        break;
                    default:
                        System.out.println(e);
                        break;
                }
                final long before = System.currentTimeMillis();
                path = new AStarRoute(map, target.x, target.y, start.x, start.y)
                        .getResult();
                final long after = System.currentTimeMillis();
                System.out.println("寻径耗时(ms):" + (after - before));
                repaint();
            }
        });
        offscreen = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_4BYTE_ABGR_PRE);
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.setTitle("Java的A*寻径实现");
        frame.setContentPane(new AStarRouteTest());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(final Graphics g) {
        final Graphics2D graphics2d = offscreen.createGraphics();
        final int helf = CS >> 1;
        // 绘制地图
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case 0:
                        graphics2d.setColor(Color.cyan);
                        graphics2d.fill3DRect(j * CS, i * CS, CS, CS, true);
                        break;
                    case 1:
                        graphics2d.setColor(Color.gray);
                        graphics2d.fill3DRect(j * CS, i * CS, CS, CS, true);
                        break;
                    case 2:
                        break;
                    default:
                        graphics2d.setColor(Color.black);
                        graphics2d.fill3DRect(j * CS, i * CS, CS, CS, true);
                        break;
                }
            }
        }
        graphics2d.setColor(Color.RED);
        graphics2d.fillRect(start.x * CS, start.y * CS, CS, CS);
        graphics2d.setColor(Color.BLUE);
        graphics2d.fillRect(target.x * CS, target.y * CS, CS, CS);
        // 绘制路径
        if (path != null) {
            graphics2d.setColor(Color.YELLOW);
            // 遍历坐标，并一一描绘
            for (int i = 0; i < path.length; i++) {
                // 描绘边框
                graphics2d.fillRect(path[i].x * CS + (helf >> 1), path[i].y * CS
                        + (helf >> 1), helf, helf);
            }
        }
        graphics2d.dispose();
        g.drawImage(offscreen, 0, 0, null);
    }
}
