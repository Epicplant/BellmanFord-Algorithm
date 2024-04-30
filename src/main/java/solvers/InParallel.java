package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyLockTask;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxInTask;
import paralleltasks.RelaxOutTaskLock;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class InParallel implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Parser.tuple>[] g = Parser.parseInverse(adjMatrix);

        int[] distOne = new int[adjMatrix.length];
        int[] distTwo = new int[adjMatrix.length];
        int[] pred = new int[adjMatrix.length];

        for (int i = 0; i < adjMatrix.length; i++) {
            distOne[i] = Integer.MAX_VALUE;
            distTwo[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        distOne[source] = 0;
        distTwo[source] = 0;


        for (int i = 0; i < adjMatrix.length; i++) {
            distTwo = ArrayCopyTask.copy(distOne);
            RelaxInTask.parallel( g, distOne, distTwo, pred);
        }



        List<Integer> temp = GraphUtil.getCycle(pred);
        return temp;
    }
}