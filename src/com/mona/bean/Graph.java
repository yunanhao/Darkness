package com.mona.bean;

import org.junit.Test;

/**
 * 有向图
 */
public class Graph<DATA, COST extends Comparable<? super COST>> {
    /**
     * 图中所有的顶点对象
     */
    private Vertex<DATA, COST>[] vertexs;

    public Graph() {
        vertexs = new Vertex[0];
    }

    public int size() {
        return vertexs.length;
    }
    @Test
    public void test() {
        Graph<String, Integer> graph = new Graph<>();
        graph.link("a", "b", 2);
        graph.link("b", "c", 5);
        graph.link("a", "d", 7);
        System.out.println(graph);
    }

    public void link(DATA origin, DATA target, COST cost) {
        Vertex<DATA, COST> vertexOrigin = getVertex(origin);
        Vertex<DATA, COST> vertexTarget = getVertex(target);
        if (vertexOrigin == null) {
            vertexOrigin = createVertex(origin);
        }
        if (vertexTarget == null) {
            vertexTarget = createVertex(target);
        }
        vertexOrigin.connect(vertexTarget, cost);
    }

    public boolean contains(DATA data) {
        for (Vertex<DATA, COST> v : vertexs) {
            if (data.equals(v.mData)) {
                return true;
            }
        }
        return false;
    }

    public Vertex<DATA, COST> getVertex(DATA data) {
        for (Vertex<DATA, COST> v : vertexs) {
            if (data.equals(v.mData)) {
                return v;
            }
        }
        return null;
    }

    public Vertex<DATA, COST> createVertex(DATA data) {
        Vertex<DATA, COST> vertex = getVertex(data);
        if (vertex != null) {
            return vertex;
        }
        int length = this.vertexs.length;
        Vertex<DATA, COST>[] vertexes = new Vertex[length + 1];
        System.arraycopy(this.vertexs, 0, vertexes, 0, length);
        vertex = new Vertex<>();
        vertex.setDATA(data);
        vertexes[length] = vertex;
        this.vertexs = vertexes;
        return vertexes[length];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex vertex : vertexs) {
            sb.append(vertex);
            sb.append('\n');
        }
        return sb.toString();
    }

    public static final class Vertex<DATA, COST extends Comparable<? super COST>> {
        /**
         * 顶点包含的数据体对象
         */
        private DATA mData;
        /**
         * 坐标
         */
        private int[] mLocation;
        /**
         * 附近的邻居
         */
        private Edge<DATA, COST> mFirstEdge;

        /**
         * 得到节点的数据
         *
         * @return 数据对象
         */
        public DATA getDATA() {
            return mData;
        }

        /**
         * 设置节点的数据
         *
         * @param data 数据对象
         */
        public Vertex<DATA, COST> setDATA(DATA data) {
            this.mData = data;
            return this;
        }

        /**
         * 增加一条边
         *
         * @param target 边
         * @param cost 花费
         * @return 是否操作成功
         */
        private boolean connect(Vertex<DATA, COST> target, COST cost) {
            Edge<DATA, COST> edge = new Edge<>(this, target, cost);
            if (mFirstEdge != null) {
                edge.setNext(mFirstEdge);
            }
            mFirstEdge = edge;
            return true;
        }

        /**
         * 删除顶点
         *
         * @param target 边
         * @return 是否操作成功
         */
        public boolean disconnect(Vertex<DATA, COST> target) {
            if (mFirstEdge == null) {
                return false;
            }
            Edge<DATA, COST> prev = mFirstEdge;
            Edge<DATA, COST> next = mFirstEdge.next;
            while (next != null) {
                if (next.getTarget() == target) {
                    prev.setNext(next.getNext());
                    return true;
                }
                prev = next;
                next = next.getNext();
            }
            return false;
        }

        /**
         * 是否可以直接到达目标顶点
         *
         * @param vertex 顶点对象
         * @return true表示可以直达
         */
        public boolean contains(Vertex<DATA, COST> target) {
            Edge<DATA, COST> next = mFirstEdge;
            while (next != null) {
                if (next.getTarget() == target) {
                    return true;
                }
                next = next.getNext();
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(mData);
            sb.append(":");
            Edge<DATA, COST> next = mFirstEdge;
            while (next != null) {
                sb.append(next);
                next = next.getNext();
            }
            return sb.toString();
        }

        public Edge<DATA, COST> getEdge(Vertex<DATA, COST> target) {
            for (Edge<DATA, COST> next = mFirstEdge; next != null; next = next.getNext()) {
                if (next.getTarget() == target) {
                    return next;
                }
            }
            return null;
        }
    }

    public static final class Edge<DATA, COST extends Comparable<? super COST>> {
        /**
         * 起点 终点
         */
        private Vertex<DATA, COST> mOrigin, mTarget;
        /**
         * 用于组成边的链表
         */
        private Edge<DATA, COST> next;
        /**
         * 从起点到终点需要的花费
         */
        private COST mCost;

        public Edge(Vertex<DATA, COST> origin, Vertex<DATA, COST> target, COST cost) {
            this.mOrigin = origin;
            this.mTarget = target;
            this.mCost = cost;
        }

        public COST getCost() {
            return mCost;
        }

        public void setCost(COST cost) {
            this.mCost = cost;
        }

        public Edge<DATA, COST> getNext() {
            return next;
        }

        public void setNext(Edge<DATA, COST> next) {
            this.next = next;
        }

        public Vertex<DATA, COST> getTarget() {
            return mTarget;
        }

        public Vertex<DATA, COST> getOrigin() {
            return mOrigin;
        }

        @Override
        public String toString() {
            return "[" +
                    mTarget.getDATA() +
                    "-->" +
                    getCost() +
                    ']';
        }
    }
}
