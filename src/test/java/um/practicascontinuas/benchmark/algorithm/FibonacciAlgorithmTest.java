package um.practicascontinuas.benchmark.algorithm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FibonacciAlgorithmTest {

  private final FibonacciAlgorithm algorithm = new FibonacciAlgorithm();

  /**
   * Implementación independiente: F(n) exacto con BigInteger por duplicación rápida (F(2k) = F(k) ·
   * (2·F(k+1) − F(k)), F(2k+1) = F(k)² + F(k+1)²). El módulo se aplica solo al final.
   */
  private static long fibonacciBigInteger(int n) {
    BigInteger a = BigInteger.ZERO; // F(k)
    BigInteger b = BigInteger.ONE; // F(k + 1)
    for (int bit = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(n); bit >= 0; bit--) {
      BigInteger c = a.multiply(b.shiftLeft(1).subtract(a)); // F(2k)
      BigInteger d = a.multiply(a).add(b.multiply(b)); // F(2k + 1)
      if (((n >>> bit) & 1) == 0) {
        a = c;
        b = d;
      } else {
        a = d;
        b = c.add(d);
      }
    }
    return a.mod(BigInteger.valueOf(FibonacciAlgorithm.MOD)).longValueExact();
  }

  @Test
  void elNombreCoincideConElContrato() {
    assertEquals("fibonacci", algorithm.name());
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "1, 1", "2, 1", "10, 55", "44, 701408733"})
  void calculaValoresConocidos(int n, long expected) {
    assertEquals(expected, FibonacciAlgorithm.fibonacci(n));
  }

  @Test
  void elPrimerValorQueSuperaElModuloSeReduce() {
    // F(45) = 1 134 903 170 > 1 000 000 007
    assertEquals(1_134_903_170L - FibonacciAlgorithm.MOD, FibonacciAlgorithm.fibonacci(45));
  }

  @ParameterizedTest
  @ValueSource(ints = {93, 100, 1000, 12_345, 100_000})
  void coincideConBigIntegerParaValoresGrandes(int n) {
    assertEquals(fibonacciBigInteger(n), FibonacciAlgorithm.fibonacci(n));
  }

  @Test
  void calculaElTamanoMaximoSinErrores() {
    long result = assertDoesNotThrow(() -> FibonacciAlgorithm.fibonacci(1_000_000));
    assertEquals(fibonacciBigInteger(1_000_000), result);
  }

  @Test
  void rechazaNNegativo() {
    assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithm.fibonacci(-1));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 1000, 1_000_000})
  void runCalculaYGuardaElResultado(int size) {
    algorithm.run(size);
    assertEquals(FibonacciAlgorithm.fibonacci(size), algorithm.lastResult());
  }
}
