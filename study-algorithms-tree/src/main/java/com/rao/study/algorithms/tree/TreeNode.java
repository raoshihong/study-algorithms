package com.rao.study.algorithms.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    public static void main(String[] args){

        Node root = new Node(13);
        List<Node> nodes = new ArrayList<Node>();
        nodes.add(new Node(10));
        nodes.add(new Node(20));
        nodes.add(new Node(6));
        nodes.add(new Node(8));
        nodes.add(new Node(7));
        nodes.add(new Node(9));
        nodes.add(new Node(3));

        nodes.stream().forEach(node -> insert(root,node));

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

        System.out.println(root);

        System.out.println("前序遍历====================");
        pre(root);

        System.out.println("中序遍历====================");
        mid(root);

        System.out.println("后序遍历====================");
        succ(root);

        System.out.println("二分查找===================");
        search(root,new Node(20));

        System.out.println("二叉树节点删除========================");
        delete(root,new Node(6));

        System.out.println("二叉树节点删除后的结果,前序遍历============");
        pre(root);

        System.out.println("二叉树节点删除后的结果,中序遍历============");
        mid(root);

        System.out.println("二叉树节点删除后的结果,后序遍历============");
        succ(root);
    }

    /**
     * 前序遍历: 就是根节点在左右子节点前先输出,然后同级先左再右,如果左节点还有子节点,那么继续左节点的子节点优先,直到leftChild为null
     * 13 , 10  , 6  , 3 , 8 , 7 , 9 , 20
     */
    public static void pre(Node root){
        if (root!=null) {
            System.out.println(root.getData());//先输出根节点
            if (root.getLeftChild()!=null) {//再输出左节点
                pre(root.getLeftChild());
            }
            if (root.getRightChild()!=null){//再输出右节点
                pre(root.getRightChild());
            }
        }
    }

    /**
     * 中序遍历(比较重要) 根节点在左右节点之间输出,如果左节点还有左子节点则继续遍历,直到leftChild为null
     * 3 , 6 , 7 , 8 , 9 , 10 , 13 , 20
     */
    public static void mid(Node root){
        if (root!=null) {
            if (root.getLeftChild()!=null) {//先输出左节点
                mid(root.getLeftChild());
            }
            //再输出根节点
            System.out.println(root.getData());
            if (root.getRightChild()!=null) {//再输出右节点
                mid(root.getRightChild());
            }
        }
    }

    /**
     * 后序遍历 ,根节点在左右节点后输出，然后同级先左再右, 如果左节点还有左子节点，则继续左遍历，直到leftChild为null,同理如果右节点还有右子节点，则继续遍历,直到rightChild为null
     * 3 , 7 , 9 , 8 , 6 , 10 , 20 , 13
     */
    public static void succ(Node root){
        if (root!=null) {
            //先输出左节点
            if (root.getLeftChild()!=null) {
                succ(root.getLeftChild());
            }
            //再输出右节点
            if (root.getRightChild()!=null) {
                succ(root.getRightChild());
            }
            //最后输出根节点
            System.out.println(root.getData());
        }
    }

    /**
     * 层次遍历 ,由上到下，由左到右
     * 13 , 10 , 20 , 6 , 3 , 8 , 7 , 9
     */
    public static void level(Node root){

    }

    /**
     * 二叉树的插入比较简单,只需要递归找到对应的叶子结点
     * @param root
     * @param child
     */
    public static void insert(Node root,Node child){

        if (root != null) {

            if (child.getData()<root.getData()) {

                if (root.getLeftChild()==null) {
                    root.setLeftChild(child);
                    child.setParent(root);
                }else{
                    //递归遍历,直到找到叶子节点
                    insert(root.getLeftChild(),child);
                }

            }else{
                if (root.getRightChild()==null) {
                    root.setRightChild(child);
                    child.setParent(root);
                }else{
                    //递归
                    insert(root.getRightChild(),child);
                }
            }

        }else {
            root = child;
        }

    }

    private static int t = 0;
    /**
     * 二分查找
     * @param root
     * @param target
     */
    public static Node search(Node root,Node target){
        ++t;
        System.out.println("查找轨迹:"+root.getData()+"->");
        if (root!=null) {
            //先查根
            if (root.getData() == target.getData()) {
                System.out.println("查找次数:"+t);
                return root;
            } else if (root.getData() > target.getData()) {//再查左边
                return search(root.getLeftChild(),target);
            } else { //再查右边
                return search(root.getRightChild(),target);
            }
        }
        return null;
    }

    /**
     * 二叉树删除,二叉树的删除比较复杂,分为三种情况
     * 1.删除的节点为叶子节点,直接删除
     * 2.删除的节点有且只有一个子节点(左节点或者右节点), 删除当前节点,并将子节点设置为当前节点的位置
     * 3.删除的节点有两个子节点(左右子节点),
     *      先找到被删除节点的右节点的最小值节点或者最大值节点,
     *      删除当前节点,并将最小值节点或者最大值节点赋值到当前被删除的节点位置
     *      再删除最小值节点或者最大值节点(如果最左子节点有右子节点或者最右子节点有左子节点,则该最小值节点或者最大值节点的删除操作可以按照第二种情况进行删除)
     * 最小节点7没有左子节点
     *          13                                      13
     *        /   \                                    /  \
     *      10    20                                  10   20
     *     /                                          /
     *    6                   --->删除节点6后         7
     *   / \                                        / \
     *  3   8                                      3   8
     *     / \                                          \
     *    7   9                                          9
     *
     * 删除节点6后的
     * 前序遍历: 13 , 10 , 7 , 3 , 8 , 9 , 20
     * 中序遍历: 3 , 7 , 8 , 9 , 10 , 13 , 20
     * 后序遍历: 3 , 9 , 8 , 7 , 10 , 20 , 13
     *
     * 最小节点7有左子节点
     *          13
     *        /   \
     *      10    20
     *     /
     *    6
     *   / \
     *  3   8
     *     / \
     *    7   9
     *     \
     *      7.5
     */
    public static void delete(Node root,Node deleteNode){

        if (root!=null) {
            //通过二分查找目标节点
            Node target = search(root,deleteNode);
            if (target  == null) {
                return;
            }
            //判断目标节点是为第一种情况
            if (target.getLeftChild() == null && target.getRightChild() == null) {//被删目标节点是叶子节点
                if (target.getParent().getLeftChild().getData().equals(target.getData())) {//目标节点为其父节点的左子节点
                    target.getParent().setLeftChild(null);
                }else{
                    target.getParent().setRightChild(null);
                }
                //去除引用
                target.setParent(null);
                target = null;//help GC
            } else  if (target.getLeftChild() == null || target.getRightChild() == null) { //判断目标节点是第二种情况
                if (target.getRightChild() != null) {//被删目标节点只有右孩子
                    if(target.getParent().getLeftChild().getData().equals(target.getData())) {//被删除的节点是父节点的左节点
                        target.getParent().setLeftChild(target.getRightChild());//设置父节点的左节点为目标节点的右孩子
                    }else{
                        target.getParent().setRightChild(target.getRightChild());
                    }
                    target.getRightChild().setParent(target.getParent());//设置目标节点的右孩子的的父节点为目标节点的父节点
                    target = null;
                }else{//只有左孩子
                    if(target.getParent().getLeftChild().getData().equals(target.getData())) {//被删除的节点是父节点的左节点
                        target.getParent().setLeftChild(target.getLeftChild());//设置父节点的左节点为目标节点的右孩子
                    }else{
                        target.getParent().setRightChild(target.getLeftChild());
                    }
                    target.getLeftChild().setParent(target.getParent());
                    target = null;
                }
            } else if (target.getLeftChild() != null && target.getRightChild()!=null) {//判断目标节点是第三种情况(第三种情况带有递归),比如删除6
                //3.1寻找右节点的最大节点或者最小节点
                //这里以右节点的最左节点为例,6的右节点的最左节点7
                Node mostLeftNode = searchMostLeft(target.getRightChild());
                //3.2 删除当前节点,并把最小节点赋值到被删除的当前节点
                target.setData(mostLeftNode.getData());//交互值
                //3.3 删除原来位置的最小值节点(采用递归)
                delete(mostLeftNode,mostLeftNode);
            }
        }

    }

    /**
     * 获取最左子孙节点
     * @param root
     * @return
     */
    public static Node searchMostLeft(Node root){
        if (root.getLeftChild() == null) {
            return root;
        }else{
            return searchMostLeft(root.getLeftChild());
        }
    }
}
