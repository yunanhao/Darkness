package demo;

import com.mona.util.ShapeUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 二维世界物体的抽象模型
 * 此接口坐标系为X向右为正 Y向下为正 弧度均以X轴正方向为0顺时针增加
 * 实现此接口的物体应具有
 * 平面的起始坐标
 * 自身的宽度和高度
 * 平面中运动速度和运动方向
 * 物体自身的焦点方向
 * 方向分为两种：
 * 八方和360°
 * 前者采用状态码的叠加和移除进行方向变换
 * 后者采用弧度进行方向变换
 */
public abstract class Box2D implements Cloneable, Shape {
    /**
     * 方向为左的二进制状态码
     */
    public static final int LEFT = 0B1000;
    /**
     * 方向为右的二进制状态码
     */
    public static final int RIGHT = 0B0010;
    /**
     * 方向为上的二进制状态码
     */
    public static final int UP = 0B0100;
    /**
     * 方向为下的二进制状态码
     */
    public static final int DOWN = 0B0001;
    /**
     * 没有方向对应的弧度
     */
    public static final double DIR_CENTER = Double.NaN;
    /**
     * 方向为右所对应的弧度
     */
    public static final double DIR_RIGHT = 0;
    /**
     * 方向为右上所对应的弧度
     */
    public static final double DIR_RIGHTUP = Math.PI * 7 / 4;
    /**
     * 方向为正上所对应的弧度
     */
    public static final double DIR_UP = Math.PI * 3 / 2;
    /**
     * 方向为左上所对应的弧度
     */
    public static final double DIR_LEFTUP = Math.PI * 5 / 4;
    /**
     * 方向为正左所对应的弧度
     */
    public static final double DIR_LEFT = Math.PI;
    /**
     * 方向为左下所对应的弧度
     */
    public static final double DIR_LEFTDOWN = Math.PI * 3 / 4;
    /**
     * 方向为正下所对应的弧度
     */
    public static final double DIR_DOWN = Math.PI / 2;
    /**
     * 方向为右下所对应的弧度
     */
    public static final double DIR_RIGHTDOWN = Math.PI / 4;
    /**
     * 斜向运动时速率应乘以的倍数
     */
    public static final double Oblique = 0.70710678118654752440084436210485;
    /**
     * 一度所对应的弧度
     */
    public static final double Degre_One = 0.01745329251994329576923690768489;

    /**
     * 2D 仿射变换
     */
    protected AffineTransform atx;
    /**
     * 物体的图像
     */
    protected BufferedImage boxImage;
    /**
     * 物体朝向(以X正方向顺时针的偏角为正 弧度为单位)
     */
    protected double orientation;
    /**
     * 物体的中心
     */
    protected double centerX, centerY;
    /**
     * 物体的重心
     */
    protected double focusX, focusY;
    /**
     * 速率(默认大于等于0) 物体移动方向
     */
    protected double v, direction, cos, sin;
    /**
     * 物体顶点的数组
     */
    protected double[] points;
    /**
     * 物体的包围盒
     */
    protected double minX, minY, maxX, maxY;

    /**
     * 物体顶点数组的长度
     */
    protected int length;

    @Override
    public Box2D clone() throws CloneNotSupportedException {
        Box2D box2d = (Box2D) super.clone();
        box2d.points = points.clone();
        return box2d;
    }

