package com.mona.common.container.tree;

import java.util.Arrays;
import java.util.Random;

public class BinaryTree<E extends Comparable<E>> {
    TreeNode<E> root;

    public static void main(String[] args) {
        BinaryTree<Integer> tree = new BinaryTree<>();
        TreeNode[] arr = new TreeNode[16];
        Random random = new Random();
        //	int[] nums = { 94, 85, 9, 79, 40, 58, 26, 45, 2, 33 };
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new TreeNode<>(random.nextInt(99));
            tree.add(arr[i]);
        }
        System.out.print("插入数据：");
        System.out.println(Arrays.toString(arr));
        System.out.print("二叉树：");
        tree.midOrderTraverse(tree.root);
        System.out.println();
        System.out.print("数据排序：");
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        //	tree.del(arr[9]);
        //	System.out.print("删除后：");
        //	tree.midOrderTraverse(tree.root);
        //	System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print("删除数字");
            System.out.println(arr[i]);
            tree.del(arr[i]);
            tree.midOrderTraverse(tree.root);
            System.out.println();
            tree.add(arr[i]);
        }
        System.out.println("逐个删除：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print("删除数字");
            System.out.println(arr[i]);
            tree.del(arr[i]);
            tree.midOrderTraverse(tree.root);
            System.out.println();
        }
    }

    public void add(TreeNode<E> add) {
        if (add != null) {
            add.parent = null;
            add.childL = null;
            add.childR = null;
        }
        if (root == null) {
            root = add;
            return;
        }
        int i;
        TreeNode<E> node = root;
        while (true) {
            i = node.compareTo(add);
            if (i > 0) {
                if (node.childL != null) {
                    node = node.childL;
                } else {
                    node.childL = add;
                    add.parent = node;
                }
            } else if (i < 0) {
                if (node.childR != null) {
                    node = node.childR;
                } else {
                    node.childR = add;
                    add.parent = node;
                }
            } else {
                return;
            }
        }
    }

    public void del(TreeNode<E> del) {
        TreeNode<E> temp;
        //如果目标具有左节点则取左边最大值进行替换
        if ((temp = del.childL) != null) {
            while (temp.childR != null) {
                temp = temp.childR;//循环找到左侧最大元素
            } //此时node的右节点一定为null，可能存在左节点
            if (temp.childL != null) {
                temp.childL.parent = temp.parent;
            }
            //当删除节点的左节点不是替补时，将其左节点移交给替补
            if (del.childL != temp) {
                //将替补的左节点移交到替补父节点的右节点
                temp.parent.childR = temp.childL;
                del.childL.parent = temp;
                temp.childL = del.childL;
            }
            temp.childR = del.childR;
            //如果删除节点具有右节点则移交给替补
            if (del.childR != null) {
                del.childR.parent = temp;
            }
        } else if ((temp = del.childR) != null) {
            //如果目标具有右节点则取右边最小值进行替换
            while (temp.childL != null) {
                temp = temp.childL;//循环找到右侧最大元素
            } //此时node的左节点一定为null，可能存在右节点
            if (temp.childR != null) {
                temp.childR.parent = temp.parent;
            }
            //当待删除节点的左节点不是替补时，将其左节点移交给替补
            if (del.childR != temp) {
                //将替补的右节点移交到替补父节点的左侧子节点
                temp.parent.childL = temp.childR;
                temp.childR = del.childR;
                del.childR.parent = temp;
            }
            temp.childL = del.childL;
            //如果待删除节点具有左节点则移交给替补
            if (del.childL != null) {
                del.childL.parent = temp;
            }
        }
        if (temp != null) { //为空表示del没有子节点
            temp.parent = del.parent;
        }
        if (del.parent == null) {
            root = temp;
            return;
        }
        if (del.parent.childL == del) {
            del.parent.childL = temp;
        } else {
            del.parent.childR = temp;
        }
    }

    /**
     * 中序遍历
     */
    public void midOrderTraverse(TreeNode<E> root) {
        if (root == null) {
            return;
        }
        //LDR
        midOrderTraverse(root.childL);
        System.out.print(root.data + " ");
        midOrderTraverse(root.childR);
    }

    static class TreeNode<E extends Comparable<E>> implements Comparable<TreeNode<E>> {
        E data;
        TreeNode<E> parent;
        TreeNode<E> childL;
        TreeNode<E> childR;

        public TreeNode(E data) {
            this.data = data;
        }

        @Override
        public int compareTo(TreeNode<E> o) {
            return data.compareTo(o.data);
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }
}
