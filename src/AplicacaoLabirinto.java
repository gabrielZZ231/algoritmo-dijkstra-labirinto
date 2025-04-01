import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AplicacaoLabirinto extends JFrame {
    private static final int TAMANHO_CELULA = 30;
    private static final int LARGURA_LABIRINTO = 15;
    private static final int ALTURA_LABIRINTO = 15;
    private static final int ATRASO = 100;
    private GeradorLabirinto gerador;
    private ResolvedorDijkstra resolvedor;
    private VisualizadorLabirinto visualizador;
    private Timer temporizador;
    private enum Estado {GERANDO, RESOLVENDO, FINALIZADO}
    private Estado estadoAtual = Estado.GERANDO;
    private int inicioX = 1;
    private int inicioY = 1;
    private int fimX = LARGURA_LABIRINTO - 2;
    private int fimY = ALTURA_LABIRINTO - 2;

    public AplicacaoLabirinto() {
        setTitle("Gerador e Resolvedor de Labirintos (Dijkstra)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        visualizador = new VisualizadorLabirinto(TAMANHO_CELULA);
        add(visualizador, BorderLayout.CENTER);
        gerador = new GeradorLabirinto(LARGURA_LABIRINTO, ALTURA_LABIRINTO, inicioX, inicioY);
        visualizador.atualizarLabirinto(gerador.obterLabirinto());
        JPanel painelControles = new JPanel();
        JButton botaoIniciar = new JButton("Iniciar");
        JButton botaoReiniciar = new JButton("Reiniciar");
        painelControles.add(botaoIniciar);
        painelControles.add(botaoReiniciar);
        add(painelControles, BorderLayout.SOUTH);

        botaoIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (temporizador != null && !temporizador.isRunning()) {
                    temporizador.start();
                }
            }
        });

        botaoReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarAplicacao();
            }
        });

        temporizador = new Timer(ATRASO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarLabirinto();
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        temporizador.start();
    }

    private void atualizarLabirinto() {
        switch (estadoAtual) {
            case GERANDO:
                if (!gerador.executarPasso()) {
                    estadoAtual = Estado.RESOLVENDO;
                    resolvedor = new ResolvedorDijkstra(gerador.obterLabirinto(), inicioX, inicioY, fimX, fimY);
                    System.out.println("Geração concluída! Iniciando resolução...");
                }
                visualizador.atualizarLabirinto(gerador.obterLabirinto());
                break;

            case RESOLVENDO:
                if (!resolvedor.executarPasso()) {
                    estadoAtual = Estado.FINALIZADO;
                    System.out.println("Labirinto resolvido com Dijkstra!");
                }
                visualizador.atualizarLabirinto(resolvedor.obterLabirintoResolvido());
                break;

            case FINALIZADO:
                temporizador.stop();
                break;
        }
    }

    private void reiniciarAplicacao() {
        temporizador.stop();
        estadoAtual = Estado.GERANDO;
        gerador = new GeradorLabirinto(LARGURA_LABIRINTO, ALTURA_LABIRINTO, inicioX, inicioY);
        visualizador.atualizarLabirinto(gerador.obterLabirinto());
        temporizador.start();
    }
}
