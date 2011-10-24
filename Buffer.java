import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Buffer
{
    private byte[] data;

    // ----------------------------------------------------------
    /**
     * @return the data
     */
    public byte[] getData()
    {
        return data;
    }

    // ----------------------------------------------------------
    /**
     * @param data the data to set
     */
    public void setData(byte[] data)
    {
        this.data = data;
    }

    public Buffer()
    {
        data = new byte[BufferPool.BUFFER_SIZE];
    }

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


