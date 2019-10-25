package com.rao.study.algorithms.tree.redblack.v1;

public class RedBlackNode {
    private boolean red;
    private RedBlackNode leftChild;
    private RedBlackNode rightChild;
    private RedBlackNode parent;
    private Integer data;

    public RedBlackNode(boolean red, Integer data) {
        this.red = red;
        this.data = data;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

    public RedBlackNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(RedBlackNode leftChild) {
        this.leftChild = leftChild;
    }

    public RedBlackNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(RedBlackNode rightChild) {
        this.rightChild = rightChild;
    }

    public RedBlackNode getParent() {
        return parent;
    }

    public void setParent(RedBlackNode parent) {
        this.parent = parent;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public int compare(RedBlackNode other){
        if (this.data<other.data) {
            return -1;
        } else if(this.data>other.data){
            return 1;
        } else {
            return 0;
        }
    }
}
