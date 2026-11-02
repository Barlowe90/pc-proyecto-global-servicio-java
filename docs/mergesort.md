# MergeSort

## 1. Resumen

Algoritmo de ordenación por mezcla. Es el primer algoritmo exigido por el boletín 0 para todos los microservicios y sirve de referencia para comparar lenguajes.

## 2. Contrato

- **Nombre canónico:** `mergesort` (valor del campo `algorithm`).
- **Significado de `size`:** número de elementos del array a ordenar.
- **Tipo y distribución de los datos:** `int` de 32 bits uniformemente aleatorios en todo el rango (`ThreadLocalRandom.ints`).
- **Resultado esperado:** el array queda ordenado de forma ascendente.
- **Casos límite y restricciones:** `size` entre 1 y 1 000 000 (validado por `BenchmarkService`).

## 3. Funcionamiento e implementación

Variante descendente (*top-down*) recursiva: divide el tramo por la mitad, ordena cada mitad y las mezcla.

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/MergeSortAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la inyecta en `BenchmarkService`.
- **Decisiones técnicas:** se reserva un único buffer auxiliar de tamaño `n` para todas las mezclas (no se crean arrays en cada llamada recursiva). La comparación `<=` hace que el algoritmo sea estable.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | O(n log n) | O(n) |
| Medio | O(n log n) | O(n) |
| Peor | O(n log n) | O(n) |

La profundidad de recursión es O(log n), por lo que no hay riesgo de desbordar la pila con el tamaño máximo.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** en cada una de las 5 ejecuciones se genera un array aleatorio nuevo.
- **Qué operaciones se incluyen/excluyen del cronómetro:** el contrato `Algorithm.run(size)` genera la entrada y la ordena, así que la generación del array (O(n)) queda dentro del tiempo medido.
- **Reloj utilizado:** `System.nanoTime()`.
- **Calentamiento de la JVM:** no se realiza.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de las 5 mediciones.
- **Consideraciones:** los primeros benchmarks tras arrancar la JVM son más lentos porque el JIT aún no ha compilado el código.

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/MergeSortAlgorithmTest.java` y `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java`.
- **Casos comprobados:** array vacío y de un elemento, dos elementos, ya ordenado, orden inverso, duplicados, negativos y extremos (`Integer.MIN_VALUE`/`MAX_VALUE`), arrays aleatorios de tamaño par e impar comparados con `Arrays.sort`, y registro del algoritmo en el servicio de Spring.
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"mergesort","size":1000}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.
