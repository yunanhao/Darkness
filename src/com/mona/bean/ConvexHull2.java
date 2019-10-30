package com.mona.bean;

import java.util.Scanner;

public class ConvexHull2 {
    public static int[][] g_result = new int[240][2];

    /*getResult()实现功能：以坐标P0(x1,y1)和Pn(x2,y2)为直线，找出pack里面里这条直线最远的点Pmax
     *并找出直线P0Pmax和PmaxPn的上包，进行递归。
     *注：Pack[0][0]存放点的个数，pack[1]开始存放点的坐标。
     *全局变量g_result[][]用来存放凸包上的点，即最终所要的答案。同样g_result[0][0]存放的是已找到的点的个数。
     **/
    public static void getResult(int[][] Pack, int x1, int y1, int x2, int y2) {
        int i, t, x3, y3, R, Rmax, tmax;
        int[][] ResultPack = new int[240][2];
        ResultPack[0][0] = 0;
        if (Pack[0][0] <= 1) {
            return;
        }
        x3 = Pack[1][0];
        y3 = Pack[1][1];
        R = x1 * y2 + x3 * y1 + x2 * y3 - x3 * y2 - x2 * y1 - x1 * y3;
        Rmax = R;
        tmax = 1;
        for (i = 2; i <= Pack[0][0]; i++) {
            x3 = Pack[i][0];
            y3 = Pack[i][1];
            R = x1 * y2 + x3 * y1 + x2 * y3 - x3 * y2 - x2 * y1 - x1 * y3;
            if (R >= 0) {
                t = ++ResultPack[0][0];
                ResultPack[t][0] = x3;
                ResultPack[t][1] = y3;
            }
            if (R > Rmax) {
                Rmax = R;
                tmax = i;
            }
        }
        if (Rmax <= 0) {
            for (i = 1; i < ResultPack[0][0]; i++) {
                x3 = ResultPack[i][0];
                y3 = ResultPack[i][1];
                R = x1 * y2 + x3 * y1 + x2 * y3 - x3 * y2 - x2 * y1 - x1 * y3;
                if (R == 0 && !(x3 == x2 && y3 == y2 || x3 == x1 && y3 == y1)) {
                    t = ++g_result[0][0];
                    g_result[t][0] = ResultPack[i][0];
                    g_result[t][1] = ResultPack[i][1];
                }
            }
            return;
        } else {
            t = ++g_result[0][0];
            g_result[t][0] = Pack[tmax][0];
            g_result[t][1] = Pack[tmax][1];
            if (ResultPack[0][0] == 0) {
                return;
            }
        }
        getResult(ResultPack, x1, y1, Pack[tmax][0], Pack[tmax][1]);
        getResult(ResultPack, Pack[tmax][0], Pack[tmax][1], x2, y2);
    }

    public static void main(String[] args) {
        int[][] Point = new int[240][2];// Point存所有点。
        int i = 1;
        int x1, y1, x2, y2, x3, y3;
        g_result[0][0] = 0;
        Point[0][0] = 0;// Point的第一行第一列元素存放包里面有几个点。初始化为0。
        System.out.println("请输入所有点的坐标：\n");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String point[] = string.split(",");
            if (point.length != 2) {
                break;
            }
            int x = Integer.parseInt(point[0]);
            int y = Integer.parseInt(point[1]);
            i++;
            Point[i][0] = x;
            Point[i][1] = y;
        }
        scanner.close();
        Point[0][0] = i - 1;
        x1 = Point[1][0];
        y1 = Point[1][1];
        x2 = x1;
        y2 = y1;
        for (i = 2; i <= Point[0][0]; i++) {
            x3 = Point[i][0];
            y3 = Point[i][1];
            if (x3 < x1) {
                x1 = x3;
                y1 = y3;
            } else if (x3 > x2) {
                x2 = x3;
                y2 = y3;
            }
        }
        g_result[1][0] = x1;
        g_result[1][1] = y1;
        g_result[2][0] = x2;
        g_result[2][1] = y2;
        g_result[0][0] += 2;
        getResult(Point, x1, y1, x2, y2);
        getResult(Point, x2, y2, x1, y1);

        System.out.println("\n\n构成凸包的点有：\n");
        for (i = 1; i <= g_result[0][0]; i++) {
            System.out.printf("(%d,%d)\n", g_result[i][0], g_result[i][1]);
        }
    }

    public static class Node<T> {
        public Node<T> previous;
        public Node<T> next;
        public Node<T> root;
        public Node<T> parent;
        public Node<T> child;
        public Node<T>[] parents;
        public Node<T>[] childs;
        public T current;
    }
}
