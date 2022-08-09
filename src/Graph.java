import java.util.Arrays;

public class Graph {
    private int countNodes;
    private int countEdges;
    private int[][] adjMatrix;

    public Graph(int countNodes) {
        this.countNodes = countNodes;
        this.adjMatrix = new int[countNodes][countNodes];
    }

    public void addEdge(int source, int sink, int weight) {
        if (source < 0 || source > this.countNodes - 1 || sink < 0 || sink > this.countNodes - 1 || weight <= 0) {
            System.err.println("Invalid value for source, sink or weight");
            return;
        }
        this.adjMatrix[source][sink] = weight;
        this.countEdges++;
    }

    public int degree(int node) {
        //returns th degree of a node
        int degrre = 0;
        if (node < 0 || node > this.countNodes - 1) {
            System.err.println("Invalid value for node");
        } else {
            for (int i = 0; i < this.adjMatrix[node].length; i++) {
                if (this.adjMatrix[node][i] != 0) {
                    degrre++;
                }
            }
        }
        return degrre;
    }

    public int getCountNodes() {
        return countNodes;
    }

    public int getCountEdges() {
        return countEdges;
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < this.adjMatrix[i].length; j++) {
                str += this.adjMatrix[i][j] + "\t";
            }
            str += "\n";
        }
        return str;
    }
}
