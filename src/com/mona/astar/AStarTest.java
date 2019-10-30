package com.mona.astar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
public class AStarTest extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    // 地图描述
    private final int[][] map = new int[80][160];
    // 无法移动区域
    private final int[] limit = {1};
    private final int CS = 8;
    private final AStar aStar;
    private final Random random;
    private final Dimension dimension;
    private final Graphics2D graphics2d;
    private final BufferedImage offscreen;
    private final Button[] buttons;
    private boolean isSetStart, isRemove, isAdd;
    private int mouseX, mouseY, startX, startY, targetX, targetY;
    private long before, after;
    /**
     * 从起点到目标的路径
     */
    private List<Node> path;

    public AStarTest() {
        random = new Random();
        dimension = new Dimension(map[0].length * CS, map.length * CS + 32);
        setPreferredSize(dimension);
        aStar = new AStar(map, limit);
        offscreen = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_4BYTE_ABGR_PRE);
        graphics2d = offscreen.createGraphics();
        addMouseListener(this);
        addMouseMotionListener(this);
        buttons = new Button[]{new Button("RandomMap"), new Button("ClearMap"),
                new Button("SetStart"), new Button("AddLimit"),
                new Button("RemoveLimit"), new Button("StartSearch"),};
        for (int i = 0, len = buttons.length; i < len; i++) {
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.setTitle("Java的A*寻径实现");
        frame.setContentPane(new AStarTest());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        switch (e.getActionCommand()) {
            case "RandomMap":
                for (int i = 0; i < map.length; i++) {
                    Arrays.fill(map[i], 0);
                    for (int j = 0; j < map[i].length; j++) {
                        if (random.nextInt(3) == 2) {
                            map[i][j] = 1;
                        }
                    }
                }
                break;
            case "ClearMap":
                for (int i = 0; i < map.length; i++) {
                    Arrays.fill(map[i], 0);
                }
                break;
            case "AddLimit":
                isAdd = false;
                isRemove = false;
                isSetStart = false;
                isAdd = true;
                break;
            case "RemoveLimit":
                isAdd = false;
                isRemove = false;
                isSetStart = false;
                isRemove = true;
                break;
            case "SetStart":
                isAdd = false;
                isRemove = false;
                isSetStart = false;
                isSetStart = true;
                break;
            case "StartSearch":
                before = System.currentTimeMillis();
                path = aStar.init(map, limit)
                        .searchPath(startX, startY, targetX, targetY);
                after = System.currentTimeMillis();
                break;
        }
        repaint();
    }

    @Override
    public void paint(final Graphics g) {
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
        graphics2d.fillRect(startX * CS, startY * CS, CS, CS);
        graphics2d.setColor(Color.BLUE);
        graphics2d.fillRect(targetX * CS, targetY * CS, CS, CS);
        // 绘制路径
        if (path != null) {
            graphics2d.setColor(Color.YELLOW);
            // 遍历坐标，并一一描绘
            for (int i = 0; i < path.size(); i++) {
                final Node node = path.get(i);
                // 描绘边框
                graphics2d.fillRect(node.x * CS + (helf >> 1), node.y * CS
                        + (helf >> 1), helf, helf);
            }
        }
        graphics2d.setColor(Color.black);
        graphics2d.drawString("寻径耗时(ms):" + (after - before), 0,
                dimension.height - 64);
        graphics2d.drawString("寻径次数" + aStar.count, 0, dimension.height - 48);
        g.drawImage(offscreen, 0, 32, null);
    }

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
        mouseX = e.getX() / CS;
        mouseY = (e.getY() - 32) / CS;
        if (e.getButton() == 1) {
            if (mouseY < map.length && mouseX < map[0].length) {
                if (isAdd) {
                    map[mouseY][mouseX] = 1;
                }
                if (isRemove) {
                    map[mouseY][mouseX] = 0;
                }
                if (isSetStart) {
                    startX = mouseX;
                    startY = mouseY;
                }
            }
        }
        if (e.getButton() == 3) {
            targetX = mouseX;
            targetY = mouseY;
        }
        repaint();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (mouseY < map.length && mouseX < map[0].length) {
            if (isAdd) {
                map[(e.getY() - 32) / CS][e.getX() / CS] = 1;
            }
            if (isRemove) {
                map[(e.getY() - 32) / CS][e.getX() / CS] = 0;
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
    }
}
