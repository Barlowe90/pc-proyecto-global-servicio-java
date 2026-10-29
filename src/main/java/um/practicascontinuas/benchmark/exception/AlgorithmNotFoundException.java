package um.practicascontinuas.benchmark.exception;

/** El algoritmo pedido no existe en este microservicio (HTTP 404). */
public class AlgorithmNotFoundException extends RuntimeException {

  public AlgorithmNotFoundException(String algorithm) {
    super("Algoritmo no soportado: " + algorithm);
  }
}
