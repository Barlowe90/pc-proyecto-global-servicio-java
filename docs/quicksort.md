# QuickSort

## 1. Resumen

Algoritmo de ordenación rápida por divide y vencerás. Es uno de los algoritmos propuestos en el boletín 0 y permite compararlo con MergeSort, que tiene la misma complejidad media pero no ordena en el sitio.

## 2. Contrato

- **Nombre canónico:** `quicksort` (valor del campo `algorithm`).
- **Significado de `size`:** número de elementos del array a ordenar.
- **Tipo y distribución de los datos:** `int` de 32 bits uniformemente aleatorios en todo el rango (`ThreadLocalRandom.ints`), igual que en MergeSort.
- **Resultado esperado:** el array queda ordenado de forma ascendente, sin perder ni duplicar elementos.
- **Casos límite y restricciones:** `size` entre 1 y 1 000 000 (validado por `BenchmarkService`). La implementación también acepta arrays vacíos o de un elemento, que no se modifican.

## 3. Funcionamiento e implementación

Se elige un pivote, se reparte el tramo en dos partes (menores o iguales y mayores o iguales que el pivote) y se ordena cada parte por separado.

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/QuickSortAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la inyecta en `BenchmarkService` sin tocar el servicio.
- **Decisiones técnicas:**
  - **Partición de Hoare** en el sitio, con el elemento central como pivote. Hace menos intercambios que la de Lomuto y reparte bien los elementos iguales, así que muchos duplicados no la degradan.
  - **Pivote central:** con arrays ya ordenados o en orden inverso no cae en el peor caso, como pasaría tomando el primer o el último elemento.
  - **Recursión sobre la parte más pequeña:** la parte grande se sigue ordenando en un bucle, de modo que la profundidad de la pila es como mucho O(log n) incluso en el peor caso.
  - No es estable, pero con `int` no importa.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | O(n log n) | O(log n) |
| Medio | O(n log n) | O(log n) |
| Peor | O(n²) | O(log n) |

El peor caso aparece si el pivote cae siempre en un extremo. Con datos aleatorios y pivote central es muy improbable. La memoria adicional es solo la pila de llamadas, acotada a O(log n) por recursar sobre la parte más pequeña.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** en cada una de las 5 ejecuciones se genera un array aleatorio nuevo.
- **Qué operaciones se incluyen/excluyen del cronómetro:** el contrato `Algorithm.run(size)` genera la entrada y la ordena, así que la generación del array (O(n)) queda dentro del tiempo medido, igual que en MergeSort.
- **Reloj utilizado:** `System.nanoTime()` (en `BenchmarkService`).
- **Calentamiento de la JVM:** no se realiza.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de las 5 mediciones.
- **Consideraciones:** las primeras peticiones tras arrancar la JVM son más lentas porque el JIT aún no ha compilado el código.

Resultados orientativos en local (segunda tanda de peticiones, JVM ya caliente):

| `size` | QuickSort (ms) | MergeSort (ms) |
|---|---|---|
| 1 000 | 0,09 | 0,10 |
| 10 000 | 1,17 | 1,33 |
| 100 000 | 10,45 | 15,36 |
| 1 000 000 | 144,63 | 147,80 |

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/QuickSortAlgorithmTest.java` y `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java`.
- **Casos comprobados:** array vacío y de un elemento, dos elementos, ya ordenado, orden inverso, duplicados y todos iguales, negativos y extremos (`Integer.MIN_VALUE`/`MAX_VALUE`), arrays aleatorios de tamaño par e impar, arrays grandes ya ordenados e inversos, array grande con muchos duplicados (todos comparados con `Arrays.sort`) y registro del algoritmo en el servicio de Spring.
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"quicksort","size":1000}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.
