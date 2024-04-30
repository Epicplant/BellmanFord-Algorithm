package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyLockTask;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

public class OutParallelLock implements BellmanFordSolver {

    public static final ForkJoinPool pool = new ForkJoinPool();

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Parser.tuple>[] g = Parser.parse(adjMatrix);


        Value[] distOne = new Value[adjMatrix.length];
        Value[] distTwo = new Value[adjMatrix.length];
        int[] pred = new int[adjMatrix.length];

        for(int i = 0; i < adjMatrix.length; i++) {
            distOne[i] = new Value(Integer.MAX_VALUE);
            distTwo[i] = new Value(Integer.MAX_VALUE);
            pred[i] = -1;
        }
        distOne[source].val = 0;
        distTwo[source].val = 0;



        for(int i = 0; i < adjMatrix.length; i++) {
            distTwo = ArrayCopyLockTask.copy(distOne);
            RelaxOutTaskLock.parallel( g, distOne, distTwo, pred);
        }

        List<Integer> temp = GraphUtil.getCycle(pred);
        return temp;



    }

    public static class Value {

        public int val;
        public ReentrantLock lock;

        public Value(int v) {
                lock = new ReentrantLock();
                val = v;
        }
    }

}