package com.rao.study.algorithms.tree.binary;

public class BinaryTree {

    private BinaryNode root;

    public BinaryNode getRoot() {
        return root;
    }

    public void setRoot(BinaryNode root) {
        this.root = root;
    }

    /**
     * 前序遍历: 就是根节点在左右子节点前先输出,然后同级先左再右,如果左节点还有子节点,那么继续左节点的子节点优先,直到leftChild为null
     * 13 , 10  , 6  , 3 , 8 , 7 , 9 , 20
     */
    public void pre(BinaryNode root){
        if (root!=null) {
            System.out.print(root.getData() + " ");//先输出根节点
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
    public void mid(BinaryNode root){
        if (root!=null) {
            if (root.getLeftChild()!=null) {//先输出左节点
                mid(root.getLeftChild());
            }
            //再输出根节点
            System.out.print(root.getData() + " ");//先输出根节点
            if (root.getRightChild()!=null) {//再输出右节点
                mid(root.getRightChild());
            }
        }
    }

    /**
     * 后序遍历 ,根节点在左右节点后输出，然后同级先左再右, 如果左节点还有左子节点，则继续左遍历，直到leftChild为null,同理如果右节点还有右子节点，则继续遍历,直到rightChild为null
     * 3 , 7 , 9 , 8 , 6 , 10 , 20 , 13
     */
    public void succ(BinaryNode root){
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
            System.out.print(root.getData() + " ");//先输出根节点
        }
    }

    /**
     * 层次遍历 ,由上到下，由左到右
     * 13 , 10 , 20 , 6 , 3 , 8 , 7 , 9
     */
    public void level(BinaryNode root){

    }

    public void insert(Integer data){
        this.root = insert(this.root,data);
    }

    /**
     * 二叉树的插入比较简单,只需要递归找到对应的叶子结点
     * @param root
     */
    private BinaryNode insert(BinaryNode root, Integer data){
        BinaryNode child = new BinaryNode(data);
        if (root == null) {
            root = child;
        }else{
            if (root.compare(child) >= 0) {
                root.setLeftChild(insert(root.getLeftChild(),data));
            }else{
                root.setRightChild(insert(root.getRightChild(),data));
            }
        }
        return root;
    }

    private static int t = 0;
    /**
     * 二分查找
     * @param root
     * @param data
     */
    public BinaryNode search(BinaryNode root, int data){
        ++t;
        if (root!=null) {
            System.out.println("查找轨迹:"+root.getData()+"->");
            //先查根
            if (root.getData() == data) {
                System.out.println("查找次数:"+t);
                return root;
            } else if (root.getData() > data) {//再查左边
                return search(root.getLeftChild(),data);
            } else { //再查右边
                return search(root.getRightChild(),data);
            }
        }
        return null;
    }

    public void remove(int data){
        this.root = remove(this.root,data);
    }

    /**
     * 二叉树删除,二叉树的删除比较复杂,分为三种情况
     * 1.删除的节点为叶子节点,直接删除
     * 2.删除的节点有且只有一个子节点(左节点或者右节点), 删除当前节点,并将子节点设置为当前节点的位置
     * 3.删除的节点有两个子节点(左右子节点),
     *      先找到被删除节点的右节点的最小值节点或者左节点的最大值节点,
     *      删除当前节点,并将右节点最小值节点或者左节点最大值节点赋值到当前被删除的节点位置
     *      再删除右节点最小值节点或者左节点最大值节点(如果右节点最小值节点有左子节点或者左节点最大值节点有右子节点,则该最小值节点或者最大值节点的删除操作可以按照第二种情况进行删除)
     * 比如删除6节点,寻找6节点的右节点的最小节点7没有左子节点
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
    private BinaryNode remove(BinaryNode root, int data){
        BinaryNode binaryNode = new BinaryNode(data);
        if (root == null) {
            return null;
        }else{
            if (root.compare(binaryNode)>0) {
                root.setLeftChild(remove(root.getLeftChild(),data));
            }else if(root.compare(binaryNode)<0){
                root.setRightChild(remove(root.getRightChild(),data));
            }else{
                //判断目标节点是为第一种情况
                if (root.getLeftChild() == null && root.getRightChild() == null) {
                    //被删目标节点是叶子节点,则直接删除该叶子节点
                    return null;
                } else  if (root.getLeftChild() == null || root.getRightChild() == null) {
                    //判断目标节点是第二种情况,被删节点有一个子节点,则直接将子节点覆盖当前节点
                    return root.getLeftChild() != null?root.getLeftChild():root.getRightChild();
                } else if (root.getLeftChild() != null && root.getRightChild()!=null) {
                    //判断目标节点是第三种情况(第三种情况带有递归),比如删除6
                    //3.1寻找右节点的最小节点或者左节点的最大节点
                    //这里以右节点的最小节点为例,6的右节点的最小(左)节点7
                    BinaryNode rightMin = findRightMin(root.getRightChild());
                    //3.2 删除原来位置的最小值节点(采用递归)
                    remove(root,rightMin.getData());
                    //3.3 删除当前节点,并把最小节点赋值到被删除的当前节点
                    root.setData(rightMin.getData());//交互值
                }
            }
        }
        return root;
    }

    /**
     * 获取右孩子节点的最小节点
     * @param root
     * @return
     */
    public BinaryNode findRightMin(BinaryNode root){
        if (root.getLeftChild() == null) {
            return root;
        }else{
            return findRightMin(root.getLeftChild());
        }
    }
}
