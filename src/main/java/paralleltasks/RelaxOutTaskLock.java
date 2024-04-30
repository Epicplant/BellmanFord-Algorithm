package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import main.Parser;
import solvers.OutParallelLock;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private final int lo, hi;
    private final OutParallelLock.Value[] distOne, distTwo;
    private final int[]  pred;
    private final List<Parser.tuple>[] ver;


    public RelaxOutTaskLock(List<Parser.tuple>[] ver, OutParallelLock.Value[] distOne, OutParallelLock.Value[]
            distTwo, int[] pred, int lo, int hi) {
        this.ver = ver;
        this.distOne = distOne;
        this.distTwo = distTwo;
        this.pred = pred;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {


        if(hi - lo <= CUTOFF) {
            sequential(ver, distOne, distTwo, pred, lo, hi);
            return;
        }

        int mid = lo + (hi-lo)/2;
        RelaxOutTaskLock left = new  RelaxOutTaskLock(ver, distOne, distTwo, pred, lo, mid);
        RelaxOutTaskLock right = new  RelaxOutTaskLock(ver, distOne, distTwo, pred, mid, hi);

        left.fork();
        right.compute();
        left.join();

    }

    public static void sequential(List<Parser.tuple>[] ver, OutParallelLock.Value[] distOne, OutParallelLock.Value[]
            distTwo, int[] pred, int lo, int hi) {

        for(int i = lo; i < hi; i++) {

            for (Parser.tuple current : (List<Parser.tuple>) ver[i]) {

                distOne[current.vertexID].lock.lock();

                try {
                    if (distTwo[i].val != Integer.MAX_VALUE && distOne[current.vertexID].val > distTwo[i].val + current.weight) {

                        distOne[current.vertexID].val = distTwo[i].val + current.weight;
                        pred[current.vertexID] = i;

                    }

                } catch(Exception temp) {
                    distOne[current.vertexID].lock.unlock();
                    throw temp;
                }
                
                distOne[current.vertexID].lock.unlock();

            }
        }
    }

    public static void parallel(List<Parser.tuple>[] arr, OutParallelLock.Value[] distOne, OutParallelLock.Value[] distTwo, int[] pred) {
        pool.invoke(new RelaxOutTaskLock(arr, distOne, distTwo, pred, 0, arr.length));
    }
}