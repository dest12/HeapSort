import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A Buffer that stores bytes in a byte array.  The array and individual shorts
 * can be retrieved and set.
 *  @author Joshua Rush (jdrush89)
 *  @author Benjamin Roble (broble)
 *  @version Nov 2, 2011
 */
public class Buffer
{
    //the byte array that stores the bytes in this buffer
    private byte[] data;
    /**
     * Create a new Buffer, initializing the data array.
     */
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
     * Set the bytes in the data array to be the bytes in the newData array,
     * starting at the indicated position.  pos + newData.length should not be
     * greater than data.length.
     * @param pos the position to start overwriting bytes at
     * @param newData the byte array containing the bytes to be copied
     */
    public void setRecord(int pos, byte[] newData)
    {
        for(int i = 0; i < newData.length; i++)
        {
            data[i + pos] = newData[i];
        }
    }
    /**
     * Return a record made up of a 2 byte key and 2 byte value.
     * @param pos the byte position where the record starts
     * @return the record
     */
    public byte[] getRecord(int pos)
    {
        byte[] record = new byte[4];
        System.arraycopy(data, pos, record, 0, 4);
        return record;
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
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put(data[pos]);
        bb.put(data[pos+1]);
        short shortVal = bb.getShort(0);
        if (shortVal < 0)
            System.out.println("Short was negative: " + shortVal);
        return shortVal;
    }
}


