package com.rao.study.algorithms.tree.redblack.v1;

public class RedBlackTree {
    private RedBlackNode root;

    public RedBlackNode getRoot() {
        return root;
    }

    public void setRoot(RedBlackNode root) {
        this.root = root;
    }

    public void insert(int data){
        this.root = insert(this.root,data);
        this.root.setRed(false);//永远保证根节点为黑色
    }

    /**
     * 1.如果当前插入的节点的父节点为空(即当前插入的节点为根节点),则直接将当前节点设置为黑色
     * 2.如果当前插入的节点的父节点不为空,则需要判断父节点的颜色
     *     2.1 如果父节点为黑色,则直接插入的节点不用变更颜色
     *     2.2 如果父节点为红色,则需要叔叔节点来进行判断 (注:红黑树中的叶子节点为NIL节点,且颜色规定为黑色)
     *         2.2.1 如果叔叔节点为红色(即叔叔节点不为空且颜色为红色),则将父节点和叔叔节点的颜色填充为黑色,当前插入节点的祖父节点的颜色填充为红色,再将当前调整的树的根节点索引设置为祖父节点,重复1判断
     *         2.2.2 如果叔叔节点为黑色(注:这里的黑色包括叔叔节点存在且为黑色和叔叔节点不存在为nil,在红黑树中叶子节点为NIL节点,NIL节点为黑色),则需要判断当前节点的父节点的位置和当前插入节点的位置
     *             2.2.2.1 如果当前插入的节点的父节点为其祖父节点的左节点
     *                 2.2.2.1.1 如果当前插入的节点为其父节点的左节点,则需要进行右旋转,并交换当前插入节点的父节点和祖父节点的颜色,再将当前调整的树的根节点索引设置为祖父节点,重复1判断
     *                 2.2.2.1.2 如果当前插入的节点为其父节点的右节点,则需要先对当前插入节点和父节点进行左旋,左旋后,再执行2.2.2.1.1的操作进行右旋
     *             2.2.2.2 如果当前插入的节点的父节点为其祖父节点的右节点
     *                 2.2.2.2.1 如果当前插入的节点为其父节点的右节点,则需要进行左旋转,并交换当前插入节点的父节点和祖父节点的颜色,再将当前调整的树的根节点索引设置为祖父节点,重复1判断
     *                 2.2.2.2.2 如果当前插入的节点为其父节点的左节点,则需要先对当前插入节点和父节点进行右旋,右旋后,再执行2.2.2.2.1的操作进行左旋
     * @param root
     * @param data
     * @return
     */
    private RedBlackNode insert(RedBlackNode root,int data){
        //创建一个新的节点
        RedBlackNode redBlackNode = new RedBlackNode(true,data);
        if (root == null) {
            root = redBlackNode;
            return root;
        }else{
            if (root.compare(redBlackNode)>=0) {
                RedBlackNode leftChild = insert(root.getLeftChild(),data);
                root.setLeftChild(leftChild);
            } else if(root.compare(redBlackNode) <0){
                RedBlackNode rightChild = insert(root.getRightChild(),data);
                root.setRightChild(rightChild);
            }
        }

        //判断是否需要调整
        return should(root);
    }

    public void remove(int data){
        this.root = remove(this.root,data);
        if (this.root!=null) {
            this.root.setRed(false);//永远保证根节点为黑色
        }
    }

    private RedBlackNode remove(RedBlackNode root,int data){
        //红黑树删除操作跟普通的二叉树删除操作一样,只是在删除后需要保证红黑树任然满足限制条件,所以需要对树进行平衡处理

        RedBlackNode redBlackNode = new RedBlackNode(true,data);
        if (root == null) {
            return null;
        }else {
            if (redBlackNode.compare(root)<0) {
                root.setLeftChild(remove(root.getLeftChild(),data));
                //刷新root
            } else if (redBlackNode.compare(root)>0) {
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
                    RedBlackNode leftMax = findLeftMax(root.getLeftChild());
                    root = remove(root,leftMax.getData());
                    root.setData(leftMax.getData());
                }
            }
        }

        //进行平衡处理
        return should(root);
    }

    private RedBlackNode findLeftMax(RedBlackNode root){
        if (root.getRightChild() == null) {
            return root;
        } else {
            return findLeftMax(root.getRightChild());
        }
    }

    /**
     * 从父节点的角度
     * 从上面几点我们可以理解,上面的红黑树操作采用的是将红色节点左倾，我们可以理解为,只要有右节点为红色,则需要进行左旋,下面从父节点的角度理解,即理解为父节点是否失衡:
     *  1.插入的节点为根节点,直接设置颜色为black
     *  2.插入节点的父节点的右节点为红色,左节点不为红色,则进行左旋
     *  3.插入节点的父节点的左节点为红色,且父节点的左节点的左节点为红色,则进行右旋
     *  4.插入节点的父节点的左右子节点都为红色,则进行颜色填充,左右节点变为黑色,父节点变为红色
     *
     *  上面的操作是有顺序的,步骤2左旋后,就可能出现步骤3的情况,步骤3右旋后就会出现步骤4的情况
     * @param parent
     * @return
     */
    private RedBlackNode should(RedBlackNode parent){
        //下面几种情况,表示父节点已经失衡了,需要做相应的调整
        if (isRed(parent.getRightChild()) && !isRed(parent.getLeftChild())) {
            parent = rotateLeft(parent);
        }
        if (isRed(parent.getLeftChild()) && parent.getLeftChild()!=null && isRed(parent.getLeftChild().getLeftChild())) {//这里会有空指针
            //LL型,需要交换颜色,父节跟左节点交换颜色
            boolean isRed = parent.isRed();
            parent.setRed(parent.getLeftChild().isRed());
            parent.getLeftChild().setRed(isRed);
            //再进行旋转
            parent = rotateRight(parent);
        }

        if(isRed(parent.getLeftChild()) && isRed(parent.getRightChild())){
            parent = fillColor(parent);
        }
        return parent;
    }

    private RedBlackNode rotateLeft(RedBlackNode root){
        RedBlackNode rightNode = root.getRightChild();
        root.setRightChild(rightNode.getLeftChild());
        rightNode.setLeftChild(root);
        rightNode.setRed(root.isRed());
        root.setRed(true);
        return rightNode;
    }

    private RedBlackNode rotateRight(RedBlackNode root){
        RedBlackNode leftNode = root.getLeftChild();
        root.setLeftChild(leftNode.getRightChild());
        leftNode.setRightChild(root);
        leftNode.setRed(root.isRed());
        root.setRed(true);
        return leftNode;
    }

    private RedBlackNode fillColor(RedBlackNode root){
        root.setRed(true);
        root.getLeftChild().setRed(false);
        root.getRightChild().setRed(false);
        return root;
    }

    private boolean isRed(RedBlackNode node){
        return node!=null&&node.isRed()?true:false;
    }

    /**
     * 中序遍历(比较重要) 根节点在左右节点之间输出,如果左节点还有左子节点则继续遍历,直到leftChild为null
     * 3 , 6 , 7 , 8 , 9 , 10 , 13 , 20
     */
    public void mid(RedBlackNode root){
        if (root!=null) {
            if (root.getLeftChild()!=null) {//先输出左节点
                mid(root.getLeftChild());
            }
            //再输出根节点
            System.out.println("value:"+root.getData()+"---color:"+(root.isRed()?"red":"black"));//先输出根节点
            if (root.getRightChild()!=null) {//再输出右节点
                mid(root.getRightChild());
            }
        }
    }

}
