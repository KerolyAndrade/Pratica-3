import java.awt.*;
import java.util.*;

public class BuscaProfundidade {
    public static List<Point> buscar(int[][] labirinto, int startX, int startY, int endX, int endY) {
        int linhas = labirinto.length;
        int colunas = labirinto[0].length;
        boolean[][] visitado = new boolean[linhas][colunas];
        Point[][] anterior = new Point[linhas][colunas];

        Stack<Point> pilha = new Stack<>();
        pilha.push(new Point(startX, startY));
        visitado[startX][startY] = true;

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!pilha.isEmpty()) {
            Point atual = pilha.pop();
            if (atual.x == endX && atual.y == endY) break;

            for (int k = 0; k < 4; k++) {
                int nx = atual.x + dx[k];
                int ny = atual.y + dy[k];

                if (nx >= 0 && ny >= 0 && nx < linhas && ny < colunas
                        && labirinto[nx][ny] == 1 && !visitado[nx][ny]) {
                    pilha.push(new Point(nx, ny));
                    visitado[nx][ny] = true;
                    anterior[nx][ny] = atual;
                }
            }
        }

        if (!visitado[endX][endY]) return null;

        List<Point> caminho = new ArrayList<>();
        for (Point p = new Point(endX, endY); p != null; p = anterior[p.x][p.y]) {
            caminho.add(p);
        }
        Collections.reverse(caminho);
        return caminho;
    }
}