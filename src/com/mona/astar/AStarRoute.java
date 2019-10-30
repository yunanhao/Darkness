package com.mona.astar;

import java.awt.*;
import java.util.ArrayList;

public class AStarRoute {
    public static final int DIR_NULL = 0;
    public static final int DIR_DOWN = 1; // 方向：下
    public static final int DIR_UP = 2; // 方向：上
    public static final int DIR_LEFT = 3; // 方向：左
    public static final int DIR_RIGHT = 4; // 方向：右
    private static final int EXIST = 1;
    private static final int NOT_EXIST = 0;
    private static final int ISEXIST = 0;
    private static final int EXPENSE = 1; // 自身的代价
    private static final int DISTANCE = 2; // 距离的代价
    private static final int COST = 3; // 消耗的总代价
    private static final int FATHER_DIR = 4; // 父节点的方向
    /**
     * 地图矩阵，0表示能通过，1表示不能通过
     */
    private final int[][] map;
    private final int map_w;         // 地图宽度
    private final int map_h;         // 地图高度
    private final int start_x;       // 起点坐标X
    private final int start_y;       // 起点坐标Y

    private final int end_x;         // 终点坐标X
    private final int end_y;         // 终点坐标Y
    private final int astar_counter; // 算法嵌套深度
    public int openList[][][]; // 打开列表
    private boolean closeList[][]; // 关闭列表
    private int openListLength;
    private boolean isFound;       // 是否找到路径

