package com.mona.util;

/*第4章 分治法  凸包问题的分治解法*/

import com.mona.bean.Point;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class ConvexHull {
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(onLeft(3, 3, 5, 2, 5, 5));
        Point[] Points = new Point[15];

        Points[0] = new Point(-5, 7);
        Points[1] = new Point(3, -6);
        Points[2] = new Point(5, 4);
        Points[3] = new Point(-5, -5);
        Points[4] = new Point(1, 7);
        Points[5] = new Point(6, 0);

        Points[6] = new Point(0, 0);
        Points[7] = new Point(-5, 0);
        Points[8] = new Point(3, -2);
        Points[9] = new Point(3, 4);

        Points[10] = new Point(1, 6);
        Points[11] = new Point(5, 3);
        Points[12] = new Point(-4, -5);
        Points[13] = new Point(-3, 6);
        Points[14] = new Point(2, 5);

        Arrays.sort(Points); // 预排序处理

        LinkedList<Point> list = new LinkedList<Point>();
        for (int i = 0; i < Points.length; i++) {
            list.add(Points[i]); // list存放全部的顶点
        }

        LinkedList<Point> result = getConvexHulls(list); // result用来存放最终的结果顶点

        System.out.println("一共有 " + result.size() + " 个顶点, " + "凸包的顶点是： ");
        Iterator<Point> it = result.iterator();
        while (it.hasNext()) {
            Point next = it.next();
            System.out.print("(" + next.x + "," + next.y + ")" + "  ");
        }
    }

    public static LinkedList<Point> getConvexHulls(LinkedList<Point> list) {
        // 将凸包顶点以result链表返回
        LinkedList<Point> result = new LinkedList<Point>();

        Point temp1 = list.removeFirst();
        Point temp2 = list.removeLast();
        result.add(temp1);
        // 递归的处理temp1 ---> temp2左右两侧的点
        dealWithLeft(temp1, temp2, result, list);
        result.add(temp2);
        dealWithLeft(temp2, temp1, result, list);// 注意每次要将result带着，存放结果集

        return result;
    }

    public static void dealWithLeft(Point p1, Point p2, LinkedList<Point> result, LinkedList<Point> list) {
        // 递归的处理p1，p2构成的射线左边的点
        Iterator<Point> it = list.iterator();

        // 找出左边最高的点Pmax
        Point Pmax = null;
        int max = 0;
        while (it.hasNext()) {
            Point next = it.next();
            int x1 = p1.x, y1 = p1.y;
            int x2 = p2.x, y2 = p2.y;
            int x3 = next.x, y3 = next.y;

            int compute = x1 * y2 + x3 * y1 + x2 * y3 - x3 * y2 - x2 * y1 - x1 * y3;
            if (compute > max) {
                max = compute;
                Pmax = next;
            }
        }

        // 又找到了一个顶点
        if (Pmax != null) {
            // 递归
            dealWithLeft(p1, Pmax, result, list);
            result.add(Pmax);
            list.remove(Pmax);
            dealWithLeft(Pmax, p2, result, list);
        }
    }

    public static int onLeft(Point test, Point start, Point end) {
        // 判断target是否在p1--->p2射线的左侧
        double ax = start.x;
        double ay = start.y;
        double bx = end.x;
        double by = end.y;
        double px = test.x;
        double py = test.y;
        double compute = (bx - ax) * (py - ay) - (px - ax) * (by - ay);
        if (compute > 0) {
            return 1;
        }
        if (compute < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * P点和向量AB的关系
     *
     * @return 1:P在向量AB的左方
     */
    public static double onLeft(double ax, double ay, double bx, double by, double px, double py) {
        double compute;
        compute = (bx - ax) * (py - ay) - (px - ax) * (by - ay);
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
