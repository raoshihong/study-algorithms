package com.rao.study.algorithms.tree.redblack.v2;

public class RedBlackTree {
    private RedBlackNode root;

    public RedBlackNode getRoot() {
        return root;
    }

    public void setRoot(RedBlackNode root) {
        this.root = root;
    }

    public void insert(int data){
        for (RedBlackNode p = this.root;;){
            RedBlackNode redBlackNode = new RedBlackNode(true,data);
            if (p == null) {
                redBlackNode.setRed(false);
                this.root = redBlackNode;
                break;
            }
            int dir = 0;
            if (p.compare(redBlackNode)>=0) {
                dir = -1;
            } else if(p.compare(redBlackNode) <0){
                dir = 1;
            }
            RedBlackNode xp = p;
            p = (dir<=0)?p.getLeftChild():p.getRightChild();
            if (p == null) {//到叶子节点了
                //插入节点
                if (dir<=0) {
                    xp.setLeftChild(redBlackNode);
                    redBlackNode.setParent(xp);
                }else{
                    xp.setRightChild(redBlackNode);
                    redBlackNode.setParent(xp);
                }
                //节点插入完后,对整个数进行平衡处理,进行旋转和填充颜色 ,上面是logN的时间,下面平衡也花费logN的时间,所以红黑树需要花费2logN的时间
                this.root = balanceInsertion(root,redBlackNode);
                break;
            }
        }
    }

    /**
     * 从子节点的角度出发
     *
     * 1.如果当前插入的节点的父节点为空(即当前插入的节点为根节点),则直接将当前节点设置为黑色
     * 2.如果当前插入的节点的父节点不为空,则需要判断父节点的颜色
     *     2.1 如果父节点为黑色,则直接插入的节点不用变更颜色
     *     2.2 如果父节点为红色,则需要叔叔节点来进行判断 (注:红黑树中的叶子节点为NIL节点,且颜色规定为黑色,判断叔叔节点之前需要先判断祖父节点是否存在,如果祖父节点不存在则直接返回父节点)
     *         2.2.1 如果叔叔节点为红色(即叔叔节点不为空且颜色为红色),则将父节点和叔叔节点的颜色填充为黑色,当前插入节点的祖父节点的颜色填充为红色,将当前节点索引设置为祖父节点 （只有这块当前节点是会不断上升的,所以当前节点为root时退出循环）
     *         2.2.2 如果叔叔节点为黑色(注:这里的黑色包括叔叔节点存在且为黑色和叔叔节点不存在为nil,在红黑树中叶子节点为NIL节点,NIL节点为黑色),则需要判断当前节点的父节点的位置和当前插入节点的位置
     *             2.2.2.1 如果当前插入的节点的父节点为其祖父节点的左节点
     *                 2.2.2.1.1 如果当前插入的节点为其父节点的左节点,则需要进行LL型 ,右旋转,并交换当前插入节点的父节点和祖父节点的颜色,
     *                 2.2.2.1.2 如果当前插入的节点为其父节点的右节点,则需要先对当前插入节点和父节点进行左旋,左旋后,再执行2.2.2.1.1的操作进行右旋
     *             2.2.2.2 如果当前插入的节点的父节点为其祖父节点的右节点
     *                 2.2.2.2.1 如果当前插入的节点为其父节点的右节点,则需要进行RR型 ,左旋转,并交换当前插入节点的父节点和祖父节点的颜色
     *                 2.2.2.2.2 如果当前插入的节点为其父节点的左节点,则需要先对当前插入节点和父节点进行右旋,右旋后,再执行2.2.2.2.1的操作进行左旋
     *
     * @param root 为当前节点的父节点
     * @param current 为当前节点
     * @return
     */
    private RedBlackNode balanceInsertion(RedBlackNode root,RedBlackNode current){

        for (;;){
            //如果当前的节点的父节点为null,则表示当前节点为树的根节点
            RedBlackNode parent = current.getParent();
            if (parent == null) {
                current.setRed(false);
                return current;
            }
            //插入节点的父节点为黑色或者祖父节点为空,则直接不用调整
            RedBlackNode gParent = parent.getParent();
            if (!parent.isRed() || gParent == null) {
                root.setRed(false);
                return root;
            }

            //祖父节点不为空且父节点为红色   祖父节点的左右节点为红色
            if (isRed(gParent.getLeftChild()) && isRed(gParent.getRightChild())) {
                //祖父节点的左右节点都为红色,将祖父节点设置为当前
                gParent.setRed(true);
                gParent.getLeftChild().setRed(false);
                gParent.getRightChild().setRed(false);
                //当前节点上升
                current = gParent;
            }else{//叔叔节点为黑色
                if (gParent.getLeftChild()!=null && parent.compare(gParent.getLeftChild())==0) {//父节点为祖父节点的左节点
                    //当前节点的父节点的右节点不为空,且当前节点与父节点的右节点相等,则当前节点为父节点的右节点,反之当前节点为父节点的左节点
                    if (parent.getRightChild() != null && current.compare(parent.getRightChild()) == 0) {
                        // 当前节点为父节点的右节点  LR型,则先进行RR型旋转,左旋,左旋完后,满足下面的LL型
                        //此时需要旋转,旋转后,需要交互父节点和当前节点的位置
                        /**
                         * 开始是
                         *      gp                                      gp                                                         gp
                         *     /                                       /                                                           /
                         *    p                 旋转后               curr          为了满足curr是LL型,则需要将curr和p进行交换         p
                         *     \                                     /                                                           /
                         *    curr                                  p                                                          curr
                         *
                         */
                        current = parent;
                        root = rotateLeft(parent);
                        parent = current.getParent();
                        gParent = parent.getParent();
                    }

                    //LL型
                    if (current.compare(parent.getLeftChild()) == 0) {
                        // 当前节点为父节点的左节点,满足LL型,进行右旋
                        //交换颜色,父节点跟祖父节点颜色交换
//                        boolean isRed = parent.isRed();
//                        parent.setRed(gParent.isRed());//黑色
//                        gParent.setRed(isRed);//红色
                        parent.setRed(false);//黑色
                        gParent.setRed(true);//红色
                        //进行右旋,旋转后,这当前节点为红色,当前节点的父节点为黑色,此时满足当前节点父节点为黑色则直接插入(不用再调整,直接返回,再次循环就会进入上面的判断)
                        root = rotateRight(gParent);
                    }
                } else {//祖父节点的左节点为空或者父节点不等于祖父节点的左节点,那么父节点就是祖父节点的右节点
                    //父节点为祖父节点的右节点
                    if (parent.getLeftChild() != null && current.compare(parent.getLeftChild()) == 0) {
                        //当前节点为父节点的左节点 RL型,则先进行LL型旋转,右旋,右旋完后,满足下面的RR型
                        current = parent;
                        root = rotateRight(parent);
                        parent = current.getParent();
                        gParent = parent.getParent();
                    }

                    //RR型
                    if (current.compare(parent.getRightChild()) == 0) {
                        // 当前节点为父节点的右节点,满足RR型,进行左旋
                        //交换颜色,父节点跟祖父节点颜色交换
//                        boolean isRed = parent.isRed();
//                        parent.setRed(gParent.isRed());
//                        gParent.setRed(isRed);
                        parent.setRed(false);//黑色
                        gParent.setRed(true);//红色
                        //进行左旋,RR型,旋转后,当前节点的父节点为黑,所以不需要再做调整
                        root = rotateLeft(gParent);
                    }
                }
            }
        }
    }

