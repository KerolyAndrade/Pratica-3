/**
 * Biblioteca com implementações de algoritmos clássicos de ordenação.
 * Suporta ordenação de objetos do tipo Nota, baseado no campo "valor".
 */
public class SortLib {

    // ===================== MERGE SORT =====================

    /**
     * Ordena um array de Nota usando o algoritmo Merge Sort (divisão e conquista).
     */
    public static void mergeSort(Nota[] notas, int inicial, int termino) {
        int quantidade = termino - inicial + 1;

        if (quantidade > 1) {
            int meio = (inicial + termino) / 2;
            mergeSort(notas, inicial, meio);
            mergeSort(notas, meio + 1, termino);
            intercalar(notas, inicial, meio, termino);
        }
    }

    private static void intercalar(Nota[] notas, int inicial, int meio, int termino) {
        Nota[] resultado = new Nota[termino - inicial + 1];

        int atual = 0, atual1 = inicial, atual2 = meio + 1;

        while (atual1 <= meio && atual2 <= termino) {
            if (notas[atual1].getValor() < notas[atual2].getValor()) {
                resultado[atual++] = notas[atual1++];
            } else {
                resultado[atual++] = notas[atual2++];
            }
        }

        while (atual1 <= meio) resultado[atual++] = notas[atual1++];
        while (atual2 <= termino) resultado[atual++] = notas[atual2++];

        for (int i = 0; i < resultado.length; i++) {
            notas[inicial + i] = resultado[i];
        }
    }

    /**
     * Ordena um array usando o algoritmo Quick Sort (particionamento recursivo).
     */
    public static void quickSort(Nota[] notas, int esquerda, int direita) {
        if (esquerda < direita) {
            int p = particiona(notas, esquerda, direita);
            quickSort(notas, esquerda, p);
            quickSort(notas, p + 1, direita);
        }
    }

    private static int particiona(Nota[] notas, int esquerda, int direita) {
        int meio = (esquerda + direita) / 2;
        Nota pivot = notas[meio];
        int i = esquerda - 1, j = direita + 1;

        while (true) {
            do { i++; } while (notas[i].getValor() < pivot.getValor());
            do { j--; } while (notas[j].getValor() > pivot.getValor());
            if (i >= j) return j;
            trocar(notas, i, j);
        }
    }


