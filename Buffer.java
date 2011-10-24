import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Buffer that stores bytes in a byte array.  The array and individual shorts
 * can be retrieved and set.
 */
public class Buffer
{
    //the byte array that stores the bytes in this buffer
    private byte[] data;
    
    public Buffer()
    {
        data = new byte[BufferPool.BUFFER_SIZE];
    }
    // ----------------------------------------------------------
    /**
     * Return the byte array storing data.
     * @return the byte array
     */
    public byte[] getData()
    {
        return data;
    }

    // ----------------------------------------------------------
    /**
     * Set the data byte array.
     * @param data the data to set
     */
    public void setData(byte[] data)
    {
        this.data = data;
    }

    /**
     * Returns a short made up of the bytes in the array at position pos and
     * pos + 1.
     * @param pos the position in the data array to get the short from
     * @return a short from the data array
     */
    public short getShort(int pos)
    {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[pos]);
        bb.put(data[pos+1]);
        short shortVal = bb.getShort(0);
        return shortVal;
    }
}


