/**
 * A data structure for a max heap, a tree structure where each child is less
 * than its parent.  Given a set of data, the max heap can heapify the contents,
 * then sort it by continually removing the root.  The max heap accesses the
 * data through a BufferPool.
 *  @author Joshua Rush (jdrush89)
 *  @author Benjamin Roble (broble)
 *  @version Nov 2, 2011
 */
public class MaxHeap {
    private long n;       // # of things in heap
    private long size;    // the max size of the heap
    private BufferPool buffPool;    //the BufferPool the heap communicates with
    /**
     * Create a MaxHeap, setting the bufferPool to communicate with, and the
     * size of the heap.
     * @param buffPool the bufferPool this heap will communicate with
     * @param size the number of elements that will be in the heap
     */
    public MaxHeap(BufferPool buffPool, long size) {
        this.buffPool = buffPool;
        n = size;
        this.size = size;
        buildheap();
    }
    /**
     * Return the size of the heap.
     * @return the number of elements in the heap
     */
    public long heapsize() {
        return n;
    }
    /**
     * Return whether or not the element at the given position is a leaf.
     * @param i the element to check
     * @return whether the element is a leaf
     */
    public boolean isLeaf(long i) {
        return (i >= n/2) && (i < n);
    }
    /**
     * Return the position of element's left child
     * @param i the position of the element whose left child is to be returned
     * @return the position of the left child
     * @precondition the element has a left child
     */
    public long leftchild(long i) {
        assert i < n/2 : "Position has no left child";
        return 2*i + 1;
    }
    /**
     * Return the position of element's right child
     * @param pos the position of the element whose right child is to be returned
     * @return the position of the right child
     * @precondition the element has a right child
     */
    public long rightchild(long pos) {
        assert pos < (n-1)/2 : "Position has no right child";
        return 2*pos + 2;
    }
    /**
     * Return the position of element's parent
     * @param pos the position of the element whose parent is to be returned
     * @return the position of the parent
     * @precondition the element has a parent
     */
    public long parent(long pos) {
        assert pos > 0 : "Position has no parent";
        return (pos-1)/2;
    }
    /**
     * Heapify the contents of the heap by calling siftdown on all internal
     * nodes.
     */
    public void buildheap()
    {
        for (long i=n/2-1; i>=0; i--)
        {
            siftdown(i);
        }
    }

    /**
     * Sift an element down, swapping it with its largest child until it is
     * larger than both its children.
     * @param i the position of the element to sift down
     */
    private void siftdown(long i) {
        assert (i >= 0) && (i < n) :
            "Illegal heap position";
        while (!isLeaf(i)) {
            long j = leftchild(i);
            if ((j<(n-1)) &&
                (buffPool.requestKey(j) < buffPool.requestKey(j + 1)))
                j++; // index of child w/ greater value
            if (buffPool.requestKey(i) >= (buffPool.requestKey(j)))
                return;
            swap(i, j);
            i = j;  // Move down
        }
    }
    /**
     * Swap two elements in the MaxHeap.
     * @param recNum1 the position of the first element to swap
     * @param recNum2 the position of the second element to swap
     */
    private void swap(long recNum1, long recNum2)
    {
        byte[] temp = buffPool.getRecord(recNum1);
        buffPool.setRecord(recNum1, buffPool.getRecord(recNum2));
        buffPool.setRecord(recNum2, temp);
    }
    /**
     * Remove the max element from the heap, swapping it with the last element,
     * and moving the size marker back one.
     * @precondition the heap isn't empty
     * @return the record removed, stored in a byte array
     */
    public byte[] removemax() {
        assert n > 0 : "Removing from empty heap";
        swap(0, --n);
        if (n != 0) siftdown(0);
        return buffPool.getRecord(n);
    }
    /**
     * Insert a record into the heap.  The record will start at the bottome and
     * sift its way up to the correct position.
     * @param val the record to be inserted into the heap
     * @precondition the heap isn't full
     */
    public void insert(byte[] val) {
        assert n < size : "Heap is full";
        long curr = n++;
        buffPool.setRecord(curr, val);
        // Siftup until curr parent's key > curr key
        while ((curr != 0)  &&
            (buffPool.requestKey(curr) > (buffPool.requestKey(parent(curr)))
                )) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }

    /**
     * Sort the elements in the heap by removing the max once per element.
     */
    public void heapsort() {
      for (int i=0; i<size; i++)  // Now sort
          this.removemax(); // Put max at end of heap
    }


}

