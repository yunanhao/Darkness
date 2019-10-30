package com.mona.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A星算法步骤：
 * 1.起点先添加到开启列表中
 * 2.开启列表中有节点的话，取出第一个节点，即最小F值的节点,
 * ->判断此节点是否是目标点，是则找到了，跳出;
 * ->根据此节点取得八个方向的节点，求出G，H，F值；
 * ->判断每个节点在地图中是否能通过，不能通过则加入关闭列表中，跳出;
 * ->判断每个节点是否在关闭列表中，在则跳出；
 * ->判断每个节点是否在开启列表中，在则更新G值，F值，还更新其父节点；不在则将其添加到开启列表中，计算G值，H值，F值，添加其节点。
 * 3.把此节点从开启列表中删除，再添加到关闭列表中；
 * 4.把开启列表中按照F值最小的节点进行排序，最小的F值在第一个；
 * 5.重复2，3，4步骤，直到目标点在开启列表中，即找到了；目标点不在开启列表中，开启列表为空，即没找到
 */
public class AStar {
    /**
     * 垂直或水平方向移动的路径评分
     */
    private final int COST_STRAIGHT = 64;
    /**
     * 斜方向移动的路径评分
     */
    private final int COST_DIAGONAL = 90;
    /**
     * 是否能斜向移动
     */
    public boolean isObliqueable;
    /**
     * 是否能直线移动
     */
    public boolean isStraightable;
    int count;
    /**
     * 地图数组
     */
    private int[][] map;
    /**
     * 障碍物数组
     */
    private int[] limit;
    /**
     * 不可达区域数组
     */
    private boolean[][] close;
    /**
     * 存放未关闭节点的集合
     */
    private List<Node> open;
    /**
     * 结果集
     */
    private List<Node> result;
    /**
     * 地图数组的宽度
     */
    private int mapW;
    /**
     * 地图数组的高度
     */
    private int mapH;
    /**
     * 起点节点对象
     */
    private Node startNode;
    /**
     * 当前节点对象
     */
    private Node current;
    /**
     * 判断通行的通用节点(另一个作用:Map所有key对应的键 )
     */
    private Node tempNode;

    /**
     * @param map   地图数组
     * @param limit 不可通行区域编号的数组
     */
    public AStar(int[][] map, int... limit) {
        tempNode = new Node(0, 0, null);
        startNode = new Node(0, 0, null);
        open = new ArrayList<Node>();
        result = new ArrayList<Node>();
        isObliqueable = false;
        isStraightable = true;
        init(map, limit);
    }

    /**
     * 重新初始化 提高类的复用性
     *
     * @param map   地图数组
     * @param limit 不可通行区域编号的数组
     */
    public AStar init(int[][] map, int... limit) {
        this.map = map;
        this.limit = limit;
        if (close == null || mapW != map[0].length || mapH != map.length) {
            mapW = map[0].length;
            mapH = map.length;
            close = new boolean[mapH][mapW];
        }
        return this;
    }

    /**
     * 程序入口
     * 查找核心算法
     */
    public List<Node> searchPath(int startX, int startY,
                                 int targetX, int targetY) {
        if (startX < 0 || startX >= mapW || targetX < 0 || targetX >= mapW
                || startY < 0 || startY >= mapH || targetY < 0 || targetY >= mapH)
            return null;
        // 查找障碍集合是否存在下一步的坐标
        for (int i = 0, y, x, h = close.length, w = close[0].length, len = limit.length; i < len; i++) {
            /** 将地图数组映射到一个布尔二维数组(可通行为true其他为false) */
            for (y = 0; y < h; y++) {
                for (x = 0; x < w; x++) {
                    close[y][x] = map[y][x] != limit[i];
                }
            }
        }
        count = 0;
        // 每次调用寻径时 先清空所有集合中的原有数据
        open.clear();
        // 起点先添加到开启列表中
        startNode.x = startX;
        startNode.y = startY;
        open.add(startNode);
        while (!open.isEmpty()) {
            // 开启列表中排序，把F值最低的放到最底端
            Collections.sort(open);
            // 从开启列表中取出并删除第一个节点(F为最低的)
            current = open.remove(0);
            // 判断是否找到目标点
            if (current.x == targetX && current.y == targetY) {
                result.clear();
                // 将终点到起点的路径添加到结果集中
                while (current.parent != null) {
                    result.add(current);
                    current = current.parent;
                }
                return result;
            }
            // 左
            if (isStraightable && current.x - 1 >= 0) {
                createNextStep(current.x - 1, current.y, targetX, targetY,
                        COST_STRAIGHT);
            }
            // 上
            if (isStraightable && current.y - 1 >= 0) {
                createNextStep(current.x, current.y - 1, targetX, targetY,
                        COST_STRAIGHT);
            }
            // 右
            if (isStraightable && current.x + 1 < mapW) {
                createNextStep(current.x + 1, current.y, targetX, targetY,
                        COST_STRAIGHT);
            }
            // 下
            if (isStraightable && current.y + 1 < mapH) {
                createNextStep(current.x, current.y + 1, targetX, targetY,
                        COST_STRAIGHT);
            }
            // 左上
            if (isObliqueable && current.x - 1 >= 0 && current.y - 1 >= 0) {
                createNextStep(current.x - 1, current.y - 1, targetX, targetY,
                        COST_DIAGONAL);
            }
            // 左下
            if (isObliqueable && current.x - 1 >= 0 && current.y + 1 < mapH) {
                createNextStep(current.x - 1, current.y + 1, targetX, targetY,
                        COST_DIAGONAL);
            }
            // 右上
            if (isObliqueable && current.x + 1 < mapW && current.y - 1 >= 0) {
                createNextStep(current.x + 1, current.y - 1, targetX, targetY,
                        COST_DIAGONAL);
            }
            // 右下
            if (isObliqueable && current.x + 1 < mapW && current.y + 1 < mapH) {
                createNextStep(current.x + 1, current.y + 1, targetX, targetY,
                        COST_DIAGONAL);
            }
            // 添加到关闭列表中
            close[current.y][current.x] = false;
        }
        return null;
    }

    /**
     * 根据坐标可否通行创建下一步
     */
    private boolean createNextStep(int x, int y,
                                   int targetX, int targetY, int cost) {
        // 查找关闭集合中是否存在下一步的坐标
        if (!close[y][x]) return false;
        // 查找开启列表中是否存在
        tempNode.x = x;
        tempNode.y = y;
        int index = open.indexOf(tempNode);
        Node node;
        if (index != -1) {
            // G值是否更小，即是否更新G，F值
            if (current.g + cost < open.get(index).g) {
                // 计算G F值
                tempNode.g = current.g + cost;
                tempNode.h = 0;
                tempNode.f = tempNode.g + tempNode.h;
                tempNode.parent = current;
                node = new Node(tempNode);
                // 替换原有节点
                open.set(index, node);
                ++count;
            }
        } else {
            // 计算G H F值
            tempNode.g = current.g + cost;
            tempNode.h = ((tempNode.x > targetX ? tempNode.x - targetX : targetX
                    - tempNode.x) << 6)
                    + ((tempNode.y > targetY ? tempNode.y - targetY : targetY - tempNode.y) << 6);
            tempNode.f = tempNode.g + tempNode.h;
            tempNode.parent = current;
            // 添加到开启列表中
            node = new Node(tempNode);
            open.add(node);
            ++count;
        }
        return true;
    }
}
