# SelectionSort

## 1. Resumen

Algoritmo de ordenación por selección. En cada pasada busca el mínimo de la parte del array que aún no está ordenada y lo intercambia con la primera posición de esa parte, de modo que el prefijo ordenado crece en un elemento. Es uno de los algoritmos propuestos en el boletín 0 y, junto con InsertionSort, sirve de contraste cuadrático frente a MergeSort y QuickSort, que son O(n log n).

## 2. Contrato

- **Nombre canónico:** `selectionsort` (valor del campo `algorithm`).
- **Significado de `size`:** número de elementos del array a ordenar.
- **Tipo y distribución de los datos:** `int` de 32 bits uniformemente aleatorios en todo el rango (`ThreadLocalRandom.ints`), con posibles negativos y duplicados, igual que en MergeSort, QuickSort e InsertionSort.
- **Resultado esperado:** el mismo array queda ordenado de forma ascendente e in situ, sin crear otro array.
- **Casos límite y restricciones:** `size` entre 1 y 1 000 000 (validado por `BenchmarkService`). La implementación también acepta arrays vacíos o de un elemento, que no se modifican. Al ser O(n²) en todos los casos, conviene usar tamaños pequeños o moderados (hasta unos pocos miles o decenas de miles): cerca del máximo una petición tardaría minutos.

## 3. Funcionamiento e implementación

Variante iterativa clásica: para cada posición `i` se recorre el tramo `[i + 1, n)` buscando el índice del menor elemento y, al terminar la búsqueda, se hace un único intercambio entre `array[i]` y ese mínimo.

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/SelectionSortAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la inyecta en `BenchmarkService` sin tocar el servicio ni el controlador.
- **Decisiones técnicas:**
  - **Un solo intercambio por pasada:** durante la búsqueda solo se guarda el índice del mínimo y se intercambia una vez al final, en lugar de intercambiar cada vez que aparece un valor menor. Así se hacen n − 1 intercambios en total.
  - **Comparación estricta `<`:** ante varios mínimos iguales se queda con el primero, lo que evita trabajo inútil, pero no hace el algoritmo estable (ver apartado 4).
  - **Misma estructura que InsertionSort y MergeSort:** un `sort(int[])` público para el paquete, que descarta arrays de menos de 2 elementos, y un `sort(array, from, to)` privado que ordena el tramo `[from, to)`.
  - Solo usa variables locales (`i`, `j`, `min`, `tmp`), por lo que la memoria adicional es constante.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | O(n²) | O(1) |
| Medio | O(n²) | O(1) |
| Peor | O(n²) | O(1) |

La búsqueda del mínimo no depende del orden de los datos: la pasada `i` siempre compara los n − 1 − i elementos restantes, así que se hacen exactamente n(n−1)/2 comparaciones en cualquier caso, incluso con el array ya ordenado. El número de intercambios es n − 1. A diferencia de InsertionSort, no tiene un mejor caso lineal, y con datos aleatorios hace aproximadamente el doble de comparaciones.

**No es estable:** el intercambio puede saltar por encima de elementos iguales. Por ejemplo, en `[2a, 2b, 1]` la primera pasada intercambia `2a` con `1` y queda `[1, 2b, 2a]`. Con `int` no se puede observar, porque dos enteros iguales son indistinguibles, así que los tests no lo comprueban.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** en cada una de las 5 ejecuciones se genera un array aleatorio nuevo.
- **Qué operaciones se incluyen/excluyen del cronómetro:** el contrato `Algorithm.run(size)` genera la entrada y la ordena, así que la generación del array (O(n)) queda dentro del tiempo medido, igual que en el resto de algoritmos de ordenación.
- **Reloj utilizado:** `System.nanoTime()` (en `BenchmarkService`).
- **Calentamiento de la JVM:** no se realiza.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de las 5 mediciones.
- **Consideraciones:** las primeras peticiones tras arrancar la JVM son más lentas porque el JIT aún no ha compilado el código. Con tamaños muy pequeños dominan los costes lineales y fijos, por lo que el crecimiento cuadrático se aprecia a partir de unos miles de elementos.

Resultados orientativos en local con `curl` (JVM ya caliente, tres tandas de peticiones). El cociente se calcula con la mediana de las tres tandas de cada tamaño:

| `size` | SelectionSort (ms) | Tamaño respecto al anterior | Tiempo respecto al anterior |
|---|---|---|---|
| 100 | 0,018 – 0,024 | — | — |
| 1 000 | 0,37 – 0,96 | ×10 | ×~25 |
| 5 000 | 6,35 – 6,48 | ×5 | ×~12 |
| 10 000 | 24,4 – 24,5 | ×2 | ×~3,9 |

De 5 000 a 10 000 el tiempo se multiplica por ~3,9, prácticamente el ×4 que corresponde a un algoritmo cuadrático, y estas medidas apenas varían entre tandas (menos de un 2 %). Por debajo de 1 ms las mediciones son mucho más ruidosas: con 1 000 elementos el tiempo osciló entre 0,37 y 0,96 ms de una tanda a otra, porque a esa escala pesan los costes fijos y lineales (generación del array, estado de la CPU y del JIT, recolector de basura). Por eso los cocientes en los que interviene 1 000 se alejan de los teóricos (×100 y ×25) y el crecimiento cuadrático debe valorarse con los tamaños grandes. Frente a [InsertionSort](insertionsort.md), que con 5 000 y 10 000 elementos tardó 2,4–2,5 ms y 8,4–8,5 ms, SelectionSort es entre 2,5 y 3 veces más lento con los mismos tamaños, ya que siempre hace n(n−1)/2 comparaciones.

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/SelectionSortAlgorithmTest.java`, `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java` y `src/test/java/um/practicascontinuas/benchmark/controller/BenchmarkControllerIntegrationTest.java`.
- **Casos comprobados:** nombre del algoritmo, array vacío y de un elemento, dos elementos, ya ordenado, orden inverso, duplicados, todos iguales, negativos y extremos (`Integer.MIN_VALUE`/`MAX_VALUE`), ordenación del mismo array, arrays aleatorios de tamaño par e impar con semilla fija (todos comparados con `Arrays.sort`), registro del algoritmo en el servicio de Spring y presencia en `GET /api/v1/algorithms`. Los tamaños de prueba se limitan a unos 5 000 elementos por ser O(n²).
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"selectionsort","size":1000}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.
