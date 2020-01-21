package com.mona.rpg.view;

import com.mona.rpg.control.IController;
import com.mona.rpg.model.IDrawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 整个工程的容器
 */
public class Container implements IDrawable, MouseListener, MouseMotionListener, MouseWheelListener {
    private IController controller;
    private IScene[] mScenes;
    private ICanvas mCanvas;
    private IPaint mPaint;

    public void init() {
        mCanvas.draw(mScenes[mScenes.length - 1]);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        panel.addMouseListener(this);
        panel.addMouseWheelListener(this);
        panel.addMouseMotionListener(this);
        JFrame frame = new JFrame("血腥大陆 V1.0");
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void draw(ICanvas canvas) {
        canvas.setPaint(mPaint);

        canvas.draw(mScenes[mScenes.length - 1]);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.postEvent("mouseDragged", e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.postEvent("mouseMoved", e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.postEvent("mouseClicked", e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        controller.postEvent("mouseEntered", e.getX(), e.getY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.postEvent("mouseExited", e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.postEvent("mousePressed", e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.postEvent("mouseReleased", e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controller.postEvent("mouseWheelMoved", e.getX(), e.getY());
    }

}
