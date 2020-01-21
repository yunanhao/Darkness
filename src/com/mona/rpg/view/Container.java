package com.mona.rpg.view;

import com.mona.rpg.control.IController;
import com.mona.rpg.model.IDrawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    }

    @Override
    public void mouseExited(MouseEvent e) {

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

    }

}
