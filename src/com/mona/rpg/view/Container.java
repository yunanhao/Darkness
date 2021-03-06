package com.mona.rpg.view;

import com.mona.rpg.control.ControllerImpl;
import com.mona.rpg.control.EventImpl;
import com.mona.rpg.control.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 整个工程的容器
 */
public class Container extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private IController controller;
    private Graphics2D graphics2D;

    public void init() {
        controller = ControllerImpl.getInstance();
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        JFrame frame = new JFrame("血腥大陆 V1.0");
        frame.setContentPane(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        graphics2D = (Graphics2D) g;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseDragged", e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseMoved", e.getX(), e.getY()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseClicked", e.getX(), e.getY()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseEntered", e.getX(), e.getY()));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseExited", e.getX(), e.getY()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.postEvent(EventImpl.create("mousePressed", e.getX(), e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.postEvent(EventImpl.create("mouseReleased", e.getX(), e.getY()));
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controller.postEvent(EventImpl.create("mouseWheelMoved", e.getX(), e.getY()));
    }

}
