package um.practicascontinuas.benchmark.algorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

/** MergeSort descendente (top-down) sobre enteros aleatorios, con un único buffer auxiliar. */
@Component
public class MergeSortAlgorithm implements Algorithm {

  @Override
  public String name() {
    return "mergesort";
  }

  @Override
  public void run(int size) {
    sort(ThreadLocalRandom.current().ints(size).toArray());
  }

  /** Ordena el array de forma ascendente y estable, modificándolo en el sitio. */
  static void sort(int[] array) {
    if (array.length < 2) {
      return;
    }
    sort(array, new int[array.length], 0, array.length);
  }

  /** Ordena el tramo [from, to) usando {@code buffer} para las mezclas. */
  private static void sort(int[] array, int[] buffer, int from, int to) {
    if (to - from < 2) {
      return;
    }
    int mid = (from + to) >>> 1;
    sort(array, buffer, from, mid);
    sort(array, buffer, mid, to);
    merge(array, buffer, from, mid, to);
  }

  /** Mezcla los tramos ordenados [from, mid) y [mid, to). */
  private static void merge(int[] array, int[] buffer, int from, int mid, int to) {
    System.arraycopy(array, from, buffer, from, to - from);
    int left = from;
    int right = mid;
    for (int i = from; i < to; i++) {
      // <= mantiene la estabilidad: a igualdad gana el elemento de la izquierda
      if (right >= to || (left < mid && buffer[left] <= buffer[right])) {
        array[i] = buffer[left++];
      } else {
        array[i] = buffer[right++];
      }
    }
  }
}
