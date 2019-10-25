package com.rao.study.algorithms.tree.avltree;

public class AVLTree {

    private AVLNode root;

    public AVLNode getRoot() {
        return root;
    }

    public void setRoot(AVLNode root) {
        this.root = root;
    }

    public void insert(int data){
        this.root = insert(this.root,data);
    }

    private AVLNode insert(AVLNode root,int data){
        AVLNode avlNode = new AVLNode(data);

        //1.先正常插入节点
        if (root == null) {
            root = avlNode;
        }else {
            if (avlNode.compare(root) <= 0) {
                AVLNode leftNode = insert(root.getLeftChild(), data);
                root.setLeftChild(leftNode);
            } else {
                AVLNode rightNode = insert(root.getRightChild(), data);
                root.setRightChild(rightNode);
            }
        }

        //2.在对节点是否旋转
        if (Math.abs(height(root.getLeftChild()) - height(root.getRightChild())) > 1) {
            //进行旋转
            root = rotate(root);
        }

        //3.刷新当前节点的高度
        refreshHeight(root);
        //可以理解为,从叶子节点,最终返回到跟节点
        return root;
    }

    public void remove(int data){
        this.root = remove(this.root,data);
    }

    private AVLNode remove(AVLNode root,int data){
        AVLNode avlNode = new AVLNode(data);
        if (root == null) {
            return null;
        }else {
            if (avlNode.compare(root)<0) {
                root.setLeftChild(remove(root.getLeftChild(),data));
                //刷新root
            } else if (avlNode.compare(root)>0) {
                root.setRightChild(remove(root.getRightChild(),data));
                //刷新root
            } else {
                //第一种情况,被删除的节点为叶子节点
                if (root.getLeftChild() == null && root.getRightChild() == null) {
                    //直接删除
                    return null;
                } else if(root.getLeftChild() == null || root.getRightChild() == null){
                    //第二种情况,被删除的节点有一个子节点,直接用其子节点替换当前被删除节点
                    return root.getRightChild() !=null?root.getRightChild():root.getLeftChild();
                } else if (root.getLeftChild() != null && root.getRightChild() != null) {
                    //第三种情况,被删除的节点有左右节点,此时需要寻找左孩子节点的最大叶子节点或者寻找右孩子节点的最小叶子节点,找到后,将该叶子节点替换当前被删除节点，然后再删除该叶子节点
                    //下面以寻找左孩子节点的最大叶子节点为例
                    AVLNode leftMax = findLeftMax(root.getLeftChild());
                    root = remove(root,leftMax.getData());
                    root.setData(leftMax.getData());
                }
            }
        }

        //判断是否平衡
        if (Math.abs(height(root.getLeftChild()) - height(root.getRightChild())) > 1) {
            root = rotate(root);
        }

        //刷新高度
        refreshHeight(root);
        return root;
    }

    private AVLNode findLeftMax(AVLNode root){
        if (root.getRightChild() == null) {
            return root;
        } else {
            return findLeftMax(root.getRightChild());
        }
    }

    //获取节点的高度
    private int height(AVLNode avlNode){
        if (avlNode == null) {
            return 0;
        }else {
            return avlNode.getHeight();
        }
    }

    /**
     * 刷新高度,实际就是取节点的左右孩子节点的最大高度作为当前节点的高度
     * @param avlNode
     */
    private void refreshHeight(AVLNode avlNode){
        avlNode.setHeight(Math.max(height(avlNode.getLeftChild()),height(avlNode.getRightChild()))+1);
    }

    /**
     * LL型
     *                  13                   10
     *                 /  \                 /  \
     *               10  20               6   13
     *              /  \                 /   /  \
     *             6    11              3  11   20
     *            /
     *           3
     *
     *           需要刷新节点13的高度
    */
    private AVLNode rotateLL(AVLNode avlNode){//旋转后,当前的节点变更了,则需要返回旋转后的那个节点
        // 右旋
        AVLNode leftNode = avlNode.getLeftChild();
        avlNode.setLeftChild(leftNode.getRightChild());//第一步
//        leftNode.getRightChild().setParent(avlNode);
        leftNode.setRightChild(avlNode);//第二步
//        leftNode.setParent(avlNode.getParent());
//        avlNode.setParent(leftNode);
        return leftNode;
    }

    /**
     * RR型
     *                  13                      20
     *                 /  \                    /  \
     *                10  20                  13  21
     *                   / \                 / \   \
     *                  19  21             10  19  30
     *                      \
     *                      30
     *          需要刷新节点13的高度
     */
    private AVLNode rotateRR(AVLNode avlNode){
        //进行左旋
        AVLNode rightNode = avlNode.getRightChild();
        avlNode.setRightChild(rightNode.getLeftChild());//第一步
//        rightNode.getLeftChild().setParent(avlNode);
        rightNode.setLeftChild(avlNode);//第二步
//        rightNode.setParent(avlNode.getParent());
//        avlNode.setParent(rightNode);
        return rightNode;
    }

