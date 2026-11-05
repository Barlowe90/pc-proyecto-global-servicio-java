package um.practicascontinuas.benchmark.algorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/** InsertionSort iterativo con desplazamientos sobre enteros aleatorios. Coste O(n²). */
@Component
public class InsertionSortAlgorithm implements Algorithm {

  @Override
  public String name() {
    return "insertionsort";
  }

  @Override
  public void run(int size) {
    sort(ThreadLocalRandom.current().ints(size).toArray());
  }

  /** Ordena el array de forma ascendente y estable, modificándolo en el sitio con memoria O(1). */
  static void sort(int[] array) {
    if (array.length < 2) {
      return;
    }
    sort(array, 0, array.length);
  }

  /** Ordena el tramo [from, to) desplazando a la derecha los elementos mayores. */
  private static void sort(int[] array, int from, int to) {
    for (int i = from + 1; i < to; i++) {
      int value = array[i];
      int j = i;
      while (j > from && array[j - 1] > value) {
        array[j] = array[j - 1];
        j--;
      }
      array[j] = value;
    }
  }
}
