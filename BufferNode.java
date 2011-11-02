/**
 * A node in a linked list that references a buffer, it's position in the file,
 * and whether it has been changed or not.
 *  @author Joshua Rush (jdrush89)
 *  @author Benjamin Roble (broble)
 *  @version Nov 2, 2011
 */
public class BufferNode
{
    //The buffer this node is associated with.
    private Buffer myBuffer;
    //A flag to keep track of whether or not the buffer has changed.
    private boolean changed;
    //The starting position of the buffer in the file (the byte #)
    private long blockID;
    //A constant to represent the starting position of an empty buffer.
    private static final int EMPTY = -1;

    /**
     * Initialize the BufferNode, creating a new Buffer and setting the blockID
     * and changed flag.
     */
    public BufferNode() {
        myBuffer = new Buffer();
        blockID = EMPTY;
        changed = false;
    }
    // ----------------------------------------------------------
    /**
     * Return the buffer associated with this BufferNode.
     * @return the buffer
     */
    public Buffer getBuffer()
    {
        return myBuffer;
    }
    // ----------------------------------------------------------
    /**
     * Set the buffer associated with this BufferNode.
     * @param myBuffer the buffer to set
     */
    public void setMyBuffer(Buffer myBuffer)
    {
        this.myBuffer = myBuffer;
    }
    // ----------------------------------------------------------
    /**
     * Return whether or not the buffer has been changed since it read in data.
     * @return the whether the buffer has been changed
     */
    public boolean isChanged()
    {
        return changed;
    }
    // ----------------------------------------------------------
    /**
     * Set whether the buffer in this node has been changed.
     * @param changed whether the buffer has been changed
     */
    public void setChanged(boolean changed)
    {
        this.changed = changed;
    }
    // ----------------------------------------------------------
    /**
     * Return the byte number where the data block starts for the buffer
     * associated with this node.
     * @return the blockID
     */
    public long getBlockID()
    {
        return blockID;
    }
    // ----------------------------------------------------------
    /**
     * Set the byte position from the file where the data in this node's buffer starts.
     * @param startReadingPosition the blockID to set
     */
    public void setBlockID(long startReadingPosition)
    {
        this.blockID = startReadingPosition;
    }

}
