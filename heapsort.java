//Compiled using Eclipse Java compiler on a Windows 7 64-bit machine.
//Date completed: 11/2/11
//Sorts a file containing 2 byte keys and records.  The file is sorted using
//a heapsort algorithm.  The heapsort accesses the file through a buffer pool.
//Statistics about the sort and buffer pool performance are reported in a specified
//stat file.



/**
 * Initializes a client that executes a heapsort algorithm on a binary file.
 * The heapsort interfaces with the file through a buffer pool.  The
 * client then writes stats to a stat file about the bufferpool and
 * sorting performance.
 *
 *  @author Joshua Rush (jdrush89)
 *  @author Benjamin Roble (broble)
 *  @version Nov 2, 2011
 */


// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
//
public class heapsort
{
    /**
     * Initializes the client that will sort a binary file using a heapsort
     * algorithm.  The heapsorter will communicate with the file through a
     * buffer pool.  Statistcs about the sorting will be output to the specified
     * stat file.
     * @param args the string arguments for running the program
     *        args[0] the binary file to sort
     *        args[1] the number of buffers to be used in a buffer pool
     *        args[2] the name of the stat file for outputting statistics
     */
    public static void main(String[] args)
    {
        Client client = new Client(args[0], args[1], args[2]);
    }
}
