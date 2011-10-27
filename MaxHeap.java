
public class MaxHeap {
    private long n;       // # of things in heap
    private long size;
    private BufferPool buffPool;

    public MaxHeap(BufferPool buffPool, long size) {
        this.buffPool = buffPool;
        n = size;
        this.size = size;
        buildheap();
    }

    public long heapsize() {
        return n;
    }
    // Is pos a leaf position?
    public boolean isLeaf(long i) {
        return (i >= n/2) && (i < n);
    }
    // Leftchild position
    public long leftchild(long i) {
        assert i < n/2 : "Position has no left child";
        return 2*i + 1;
    }
    // Rightchild position
    public long rightchild(long pos) {
        assert pos < (n-1)/2 : "Position has no right child";
        return 2*pos + 2;
    }
    public long parent(long pos) {
        assert pos > 0 : "Position has no parent";
        return (pos-1)/2;
    }

    public void buildheap() // Heapify contents
    { for (long i=n/2-1; i>=0; i--) siftdown(i); }

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

    private void swap(long recNum1, long recNum2)
    {
        byte[] temp = buffPool.getRecord(recNum1);
        buffPool.setRecord(recNum1, buffPool.getRecord(recNum2));
        buffPool.setRecord(recNum2, temp);
    }

    public byte[] removemax() {
        assert n > 0 : "Removing from empty heap";
        swap(0, --n);
        if (n != 0) siftdown(0);
        return buffPool.getRecord(n);
    }

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


    public void heapsort() {
      for (int i=0; i<size; i++)  // Now sort
          this.removemax(); // Put max at end of heap
    }


}

