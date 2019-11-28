package com.mona.astar;

import java.util.Comparator;

// 节点类
public class Node implements Comparable<Node>, Comparator<Node> {
    /**
     * 节点坐标
     */
    public int x, y;
    /**
     * F(n)=G(n)+H(n)
     */
    public int f;
    /**
     * 起点到当前点的移动耗费
     */
    public int g;
    /**
     * 当前点到终点的移动耗费，即
     * 曼哈顿距离|x1-x2|*水平方向单元格宽度+
     * |y1-y2|*垂直方向单元格宽度(忽略障碍物)
     */
    public int h;
    /**
     * 父节点
     */
    public Node parent;

    /**
     * 通过给定值构造一个节点对象
     */
    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    /**
     * 通过给定节点构造一个新的节点对象
     */
    public Node(Node node) {
        x = node.x;
        y = node.y;
        f = node.f;
        g = node.g;
        h = node.h;
        parent = node.parent;
    }

    /**
     * 将节点重新赋值
     */
    public void reset(Node node) {
        x = node.x;
        y = node.y;
        f = node.f;
        g = node.g;
        h = node.h;
        parent = node.parent;
    }

    @Override
    public int compareTo(Node node) {
        if (f > node.f) return 1;
        else if (f < node.f) return -1;
        else return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        if (x != other.x) return false;
        return y == other.y;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ") F=" + f + "";
    }

    @Override
    public int compare(Node node, Node other) {
        if (node.f > other.f) return 1;
        else if (node.f < other.f) return -1;
        else return 0;
    }

}
