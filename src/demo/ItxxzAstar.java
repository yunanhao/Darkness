package demo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: IT学习者-螃蟹
 * @官网: www.itxxz.com
 * @version: 2015年3月3日 下午2:34:02
 */
public class ItxxzAstar {
    // 迷宫数组地图
    private static final int[][] mazeArray = {{1, 0, 0, 0, 0}, {1, 0, 2, 0, 0}, {1, 0, 0, 0, 1}, {1, 0, 0, 0, 0},
            {1, 1, 1, 1, 0}, {1, 0, 0, 0, 0}, {3, 0, 1, 1, 1}};
    // 开启队列，用于存放待处理的节点
    Queue<Point> openQueue = null;
    // 关闭队列，用于存放已经处理过的节点
    Queue<Point> closedQueue = null;
    // 起始节点到某个节点的距离
    int[][] FList = null;
    // 某个节点到目的节点的距离
    int[][] GList = null;
    // 起始节点经过某个节点到目的节点的距离
    int[][] HList = null;
    // 开始节点
    private Point startPoint = null;
    // 当前节点
    private Point endPoint = null;
    // 结束节点
    private Point currentPoint = null;
    // 最短距离坐标节点
    private Point shortestFPoint = null;
    // 迷宫坐标对象
    private Point[][] mazePoint = null;

    /**
     * 构造函数
     *
     * @param maze       迷宫图
     * @param startPoint 起始节点
     * @param endPoint   结束节点
     */
    public ItxxzAstar(final Point[][] mazePoint, final Point startPoint, final Point endPoint) {
        this.mazePoint = mazePoint;
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        openQueue = new LinkedList<Point>();
        openQueue.offer(startPoint);

        closedQueue = new LinkedList<Point>();

        FList = new int[mazePoint.length][mazePoint[0].length];
        GList = new int[mazePoint.length][mazePoint[0].length];
        HList = new int[mazePoint.length][mazePoint[0].length];

        for (int i = 0; i < mazePoint.length; i++) {
            for (int j = 0; j < mazePoint[0].length; j++) {
                FList[i][j] = Integer.MAX_VALUE;
                GList[i][j] = Integer.MAX_VALUE;
                HList[i][j] = Integer.MAX_VALUE;
            }
        }

        // 起始节点到当前节点的距离
        GList[startPoint.getX()][startPoint.getY()] = 0;
        // 当前节点到目的节点的距离
        HList[startPoint.getX()][startPoint.getY()] = getPointDistance(startPoint.getX(), startPoint.getY(), endPoint
                .getX(), endPoint.getY());
        // f(x) = g(x) + h(x)
        FList[startPoint.getX()][startPoint.getY()] = GList[startPoint.getX()][startPoint.getY()] + HList[startPoint
                .getX()][startPoint.getY()];

    }

    /**
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午2:34:02
     */

    public static void main(final String[] args) {

        // 创建节点迷宫图
        final Point[][] mazePoint = new Point[mazeArray.length][mazeArray[0].length];
        for (int i = 0; i < mazePoint.length; i++) {
            for (int j = 0; j < mazePoint[0].length; j++) {
                mazePoint[i][j] = new Point(i, j, mazeArray[i][j]);
            }
        }

        final Point start = mazePoint[1][2];
        final Point end = mazePoint[6][0];

        final ItxxzAstar star = new ItxxzAstar(mazePoint, start, end);
        star.start();

        System.out.println(mazeArray.length + "," + mazeArray[0].length);

        star.printPath();

    }

    /**
     * 数组迷宫地图
     *
     * 0、可通行 1、障碍 2、开始节点 3、结束节点
     *
     */

    /**
     * 计算当前坐标与结束坐标之间的距离
     * <p>
     * 计算方法为每向相信坐标移动一次算作一个距离单位
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:04:37
     */
    private int getPointDistance(final int current_x, final int current_y, final int end_x, final int end_y) {
        return Math.abs(current_x - end_x) + Math.abs(current_y - end_y);
    }

    /**
     * 开始迷宫搜索
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:18:05
     */
    public void start() {
        while ((currentPoint = findShortestFPoint()) != null) {
            if (currentPoint.getX() == endPoint.getX() && currentPoint.getY() == endPoint.getY()) {
                return;
            }
            updateNeighborPoints(currentPoint);
        }
    }

    /**
     * 获取距离最短的坐标点
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:20:26
     */
    public Point findShortestFPoint() {
        currentPoint = null;
        shortestFPoint = null;
        int shortestFValue = Integer.MAX_VALUE;

        final Iterator<Point> it = openQueue.iterator();

        while (it.hasNext()) {
            currentPoint = it.next();
            if (FList[currentPoint.getX()][currentPoint.getY()] <= shortestFValue) {
                shortestFPoint = currentPoint;
                shortestFValue = FList[currentPoint.getX()][currentPoint.getY()];
            }
        }

        if (shortestFValue != Integer.MAX_VALUE) {
            System.out.println("【移除节点】:" + shortestFPoint.getValue() + "[" + shortestFPoint.getX() + "," + shortestFPoint
                    .getY() + "]");

            openQueue.remove(shortestFPoint);
            closedQueue.offer(shortestFPoint);
        }

        return shortestFPoint;
    }

