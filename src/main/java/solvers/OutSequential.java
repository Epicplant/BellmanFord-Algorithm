package solvers;

import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;


import java.util.LinkedList;
import java.util.List;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Parser.tuple>[] g = Parser.parse(adjMatrix);


        int[] distOne = new int[adjMatrix.length];
        int[] distTwo = new int[adjMatrix.length];
        int[] pred = new int[adjMatrix.length];

        for(int i = 0; i < adjMatrix.length; i++) {
            distOne[i] = Integer.MAX_VALUE;
            distTwo[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }
        distOne[source] = 0;
        distTwo[source] = 0;

        //loops through vertices
        for(int i = 0; i < adjMatrix.length; i++) {

           for(int j = 0; j < adjMatrix.length; j++) {
               distTwo[j] = distOne[j];
           }

           //loops through edges
            for(int j = 0; j < adjMatrix.length; j++) {

                for (Parser.tuple current :  g[j]) {

                    if (distTwo[j] != Integer.MAX_VALUE && distOne[current.vertexID] > distTwo[j] + current.weight) {

                        distOne[current.vertexID] = distTwo[j] + current.weight;
                        pred[current.vertexID] = j;

                    }
                }
            }
        }
        List<Integer> temp = GraphUtil.getCycle(pred);
        return temp;

    }
}