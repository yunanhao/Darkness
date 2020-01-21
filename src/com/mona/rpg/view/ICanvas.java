package com.mona.rpg.view;

import com.mona.rpg.model.IBaseDrawable;
import com.mona.rpg.model.IMatrix;

/**
 * 执行绘制操作
 */
public interface ICanvas {

    /**
     * 绘制矩形
     */
    void drawRect(int leftTop, int rightTop, int rightBottom, int leftBottom);

    /**
     * 绘制圆形
     */
    void drawOval(int centerX, int CenterY, int width, int height);

    /**
     * 绘制曲线
     */
    void drawCurve(double... points);

    /**
     * 绘制复杂对象
     */
    void draw(IBaseDrawable drawable);

    /**
     * 设置画笔
     */
    IPaint setPaint(IPaint paint);

    /**
     * 获取当前画笔对象
     */
    IPaint getPaint();

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