    /**
     * 更新临近节点
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:47:03
     */
    private void updateNeighborPoints(final Point currentPoint) {
        final int current_x = currentPoint.getX();
        final int current_y = currentPoint.getY();
        System.out.println("当前节点：[" + current_x + "," + current_y + "]");
        // 上
        if (checkPosValid(current_x - 1, current_y)) {
            System.out.print("上");
            updatePoint(mazePoint[current_x][current_y], mazePoint[current_x - 1][current_y]);
        }
        // 下
        if (checkPosValid(current_x + 1, current_y)) {
            System.out.print("下");
            updatePoint(mazePoint[current_x][current_y], mazePoint[current_x + 1][current_y]);
        }
        // 左
        if (checkPosValid(current_x, current_y - 1)) {
            System.out.print("左");
            updatePoint(mazePoint[current_x][current_y], mazePoint[current_x][current_y - 1]);
        }
        // 右
        if (checkPosValid(current_x, current_y + 1)) {
            System.out.print("右");
            updatePoint(mazePoint[current_x][current_y], mazePoint[current_x][current_y + 1]);
        }
        System.out.println("---------------");
    }

    /**
     * 检查该节点是否有效
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:48:25
     */
    private boolean checkPosValid(final int x, final int y) {
        // 检查x,y是否越界， 并且当前节点不是墙
        if (x >= 0 && x < mazePoint.length && y >= 0 && y < mazePoint[0].length && mazePoint[x][y].getValue() != 1) {
            // 检查当前节点是否已在关闭队列中，若存在，则返回 "false"
            final Iterator<Point> it = closedQueue.iterator();
            Point point = null;
            while (it.hasNext()) {
                if ((point = it.next()) != null) {
                    if (point.getX() == x && point.getY() == y) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 更新当前节点
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:46:53
     */
    private void updatePoint(final Point lastPoint, final Point currentPoint) {
        final int last_x = lastPoint.getX();
        final int last_y = lastPoint.getY();
        final int current_x = currentPoint.getX();
        final int current_y = currentPoint.getY();

        // 起始节点到当前节点的距离
        final int temp_g = GList[last_x][last_y] + 1;
        // 当前节点到目的位置的距离
        System.out.print("  [" + current_x + "," + current_y + "]" + mazePoint[current_x][current_y].getValue());
        final int temp_h = getPointDistance(current_x, current_y, endPoint.getX(), endPoint.getY());
        System.out.println("到目的位置的距离 :" + temp_h);
        // f(x) = g(x) + h(x)
        final int temp_f = temp_g + temp_h;
        System.out.println("f(x) = g(x) + h(x) :" + temp_f + "=" + temp_g + "+" + temp_h);

        // 如果当前节点在开启列表中不存在，则：置入开启列表，并且“设置”
        // 1) 起始节点到当前节点距离
        // 2) 当前节点到目的节点的距离
        // 3) 起始节点到目的节点距离
        if (!openQueue.contains(currentPoint)) {
            openQueue.offer(currentPoint);
            currentPoint.setFather(lastPoint);
            System.out.println("添加到开启列表:" + currentPoint.getValue() + "[" + currentPoint.getX() + "," + currentPoint.getY()
                    + "]");
            // 起始节点到当前节点的距离
            GList[current_x][current_y] = temp_g;
            // 当前节点到目的节点的距离
            HList[current_x][current_y] = temp_h;
            // f(x) = g(x) + h(x)
            FList[current_x][current_y] = temp_f;
        } else {

            // 如果当前节点在开启列表中存在，并且，
            // 从起始节点、经过上一节点到当前节点、至目的地的距离 < 上一次记录的从起始节点、到当前节点、至目的地的距离，
            // 则：“更新”
            // 1) 起始节点到当前节点距离
            // 2) 当前节点到目的节点的距离
            // 3) 起始节点到目的节点距离
            if (temp_f < FList[current_x][current_y]) {
                // 起始节点到当前节点的距离
                GList[current_x][current_y] = temp_g;
                // 当前节点到目的位置的距离
                HList[current_x][current_y] = temp_h;
                // f(x) = g(x) + h(x)
                FList[current_x][current_y] = temp_f;
                // 更新当前节点的父节点
                currentPoint.setFather(lastPoint);
            }
            System.out.println("currentPoint:" + currentPoint.getValue() + "[" + currentPoint.getX() + "," + currentPoint
                    .getY() + "]");
            System.out.println("currentPoint.father:" + currentPoint.getFather().getValue() + "[" + currentPoint.getFather()
                    .getX() + "," + currentPoint.getFather().getY() + "]");
        }
    }

    /**
     * 打印行走路径
     *
     * @author: IT学习者-螃蟹
     * @官网: www.itxxz.com
     * @version: 2015年3月3日 下午3:46:39
     */
    public void printPath() {
        System.out.println("================ 开始打印行走路径【用 8 表示】 ================");
        Point father_point = null;
        final int[][] result = new int[mazeArray.length][mazeArray[0].length];
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                result[i][j] = 0;
            }
        }

        int step = 0;
        father_point = mazePoint[endPoint.getX()][endPoint.getY()];
        while (father_point != null) {
            System.out.println("【father_point】" + father_point.getValue() + "[" + father_point.getX() + "," + father_point
                    .getY() + "]");
            if (father_point.equals(startPoint)) {
                result[father_point.getX()][father_point.getY()] = 2;
            } else if (father_point.equals(endPoint)) {
                result[father_point.getX()][father_point.getY()] = 3;
                step++;
            } else {
                result[father_point.getX()][father_point.getY()] = 8;
                step++;
            }
            father_point = father_point.getFather();
        }
        // 打印行走步数
        System.out.println("step is : " + step);
        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }

}

class Point {
    int x, y, value;
    Point parent;

    public Point(final int x, final int y, final int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public Point getFather() {
        return parent;
    }

    public void setFather(final Point lastPoint) {
        parent = lastPoint;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

}