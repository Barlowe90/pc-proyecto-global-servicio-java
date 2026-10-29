package um.practicascontinuas.benchmark.dto;

/** Respuesta: timeMs es la mediana de las mediciones. */
public record BenchmarkResponse(
    String language, String algorithm, int size, double timeMs, int runs) {}
