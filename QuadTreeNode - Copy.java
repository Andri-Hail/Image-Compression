
public interface QuadTreeNode {
    /**
     * Gets the color at coordinate {@code (x, y)}. Find the leaf node that
     * corresponds to this coordinate and get the color of that node.
     * <p/>
     *
     * @param x the {@code x}-coordinate
     * @param y the {@code y}-coordinate
     * @return the color that is on (x, y)
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of bounds
     */
    int getColor(int x, int y);

    /**
     * Sets the color at coordinates {@code (x, y)}. 
     *
     * @param x     the {@code x}-coordinate
     * @param y     the {@code y}-coordinate
     * @param color the color (x, y) should be set to
     * @throws IllegalArgumentException if {@code x} or {@code y} is out of bounds
     */
    void setColor(int x, int y, int color);

    /**
     * Returns the {@link QuadTreeNode} in the specified quadrant.
     * If this QuadTreeNode is a leaf, then this method returns {@code null}
     * for all quadrants. Otherwise this method returns a non-{@code null} value.
     *
     * @param quadrant the quadrant to retrieve
     * @return quadrant instance or {@code null}
     */
    QuadTreeNode getQuadrant(QuadName quadrant);

    /**
     * Returns the dimensions of the area this QuadTreeNode represents.
     * <p/>
     *
     * @return the size of the square's side length represented by this QuadTreeNode.
     */
    int getDimension();

    /**
     * Returns the number of descendants this current node contains (including itself).
     * <p/>
     *
     * @return the number of descendants contained by this QuadTreeNode
     */
    int getSize();

    /**
     * Returns {@code true} if this {@link QuadTreeNode} is a leaf.
     *
     * @return {@code true} if the QuadTreeNode is a leaf
     */
    boolean isLeaf();

    /**
     * Decompresses the QuadTree into a 2d-array. The returned array contains integers that
     * represent the color at each coordinate. The returned 2D array satisfies {@code
     * result[y][x] == getColor(x, y)} for each coordinate {@code (x, y)}.
     *
     * @return a newly initialized array storing the decompressed image data
     */
    int[][] decompress();

    /**
     * Gets the compression ratio of the current QuadTree.
     *
     * @return the compression ratio
     */
    double getCompressionRatio();

    /**
     * Enumeration for representing the location of a quadrant.
     */
    enum QuadName {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