    public void remove(){

    }

    /**
     * RR型
     * root
     *  \
     *  right
     *   \
     * @param p
     * @return
     */
    private RedBlackNode rotateLeft(RedBlackNode p){
        RedBlackNode rightNode = p.getRightChild();
        p.setRightChild(rightNode.getLeftChild());
        if (rightNode.getLeftChild()!=null) {
            rightNode.getLeftChild().setParent(p);
        }
        rightNode.setLeftChild(p);
        rightNode.setParent(p.getParent());
        if (p.getParent()!=null) {
            if(p.getParent().getLeftChild() !=null && p.getData() == p.getParent().getLeftChild().getData()){
                p.getParent().setLeftChild(rightNode);
            }else{
                p.getParent().setRightChild(rightNode);
            }
        }else{//父节点为空,则表示当前rightNode为根节点了
            this.root = rightNode;
            this.root.setRed(false);
        }
        p.setParent(rightNode);
        return this.root;
    }

    private RedBlackNode rotateRight(RedBlackNode p){
        RedBlackNode leftNode = p.getLeftChild();
        p.setLeftChild(leftNode.getRightChild());
        if (leftNode.getRightChild()!=null) {
            leftNode.getRightChild().setParent(p);
        }
        leftNode.setRightChild(p);
        leftNode.setParent(p.getParent());
        if (p.getParent()!=null) {
            if(p.getParent().getLeftChild() !=null && p.getData() == p.getParent().getLeftChild().getData()){
                p.getParent().setLeftChild(leftNode);
            }else{
                p.getParent().setRightChild(leftNode);
            }
        }else{
            this.root = leftNode;
            this.root.setRed(false);
        }
        p.setParent(leftNode);
        return this.root;
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
