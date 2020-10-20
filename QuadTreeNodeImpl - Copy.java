
// CIS 121, QuadTree

public class QuadTreeNodeImpl implements QuadTreeNode {

    int x;
    int y;
    Integer color;
    boolean isLeaf;
    int size;
    QuadName quadrant;
    QuadTreeNodeImpl nodeUL;
    QuadTreeNodeImpl nodeLL;
    QuadTreeNodeImpl nodeUR;
    QuadTreeNodeImpl nodeLR;

    QuadTreeNodeImpl(int x, int y, Integer color, boolean isLeaf, int size, QuadTreeNodeImpl nodeUL,
            QuadTreeNodeImpl nodeUR, QuadTreeNodeImpl nodeLR, QuadTreeNodeImpl nodeLL) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.isLeaf = isLeaf;
        this.size = size;
        // this.quadrant = quadrant;
        this.nodeUL = nodeUL;
        this.nodeUR = nodeUR;
        this.nodeLL = nodeLL;
        this.nodeLR = nodeLR;

    }

    /* 
     * @param image  image to put into the tree
     * @param yBound
     * @return the newly build QuadTreeNode instance which stores the compressed
     *         image
     * @throws IllegalArgumentException if image is null
     * @throws IllegalArgumentException if image is empty
     * @throws IllegalArgumentException if image.length is not a power of 2
     * @throws IllegalArgumentException if image, the 2d-array, is not a perfect
     *                                  square
     */
    public static QuadTreeNodeImpl buildFromIntArray(int[][] image) {
        if (image == null) {
            throw new IllegalArgumentException();
        }
        if (image.length == 0) {
            throw new IllegalArgumentException();
        }
        int x = image[0].length;
        int y = image.length;
        if (x == 0 || y == 0) {
            throw new IllegalArgumentException();
        }
        if (((x & (x - 1)) != 0) || ((y & (y - 1)) != 0)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < y; i++) {
            if (image[i].length != y) {
                throw new IllegalArgumentException();
            }
        }

        return createTree(image, 0, x - 1, 0, y - 1, null);

    }

    public static QuadTreeNodeImpl createTree(int[][] image, int xStart, int xEnd, int yStart, 
            int yEnd,
            QuadTreeNodeImpl parent) {
        if (xEnd == xStart) {
            return new QuadTreeNodeImpl(xEnd - xStart + 1, yEnd - yStart + 1, 
                    image[yStart][xStart], true, 1, null, null, null, null);
        } else {
            QuadTreeNodeImpl a = createTree(image, xStart, (xEnd - (xEnd - xStart + 1) / 2), yStart,
                    (yEnd - (yEnd - yStart + 1) / 2), parent);
            QuadTreeNodeImpl b = createTree(image, (1 + (xEnd - (xEnd - xStart + 1) / 2)), xEnd, 
                    yStart,
                    (yEnd - (yEnd - yStart + 1) / 2), parent);
            QuadTreeNodeImpl c = createTree(image, (1 + (xEnd - (xEnd - xStart + 1) / 2)), xEnd,
                    (1 + (yEnd - (yEnd - yStart + 1) / 2)), yEnd, parent);
            QuadTreeNodeImpl d = createTree(image, xStart, (xEnd - (xEnd - xStart + 1) / 2),
                    (1 + (yEnd - (yEnd - yStart + 1) / 2)), yEnd, parent);

            if (a.color == b.color && a.color == c.color && a.color == d.color && a.color != null) {
                return new QuadTreeNodeImpl(xEnd - xStart + 1, yEnd - yStart + 1, a.color, true, 1,
                        null, null, null, null);
            } else {
                Integer noColor = null;
                return new QuadTreeNodeImpl(xEnd - xStart + 1, yEnd - yStart + 1, noColor, false,
                        (1 + a.size + b.size + c.size + d.size), a, b, c, d);
            }
        }
    }

    @Override
    public int getColor(int x, int y) {
        if (x < 0 || y < 0 || x >= this.x || y >= this.y) {
            throw new IllegalArgumentException();
        }
        if (this.isLeaf()) {

            return Integer.valueOf(this.color);
        } else {
            if (x < this.x / 2 && y < this.y / 2) {
                return this.nodeUL.getColor(x, y);
            } else if (x < this.x / 2 && y >= this.y / 2) {
                return this.nodeLL.getColor(x, y - this.y / 2);
            } else if (x >= this.x / 2 && y >= this.y / 2) {
                return this.nodeLR.getColor(x - this.x / 2, y - this.y / 2);
            } else if (x >= this.x / 2 && y < this.y / 2) {
                return this.nodeUR.getColor(x - this.x / 2, y);
            }
        }

        return Integer.valueOf(this.color);
    }

    void setColorHelper(QuadTreeNodeImpl node, int c, int x, int y) {
        if (node.x == 1) {
            node.color = c;
            node.isLeaf = true;
            node.size = 1;
        } else {
            node.nodeUR = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, true, 1, null, 
                    null, null, null);
            node.nodeLL = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, true, 1, null, 
                    null, null, null);
            node.nodeLR = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, true, 1, null,
                    null, null, null);
            node.nodeUL = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, true, 1, null,
                    null, null, null);

            if (x < node.x / 2 && y < node.y / 2) {

                node.nodeUL = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, false, 1, 
                        null, null, null, null);
                node.color = null;
                setColorHelper(node.nodeUL, c, x, y);

            } else if (x < node.x / 2 && y >= node.y / 2) {

                node.nodeLL = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, false, 1, 
                        null, null, null, null);
                node.color = null;
                setColorHelper(node.nodeLL, c, x, y - node.y / 2);

            } else if (x >= node.x / 2 && y >= node.y / 2) {

                node.nodeLR = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, false, 1, 
                        null, null, null, null);
                node.color = null;
                setColorHelper(node.nodeLR, c, x - node.x / 2, y - node.y / 2);

            } else if (x >= node.x / 2 && y < node.y / 2) {

                node.nodeUR = new QuadTreeNodeImpl(node.x / 2, node.y / 2, node.color, false, 1, 
                        null, null, null, null);
                node.color = null;
                setColorHelper(node.nodeUR, c, x - node.x / 2, y);
            }
        }

    }

    @Override
    public void setColor(int x, int y, int c) {
        if (x < 0 || y < 0 || x >= this.x || y >= this.y) {
            throw new IllegalArgumentException();
        }
        if (this.isLeaf()) {
            if (this.x == 1 || this.color == c) {
                this.color = c;

            } else {
                this.isLeaf = false;
                setColorHelper(this, c, x, y);
            }

        } else {
            if (x < this.x / 2 && y < this.y / 2) {
                this.nodeUL.setColor(x, y, c);
            } else if (x < this.x / 2 && y >= this.y / 2) {
                this.nodeLL.setColor(x, y - this.y / 2, c);
            } else if (x >= this.x / 2 && y >= this.y / 2) {
                this.nodeLR.setColor(x - this.x / 2, y - this.y / 2, c);
            } else if (x >= this.x / 2 && y < this.y / 2) {
                this.nodeUR.setColor(x - this.x / 2, y, c);
            }
        }
        
        if (this.nodeUL != null) {
            if ((this.nodeUL.color == this.nodeUR.color && this.nodeUL.color == this.nodeLL.color
                  && this.nodeUL.color == this.nodeLR.color && this.nodeUL.color != null)) {
                this.color = this.nodeUR.color;
                this.nodeUL = null;
                this.nodeLL = null;
                this.nodeLR = null;
                this.nodeUR = null;
    
            }
        }

    }

    @Override
    public QuadTreeNode getQuadrant(QuadName quadrant) {

        if (quadrant == QuadTreeNode.QuadName.TOP_LEFT) {
            return this.nodeUL;
        } else if (quadrant == QuadTreeNode.QuadName.BOTTOM_LEFT) {
            return this.nodeLL;
        } else if (quadrant == QuadTreeNode.QuadName.TOP_RIGHT) {
            return this.nodeUR;
        } else {
            return this.nodeLR;
        }
    }

    @Override
    public int getDimension() {
        return this.x;

    }

    @Override
    public int getSize() {
        if (this.isLeaf()) {
            return 1;
        } else {
            return 1 + this.nodeUL.getSize() + this.nodeLL.getSize() + this.nodeLR.getSize() 
                + this.nodeUR.getSize();
        }

    }

    @Override
    public boolean isLeaf() {
        if (this.nodeUL == null && this.nodeUR == null && this.nodeLL == null && 
                this.nodeLR == null) {
            return true;
        }
        if ((this.nodeUL.color == this.nodeUR.color && this.nodeUL.color == this.nodeLL.color
                && this.nodeUL.color == this.nodeLR.color && this.nodeUL.color != null)) {
            this.color = this.nodeUL.color;
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int[][] decompress() {
        int[][] image = new int[this.x][this.y];
        for (int i = 0; i < this.y; i++) {
            for (int j = 0; j < this.x; j++) {
                image[i][j] = this.getColor(j, i);
            }
        }
        return image;
    }

    @Override
    public double getCompressionRatio() {
        return (double) this.getSize() / (this.x * this.y);
    }
}
