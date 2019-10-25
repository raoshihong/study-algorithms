package com.rao.study.algorithms.tree.binary;

/**
 * 二叉树中的节点
 */
public class BinaryNode {
    //父节点
    private BinaryNode parent;
    //右孩子
    private BinaryNode rightChild;
    //左孩子
    private BinaryNode leftChild;
    //节点数据
    private Integer data;

    public BinaryNode() {
    }

    public BinaryNode(Integer data) {
        this.data = data;
    }

    public BinaryNode(BinaryNode parent, BinaryNode rightChild, BinaryNode leftChild, Integer data) {
        this.parent = parent;
        this.rightChild = rightChild;
        this.leftChild = leftChild;
        this.data = data;
    }

    public BinaryNode getParent() {
        return parent;
    }

    public void setParent(BinaryNode parent) {
        this.parent = parent;
    }

    public BinaryNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryNode rightChild) {
        this.rightChild = rightChild;
    }

    public BinaryNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryNode leftChild) {
        this.leftChild = leftChild;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public int compare(BinaryNode other){
        if (this.data > other.data) {
            return 1;
        } else if(this.data < other.data){
            return -1;
        } else{
            return 0;
        }
    }
}
