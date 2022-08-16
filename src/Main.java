public class Main {
    public static void main(String[] args) {
//        Graph g1 = new Graph(4);
//        g1.addEdge(0, 1, 3);
//        g1.addEdge(1, 0, 3);
//        g1.addEdge(0, 3, 4);
//        g1.addEdge(3, 0, 4);
//        g1.addEdge(3, 4, 2); //aviso
//        System.out.println(g1);
//        System.out.println(g1.degree(0));
//        System.out.println(g1.highestDegree());
//        System.out.println(g1.lowestDegree());
//        System.out.println(g1.complement());

        Graph g1 = new Graph(4);
        Graph g2 = new Graph(3);
        Graph g3 = new Graph(4);

        g1.addEdge(0,1,1);
        g1.addEdge(0,3,1);

        g2.addEdge(0,1,1);

        g3.addEdge(3,1,1);

        System.out.println(g1);
        System.out.println(" ");
        System.out.println(g2);
        System.out.println(" ");
        System.out.println(g3);

        System.out.println(g1.subGraf(g2));



    }
}
