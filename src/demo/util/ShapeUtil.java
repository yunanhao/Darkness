package demo.util;

import demo.bean.Vector;

import java.awt.geom.Point2D;

/**
 * 图形碰撞检测
 */
public interface ShapeUtil {
    /**
     * 斜向运动时速率应乘以的倍数
     */
    double Oblique = 0.70710678118654752440084436210485;
    /**
     * 一度所对应的弧度
     */
    double Degre_One = 0.01745329251994329576923690768489;
    /**
     * 方向为左的二进制状态码
     */
    int LEFT = 0B1000;
    /**
     * 方向为右的二进制状态码
     */
    int RIGHT = 0B0010;
    /**
     * 方向为上的二进制状态码
     */
    int UP = 0B0100;
    /**
     * 方向为下的二进制状态码
     */
    int DOWN = 0B0001;
    int OUT_LEFT = 0B0001;
    int OUT_TOP = 0B0010;
    int OUT_RIGHT = 0B0100;
    int OUT_BOTTOM = 0B1000;
    /**
     * 方向为右所对应的弧度
     */
    double DIR_RIGHT = 0;
    /**
     * 方向为右上所对应的弧度
     */
    double DIR_RIGHTUP = Math.PI * 7 / 4;
    /**
     * 方向为正上所对应的弧度
     */
    double DIR_UP = Math.PI * 3 / 2;
    /**
     * 方向为左上所对应的弧度
     */
    double DIR_LEFTUP = Math.PI * 5 / 4;
    /**
     * 方向为正左所对应的弧度
     */
    double DIR_LEFT = Math.PI;
    /**
     * 方向为左下所对应的弧度
     */
    double DIR_LEFTDOWN = Math.PI * 3 / 4;
    /**
     * 方向为正下所对应的弧度
     */
    double DIR_DOWN = Math.PI / 2;
    /**
     * 方向为右下所对应的弧度
     */
    double DIR_RIGHTDOWN = Math.PI / 4;

    /**
     * 返回从点到线的距离的平方。测量的距离是指定点与位于指定坐标定义的无限延长线上的最近点之间的距离。 如果指定点与线相交，则此方法返回 0.0。
     *
     * @param x1 the X coordinate of the start point of the specified line
     * @param y1 the Y coordinate of the start point of the specified line
     * @param x2 the X coordinate of the end point of the specified line
     * @param y2 the Y coordinate of the end point of the specified line
     * @param px the X coordinate of the specified point being measured against the
     *           specified line
     * @param py the Y coordinate of the specified point being measured against the
     *           specified line
     * @return a int value that is the square of the distance from the specified
     * point to the specified line.
     * @see #ptSegDistSq(int, int, int, int, int, int)
     * @since 1.2
     */
    static int ptLineDistSq(final int x1, final int y1, int x2, int y2, int px, int py) {
        // Adjust vectors relative to x1,y1
        // x2,y2 becomes relative vector from x1,y1 to end of segment
        x2 -= x1;
        y2 -= y1;
        // px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        final int dotprod = px * x2 + py * y2;
        // dotprod is the length of the px,py vector
        // projected on the x1,y1=>x2,y2 vector times the
        // length of the x1,y1=>x2,y2 vector
        final int projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
        // Distance to line is now the length of the relative point
        // vector minus the length of its projection onto the line
        int lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return lenSq;
    }

    /**
     * 返回一个指示符，指示指定点 (px,py) 相对于从 (x1,y1) 到 (x2,y2) 的线段的位置。返回值可以为 1、-1 或 0，指示为了指向指定点
     * (px,py)，指定的线段必须绕其第一个端点 (x1,y1) 旋转的方向。 返回值 1 指示线段转动的方向是从 X 正半轴到 Y 负半轴。在 Java
     * 2D 使用的默认坐标系中，此方向为逆时针方向。 返回值 -1 指示线段转动的方向是从 X 正半轴到 Y 正半轴。在默认的坐标系中，此方向为顺时针方向。
     * 返回值 0 指示点恰好位于线段上。注意，指示符 0 是非常罕见的，并且因浮点舍入问题而不可用于确定共线性。
     * 如果点与线段共线，但是不在端点之间，则点位于“(x1,y1) 之外”时值为 -1，点位于“(x2,y2) 之外”时值为 1。
     *
     * @param ax 指定线段起始点的 X 坐标
     * @param ay 指定线段起始点的Y 坐标
     * @param bx 指定线段结束点的 X 坐标
     * @param by 指定线段结束点的 Y 坐标
     * @param px 将与指定线段比较的指定点的 X 坐标
     * @param py 将与指定线段比较的指定点的Y 坐标
     * @return 一个整数，指示第三个指定坐标相对于前两个指定坐标所形成线段的位置。
     * @since 1.2
     */
    static int relativeCCW(final int ax, final int ay, int bx, int by, int px, int py) {
        bx -= ax;
        by -= ay;
        px -= ax;
        py -= ay;
        int ccw = px * by - py * bx;
        if (ccw == 0) {
            // 这一点是共线的，分类的基础上的点落在段侧。
            // 我们可以用PX的投影计算相对值，
            // PY到段负值表示点项目在用作投影起源的特定端点的方向外段。
            ccw = px * bx + py * by;
            if (ccw > 0) {
                // 反向投影是相对于原来的X2，Y2 X2，Y2根本否定。px和py需要（X2
                // X1）或（Y2—Y1）减去他们（基于原始值）因为我们真的很想得到一个肯定的回答时，
                // 关键是“超越（x2，y2）”，然后我们要计算逆反正我们离开X2和Y2否定
                px -= bx;
                py -= by;
                ccw = px * bx + py * by;
                if (ccw < 0) {
                    ccw = 0;
                }
            }
        }
        return ccw < 0 ? -1 : ccw > 0 ? 1 : 0;
    }

    /**
     * 检测圆形对碰
     *
     * @param x1 圆形1 的圆心X 坐标
     * @param y1 圆形2 的圆心X 坐标
     * @param r1 圆形1 的半径
     * @param x2 圆形1 的圆心Y 坐标
     * @param y2 圆形2 的圆心Y 坐标
     * @param r2 圆形2 的半径
     * @return
     */
    static boolean isCollisionCircle(final int x1, final int y1, final int r1, final int x2, final int y2,
                                     final int r2) {
        // 如果两圆的圆心距小于或等于两圆半径则认为发生碰撞
        return Math.hypot(x1 - x2, y1 - y2) <= r1 + r2;
    }

