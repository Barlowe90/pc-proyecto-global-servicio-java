package um.practicascontinuas.benchmark.algorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/** SelectionSort iterativo clásico sobre enteros aleatorios. Coste O(n²) en todos los casos. */
@Component
public class SelectionSortAlgorithm implements Algorithm {

  @Override
  public String name() {
    return "selectionsort";
  }

  @Override
  public void run(int size) {
    sort(ThreadLocalRandom.current().ints(size).toArray());
  }

  /** Ordena el array de forma ascendente, modificándolo en el sitio con memoria O(1). */
  static void sort(int[] array) {
    if (array.length < 2) {
      return;
    }
    sort(array, 0, array.length);
  }

  /**
   * Ordena el tramo [from, to): en cada pasada busca el mínimo de la parte sin ordenar y lo
   * intercambia con su primera posición, de modo que el prefijo ordenado crece en uno.
   */
  private static void sort(int[] array, int from, int to) {
    for (int i = from; i < to - 1; i++) {
      int min = i;
      for (int j = i + 1; j < to; j++) {
        if (array[j] < array[min]) {
          min = j;
        }
      }
      int tmp = array[i];
      array[i] = array[min];
      array[min] = tmp;
    }
  }
}
