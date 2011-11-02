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
 *  @author Joshua Rush (jdrush89)
 *  @author Benjamin Roble (broble)
 *  @version Nov 2, 2011
 */


public class Client
{

    /**
     * Create a new Client to sort a binary file using a heapsort algorithm.
     * Creates a buffer pool for the heapsorter to use when communicating with
     * the file.  Stores statitics about the sorting in the specified stat file.
     * @param fileName the name of the binary file to sort
     * @param numBuffs the number of buffers to be used in the BufferPool
     * @param pStatFile the name of the file where statistics about the sort
     * will be saved
     */
    public Client(String fileName, String numBuffs, String pStatFile)
    {
        File heapFile = new File(fileName);
        BufferPool buffPool = new BufferPool(Integer.parseInt(numBuffs), heapFile);
        File statFile = new File(pStatFile);
        MaxHeap heapSorter = new MaxHeap(buffPool, heapFile.length() / 4);
        long startTime = System.currentTimeMillis();
        heapSorter.heapsort();
        buffPool.flush();
        long timeElapsed = System.currentTimeMillis() - startTime;

        writeStats(statFile, timeElapsed, fileName, buffPool);
        buffPool.print();
    }
    /**
     * Write the stats from the heapsort to the statFile.
     * @param statFile the name of the file where stats are to be written
     * @param timeElapsed the amount of time it took to sort the file.
     * @param fileName the name of the stat file
     * @param buffPool the bufferPool whose stats are to be printed
     */
    private void writeStats(File statFile, long timeElapsed, String fileName, BufferPool buffPool)
    {
        try{
            // Create file
            FileWriter fstream = new FileWriter(statFile, true);
            BufferedWriter out = new BufferedWriter(fstream);
            //get and print stats
            out.append("Reading data file '"+fileName+"'");
            out.newLine();
            out.append("Cache Misses:"+buffPool.getCacheMisses());
            out.newLine();
            out.append("Cache Hits:"+buffPool.getCacheHits());
            out.newLine();
            out.append("Disk Reads:"+buffPool.getDiskReads());
            out.newLine();
            out.append("Disk Writes: "+buffPool.getDiskWrites());
            out.newLine();
            out.append("Heapsort completed in "+timeElapsed+" ms.");
            out.newLine();

            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

        }
    }
}
