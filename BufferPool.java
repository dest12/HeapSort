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
    //A constant to represent data not being found in a current buffer
    private static final int BUFFER_NOT_FOUND = -1;
    //A constant for the number of bytes a buffer can hold
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
     * Return a key 
     * @param recNum
     * @return
     */
    public short requestKey(long recNum) {
        BufferNode bn = bufferContains(recNum);
        if(bn == null) bn = bufferRead(recNum);

        return getKey(bn, recNum);
    }

    private BufferNode bufferContains(long recNum) {
        for(BufferNode bNode : bufferList) {
            if(bNode.getBlockID() <= recNum && bNode.getBlockID() + BUFFER_SIZE > recNum) {
                return bNode;
            }
        }

        return null;
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
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


        long startReadingPosition = (recNum / 4096);
        startReadingPosition *= 4096;

        currentNode.setBlockID(startReadingPosition);

        try
        {
            myFile.seek(startReadingPosition);
            myFile.read(currentNode.getBuffer().getData());
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
        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }

    public Object getKey(long j)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
