package um.practicascontinuas.benchmark.algorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;
import um.practicascontinuas.benchmark.exception.InvalidRequestException;

/**
 * Esta clase implementa el algoritmo de multiplicación de matrices.
 *
 * @author Hugo Sánchez
 * @version 1.0
 * @see Algorithm
 */
@Component
public class MatMulAlgorithm implements Algorithm {

  private static final int MAX_SIZE = 1000;

  @Override
  public String name() {
    return "matmul";
  }

  /**
   * Ejecuta la multiplicación de matrices cuadradas generadas aleatoriamente.
   *
   * @param size número de filas y columnas de las matrices de entrada
   */
  @Override
  public void run(int size) {
    if (size < 1 || size > MAX_SIZE) {
      throw new InvalidRequestException(
          "El campo 'size' para matmul debe estar entre 1 y " + MAX_SIZE);
    }

    double[][] a = new double[size][size];
    double[][] b = new double[size][size];

    for (int i = 0; i < size; ++i) {
      a[i] = ThreadLocalRandom.current().doubles(size).toArray();
      b[i] = ThreadLocalRandom.current().doubles(size).toArray();
    }

    matmul(a, b);
  }

  /**
   * Multiplica dos matrices cuadradas y devuelve una matriz nueva con el resultado.
   *
   * @param a la matriz izquierda
   * @param b la matriz derecha
   * @return una matriz nueva que contiene el producto {@code a × b}
   * @throws IllegalArgumentException si una matriz es nula, vacía, no cuadrada o si sus dimensiones
   *     no coinciden
   * @implNote Usa la implementación alternativa IKJ para mejorar la localidad espacial y el acceso
   *     a la caché.
   */
  static double[][] matmul(double[][] a, double[][] b) {
    if (!validarEntrada(a) || !validarEntrada(b) || a.length != b.length) {
      throw new IllegalArgumentException(
          "Las matrices deben ser cuadradas y tener las mismas dimensiones");
    }

    int size = a.length;
    double[][] result = new double[size][size];
    for (int i = 0; i < size; ++i) {
      for (int k = 0; k < size; ++k) {
        for (int j = 0; j < size; ++j) {
          result[i][j] += a[i][k] * b[k][j];
        }
      }
    }
    return result;
  }

  private static boolean validarEntrada(double[][] m) {
    if (m == null || m.length == 0) {
      return false;
    }
    for (double[] fila : m) {
      if (fila == null || fila.length != m.length) {
        return false;
      }
    }
    return true;
  }
}