    /**
     * LR型 == LL型 + RR型
     *       13               13                11
     *      /  \             /  \              /  \
     *     9   20           11  20            9   13
     *   /  \              /                 / \   \
     *  8   11            9                 8  10  20
     *     /             / \
     *    10            8  10
     *  需要刷新9,11,13这三个节点的高度
     */
    private AVLNode rotateLR(AVLNode avlNode){
        // 13为失衡点
        //先进行RR型处理9-11-10 , 此时9可以理解为失衡点
        AVLNode leftNode = rotateRR(avlNode.getLeftChild());
        //再进行LL型处理 13-11-9-8 ,此时13可以理解为失衡点
        avlNode.setLeftChild(leftNode);
        return rotateLL(avlNode);
    }

    /**
     * RL型
     *     13           13                     21
     *    /  \         /  \                   /  \
     *   10  24       10  21                 13  24
     *      /  \            \               /    / \
     *     21 25            24             10   22 25
     *      \              /  \
     *      22            22  25
     *  需要刷新21,24,13的高度
     */
    private AVLNode rotateRL(AVLNode avlNode){
        //13为失衡点
        //先进行LL型处理24-21-22,此时24为失衡点
        AVLNode rightNode = rotateLL(avlNode.getRightChild());
        //在进行RR型处理13-21-24-25 ,此时13为失衡点
        avlNode.setRightChild(rightNode);
        return rotateRR(avlNode);
    }

    //对节点进行旋转
    private AVLNode rotate(AVLNode avlNode){
        if (height(avlNode.getLeftChild()) > height(avlNode.getRightChild())  // L
                && height(avlNode.getLeftChild().getLeftChild()) >= height(avlNode.getLeftChild().getRightChild())) {//L
            System.out.println("LL型=====LL型");
            //旋转后，当前位置的节点已经变更成旋转后的那个节点了,所以需要返回覆盖当前位置的这个节点
            avlNode = rotateLL(avlNode);
            //LL旋转后,avlNode节点已经被替换,且会改变右节点的的高度
            //刷新当前根节点的右节点的高度
            refreshHeight(avlNode.getRightChild());
        }else if (height(avlNode.getRightChild()) > height(avlNode.getLeftChild()) //R
                && height(avlNode.getRightChild().getRightChild()) >= height(avlNode.getRightChild().getLeftChild())) {//R
            System.out.println("RR型=====RR型");
            //进行左旋
            avlNode = rotateRR(avlNode);
            //刷新当前根节点的左节点的高度
            refreshHeight(avlNode.getLeftChild());
        }else if (height(avlNode.getLeftChild()) > height(avlNode.getRightChild()) //L
                && height(avlNode.getLeftChild().getRightChild()) > height(avlNode.getLeftChild().getLeftChild())) {//R
            System.out.println("LR型=====LR型");
            avlNode = rotateLR(avlNode);
            //经过左右旋转,则需要刷新当前根节点左右节点的高度
            refreshHeight(avlNode.getLeftChild());
            refreshHeight(avlNode.getRightChild());
        }else if (height(avlNode.getRightChild()) > height(avlNode.getLeftChild()) //R
                && height(avlNode.getRightChild().getLeftChild()) > height(avlNode.getRightChild().getRightChild())) {//L
            System.out.println("RL型=====RL型");
            avlNode = rotateRL(avlNode);
            //经过左右旋转,则需要刷新当前根节点左右节点的高度
            refreshHeight(avlNode.getLeftChild());
            refreshHeight(avlNode.getRightChild());
        }

        return avlNode;
    }

    public void pre(AVLNode root){
        if (root!=null) {
            System.out.println("value:"+root.getData()+"---height:"+root.getHeight());//先输出根节点
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
    public void mid(AVLNode root){
        if (root!=null) {
            if (root.getLeftChild()!=null) {//先输出左节点
                mid(root.getLeftChild());
            }
            //再输出根节点
            System.out.println("value:"+root.getData()+"---height:"+root.getHeight());//先输出根节点
            if (root.getRightChild()!=null) {//再输出右节点
                mid(root.getRightChild());
            }
        }
    }

    /**
     * 后序遍历 ,根节点在左右节点后输出，然后同级先左再右, 如果左节点还有左子节点，则继续左遍历，直到leftChild为null,同理如果右节点还有右子节点，则继续遍历,直到rightChild为null
     * 3 , 7 , 9 , 8 , 6 , 10 , 20 , 13
     */
    public void succ(AVLNode root){
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
            System.out.println("value:"+root.getData()+"---height:"+root.getHeight());//先输出根节点
        }
    }

}
