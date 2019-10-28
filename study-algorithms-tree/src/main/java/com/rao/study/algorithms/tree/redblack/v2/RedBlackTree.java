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
                //节点插入完后,对整个树进行平衡处理,进行旋转和填充颜色 ,上面是logN的时间,下面平衡也花费logN的时间,所以红黑树需要花费2logN的时间
                //对整棵树平衡后,返回这棵树的根节点
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

    /**
     * 删除操作:
     * 1.删除的节点没有左右子节点 (直接删除)
     * 2.删除的节点只有一个子节点 (删除,使用其叶子节点替代被删除节点)
     * 3.删除的节点有左右子节点 (寻找被删除节点的右节点的最小子孙节点,替代被删除的节点,再删除最小子孙节点)
     *
     * 平衡操作:
     * 先找到替代节点，在从替代节点开始往上对整棵树进行自平衡操作
     *
     * 删除节点后，可能会影响整棵树对平衡，所以我们需要根据相关的删除情况进行相应的调整方式
     * 1.如果删除的节点为红色，此时整棵树的黑色节点并没有改变，所以不用做平衡处理
     * 2.如果删除的节点为黑色，此时因为整棵树的某个路径少了一个黑色节点可能导致失衡,所以需要进行平衡处理
     *     可以这么理解，如果此时删除的节点为父节点的左子节点，那么可以认为父节点的左子树少了一个黑色节点，右子树多了一个黑色节点，那么要想达到平衡，则需要将父节点的右子树的某个节点设置为红色，
     *     设置为红色节点的前提是：该节点的子节点必须都为黑色，父节点可以任意颜色（因为可以继续向上调整），所以可以这么理解，从替代节点的兄弟节点出发，如果兄弟节点的为黑色且兄弟节点的左右子节点都为黑色，
     *     那么我们可以大胆的将兄弟节点设置为红色节点，再将x节点设置为其父节点的索引，再次进行平衡判断（原因就是可能将其兄弟节点设置为红色节点后，其父节点也为红色，此时树失衡，所以将x再次设置为其父节点位子再次进行失衡判断）
     *     不断的往上平衡，直到根节点，因为根节点必须为黑色，所以平衡完后，最终肯定有一个节点变成了红色（可以理解为x的兄弟节点），所以整棵树达到平衡
     *
     */
    public void remove(int data){
        //红黑树删除节点还是跟普通二叉树删除节点一样
        //查找相同节点
        RedBlackNode p = find(this.root,data);
        if (p==null) {
            return;
        }else{

            if (p == this.root && p.getLeftChild() == null && p.getRightChild() == null) {
                this.root = null;
                return;
            }

            //根据p的叶子节点的情况进行删除节点,并找到其替代节点
            RedBlackNode x = null;
            //1.p没有子节点
            if (p.getLeftChild() == null && p.getRightChild() == null) {
                x = p;
            }else if (p.getRightChild() == null || p.getLeftChild() == null) {
                //2.p只有一个子节点
                x = p.getRightChild()!=null?p.getRightChild():p.getLeftChild();
            } else if (p.getRightChild() != null && p.getLeftChild() != null) {//3.p有两个子节点
                //查找其右节点的最小子孙节点
                RedBlackNode s = p.getRightChild();
                while (s.getLeftChild()!=null) {
                    s = s.getLeftChild();
                }

                //删除节点有两个子节点的情况,实际相当于删除的是最小子节点s,如果s有右子节点,那么替代s的节点就是s的右子节点
                if (s.getRightChild()!=null) {
                    x = s.getRightChild();
                }else {//如果s没有右子节点,则表示s没有左右子节点的情况
                    x = s;
                }

                //进行p和s的替换
                p.setData(s.getData());//删除节点有两个子节点的情况,实际不是删除该节点,而是将其值直接替换为他的右节点的最小子孙节点的值,然后才是真正的删除其右节点的最小子孙节点
                //将最小子孙节点设置为要删除的p节点
                p = s;
            }

            //进行节点的删除
            if (p!=x) {//将其p没有子节点的情况排除,这种情况要到平衡后才能删除
                x.setParent(p.getParent());
                if (p.getParent().getRightChild() == p) {
                    p.getParent().setRightChild(x);
                } else {
                    p.getParent().setLeftChild(x);
                }
                x.setRightChild(p.getRightChild());
                x.setLeftChild(p.getLeftChild());
                //清除p节点
                p.setParent(null);
                p.setRightChild(null);
                p.setLeftChild(null);
            }

            //对是否需要平衡进行判断,如果删除的节点是红色,那么就直接删除,不影响整棵树的平衡,如果删除的是黑色节点,则表示整棵树的某个路径少了一个黑色节点,整棵树失去了平衡,需要进行平衡处理
            if (!p.isRed()) {
                //进行从替代节点开始,对整棵树进行自平衡
                this.root = balanceDeletion(this.root,x);
            }

            //对x == p的节点进行删除处理
            if (x == p) {
                //直接删除p节点
                RedBlackNode pp = p.getParent();
                if (pp != null) {
                    if (p == pp.getLeftChild()) {
                        pp.setLeftChild(null);
                    }else {
                        pp.setRightChild(null);
                    }

                    p.setParent(null);
                }
            }
        }
    }

    /**
     *
     *
     *
     * 平衡处理分以下几种情况：
     *     1.如果替代节点为红色，直接将替代节点设置为黑色，因为少了个黑色，所以直接补上黑色即可
     *     2.如果替代节点为黑色且不是树的根节点，则需要根据替代节点的兄弟节点进行判断
     *      2.1替代节点为其父节点的左节点的情况
     *         2.1.1 替代节点的兄弟节点为黑色
     *             2.1.1.1 兄弟节点的左右子节点都为黑色,此时设置兄弟节点颜色为红色，将替代节点的父节点位置赋值给替代节点，再对新对替换节点进行平衡处理（突破口）
     *             2.1.1.2 兄弟节点的右节点为红色，左节点颜色任意，则将兄弟节点设置为父节点的颜色，父节点的颜色设置为黑色，兄弟节点的右节点设置为黑色，再对父节点进行左选，最终形成wl为x对兄弟节点,而wl的颜色任意,所以相当于最终又开始对x进行新一轮对平衡处理
     *                           p
     *                          / \
     *                         x   w
     *                            / \
     *                           wl wr
     *                        上面p颜色任意，x颜色为黑色,w颜色为黑色，wl的颜色任意，wr的颜色为红色
     *                        经过2.1.1.2的处理后
     *                             w
     *                            / \
     *                           p  wr
     *                          / \
     *                         x  wl
     *
     *                       此时x的兄弟节点变成了wl，因为wl的颜色任意,所以相当于右重新开始对x进行平衡判断
     *
     *             2.1.1.3 兄弟节点的右节点为黑色，左节点为红色，则兄弟节点设置为红色，兄弟的左节点设置为黑色，再对兄弟节点进行左旋，得到2.1.1.2的情况，再通过2.1.1.2重新对x节点进行平衡处理
     *
     *
     *                              p
     *                             / \
     *                            x   w
     *                               / \
     *                              wl wr
     *                           上面p颜色任意，x颜色为黑色,w颜色为黑色，wl的颜色为红色，wr的颜色为黑色
     *                           经过2.1.1.2的处理后
     *                                p
     *                               / \
     *                              x  wl
     *                                   \
     *                                    w
     *                                     \
     *                                     wr
     *                              此时x的兄弟节点变成了wl，此时wl的颜色为黑色，w的颜色变成了红色，刚好满足2.1.1.2的情况，所以再根据2.1.1.2的情况进行处理
     *
     *
     *         2.1.2 替代节点的兄弟节点为红色，则将父节点设置为红色，兄弟节点设置为黑色，对父节点进行左旋，得到x对兄弟节点为黑色，那么又相当于再次对x为黑色，x的兄弟节点为黑色对情况进行判断兄弟的左右子节点的颜色判断
     *               红黑树如果一个节点为黑色，那么它的父节点一定为黑色，它的左右子节点一定为黑色，所以经过2.1.2的情况处理，最终x的兄弟节点变成了黑色，所以又要重新对x进行上面对情况对处理
     *
     *
     *
     *      2.2 替代节点为父节点对右节点的情况
     *         这种情况跟上面的左节点逻辑是一样的，只是左右节点互换
     * @param root
     * @param x
     * @return
     */
    private RedBlackNode balanceDeletion(RedBlackNode root,RedBlackNode x){

        //对整棵树从x开始进行自平衡
        for(RedBlackNode xp;;){
            if (x == null || x == root) {//表示x达到了根
                return root;
            }

            if (x.getParent() == null) {
                x.setRed(false);
                return root;
            }

            //如果替代节点为红色，直接将替代节点设置为黑色，因为少了个黑色，所以直接补上黑色即可
            if (x.isRed()) {
                x.setRed(false);
                return root;
            }

            //否则如果替代节点为黑色,则需要判断其兄弟节点是什么颜色,以替代节点为其父节点的左节点为例,则兄弟节点为其父节点的右节点,因为左边少了一个黑节点,所以需要右边也少一个黑节点
            //如果兄弟节点为黑色,并且兄弟节点的左右子节点为黑色,此时就可以直接将兄弟节点直接设置为红色(因为红色节点的父节点,左右子节点都只能是黑色)

            //1.替代节点为父节点的左节点
            xp = x.getParent();
            if (x == xp.getLeftChild()) {
                RedBlackNode s = xp.getRightChild();
                if (s == null) {//表示兄弟节点为空,那么只有将x往上升,再对x的兄弟节点进行处理
                    x = xp;
                }else{
                    if (!s.isRed()) {
                        //兄弟节点为黑色
                        if (!isRed(s.getLeftChild()) && !isRed(s.getRightChild())) {//这块是整个平衡的突破口,其他情况都需要不断调整,往这种情况靠拢
                            // 1.1 兄弟节点为黑色,且兄弟节点的左右子节点为黑色

                            //这种情况,直接将兄弟节点设置为红色,这样就相当于平衡了其父节点,但是有可能其父节点的父节点不平衡,所以，将替代节点往上升,继续平衡判断
                            s.setRed(true);
                            x = xp;//x上升
                        } else {

                            if (isRed(s.getLeftChild())) {
                                // 兄弟节点的左节点为红色,右节点为黑色
                                //这种情况需要交换兄弟节点和兄弟节点的左节点的颜色,再对兄弟节点进行右旋,得到上面x为黑色,兄弟节点为黑色,兄弟节点的右节点为红色的情况
                                //交换颜色
                                s.getLeftChild().setRed(false);
                                s.setRed(true);
                                //在对兄弟节点进行右旋
                                root = rotateRight(s);

                                //右旋完后,得到的情况是x节点为黑色,兄弟节点为黑色,兄弟节点的右节点为红色,就是下面的情况
                            }

                            if(isRed(s.getRightChild())){
                                //兄弟节点的右节点为红色,左节点颜色任意,这种情况,无法将兄弟节点设置为黑色,所以需要对x的父节点进行旋转
                                //先将兄弟节点的颜色设置为父节点的颜色
                                s.setRed(xp.isRed());
                                //再将父节点设置为黑色
                                xp.setRed(false);
                                //再将兄弟节点的右节点设置为黑色
                                s.getRightChild().setRed(false);
                                //对父节点进行左旋
                                root = rotateLeft(xp);

                                //左旋完后,得到的情况是x节点为黑色,兄弟节点颜色任意,此时就需要重新对整棵树进行平衡处理
                                //这种情况表示已经达到了平衡,所以把x设置为root
                                x = root;
                            }

                        }

                    }else{
                        //兄弟节点为红色
                        //兄弟节点为红色,那么意思就是兄弟节点的左右子节点为黑色,父节点为黑色,此时交换兄弟节点和父节点的颜色,再对父节点进行左旋,最终得到x为黑色,x 的兄弟节点为黑色的情况
                        //为什么需要交换父节点和兄弟节点的颜色,原因就是为了保证那棵树的根仍然为黑色
                        xp.setRed(true);
                        s.setRed(false);
                        root = rotateLeft(xp);

                        //左旋后,得到的情况是x节点为黑色,兄弟节点为黑色的情况
                    }
                }
            } else {
                //替代节点为右节点的情况,(处理逻辑跟左节点的逻辑一样,只是节点的左右替换下)
                RedBlackNode s = xp.getLeftChild();
                if (s == null) {//兄弟节点为空,则直接向上进行判断
                    x = xp;
                }else {
                    //兄弟节点为黑色
                    if (!isRed(s)) {
                        //兄弟节点的左右子节点为黑色
                        if (!isRed(s.getLeftChild()) && !isRed(s.getRightChild())) {
                            s.setRed(true);
                            x = xp;//x上升
                        } else if (isRed(s.getLeftChild())) {
                            //兄弟节点的左节点为红色,右节点颜色任意
                            s.setRed(xp.isRed());
                            xp.setRed(false);
                            s.getLeftChild().setRed(false);
                            root = rotateRight(xp);
                        } else if (isRed(s.getRightChild())) {
                            //兄弟节点的右节点为红色,左节点为黑色
                            s.setRed(true);
                            s.getRightChild().setRed(false);
                            root = rotateLeft(xp);
                        }
                    }else{
                        //兄弟节点为红色
                        xp.setRed(true);
                        s.setRed(false);
                        root = rotateRight(xp);
                    }
                }
            }
        }
    }

    private RedBlackNode find(RedBlackNode p,int data){
        if (p == null) {
            return null;
        }

        if (p.getData() > data) {
            return find(p.getLeftChild(), data);
        } else if (p.getData() < data) {
            return find(p.getRightChild(), data);
        } else {
            return p;
        }

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
