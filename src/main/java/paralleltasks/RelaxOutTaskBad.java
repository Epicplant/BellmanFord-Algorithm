package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import main.Parser;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private final int lo, hi;
    private final int[] distOne, distTwo, pred;
    private final List<Parser.tuple>[] ver;

    public RelaxOutTaskBad(List<Parser.tuple>[] ver, int[] distOne, int[] distTwo, int[] pred, int lo, int hi) {
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
        RelaxOutTaskBad left = new  RelaxOutTaskBad(ver, distOne, distTwo, pred, lo, mid);
        RelaxOutTaskBad right = new  RelaxOutTaskBad(ver, distOne, distTwo, pred, mid, hi);

        left.fork();
        right.compute();
        left.join();

    }

    public static void sequential(List<Parser.tuple>[] ver, int[] distOne, int[] distTwo, int[] pred, int lo, int hi) {


        for (int i = lo; i < hi; i++) {

            for (Parser.tuple current : (List<Parser.tuple>) ver[i]) {

                if (distTwo[i] != Integer.MAX_VALUE && distOne[current.vertexID] > distTwo[i] + current.weight) {
                    distOne[current.vertexID] = distTwo[i] + current.weight;
                    pred[current.vertexID] = i;
                }
            }


        }

    }

    public static void parallel(List<Parser.tuple>[] ver, int[] distOne, int[] distTwo, int[] pred) {
        pool.invoke(new RelaxOutTaskBad(ver, distOne, distTwo, pred, 0, ver.length));
    }

}
