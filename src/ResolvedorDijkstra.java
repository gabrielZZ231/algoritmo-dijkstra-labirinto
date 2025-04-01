import java.util.*;

public class ResolvedorDijkstra {
    private char[][] labirinto;
    private boolean[][] visitado;
    private PriorityQueue<No> filaPrioridade;
    private Map<Ponto, No> mapaNos;

    private static final char INICIO = 'S';
    private static final char FIM = 'E';
    private static final char CAMINHO = ' ';
    private static final char SOLUCAO = '.';

    private int inicioX, inicioY, fimX, fimY;
    private boolean caminhoEncontrado = false;

    public ResolvedorDijkstra(char[][] labirinto, int inicioX, int inicioY, int fimX, int fimY) {
        this.labirinto = labirinto;
        this.inicioX = inicioX;
        this.inicioY = inicioY;
        this.fimX = fimX;
        this.fimY = fimY;
        this.visitado = new boolean[labirinto.length][labirinto[0].length];
        labirinto[inicioY][inicioX] = INICIO;
        labirinto[fimY][fimX] = FIM;
        filaPrioridade = new PriorityQueue<>(Comparator.comparingInt(no -> no.distancia));
        mapaNos = new HashMap<>();
        No noInicial = new No(inicioX, inicioY, null, 0);
        filaPrioridade.add(noInicial);
        mapaNos.put(new Ponto(inicioX, inicioY), noInicial);
    }

    public boolean executarPasso() {
        if (caminhoEncontrado || filaPrioridade.isEmpty()) {
            return false;
        }

        No atual = filaPrioridade.poll();
        int x = atual.x;
        int y = atual.y;

        if (visitado[y][x]) {
            return true;
        }

        visitado[y][x] = true;

        if (x == fimX && y == fimY) {
            reconstruirCaminho(atual);
            filaPrioridade.clear();
            caminhoEncontrado = true;
            return false;
        }

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < labirinto[0].length && ny >= 0 && ny < labirinto.length &&
                    (labirinto[ny][nx] == CAMINHO || labirinto[ny][nx] == FIM) &&
                    !visitado[ny][nx]) {

                int novaDistancia = atual.distancia + 1;

                Ponto novoPonto = new Ponto(nx, ny);
                No noExistente = mapaNos.get(novoPonto);

                if (noExistente == null || novaDistancia < noExistente.distancia) {
                    No novoNo = new No(nx, ny, atual, novaDistancia);
                    filaPrioridade.add(novoNo);
                    mapaNos.put(novoPonto, novoNo);
                }
            }
        }

        return true;
    }

    private void reconstruirCaminho(No noFinal) {
        No atual = noFinal;

        for (int y = 0; y < labirinto.length; y++) {
            for (int x = 0; x < labirinto[0].length; x++) {
                if (labirinto[y][x] == 'v' || labirinto[y][x] == CAMINHO) {
                    labirinto[y][x] = ' ';
                }
            }
        }

        while (atual != null) {
            int x = atual.x;
            int y = atual.y;
            if (labirinto[y][x] != INICIO && labirinto[y][x] != FIM) {
                labirinto[y][x] = SOLUCAO;
            }
            atual = atual.pai;
        }
    }

    public char[][] obterLabirintoResolvido() {
        return labirinto;
    }
}
