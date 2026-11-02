package um.practicascontinuas.benchmark.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduce las excepciones a respuestas HTTP con formato JSON uniforme. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(AlgorithmNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(AlgorithmNotFoundException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalid(InvalidRequestException ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  /** JSON mal formado o con tipos incorrectos (p. ej. "size": "abc"). */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadable() {
    return error(HttpStatus.BAD_REQUEST, "Cuerpo de la petición inválido");
  }

  /** Error inesperado: se registra en el log y no se filtran detalles al cliente. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
    log.error("Error inesperado", ex);
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
  }

  private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
  }
}
