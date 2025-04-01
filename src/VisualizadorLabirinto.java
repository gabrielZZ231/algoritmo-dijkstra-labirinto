import javax.swing.*;
import java.awt.*;

public class VisualizadorLabirinto extends JPanel {
    private char[][] labirinto;
    private final int tamanhoCelula;
    private static final char PAREDE = '#';
    private static final char CAMINHO = ' ';
    private static final char INICIO = 'S';
    private static final char FIM = 'E';
    private static final char SOLUCAO = '.';

    public VisualizadorLabirinto(int tamanhoCelula) {
        this.tamanhoCelula = tamanhoCelula;
        setBackground(Color.LIGHT_GRAY);
    }

    public void atualizarLabirinto(char[][] labirinto) {
        this.labirinto = labirinto;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (labirinto == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int y = 0; y < labirinto.length; y++) {
            for (int x = 0; x < labirinto[0].length; x++) {
                switch (labirinto[y][x]) {
                    case PAREDE:
                        g2d.setColor(Color.BLACK);
                        break;
                    case INICIO:
                        g2d.setColor(new Color(0, 200, 0));
                        break;
                    case FIM:
                        g2d.setColor(new Color(255, 50, 50));
                        break;
                    case SOLUCAO:
                        g2d.setColor(Color.RED);
                        break;
                    default:
                        continue;
                }
                g2d.fillRect(x * tamanhoCelula, y * tamanhoCelula, tamanhoCelula, tamanhoCelula);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (labirinto == null) {
            return new Dimension(400, 400);
        }
        return new Dimension(labirinto[0].length * tamanhoCelula, labirinto.length * tamanhoCelula);
    }
}
