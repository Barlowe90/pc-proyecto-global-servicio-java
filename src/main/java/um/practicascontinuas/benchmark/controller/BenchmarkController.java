package um.practicascontinuas.benchmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import um.practicascontinuas.benchmark.dto.BenchmarkRequest;
import um.practicascontinuas.benchmark.dto.BenchmarkResponse;
import um.practicascontinuas.benchmark.service.BenchmarkService;

@RestController
@RequestMapping("/api/v1/benchmark")
public class BenchmarkController {

  private final BenchmarkService benchmarkService;

  public BenchmarkController(BenchmarkService benchmarkService) {
    this.benchmarkService = benchmarkService;
  }

  @PostMapping
  public ResponseEntity<BenchmarkResponse> benchmark(@RequestBody BenchmarkRequest request) {

    BenchmarkResponse response = benchmarkService.benchmark(request);

    return ResponseEntity.ok(response);
  }
}