    /**
     * 利用射线法判断点P是否在多边形上
     */
    @Override
    public boolean contains(double px, double py) {
        int hits = 0;
        double sx, sy, ex, ey, ccw0, ccw1, temp;
        sx = points[length - 2];
        sy = points[length - 1];
        for (int i = 1; i < length; i += 2, sx = ex, sy = ey) {
            ex = points[i - 1];
            ey = points[i];
            if (sy == ey) {
                continue;// 如果是平行线
            }
            if (sy < ey ? py < sy || py >= ey : py < ey || py >= sy) {
                continue;
            }
            if (sx > ex) {
                if (px > sx) {
                    continue;
                }
                if (px < ex) {
                    hits++;
                    continue;
                }
                temp = (py - sy) * (ex - sx);
                ccw0 = (px - sx) * (ey - sy) - temp;// 端点位置
                ccw1 = (sx + 1) * (ey - sy) - temp;// 射线位置
            } else {
                if (px > ex) {
                    continue;
                }
                if (px < sx) {
                    hits++;
                    continue;
                }
                temp = (py - sy) * (ex - sx);
                ccw0 = (px - sx) * (ey - sy) - temp;// 端点位置
                ccw1 = (ex + 1) * (ey - sy) - temp;// 射线位置
            }
            if (ccw0 == 0) {
                return true;
            }
            if (ccw0 > 0 ? ccw1 < 0 : ccw1 > 0) {
                hits++;
            }
        }
        return (hits & 1) != 0;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && !intersects(x, y, w, h);
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * 检测点是否在任意多边形上
     */
    public boolean contains1(double x, double y) {
        int hits = 0;
        double lastx = points[points.length - 2];
        double lasty = points[points.length - 1];
        double curx, cury, leftx, test1, test2;
        for (int i = 1; i < length; lastx = curx, lasty = cury, i += 2) {
            curx = points[i - 1];
            cury = points[i];
            if (cury == lasty) {
                continue;// 如果是平行线
            }
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;// x大于线段的最大横坐标
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;// 指定点的水平线与线段无交点
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }
            if (test1 < test2 / (lasty - cury) * (lastx - curx)) {
                hits++;
            }
        }
        return (hits & 1) != 0;
    }

