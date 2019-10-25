package com.rao.study.algorithms.tree.redblack.v1;

import java.util.Arrays;
import java.util.List;

public class RedBlackTest {
    public static void main(String[] args){

        //30,20,35,32,39,37,33
        RedBlackTree redBlackTree = generator(Arrays.asList(30,20,40,10,15,25));

        redBlackTree.mid(redBlackTree.getRoot());

        System.out.println("根节点:"+redBlackTree.getRoot().getData());
        redBlackTree.remove(30);

        redBlackTree.mid(redBlackTree.getRoot());
    }

    public static RedBlackTree generator(List<Integer> datas){
        RedBlackTree redBlackTree = new RedBlackTree();
        datas.stream().forEach(data->{
            redBlackTree.insert(data);
        });

        return redBlackTree;
    }
}
