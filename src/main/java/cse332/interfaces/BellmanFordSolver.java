package cse332.interfaces;

import java.util.List;

public interface BellmanFordSolver {

    List<Integer> solve(int[][] adjMatrix, int source);

}
