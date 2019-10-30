package com.mona.util;

public class Shape2D {

    /**
     * 根据入射向量和反射面获取反射向量
     */
    public static double[] echo(double vx, double vy, double bx, double by) {
        if (bx == 0) return new double[]{-vx, vy};// 反射面为Y轴
        if (by == 0) return new double[]{vx, -vy};// 反射面为X轴
        double length = 2 * (vy * bx - vx * by) / (bx * bx + by * by);
        vx -= -by * length;
        vy -= bx * length;
        // System.out.println("反射向量("+vx+","+vy+")");
        return new double[]{vx, vy};
    }

    /**
     * 利用射线法判断点P是否在多边形上
     */
    public static boolean contains(double px, double py, double... points) {
        double sx, sy, ex, ey, ccw0, ccw1, temp;
        int hits = 0;
        sx = points[points.length - 2];
        sy = points[points.length - 1];
        for (int i = 1; i < points.length; i += 2, sx = ex, sy = ey) {
            ex = points[i - 1];
            ey = points[i];
            if (sy == ey) continue;// 如果是平行线
            if (sy < ey ? py < sy || py >= ey : py < ey || py >= sy) continue;
            if (sx > ex) {
                if (px > sx) continue;
                if (px < ex) {
                    hits++;
                    continue;
                }
                temp = (py - sy) * (ex - sx);
                ccw0 = (px - sx) * (ey - sy) - temp;// 端点位置
                ccw1 = (sx + 1) * (ey - sy) - temp;// 射线位置
            } else {
                if (px > ex) continue;
                if (px < sx) {
                    hits++;
                    continue;
                }
                temp = (py - sy) * (ex - sx);
                ccw0 = (px - sx) * (ey - sy) - temp;// 端点位置
                ccw1 = (ex + 1) * (ey - sy) - temp;// 射线位置
            }
            if (ccw0 == 0) return true;
            if (ccw0 > 0 ? ccw1 < 0 : ccw1 > 0) hits++;
        }
        return (hits & 1) != 0;
    }

