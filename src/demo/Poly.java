package demo;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * 多边形 给定的点必须有序能围成一个凸多边形
 */
public class Poly implements Cloneable, Shape {
    // 平移（Translation）、缩放（Scale）、翻转（Flip）、旋转（Rotation）和错切（Shear）
    public static final Poly Pentastar = new Poly(0, 100, 168, 130, 286, 10, 308, 176, 460, 250, 308, 324, 286, 490, 168,
            370, 0, 400, 80, 250);
    public static final Poly five = new Poly(0, 100, 286, 10, 460, 250, 286, 490, 0, 400);
    public static final Poly six = new Poly(0, 10, 17.320508075689, 0, 17.320508075689 * 2, 10, 17.320508075689 * 2, 30,
            17.320508075689, 40, 0, 30);
    private double[] points;
    private double focusX, focusY;
    private int length, minX, minY, maxX, maxY;

    public Poly(double... points) {
        init(points);
    }

    @Override
    public Poly clone() throws CloneNotSupportedException {
        Poly poly = (Poly) super.clone();
        poly.points = points.clone();
        return poly;
    }

    /**
     * 获取多边形的点数组
     */
    public double[] getPoints() {
        return points;
    }

    /**
     * 获取多边形的点数组
     */
    public int getPointsLength() {
        return length >> 1;
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
    public void init(double... points) {
        this.points = points;
        length = (points.length & 1) != 0 ? points.length - 1 : points.length;
        refresh();
        focusX = getCenterX();
        focusY = getCenterY();
    }

    /**
     * 刷新包围盒大小
     */
    public void refresh() {
        // 获取包围多边形的矩形边框
        minX = (int) points[0];
        minY = (int) points[1];
        maxX = (int) points[0];
        maxY = (int) points[1];
        for (int i = 1; i < length; i += 2) {
            if (minX > points[i - 1]) {
                minX = (int) points[i - 1];
            }
            if (minY > points[i]) {
                minY = (int) points[i];
            }
            if (maxX < points[i - 1]) {
                maxX = points[i - 1] > (int) points[i - 1] ? (int) points[i - 1] + 1 : (int) points[i - 1];
            }
            if (maxY < points[i]) {
                maxY = points[i] > (int) points[i] ? (int) points[i] + 1 : (int) points[i];
            }
        }
    }

    /**
     * 获取中心x坐标
     */
    public int getCenterX() {
        return minX + maxX >> 1;
    }

    /**
     * 获取中心y坐标
     */
    public int getCenterY() {
        return minY + maxY >> 1;
    }

    /**
     * 获取重心x坐标
     */
    public double getFocusX() {
        return focusX;
    }

    /**
     * 设置重心x坐标
     */
    public void setFocusX(double focusX) {
        this.focusX = focusX;
    }

    /**
     * 获取重心y坐标
     */
    public double getFocusY() {
        return focusY;
    }

    /**
     * 设置重心y坐标
     */
    public void setFocusY(double focusY) {
        this.focusY = focusY;
    }

    /**
     * 将多边形向坐标轴正方向平移(tx,ty)
     */
    public void translate(int tx, int ty) {
        for (int i = 1; i < length; i += 2) {
            points[i - 1] += tx;
            points[i] += ty;
        }
        minX += tx;
        minY += ty;
        maxX += tx;
        maxY += ty;
    }

    /**
     * 图形绕指定坐标(逆时针)旋转指定弧度
     *
     * @param cx    旋转中心的x
     * @param cy    旋转中心的y
     * @param theta 旋转的弧度
     */
    public void rotate(int cx, int cy, double theta) {
        double x, y, sin = StrictMath.sin(theta), cos = StrictMath.cos(theta);
        for (int i = 1; i < length; i += 2) {
            points[i - 1] -= cx;
            points[i] -= cy;
            x = cx + points[i - 1] * cos + points[i] * sin;
            y = cy - points[i - 1] * sin + points[i] * cos;
            points[i - 1] = x;
            points[i] = y;
        }
        refresh();
    }

    // TODO 图形绕指定直线翻转
    public void flip(int ax, int ay, int bx, int by) {
        for (int i = 1; i < length; i += 2) {
        }
        points[length] = points[0];
        points[length + 1] = points[1];
        refresh();
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

    /**
     * 缩放
     */
    public Poly scale(int x, int y, double size) {
        for (int i = 1; i < length; i += 2) {
            points[i - 1] = (points[i - 1] - x) * size + x;
            points[i] = (points[i] - y) * size + y;
        }
        refresh();
        return this;
    }

    /**
     * 缩放
     */
    public Poly scale(double size) {
        return scale(getCenterX(), getCenterY(), size);
    }

    public Poly setLocation(int x, int y) {
        translate(x - getCenterX(), y - getCenterY());
        return this;
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

    // 任意两个多边形的边是否相交
    public boolean intersects(Poly poly) {
        if (maxX < poly.minX || poly.maxX < minX || maxY < poly.minY || poly.maxY < minY) {
            return false;
        }
        double otherpoints[] = poly.points;
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
                dx = otherpoints[j - 3];
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

    // 两凸多边形是否有重叠
    public boolean intersects2(Poly poly) {
        if (maxX < poly.minX || maxX < poly.minX || maxY < poly.minY || maxY < poly.minY) {
            return false;
        }
        /**
         * 循环数组中的坐标依次去多边形上的一条边视作分离轴 计算多边形其余点和另一个多边形上所有点是否位于分离轴的两侧
         */
        double ax, ay, bx, by, px, py, ccw, temp;
        int i, j, slen = poly.points.length;
        // 多边形最后一条边是终点到起点的线
        ax = points[length - 2];
        ay = points[length - 1];
        bx = points[0] - ax;
        by = points[1] - ay;
        px = points[2] - ax;
        py = points[3] - ay;
        // 第二个点相对于多边形最后一条边(终点到起点的线)的位置
        ccw = px * by - py * bx;
        for (j = 1; j < slen; j += 2) {
            px = poly.points[j - 1] - ax;
            py = poly.points[j] - ay;
            temp = px * by - py * bx;
            // 另个多边形上点位于假定分离轴的同侧 此假定分离轴不可能为真分离轴
            if (temp * ccw >= 0) {
                break;
            }
        }
        // 当假定分离轴成功分离所有点时表示二者没有重叠
        if (j >= slen) {
            return false;
        }
        for (i = 3; i < length; i += 2) {
            /**
             * a b两点便是假定分离轴的线段两点坐标 p(待测点)是两个多边形上非a b两点以外的点 利用向量计算出p点在分离轴的位置
             */
            ax = points[i - 3];
            ay = points[i - 2];
            bx = points[i - 1] - ax;
            by = points[i] - ay;
            for (j = 1; j < slen; j += 2) {
                px = poly.points[j - 1] - ax;
                py = poly.points[j] - ay;
                temp = px * by - py * bx;
                if (temp * ccw >= 0) {
                    break;
                }
            }
            if (j >= slen) {
                return false;
            }
        }
        // 以下代码块重复了上面的功能只是换了个多边形取分离轴
        ax = poly.points[slen - 2];
        ay = poly.points[slen - 1];
        bx = poly.points[0] - ax;
        by = poly.points[1] - ay;
        px = poly.points[2] - ax;
        py = poly.points[3] - ay;
        ccw = px * by - py * bx;
        for (j = 1; j < length; j += 2) {
            px = points[j - 1] - ax;
            py = points[j] - ay;
            temp = px * by - py * bx;
            if (temp * ccw >= 0) {
                break;
            }
        }
        if (j >= length) {
            return false;
        }
        for (i = 3; i < slen; i += 2) {
            ax = poly.points[i - 3];
            ay = poly.points[i - 2];
            bx = poly.points[i - 1] - ax;
            by = poly.points[i] - ay;
            for (j = 1; j < length; j += 2) {
                px = points[j - 1] - ax;
                py = points[j] - ay;
                temp = px * by - py * bx;
                if (temp * ccw >= 0) {
                    break;
                }
            }
            if (j >= length) {
                return false;
            }
        }
        return true;
    }

    public void paint(Graphics g) {
        for (int i = 3, length = points.length; i <= length; i += 2) {
            g.drawLine(points[i - 3] + 0.5 >= (int) points[i - 3] + 1 ? (int) points[i - 3] + 1 : (int) points[i - 3],
                    points[i - 2] + 0.5 >= (int) points[i - 2] + 1 ? (int) points[i - 2] + 1 : (int) points[i - 2], points[i - 1]
                            + 0.5 >= (int) points[i - 1] + 1 ? (int) points[i - 1] + 1 : (int) points[i - 1], points[i]
                            + 0.5 >= (int) points[i] + 1 ? (int) points[i] + 1 : (int) points[i]);
        }
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

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
        if (maxX < x || w < minX || maxY < y || h < minY) {
            return false;
        }
        double ax, ay, bx, by, cx, cy, dx, dy;
        double abac, abad, cdca, cdcb;
        cx = x;
        cy = y;
        dx = x + w;
        dy = y;
        for (int i = 0; i < 4; i++, cx = dx, cy = dy) {
            switch (i) {
                case 1:
                    dx = x + w;
                    dy = y + h;
                    break;
                case 2:
                    dx = x;
                    dy = y + h;
                    break;
                case 3:
                    dx = x;
                    dy = y;
            }
            ax = points[length - 2];
            ay = points[length - 1];
            for (int j = 1; j < length; j += 2, ax = bx, ay = by) {
                bx = points[j - 1];
                by = points[j];
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

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && !intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new UnitIterator(at, points);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }

}

class PolygonPathIterator implements PathIterator {
    AffineTransform transform;
    int index;
    int npoints;
    double[] points;

    public PolygonPathIterator(AffineTransform at, double[] points) {
        transform = at;
        this.points = points;
        npoints = points.length >> 1;
        if (npoints == 0) {
            index = 1;
        }
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
}