    /**
     * java原生方法检测两个矩形区域是否有碰撞关系 使用时矩形长长必须为正
     *
     * @param x1 第一个矩形的X
     * @param y1 第一个矩形的Y
     * @param l1 第一个矩形的长
     * @param w1 第一个矩形的宽
     * @param x2 第二个矩形的X
     * @param y2 第二个矩形的Y
     * @param l2 第二个矩形的长
     * @param w2 第二个矩形的宽
     */
    static boolean isCollisions(final int x1, final int y1, int l1, int w1, final int x2, final int y2, int l2,
                                int w2) {
        // 至少一个维度是负的
        if ((l1 | w1 | l2 | w2) < 0) {
            return false;
        }
        // 注：如果任何维度为零，下面的测试必须返回假
        if (x2 < x1 || y2 < y1) {
            return false;
        }
        l1 += x1;
        l2 += x2;
        if (l2 <= x2) {
            // X+W溢出或W为零，如果原w为零或X+W没有溢出或溢出的X+W小于溢出X+W返回false
            if (l1 >= x1 || l2 > l1) {
                return false;
            }
        } else {
            // X+W没有溢出 并且不是零 如果 原来的 w 是0或者 x+w 没有溢出x+w 比X+W小返回假
            if (l1 >= x1 && l2 > l1) {
                return false;
            }
        }
        w1 += y1;
        w2 += y2;
        if (w2 <= y2) {
            return w1 < y1 && w2 <= w1;
        } else {
            return w1 < y1 || w2 <= w1;
        }
    }

