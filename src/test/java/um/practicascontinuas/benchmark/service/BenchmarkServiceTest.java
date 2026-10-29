package um.practicascontinuas.benchmark.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import um.practicascontinuas.benchmark.algorithm.Algorithm;
import um.practicascontinuas.benchmark.dto.BenchmarkRequest;
import um.practicascontinuas.benchmark.dto.BenchmarkResponse;
import um.practicascontinuas.benchmark.exception.AlgorithmNotFoundException;
import um.practicascontinuas.benchmark.exception.InvalidRequestException;

class BenchmarkServiceTest {

  /** Algoritmo falso que cuenta cuántas veces se ejecuta. */
  static class CountingAlgorithm implements Algorithm {
    int calls = 0;

    @Override
    public String name() {
      return "mergesort";
    }

    @Override
    public void run(int size) {
      calls++;
    }
  }

  private final CountingAlgorithm algorithm = new CountingAlgorithm();
  private final BenchmarkService service = new BenchmarkService(List.of(algorithm));

  @Test
  void ejecutaCincoMedicionesYDevuelveElContrato() {
    BenchmarkResponse response = service.benchmark(new BenchmarkRequest("mergesort", 1000));

    assertEquals(5, algorithm.calls);
    assertEquals("java", response.language());
    assertEquals("mergesort", response.algorithm());
    assertEquals(1000, response.size());
    assertEquals(5, response.runs());
  }

  @Test
  void elNombreNoDistingueMayusculasNiEspacios() {
    assertEquals(
        "mergesort", service.benchmark(new BenchmarkRequest(" MergeSort ", 10)).algorithm());
  }

  @Test
  void algoritmoDesconocidoLanzaNotFound() {
    assertThrows(
        AlgorithmNotFoundException.class,
        () -> service.benchmark(new BenchmarkRequest("bogosort", 10)));
  }

  @Test
  void algoritmoVacioLanzaInvalidRequest() {
    assertThrows(
        InvalidRequestException.class, () -> service.benchmark(new BenchmarkRequest(" ", 10)));
  }

  @Test
  void tamanoFueraDeRangoLanzaInvalidRequest() {
    assertThrows(
        InvalidRequestException.class,
        () -> service.benchmark(new BenchmarkRequest("mergesort", 0)));
    assertThrows(
        InvalidRequestException.class,
        () -> service.benchmark(new BenchmarkRequest("mergesort", BenchmarkService.MAX_SIZE + 1)));
  }
}
