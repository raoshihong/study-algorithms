package com.rao.study.algorithms.tree.avltree;


public class AVLNode {

    //父节点
//    private AVLNode parent;
    //右孩子
    private AVLNode rightChild;
    //左孩子
    private AVLNode leftChild;
    //节点数据
    private Integer data;
    //记录当前节点的高度
    private Integer height;

    public AVLNode(Integer data) {
        this.data = data;
    }

//    public AVLNode getParent() {
//        return parent;
//    }
//
//    public void setParent(AVLNode parent) {
//        this.parent = parent;
//    }

    public AVLNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(AVLNode rightChild) {
        this.rightChild = rightChild;
    }

    public AVLNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(AVLNode leftChild) {
        this.leftChild = leftChild;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int compare(AVLNode other){
        if (this.data == other.data) {
            return 0;
        } else if (this.data>other.data) {
            return 1;
        } else {
            return -1;
        }
    }
}
