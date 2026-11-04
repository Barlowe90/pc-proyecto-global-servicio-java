package um.practicascontinuas.benchmark.algorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/** QuickSort con partición de Hoare en el sitio sobre enteros aleatorios. */
@Component
public class QuickSortAlgorithm implements Algorithm {

  @Override
  public String name() {
    return "quicksort";
  }

  @Override
  public void run(int size) {
    sort(ThreadLocalRandom.current().ints(size).toArray());
  }

  /** Ordena el array de forma ascendente, modificándolo en el sitio. */
  static void sort(int[] array) {
    if (array.length < 2) {
      return;
    }
    sort(array, 0, array.length - 1);
  }

  /**
   * Ordena el tramo [lo, hi]. Se llama recursivamente a la parte más pequeña y la grande se sigue
   * ordenando en el bucle, así la pila no pasa de O(log n) llamadas.
   */
  private static void sort(int[] array, int lo, int hi) {
    while (lo < hi) {
      int p = partition(array, lo, hi);
      if (p - lo < hi - p) {
        sort(array, lo, p);
        lo = p + 1;
      } else {
        sort(array, p + 1, hi);
        hi = p;
      }
    }
  }

  /**
   * Partición de Hoare con el elemento central como pivote. Devuelve j tal que los elementos de
   * [lo, j] son &lt;= pivote y los de [j + 1, hi] son &gt;= pivote.
   */
  private static int partition(int[] array, int lo, int hi) {
    int pivot = array[(lo + hi) >>> 1];
    int i = lo - 1;
    int j = hi + 1;
    while (true) {
      do {
        i++;
      } while (array[i] < pivot);
      do {
        j--;
      } while (array[j] > pivot);
      if (i >= j) {
        return j;
      }
      int tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
    }
  }
}
