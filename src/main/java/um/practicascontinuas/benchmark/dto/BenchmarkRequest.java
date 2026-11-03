package um.practicascontinuas.benchmark.dto;

/** Petición: POST /api/v1/benchmark */
public record BenchmarkRequest(String algorithm, int size) {}
