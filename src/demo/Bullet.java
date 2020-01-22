package demo;

import demo.util.ShapeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 子弹模型
 * 采用AffineTransform对图像进行自由变换
 * 并采用两个私有变量记录起点进行变换
 * 继承的变量用于检测碰撞
 * 目测没有偏移
 */
public class Bullet extends Box2D {
    static double[] dirs = {DIR_RIGHTDOWN, DIR_RIGHT, DIR_RIGHTUP, DIR_UP, DIR_LEFTUP, DIR_LEFT, DIR_LEFTDOWN,
            DIR_DOWN};
    protected double temp_x, temp_y, temp_s;
    /**
     * 发射者
     */
    private Box2D parent;

    public Bullet(BufferedImage boxImage, double centerX, double centerY, double v, double direction, double... points) {
        init(boxImage, 0, 0, centerX, centerY, v, direction, points);
    }

    public Bullet(BufferedImage boxImage, double shootX, double shootY, double centerX, double centerY, double v,
                  double direction, double... points) {
        init(boxImage, shootX, shootY, centerX, centerY, v, direction, points);
        translate(shootX - focusX, shootY - focusY);
    }

    public Bullet() {
        this(null, 100, 100, 0, 0, 0.3, Math.random() * Math.PI * 2, new double[]{40, 14, 50, 6, 60,
                14, 50, 22});
        //		super(bullet[4], 0,0, 0.3, dirs[(int)(Math.random()*dirs.length)], new double[]{40,14,50,6,60,14,50,22});
        //		super(bullet[4], 0,0, 0.3, DIR_RIGHTDOWN, new double[]{40,14,50,6,60,14,50,22});
        scale(focusX, focusY, 2);
    }

    public Bullet(double x, double y) {
        this(null, x, y, 0, 0, 0.3, Math.random() * Math.PI * 2, new double[]{40, 14, 50, 6, 60, 14, 50, 22});
        scale(focusX, focusY, 2);
    }

    public void back() {
        setDirection(direction + Math.PI);
        move();
    }

    public void setDirection2(double direction) {
        this.direction = direction;
        if (direction == Double.NaN) {
            cos = 0;
            sin = 0;
        } else {
            cos = Math.cos(direction);
            sin = Math.sin(direction);
        }
    }

    public void setLocatoin(double lx, double ly) {
        lx -= focusX;
        ly -= focusY;
        translate(lx, ly);
    }

    //根据入射向量和反射面获取反射向量
    public void echo(double ax, double ay, double bx, double by) {
        bx -= ax;
        by -= ay;
        ax = v * cos;
        ay = v * sin;
        if (bx == 0) {
            // 反射面为Y轴
            ax = -ax;
            setDirection(ShapeUtil.getDirection(ax, ay));
            return;
        }
        if (by == 0) {
            // 反射面为X轴
            ay = -ay;
            setDirection(ShapeUtil.getDirection(ax, ay));
            return;
        }
        double length = 2 * (ax * bx - ay * by) / (bx * bx + by * by);
        ax -= -by * length;
        ay -= bx * length;
        setDirection(ShapeUtil.getDirection(ax, ay));
    }

    @Override
    public void paint(Graphics2D g) {
        move();
        //	roate1(direction);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //	g.drawImage(boxImage, atx, null);
        g.setColor(Color.blue);
        g.fill(this);
        g.setColor(Color.yellow);
        g.fillOval((int) (focusX - 6), (int) (focusY - 6), 12, 12);
        g.setColor(Color.red);
        g.fillOval((int) (focusX - 4), (int) (focusY - 4), 8, 8);
        g.drawLine((int) focusX, (int) focusY, (int) (focusX + cos * 40), (int) (focusY + sin * 40));
        //	roate2(direction);
    }

    public void roate1(double direction) {
        atx.rotate(direction, centerX + boxImage.getWidth() / 2, centerY + boxImage.getHeight() / 2);
        atx.translate(-boxImage.getWidth() / 2, -boxImage.getHeight() / 2);
        atx.translate(temp_s += v, 0);
    }

    public void roate2(double direction) {
        atx.translate(-temp_s, 0);
        atx.translate(boxImage.getWidth() / 2, boxImage.getHeight() / 2);
        atx.rotate(-direction, centerX + boxImage.getWidth() / 2, centerY + boxImage.getHeight() / 2);
    }

    /**
     * 图形绕中心旋转指定弧度(使用直白的方法实现)
     *
     * @param theta 旋转的弧度
     */
    public void rotateTo2(double theta) {
        double temp_x, temp_y;
        if (theta == Double.NaN) {
            cos = 0;
            sin = 0;
            return;
        }
        //		 this.cos = this.cos;
        sin = -sin;
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        System.out.println("this.sin=" + this.sin + ",this.cos=" + this.cos);
        System.out.println("sin=" + sin + ",cos=" + cos);
        for (int i = 1; i < length; i += 2) {
            points[i - 1] -= focusX;
            points[i] -= focusY;
            temp_x = points[i - 1] * this.cos - points[i] * this.sin;
            temp_y = points[i - 1] * this.sin + points[i] * this.cos;
            points[i - 1] = temp_x;
            points[i] = temp_y;
            temp_x = points[i - 1] * cos - points[i] * sin;
            temp_y = points[i - 1] * sin + points[i] * cos;
            points[i - 1] = temp_x + focusX;
            points[i] = temp_y + focusY;
        }
        direction = theta;
        this.cos = cos;
        this.sin = sin;
    }

    public Box2D getParent() {
        return parent;
    }

    public void setParent(final Box2D parent) {
        this.parent = parent;
    }

}
