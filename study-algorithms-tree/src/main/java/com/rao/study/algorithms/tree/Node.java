package com.rao.study.algorithms.tree;

/**
 * 二叉树中的节点
 */
public class Node {
    //父节点
    private Node parent;
    //右孩子
    private Node rightChild;
    //左孩子
    private Node leftChild;
    //节点数据
    private Integer data;

    public Node() {
    }

    public Node(Integer data) {
        this.data = data;
    }

    public Node(Node parent, Node rightChild, Node leftChild, Integer data) {
        this.parent = parent;
        this.rightChild = rightChild;
        this.leftChild = leftChild;
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
