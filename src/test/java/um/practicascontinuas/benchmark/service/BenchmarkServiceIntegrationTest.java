package um.practicascontinuas.benchmark.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import um.practicascontinuas.benchmark.dto.BenchmarkRequest;
import um.practicascontinuas.benchmark.dto.BenchmarkResponse;

/** Comprueba que Spring registra los algoritmos en el servicio y que se miden de verdad. */
@SpringBootTest
class BenchmarkServiceIntegrationTest {

  @Autowired private BenchmarkService service;

  @Test
  void mergesortEstaRegistradoYDevuelveUnTiempoValido() {
    BenchmarkResponse response = service.benchmark(new BenchmarkRequest("mergesort", 10_000));

    assertEquals("java", response.language());
    assertEquals("mergesort", response.algorithm());
    assertEquals(10_000, response.size());
    assertEquals(BenchmarkService.RUNS, response.runs());
    assertTrue(response.timeMs() >= 0, "timeMs no puede ser negativo");
  }

  @Test
  void quicksortEstaRegistradoYDevuelveUnTiempoValido() {
    BenchmarkResponse response = service.benchmark(new BenchmarkRequest("quicksort", 10_000));

    assertEquals("quicksort", response.algorithm());
    assertEquals(10_000, response.size());
    assertEquals(BenchmarkService.RUNS, response.runs());
    assertTrue(response.timeMs() >= 0, "timeMs no puede ser negativo");
  }

  @Test
  void fibonacciEstaRegistradoYDevuelveUnTiempoValido() {
    BenchmarkResponse response = service.benchmark(new BenchmarkRequest("fibonacci", 100_000));

    assertEquals("fibonacci", response.algorithm());
    assertEquals(100_000, response.size());
    assertEquals(BenchmarkService.RUNS, response.runs());
    assertTrue(response.timeMs() >= 0, "timeMs no puede ser negativo");
  }

  @Test
  void insertionsortEstaRegistradoYDevuelveUnTiempoValido() {
    BenchmarkResponse response = service.benchmark(new BenchmarkRequest("insertionsort", 10_000));

    assertEquals("insertionsort", response.algorithm());
    assertEquals(10_000, response.size());
    assertEquals(BenchmarkService.RUNS, response.runs());
    assertTrue(response.timeMs() >= 0, "timeMs no puede ser negativo");
  }
}
