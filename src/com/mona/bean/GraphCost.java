package com.mona.bean;

/**
 * 记录起点到达终点所需要付出的消耗
 *
 * @author Master
 */
public interface GraphCost<DATA> extends Comparable<GraphCost> {
    /**
     * 获取记录的目标节点
     *
     * @return 获取目标节点
     */
    GraphNode<DATA> getTargetNode();

    /**
     * 与另一个需要付出的代价消耗进行对比
     *
     * @param o 需要比较的对象
     * @return 大小
     */
    @Override
    int compareTo(GraphCost o);
}
