package com.mona.bean;

import java.util.Arrays;
import java.util.Random;

/**
 * 二叉查找树
 * Created by sylva on 2018/12/9.
 */

public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;
    private int size;

    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        Node[] arr = new Node[16];
        Random random = new Random();
        //	int[] nums = { 94, 85, 9, 79, 40, 58, 26, 45, 2, 33 };
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Node<>(random.nextInt(99));
            tree.put((Integer) arr[i].data);
        }
        System.out.print("插入数据：");
        System.out.println(Arrays.toString(arr));
        System.out.print("二叉树：");
        tree.ldrTraverse(tree.root);
        System.out.println();
        System.out.print("数据排序：");
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        for (int i = 0; i < arr.length; i++) {
            System.out.print("删除数字 ");
            System.out.println(arr[i]);
            tree.remove((Integer) arr[i].data);
            System.out.print("结果：");
            tree.ldrTraverse(tree.root);
            System.out.println();
            tree.put((Integer) arr[i].data);
        }
        System.out.println("逐个删除：");
        for (int i = 0; i < arr.length; i++) {
            System.out.print("删除数字");
            System.out.println(arr[i]);
            tree.remove((Integer) arr[i].data);
            tree.ldrTraverse(tree.root);
            System.out.println();
        }
    }

    public int size() {
        return size;
    }

    public Node<T> getRoot() {
        return root;
    }

    /**
     * 放入元素
     *
     * @param data 元素值
     * @return 是否重复，true代表已经有这个元素
     */
    public boolean put(T data) {
        Node<T> node = new Node<>(data);
        if (root == null) {
            size++;
            root = node;
            return false;
        }
        Node<T> start = root;
        Node<T> prev = null;
        while (start != null) {
            prev = start;
            if (start.data.compareTo(data) == 0) {
                return true;
            } else if (start.data.compareTo(data) > 0) {
                start = start.leftChild;
            } else {
                start = start.rightChild;
            }
        }
        size++;
        node.parent = prev;
        if (prev.data.compareTo(data) > 0) {
            prev.leftChild = node;
            return false;
        } else {
            prev.rightChild = node;
            return false;
        }
    }

    /**
     * 删除数据
     *
     * @param data 要删除的数据
     * @return true代表有这个数据而且已经删除，false代表没有这个数据
     */
    public boolean remove(T data) {
        Node<T> node = find(data);
        if (node == null) {
            return false;
        }
        //如果有右子树
        if (node.rightChild != null) {
            Node<T> right = node.rightChild;
            Node<T> minInRight = findMinNode(right);
            replaceNode(node, minInRight);
        } else if (node.leftChild != null) {
            //如果只有左子树
            Node<T> left = node.leftChild;
            Node<T> maxInLeft = findMaxNode(left);
            replaceNode(node, maxInLeft);
        } else {
            //断子绝孙
            replaceNode(node, null);
        }
        size--;
        return true;
    }

    /**
     * 查找（查找二叉树）中最小的节点
     *
     * @param root 根节点
     */
    private Node<T> findMinNode(Node root) {
        for (Node node = root; node.leftChild != null; node = node.leftChild) {
            return node;
        }
        return root;
    }

    /**
     * 查找（查找二叉树）中最大的节点
     *
     * @param root 根节点
     */
    private Node<T> findMaxNode(Node root) {
        for (Node node = root; node.rightChild != null; node = node.rightChild) {
            return node;
        }
        return root;
    }

    /**
     * 把老节点替换成新节点（这真是一个悲伤的故事）
     *
     * @param node    老节点
     * @param newNode 新节点
     */
    private void replaceNode(Node node, Node newNode) {
        //原节点的关系就是父节点和左右子
        Node parent = node.parent;
        Node leftChild = node.leftChild;
        Node rightChild = node.rightChild;
        //让他六亲不认
        node.parent = null;
        node.leftChild = null;
        node.rightChild = null;
        //如果他爸活着，他爸心里新节点已经代替了他的位置
        if (parent != null) {
            if (parent.data.compareTo(node.data) > 0) {
                parent.leftChild = newNode;
            } else {
                parent.rightChild = newNode;
            }
        }
        //如果小儿子活着，小儿子把新节点当成爸爸
        if (leftChild != null) {
            leftChild.parent = newNode;
        }
        //如果大儿子活着，大儿子也抛弃了他
        if (rightChild != null) {
            rightChild.parent = newNode;
        }
        //如果新节点活着，让新节点承认这些关系
        if (newNode != null) {
            //以下三个if是用来判断上文提到的替代品节点根关系目标节点相同的情况
            //这种情况直接放弃继承这种关系
            if (newNode != parent) {
                newNode.parent = parent;
            }
            if (newNode != leftChild) {
                newNode.leftChild = leftChild;
            }
            if (newNode != rightChild) {
                newNode.rightChild = rightChild;
            }
        }
        //root其实是个跟树本身无关的外部引用，前面已经把新节点的关系处理完毕
        //这里只需要把外部引用指向新节点即可
        if (node == root) {
            root = newNode;
        }
    }

    /**
     * 孤立一个节点，断开它跟外界的关系
     *
     * @param node 被孤立的节点
     */
    private void isolateNode(Node node) {
        replaceNode(node, null);
    }

    private Node<T> find(T data) {
        Node<T> result = root;
        while (result != null) {
            int compareResult = result.data.compareTo(data);
            if (compareResult == 0) {
                return result;
            } else if (compareResult > 0) {
                result = result.leftChild;
            } else {
                result = result.rightChild;
            }
        }
        return result;
    }

    /**
     * 中序遍历
     *
     * @param root 相对根节点
     */
    public void ldrTraverse(Node root) {
        if (root == null) {
            return;
        }
        ldrTraverse(root.leftChild);
        System.out.print(root.data);
        System.out.print(" ");
        ldrTraverse(root.rightChild);
    }

    public static class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
        private T data;
        private Node<T> parent;
        private Node<T> leftChild;
        private Node<T> rightChild;

        private Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public Node<T> getParent() {
            return parent;
        }

        public Node<T> getLeftChild() {
            return leftChild;
        }

        public Node<T> getRightChild() {
            return rightChild;
        }

        @Override
        public int compareTo(Node<T> o) {
            if (data != null) {
                return data.compareTo(o.data);
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }
}
