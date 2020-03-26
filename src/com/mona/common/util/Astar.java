package com.mona.common.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/***/
public class Astar {
    private int[][] map_array;
    private int origin_x, origin_y;
    //    private int current_x, current_y;
    private int target_x, target_y;
    private List<Node> open_list;
    private boolean[][] close_array;
    private SearchListener searchListener;

    public Astar(int[][] map_array) {
        this.map_array = map_array;
        open_list = new ArrayList<>();
        close_array = new boolean[map_array.length][map_array[0].length];
    }

    public void setOrigin(int origin_x, int origin_y) {
        this.origin_x = origin_x;
        this.origin_y = origin_y;
    }

    public void setTarget(int target_x, int target_y) {
        this.target_x = target_x;
        this.target_y = target_y;
    }

    public int[] find() {
        open_list.clear();
        Node current = new Node();
        current.setData(origin_x, origin_y, target_x, target_y);
        open_list.add(current);
        while (!open_list.isEmpty()) {
            current = open_list.remove(0);
            if (searchListener != null) {
                searchListener.onNext(current);
            }
            System.err.println("(" + current.index_x + "," + current.index_y + ")=" + map_array[current.index_y][current.index_x]);
            if (current.index_x == target_x && current.index_y == target_y) {
                break;
            }
            next(current);
            open_list.sort(Comparator.comparingInt(e -> e.f));
        }
        return null;
    }

    private void next(Node current) {
        close_array[current.index_y][current.index_x] = true;
        int data = map_array[current.index_x][current.index_y];
        if ((data & 0B1000) != 0) {
            left(current);
        }
        if ((data & 0B0100) != 0) {
            up(current);
        }
        if ((data & 0B0010) != 0) {
            right(current);
        }
        if ((data & 0B0001) != 0) {
            down(current);
        }
    }

    private void left(Node current) {
        if (current.index_x == 0) return;
        if (close_array[current.index_y][current.index_x - 1]) return;
        current = new Node(current.index_x - 1, current.index_y, target_x, target_y);
        open_list.add(current);
    }

    private void up(Node current) {
        if (current.index_y == 0) return;
        if (close_array[current.index_y - 1][current.index_x]) return;
        current = new Node(current.index_x, current.index_y - 1, target_x, target_y);
        open_list.add(current);
    }

    private void right(Node current) {
        if (current.index_x == map_array[0].length - 1) return;
        if (close_array[current.index_y][current.index_x + 1]) return;
        current = new Node(current.index_x + 1, current.index_y, target_x, target_y);
        open_list.add(current);
    }

    private void down(Node current) {
        if (current.index_y == map_array.length - 1) return;
        if (close_array[current.index_y + 1][current.index_x]) return;
        current = new Node(current.index_x, current.index_y + 1, target_x, target_y);
        open_list.add(current);
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public interface SearchListener {
        void onNext(Node node);
    }

    public static class Node {
        public int index_x;
        public int index_y;
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
         * 前驱元素
         */
        public Node prev;

        public Node() {

        }

        public Node(int index_x, int index_y, int target_x, int target_y) {
            setData(index_x, index_y, target_x, target_y);
        }

        private void setData(int index_x, int index_y, int target_x, int target_y) {
            this.index_x = index_x;
            this.index_y = index_y;
            this.g = 0;
            this.h = Math.abs(target_x - index_x) + Math.abs(target_y - index_y);
            this.f = this.g + this.h;
        }
    }
}
