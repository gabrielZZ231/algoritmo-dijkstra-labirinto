import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class LabirintoDijkstra extends JFrame {
    private static final int TAMANHO_CELULA = 20;
    private static final int LARGURA_LABIRINTO = 31;
    private static final int ALTURA_LABIRINTO = 31;
    private static final int ATRASO = 50;
    private char[][] labirinto;
    private boolean[][] visitado;
    private JPanel painelLabirinto;
    private Timer temporizador;
    private PriorityQueue<No> filaDijkstra;
    private Map<Ponto, No> mapaNos;
    private List<No> caminhoMaisCurto;
    private static final char PAREDE = '#';
    private static final char CAMINHO = ' ';
    private static final char INICIO = 'S';
    private static final char FIM = 'E';
    private static final char SOLUCAO = '.';
    private static final char VISITADO = 'v';
    private int inicioX = 1;
    private int inicioY = 1;
    private int fimX = LARGURA_LABIRINTO - 2;
    private int fimY = ALTURA_LABIRINTO - 2;

    public LabirintoDijkstra() {
        setTitle("Resolvedor de Labirintos (Dijkstra)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        labirinto = new char[ALTURA_LABIRINTO][LARGURA_LABIRINTO];
        visitado = new boolean[ALTURA_LABIRINTO][LARGURA_LABIRINTO];
        caminhoMaisCurto = new ArrayList<>();

        for (int y = 0; y < ALTURA_LABIRINTO; y++) {
            for (int x = 0; x < LARGURA_LABIRINTO; x++) {
                labirinto[y][x] = (x % 2 == 1 && y % 2 == 1) ? CAMINHO : PAREDE;
            }
        }

        labirinto[inicioY][inicioX] = INICIO;
        labirinto[fimY][fimX] = FIM;

        painelLabirinto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharLabirinto(g);
            }
        };
        painelLabirinto.setPreferredSize(new Dimension(LARGURA_LABIRINTO * TAMANHO_CELULA, ALTURA_LABIRINTO * TAMANHO_CELULA));
        add(painelLabirinto);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        temporizador = new Timer(ATRASO, e -> {
            atualizarLabirinto();
            imprimirLabirinto();
        });
        temporizador.start();
        mapaNos = new HashMap<>();
        inicializarDijkstra();
    }

    private void desenharLabirinto(Graphics g) {
        for (int y = 0; y < ALTURA_LABIRINTO; y++) {
            for (int x = 0; x < LARGURA_LABIRINTO; x++) {
                switch (labirinto[y][x]) {
                    case PAREDE:
                        g.setColor(Color.BLACK);
                        break;
                    case CAMINHO:
                        g.setColor(Color.WHITE);
                        break;
                    case INICIO:
                        g.setColor(Color.GREEN);
                        break;
                    case FIM:
                        g.setColor(Color.RED);
                        break;
                    case SOLUCAO:
                        g.setColor(Color.BLUE);
                        break;
                    case VISITADO:
                        g.setColor(new Color(255, 200, 200));
                        break;
                }
                g.fillRect(x * TAMANHO_CELULA, y * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
            }
        }
    }

    private void atualizarLabirinto() {
        if (!filaDijkstra.isEmpty()) {
            resolverLabirintoComDijkstraPasso();
        } else {
            temporizador.stop();
            System.out.println("Labirinto resolvido com Dijkstra!");
        }
        painelLabirinto.repaint();
    }

    private void imprimirLabirinto() {
        for (int y = 0; y < ALTURA_LABIRINTO; y++) {
            for (int x = 0; x < LARGURA_LABIRINTO; x++) {
                System.out.print(labirinto[y][x]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void inicializarDijkstra() {
        filaDijkstra = new PriorityQueue<>(Comparator.comparingInt(no -> no.distancia));
        mapaNos = new HashMap<>();
        visitado = new boolean[ALTURA_LABIRINTO][LARGURA_LABIRINTO];

        No noInicio = new No(inicioX, inicioY, null, 0);
        filaDijkstra.add(noInicio);
        mapaNos.put(new Ponto(inicioX, inicioY), noInicio);
    }

    private void resolverLabirintoComDijkstraPasso() {
        if (filaDijkstra.isEmpty()) {
            return;
        }

        No atual = filaDijkstra.poll();
        int x = atual.x;
        int y = atual.y;

        if (visitado[y][x]) {
            return;
        }

        visitado[y][x] = true;

        if (x == fimX && y == fimY) {
            reconstruirCaminho(atual);
            filaDijkstra.clear();
            return;
        }

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < LARGURA_LABIRINTO && ny >= 0 && ny < ALTURA_LABIRINTO &&
                    (labirinto[ny][nx] == CAMINHO || labirinto[ny][nx] == FIM) && !visitado[ny][nx]) {

                int novaDistancia = atual.distancia + 1;

                Ponto novoPonto = new Ponto(nx, ny);
                No noExistente = mapaNos.get(novoPonto);

                if (noExistente == null || novaDistancia < noExistente.distancia) {
                    No novoNo = new No(nx, ny, atual, novaDistancia);
                    filaDijkstra.add(novoNo);
                    mapaNos.put(novoPonto, novoNo);
                }
            }
        }
    }

    private void reconstruirCaminho(No noFim) {
        for (int y = 0; y < ALTURA_LABIRINTO; y++) {
            for (int x = 0; x < LARGURA_LABIRINTO; x++) {
                if (labirinto[y][x] == VISITADO) {
                    labirinto[y][x] = CAMINHO;
                }
            }
        }

        caminhoMaisCurto.clear();
        No atual = noFim;
        while (atual.pai != null) {
            caminhoMaisCurto.add(atual);
            atual = atual.pai;
        }
        caminhoMaisCurto.add(atual);
        Collections.reverse(caminhoMaisCurto);
        marcarCaminho();
    }

    private void marcarCaminho() {
        for (No no : caminhoMaisCurto) {
            if (labirinto[no.y][no.x] != INICIO && labirinto[no.y][no.x] != FIM) {
                labirinto[no.y][no.x] = SOLUCAO;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LabirintoDijkstra::new);
    }
}
