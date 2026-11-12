package um.practicascontinuas.benchmark.algorithm;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import um.practicascontinuas.benchmark.exception.InvalidRequestException;

class MatMulAlgorithmTest {

  private final MatMulAlgorithm algorithm = new MatMulAlgorithm();

  @Test
  void elNombreCoincideConElContrato() {
    assertEquals("matmul", algorithm.name());
  }

  @Test
  void multiplicaMatricesDeUnoPorUno() {
    assertMatrixEquals(
        new double[][] {{6}}, MatMulAlgorithm.matmul(new double[][] {{2}}, new double[][] {{3}}));
  }

  @Test
  void multiplicaMatricesDeDosPorDos() {
    double[][] a = {{1, 2}, {3, 4}};
    double[][] b = {{5, 6}, {7, 8}};

    assertMatrixEquals(new double[][] {{19, 22}, {43, 50}}, MatMulAlgorithm.matmul(a, b));
  }

  @Test
  void multiplicaMatricesDeTresPorTres() {
    double[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    double[][] b = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};

    assertMatrixEquals(
        new double[][] {{30, 24, 18}, {84, 69, 54}, {138, 114, 90}}, MatMulAlgorithm.matmul(a, b));
  }

  @Test
  void multiplicarPorLaIdentidadDevuelveLaMatrizOriginal() {
    double[][] a = {{1, 2}, {3, 4}};

    double[][] result = MatMulAlgorithm.matmul(a, new double[][] {{1, 0}, {0, 1}});

    assertMatrixEquals(a, result);
    assertNotSame(a, result);
  }

  @Test
  void multiplicarPorLaMatrizNulaDevuelveCeros() {
    double[][] result = MatMulAlgorithm.matmul(new double[][] {{1, 2}, {3, 4}}, new double[2][2]);

    assertMatrixEquals(new double[2][2], result);
  }

  @Test
  void noModificaLasMatricesDeEntrada() {
    double[][] a = {{1, 2}, {3, 4}};
    double[][] b = {{5, 6}, {7, 8}};

    MatMulAlgorithm.matmul(a, b);

    assertMatrixEquals(new double[][] {{1, 2}, {3, 4}}, a);
    assertMatrixEquals(new double[][] {{5, 6}, {7, 8}}, b);
  }

  @Test
  void rechazaDimensionesIncompatiblesYMatricesNoCuadradas() {
    assertThrows(
        IllegalArgumentException.class,
        () -> MatMulAlgorithm.matmul(new double[][] {{1}}, new double[][] {{1, 2}, {3, 4}}));
    assertThrows(
        IllegalArgumentException.class,
        () -> MatMulAlgorithm.matmul(new double[][] {{1, 2}}, new double[][] {{1, 2}, {3, 4}}));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 5, 10, 50, 100})
  void runGeneraYMultiplicaSinErrores(int size) {
    assertDoesNotThrow(() -> algorithm.run(size));
  }

  @Test
  void runRechazaTamanoSuperiorAlLimite() {
    assertThrows(InvalidRequestException.class, () -> algorithm.run(1001));
  }

  private static void assertMatrixEquals(double[][] expected, double[][] actual) {
    assertEquals(expected.length, actual.length);
    for (int i = 0; i < expected.length; ++i) {
      assertArrayEquals(expected[i], actual[i]);
    }
  }
}
