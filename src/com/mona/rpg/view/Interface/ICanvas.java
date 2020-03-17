package com.mona.rpg.view.Interface;

import com.mona.rpg.model.Interface.IDrawable;
import com.mona.rpg.model.Interface.IMatrix;

/**
 * 执行绘制操作
 */
public interface ICanvas extends IDrawable {

    int getColor();

    int setColor(int color);

    /**
     * 绘制矩形
     */
    void drawRect(int left, int top, int right, int bottom);

    /**
     * 绘制圆形
     */
    void drawOval(int centerX, int centerY, int width, int height);

    /**
     * 绘制曲线
     */
    void drawCurve(double... points);

    /**
     * 绘制复杂对象
     */
    void draw(IDrawable drawable);

    void translate(float dx, float dy);

    void scale(float sx, float sy);

    default void scale(float sx, float sy, float px, float py) {
        if (sx == 1.0f && sy == 1.0f) return;
        translate(px, py);
        scale(sx, sy);
        translate(-px, -py);
    }

    void rotate(float degrees);

    default void rotate(float degrees, float px, float py) {
        if (degrees == 0.0f) return;
        translate(px, py);
        rotate(degrees);
        translate(-px, -py);
    }

    void skew(float sx, float sy);

    void concat(IMatrix matrix);

    void setMatrix(IMatrix matrix);

    void getMatrix(IMatrix ctm);
}
