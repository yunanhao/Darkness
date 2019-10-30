package com.mona.bean;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 四方树
 */
public class Quadtree {
    /**
     * 节点分裂前一个节点最多可以存储多少
     */
    private final int MAX_OBJECTS = 10;
    /**
     * 定义了四叉树的深度
     */
    private final int MAX_LEVELS = 5;
    /**
     * 当前节点（0就表示是每四个节点的父节点）
     */
    private final int level;
    private final List<Rectangle> objects;
    /**
     * 一个节点的2D空间的面积
     */
    private final Rectangle bounds;
    /**
     * 存储四个子节点
     */
    private final Quadtree[] nodes;

    /*
     * Constructor
     */
    public Quadtree(final int pLevel, final Rectangle pBounds) {
        level = pLevel;
        objects = new ArrayList<Rectangle>();
        bounds = pBounds;
        nodes = new Quadtree[4];
    }

    /*
     * Clears the quadtree
     */
    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++)
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
    }

    /*
     * Insert the object into the quadtree. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding nodes.
     */
    public void insert(final Rectangle pRect) {
        if (nodes[0] != null) {
            final int index = getIndex(pRect);

            if (index != -1) {
                nodes[index].insert(pRect);

                return;
            }
        }

        objects.add(pRect);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            split();

            int i = 0;
            while (i < objects.size()) {
                final int index = getIndex(objects.get(i));
                if (index != -1)
                    nodes[index].insert(objects.remove(i));
                else i++;
            }
        }
    }

    /*
     * Return all objects that could collide with the given object
     */
    public List<Rectangle> retrieve(final List<Rectangle> returnObjects,
                                    final Rectangle pRect) {
        final int index = getIndex(pRect);
        if (index != -1 && nodes[0] != null)
            nodes[index].retrieve(returnObjects, pRect);

        returnObjects.addAll(objects);

        return returnObjects;
    }

    /*
     * Determine which node the object belongs to. -1 means
     * object cannot completely fit within a child node and is part
     * of the parent node
     */
    private int getIndex(final Rectangle pRect) {
        int index = -1;
        final double verticalMidpoint = bounds.getX() + bounds.getWidth() / 2;
        final double horizontalMidpoint = bounds.getY() + bounds.getHeight() / 2;

        // Object can completely fit within the top quadrants
        final boolean topQuadrant = pRect.getY() < horizontalMidpoint
                && pRect.getY() + pRect.getHeight() < horizontalMidpoint;
        // Object can completely fit within the bottom quadrants
        final boolean bottomQuadrant = pRect.getY() > horizontalMidpoint;

        // Object can completely fit within the left quadrants
        if (pRect.getX() < verticalMidpoint
                && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant)
                index = 1;
            else if (bottomQuadrant) index = 2;
        }
        // Object can completely fit within the right quadrants
        else if (pRect.getX() > verticalMidpoint) if (topQuadrant)
            index = 0;
        else if (bottomQuadrant) index = 3;

        return index;
    }

    /*
     * Splits the node into 4 subnodes
     */
    private void split() {
        final int subWidth = (int) (bounds.getWidth() / 2);
        final int subHeight = (int) (bounds.getHeight() / 2);
        final int x = (int) bounds.getX();
        final int y = (int) bounds.getY();

        nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth,
                subHeight));
        nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight,
                subWidth, subHeight));
        nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y
                + subHeight, subWidth, subHeight));
    }
}