package com.rao.study.algorithms.tree.redblack.v2;

import java.util.Arrays;
import java.util.List;

public class RedBlackTest {
    public static void main(String[] args){

        //30
        //30,20,35,32,39,37,33
        //30,20,19
        //30,20,31,19
        //30,20,40,10,15
        //30,20,40,10,15,25
        //30,20,35,15,25,23,28,29
        //30,15,45,7,23,35,50,3,10,2
        //26,17,41,14,21,30,47,10,16,19,23,28,38,7,12,15,20,35,39,3
        //26,20,41,14,21,30,47,10,16,19,23,28,38,7,12,15,35,39,3
        RedBlackTree redBlackTree = generator(Arrays.asList(30,20,35,12,25,22,24,23));

        redBlackTree.mid(redBlackTree.getRoot());

        System.out.println("删除节点");
        System.out.println("根节点:"+redBlackTree.getRoot().getData());
        redBlackTree.remove(30);
        System.out.println("删除后的根节点:"+(redBlackTree.getRoot()==null?"":redBlackTree.getRoot().getData()));
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
