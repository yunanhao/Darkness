//package com.mona.rpg.demo;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.geom.Arc2D;
//import java.awt.geom.CubicCurve2D;
//import java.awt.geom.QuadCurve2D;
//
//public class MyPanel extends JPanel {
//    MyPanel() {
//        this.setPreferredSize(new Dimension(table.getWidth(), table.getHeight()));
//        this.addMouseMotionListener(new MouseMotionListener() {
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                dragX = e.getX();
//                dragY = e.getY();
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                mouseX = e.getX();
//                mouseY = e.getY();
//            }
//        });
//        this.addMouseListener(new MouseListener() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                clickX = e.getX();
//                clickY = e.getY();
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                pressX = e.getX();
//                pressY = e.getY();
//                d = Box2D.getDirection(pressX - LIST.get(0).getFocusX(), pressY - LIST.get(0).getFocusY());
//                LIST.get(0).rotateTo(d);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                releasX = e.getX();
//                releasY = e.getY();
//            }
//        });
//        JFrame frame = new JFrame("血腥大陆 V1.0");
//        frame.setContentPane(this);
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//        for (int i = 0; i < 10; i++) {
//            LIST.add(new Bullet(Math.random() * getWidth(), Math.random() * getHeight()));
//        }
//        this.addMouseWheelListener(Monitor.getInstance());
//        this.addMouseListener(Monitor.getInstance());
//    }
//
//    @Override
//    public void paint(Graphics graphics) {
//        Graphics2D g = (Graphics2D) graphics;
//        paintTime = System.currentTimeMillis();
//        int count = 0;
//        g.setColor(Color.darkGray);
//        g.fillRect(0, 0, getWidth(), getHeight());
//        //		g.drawImage(table, 0, 0, null);
//        g.setColor(Color.yellow);
//        for (Bullet bullet : LIST) {
//            bullet.move();
//            if (!bullet.inside(0, 0, getWidth(), getHeight())) {
//                if (bullet.crashLine(0, 0, getWidth(), 0)) {
//                    bullet.echo(0, 0, getWidth(), 0);
//                }
//                if (bullet.crashLine(getWidth(), 0, getWidth(), getHeight())) {
//                    bullet.echo(getWidth(), 0, getWidth(), getHeight());
//                }
//                if (bullet.crashLine(getWidth(), getHeight(), 0, getHeight())) {
//                    bullet.echo(getWidth(), getHeight(), 0, getHeight());
//                }
//                if (bullet.crashLine(0, getHeight(), 0, 0)) {
//                    bullet.echo(0, getHeight(), 0, 0);
//                }
//            }
//            if (bullet.intersects(200, 100, 300, 200)) {
//                if (bullet.crashLine(200, 100, 500, 100)) {
//                    bullet.echo(200, 100, 500, 100);
//                }
//                if (bullet.crashLine(500, 100, 500, 300)) {
//                    bullet.echo(500, 100, 500, 300);
//                }
//                if (bullet.crashLine(500, 300, 200, 300)) {
//                    bullet.echo(500, 300, 200, 300);
//                }
//                if (bullet.crashLine(200, 300, 200, 100)) {
//                    bullet.echo(200, 300, 200, 100);
//                }
//            }
//            bullet.paint((Graphics2D) g);
//        }
//        //		d = Unit.getDirection(mouseX-LIST.get(0).getFocusX(), mouseY-LIST.get(0).getFocusY());
//        //		LIST.get(0).rotateTo(d);
//        for (int i = LIST.size() - 1; i >= 0; i--) {
//            for (int j = i - 1; j >= 0; j--) {
//                if (LIST.get(i).intersects(LIST.get(j))) {
//                    LIST.get(i).back();
//                    LIST.get(j).back();
//                }
//            }
//        }
//        g.drawString("发射弧度:" + d, 0, 16 * ++count);
//        g.setColor(Color.white);
//        g.drawString("绘制耗时:" + (System.currentTimeMillis() - paintTime) + "ms", 0, 16 * ++count);
//        g.drawRect(200, 100, 300, 200);
//        g.drawString("是否碰撞:" + LIST.get(0).intersects(200, 100, 300, 200), 0, 16 * ++count);
//        g.setColor(Color.green);
//        g.draw(new QuadCurve2D.Double(200, 200, mouseX, mouseY, 500, 200));
//        g.setColor(Color.yellow);
//        g.draw(new CubicCurve2D.Double(200, 200, dragX, dragY, mouseX, mouseY, 500, 200));
//        g.setColor(Color.blue);
//        g.draw(new Arc2D.Double(200, 100, 300, 200, 30, 300, Arc2D.PIE));
//    }
//}
