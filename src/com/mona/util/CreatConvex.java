package com.mona.util;

import com.mona.bean.Point;
import com.mona.bean.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreatConvex {
    public static List<Point> getConvexHull(LinkedList<Point> pointList) {
        Vector[] points = new Vector[pointList.size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vector(pointList.get(i).x, pointList.get(i).y);
        }
        List<Vector> rList = getConvexHull(points);
        List<Point> result = new ArrayList<Point>();
        for (int i = 0; i < rList.size(); i++) {
            Point point = new Point((int) rList.get(i).x, (int) rList.get(i).y);
            result.add(point);
        }
        return result;
    }

    public static double[] getConvexHull(java.awt.Point... points) {
        Vector[] vector2ds = new Vector[points.length];
        for (int i = 0; i < points.length; i++) {
            vector2ds[i] = new Vector(points[i].x, points[i].y);
        }
        List<Vector> rList = getConvexHull(vector2ds);
        double[] result = new double[rList.size() * 2];
        for (int i = 0; i < result.length; i += 2) {
            result[i] = rList.get(i / 2).x;
            result[i + 1] = rList.get(i / 2).y;
        }
        return result;
    }

    /**
     * 凸包生成算法
     */
    public static ArrayList<Vector> getConvexHull(Vector... points) {
        Long time = System.nanoTime();
        ArrayList<Vector> pointlist = new ArrayList<Vector>();
        ArrayList<Vector> result = new ArrayList<Vector>();
        Vector left = null, right = null, current;
        for (int j, i = 0; i < points.length; i++) {
            current = points[i];
            if (current == null) {
                continue;
            }
            if (left == null || current.x < left.x || current.x == left.x && current.y < left.y) {
                left = current;
            }
            if (right == null || current.x > right.x || current.x == right.x && current.y > right.y) {
                right = current;
            }
            if (pointlist.size() == 0) {
                pointlist.add(current);
                continue;
            }
            for (j = 0; j < pointlist.size(); j++) {
                if (current.x < pointlist.get(j).x) {
                    pointlist.add(j, current);
                    break;
                }
                if (current.x > pointlist.get(j).x) {
                    if (j + 1 == pointlist.size()) {
                        pointlist.add(current);
                        break;
                    }
                    continue;
                }
                if (current.y < pointlist.get(j).y) {
                    pointlist.add(j, current);
                    break;
                }
                if (current.y > pointlist.get(j).y) {
                    pointlist.add(current);
                    break;
                }
            }
        }
        System.out.println("sort:" + pointlist);
        pointlist.remove(left);
        pointlist.remove(right);
        result.add(left);
        separate(left, right, pointlist, result);
        result.add(right);
        separate(right, left, pointlist, result);
        System.out.println("耗时:" + (System.nanoTime() - time) + "纳秒");
        return result;
    }

    private static void separate(Vector left, Vector right, List<Vector> pointlist, List<Vector> result) {
        Vector current, pointMax = null;
        double distanceMax = 0.0D, temp;
        double x = right.x - left.x;
        double y = right.y - left.y;
        for (int i = 0; i < pointlist.size(); i++) {
            current = pointlist.get(i);
            temp = x * (current.y - left.y) - y * (current.x - left.x);
            if (temp > distanceMax) {
                distanceMax = temp;
                pointMax = current;
            }
        }
        if (pointMax != null) {
            pointlist.remove(pointMax);
            separate(left, pointMax, pointlist, result);
            result.add(pointMax);
            separate(pointMax, right, pointlist, result);
        }
    }

    /**
     * P点和向量AB的关系
     *
     * @return 1:P在向量AB的左方
     */
    public static int onLeft(double ax, double ay, double bx, double by, double px, double py) {
        double compute = (bx - ax) * (py - ay) - (px - ax) * (by - ay);
        // compute = ax * by + px * ay + bx * py - px * by - bx * ay - ax * py;
        if (compute > 0) {
            return 1;
        }
        if (compute < 0) {
            return -1;
        }
        return 0;
    }
}
