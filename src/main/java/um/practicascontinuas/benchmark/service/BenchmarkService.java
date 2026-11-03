package um.practicascontinuas.benchmark.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import um.practicascontinuas.benchmark.algorithm.Algorithm;
import um.practicascontinuas.benchmark.dto.BenchmarkRequest;
import um.practicascontinuas.benchmark.dto.BenchmarkResponse;
import um.practicascontinuas.benchmark.exception.AlgorithmNotFoundException;
import um.practicascontinuas.benchmark.exception.InvalidRequestException;

/** Valida la petición, ejecuta el algoritmo 5 veces y devuelve la mediana en milisegundos. */
@Service
public class BenchmarkService {

  static final String LANGUAGE = "java";
  static final int RUNS = 5;
  static final int MAX_SIZE = 1_000_000;

  private final Map<String, Algorithm> algorithms;

  /** Spring inyecta todos los beans Algorithm: añadir uno nuevo no obliga a tocar el servicio. */
  public BenchmarkService(List<Algorithm> algorithms) {
    this.algorithms =
        algorithms.stream()
            .collect(Collectors.toMap(a -> a.name().toLowerCase(Locale.ROOT), a -> a));
  }

  public BenchmarkResponse benchmark(BenchmarkRequest request) {
    if (request.algorithm() == null || request.algorithm().isBlank()) {
      throw new InvalidRequestException("El campo 'algorithm' es obligatorio");
    }
    if (request.size() < 1 || request.size() > MAX_SIZE) {
      throw new InvalidRequestException("El campo 'size' debe estar entre 1 y " + MAX_SIZE);
    }

    String name = request.algorithm().trim().toLowerCase(Locale.ROOT);
    Algorithm algorithm = algorithms.get(name);
    if (algorithm == null) {
      throw new AlgorithmNotFoundException(request.algorithm());
    }

    double[] timesMs = new double[RUNS];
    for (int i = 0; i < RUNS; i++) {
      long start = System.nanoTime();
      algorithm.run(request.size());
      timesMs[i] = (System.nanoTime() - start) / 1_000_000.0;
    }
    Arrays.sort(timesMs);

    return new BenchmarkResponse(LANGUAGE, name, request.size(), timesMs[RUNS / 2], RUNS);
  }
}