    /**
     * Ordena usando Insertion Sort (ordenação por inserção).
     */
    public static void insertionSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (int i = 1; i < quantidadeElementos; i++) {
            int j = i;
            while (j > 0 && notas[j].getValor() < notas[j - 1].getValor()) {
                trocar(notas, j, j - 1);
                j--;
            }
        }
    }


    /**
     * Ordena usando Bubble Sort com otimização por flag.
     */
    public static void bubbleSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (int i = 0; i < quantidadeElementos - 1; i++) {
            boolean trocou = false;
            for (int j = 0; j < quantidadeElementos - 1 - i; j++) {
                if (notas[j].getValor() > notas[j + 1].getValor()) {
                    trocar(notas, j, j + 1);
                    trocou = true;
                }
            }
            if (!trocou) break;
        }
    }


    /**
     * Ordena usando Selection Sort (busca do menor a cada iteração).
     */
    public static void selectionSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (int i = 0; i < quantidadeElementos; i++) {
            int menor = buscaMenorSalario(notas, i, quantidadeElementos - 1);
            trocar(notas, i, menor);
        }
    }

    private static int buscaMenorSalario(Nota[] notas, int inicio, int fim) {
        int menor = inicio;
        for (int i = inicio + 1; i <= fim; i++) {
            if (notas[i].getValor() < notas[menor].getValor()) {
                menor = i;
            }
        }
        return menor;
    }


    /**
     * Ordena usando Heap Sort (estrutura de heap máximo).
     */
    public static void heapSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (int i = quantidadeElementos / 2 - 1; i >= 0; i--) {
            heapify(notas, quantidadeElementos, i);
        }

        for (int i = quantidadeElementos - 1; i > 0; i--) {
            trocar(notas, 0, i);
            heapify(notas, i, 0);
        }
    }

    private static void heapify(Nota[] notas, int n, int i) {
        int maior = i, esq = 2 * i + 1, dir = 2 * i + 2;

        if (esq < n && notas[esq].getValor() > notas[maior].getValor()) maior = esq;
        if (dir < n && notas[dir].getValor() > notas[maior].getValor()) maior = dir;

        if (maior != i) {
            trocar(notas, i, maior);
            heapify(notas, n, maior);
        }
    }


    /**
     * Ordena usando Counting Sort. Apenas para valores inteiros e não-negativos.
     */
    public static void countingSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (Nota nota : notas) {
            if (nota.getValor() < 0 || nota.getValor() != (int) nota.getValor()) {
                throw new IllegalArgumentException("Counting Sort só funciona com inteiros não-negativos.");
            }
        }

        int min = (int) notas[0].getValor();
        int max = (int) notas[0].getValor();
        for (Nota nota : notas) {
            int valor = (int) nota.getValor();
            if (valor < min) min = valor;
            if (valor > max) max = valor;
        }

        int range = max - min + 1;
        int[] count = new int[range];
        Nota[] output = new Nota[quantidadeElementos];

        for (Nota nota : notas) count[(int) nota.getValor() - min]++;
        for (int i = 1; i < count.length; i++) count[i] += count[i - 1];

        for (int i = quantidadeElementos - 1; i >= 0; i--) {
            int valor = (int) notas[i].getValor();
            output[count[valor - min] - 1] = notas[i];
            count[valor - min]--;
        }

        System.arraycopy(output, 0, notas, 0, quantidadeElementos);
    }


    /**
     * Ordena usando Shell Sort (inserção com saltos).
     */
    public static void shellSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (int intervalo = quantidadeElementos / 2; intervalo > 0; intervalo /= 2) {
            for (int i = intervalo; i < quantidadeElementos; i++) {
                Nota temp = notas[i];
                int j = i;
                while (j >= intervalo && notas[j - intervalo].getValor() > temp.getValor()) {
                    notas[j] = notas[j - intervalo];
                    j -= intervalo;
                }
                notas[j] = temp;
            }
        }
    }


    /**
     * Ordena usando Radix Sort. Funciona apenas com inteiros não-negativos.
     */
    public static void radixSort(Nota[] notas, int quantidadeElementos) {
        validarEntrada(notas, quantidadeElementos);

        for (Nota nota : notas) {
            if (nota.getValor() < 0 || nota.getValor() != (int) nota.getValor()) {
                throw new IllegalArgumentException("Radix Sort só suporta inteiros não-negativos.");
            }
        }

        int max = (int) notas[0].getValor();
        for (Nota nota : notas) {
            if (nota.getValor() > max) max = (int) nota.getValor();
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(notas, quantidadeElementos, exp);
        }
    }

    private static void countingSortByDigit(Nota[] notas, int n, int exp) {
        Nota[] output = new Nota[n];
        int[] count = new int[10];

        for (Nota nota : notas) count[((int) nota.getValor() / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            int digito = ((int) notas[i].getValor() / exp) % 10;
            output[count[digito] - 1] = notas[i];
            count[digito]--;
        }

        System.arraycopy(output, 0, notas, 0, n);
    }


    /**
     * Troca dois elementos no array.
     */
    private static void trocar(Nota[] notas, int i, int j) {
        Nota temp = notas[i];
        notas[i] = notas[j];
        notas[j] = temp;
    }

    /**
     * Valida se o array e o número de elementos são válidos.
     */
    private static void validarEntrada(Nota[] notas, int quantidadeElementos) {
        if (notas == null) {
            throw new IllegalArgumentException("Array de notas não pode ser nulo");
        }
        if (quantidadeElementos <= 0) {
            throw new IllegalArgumentException("Quantidade de elementos inválida: " + quantidadeElementos);
        }
    }
}
