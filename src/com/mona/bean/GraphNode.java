package com.mona.bean;

/**
 * @author Master
 */
public class GraphNode<DATA> {
    private DATA data;
    private GraphCost[] costs;
    private GraphNode<DATA>[] prevs;

    /**
     * 得到节点的数据
     *
     * @return 数据对象
     */
    public DATA getDATA() {
        return data;
    }

    /**
     * 设置节点的数据
     *
     * @param data 数据对象
     */
    public void setDATA(DATA data) {
        this.data = data;
    }

    /**
     * 增加一个前驱节点
     *
     * @param graphNode 操作的节点对象
     * @return 是否操作成功
     */
    public boolean addPrev(GraphNode<DATA> graphNode) {
        return false;
    }

    /**
     * 删除一个前驱节点
     *
     * @param graphNode 操作的节点对象
     * @return 是否操作成功
     */
    public boolean delPrev(GraphNode<DATA> graphNode) {
        return false;
    }

    /**
     * 得到一个前驱节点
     *
     * @param index 需要获得的节点下标
     * @return 节点对象
     */
    public GraphNode<DATA> getPrev(int index) {
        return prevs[index];
    }

    /**
     * 得到所有的前驱节点
     *
     * @return 前驱节点集合
     */
    public GraphNode<DATA>[] getPrevList() {
        return prevs;
    }

    /**
     * 增加一个后继节点
     *
     * @param graphNode 操作的节点对象
     * @param cost      到达节点的消耗记录对象
     * @return 是否操作成功
     */
    public boolean addNext(GraphNode<DATA> graphNode, GraphCost cost) {
        return false;
    }

    /**
     * 删除一个后继节点
     *
     * @param graphNode 操作的节点对象
     * @return 是否操作成功
     */
    public boolean delNext(GraphNode<DATA> graphNode) {
        return false;
    }

    /**
     * 得到所有的后继节点
     *
     * @return 后继节点集合
     */
    public GraphNode<DATA>[] getNextList() {
        return null;
    }

    /**
     * 得到节点消耗
     *
     * @return 消耗对象
     */
    public GraphCost[] getCostList() {
        return costs;
    }
}
