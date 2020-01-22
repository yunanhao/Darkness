package demo.util;

import java.awt.*;

/**
 * 判断一个点是否在凸多边形内部
 */
public class Quadrangle {
    public static boolean pInQuadrangle(final Point[] point, final Point p) {
        switch (point.length) {
            case 3:
                return pIn3(point[0], point[1], point[2], p);
            case 4:
                return pIn4(point[0], point[1], point[2], point[3], p);
            case 5:
                return pIn5(point[0], point[1], point[2], point[3], point[4], p);
        }
        return false;
    }

    // 判断是否在5边形内
    public static boolean pIn5(final Point a, final Point b, final Point c,
                               final Point d, final Point e, final Point p) {
        final double dTriangle = triangleArea(a, b, p) + triangleArea(b, c, p)
                + triangleArea(c, d, p) + triangleArea(d, e, p) + triangleArea(e, a, p);
        final double dQuadrangle = triangleArea(a, b, c) + triangleArea(c, d, a)
                + triangleArea(a, d, e);
        return dTriangle == dQuadrangle;
    }

    // 判断是否在4边形内
    public static boolean pIn4(final Point a, final Point b, final Point c,
                               final Point d, final Point p) {
        final double dTriangle = triangleArea(a, b, p) + triangleArea(b, c, p)
                + triangleArea(c, d, p) + triangleArea(d, a, p);
        final double dQuadrangle = triangleArea(a, b, c) + triangleArea(c, d, a);
        return dTriangle == dQuadrangle;
    }

    // 判断是否在3角形内
    public static boolean pIn3(final Point a, final Point b, final Point c,
                               final Point p) {
        final double dTriangle = triangleArea(a, b, p) + triangleArea(b, c, p)
                + triangleArea(c, a, p);
        final double dQuadrangle = triangleArea(a, b, c);
        return dTriangle == dQuadrangle;
    }

    public static boolean pInQuadrangle(final float[] px, final float[] py,
                                        final float x, final float y) {
        final Point a = new Point((int) px[0], (int) py[0]);
        final Point b = new Point((int) px[1], (int) py[1]);
        final Point c = new Point((int) px[2], (int) py[2]);
        final Point d = new Point((int) px[3], (int) py[3]);
        final Point p = new Point((int) x, (int) y);

        final double dTriangle = triangleArea(a, b, p) + triangleArea(b, c, p)
                + triangleArea(c, d, p) + triangleArea(d, a, p);
        final double dQuadrangle = triangleArea(a, b, c) + triangleArea(c, d, a);
        return dTriangle == dQuadrangle;
    }

    private static double triangleArea(final Point a, final Point b, final Point c) {
        final double result = Math.abs((a.x * b.y + b.x * c.y + c.x * a.y - b.x
                * a.y - c.x * b.y - a.x * c.y) / 2.0D);
        return result;
    }

    public static Point getIntersectPoint(final Point p0, final Point p1,
                                          final Point p2, final Point p3) {
        final double[] line1 = CalParam(p0, p1);
        final double[] line2 = CalParam(p2, p3);
        final double a1 = line1[0];
        final double a2 = line2[0];
        final double b1 = line1[1];
        final double b2 = line2[1];
        final double c1 = line1[2];
        final double c2 = line2[2];
        Point p = null;
        final double m = a1 * b2 - a2 * b1;
        if (m == 0) return null;
        final int x = (int) ((c2 * b1 - c1 * b2) / m);
        final int y = (int) ((c1 * a2 - c2 * a1) / m);
        p = new Point(x, y);
        return p;
    }

    public static double[] CalParam(final Point p1, final Point p2) {
        double a, b, c;
        a = p2.y - p1.y;
        b = p1.x - p2.x;
        c = -b * p1.y - a * p1.x;
        if (b < 0) {
            a *= -1;
            b *= -1;
            c *= -1;
        } else if (b == 0 && a < 0) {
            a *= -1;
            c *= -1;
        }
        return new double[]{a, b, c};
    }

    public static Point GetIntersection(final Point a, final Point b,
                                        final Point c, final Point d) {
        final Point intersection = new Point(0, 0);

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) + Math.abs(d.y - c.y)
                + Math.abs(d.x - c.x) == 0) {
            if (c.x - a.x + c.y - a.y == 0) {
            } else {
            }
            return null;
        }

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) == 0) {
            if ((a.x - d.x) * (c.y - d.y) - (a.y - d.y) * (c.x - d.x) == 0) {
            } else {
            }
            return null;
        }
        if (Math.abs(d.y - c.y) + Math.abs(d.x - c.x) == 0) {
            if ((d.x - b.x) * (a.y - b.y) - (d.y - b.y) * (a.x - b.x) == 0) {
            } else {
            }
            return null;
        }

        if ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y) == 0)
            return null;

        intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) - c.x
                * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x))
                / ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));
        intersection.y = ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y
                * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y))
                / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

        if ((intersection.x - a.x) * (intersection.x - b.x) <= 0
                && (intersection.x - c.x) * (intersection.x - d.x) <= 0
                && (intersection.y - a.y) * (intersection.y - b.y) <= 0
                && (intersection.y - c.y) * (intersection.y - d.y) <= 0) return intersection;
        else return null;
    }

    public static Point GetIntersection_a(final Point a, final Point b,
                                          final Point c, final Point d) {
        final Point intersection = new Point(0, 0);

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) + Math.abs(d.y - c.y)
                + Math.abs(d.x - c.x) == 0) {
            if (c.x - a.x + c.y - a.y == 0) {
            } else {
            }
            return null;
        }

        if (Math.abs(b.y - a.y) + Math.abs(b.x - a.x) == 0) {
            if ((a.x - d.x) * (c.y - d.y) - (a.y - d.y) * (c.x - d.x) == 0) {
            } else {
            }
            return null;
        }
        if (Math.abs(d.y - c.y) + Math.abs(d.x - c.x) == 0) {
            if ((d.x - b.x) * (a.y - b.y) - (d.y - b.y) * (a.x - b.x) == 0) {
            } else {
            }
            return null;
        }

        if ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y) == 0)
            return null;

        intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) - c.x
                * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x))
                / ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));
        intersection.y = ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y
                * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y))
                / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

        if ((intersection.x - a.x) * (intersection.x - b.x) <= 0
                && (intersection.x - c.x) * (intersection.x - d.x) <= 0
                && (intersection.y - a.y) * (intersection.y - b.y) <= 0
                && (intersection.y - c.y) * (intersection.y - d.y) <= 0) return intersection;
        else return intersection;
    }
}
