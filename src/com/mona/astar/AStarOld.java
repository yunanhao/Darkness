package com.mona.astar;

import java.util.*;

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
public class AStarOld {
    /**
     * 不可通行的区域的编号
     */
    private final Set<Integer> obstacle;
    /**
     * 存放未关闭节点的集合
     */
    private final ArrayList<Node> open;
    /**
     * 存放已关闭节点的集合
     */
    private final Map<Node, Object> close;
    /**
     * 垂直或水平方向移动的路径评分
     */
    private final int COST_STRAIGHT = 64;
    /**
     * 斜方向移动的路径评分
     */
    private final int COST_DIAGONAL = 90;
    /**
     * 判断通行的通用节点(另一个作用:Map所有key对应的键 )
     */
    private final Node judgeNode;
    /**
     * 是否能斜向移动
     */
    public boolean isObliqueable;
    int count;
    /**
     * 地图数组
     */
    private int[][] map;
    /**
     * 地图数组的宽度
     */
    private int mapW;
    /**
     * 地图数组的高度
     */
    private int mapH;

    public AStarOld(final int[][] map, final Set<Integer> limit) {
        judgeNode = new Node(0, 0, null);
        open = new ArrayList<Node>();
        close = new HashMap<Node, Object>();
        obstacle = new HashSet<>();
        isObliqueable = true;
        init(map, limit);
    }

    /**
     * 重新初始化 提高类的复用性
     */
    public final AStarOld init(final int[][] map, final Set<Integer> limit) {
        this.map = map;
        obstacle.clear();
        obstacle.addAll(limit);
        mapW = map[0].length;
        mapH = map.length;
        return this;
    }

    /**
     * 程序入口
     * 查找核心算法
     */
    public final List<Node> searchPath(final int startX, final int startY,
                                       final int targetX, final int targetY) {
        if (startX < 0 || startX >= mapW || targetX < 0 || targetX >= mapW
                || startY < 0 || startY >= mapH || targetY < 0 || targetY >= mapH)
            return null;
        // 每次调用寻径时 先清空所有集合中的原有数据
        open.clear();
        close.clear();
        count = 0;
        // 起点先添加到开启列表中
        open.add(new Node(startX, startY, null));
        for (Node current = null; !open.isEmpty(); ) {
            // 开启列表中排序，把F值最低的放到最底端
            Collections.sort(open);
            // 从开启列表中取出并删除第一个节点(F为最低的)
            current = open.remove(0);
            // 判断是否找到目标点
            if (current.x == targetX && current.y == targetY) {
                final ArrayList<Node> result = new ArrayList<Node>();
                // 将终点到起点的路径添加到结果集中
                while (current.parent != null) {
                    result.add(current);
                    current = current.parent;
                }
                return result;
            }
            // 左
            if (current.x - 1 >= 0) {
                createNextStep(current.x - 1, current.y, targetX, targetY,
                        COST_STRAIGHT, current);
            }
            // 上
            if (current.y - 1 >= 0) {
                createNextStep(current.x, current.y - 1, targetX, targetY,
                        COST_STRAIGHT, current);
            }
            // 右
            if (current.x + 1 < mapW) {
                createNextStep(current.x + 1, current.y, targetX, targetY,
                        COST_STRAIGHT, current);
            }
            // 下
            if (current.y + 1 < mapH) {
                createNextStep(current.x, current.y + 1, targetX, targetY,
                        COST_STRAIGHT, current);
            }
            // 左上
            if (isObliqueable && current.x - 1 >= 0 && current.y - 1 >= 0) {
                createNextStep(current.x - 1, current.y - 1, targetX, targetY,
                        COST_DIAGONAL, current);
            }
            // 左下
            if (isObliqueable && current.x - 1 >= 0 && current.y + 1 < mapH) {
                createNextStep(current.x - 1, current.y + 1, targetX, targetY,
                        COST_DIAGONAL, current);
            }
            // 右上
            if (isObliqueable && current.x + 1 < mapW && current.y - 1 >= 0) {
                createNextStep(current.x + 1, current.y - 1, targetX, targetY,
                        COST_DIAGONAL, current);
            }
            // 右下
            if (isObliqueable && current.x + 1 < mapW && current.y + 1 < mapH) {
                createNextStep(current.x + 1, current.y + 1, targetX, targetY,
                        COST_DIAGONAL, current);
            }
            // 添加到关闭列表中
            close.put(current, judgeNode);
        }
        return null;
    }

    /**
     * 根据坐标可否通行创建下一步
     */
    private final boolean createNextStep(final int x, final int y,
                                         final int targetX, final int targetY, final int cost, final Node parent) {
        count++;
        final Node child = new Node(x, y, parent);
        // 查找障碍集合是否存在下一步的坐标
        if (obstacle.contains(map[y][x]))
            return close.put(child, judgeNode) == null && false;
        // 查找关闭集合中是否存在下一步的坐标
        if (close.containsKey(child)) return false;
        // 查找开启列表中是否存在
        final int index = open.indexOf(child);
        if (index == -1) {
            // 计算G H F值
            child.g = parent.g + cost;
            // TODO
            child.h = Math.abs(child.x - targetX) + Math.abs(child.y - targetY) << 4;
            child.f = child.g + child.h;
            // 添加到开启列表中
            open.add(child);
        } else {
            // G值是否更小，即是否更新G，F值
            if (parent.g + cost <= open.get(index).g) {
                // 计算G F值
                child.g = parent.g + cost;
                child.f = child.g + child.h;
                open.set(index, child);
            }
        }
        return true;
    }

}
