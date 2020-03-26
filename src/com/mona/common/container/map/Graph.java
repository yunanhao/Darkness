package com.mona.common.container.map;

/**
 * 有向图
 */
public class Graph<DATA, COST extends Comparable<? super COST>> {
    /**
     * 图中所有的顶点对象
     */
    private Vertex<DATA, COST> firstVertex;

    public int size() {
        int i = 0;
        Vertex<DATA, COST> temp = firstVertex;
        while (temp != null) {
            temp = temp.next;
            i++;
        }
        return i;
    }

    public void link(DATA origin, DATA target, COST cost) {
        if (origin.equals(target)) {
            return;
        }
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
        Vertex<DATA, COST> temp = firstVertex;
        while (temp != null) {
            if (data.equals(temp.mData)) {
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public Vertex<DATA, COST> getVertex(DATA data) {
        Vertex<DATA, COST> temp = firstVertex;
        while (temp != null) {
            if (data.equals(temp.mData)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    public Vertex<DATA, COST> createVertex(DATA data) {
        Vertex<DATA, COST> vertex = getVertex(data);
        if (vertex != null) {
            return vertex;
        }
        vertex = new Vertex<>();
        vertex.setDATA(data);
        if (firstVertex != null) {
            firstVertex.prev = vertex;
            vertex.next = firstVertex;
        }
        firstVertex = vertex;
        return vertex;
    }

    public Vertex<DATA, COST> deleteVertex(DATA data) {
        for (Vertex<DATA, COST> vertex = firstVertex; vertex != null; vertex = vertex.next) {
            if (data.equals(vertex.mData)) {
                //重新连接图中节点的关系网
                if (vertex.prev != null) {
                    vertex.prev.next = vertex.next;
                } else {
                    firstVertex = vertex.next;
                }
                if (vertex.next != null) {
                    vertex.next.prev = vertex.prev;
                }
                //删除被删除节点的前驱元素中可以到达当前节点的边
                Edge<DATA, COST> edge = vertex.prevEdge;
                while (edge != null) {
                    if (vertex.equals(edge.mTarget)) {
                        edge.mOrigin.disconnect(vertex);
                    }
                    edge = edge.mPrev;
                }
                //删除被删除的节点可以抵达的目标节点所持有的前驱
                edge = vertex.nextEdge;
                while (edge != null) {
                    edge.mTarget.delPrev(edge);
                    edge = edge.mNext;
                }
                return vertex;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append('"');
        sb.append("graph");
        sb.append('"');
        sb.append(':');
        sb.append('[');
        for (Vertex<DATA, COST> vertex = firstVertex; vertex != null; vertex = vertex.next) {
            sb.append('{');
            sb.append(vertex);
            sb.append('}');
            sb.append(',');
            sb.append('\n');
        }
        if (sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(']');
        sb.append('}');
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

        private Edge<DATA, COST> prevEdge;
        private Edge<DATA, COST> nextEdge;

        private Vertex<DATA, COST> prev;
        private Vertex<DATA, COST> next;

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
         * 与目标节点进行连接
         *
         * @param target 边
         * @param cost   花费
         * @return 是否操作成功
         */
        private boolean connect(Vertex<DATA, COST> target, COST cost) {
            Edge<DATA, COST> edge = getEdge(target);
            if (edge != null) {
                return true;
            }
            edge = new Edge<>(this, target, cost);
            if (nextEdge != null) {
                edge.mNext = nextEdge;
            }
            nextEdge = edge;
            target.addPrev(edge);
            return true;
        }

        /**
         * 断开和目标节点的连接
         *
         * @param target 边
         * @return 是否操作成功
         */
        private boolean disconnect(Vertex<DATA, COST> target) {
            Edge<DATA, COST> prev = null;
            Edge<DATA, COST> curr = nextEdge;
            while (curr != null) {
                if (curr.getTarget() == target) {
                    if (curr == nextEdge) {
                        nextEdge = nextEdge.mNext;
                    } else {
                        prev.mNext = curr.mNext;
                    }
                    target.delPrev(curr);
                    return true;
                }
                prev = curr;
                curr = curr.mNext;
            }
            return false;
        }

        /**
         * 注册前驱节点
         */
        private void addPrev(Edge<DATA, COST> temp) {
            if (prevEdge == null) {
                prevEdge = temp;
            } else {
                temp.mPrev = prevEdge;
                prevEdge = temp;
            }
        }

        /**
         * 删除前驱节点
         */
        private boolean delPrev(Edge<DATA, COST> temp) {
            Edge<DATA, COST> prev = null;
            Edge<DATA, COST> curr = prevEdge;
            while (curr != null) {
                if (curr == temp) {
                    if (curr == prevEdge) {
                        prevEdge = prevEdge.mPrev;
                    } else {
                        prev.mPrev = curr.mPrev;
                    }
                    return true;
                }
                prev = curr;
                curr = curr.mPrev;
            }
            return false;
        }

        /**
         * 获取到达目标节点的路径
         *
         * @return 如果不可达则返回null
         */
        private Edge<DATA, COST> getEdge(Vertex<DATA, COST> target) {
            for (Edge<DATA, COST> next = nextEdge; next != null; next = next.mNext) {
                if (next.getTarget() == target) {
                    return next;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('"');
            sb.append(mData);
            sb.append('"');
            sb.append(':');
            sb.append('{');
            sb.append('"');
            sb.append("prev");
            sb.append('"');
            sb.append(':');
            sb.append('[');
            Edge<DATA, COST> next = prevEdge;
            while (next != null) {
                sb.append('"');
                sb.append(next.mOrigin.mData);
                sb.append('"');
                sb.append(',');
                next = next.mPrev;
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(']');
            sb.append(',');
            sb.append('"');
            sb.append("next");
            sb.append('"');
            sb.append(':');
            sb.append('[');
            next = nextEdge;
            while (next != null) {
                sb.append('"');
                sb.append(next.mTarget.mData);
                sb.append(':');
                sb.append(next.mCost);
                sb.append('"');
                sb.append(',');
                next = next.mNext;
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(']');
            sb.append('}');
            return sb.toString();
        }
    }

    public static final class Edge<DATA, COST extends Comparable<? super COST>> {
        private Edge<DATA, COST> mPrev, mNext;
        /**
         * 起点 终点
         */
        private Vertex<DATA, COST> mOrigin, mTarget;
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

        public Vertex<DATA, COST> getTarget() {
            return mTarget;
        }

        public Vertex<DATA, COST> getOrigin() {
            return mOrigin;
        }

        @Override
        public String toString() {
            return "[" +
                    mOrigin.getDATA() +
                    "-->" +
                    mTarget.getDATA() +
                    ":" +
                    getCost() +
                    ']';
        }
    }
}