    /**
     * 多边形是否包含点
     */
    static boolean contains(double x, double y, double... points) {
        int hits = 0;
        double lastx = points[points.length - 2];
        double lasty = points[points.length - 1];
        double curx, cury;
        for (int i = 1; i < points.length; lastx = curx, lasty = cury, i += 2) {
            curx = points[i - 1];
            cury = points[i];
            if (cury == lasty) {
                continue;
            }
            double leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }
            double test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;
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
    static boolean contains2(double px, double py, double... points) {
        double sx, sy, ex, ey, ccw0, ccw1, temp;
        int hits = 0;
        sx = points[points.length - 2];
        sy = points[points.length - 1];
        for (int i = 1; i < points.length; i += 2, sx = ex, sy = ey) {
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

    /**
     * 检测两个3D长方体是否相交
     */
    static boolean contains3D(int x, int l, int y, int w, int z, int h, int sx, int sl, int sy, int sw, int sz,
                              int sh) {
        if (l < 0) {
            x += l;
            l = -l;
        }
        if (w < 0) {
            y += w;
            w = -w;
        }
        if (h < 0) {
            z += h;
            h = -h;
        }
        if (sl < 0) {
            sx += sl;
            sl = -sl;
        }
        if (sw < 0) {
            sy += sw;
            sw = -sw;
        }
        if (sh < 0) {
            sz += sh;
            sh = -sh;
        }
        return x >= sx && x + l <= sx + sl && y >= sy && y + w <= sy + sw && z >= sz && z + h <= sz + sh || x <= sx && x
                + l >= sx + sl && y <= sy && y + w >= sy + sw && z <= sz && z + h >= sz + sh;
    }

    /**
     * 检测两个单位在3D空间中是否存在重合的部分 有可能一个物体被另一个包含
     *
     * @param sx
     * @param sl
     * @param sy
     * @param sw
     * @param sz
     * @param sh
     */
    static boolean intersect3D(int x, int l, int y, int w, int z, int h, int sx, int sl, int sy, int sw, int sz,
                               int sh) {
        if (l < 0) {
            x += l;
            l = -l;
        }
        if (w < 0) {
            y += w;
            w = -w;
        }
        if (h < 0) {
            z += h;
            h = -h;
        }
        if (sl < 0) {
            sx += sl;
            sl = -sl;
        }
        if (sw < 0) {
            sy += sw;
            sw = -sw;
        }
        if (sh < 0) {
            sz += sh;
            sh = -sh;
        }
        return !(x > sx + sl || sx > x + l || y > sy + sw || sy > y + w || z > sz + sh || sz > z + h);
    }

    /**
     * 基于AST分离轴算法判断两个凸多边形是否重叠 注意:两个数组均以(x,y)的形式将坐标存储到数组中
     *
     * @param point1 第一个凸多边形的坐标数组
     * @param point2 第二个凸多边形的坐标数组
     */
    static boolean isOverlap2D(int[] point1, int[] point2) {
        // 先利用AABB包围盒进行粗略检测
        int minX1 = point1[0], minY1 = point1[1], maxX1 = point1[0], maxY1 = point1[1];
        int minX2 = point2[0], minY2 = point2[1], maxX2 = point2[0], maxY2 = point2[1];
        // 获取两个凸多边形的最大(x,y)和最小(x,y)以便于生成AABB包围盒
        for (int i = 3; i < point1.length; i += 2) {
            if (minX1 > point1[i - 1]) {
                minX1 = point1[i - 1];
            }
            if (minY1 > point1[i]) {
                minY1 = point1[i];
            }
            if (maxX1 < point1[i - 1]) {
                maxX1 = point1[i - 1];
            }
            if (maxY1 < point1[i]) {
                maxY1 = point1[i];
            }
        }
        for (int j = 3; j < point2.length; j += 2) {
            if (minX2 > point2[j - 1]) {
                minX2 = point2[j - 1];
            }
            if (minY2 > point2[j]) {
                minY2 = point2[j];
            }
            if (maxX2 < point2[j - 1]) {
                maxX2 = point2[j - 1];
            }
            if (maxY2 < point2[j]) {
                maxY2 = point2[j];
            }
        }
        if (maxX1 < minX2 || maxX2 < minX1 || maxY1 < minY2 || maxY2 < minY1) {
            return false;
        }
        // 当AABB包围盒检测到有碰撞发生时就使用分离轴继续检测
        int i, j, len1 = point1.length, len2 = point2.length, ccw, temp, ax, ay, bx, by, px, py;
        // 多边形最后一条边是终点到起点的线
        ax = point1[len1 - 2];
        ay = point1[len1 - 1];
        bx = point1[0] - ax;
        by = point1[1] - ay;
        px = point1[2] - ax;
        py = point1[3] - ay;
        // 第二个点相对于多边形最后一条边(终点到起点的线)的位置
        ccw = px * by - py * bx;
        for (j = 1; j < len2; j += 2) {
            px = point2[j - 1] - ax;
            py = point2[j] - ay;
            temp = px * by - py * bx;
            // 另个多边形上点位于假定分离轴的同侧 此假定分离轴不可能为真分离轴
            if ((temp ^ ccw) >= 0) {
                break;
            }
        }
        // 当假定分离轴成功分离所有点时表示二者没有重叠
        if (j >= len2) {
            return false;
        }
        /**
         * 循环数组中的坐标依次去多边形上的一条边视作分离轴 计算多边形其余点和另一个多边形上所有点是否位于分离轴的两侧
         */
        for (i = 3; i < len1; i += 2) {
            /**
             * a b两点便是假定分离轴的线段两点坐标 p(待测点)是两个多边形上非a b两点以外的点 利用向量计算出p点在分离轴的位置
             */
            ax = point1[i - 3];
            ay = point1[i - 2];
            bx = point1[i - 1] - ax;
            by = point1[i] - ay;
            for (j = 1; j < len2; j += 2) {
                px = point2[j - 1] - ax;
                py = point2[j] - ay;
                temp = px * by - py * bx;
                if ((temp ^ ccw) >= 0) {
                    break;
                }
            }
            if (j >= len2) {
                return false;
            }
        }
        // 以下代码块重复了上面的功能只是换了个多边形取分离轴
        ax = point2[len2 - 2];
        ay = point2[len2 - 1];
        bx = point2[0] - ax;
        by = point2[1] - ay;
        px = point2[2] - ax;
        py = point2[3] - ay;
        ccw = px * by - py * bx;
        for (j = 1; j < len1; j += 2) {
            px = point1[j - 1] - ax;
            py = point1[j] - ay;
            temp = px * by - py * bx;
            if ((temp ^ ccw) >= 0) {
                break;
            }
        }
        if (j >= len1) {
            return false;
        }
        for (i = 3; i < len2; i += 2) {
            ax = point2[i - 3];
            ay = point2[i - 2];
            bx = point2[i - 1] - ax;
            by = point2[i] - ay;
            for (j = 1; j < len1; j += 2) {
                px = point1[j - 1] - ax;
                py = point1[j] - ay;
                temp = px * by - py * bx;
                if ((temp ^ ccw) >= 0) {
                    break;
                }
            }
            if (j >= len1) {
                return false;
            }
        }
        return true;
    }

    // 任意两个多边形的边是否相交
    static boolean intersects(int[] point1, int[] point2) {
        // 先利用AABB包围盒进行粗略检测
        double ax = point1[0], ay = point1[1], bx = point1[0], by = point1[1];
        double cx = point2[0], cy = point2[1], dx = point2[0], dy = point2[1];
        // 获取两个多边形的最大(x,y)和最小(x,y)以便于生成AABB包围盒
        for (int i = 3; i < point1.length; i += 2) {
            if (ax > point1[i - 1]) {
                ax = point1[i - 1];
            }
            if (ay > point1[i]) {
                ay = point1[i];
            }
            if (bx < point1[i - 1]) {
                bx = point1[i - 1];
            }
            if (by < point1[i]) {
                by = point1[i];
            }
        }
        for (int j = 3; j < point2.length; j += 2) {
            if (cx > point2[j - 1]) {
                cx = point2[j - 1];
            }
            if (cy > point2[j]) {
                cy = point2[j];
            }
            if (dx < point2[j - 1]) {
                dx = point2[j - 1];
            }
            if (dy < point2[j]) {
                dy = point2[j];
            }
        }
        if (bx < cx || dx < ax || by < cy || dy < ay) {
            return false;
        }
        int i, j, len = point1.length, len2 = point2.length;
        for (i = 3; i < len; i += 2) {
            if (point1[i - 3] > point1[i - 1]) {
                ax = point1[i - 1];
                bx = point1[i - 3];
            } else {
                ax = point1[i - 3];
                bx = point1[i - 1];
            }
            if (point1[i] > point1[i - 2]) {
                ay = point1[i - 2];
                by = point1[i];
            } else {
                ay = point1[i];
                by = point1[i - 2];
            }
            for (j = 3; j < len2; j += 2) {
                if (point2[j - 3] > point2[j - 1]) {
                    cx = point2[j - 1];
                    dx = point2[j - 3];
                } else {
                    cx = point2[j - 3];
                    dx = point2[j - 1];
                }
                if (point2[j] > point2[j - 2]) {
                    cy = point2[j - 2];
                    dy = point2[j];
                } else {
                    cy = point2[j];
                    dy = point2[j - 2];
                }
                if (bx < cx || by < cy || dx < ax || dy < ay) {
                    continue;
                }
                ax = (point1[i - 1] - point1[i - 3]) * (point2[j - 2] - point1[i - 2]) - (cx - point1[i - 3]) * (point1[i]
                        - point1[i - 2]);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
                bx = (point1[i - 1] - point1[i - 3]) * (point2[j] - point1[i - 2]) - (point2[j - 1] - point1[i - 3])
                        * (point1[i] - point1[i - 2]);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
                cx = (point2[j - 1] - point2[j - 3]) * (point1[i - 2] - point2[j - 2]) - (point1[i - 3] - point2[j - 3])
                        * (point2[j] - point2[j - 2]);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
                dx = (point2[j - 1] - point2[j - 3]) * (point1[i] - point2[j - 2]) - (point1[i - 1] - point2[j - 3])
                        * (point2[j] - point2[j - 2]);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
                if ((ax >= 0 && bx <= 0 || ax <= 0 && bx >= 0) && (cx >= 0 && dx <= 0 || cx <= 0 && dx >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取多边形重心
     */
    static Vector getGravityCenter(Vector gravity, double... points) {
        double sum_area = 0, sum_x = 0, sum_y = 0;
        double sx = points[0];
        double sy = points[1];
        double ax = points[2];
        double ay = points[3];
        double bx, by, temp_area;
        for (int i = 5; i < points.length; i += 2) {
            bx = points[i - 1];
            by = points[i];
            temp_area = (ax - sx) * (by - sy) - (bx - sx) * (ay - sy);
            sum_area += temp_area;
            sum_x += (sx + ax + bx) * temp_area;
            sum_y += (sy + ay + by) * temp_area;
            ax = bx;
            ay = by;
        }
        sum_area = 1.0 / sum_area / 3;
        if (gravity == null) {
            gravity = new Vector(sum_x * sum_area, sum_y * sum_area);
        } else {
            gravity.set(sum_x * sum_area, sum_y * sum_area);
        }
        return gravity;
    }

    /**
     * 得到点p关于线段a-b的垂足坐标
     */
    static Vector getPedal(double ax, double ay, double bx, double by, double px, double py) {
        bx -= ax;
        by -= ay;
        px -= ax;
        py -= ay;
        double k = (px * bx + py * by) / (bx * bx + by * by);
        return new Vector(bx * k + ax, by * k + ay);
    }

    /**
     * 获取一个点到另一线段所在直线的垂足坐标
     */
    static Vector getPoint(final double ax, double ay, final double bx, final double by, final double px,
                           final double py) {
        double dotprod = (px - ax) * (bx - ax) + (py - ay) * (by - ay);
        double projlenSq = dotprod * dotprod / ((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
        double pc2 = (px - ax) * (px - ax) + (py - ay) * (py - ay) - projlenSq;
        if (pc2 < 0) {
            pc2 = 0;
        }
        double pa2 = (px - ax) * (px - ax) + (py - ay) * (py - ay);
        double pb2 = (px - bx) * (px - bx) + (py - by) * (py - by);
        double ab = Math.hypot(ax - bx, ay - by);
        if (ab != 0) {
            double ac = Math.sqrt(pa2 - pc2);
            double bc = Math.sqrt(pb2 - pc2);
            double abx = ax - bx;
            if (abx < 0) {
                abx = -abx;
            }
            double aby = ay - by;
            if (aby < 0) {
                aby = -aby;
            }
            double acx = abx * ac / ab;
            double acy = aby * ac / ab;
            if (bc > ab && bc > ac) {
                double cx = ax + (bx > ax ? -acx : acx);
                double cy = ay + (by > ay ? -acy : acy);
                return new Vector(cx, cy);
            } else {
                double cx = ax + (bx > ax ? acx : -acx);
                double cy = ay + (by > ay ? acy : -acy);
                return new Vector(cx, cy);
            }
        }
        return null;
    }

    /**
     * 获取点p到线段a-b垂线长度的平方
     */
    static double getDistanceOfLine(double ax, double ay, double bx, double by, double px, double py) {
        bx -= ax;
        by -= ay;
        px -= ax;
        py -= ay;
        double dotprod = px * bx + py * by;
        System.out.println("dotprod:" + dotprod);
        double projlenSq = dotprod * dotprod / (bx * bx + by * by);
        System.out.println("projlenSq:" + projlenSq);
        double lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return lenSq;
    }

    /**
     * 获取点p到线段a-b距离的平方
     */
    static double getDistanceOfSegment(double ax, double ay, double bx, double by, double px, double py) {
        bx -= ax;
        by -= ay;
        px -= ax;
        py -= ay;
        double dotprod = px * bx + py * by;
        double projlenSq;
        if (dotprod <= 0.0) {
            projlenSq = 0.0;
        } else {
            px = bx - px;
            py = by - py;
            dotprod = px * bx + py * by;
            if (dotprod <= 0.0) {
                projlenSq = 0.0;
            } else {
                projlenSq = dotprod * dotprod / (bx * bx + by * by);
            }
        }
        double lenSq = px * px + py * py - projlenSq;
        if (lenSq < 0) {
            lenSq = 0;
        }
        return lenSq;
    }

    /**
     * 在从a到b为方向的射线上，距离a点distanceToA的点
     */
    static Vector extentPoint(Vector a, Vector b, double distanceToA) {
        double disX = b.getX() - a.getX();
        double disY = b.getY() - a.getY();
        double dis = Math.sqrt(disX * disX + disY * disY);
        double sin = (b.getY() - a.getY()) / dis;
        double cos = (b.getX() - a.getX()) / dis;
        double deltaX = distanceToA * cos;
        double deltaY = distanceToA * sin;
        return new Vector(a.getX() + deltaX, a.getY() + deltaY);
    }

    /**
     * 获取入射向量i关于平面向量n的反射向量
     */
    static Vector getReflexVector(Vector i, Vector n) {
        double temp = i.dot(n) / n.lengthSquared();
        Vector vector = n.clone().multiply(temp).negate().multiply(2).add(i);
        return vector;
    }

    /**
     * 获取入射向量a-b关于平面向量c-d的反射向量
     */
    static Vector getReflexVector(double ax, double ay, double bx, double by, double cx, double cy, double dx,
                                  double dy) {
        bx -= ax;
        by -= ay;
        dx -= cx;
        dy -= cy;
        double nx = -dy;
        double ny = dx;
        double temp = (bx * nx + by * ny) * 2 / (nx * nx + ny * ny) * 2;
        double x = bx - temp * nx;
        double y = by - temp * ny;
        return new Vector(x, y);
    }

    /**
     * 根据入射向量和反射面获取反射向量
     */
    static Vector getReflexVector2(double vx, double vy, double bx, double by) {
        if (bx == 0) {
            return new Vector(-vx, vy);// 反射面为Y轴
        }
        if (by == 0) {
            return new Vector(vx, -vy);// 反射面为X轴
        }
        double length = 2 * (vy * bx - vx * by) / (bx * bx + by * by);
        vx -= -by * length;
        vy -= bx * length;
        // System.out.println("反射向量("+vx+","+vy+")");
        return new Vector(vx, vy);
    }

    /**
     * 获取入射向量a-b关于平面向量c-d的反射向量
     */
    static Vector getReflexVector3(double ax, double ay, double bx, double by, double cx, double cy, double dx,
                                   double dy) {
        double in = (ax - bx) * (cy - dy) + (ay - by) * (dx - cx);
        double n2 = (dy - cy) * (dy - cy) + (dx - cx) * (dx - cx);
        double temp = 2 * in / n2;
        double x = bx - ax - temp * (dy - cy);
        double y = by - ay + temp * (dx - cx);
        return new Vector(x, y);
    }

    /**
     * 两条线段是否有交点
     */
    static boolean intersects(double ax, double ay, double bx, double by, double cx, double cy, double dx,
                              double dy) {
        double minx, miny, maxx, maxy;
        double abac, abad, cdca, cdcb;
        if (ax > bx) {
            minx = bx;
            maxx = ax;
        } else {
            minx = ax;
            maxx = bx;
        }
        if (ay > by) {
            miny = by;
            maxy = ay;
        } else {
            miny = ay;
            maxy = by;
        }
        if (cx > dx) {
            abac = dx;
            cdca = cx;
        } else {
            abac = cx;
            cdca = dx;
        }
        if (cy > dy) {
            abad = dy;
            cdcb = cy;
        } else {
            abad = cy;
            cdcb = dy;
        }
        if (maxx < abac || maxy < abad || cdca < minx || cdcb < miny) {
            return false;
        }
        abac = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
        abad = (bx - ax) * (dy - ay) - (dx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
        cdca = (dx - cx) * (ay - cy) - (ax - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
        cdcb = (dx - cx) * (by - cy) - (bx - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
        if (abac == 0 || abad == 0 || cdca == 0 || cdcb == 0) {
            return true;
        }
        return (abac > 0 && abad < 0 || abac < 0 && abad > 0) && (cdca > 0 && cdcb < 0 || cdca < 0 && cdcb > 0);
    }

    /**
     * 获得两线段的交点
     */
    static double[] getPoint(final double ax, double ay, final double bx, final double by, final double cx,
                             final double cy, double dx, double dy) {
        // 三角形abc 面积的2倍
        double area_abc = (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
        // 三角形abd 面积的2倍
        double area_abd = (ax - dx) * (by - dy) - (ay - dy) * (bx - dx);
        // 面积符号相同则两点在线段同侧,不相交 (对点在线段上的情况,本例当作不相交处理);
        if (area_abc >= 0 && area_abd >= 0 || area_abc <= 0 && area_abd <= 0) {
            return null;
        }
        // 三角形cda 面积的2倍
        double area_cda = (cx - ax) * (dy - ay) - (cy - ay) * (dx - ax);
        // 三角形cdb 面积的2倍
        // 注意: 这里有一个小优化.不需要再用公式计算面积,而是通过已知的三个面积加减得出.
        double area_cdb = area_cda + area_abc - area_abd;
        if (area_cda >= 0 && area_cdb >= 0 || area_cda <= 0 && area_cdb <= 0) {
            return null;
        }
        // 计算交点坐标
        double t = area_cda / (area_abd - area_abc);
        dx = t * (bx - ax);
        dy = t * (by - ay);
        dx += ax;
        dy += ay;
        return new double[]{dx, dy};
    }

    /**
     * 获得两线段的交点
     */
    static double[] getIntersection(final double ax, double ay, final double bx, final double by, final double cx,
                                    final double cy, double dx, double dy) {
        double s1 = (by - ay) * (dx - ax) - (bx - ax) * (dy - ay);
        double s2 = -((cx - ax) * (by - ay) - (cy - ay) * (bx - ax));
        double px = (cx * s1 + dx * s2) / (s1 + s2);
        double py = (cy * s1 + dy * s2) / (s1 + s2);
        return new double[]{px, py};
    }

    /**
     * 求两条直线交点(运用定比分点)
     */
    static Vector getIntersectionPoint(double ax, double ay, double bx, double by, double cx, double cy, double dx,
                                       double dy) {
        double area_abc = (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
        double area_abd = (ax - dx) * (by - dy) - (ay - dy) * (bx - dx);
        double area_cda = (cx - ax) * (dy - ay) - (cy - ay) * (dx - ax);
        double t = area_cda / (area_abd - area_abc);
        if (t >= 0.0 && t <= 1.0) {
            // 交点落在线段上
        }
        return new Vector(ax + t * (bx - ax), ay + t * (by - ay));
    }

    /**
     * 求两条直线交点(行列式)
     */
    static Vector getIntersectionPoint2(double ax, double ay, double bx, double by, double cx, double cy,
                                        double dx, double dy) {
        double D = (bx - ax) * (cy - dy) - (dx - cx) * (ay - by);
        double D1 = (cy * dx - cx * dy) * (bx - ax) - (ay * bx - ax * by) * (dx - cx);
        double D2 = (ay * bx - ax * by) * (cy - dy) - (cy * dx - cx * dy) * (ay - by);
        return new Vector(D1 / D, D2 / D);
    }

    /**
     * 求两条直线交点
     */
    static Vector getIntersectionPoint3(double ax, double ay, double bx, double by, double cx, double cy,
                                        double dx, double dy) {
        // 如果分母为0 则平行或共线, 不相交
        double denominator = (by - ay) * (dx - cx) - (ax - bx) * (cy - dy);
        if (denominator == 0) {
            return null;
        }
        denominator = 1 / denominator;
        // 线段所在直线的交点坐标 (x , y)
        double x = ((bx - ax) * (dx - cx) * (cy - ay) + (by - ay) * (dx - cx) * ax - (dy - cy) * (bx - ax) * cx)
                * denominator;
        double y = -((by - ay) * (dy - cy) * (cx - ax) + (bx - ax) * (dy - cy) * ay - (dx - cx) * (by - ay) * cy)
                * denominator;
        return new Vector(x, y);
    }

    /**
     * 若点a大于点b,即点a在点b顺时针方向,返回true,否则返回false
     */
    static boolean PointCmp(Vector a, Vector b, Vector center) {
        if (a.x >= 0 && b.x < 0) {
            return true;
        }
        if (a.x == 0 && b.x == 0) {
            return a.y > b.y;
        }
        // 向量OA和向量OB的叉积
        double det = (a.x - center.x) * (b.y - center.y) - (b.x - center.x) * (a.y - center.y);
        if (det < 0) {
            return true;
        }
        if (det > 0) {
            return false;
        }
        // 向量OA和向量OB共线，以距离判断大小
        double d1 = (a.x - center.x) * (a.x - center.x) + (a.y - center.y) * (a.y - center.y);
        double d2 = (b.x - center.x) * (b.x - center.y) + (b.y - center.y) * (b.y - center.y);
        return d1 > d2;
    }

    static void ClockwiseSortPoints(Vector[] vPoints) {
        // 计算重心
        Vector center = new Vector();
        double x = 0, y = 0;
        for (int i = 0; i < vPoints.length; i++) {
            x += vPoints[i].x;
            y += vPoints[i].y;
        }
        center.x = x / vPoints.length;
        center.y = y / vPoints.length;
        // 冒泡排序
        for (int i = 0; i < vPoints.length - 1; i++) {
            for (int j = 0; j < vPoints.length - i - 1; j++) {
                if (PointCmp(vPoints[j], vPoints[j + 1], center)) {
                    Vector tmp = vPoints[j];
                    vPoints[j] = vPoints[j + 1];
                    vPoints[j + 1] = tmp;
                }
            }
        }
    }

    /**
     * 获取一个int数字二进制的某位数的布尔表现形式
     *
     * @param number :要获取二进制值的数
     * @param index  :第一位为1，依次类推 移动的位数超过了该类型的最大位数， 那么编译器会对移动的位数取模
     */
    static boolean getBooleanByIndex(final int number, final int index) {
        return (number >> 32 - index & 1) == 1;
    }

    /**
     * 将一个int数字二进制的某位数改变
     *
     * @param number :要设置二进制值的数
     * @param index  :第一位为1，依次类推 移动的位数超过了该类型的最大位数， 那么编译器会对移动的位数取模
     * @return 修改之后的数值
     */
    static int setBooleanByIndex(final int number, final int index, final boolean flag) {
        if (flag) {
            return 1 << 32 - index | number;
        } else {
            return ~(1 << 32 - index) & number;
        }
    }

    /// <summary>
    /// 根据余弦定理求两个线段夹角
    /// </summary>
    /// <param name="o">端点</param>
    /// <param name="s">start点</param>
    /// <param name="e">end点</param>
    /// <returns></returns>
    static double Angle(double ox, double oy, double sx, double sy, double ex, double ey) {
        double cosfi = 0, fi = 0, norm = 0;
        double dsx = sx - ox;
        double dsy = sy - oy;
        double dex = ex - ox;
        double dey = ey - oy;
        cosfi = dsx * dex + dsy * dey;
        norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);
        cosfi /= Math.sqrt(norm);
        if (cosfi >= 1.0) {
            return 0;
        }
        if (cosfi <= -1.0) {
            return Math.PI;
        }
        fi = Math.acos(cosfi);

        if (180 * fi / Math.PI < 180) {
            return 180 * fi / Math.PI;
        } else {
            return 360 - 180 * fi / Math.PI;
        }
    }

    /**
     * 根据角度返回象限
     */
    static int gerQuadrantByRadian(final double orientation) {
        if (orientation > 0 && orientation < Math.PI / 2) {
            return 1;
        } else if (orientation > Math.PI / 2 && orientation < Math.PI) {
            return 2;
        } else if (orientation > Math.PI && orientation < Math.PI * 3 / 2) {
            return 3;
        } else if (orientation > Math.PI * 3 / 2 && orientation < Math.PI * 2) {
            return 4;
        } else {
            return 0;
        }
    }

    /**
     * 根据给定的两点间的差值x,y (终点减去起点坐标) 获得与X的夹角弧度
     *
     * @param x 焦点和物体中心水平方向的差值
     * @param y 焦点和物体中心竖直方向的差值
     * @return 与X方向的夹角(逆时针为正 弧度单位 范围[0, 2π) ) 如果焦点就是中心则返回NaN 其他情况返回朝向与X轴正方向的夹角 顺时针为正
     */
    static double getDirection(final double x, final double y) {
        if (x == 0 || y == 0 || x == y || x == -y) {
            if (x > 0) {
                if (y > 0) {
                    return Math.PI / 4;
                }
                if (y < 0) {
                    return Math.PI * 7 / 4;
                }
                return 0; // 向右
            }
            if (x < 0) {
                if (y > 0) {
                    return Math.PI * 3 / 4;
                }
                if (y < 0) {
                    return Math.PI * 5 / 4;
                }
                return Math.PI;// 向左
            }
            if (y > 0) {
                return Math.PI / 2;
            }
            if (y < 0) {
                return Math.PI * 3 / 2;
            }
            return Double.NaN;
        }
        if (x < 0) {
            return Math.atan(y / x) + Math.PI;
        }
        if (x > 0 && y < 0) {
            return Math.atan(y / x) + Math.PI * 2;
        }
        return Math.atan(y / x);
        /** 下面的判断以Y向下为正 且返回角度范围[-π,π] */
        // if (x < 0 && y > 0) {
        // return Math.atan(y / x) + Math.PI;
        // } else if (x < 0 && y < 0) {
        // return Math.atan(y / x) - Math.PI;
        // } else {
        // return Math.atan(y / x);
        // }
    }

    // TODO

    /**
     * 检测目标矩形区域能否在地图上移动
     *
     * @param map    地图数组(一维效率宽所以采用)
     * @param x      相对于整幅地图的左上角横坐标
     * @param y      相对于整幅地图的左上角纵坐标
     * @param w      需要判断的物体的长度
     * @param h      需要判断的物体的宽度
     * @param map_w  地图的全长
     * @param map_h  地图的总宽
     * @param tile_w 地图图块长度
     * @param tile_h 地图图块宽度
     * @param column 地图横向的图块数目
     */
    static boolean isAllowCross(final int[] map, final int x, final int y, final int w, final int h,
                                final int tile_w, final int tile_h, final int column) {
        if (map == null) {
            return true;
        }
        if (x > 0 && y > 0 && x + w < tile_w * column && y + h < tile_h * map.length / column) {
            final int tx = x / tile_w;
            final int txw = (x + w) / tile_w;
            int ty = y / tile_h;
            final int tyh = (y + h) / tile_h;
            final int a = ty * column;
            final int b = tyh * column;
            boolean flag = true;
            for (int temp = tx, index = tx; temp < txw; ) {
                if (flag) {
                    flag &= map[index + a] == 0 && map[index + b] == 0;
                } else {
                    return false;
                }
                index++;
                temp += tile_w;
            }
            for (int index = ty; ty < tyh; ) {
                if (flag) {
                    flag &= map[tx + index * column] == 0 && map[txw + index * column] == 0;
                } else {
                    return false;
                }
                index++;
                ty += tile_h;
            }
            return flag && map[txw + b] == 0;
        }
        return false;
    }

    /**
     * (推荐)检测目标矩形区域能否在地图上移动
     *
     * @param map    地图二维数组
     * @param x      相对于整幅地图的左上角横坐标
     * @param y      相对于整幅地图的左上角纵坐标
     * @param w      需要判断的物体的长度
     * @param h      需要判断的物体的宽度
     * @param map_w  地图的全长
     * @param map_h  地图的总宽
     * @param tile_w 地图图块长度
     * @param tile_h 地图图块宽度
     */
    static boolean isAllowCross(final int[][] map, final int tile_w, final int tile_h, final int x, final int y,
                                final int w, final int h) {
        if (map == null) {
            return true;
        }
        final int tx = x / tile_w;
        final int txw = (x + w) / tile_w;
        final int ty = y / tile_h;
        final int tyh = (y + h) / tile_h;
        if (x >= 0 && y >= 0 && txw < map[0].length && tyh < map.length) {
            boolean flag = map[ty][tx] == 0 && map[tyh][txw] == 0 && map[ty][txw] == 0 && map[tyh][tx] == 0;
            for (int temp = ty + 1; temp < tyh; temp++) {
                if (flag) {
                    flag &= map[temp][tx] == 0 && map[temp][txw] == 0;
                } else {
                    return false;
                }
            }
            for (int temp = tx + 1; temp < txw; temp++) {
                if (flag) {
                    flag &= map[ty][temp] == 0 && map[tyh][temp] == 0;
                } else {
                    return false;
                }
            }
            return flag;
        }
        return false;
    }

    /**
     * 在两点所在直线上，以从startPoint到endPoint为方向，离startPoint的距离disToStartPoint的点
     */
    static Point2D extentPoint(Point2D startPoint, Point2D endPoint, double disToStartPoint) {
        double disX = endPoint.getX() - startPoint.getX();
        double disY = endPoint.getY() - startPoint.getY();
        double dis = Math.sqrt(disX * disX + disY * disY);
        double sin = (endPoint.getY() - startPoint.getY()) / dis;
        double cos = (endPoint.getX() - startPoint.getX()) / dis;
        double deltaX = disToStartPoint * cos;
        double deltaY = disToStartPoint * sin;

        return new Point2D.Double(startPoint.getX() + deltaX, startPoint.getY() + deltaY);
    }

    /**
     * (x, y)绕(ox, oy)旋转degree度
     */
    static Point2D rotate(double x, double y, double ox, double oy, double degree) {
        x -= ox;
        y -= oy;

        double cos = Math.cos(Math.toRadians(degree));
        double sin = Math.sin(Math.toRadians(degree));

        double temp = x * cos - y * sin;
        y = x * sin + y * cos;
        x = temp;

        return new Point2D.Double(x + ox, y + oy);
    }

    /**
     * 获取八方走位时方向的状态码
     *
     * @param dir   要添加或者移除的方向二进制数值
     * @param isAdd 是否添加 flase=移除
     * @return 设置后的方向
     */
    static int getMoveState(final int currentState, final int targetState, final boolean isAdd) {
        return isAdd ? currentState | targetState
                : (currentState & targetState) != 0 ? currentState ^ targetState : currentState;
    }

    /**
     * 获取一个int数字二进制的某位数的布尔表现形式
     *
     * @param number :要获取二进制值的数
     * @param index  :第一位为1，依次类推 移动的位数超过了该类型的最大位数， 那么编译器会对移动的位数取模
     */
    static boolean getBitByIndex(final int number, final int index) {
        return (number >> 32 - index & 1) == 1;
    }

    /**
     * 将一个int数字二进制的某位数改变
     *
     * @param number :要设置二进制值的数
     * @param index  :第一位为1，依次类推 移动的位数超过了该类型的最大位数， 那么编译器会对移动的位数取模
     * @return 修改之后的数值
     */
    static int setBitByIndex(final int number, final int index, final boolean flag) {
        if (flag) {
            return 1 << 32 - index | number;
        } else {
            return ~(1 << 32 - index) & number;
        }
    }

    /**
     * 根据弧度获取八个方向之一
     *
     * @param orientation 给定的夹角弧度 范围[0,2π]
     * @param isAccurate  是否要求精确的返回
     * @return 如果不要求精确 则必定返回八个方向中的一个 如果要求精确 不满足八个方向条件则返回-1
     */
    static int getDirection(final double orientation, final boolean isAccurate) {
        if (isAccurate) {
            if (orientation == 0 || orientation == Math.PI * 2) {
                return RIGHT;
            } else if (orientation == Math.PI / 4) {
                return RIGHT | DOWN;
            } else if (orientation == Math.PI / 2) {
                return DOWN;
            } else if (orientation == Math.PI * 3 / 4) {
                return LEFT | DOWN;
            } else if (orientation == Math.PI) {
                return LEFT;
            } else if (orientation == Math.PI * 5 / 4) {
                return LEFT | UP;
            } else if (orientation == Math.PI * 3 / 2) {
                return UP;
            } else if (orientation == Math.PI * 7 / 4) {
                return RIGHT | UP;
            } else if (orientation == Double.NaN) {
                return 0;
            } else {
                return -1;
            }
        } else if (orientation >= 0 && orientation < Math.PI / 8 || orientation >= Math.PI * 15 / 8 && orientation < Math.PI
                * 2) {
            return RIGHT;
        } else if (orientation >= Math.PI / 8 && orientation < Math.PI * 3 / 8) {
            return RIGHT | DOWN;
        } else if (orientation >= Math.PI * 3 / 8 && orientation < Math.PI * 5 / 8) {
            return DOWN;
        } else if (orientation >= Math.PI * 5 / 8 && orientation < Math.PI * 7 / 8) {
            return LEFT | DOWN;
        } else if (orientation >= Math.PI * 7 / 8 && orientation < Math.PI * 9 / 8) {
            return LEFT;
        } else if (orientation >= Math.PI * 9 / 8 && orientation < Math.PI * 11 / 8) {
            return LEFT | UP;
        } else if (orientation >= Math.PI * 11 / 8 && orientation < Math.PI * 13 / 8) {
            return UP;
        } else if (orientation >= Math.PI * 13 / 8 && orientation < Math.PI * 15 / 8) {
            return RIGHT | UP;
        } else {
            return 0;
        }
    }

    /**
     * 根据速度矢量返回一个代表大致方向的标识
     */
    static int getDirection(final int vx, final int vy) {
        if (vx > 0) {
            if (vy > 0) {
                return RIGHT | DOWN;
            }
            if (vy < 0) {
                return RIGHT | UP;
            }
            return RIGHT; // 向右
        }
        if (vx < 0) {
            if (vy > 0) {
                return LEFT | DOWN;
            }
            if (vy < 0) {
                return LEFT | UP;
            }
            return LEFT;// 向左
        }
        if (vy > 0) {
            return DOWN;
        }
        if (vy < 0) {
            return UP;
        }
        return 0; // 静止
    }

    /**
     * 一维空间两条线段是否至少有部分重叠
     */
    static boolean isOverlap(int start, int length, int otherstart, int otherlength) {
        // 将长度全转换成正数
        if (length < 0) {
            start += length;
            length = -length;
        }
        if (otherlength < 0) {
            otherstart += otherlength;
            otherlength = -otherlength;
        }
        // 只要一条线的端点在另一条线上就可以认为是线段有重叠部分
        // return start >= otherstart && start < otherstart + otherlength
        // || otherstart >= start && otherstart < start + length;
        // 上述方法的改进版本 但相差不大
        // return (start > otherstart ? start < otherlength + otherstart :
        // otherstart < length + start)
        // !(此线段的起点大于另一条的终点或者此线段的终点大于另一条的起点)
        return !(start > otherstart + otherlength || otherstart > start + length);
    }

    /**
     * 一维空间两条线段是否有包含(即一条线是否在另一条线的上面)
     */
    static boolean isContain(int start, int length, int otherstart, int otherlength) {
        // 将长度全转换成正数
        if (length < 0) {
            start += length;
            length = -length;
        }
        if (otherlength < 0) {
            otherstart += otherlength;
            otherlength = -otherlength;
        }
        return start >= otherstart && start + length <= otherstart + otherlength || otherstart >= start && otherstart
                + otherlength <= start + length;
    }

    /**
     * 检测两个矩形是否存在重叠
     */
    static boolean isCollision(int x, int y, int w, int h, int sx, int sy, int sw, int sh) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        if (sw < 0) {
            sx += sw;
            sw = -sw;
        }
        if (sh < 0) {
            sy += sh;
            sh = -sh;
        }
        // java自身提供的方法
        // return x < sl + sx && sx < l + x && y < sw + sy && sy < w + y;
        return (x > sx ? x < sw + sx : sx < w + x) && (y > sy ? y < sh + sy : sy < h + y);
    }

    /**
     * 两个矩形是否包含(边界无接触)
     *
     * @param x  第一个矩形的X
     * @param y  第一个矩形的Y
     * @param w  第一个矩形的长
     * @param h  第一个矩形的宽
     * @param sx 第二个矩形的X
     * @param sy 第二个矩形的Y
     * @param sw 第二个矩形的长
     * @param sh 第二个矩形的宽
     */
    static boolean isInside(int x, int y, int w, int h, int sx, int sy, int sw, int sh) {
        if (x == sx || y == sy) {
            return false;
        }
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        if (sw < 0) {
            sx += sw;
            sw = -sw;
        }
        if (sh < 0) {
            sy += sh;
            sh = -sh;
        }
        if (x > sx && x + w < sx + sw && y > sy && y + h < sy + sh) {
            return true;
        }
        return sx > x && sx + sw < x + w && sy > y && sy + sh < y + h;
    }

    /**
     * 检测两个矩形是否相离
     *
     * @param x  第一个矩形的X 坐标
     * @param y  第一个矩形的Y 坐标
     * @param w  第一个矩形的长
     * @param h  第一个矩形的宽
     * @param sx 第二个矩形的X 坐标
     * @param sy 第二个矩形的Y 坐标
     * @param sw 第二个矩形的长
     * @param sh 第二个矩形的宽
     * @return
     */
    static boolean isOutside(int x, int y, int w, int h, int sx, int sy, int sw, int sh) {
        if (w < 0) {
            x += w;
            w = -w;
        }
        if (h < 0) {
            y += h;
            h = -h;
        }
        if (sw < 0) {
            sx += sw;
            sw = -sw;
        }
        if (sh < 0) {
            sy += sh;
            sh = -sh;
        }
        return x > sx + sw || sx > x + w || y > sy + sh || sy > y + h;
        // java自身提供的方法
        //		return x < sl + sx && sx < l + x && y < sw + sy && sy < w + y;
        // return (x > sx ? x < sl + sx : sx < l + x)
        // && (y > sy ? y < sw + sy : sy < w + y);
    }

    /**
     * 确定指定坐标相对于此 Rectangle2D 的位置。 此方法计算适当掩码值的二进制或 (OR)， 这些掩码值针对此 Rectangle2D
     * 的每个边指示指定坐标是否在此 Rectangle2D 其余边缘的同一侧。
     */
    static int outcode(int rectX, int rectY, int rectL, int rectW, final int x, final int y) {
        int out = 0;
        if (rectL < 0) {
            rectX += rectL;
            rectL = -rectL;
        }
        if (rectW < 0) {
            rectY += rectW;
            rectW = -rectW;
        }
        if (rectL == 0) {
            out |= ShapeUtil.OUT_LEFT | ShapeUtil.OUT_RIGHT;
        } else if (x < rectX) {
            out |= ShapeUtil.OUT_LEFT;
        } else if (x > rectX + rectL) {
            out |= ShapeUtil.OUT_RIGHT;
        }
        if (rectW == 0) {
            out |= ShapeUtil.OUT_TOP | ShapeUtil.OUT_BOTTOM;
        } else if (y < rectY) {
            out |= ShapeUtil.OUT_TOP;
        } else if (y > rectY + rectW) {
            out |= ShapeUtil.OUT_BOTTOM;
        }
        return out;
    }

    /**
     * 矩形是否和指定线段有交点
     */
    static boolean intersectsLine(final int x, final int y, final int l, final int w, int sx, int sy, final int ex,
                                  final int ey) {
        int out1, out2;// 起点和终点和矩形相对关系 为0代表在矩形内部
        // 终点如果在矩形上则线段和矩形有交点
        if ((out2 = ShapeUtil.outcode(x, y, l, w, ex, ey)) == 0) {
            return true;
        }
        // 起点不在矩形上
        while ((out1 = ShapeUtil.outcode(x, y, l, w, sx, sy)) != 0) {
            // 如果线段的两个点在矩形的同一侧就表示与矩形不相交
            if ((out1 & out2) != 0) {
                return false;
            }
            // 如果起点在矩形左右
            if ((out1 & (ShapeUtil.OUT_LEFT | ShapeUtil.OUT_RIGHT)) != 0) {
                if ((out1 & ShapeUtil.OUT_RIGHT) != 0) {
                    sy = sy + (x + l - sx) * (ey - sy) / (ex - sx);
                    sx = x + l;
                } else {
                    sy = sy + (x - sx) * (ey - sy) / (ex - sx);
                    sx = x;
                }
            } else {
                // 如果起点在矩形下方
                if ((out1 & ShapeUtil.OUT_BOTTOM) != 0) {
                    sx = sx + (y + w - sy) * (ex - sx) / (ey - sy);
                    sy = y + w;
                } else {
                    sx = sx + (y - sy) * (ex - sx) / (ey - sy);
                    sy = y;
                }
            }
        }
        return true;
    }

    /**
     * 点到线段的最短距离
     */
    static double PointToSegDist(double x, double y, double ax, double ay, double bx, double by) {
        double cross = (bx - ax) * (x - ax) + (by - ay) * (y - ay);
        if (cross <= 0) {
            return Math.sqrt((x - ax) * (x - ax) + (y - ay) * (y - ay));
        }

        double d2 = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
        if (cross >= d2) {
            return Math.sqrt((x - bx) * (x - bx) + (y - by) * (y - by));
        }

        double r = cross / d2;
        double px = ax + (bx - ax) * r;
        double py = ay + (by - ay) * r;
        return Math.sqrt((x - px) * (x - px) + (py - ay) * (py - ay));
    }

    /**
     * 根据矩形的坐标和大小重新定义线段 （因为他们如果有交点必然落在矩形内部） 通过线段两端获得一个包裹的矩形
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ex
     * @param x
     * @param y
     * @param l
     * @param w
     */
    @Deprecated
    static boolean intersectsLine(int sx, int sy, int ex, int ey, final int linew, final int lineh, final int x,
                                  final int y, final int l, final int w) {
        // 如果包围线段自身的矩形边框和目标无碰撞表明线段和目标也没有相交
        if (sx >= x + l || x >= ex || sy >= y + w || y >= ey) {
            return false;
        }
        // 如果线段两个端点的一个在目标上就表明有相交
        if (sx >= x && sx < x + l && sy >= y && sy < y + w) {
            return true;
        }
        if (ex >= x && ex < x + l && ey >= y && ey < y + w) {
            return true;
        }
        /** 二分之后两个小矩形是否与目标相碰撞的两个变量 */
        boolean flagT, flagD;
        /** 线段中点 */
        for (int mx = sx + (ex - sx >> 1), my = sy + (ey - sy >> 1), temp; ; ) {
            // 根据起点 中点(近似值一般偏小) 终点获取两个矩形并判断和指定目标矩形是否相交
            // flagT = !(sx >= x + l || x >= mx || sy >= y + l || y >= my);
            // flagD = !(mx >= x + l || x >= ex || my >= y + l || y >= ey);
            flagT = sx < l + x && x < mx && sy < w + y && y < my;
            flagD = mx < l + x && x < ex && my < w + y && y < ey;
            if (flagT && flagD) {
                return true;
            }
            if (!(flagT || flagD)) {
                return false;
            }
            if (ex - mx <= linew && ey - my <= lineh) {
                return flagT || flagD;
            }
            if (flagT) {
                // 如果接近起点的矩形相交了
                ex = mx;
                ey = my;
            } else {
                // 否则就是接近终点的相交
                sx = mx;
                sy = my;
            }
            temp = ex - sx >> 1;
            if (temp >= linew) {
                mx = sx + temp;
            }
            temp = ey - sy >> 1;
            if (temp >= lineh) {
                my = sy + temp;
            }
        }
    }
}
