package um.practicascontinuas.benchmark.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MergeSortAlgorithmTest {

  private final MergeSortAlgorithm algorithm = new MergeSortAlgorithm();

  /** Ordena una copia con MergeSort y la compara con Arrays.sort. */
  private static void assertSortsLikeArraysSort(int[] input) {
    int[] expected = input.clone();
    Arrays.sort(expected);
    int[] actual = input.clone();

    MergeSortAlgorithm.sort(actual);

    assertArrayEquals(expected, actual);
  }

  @Test
  void elNombreCoincideConElContrato() {
    assertEquals("mergesort", algorithm.name());
  }

  @Test
  void arrayVacioYDeUnElementoNoCambian() {
    assertSortsLikeArraysSort(new int[] {});
    assertSortsLikeArraysSort(new int[] {42});
  }

  @Test
  void ordenaDosElementos() {
    assertSortsLikeArraysSort(new int[] {2, 1});
    assertSortsLikeArraysSort(new int[] {1, 2});
  }

  @Test
  void arrayYaOrdenadoSeMantiene() {
    assertSortsLikeArraysSort(new int[] {1, 2, 3, 4, 5, 6, 7});
  }

  @Test
  void ordenaArrayEnOrdenInverso() {
    assertSortsLikeArraysSort(new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1});
  }

  @Test
  void ordenaDuplicadosYTodosIguales() {
    assertSortsLikeArraysSort(new int[] {3, 1, 3, 2, 1, 3, 2});
    assertSortsLikeArraysSort(new int[] {5, 5, 5, 5, 5});
  }

  @Test
  void ordenaNegativosYValoresExtremos() {
    assertSortsLikeArraysSort(
        new int[] {0, -1, Integer.MAX_VALUE, Integer.MIN_VALUE, 7, -7, Integer.MIN_VALUE});
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 10, 101, 1000, 10_007})
  void ordenaArraysAleatoriosDeTamanoParEImpar(int size) {
    // semilla fija: si falla, el caso es reproducible
    int[] input = new Random(size).ints(size).toArray();
    assertSortsLikeArraysSort(input);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 1000, 100_000})
  void runGeneraYOrdenaSinErrores(int size) {
    assertDoesNotThrow(() -> algorithm.run(size));
  }
}
