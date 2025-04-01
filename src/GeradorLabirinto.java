import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class GeradorLabirinto {
    private char[][] labirinto;
    private Stack<Ponto> pilhaGeracao;
    private final Random aleatorio = new Random();
    private final int[] dx = {0, 2, 0, -2};
    private final int[] dy = {-2, 0, 2, 0};
    private static final char PAREDE = '#';
    private static final char CAMINHO = ' ';

    public GeradorLabirinto(int largura, int altura, int inicioX, int inicioY) {
        labirinto = new char[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                labirinto[y][x] = PAREDE;
            }
        }

        pilhaGeracao = new Stack<>();
        labirinto[inicioY][inicioX] = CAMINHO;
        pilhaGeracao.push(new Ponto(inicioX, inicioY));

        while (!pilhaGeracao.isEmpty()) {
            executarPasso();
        }

        labirinto[inicioY][inicioX] = CAMINHO;
        labirinto[altura - 2][largura - 2] = CAMINHO;
    }

    public boolean executarPasso() {
        if (pilhaGeracao.isEmpty()) {
            return false;
        }

        Ponto atual = pilhaGeracao.peek();
        List<Integer> direcoes = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(direcoes, aleatorio);

        for (int direcao : direcoes) {
            int nx = atual.x + dx[direcao];
            int ny = atual.y + dy[direcao];

            if (nx > 0 && nx < labirinto[0].length - 1 && ny > 0 && ny < labirinto.length - 1 && labirinto[ny][nx] == PAREDE) {
                labirinto[ny][nx] = CAMINHO;
                labirinto[atual.y + dy[direcao] / 2][atual.x + dx[direcao] / 2] = CAMINHO;
                pilhaGeracao.push(new Ponto(nx, ny));
                return true;
            }
        }

        pilhaGeracao.pop();
        return true;
    }

    public char[][] obterLabirinto() {
        return labirinto;
    }
}
