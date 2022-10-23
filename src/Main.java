import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long tempoInicial = System.currentTimeMillis();
        GraphMatrix.mazeGenerator("maze50.txt");
        long tempoFinal = System.currentTimeMillis();
        System.out.println( tempoFinal - tempoInicial );

    }
}
