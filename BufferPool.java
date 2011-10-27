import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * Contains a pool of buffers that can store byte data from a binary file.
 * When requests for data that isn't stored in a buffer is made, the pool will
 * pull a block of data from the file into an empty buffer, or overrite the data
 * in the least recently used buffer (writing it's contents to the file if
 * they've been changed).
 */
public class BufferPool
{
    //A linked list containing the buffers in this pool
    private LinkedList<BufferNode> bufferList;
    //the number of buffers this pool can hold at maximum
    private int numBuffers;
    //the binary file that the bufferpool interfaces with
    private RandomAccessFile myFile;

    //number of cache hits
    private int cacheHits = 0;

    //number of cache misses
    private int cacheMisses = 0;

     //number of disk reads
    private int diskReads = 0;

     //number of disk writes
    private int diskWrites = 0;

    /**
     * A constant for the number of bytes a buffer can hold
     */
    public static final int BUFFER_SIZE = 4096;
    /**
     * Create a new BufferPool with the specified number of buffers.
     * @param pNumBuffs the number of buffers this BufferPool can hold
     * @param pFile the binary file this BufferPool interfaces with
     */
    public BufferPool(int pNumBuffs, File pFile)
    {
        bufferList = new LinkedList<BufferNode>();
        numBuffers = pNumBuffs;
        try
        {
            myFile = new RandomAccessFile(pFile, "rw");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("COULD NOT FIND THE FILE.");
            e.printStackTrace();
        }

    }

    // ----------------------------------------------------------
    /**
     * Return a a short key value from the record at the specified position.
     * @param recNum the number of the record to get the key from
     * @return the short that is thee key for the specified record
     */
    public short requestKey(long recNum) {
        BufferNode bn = bufferContains(recNum);
        if(bn == null) bn = bufferRead(recNum);

        return getKey(bn, recNum);
    }
    /**
     * Returns the node that contains the specified record.
     * @param recNum the record number to search for
     * @return the bufferNode the record is in, or null if it isn't found
     */
    private BufferNode bufferContains(long recNum) {
        for(BufferNode bNode : bufferList) {
            if(bNode.getBlockID() <= recNum*4 && bNode.getBlockID() + BUFFER_SIZE > recNum*4) {
                cacheHits++;
                return bNode;
            }
        }
        cacheMisses++;
        return null;
    }

    // ----------------------------------------------------------
    /**
     * Get the key short value from a buffer
     * @param bufferNum
     * @param recNum
     * @return
     */
    public short getKey(BufferNode pNode, long recNum)
    {
        //pos is the position in the buffer that contains the record, so
        //will at most be 4095.
        long pos = recNum * 4 - pNode.getBlockID();
        return pNode.getBuffer().getShort((int)pos);
    }

    private BufferNode bufferRead(long recNum) {
        BufferNode currentNode;


        // our bufferPool is full, so overwrite an existing buffer
        if(bufferList.size() >= numBuffers) {
            currentNode= bufferList.getLast();
            //if a buffer was changed, we need to write it back to the file
            //before overwriting the buffer
            if(currentNode.isChanged())
                writeToFile(currentNode);

            bufferList.removeLast();
        }
        currentNode = new BufferNode();


        long startReadingPosition = ((recNum * 4 )/ 4096); //truncate
        startReadingPosition *= 4096;

        currentNode.setBlockID(startReadingPosition);

        try
        {
            myFile.seek(startReadingPosition);
            myFile.read(currentNode.getBuffer().getData());
            diskReads++;
        }
        catch (IOException e)
        {
            System.out.println("Could not read file into buffer");
            e.printStackTrace();
        }

        //move currentNode from the last position to the first position
        //since the most recently changed node is always at the front
        //of the linked list

        bufferList.addFirst(currentNode);
        bufferList.indexOf(currentNode);
        return currentNode;
    }

    private void writeToFile(BufferNode writeNode) {
        try
        {
            myFile.seek(writeNode.getBlockID());
            myFile.write(writeNode.getBuffer().getData());
            diskWrites++;
        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }

    public void setRecord(long recordNum, byte[] data)
    {
        BufferNode node = bufferContains(recordNum);
        if (node == null)
            node = bufferRead(recordNum);
        node.getBuffer().setRecord((int)(recordNum * 4 - node.getBlockID()), data);
        node.setChanged(true);
    }

    public byte[] getRecord(long recordNum)
    {
        BufferNode node = bufferContains(recordNum);
        if (node == null)
            node = bufferRead(recordNum);
        return node.getBuffer().getRecord((int)(recordNum * 4 - node.getBlockID()));
    }

    public void flush()
    {
        for(BufferNode bNode : bufferList) {
            if(bNode.isChanged())
            {
                writeToFile(bNode);
                bNode.setChanged(false);
            }
        }
    }

    public void print()
    {
        try
        {
            int count = 0;
            for(long i=0; i*4<myFile.length() - 4095; i+=1024)
            {
                byte[] record = getRecord(i);
                count++;
                System.out.print(makeShort(record[0],record[1]) + "\t" +
                    makeShort(record[2], record[3]) + "\t");
                if(count%8 == 0)
                    System.out.print("\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("File Read Error");
            e.printStackTrace();
        }
    }

    public short makeShort(byte one, byte two)
    {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(one);
        bb.put(two);
        short shortVal = bb.getShort(0);
        return shortVal;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }
    public int getCacheHits() {
        return cacheHits;
    }

    public int getDiskReads() {
        return diskReads;
    }
    public int getDiskWrites() {
        return diskWrites;
    }

}
