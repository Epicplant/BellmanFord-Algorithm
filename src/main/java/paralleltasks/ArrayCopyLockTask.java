package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import solvers.OutParallelLock;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ArrayCopyLockTask extends RecursiveAction {


    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    public static OutParallelLock.Value[]  copy(OutParallelLock.Value[] src) {
        OutParallelLock.Value[]  dst = new OutParallelLock.Value[src.length];
        pool.invoke(new ArrayCopyLockTask(src, dst, 0, src.length));
        return dst;
    }


    private final OutParallelLock.Value[] src, dst;
    private final int lo, hi;

    public ArrayCopyLockTask(OutParallelLock.Value[] src, OutParallelLock.Value[] dst, int lo, int hi) {
        this.src = src;
        this.dst = dst;
        this.lo = lo;
        this.hi = hi;
    }

    @SuppressWarnings("ManualArrayCopy")
    protected void compute() {

        if(hi - lo <= CUTOFF) {
            for(int i = lo; i < hi; i++) {
                dst[i] = new OutParallelLock.Value(src[i].val);
            }
            return;
        }

        int mid = lo + (hi-lo)/2;

        ArrayCopyLockTask left = new ArrayCopyLockTask(src, dst, lo, mid);
        ArrayCopyLockTask right = new ArrayCopyLockTask(src, dst, mid, hi);

        left.fork();
        right.compute();
        left.join();
    }

}
