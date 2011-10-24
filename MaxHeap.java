
public class MaxHeap {
    private long n;       // # of things in heap
    private BufferPool buffPool;

    public MaxHeap(BufferPool buffPool, long size) {
        this.buffPool = buffPool;
        n = size;
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
                (buffPool.getKey(j).compareTo(Heap[j+1]) < 0))
                j++; // index of child w/ greater value
            if (Heap[i].compareTo(Heap[j]) >= 0)
                return;
            DSutil.swap(Heap, i, j);
            i = j;  // Move down
        }
    }

    public E removemax() {
        assert n > 0 : "Removing from empty heap";
        DSutil.swap(Heap, 0, --n);
        if (n != 0) siftdown(0);
        return Heap[n];
    }

    public void insert(E val) {
        assert n < size : "Heap is full";
        int curr = n++;
        Heap[curr] = val;
        // Siftup until curr parent's key > curr key
        while ((curr != 0)  &&
            (Heap[curr].compareTo(Heap[parent(curr)])
                > 0)) {
            DSutil.swap(Heap, curr, parent(curr));
            curr = parent(curr);
        }
    }

    static <E extends Comparable<? super E>>
    void heapsort(E[] A) { // Heapsort
      MaxHeap<E> H = new MaxHeap<E>(A, A.length,
                                       A.length);
      for (int i=0; i<A.length; i++)  // Now sort
        H.removemax(); // Put max at end of heap
    }


}

