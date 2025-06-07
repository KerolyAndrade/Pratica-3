import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class SortLibTest {

    private static final int[] TAMANHOS = {5000, 50000, 500000};
    private static final String VERDE = "\u001B[32m";
    private static final String VERMELHO = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static final Map<String, List<Long>> tempos = new LinkedHashMap<>();

    public static void main(String[] args) {
        for (int tamanho : TAMANHOS) {
            System.out.println("\n===== Testando com " + tamanho + " elementos =====");

            Nota[] aleatorios = gerarNotasAleatorias(tamanho);
            Nota[] ordenados = copiarArray(aleatorios);
            Arrays.sort(ordenados, Comparator.comparingInt(Nota::getValor));
            Nota[] inverso = copiarArray(ordenados);
            inverterArray(inverso);
            Nota[] repetidos = gerarNotasComRepeticoes(tamanho);
            Nota[] iguais = gerarNotasIguais(tamanho, 100);

            testarCenario("Aleatório", aleatorios);
            testarCenario("Ordenado", ordenados);
            testarCenario("Inverso", inverso);
            testarCenario("Repetidos", repetidos);
            testarCenario("Iguais", iguais);
        }

        imprimirResumo();
    }

    private static void testarCenario(String nome, Nota[] base) {
        System.out.println("\n-- Cenário " + nome + " --");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        testarMetodoParalelo("Insertion Sort", base.clone(), (arr) -> SortLib.insertionSort(arr, arr.length), executor);
        testarMetodoParalelo("Selection Sort", base.clone(), (arr) -> SortLib.selectionSort(arr, arr.length), executor);
        testarMetodoParalelo("Bubble Sort", base.clone(), (arr) -> SortLib.bubbleSort(arr, arr.length), executor);
        testarMetodoParalelo("Merge Sort", base.clone(), (arr) -> SortLib.mergeSort(arr, 0, arr.length - 1), executor);
        testarMetodoParalelo("Quick Sort", base.clone(), (arr) -> SortLib.quickSort(arr, 0, arr.length - 1), executor);
        testarMetodoParalelo("Heap Sort", base.clone(), (arr) -> SortLib.heapSort(arr, arr.length), executor);
        testarMetodoParalelo("Counting Sort", base.clone(), (arr) -> SortLib.countingSort(arr, arr.length), executor);
        testarMetodoParalelo("Shell Sort", base.clone(), (arr) -> SortLib.shellSort(arr, arr.length), executor);
        testarMetodoParalelo("Radix Sort", base.clone(), (arr) -> SortLib.radixSort(arr, arr.length), executor);

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Erro ao aguardar finalização dos testes.");
        }
    }

    private static void testarMetodoParalelo(String nome, Nota[] notas, Consumer<Nota[]> metodo, ExecutorService executor) {
        executor.submit(() -> testarMetodo(nome, notas, metodo));
    }

    private static void testarMetodo(String nome, Nota[] notas, Consumer<Nota[]> metodo) {
        long inicio = System.currentTimeMillis();
        try {
            metodo.accept(notas);
        } catch (Exception e) {
            System.out.println(VERMELHO + nome + ": EXCEÇÃO - " + e.getMessage() + RESET);
            return;
        }
        long fim = System.currentTimeMillis();

        boolean ok = estaOrdenado(notas);
        String status = ok ? VERDE + "OK" + RESET : VERMELHO + "ERRO" + RESET;
        long tempo = fim - inicio;

        synchronized (tempos) {
            tempos.computeIfAbsent(nome, k -> new ArrayList<>()).add(tempo);
        }

        System.out.printf("%-15s: %s - Tempo = %d ms\n", nome, status, tempo);
    }

    private static void imprimirResumo() {
        System.out.println("\n===== RESUMO FINAL =====");
        for (Map.Entry<String, List<Long>> entry : tempos.entrySet()) {
            String nome = entry.getKey();
            List<Long> valores = entry.getValue();
            long media = (long) valores.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.printf("%-15s → Média: %d ms em %d execuções\n", nome, media, valores.size());
        }
    }

    // Geração de dados
    private static Nota[] gerarNotasAleatorias(int tamanho) {
        Nota[] notas = new Nota[tamanho];
        Random rand = new Random();
        for (int i = 0; i < tamanho; i++) {
            notas[i] = new Nota("Aluno" + i, rand.nextInt(1_000_000));
        }
        return notas;
    }

    private static Nota[] gerarNotasComRepeticoes(int tamanho) {
        Nota[] notas = new Nota[tamanho];
        Random rand = new Random();
        int[] pool = {10, 20, 30, 40, 50};
        for (int i = 0; i < tamanho; i++) {
            notas[i] = new Nota("Aluno" + i, pool[rand.nextInt(pool.length)]);
        }
        return notas;
    }

    private static Nota[] gerarNotasIguais(int tamanho, int valor) {
        Nota[] notas = new Nota[tamanho];
        for (int i = 0; i < tamanho; i++) {
            notas[i] = new Nota("Aluno" + i, valor);
        }
        return notas;
    }

    private static Nota[] copiarArray(Nota[] original) {
        return original.clone();
    }

    private static void inverterArray(Nota[] notas) {
        for (int i = 0, j = notas.length - 1; i < j; i++, j--) {
            Nota temp = notas[i];
            notas[i] = notas[j];
            notas[j] = temp;
        }
    }

    private static boolean estaOrdenado(Nota[] notas) {
        for (int i = 1; i < notas.length; i++) {
            if (notas[i].getValor() < notas[i - 1].getValor()) {
                return false;
            }
        }
        return true;
    }
}