    /**
     * 是否与指定线段有交集
     */
    public boolean crashLine(double cx, double cy, double dx, double dy) {
        double ax = points[length - 2];
        double ay = points[length - 1];
        double bx, by;
        double abac, abad, cdca, cdcb;
        for (int i = 1; i < length; i += 2, ax = bx, ay = by) {
            bx = points[i - 1];
            by = points[i];
            if (ax <= bx) {
                if (cx <= dx && bx <= cx) {
                    continue;
                }
                if (cx >= dx && bx <= dx) {
                    continue;
                }
            } else {
                if (cx <= dx && ax <= cx) {
                    continue;
                }
                if (cx >= dx && ax <= dx) {
                    continue;
                }
            }
            if (ay <= by) {
                if (cy <= dy && by <= cy) {
                    continue;
                }
                if (cy >= dy && by <= dy) {
                    continue;
                }
            } else {
                if (cy <= dy && ay <= cy) {
                    continue;
                }
                if (cy >= dy && ay <= dy) {
                    continue;
                }
            }
            abac = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
            abad = (bx - ax) * (dy - ay) - (dx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
            cdca = (dx - cx) * (ay - cy) - (ax - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
            cdcb = (dx - cx) * (by - cy) - (bx - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
            if ((abac >= 0 && abad <= 0 || abac <= 0 && abad >= 0) && (cdca >= 0 && cdcb <= 0 || cdca <= 0 && cdcb >= 0)) {
                return true;
            }
        }
        return false;
    }

    // TODO 图形绕指定直线翻转
    public void flip(int ax, int ay, int bx, int by) {
        for (int i = 1; i < length; i += 2) {
        }
        points[length] = points[0];
        points[length + 1] = points[1];
    }

    /**
     * 图形绕X翻转
     */
    public void flipByX(int x) {
        for (int i = 1; i < length; i += 2) {
            points[i - 1] = -(points[i - 1] - x - x);
        }
        minX = -(minX - x - x);
        maxX = -(maxX - x - x);
    }

    /**
     * 图形绕Y翻转
     */
    public void flipByY(int y) {
        for (int i = 1; i < length; i += 2) {
            points[i] = -(points[i] - y - y);
        }
        minY = -(minY - y - y);
        maxY = -(maxY - y - y);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * 获取物体运行方向
     */
    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        rotateTo(direction);
    }

    /**
     * 获取重心x坐标
     */
    public double getFocusX() {
        return focusX;
    }

    /**
     * 获取重心y坐标
     */
    public double getFocusY() {
        return focusY;
    }

    /**
     * 获取物体朝向
     */
    public double getOrientation() {
        return orientation;
    }

    /**
     * 设置物体的朝向
     */
    public void setOrientation(final double orientation) {
        this.orientation = orientation == Double.NaN ? this.orientation : orientation;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new UnitIterator(at, points);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }

    /**
     * 获取多边形的点数组
     */
    public double[] getPoints() {
        return points;
    }

    /**
     * 获取多边形的点长度
     */
    public int getPointsLength() {
        return length >> 1;
    }

    /**
     * 获取物体的速率
     */
    public double getV() {
        return v;
    }

    /**
     * 设置物体的速率
     */
    public void setV(final double v) {
        this.v = v;
    }

    /**
     * 获取第n点的x
     */
    public double getXByIndex(int n) {
        return points[++n << 1];
    }

    /**
     * 获取第n点的y
     */
    public double getYByIndex(int n) {
        return points[(++n << 1) + 1];
    }

    /**
     * 载入多边形数组
     */
    public void init(BufferedImage boxImage, double shootX, double shootY, double centerX, double centerY, double v,
                     double direction, double... points) {
        this.v = v;
        atx = new AffineTransform();
        this.boxImage = boxImage;
        this.centerX = centerX;
        this.centerY = centerY;
        this.points = points;
        length = (points.length & 1) != 0 ? points.length - 1 : points.length;
        resetBounds();
        initDirection(direction);
        orientation = direction;
    }

    /**
     * 初始化方向
     */
    public void initDirection(double direction) {
        cos = Math.cos(direction);
        sin = Math.sin(direction);
        double temp_x, temp_y;
        for (int i = 1; i < length; i += 2) {
            temp_x = points[i - 1] - focusX;
            temp_y = points[i] - focusY;
            points[i - 1] = temp_x * cos - temp_y * sin;
            points[i] = temp_x * sin + temp_y * cos;
        }
        this.direction = direction;
        focusX = 0;
        focusY = 0;
    }

    /**
     * 是否在指定矩形盒子的内部
     */
    public boolean inside(int x, int y, int w, int h) {
        for (int i = 1; i < length; i += 2) {
            if (points[i - 1] < x || points[i - 1] > x + w || points[i] < y || points[i] > y + h) {
                return false;
            }
        }
        return true;
    }

    /**
     * 任意两个多边形的边是否相交
     */
    public boolean intersects(Box2D box2d) {
        if (maxX < box2d.minX || box2d.maxX < minX || maxY < box2d.minY || box2d.maxY < minY) {
            return false;
        }
        double otherpoints[] = box2d.points;
        double ax, ay, bx, by, cx, cy, dx, dy;
        double abac, abad, cdca, cdcb;
        int i, j, len2 = otherpoints.length;
        ax = points[length - 2];
        ay = points[length - 1];
        for (i = 1; i < length; i += 2, ax = bx, ay = by) {
            bx = points[i - 1];
            by = points[i];
            cx = otherpoints[len2 - 2];
            cy = otherpoints[len2 - 1];
            for (j = 1; j < len2; j += 2, cx = dx, cy = dy) {
                dx = otherpoints[j - 1];
                dy = otherpoints[j];
                if (ax <= bx) {
                    if (cx <= dx && bx <= cx) {
                        continue;
                    }
                    if (cx >= dx && bx <= dx) {
                        continue;
                    }
                } else {
                    if (cx <= dx && ax <= cx) {
                        continue;
                    }
                    if (cx >= dx && ax <= dx) {
                        continue;
                    }
                }
                if (ay <= by) {
                    if (cy <= dy && by <= cy) {
                        continue;
                    }
                    if (cy >= dy && by <= dy) {
                        continue;
                    }
                } else {
                    if (cy <= dy && ay <= cy) {
                        continue;
                    }
                    if (cy >= dy && ay <= dy) {
                        continue;
                    }
                }
                abac = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
                abad = (bx - ax) * (dy - ay) - (dx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
                cdca = (dx - cx) * (ay - cy) - (ax - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
                cdcb = (dx - cx) * (by - cy) - (bx - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
                if ((abac >= 0 && abad <= 0 || abac <= 0 && abad >= 0) && (cdca >= 0 && cdcb <= 0 || cdca <= 0 && cdcb >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 只是简单认为几何图形的点在矩形内则相交
     */
    @Override
    public boolean intersects(double x, double y, double w, double h) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        w += x;
        h += y;
        // 速度太快 物体太大 墙体太薄 都有可能穿透
        for (int i = 1; i < length; i += 2) {
            if (points[i - 1] > x && points[i - 1] < w && points[i] > y && points[i] < h) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean isCollision(Box2D box2d) {
        return intersects(box2d);
    }

    /**
     * 移动
     */
    public void move() {
        if (direction == Double.NaN) {
            return;
        }
        if (direction == DIR_RIGHT) {
            move(v, 0);
        } else if (direction == DIR_RIGHTUP) {
            move(v * Oblique, -v * Oblique);
        } else if (direction == DIR_UP) {
            move(0, -v);
        } else if (direction == DIR_LEFTUP) {
            move(-v * Oblique, -v * Oblique);
        } else if (direction == DIR_LEFT) {
            move(-v, 0);
        } else if (direction == DIR_LEFTDOWN) {
            move(-v * Oblique, v * Oblique);
        } else if (direction == DIR_DOWN) {
            move(0, v);
        } else if (direction == DIR_RIGHTDOWN) {
            move(v * Oblique, v * Oblique);
        } else {
            move(v * cos, v * sin);
        }
    }

    /**
     * 向坐标轴正方向平移(vx,vy)
     */
    public void move(double vx, double vy) {
        translate(vx, vy);
        centerX += vx;
        centerY += vy;
    }

    public void onCrash() {
    }

    public void onCreate() {
    }

    public void onDestory() {
    }

    public void onReload() {
    }

    public void paint(Graphics2D g) {
        move();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.blue);
        g.fill(this);
        g.setColor(Color.yellow);
        g.fillOval((int) (getFocusX() - 6), (int) (getFocusY() - 6), 12, 12);
        g.setColor(Color.red);
        g.fillOval((int) (centerX - 4), (int) (centerY - 4), 8, 8);
    }

    /**
     * 重新加载包围盒
     */
    public void resetBounds() {
        // 获取包围多边形的矩形边框
        minX = maxX = points[0];
        minY = maxY = points[1];
        for (int i = 3; i < length; i += 2) {
            if (minX > points[i - 1]) {
                minX = points[i - 1];
            }
            if (minY > points[i]) {
                minY = points[i];
            }
            if (maxX < points[i - 1]) {
                maxX = points[i - 1];
            }
            if (maxY < points[i]) {
                maxY = points[i];
            }
        }
        focusX = (maxX - minX) / 2 + minX;
        focusY = (maxY - minY) / 2 + minY;
    }

    /**
     * 获取三角函数值
     */
    public void resetSinCos(double direction) {
        if (direction == DIR_CENTER) {
            cos = 1;
            sin = 0;
        } else if (direction == DIR_RIGHT) {
            cos = 1;
            sin = 0;
        } else if (direction == DIR_RIGHTUP) {
            cos = Oblique;
            sin = -Oblique;
        } else if (direction == DIR_UP) {
            cos = 0;
            sin = -1;
        } else if (direction == DIR_LEFTUP) {
            cos = -Oblique;
            sin = -Oblique;
        } else if (direction == DIR_LEFT) {
            cos = -1;
            sin = 0;
        } else if (direction == DIR_LEFTDOWN) {
            cos = -Oblique;
            sin = Oblique;
        } else if (direction == DIR_DOWN) {
            cos = 0;
            sin = 1;
        } else if (direction == DIR_RIGHTDOWN) {
            cos = Oblique;
            sin = Oblique;
        } else {
            cos = Math.cos(direction);
            sin = Math.sin(direction);
        }
    }

    public void rotate(double vecx, double vecy) {
        if (vecy == 0.0) {
            if (vecx < 0.0) {
                rotateTo(Math.PI);
            }
        } else if (vecx == 0.0) {
            if (vecy > 0.0) {
                rotateTo(Math.PI / 2);
            } else {
                rotateTo(Math.PI * 2 / 3);
            }
        } else {
            double len = Math.sqrt(vecx * vecx + vecy * vecy);
            double sin = vecy / len;
            double cos = vecx / len;
            double x, y;
            for (int i = 1; i < length; i += 2) {
                x = points[i - 1] * cos - points[i] * sin;
                y = points[i - 1] * sin + points[i] * cos;
                points[i - 1] = x;
                points[i] = y;
            }
        }
    }

    /**
     * 图形绕指定坐标(逆时针)旋转指定弧度
     *
     * @param cx    旋转中心的x
     * @param cy    旋转中心的y
     * @param theta 旋转的弧度
     */
    public void rotate(double cx, double cy, double theta) {
        double x, y;
        double sin = Math.sin(theta), cos = Math.cos(theta);
        for (int i = 1; i < length; i += 2) {
            x = points[i - 1] - cx;
            y = points[i] - cy;
            points[i - 1] = cx + x * cos - y * sin;
            points[i] = cy + x * sin + y * cos;
        }
    }

    /**
     * 图形绕中心旋转指定弧度
     *
     * @param theta 旋转的弧度
     */
    public void rotateTo(double theta) {
        if (theta == Double.NaN) {
            cos = 0;
            sin = 0;
            return;
        }
        double temp_x, temp_y;
        resetSinCos(theta - direction);
        for (int i = 1; i < length; i += 2) {
            temp_x = points[i - 1] - focusX;
            temp_y = points[i] - focusY;
            points[i - 1] = focusX + temp_x * cos - temp_y * sin;
            points[i] = focusY + temp_x * sin + temp_y * cos;
        }
        if (direction == 0) {
            direction = theta;
        } else {
            direction = theta;
            resetSinCos(direction);
        }
    }

    /**
     * 缩放
     */
    public void scale(double x, double y, double size) {
        if (size == 1 || size < 0) {
            return;
        }
        for (int i = 1; i < length; i += 2) {
            points[i - 1] = (points[i - 1] - x) * size + x;
            points[i] = (points[i] - y) * size + y;
        }
        minX = (minX - x) * size + x;
        minY = (minY - y) * size + y;
        maxX = (maxX - x) * size + x;
        maxY = (maxY - y) * size + y;
        focusX = (focusX - x) * size + x;
        focusY = (focusY - y) * size + y;
    }

    public void setDirection(double vecx, double vecy) {
        setDirection(ShapeUtil.getDirection(vecx - centerX, vecy - centerY));
    }

    /**
     * 设置重心坐标
     */
    public void setFocus(double focusX, double focusY) {
        this.focusX = focusX;
        this.focusY = focusY;
    }

    /**
     * 设置物体的朝向
     */
    public void setOrientation(double x, double y) {
        setOrientation(ShapeUtil.getDirection(x - centerX, y - centerY));
    }

    /**
     * 平移
     */
    public void translate(double vx, double vy) {
        for (int i = 1; i < length; i += 2) {
            points[i - 1] += vx;
            points[i] += vy;
        }
        minX += vx;
        minY += vy;
        maxX += vx;
        maxY += vy;
        focusX += vx;
        focusY += vy;
    }
}

class UnitIterator implements PathIterator {
    AffineTransform transform;
    int index;
    int npoints;
    double[] points;

    public UnitIterator(AffineTransform at, double[] points) {
        transform = at;
        this.points = points;
        npoints = points.length >> 1;
        if (npoints == 0) {
            index = 1;
        }
    }

    @Override
    public int currentSegment(double[] coords) {
        if (index >= npoints) {
            return SEG_CLOSE;
        }
        coords[0] = points[index << 1];
        coords[1] = points[(index << 1) + 1];
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, 1);
        }
        return index == 0 ? SEG_MOVETO : SEG_LINETO;
    }

    @Override
    public int currentSegment(float[] coords) {
        if (index >= npoints) {
            return SEG_CLOSE;
        }
        coords[0] = (float) points[index << 1];
        coords[1] = (float) points[(index << 1) + 1];
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, 1);
        }
        return index == 0 ? SEG_MOVETO : SEG_LINETO;
    }

    @Override
    public int getWindingRule() {
        return WIND_EVEN_ODD;
    }

    @Override
    public boolean isDone() {
        return index > npoints;
    }

    @Override
    public void next() {
        index++;
    }
}
