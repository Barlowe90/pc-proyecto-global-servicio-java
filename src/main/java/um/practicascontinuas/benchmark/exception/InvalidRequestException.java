package um.practicascontinuas.benchmark.exception;

/** Petición inválida: algoritmo vacío o tamaño fuera de rango (HTTP 400). */
public class InvalidRequestException extends RuntimeException {

  public InvalidRequestException(String message) {
    super(message);
  }
}
