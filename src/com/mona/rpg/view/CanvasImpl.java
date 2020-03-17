package com.mona.rpg.view;

import com.mona.rpg.model.Interface.IDrawable;
import com.mona.rpg.model.Interface.IMatrix;
import com.mona.rpg.view.Interface.ICanvas;

import java.awt.*;

public class CanvasImpl implements ICanvas {
    private static volatile ICanvas canvas;
    private Graphics2D graphics2D;
    private int color;

    public static ICanvas getInstance() {
        synchronized (ICanvas.class) {
            if (canvas == null) {
                synchronized (ICanvas.class) {
                    canvas = new CanvasImpl();
                }
            }
        }
        return canvas;
    }

    public void init(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
        color = 0xFFff0000;
        graphics2D.setColor(new Color(color));
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int setColor(int color) {
        this.color ^= color;
        color ^= this.color;
        this.color ^= color;
        graphics2D.setColor(new Color(this.color));
        return color;
    }

    @Override
    public void drawRect(int left, int top, int right, int bottom) {
        graphics2D.fillRect(left, top, right - left, bottom - top);
    }

    @Override
    public void drawOval(int centerX, int centerY, int width, int height) {
        graphics2D.drawOval(centerX - width / 2, centerY - height / 2, width, height);
    }

    @Override
    public void drawCurve(double... points) {

    }

    @Override
    public void draw(IDrawable drawable) {
        drawable.draw(this);
    }

    @Override
    public void translate(float dx, float dy) {

    }

    @Override
    public void scale(float sx, float sy) {

    }

    @Override
    public void rotate(float degrees) {

    }

    @Override
    public void skew(float sx, float sy) {

    }

    @Override
    public void concat(IMatrix matrix) {

    }

    @Override
    public void setMatrix(IMatrix matrix) {

    }

    @Override
    public void getMatrix(IMatrix ctm) {

    }

    @Override
    public void draw(ICanvas canvas) {

    }
}