    /**
     * 两条线段是否有交点
     */
    public static boolean intersects(double ax, double ay, double bx, double by, double cx, double cy, double dx,
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
        if (maxx < abac || maxy < abad || cdca < minx || cdcb < miny)
            return false;
        abac = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
        abad = (bx - ax) * (dy - ay) - (dx - ax) * (by - ay);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
        cdca = (dx - cx) * (ay - cy) - (ax - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
        cdcb = (dx - cx) * (by - cy) - (bx - cx) * (dy - cy);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
        if (abac == 0 || abad == 0 || cdca == 0 || cdcb == 0)
            return true;
        return (abac > 0 && abad < 0 || abac < 0 && abad > 0) && (cdca > 0 && cdcb < 0 || cdca < 0 && cdcb > 0);
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
    public static boolean isAllowCross(final int[][] map, final int tile_w,
                                       final int tile_h, final int x, final int y, final int w, final int h) {
        if (map == null) return true;
        final int tx = x / tile_w;
        final int txw = (x + w) / tile_w;
        final int ty = y / tile_h;
        final int tyh = (y + h) / tile_h;
        if (x >= 0 && y >= 0 && txw < map[0].length && tyh < map.length) {
            boolean flag = map[ty][tx] == 0 && map[tyh][txw] == 0
                    && map[ty][txw] == 0 && map[tyh][tx] == 0;
            for (int temp = ty + 1; temp < tyh; temp++)
                if (flag)
                    flag &= map[temp][tx] == 0 && map[temp][txw] == 0;
                else return false;
            for (int temp = tx + 1; temp < txw; temp++)
                if (flag)
                    flag &= map[ty][temp] == 0 && map[tyh][temp] == 0;
                else return false;
            return flag;
        }
        return false;
    }

    /**
     * 基于AST分离轴算法判断两个凸多边形是否重叠
     * 注意:两个数组均以(x,y)的形式将坐标存储到数组中
     *
     * @param point1 第一个凸多边形的坐标数组
     * @param point2 第二个凸多边形的坐标数组
     */
    public static boolean isOverlap2D(int point1[], int point2[]) {
        // 先利用AABB包围盒进行粗略检测
        int minX1 = point1[0], minY1 = point1[1], maxX1 = point1[0], maxY1 = point1[1];
        int minX2 = point2[0], minY2 = point2[1], maxX2 = point2[0], maxY2 = point2[1];
        // 获取两个凸多边形的最大(x,y)和最小(x,y)以便于生成AABB包围盒
        for (int i = 3; i < point1.length; i += 2) {
            if (minX1 > point1[i - 1]) minX1 = point1[i - 1];
            if (minY1 > point1[i]) minY1 = point1[i];
            if (maxX1 < point1[i - 1]) maxX1 = point1[i - 1];
            if (maxY1 < point1[i]) maxY1 = point1[i];
        }
        for (int j = 3; j < point2.length; j += 2) {
            if (minX2 > point2[j - 1]) minX2 = point2[j - 1];
            if (minY2 > point2[j]) minY2 = point2[j];
            if (maxX2 < point2[j - 1]) maxX2 = point2[j - 1];
            if (maxY2 < point2[j]) maxY2 = point2[j];
        }
        if (maxX1 < minX2 || maxX2 < minX1 || maxY1 < minY2 || maxY2 < minY1)
            return false;
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
            if ((temp ^ ccw) >= 0) break;
        }
        // 当假定分离轴成功分离所有点时表示二者没有重叠
        if (j >= len2) return false;
        /**
         * 循环数组中的坐标依次去多边形上的一条边视作分离轴
         * 计算多边形其余点和另一个多边形上所有点是否位于分离轴的两侧
         */
        for (i = 3; i < len1; i += 2) {
            /**
             * a b两点便是假定分离轴的线段两点坐标
             * p(待测点)是两个多边形上非a b两点以外的点
             * 利用向量计算出p点在分离轴的位置
             */
            ax = point1[i - 3];
            ay = point1[i - 2];
            bx = point1[i - 1] - ax;
            by = point1[i] - ay;
            for (j = 1; j < len2; j += 2) {
                px = point2[j - 1] - ax;
                py = point2[j] - ay;
                temp = px * by - py * bx;
                if ((temp ^ ccw) >= 0) break;
            }
            if (j >= len2) return false;
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
            if ((temp ^ ccw) >= 0) break;
        }
        if (j >= len1) return false;
        for (i = 3; i < len2; i += 2) {
            ax = point2[i - 3];
            ay = point2[i - 2];
            bx = point2[i - 1] - ax;
            by = point2[i] - ay;
            for (j = 1; j < len1; j += 2) {
                px = point1[j - 1] - ax;
                py = point1[j] - ay;
                temp = px * by - py * bx;
                if ((temp ^ ccw) >= 0) break;
            }
            if (j >= len1) return false;
        }
        return true;
    }

    /**
     * 获取一个点到另一线段所在直线的垂足坐标
     */
    public static double[] getPoint(final double ax, double ay, final double bx, final double by,
                                    final double px, final double py) {
        double dotprod = (px - ax) * (bx - ax) + (py - ay) * (by - ay);
        double projlenSq = dotprod * dotprod / ((bx - ax) * (bx - ax) + (by - ay) * (by - ay));
        double pc2 = (px - ax) * (px - ax) + (py - ay) * (py - ay) - projlenSq;
        if (pc2 < 0) pc2 = 0;
        double pa2 = (px - ax) * (px - ax) + (py - ay) * (py - ay);
        double pb2 = (px - bx) * (px - bx) + (py - by) * (py - by);
        double ab = Math.hypot(ax - bx, ay - by);
        if (ab != 0) {
            double ac = Math.sqrt(pa2 - pc2);
            double bc = Math.sqrt(pb2 - pc2);
            double abx = ax - bx;
            if (abx < 0) abx = -abx;
            double aby = ay - by;
            if (aby < 0) aby = -aby;
            double acx = abx * ac / ab;
            double acy = aby * ac / ab;
            if (bc > ab && bc > ac) {
                double cx = ax + (bx > ax ? -acx : acx);
                double cy = ay + (by > ay ? -acy : acy);
                return new double[]{cx, cy};
            } else {
                double cx = ax + (bx > ax ? acx : -acx);
                double cy = ay + (by > ay ? acy : -acy);
                return new double[]{cx, cy};
            }
        }
        return null;
    }

    /**
     * 获得两线段的交点
     */
    public static double[] getPoint(final double ax, double ay, final double bx, final double by,
                                    final double cx, final double cy, double dx, double dy) {
        // 三角形abc 面积的2倍
        double area_abc = (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
        // 三角形abd 面积的2倍
        double area_abd = (ax - dx) * (by - dy) - (ay - dy) * (bx - dx);
        // 面积符号相同则两点在线段同侧,不相交 (对点在线段上的情况,本例当作不相交处理);
        if (area_abc >= 0 && area_abd >= 0 || area_abc <= 0 && area_abd <= 0) return null;
        // 三角形cda 面积的2倍
        double area_cda = (cx - ax) * (dy - ay) - (cy - ay) * (dx - ax);
        // 三角形cdb 面积的2倍
        // 注意: 这里有一个小优化.不需要再用公式计算面积,而是通过已知的三个面积加减得出.
        double area_cdb = area_cda + area_abc - area_abd;
        if (area_cda >= 0 && area_cdb >= 0 || area_cda <= 0 && area_cdb <= 0) return null;
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
    public static double[] getIntersection(final double ax, double ay, final double bx, final double by,
                                           final double cx, final double cy, double dx, double dy) {
        double s1 = (by - ay) * (dx - ax) - (bx - ax) * (dy - ay);
        double s2 = -((cx - ax) * (by - ay) - (cy - ay) * (bx - ax));
        double px = (cx * s1 + dx * s2) / (s1 + s2);
        double py = (cy * s1 + dy * s2) / (s1 + s2);
        return new double[]{px, py};
    }

    // 任意两个多边形的边是否相交
    public boolean intersects(int point1[], int point2[]) {
        // 先利用AABB包围盒进行粗略检测
        double ax = point1[0], ay = point1[1], bx = point1[0], by = point1[1];
        double cx = point2[0], cy = point2[1], dx = point2[0], dy = point2[1];
        // 获取两个多边形的最大(x,y)和最小(x,y)以便于生成AABB包围盒
        for (int i = 3; i < point1.length; i += 2) {
            if (ax > point1[i - 1]) ax = point1[i - 1];
            if (ay > point1[i]) ay = point1[i];
            if (bx < point1[i - 1]) bx = point1[i - 1];
            if (by < point1[i]) by = point1[i];
        }
        for (int j = 3; j < point2.length; j += 2) {
            if (cx > point2[j - 1]) cx = point2[j - 1];
            if (cy > point2[j]) cy = point2[j];
            if (dx < point2[j - 1]) dx = point2[j - 1];
            if (dy < point2[j]) dy = point2[j];
        }
        if (bx < cx || dx < ax || by < cy || dy < ay)
            return false;
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
                if (bx < cx || by < cy || dx < ax || dy < ay) continue;
                ax = (point1[i - 1] - point1[i - 3]) * (point2[j - 2] - point1[i - 2])
                        - (cx - point1[i - 3]) * (point1[i] - point1[i - 2]);// 第一条线段的向量和（第一条线段的开始点与第二条线段的开始点组成的向量）的向量积
                bx = (point1[i - 1] - point1[i - 3]) * (point2[j] - point1[i - 2])
                        - (point2[j - 1] - point1[i - 3]) * (point1[i] - point1[i - 2]);// 第一条线段的向量和（第一条线段的开始点与第二条线段的结束点组成的向量）的向量积
                cx = (point2[j - 1] - point2[j - 3]) * (point1[i - 2] - point2[j - 2])
                        - (point1[i - 3] - point2[j - 3]) * (point2[j] - point2[j - 2]);// 第二条线段的向量和（第二条线段的开始点与第一条线段的开始点组成的向量）的向量积
                dx = (point2[j - 1] - point2[j - 3]) * (point1[i] - point2[j - 2])
                        - (point1[i - 1] - point2[j - 3]) * (point2[j] - point2[j - 2]);// 第二条线段的向量和（第二条线段的开始点与第一条线段的结束点组成的向量）的向量积
                if ((ax >= 0 && bx <= 0 || ax <= 0 && bx >= 0) && (cx >= 0 && dx <= 0 || cx <= 0 && dx >= 0))
                    return true;
            }
        }
        return false;
    }
}
