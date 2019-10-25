package com.rao.study.algorithms.tree.avltree;

import java.util.Arrays;
import java.util.List;

public class AVLTest {

    public static void main(String[] args){
//        //LL型 13 10 20 6 11 3 2
//        System.out.println("LL型=====================================");
//        generateTree(Arrays.asList(13,10,20,6,11,3,2));
//        //RR型 13 10 20 21 19 30
//        System.out.println("RR型=====================================");
//        generateTree(Arrays.asList(13,10,20,21,19,30));
//        //LR型 13 9 20 8 11 10/12
//        System.out.println("LR型=====================================");
//        generateTree(Arrays.asList(13,9,20,8,11,10));
//        //RL型 13 10 24 21 25 20/22
//        System.out.println("RL型=====================================");
//        generateTree(Arrays.asList(13,10,24,21,25,22));



        System.out.println("插入节点====================================");
        // 21,12,30,10,26,23,27,34,32,35
        AVLTree avlTree = generateTree(Arrays.asList(21,12,30,10,26,23,27,34,32,35));

        System.out.println("删除叶子节点节点====================================");
        avlTree.remove(10);
        avlTree.mid(avlTree.getRoot());

        System.out.println("删除带有一个子节点节点====================================");
        avlTree = generateTree(Arrays.asList(21,12,30,10,26,23,27,34,32,35));
        avlTree.remove(12);
        avlTree.mid(avlTree.getRoot());

        System.out.println("删除带有两个子节点节点====================================");
        avlTree = generateTree(Arrays.asList(14,8,21,4,12,17,23,3,6,11,13,25,1,2,5,7,9,10));
        avlTree.remove(17);
        avlTree.mid(avlTree.getRoot());
    }

    public static AVLTree generateTree(List<Integer> datas){
        AVLTree avlTree = new AVLTree();
        for (Integer data : datas) {
            avlTree.insert(data);
        }

        avlTree.mid(avlTree.getRoot());

        return avlTree;
    }

}
