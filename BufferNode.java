
public class BufferNode
{
    private Buffer myBuffer;
    private boolean changed;
    private long blockID;

    private static final int EMPTY = -1;

    public BufferNode() {
        myBuffer = new Buffer();
        blockID = EMPTY;
        changed = false;
    }
    // ----------------------------------------------------------
    /**
     * @return the myBuffer
     */
    public Buffer getBuffer()
    {
        return myBuffer;
    }
    // ----------------------------------------------------------
    /**
     * @param myBuffer the myBuffer to set
     */
    public void setMyBuffer(Buffer myBuffer)
    {
        this.myBuffer = myBuffer;
    }
    // ----------------------------------------------------------
    /**
     * @return the changed
     */
    public boolean isChanged()
    {
        return changed;
    }
    // ----------------------------------------------------------
    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed)
    {
        this.changed = changed;
    }
    // ----------------------------------------------------------
    /**
     * @return the blockID
     */
    public long getBlockID()
    {
        return blockID;
    }
    // ----------------------------------------------------------
    /**
     * @param startReadingPosition the blockID to set
     */
    public void setBlockID(long startReadingPosition)
    {
        this.blockID = startReadingPosition;
    }

}
