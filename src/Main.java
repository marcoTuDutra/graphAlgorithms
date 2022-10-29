import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int opc = 0;
        Scanner sc = new Scanner(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Informe a tarefa: ");
            System.out.println("1 - Caminho Minimo ");
            System.out.println("2 - Labirinto ");
            System.out.println("3 - Sair ");
            opc = sc.nextInt();

            switch (opc) {
                case 1:
                    System.out.print("Arquivo: ");
                    String arq = in.readLine();
                    System.out.print("Origem: ");
                    int S = sc.nextInt();
                    System.out.print("Destino: ");
                    int E = sc.nextInt();

                    GraphList g = new GraphList(arq);
                    long tempoInicial = System.currentTimeMillis();
                    System.out.println("Processando...");
                    g.bellmanFord(S, E);
                    long tempoFinal = System.currentTimeMillis();
                    System.out.println("Tempo: " + (tempoFinal - tempoInicial));
                    System.out.println("<<<>>>");
                    break;
                case 2:
                    System.out.print("Arquivo: ");
                    arq = in.readLine();
                    tempoInicial = System.currentTimeMillis();
                    GraphMatrix.mazeGenerator(arq);
                    tempoFinal = System.currentTimeMillis();
                    System.out.println("Tempo: " + (tempoFinal - tempoInicial));
                    System.out.println("<<<>>>");
                    break;
            }
        } while (opc != 3);
    }
}
