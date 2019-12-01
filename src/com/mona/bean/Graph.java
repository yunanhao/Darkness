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

    @Test
    public void test() {
        Graph<String, Integer> graph = new Graph<>();
        graph.link("a", "b", 2);
        graph.link("b", "c", 5);
        graph.link("a", "d", 7);
        System.out.println(graph);
    }

    private void link(DATA origin, DATA target, COST cost) {
        Vertex<DATA, COST> vertexOrigin = getVertex(origin);
        Vertex<DATA, COST> vertexTarget = getVertex(target);
        Edge<DATA, COST> edge;
        boolean needCreateEdge = false;
        if (vertexOrigin == null) {
            vertexOrigin = new Vertex<>();
            vertexOrigin.setDATA(origin);
            needCreateEdge = true;
        }
        if (vertexTarget == null) {
            vertexTarget = new Vertex<>();
            vertexTarget.setDATA(target);
            needCreateEdge = true;
        }
//        if (needCreateEdge){
        edge = new Edge<>(vertexOrigin, vertexTarget, cost);
//        }else {
//            edge = vertexOrigin.getEdge(vertexTarget);
//        }
        vertexOrigin.add(edge);
        vertexTarget.add(edge);
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

    Vertex<DATA, COST> createVertex(DATA data) {
        int length = this.vertexs.length;
        Vertex<DATA, COST>[] vertexs = new Vertex[length + 1];
        System.arraycopy(this.vertexs, 0, vertexs, 0, length);
        vertexs[length] = (Vertex<DATA, COST>) new Vertex<>().setDATA(data);
        this.vertexs = vertexs;
        return vertexs[length];
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
         * @param edge 边
         * @return 是否操作成功
         */
        private boolean add(Edge<DATA, COST> edge) {
            if (mFirstEdge != null) {
                edge.put(this, mFirstEdge);
            }
            mFirstEdge = edge;
            return true;
        }

        /**
         * 删除一条边
         *
         * @param edge 边
         * @return 是否操作成功
         */
        public boolean del(Edge<DATA, COST> edge) {
            if (mFirstEdge == null) {
                return false;
            }
            Edge<DATA, COST> next = mFirstEdge;
            while (true) {
                if (edge == next) {
                    mFirstEdge = next.next(this);
                    return true;
                }
                next = next.next(this);
                if (next == null) {
                    return false;
                }
            }
        }

        /**
         * 是否可以直接到达目标顶点
         *
         * @param vertex 顶点对象
         * @return true表示可以直达
         */
        public boolean contains(Vertex<DATA, COST> vertex) {
            Edge<DATA, COST> next = mFirstEdge;
            while (next != null) {
                if (next.getCost(vertex) != null) {
                    return true;
                }
                next = next.next(this);
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
                sb.append(next.target(this).mData);
                next = next.next(this);
            }
            return sb.toString();
        }

        public Edge<DATA, COST> getEdge(Vertex<DATA, COST> target) {
            for (Edge<DATA, COST> next = mFirstEdge; next != null; next = next.next(this)) {
                if (next.getCost(target) != null) {
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
        private Edge<DATA, COST> left, right;
        /**
         * 从起点到终点需要的花费
         */
        private COST mCost, mInvCost;

        public Edge(Vertex<DATA, COST> origin, Vertex<DATA, COST> target, COST cost) {
            this.mOrigin = origin;
            this.mTarget = target;
            this.mCost = cost;
        }

        public void 反向连通() {

        }

        public void 反向连通(COST invCost) {

        }

        public COST getCost(Vertex<DATA, COST> target) {
            if (target == mTarget) {
                return mCost;
            }
            if (target == mOrigin) {
                return mInvCost;
            }
            return null;
        }

        /**
         * 为目标顶点增加一条边
         */
        private Edge<DATA, COST> put(Vertex<DATA, COST> origin, Edge<DATA, COST> edge) {
            if (mOrigin == origin) {
                right = edge;
                return edge;
            }
            if (mTarget == origin) {
                left = edge;
                return edge;
            }
            return null;
        }

        private Edge<DATA, COST> next(Vertex<DATA, COST> origin) {
            if (mOrigin == origin) {
                return right;
            }
            if (mTarget == origin) {
                return left;
            }
            return null;
        }

        public Vertex<DATA, COST> target(Vertex<DATA, COST> origin) {
            if (mOrigin == origin) {
                return mTarget;
            }
            if (mTarget == origin) {
                return mOrigin;
            }
            return null;
        }
    }
}
