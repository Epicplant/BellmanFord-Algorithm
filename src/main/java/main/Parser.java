package main;

import cse332.exceptions.NotYetImplementedException;

import java.util.LinkedList;
import java.util.List;


public class Parser {

    public static class tuple {

        public int weight;
        public int vertexID;

        public tuple(int w, int v) {
            weight = w;
            vertexID = v;
        }
    }


    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list
     */
    public static List<Parser.tuple>[] parse(int[][] adjMatrix) {

        List<tuple>[] adjacency = new List[adjMatrix.length];
        for(int i = 0; i < adjacency.length; i++) {
            adjacency[i] = new LinkedList<tuple>();
        }

        for(int i = 0; i < adjMatrix.length; i++) {
            for(int j = 0; j < adjMatrix.length; j++) {
                if(adjMatrix[j][i] < Integer.MAX_VALUE) {
                    adjacency[j].add(new tuple(adjMatrix[j][i], i));
                }
            }
        }

        return adjacency;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list with incoming edges
     */
    public static List<Parser.tuple>[] parseInverse(int[][] adjMatrix) {

        List<tuple>[] adjacency = new List[adjMatrix.length];
        for(int i = 0; i < adjacency.length; i++) {
            adjacency[i] = new LinkedList<tuple>();
        }

        for(int i = 0; i < adjMatrix.length; i++) {
            for(int j = 0; j < adjMatrix.length; j++) {
                int val = adjMatrix[j][i];
                if(adjMatrix[j][i] < Integer.MAX_VALUE) {
                    adjacency[i].add(new tuple(adjMatrix[j][i], j));
                }
            }
        }

        return adjacency;
    }

}
