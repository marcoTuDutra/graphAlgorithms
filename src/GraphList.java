import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GraphList {
    private int countNodes;
    private int countEdges;
    private ArrayList<ArrayList<Edge>> adjList;
    private ArrayList<Edge> edgeList;
    private static final int INF = 999999999;

    public GraphList(int countNodes) {
        this.countNodes = countNodes;
        adjList = new ArrayList<>(this.countNodes);
        for (int i = 0; i < this.countNodes; ++i) {
            adjList.add(new ArrayList<Edge>());
        }
        edgeList = new ArrayList<>();
    }

    public GraphList(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Read header
        String[] line = bufferedReader.readLine().split(" ");
        this.countNodes = (Integer.parseInt(line[0]));
        int fileLines = (Integer.parseInt(line[1]));

        // Create and fill adjList with read edges
        adjList = new ArrayList<>(this.countNodes);
        for (int i = 0; i < this.countNodes; ++i) {
            adjList.add(new ArrayList<Edge>());
        }
        edgeList = new ArrayList<>();
        // Adds one edge at time
        for (int i = 0; i < fileLines; ++i) {
            String[] edgeInfo = bufferedReader.readLine().split(" ");
            int source = Integer.parseInt(edgeInfo[0]);
            int sink = Integer.parseInt(edgeInfo[1]);
            int weight = Integer.parseInt(edgeInfo[2]);
            addEdge(source, sink, weight);
        }
        bufferedReader.close();
        reader.close();
    }

    public void addEdge(int source, int sink, int weight) {
        if (source < 0 || source > this.countNodes - 1
                || sink < 0 || sink > this.countNodes - 1 || weight <= 0) {
            System.err.println("Invalid edge: " + source + sink + weight);
            return;
        }
        adjList.get(source).add(new Edge(source, sink, weight));
        edgeList.add(new Edge(source, sink, weight));
        this.countEdges++;
    }

    public void addEdgeUnoriented(int source, int sink, int weight) {
        addEdge(source, sink, weight);
        addEdge(sink, source, weight);
    }

    public int degree(int u) {
        if (u < 0 || u > this.countNodes - 1)
            System.err.println("Invalid node: " + u);
        return this.adjList.get(u).size();
    }

    public int highestDegree() {
        int highest = 0;
        for (int u = 0; u < this.adjList.size(); ++u) {
            int degreeNodeU = this.degree(u);
            if (highest < degreeNodeU)
                highest = degreeNodeU;
        }
        return highest;
    }

    public int lowestDegree() {
        int lowest = this.countNodes;
        for (int u = 0; u < this.adjList.size(); ++u) {
            int degreeNodeU = this.degree(u);
            if (lowest > degreeNodeU)
                lowest = degreeNodeU;
        }
        return lowest;
    }

    public GraphList complement() {
        GraphList g2 = new GraphList(this.countNodes);
        for (int u = 0; u < this.adjList.size(); ++u) {
            for (int v = 0; v < this.countNodes; ++v) {
                boolean addEdgeUV = true;
                for (int idx = 0; idx < this.adjList.get(u).size(); ++idx) {
                    int v2 = this.adjList.get(u).get(idx).getSink();
                    if (v2 == v) { // Edge (u, v) exists and should not be added
                        addEdgeUV = false;
                        break;
                    }
                }
                if (addEdgeUV && u != v) {
                    g2.addEdge(u, v, 1); // It assumes edges are unweighted
                }
            }
        }
        return g2;
    }

    public float density() {
        return (float) this.countEdges / (this.countNodes * (this.countNodes - 1));
    }

    public boolean subgraph(GraphList g2) {
        if (g2.countNodes > this.countNodes || g2.countEdges > this.countEdges)
            return false;
        for (int u = 0; u < g2.adjList.size(); ++u) {
            boolean foundEdge = false;
            for (int idx = 0; idx < g2.adjList.get(u).size(); ++idx) {
                int v = g2.adjList.get(u).get(idx).getSink();
                // Check if edge (u,v) exists in this graph
                for (int idx2 = 0; idx2 < this.adjList.get(u).size(); ++idx2) {
                    int v2 = this.adjList.get(u).get(idx2).getSink();
                    if (v == v2) { // Edge exists
                        foundEdge = true;
                        break;
                    }
                }
                if (!foundEdge)
                    return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> bfs(int s) {
        int[] desc = new int[this.countNodes];

        ArrayList<Integer> Q = new ArrayList<>();
        Q.add(s);

        ArrayList<Integer> R = new ArrayList<>();
        R.add(s);

        desc[s] = 1;

        while (Q.size() > 0) {
            int u = Q.remove(0);
            for (int v = 0; v < this.adjList.get(u).size(); v++) {
                if (desc[this.adjList.get(u).get(v).getSink()] == 0) {
                    Q.add(this.adjList.get(u).get(v).getSink());
                    R.add(this.adjList.get(u).get(v).getSink());
                    desc[this.adjList.get(u).get(v).getSink()] = 1;
                }
            }
        }
        return R;
    }

    public ArrayList<Integer> dfs(int s) {
        int[] desc = new int[this.countNodes];

        ArrayList<Integer> S = new ArrayList<>();
        S.add(s);

        ArrayList<Integer> R = new ArrayList<>();
        R.add(s);

        desc[s] = 1;

        while (S.size() > 0) {
            boolean unstack = true;
            int u = S.get(S.size() - 1);
            for (int v = 0; v < this.adjList.get(u).size(); v++) {
                if (desc[this.adjList.get(u).get(v).getSink()] == 0) {
                    S.add(this.adjList.get(u).get(v).getSink());
                    R.add(this.adjList.get(u).get(v).getSink());
                    desc[this.adjList.get(u).get(v).getSink()] = 1;
                    unstack = false;
                    break;
                }
            }
            if (unstack) {
                S.remove(S.size() - 1);
            }
        }
        return R;
    }

    public boolean connected() {
        return (this.bfs(0).size() == this.countNodes);
    }

    public boolean isOriented() {
        for (int i = 0; i < this.adjList.size(); i++) {
            for (int j = 0; j < this.adjList.get(i).size(); j++) {
                int sink = this.adjList.get(i).get(j).getSink();
                int source = this.adjList.get(i).get(j).getSource();
                boolean hasEdgeSinkSource = false;
                for (int k = 0; k < this.adjList.get(sink).size(); k++) {
                    if (this.adjList.get(sink).get(k).getSink() == source) {
                        hasEdgeSinkSource = true;
                        break;
                    }
                }
                if (!hasEdgeSinkSource) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Integer> dfsRec(int s) {
        int[] desc = new int[this.countNodes];
        ArrayList<Integer> R = new ArrayList<>();
        dfsRecAux(s, desc, R);
        return R;
    }

    public void dfsRecAux(int u, int[] desc, ArrayList<Integer> R) {
        desc[u] = 1;
        R.add(u);
        for (int idx = 0; idx < this.adjList.get(u).size(); ++idx) {
            int v = this.adjList.get(u).get(idx).getSink();
            if (desc[v] == 0) {
                dfsRecAux(v, desc, R);
            }
        }
    }

    public ArrayList<Edge> kruskal() {
        ArrayList<Edge> T = new ArrayList<Edge>(this.countNodes - 1);
        int[] F = new int[this.countNodes];
        // makeset(u)
        for (int u = 0; u < this.countNodes; ++u)
            F[u] = u;
        edgeList.sort(null);
        for (int idx = 0; idx < edgeList.size(); ++idx) {
            int u = edgeList.get(idx).getSource();
            int v = edgeList.get(idx).getSink();
            if (F[u] != F[v]) { // findset(u) != findset(v)
                T.add(edgeList.get(idx));
                // Save some iterations if tree is already built
                if (T.size() == countNodes - 1)
                    break;
                // union(u, v)
                int k = F[v];
                for (int i = 0; i < F.length; ++i) {
                    if (F[i] == k) {
                        F[i] = F[u];
                    }
                }
            }
        }
        return T;
    }

    public ArrayList<Edge> prim() {
        ArrayList<Edge> T = new ArrayList<Edge>(this.countNodes - 1);
        int s = 0;
        int[] dist = new int[this.countNodes];
        int[] parent = new int[this.countNodes];
        // Q represents the nodes that were not connected yet
        ArrayList<Integer> Q = new ArrayList<Integer>(this.countNodes);
        for (int u = 0; u < this.countNodes; ++u) {
            dist[u] = INF;
            parent[u] = -1;
            Q.add(u);
        }
        dist[s] = 0;
        while (Q.size() != 0) {
            int u = -1;
            int min = INF;
            for (int idx = 0; idx < Q.size(); ++idx) {
                int i = Q.get(idx);
                if (dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }
            // Node u is gonna be connected
            Q.remove((Integer) u);
            for (int idx = 0; idx < this.adjList.get(u).size(); ++idx) {
                int v = this.adjList.get(u).get(idx).getSink();
                int w = this.adjList.get(u).get(idx).getWeight();
                if (Q.contains(v) && w < dist[v]) {
                    dist[v] = w;
                    parent[v] = u;
                }
            }
        }
        // Recover the tree from parent array
        for (int u = 0; u < parent.length; ++u) {
            if (parent[u] != -1) {
                T.add(new Edge(u, parent[u], 1));
            }
        }
        return T;
    }

    public void bellmanFord(int s, int t){
        int V = this.countNodes;
        int E = this.countEdges;
        int []dist = new int[this.countNodes];
        int []pred = new int[this.countNodes];
        for (int u = 0; u < V; ++u){
            dist[u] = INF;
            pred[u] = 0;
        }
        dist[s] = 0;
        for(int i = 0; i < V; ++i) {
            for (int j = 0; j < E; ++j) {
                int u = this.edgeList.get(j).getSource();
                int v = this.edgeList.get(j).getSink();
                int w = this.edgeList.get(j).getWeight();
                if (dist[v] > dist[u] + w && dist[u] != Integer.MAX_VALUE) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                }
            }
        }

        ArrayList<Integer> C = new ArrayList<>();
        int aux = t;
        C.add(t);
        while(aux != s){
            aux = pred[aux];
            C.add(0, aux);
        }

        System.out.println("Caminho: "+ C);
        System.out.println("Custo: "+dist[t]);
    }

    void improvedBellmanFord(int s, int t) {
        int V = this.countNodes;
        int E = this.countEdges;
        int dist[] = new int[this.countNodes];
        int pred[] = new int[this.countNodes];
        boolean flag;

        for (int i = 0; i < V; i++) {
            dist[i] = INF;
            pred[i] = 0;
        }
        dist[s] = 0;

        for (int i = 1; i < V; ++i) {
            flag = false;
            for (int j = 0; j < E; ++j) {
                int u = this.edgeList.get(j).getSource();
                int v = this.edgeList.get(j).getSink();
                int w = this.edgeList.get(j).getWeight();
                if (dist[v] > dist[u] + w && dist[u] != Integer.MAX_VALUE) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                    flag = true;
                }
            }
            if (flag = false) {
                break;
            }
        }

        for (int j = 0; j < E; ++j) {
            int u = this.edgeList.get(j).getSource();
            int v = this.edgeList.get(j).getSink();
            int w = this.edgeList.get(j).getWeight();
            if (dist[v] > dist[u] + w && dist[u] != INF) {
                System.out.println("O grafo possui pesos negativos");
                return;
            }
        }

        ArrayList<Integer> C = new ArrayList<>();
        int aux = t;
        C.add(t);
        while (aux != s) {
            aux = pred[aux];
            C.add(0, aux);
        }

        System.out.println("Caminho: "+ C);
        System.out.println("Custo: "+dist[t]);
    }

    private int getLowerDistance(ArrayList<Integer> eddgesList, int[] distancesList) {
        int lowerDistance = INF;
        int edge = 0;

        for (int i = 0; i < eddgesList.size(); i++) {
            if (distancesList[i] < lowerDistance) {
                edge = eddgesList.get(i);
            }
        }

        return edge;
    }

    private ArrayList<Edge> getAdjEdges(int edge) {
        return this.adjList.get(edge);
    }

    public void dijkstra(int s, int t) {
        int[] dist = new int[this.countNodes];
        int[] pred = new int[this.countNodes];

        ArrayList<Integer> Q = new ArrayList<>();
        for (int v = 0; v < this.countNodes; ++v) {
            dist[v] = INF;
            pred[v] = -1;
        }

        dist[s] = 0;

        for (int v = 0; v < this.countNodes; ++v) {
            Q.add(v);
        }

        while (Q.size() != 0) {
            int u = getLowerDistance(Q, dist);
            Q.remove((Integer) u);
            for (Edge edge : getAdjEdges(u)) {
                if (dist[edge.getSink()] > dist[u] + edge.getWeight()) {
                    dist[edge.getSink()] = dist[u] + edge.getWeight();
                    pred[edge.getSink()] = u;
                }
          }
        }

        ArrayList path = new ArrayList<Integer>();

        int aux = t;
        path.add(0, aux);
        while(aux != s){
            aux = pred[aux];
            path.add(0, aux);
        }

        System.out.println("Caminho: "+ path);
        System.out.println("Custo: "+dist[t]);

    }

    public int getCountNodes() {
        return countNodes;
    }

    public void setCountNodes(int countNodes) {
        this.countNodes = countNodes;
    }

    public int getCountEdges() {
        return countEdges;
    }

    public void setCountEdges(int countEdges) {
        this.countEdges = countEdges;
    }

    public ArrayList<ArrayList<Edge>> getAdjList() {
        return adjList;
    }

    public void setAdjList(ArrayList<ArrayList<Edge>> adjList) {
        this.adjList = adjList;
    }

    public String toString() {
        String str = "";
        for (int u = 0; u < this.adjList.size(); ++u) {
            str += u + ": ";
            for (int idx = 0; idx < this.adjList.get(u).size(); ++idx) {
                str += this.adjList.get(u).get(idx) + "\t";
            }
            str += "\n";
        }
        return str;
    }


}