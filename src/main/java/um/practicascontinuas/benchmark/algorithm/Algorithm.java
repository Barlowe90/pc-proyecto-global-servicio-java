package um.practicascontinuas.benchmark.algorithm;

/**
 * Contrato de cada algoritmo. Para añadir uno nuevo (QuickSort, Fibonacci...) basta con crear una
 * clase anotada con {@code @Component} que implemente esta interfaz: el servicio la detecta solo.
 */
public interface Algorithm {

  /** Nombre usado en la petición, en minúsculas (p. ej. "mergesort"). */
  String name();

  /** Genera una entrada de tamaño {@code size} y ejecuta el algoritmo sobre ella. */
  void run(int size);
}
