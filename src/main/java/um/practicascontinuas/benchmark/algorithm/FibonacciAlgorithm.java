package um.practicascontinuas.benchmark.algorithm;

import org.springframework.stereotype.Component;

/**
 * N-ésimo número de Fibonacci por programación dinámica ascendente (bottom-up) con dos variables
 * rodantes. El resultado se calcula módulo 1 000 000 007 para no desbordar {@code long}.
 */
@Component
public class FibonacciAlgorithm implements Algorithm {

  static final long MOD = 1_000_000_007L;

  /**
   * Último resultado calculado. Al guardarlo en un campo {@code volatile} la JVM no puede eliminar
   * el cálculo como código muerto.
   */
  private volatile long lastResult;

  @Override
  public String name() {
    return "fibonacci";
  }

  @Override
  public void run(int size) {
    lastResult = fibonacci(size);
  }

  long lastResult() {
    return lastResult;
  }

  /** Devuelve F(n) mod 1 000 000 007, con F(0) = 0 y F(1) = 1. */
  static long fibonacci(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("n debe ser >= 0: " + n);
    }
    long previous = 0; // F(i - 1)
    long current = 1; // F(i)
    if (n == 0) {
      return previous;
    }
    for (int i = 2; i <= n; i++) {
      // ambos sumandos son < MOD, así que la suma cabe en long y basta con restar
      long next = previous + current;
      if (next >= MOD) {
        next -= MOD;
      }
      previous = current;
      current = next;
    }
    return current;
  }
}
