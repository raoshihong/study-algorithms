package com.rao.study.algorithms.tree.binary;

import java.util.Arrays;
import java.util.List;

public class BinaryTest {

    public static void main(String[] args){

        BinaryTree binaryTree = generateTree(Arrays.asList(13,10,20,6,8,7,9,6));

        /**
         * 最终的树的结构
         *          13
         *        /   \
         *      10    20
         *     /
         *    6
         *   / \
         *  3   8
         *     / \
         *    7   9
         */

        System.out.println("前序遍历====================");
        binaryTree.pre(binaryTree.getRoot());

        System.out.println("\n中序遍历====================");
        binaryTree.mid(binaryTree.getRoot());

        System.out.println("\n后序遍历====================");
        binaryTree.succ(binaryTree.getRoot());

        System.out.println("\n二分查找===================");
        binaryTree.search(binaryTree.getRoot(),20);

        System.out.println("\n二叉树节点删除========================");
        binaryTree.remove(6);

        System.out.println("\n二叉树节点删除后的结果,前序遍历============");
        binaryTree.pre(binaryTree.getRoot());

        System.out.println("\n二叉树节点删除后的结果,中序遍历============");
        binaryTree.mid(binaryTree.getRoot());

        System.out.println("\n二叉树节点删除后的结果,后序遍历============");
        binaryTree.succ(binaryTree.getRoot());

        System.out.println();
    }

    public static BinaryTree generateTree(List<Integer> datas){
        BinaryTree binaryTree = new BinaryTree();

        datas.stream().forEach(node -> binaryTree.insert(node));

        return binaryTree;
    }
}
