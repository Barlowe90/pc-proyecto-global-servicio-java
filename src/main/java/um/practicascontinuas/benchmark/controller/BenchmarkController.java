package um.practicascontinuas.benchmark.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import um.practicascontinuas.benchmark.algorithm.Algorithm;
import um.practicascontinuas.benchmark.dto.BenchmarkRequest;
import um.practicascontinuas.benchmark.dto.BenchmarkResponse;
import um.practicascontinuas.benchmark.service.BenchmarkService;

@RestController
@RequestMapping("/api/v1")
public class BenchmarkController {

  private final BenchmarkService benchmarkService;
  private final ObjectProvider<Algorithm> algorithms;

  public BenchmarkController(
      BenchmarkService benchmarkService, ObjectProvider<Algorithm> algorithms) {
    this.benchmarkService = benchmarkService;
    this.algorithms = algorithms;
  }

  @PostMapping("/benchmark")
  public ResponseEntity<BenchmarkResponse> benchmark(@RequestBody BenchmarkRequest request) {
    BenchmarkResponse response = benchmarkService.benchmark(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/algorithms")
  public ResponseEntity<Map<String, List<String>>> getAlgorithms() {
    List<String> names =
        algorithms.orderedStream().map(Algorithm::name).distinct().sorted().toList();
    return ResponseEntity.ok(Map.of("algorithms", names));
  }
}
