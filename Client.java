import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * // -------------------------------------------------------------------------
/**
 *  Parses the data file location, number of buffers, and stat file location,
 *  then passes the data file location and number of buffers a new BufferPool.
 *  Also initiates the heapsort on the BufferPool and records how long the
 *  sorting took in the stat file. The client then writes the stats from
 *  the BufferPool to the stat file.
 *
 *  @author Josh Rush, Ben Roble
 *  @version Oct 23, 2011
 */


public class Client
{
    private BufferPool buffPool;
    public Client(String fileName, String numBuffs, String pStatFile)
    {
        File heapFile = new File(fileName);
        buffPool = new BufferPool(Integer.parseInt(numBuffs), heapFile);
        File statFile = new File(pStatFile);
        MaxHeap heapSorter = new MaxHeap(buffPool, heapFile.length() / 4);
        long startTime = System.currentTimeMillis();
        //heapSorter.heapsort();
        long timeElapsed = System.currentTimeMillis() - startTime;

        writeStats(statFile, timeElapsed);

    }

    private void writeStats(File statFile, long timeElapsed)
    {
        try{
            // Create file
            FileWriter fstream = new FileWriter(statFile);
            BufferedWriter out = new BufferedWriter(fstream);
            out.append("ALL THE CRAP IN BUFFERPOOL");
            out.append("\nHeapsort completed in "+timeElapsed+" ms.");

            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
    }
}
