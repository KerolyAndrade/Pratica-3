import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LabirintoGUI extends JFrame {
    private static final int LINHAS = 12;
    private static final int COLUNAS = 7;
    private JButton[][] celulas = new JButton[LINHAS][COLUNAS];
    private int[][] labirinto = new int[LINHAS][COLUNAS];

    public LabirintoGUI() {
        setTitle("Labirinto - BFS & DFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(LINHAS, COLUNAS));
        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                JButton btn = new JButton();
                btn.setBackground(Color.WHITE);
                int finalI = i;
                int finalJ = j;
                btn.addActionListener(e -> {
                    if ((finalI == 0 && finalJ == 0) || (finalI == 11 && finalJ == 6)) return;
                    if (labirinto[finalI][finalJ] == 0) {
                        labirinto[finalI][finalJ] = 1;
                        btn.setBackground(Color.BLACK);
                    } else {
                        labirinto[finalI][finalJ] = 0;
                        btn.setBackground(Color.WHITE);
                    }
                });
                celulas[i][j] = btn;
                gridPanel.add(btn);
            }
        }

        JPanel controls = new JPanel();
        JButton bfsBtn = new JButton("Buscar em Largura (BFS)");
        JButton dfsBtn = new JButton("Buscar em Profundidade (DFS)");
        JButton resetBtn = new JButton("Reiniciar");

        bfsBtn.addActionListener(e -> buscar(true));
        dfsBtn.addActionListener(e -> buscar(false));
        resetBtn.addActionListener(e -> resetar());

        controls.add(bfsBtn);
        controls.add(dfsBtn);
        controls.add(resetBtn);

        add(gridPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        resetar();
        setVisible(true);
    }

    private void resetar() {
        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                labirinto[i][j] = 0;
                celulas[i][j].setBackground(Color.WHITE);
            }
        }
        labirinto[0][0] = 1;
        labirinto[11][6] = 1;
        celulas[0][0].setBackground(Color.BLACK);
        celulas[11][6].setBackground(Color.BLACK);
    }

    private void buscar(boolean usarBFS) {
        resetCaminhoVisual();
        List<Point> caminho = usarBFS
                ? BuscaLargura.buscar(labirinto, 0, 0, 11, 6)
                : BuscaProfundidade.buscar(labirinto, 0, 0, 11, 6);

        if (caminho == null) {
            JOptionPane.showMessageDialog(this, "Caminho não encontrado.");
        } else {
            for (Point p : caminho) {
                if ((p.x == 0 && p.y == 0) || (p.x == 11 && p.y == 6)) continue;
                celulas[p.x][p.y].setBackground(Color.YELLOW);
            }
        }
    }

    private void resetCaminhoVisual() {
        for (int i = 0; i < LINHAS; i++) {
            for (int j = 0; j < COLUNAS; j++) {
                celulas[i][j].setBackground(labirinto[i][j] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
        celulas[0][0].setBackground(Color.BLACK);
        celulas[11][6].setBackground(Color.BLACK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LabirintoGUI::new);
    }
}