    public AStarRoute(final int[][] map, final int start_x, final int start_y,
                      final int end_x, final int end_y) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.map = map;
        map_w = map.length;
        map_h = map[0].length;
        astar_counter = 5000;
        initCloseList();
        initOpenList(end_x, end_y);
    }

    /**
     * 添加关闭列表
     */
    private final void addCloseList(final int x, final int y) {
        closeList[x][y] = true;
    }

    /**
     * 添加一个新的节点
     */
    private final void addNewOpenList(final int x, final int y, final int newX,
                                      final int newY, final int dir) {
        if (isCanPass(newX, newY))
            if (openList[newX][newY][ISEXIST] == EXIST) {
                if (openList[x][y][EXPENSE] + getMapExpense(newX, newY) < openList[newX][newY][EXPENSE]) {
                    setFatherDir(newX, newY, dir);
                    setCost(newX, newY, x, y);
                }
            } else {
                addOpenList(newX, newY);
                setFatherDir(newX, newY, dir);
                setCost(newX, newY, x, y);
            }
    }

    /**
     * 添加打开列表
     */
    private final void addOpenList(final int x, final int y) {
        if (openList[x][y][ISEXIST] == NOT_EXIST) {
            openList[x][y][ISEXIST] = EXIST;
            openListLength++;
        }
    }

    /**
     * 寻路
     */
    private final void aStar(int x, int y) {
        // 控制算法深度
        for (int t = 0; t < astar_counter; t++) {
            if (x == end_x && y == end_y) {
                isFound = true;
                return;
            } else if (openListLength == 0) {
                isFound = false;
                return;
            }

            removeOpenList(x, y);
            addCloseList(x, y);

            // 该点周围能够行走的点
            addNewOpenList(x, y, x, y + 1, DIR_UP);
            addNewOpenList(x, y, x, y - 1, DIR_DOWN);
            addNewOpenList(x, y, x - 1, y, DIR_RIGHT);
            addNewOpenList(x, y, x + 1, y, DIR_LEFT);

            // 找到估值最小的点，进行下一轮算法
            int cost = 0x7fffffff;
            for (int i = 0; i < map_w; i++) {
                for (int j = 0; j < map_h; j++)
                    if (openList[i][j][ISEXIST] == EXIST) if (cost > getCost(i, j)) {
                        cost = getCost(i, j);
                        x = i;
                        y = j;
                    }
            }
        }
        // 算法超深
        isFound = false;
        return;
    }

    /**
     * 得到给定坐标格子此时的总消耗值
     */
    private int getCost(final int x, final int y) {
        return openList[x][y][COST];
    }

    /**
     * 得到距离的消耗值
     */
    private int getDistance(final int x, final int y, final int ex, final int ey) {
        return Math.abs(x - ex) + Math.abs(y - ey);
    }

    /**
     * 得到地图上这一点的消耗值
     */
    private int getMapExpense(final int x, final int y) {
        return 1;
    }

    /**
     * 寻径并获得寻路结果
     */
    public Point[] getResult() {
        Point[] result;
        ArrayList<Point> route;
        searchPath();
        if (!isFound) return null;
        route = new ArrayList<Point>();
        // openList是从目标点向起始点倒推的。
        int iX = end_x;
        int iY = end_y;
        while (iX != start_x || iY != start_y) {
            route.add(new Point(iX, iY));
            switch (openList[iX][iY][FATHER_DIR]) {
                case DIR_DOWN:
                    iY++;
                    break;
                case DIR_UP:
                    iY--;
                    break;
                case DIR_LEFT:
                    iX--;
                    break;
                case DIR_RIGHT:
                    iX++;
                    break;
            }
        }
        final int size = route.size();
        result = new Point[size];
        for (int i = 0; i < size; i++) {
            result[i] = new Point(route.get(i));
        }
        return result;
    }

    /**
     * 初始化关闭列表
     */
    private final void initCloseList() {
        closeList = new boolean[map_w][map_h];
        for (int i = 0; i < map_w; i++) {
            for (int j = 0; j < map_h; j++) {
                closeList[i][j] = false;
            }
        }
    }

    /**
     * 初始化打开列表
     */
    private final void initOpenList(final int ex, final int ey) {
        openList = new int[map_w][map_h][5];
        for (int i = 0; i < map_w; i++) {
            for (int j = 0; j < map_h; j++) {
                openList[i][j][ISEXIST] = NOT_EXIST;
                openList[i][j][EXPENSE] = getMapExpense(i, j);
                openList[i][j][DISTANCE] = getDistance(i, j, ex, ey);
                openList[i][j][COST] = openList[i][j][EXPENSE]
                        + openList[i][j][DISTANCE];
                openList[i][j][FATHER_DIR] = DIR_NULL;
            }
        }
        openListLength = 0;
    }

    /**
     * 判断一个点是否可以通过
     */
    private boolean isCanPass(final int x, final int y) {
        // 超出边界
        if (x < 0 || x >= map_w || y < 0 || y >= map_h) return false;
        // 地图不通
        if (map[x][y] != 0) return false;
        // 在关闭列表中
        return !isInCloseList(x, y);
    }

    /**
     * 判断一点是否在关闭列表中
     */
    private boolean isInCloseList(final int x, final int y) {
        return closeList[x][y];
    }

    /**
     * 移除打开列表的一个元素
     */
    private final void removeOpenList(final int x, final int y) {
        if (openList[x][y][ISEXIST] == EXIST) {
            openList[x][y][ISEXIST] = NOT_EXIST;
            openListLength--;
        }
    }

    /**
     * 开始寻路
     */
    private final void searchPath() {
        addOpenList(start_x, start_y);
        aStar(start_x, start_y);
    }

    /**
     * 设置消耗值
     */
    private final void setCost(final int x, final int y, final int ex,
                               final int ey) {
        openList[x][y][EXPENSE] = openList[ex][ey][EXPENSE] + getMapExpense(x, y);
        openList[x][y][DISTANCE] = getDistance(x, y, ex, ey);
        openList[x][y][COST] = openList[x][y][EXPENSE] + openList[x][y][DISTANCE];
    }

    /**
     * 设置父节点方向
     */
    private final void setFatherDir(final int x, final int y, final int dir) {
        openList[x][y][FATHER_DIR] = dir;
    }
}
