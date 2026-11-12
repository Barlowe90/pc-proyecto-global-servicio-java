# MatMul

## 1. Resumen

Multiplicación de matrices cuadradas. El algoritmo se incluye como benchmark de una carga de trabajo numérica cuyo coste crece de forma cúbica con la dimensión de las matrices.

## 2. Contrato

- **Nombre canónico:** `matmul`.
- **Significado de `size`:** dimensión $n$ de cada matriz cuadrada ($n \times n$).
- **Tipo y distribución de los datos:** valores `double` uniformes en $[0, 1)$, generados aleatoriamente en cada ejecución.
- **Resultado esperado:** una matriz nueva $C = A \times B$, sin modificar las matrices de entrada.
- **Casos límite y restricciones:** `run(size)` admite valores de $1$ a $1000$. Un tamaño fuera de ese intervalo produce `InvalidRequestException` (HTTP 400). La función de multiplicación admite matrices $1 \times 1$ y rechaza con `IllegalArgumentException` matrices nulas, vacías, no cuadradas o de dimensiones incompatibles.

## 3. Funcionamiento e implementación

Usa el algoritmo clásico de tres bucles, en orden $i$–$k$–$j$ (variante IKJ). Cada elemento del resultado se calcula como

$$
C_{ij} = \sum_{k=0}^{n-1} A_{ik} B_{kj}.
$$

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/MatMulAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la registra automáticamente en `BenchmarkService`.
- **Decisiones técnicas:** el método `matmul(a, b)` valida las dimensiones y crea una matriz de resultado independiente. `run(size)` valida el límite antes de reservar memoria y genera matrices nuevas para cada ejecución.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | $O(n^3)$ | $O(n^2)$ |
| Medio | $O(n^3)$ | $O(n^2)$ |
| Peor | $O(n^3)$ | $O(n^2)$ |

Los tres bucles recorren las dimensiones completas, independientemente de los valores de las matrices. El almacenamiento de las dos entradas y de la matriz resultado requiere $O(n^2)$ memoria.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** cada llamada a `run(size)` genera dos matrices nuevas con valores aleatorios uniformes en $[0, 1)$.
- **Qué operaciones se incluyen/excluyen del cronómetro:** la generación de las entradas y la multiplicación están dentro de `Algorithm.run(size)` y, por tanto, se miden juntas.
- **Reloj utilizado:** `System.nanoTime()` en `BenchmarkService`.
- **Calentamiento de la JVM:** no se realiza un calentamiento explícito.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de cinco ejecuciones (`runs: 5`).
- **Consideraciones sobre reproducibilidad y comparación entre lenguajes:** las matrices aleatorias y la compilación JIT pueden variar entre peticiones. Las mediciones pequeñas son especialmente sensibles al coste fijo del servicio y al ruido del entorno; deben interpretarse como orientativas.

Resultados locales reales obtenidos mediante `curl` (las respuestas incluyen la mediana devuelta por la API):

```bash
$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":1}'
{"language":"java","algorithm":"matmul","size":1,"timeMs":0.008789,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":2}'
{"language":"java","algorithm":"matmul","size":2,"timeMs":0.014481,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":3}'
{"language":"java","algorithm":"matmul","size":3,"timeMs":0.045155,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":5}'
{"language":"java","algorithm":"matmul","size":5,"timeMs":0.05049,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":10}'
{"language":"java","algorithm":"matmul","size":10,"timeMs":0.076814,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":50}'
{"language":"java","algorithm":"matmul","size":50,"timeMs":0.933051,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":100}'
{"language":"java","algorithm":"matmul","size":100,"timeMs":1.208198,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":1000}'
{"language":"java","algorithm":"matmul","size":1000,"timeMs":934.307438,"runs":5}

$ curl -X POST http://localhost:8080/api/v1/benchmark -H 'Content-Type: application/json' -d '{"algorithm":"matmul","size":1001}'
{"status":400,"message":"El campo 'size' para matmul debe estar entre 1 y 1000"}
```

La implementación tiene coste $O(n^3)$: duplicar $n$ multiplica aproximadamente por ocho el trabajo dominante. Los resultados pequeños no siguen esa proporción (por ejemplo, pasar de $n=50$ a $n=100$ solo pasa de $0.933051\,\text{ms}$ a $1.208198\,\text{ms}$), porque los tiempos son breves y pesan el coste fijo y el ruido de medición. Al comparar $n=100$ y $n=1000$, un aumento de dimensión de $10\times$ da un tiempo aproximadamente $773\times$ mayor ($1.208198\,\text{ms}$ frente a $934.307438\,\text{ms}$), del orden esperado para un crecimiento cúbico.

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/MatMulAlgorithmTest.java`, `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java` y `src/test/java/um/practicascontinuas/benchmark/controller/BenchmarkControllerIntegrationTest.java`.
- **Casos comprobados:** productos $1 \times 1$, $2 \times 2$ y $3 \times 3$ con resultados conocidos, identidad, matriz nula, entradas no modificadas, dimensiones incompatibles, rechazo de tamaños sobre el límite, nombre canónico y registro/endpoints.
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"matmul","size":100}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.

## 8. Referencias

Algunas de las referencias usadas para la implementación son:

- [Gist arunenigma](https://gist.github.com/arunenigma/3030453).
- [Matrix Multiplication - GeeksforGeeks](https://www.geeksforgeeks.org/dsa/strassens-matrix-multiplication/).
