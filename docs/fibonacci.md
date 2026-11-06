# Fibonacci

## 1. Resumen

Calcula el n-ésimo número de la sucesión de Fibonacci. El boletín 0 lo incluye entre los algoritmos del proyecto con la condición de implementarlo con **programación dinámica**, no con la recursión ingenua, que es exponencial. A diferencia de los algoritmos de ordenación, no tiene datos de entrada que generar: solo trabaja con `n`.

## 2. Contrato

- **Nombre canónico:** `fibonacci` (valor del campo `algorithm`).
- **Significado de `size`:** posición `n` de la sucesión que se calcula, con F(0) = 0 y F(1) = 1.
- **Tipo y distribución de los datos:** no hay datos aleatorios; el único dato es `n`.
- **Resultado esperado:** F(n) **módulo 1 000 000 007**, como `long`. F(93) ya no cabe en un `long`, y con el módulo cada suma sigue costando O(1) hasta el tamaño máximo (con `BigInteger` el coste de cada suma crecería con el número de dígitos).
- **Casos límite y restricciones:** la API acepta `size` entre 1 y 1 000 000 (validado por `BenchmarkService`). El método de cálculo también admite `n = 0` y `n = 1`, y lanza `IllegalArgumentException` si `n` es negativo.

## 3. Funcionamiento e implementación

DP iterativa ascendente (bottom-up): se parte de F(0) y F(1) y en cada paso se obtiene F(i) = F(i − 1) + F(i − 2). Como cada valor solo depende de los dos anteriores, no hace falta guardar la tabla entera: bastan dos variables rodantes.

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/FibonacciAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la inyecta en `BenchmarkService` y aparece en `GET /api/v1/algorithms` sin tocar el servicio ni el controlador.
- **Decisiones técnicas:**
  - **Sin recursión:** con `n = 1 000 000` una versión recursiva (incluso memoizada) provocaría `StackOverflowError`.
  - **Dos variables en lugar de una tabla:** la memoria adicional es O(1) en vez de O(n).
  - **Módulo con una resta:** los dos sumandos son menores que 1 000 000 007, así que su suma cabe de sobra en un `long` y basta con restar el módulo si se pasa, sin usar `%`.
  - **Cálculo comprobable sin la API:** `FibonacciAlgorithm.fibonacci(int n)` devuelve F(n) y es el que usan los tests.
  - **Evitar la eliminación de código muerto:** `run(size)` guarda el resultado en un campo `volatile`, de modo que el JIT no puede descartar el cálculo.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | O(n) | O(1) |
| Medio | O(n) | O(1) |
| Peor | O(n) | O(1) |

Se hacen exactamente `n − 1` iteraciones con una suma y una comparación cada una, independientemente del valor de `n`, así que los tres casos coinciden.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** no hay entrada que generar; cada una de las 5 ejecuciones recalcula F(n) desde cero.
- **Qué operaciones se incluyen/excluyen del cronómetro:** solo el cálculo de F(n) (y la escritura del resultado en el campo).
- **Reloj utilizado:** `System.nanoTime()` (en `BenchmarkService`).
- **Calentamiento de la JVM:** no se realiza, igual que en el resto de algoritmos.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de las 5 mediciones.
- **Consideraciones:** los tiempos son muy pequeños (por debajo de 1 ms incluso con `n = 1 000 000`), así que con tamaños pequeños la medida está dominada por el ruido del reloj. Las primeras peticiones tras arrancar la JVM son más lentas porque el JIT aún no ha compilado el bucle.

Resultados orientativos en local (segunda tanda de peticiones, JVM ya caliente):

| `size` | Fibonacci (ms) |
|---|---|
| 1 000 | 0,0008 |
| 100 000 | 0,0688 |
| 1 000 000 | 0,6724 |

De 100 000 a 1 000 000 el tamaño se multiplica por 10 y el tiempo por unas 9,8 veces, lo que concuerda con el crecimiento lineal esperado.

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/FibonacciAlgorithmTest.java` y `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java`.
- **Casos comprobados:**
  - Valores conocidos: F(0) = 0, F(1) = 1, F(2) = 1, F(10) = 55 y F(44) = 701 408 733 (último valor exacto antes de que actúe el módulo).
  - F(45), el primero que supera el módulo, se reduce correctamente.
  - Valores grandes (93, 100, 1 000, 12 345, 100 000 y 1 000 000) contrastados con una implementación independiente que calcula F(n) exacto con `BigInteger` por duplicación rápida y aplica el módulo al final.
  - `n = 1 000 000` sin errores; `n` negativo lanza `IllegalArgumentException`.
  - `run(size)` guarda el resultado esperado, el nombre registrado es `fibonacci` y el servicio de Spring lo ejecuta y devuelve un tiempo válido.
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"fibonacci","size":1000}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.

## 8. Referencias (opcional)

- Boletín 0, apartado 3.5: Fibonacci con programación dinámica.
- Issue [#22](https://github.com/Barlowe90/pc-proyecto-global-servicio-java/issues/22).
