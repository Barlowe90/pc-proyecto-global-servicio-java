# InsertionSort

## 1. Resumen

Algoritmo de ordenación por inserción. Recorre el array de izquierda a derecha e inserta cada elemento en su posición dentro del prefijo ya ordenado, desplazando a la derecha los elementos mayores. Es uno de los algoritmos propuestos en el boletín 0 y sirve de contraste cuadrático frente a MergeSort y QuickSort, que son O(n log n).

## 2. Contrato

- **Nombre canónico:** `insertionsort` (valor del campo `algorithm`).
- **Significado de `size`:** número de elementos del array a ordenar.
- **Tipo y distribución de los datos:** `int` de 32 bits uniformemente aleatorios en todo el rango (`ThreadLocalRandom.ints`), con posibles negativos y duplicados, igual que en MergeSort y QuickSort.
- **Resultado esperado:** el mismo array queda ordenado de forma ascendente, in situ y de forma estable, sin crear otro array.
- **Casos límite y restricciones:** `size` entre 1 y 1 000 000 (validado por `BenchmarkService`). La implementación también acepta arrays vacíos o de un elemento, que no se modifican. Al ser O(n²), conviene usar tamaños pequeños o moderados (hasta unos pocos miles o decenas de miles): cerca del máximo una petición tardaría minutos.

## 3. Funcionamiento e implementación

Variante iterativa clásica con desplazamientos: para cada posición `i` se guarda su valor, se desplazan una posición a la derecha los elementos anteriores que son mayores y se coloca el valor en el hueco que queda.

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/InsertionSortAlgorithm.java`.
- **Registro en el servicio:** la clase es un `@Component` que implementa `Algorithm`; Spring la inyecta en `BenchmarkService` sin tocar el servicio ni el controlador.
- **Decisiones técnicas:**
  - **Desplazamientos en lugar de intercambios:** cada elemento mayor se copia una vez a la derecha y el valor se escribe una sola vez al final, en vez de hacer un intercambio en cada paso.
  - **Comparación estricta `>`:** un elemento solo se desplaza si es mayor que el que se inserta, así que los iguales conservan su orden relativo y el algoritmo es estable.
  - **Misma estructura que MergeSort:** un `sort(int[])` público para el paquete, que descarta arrays de menos de 2 elementos, y un `sort(array, from, to)` privado que ordena el tramo `[from, to)`.
  - Solo usa variables locales (`value`, `i`, `j`), por lo que la memoria adicional es constante.

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | O(n) | O(1) |
| Medio | O(n²) | O(1) |
| Peor | O(n²) | O(1) |

El mejor caso es un array ya ordenado: cada elemento se compara una vez con su anterior y no se desplaza nada. El peor es un array en orden inverso, donde cada elemento recorre todo el prefijo (n(n−1)/2 desplazamientos). Con datos aleatorios cada elemento recorre de media la mitad del prefijo, por lo que el caso medio también es cuadrático. Es estable e in situ.

## 5. Protocolo de benchmarking

- **Generación de datos de cada medición:** en cada una de las 5 ejecuciones se genera un array aleatorio nuevo.
- **Qué operaciones se incluyen/excluyen del cronómetro:** el contrato `Algorithm.run(size)` genera la entrada y la ordena, así que la generación del array (O(n)) queda dentro del tiempo medido, igual que en MergeSort y QuickSort.
- **Reloj utilizado:** `System.nanoTime()` (en `BenchmarkService`).
- **Calentamiento de la JVM:** no se realiza.
- **Unidad devuelta:** milisegundos (`timeMs`), mediana de las 5 mediciones.
- **Consideraciones:** las primeras peticiones tras arrancar la JVM son más lentas porque el JIT aún no ha compilado el código. Con tamaños pequeños dominan los costes lineales (generación del array) y fijos, por lo que el crecimiento cuadrático solo se aprecia a partir de unos miles de elementos.

Resultados orientativos en local (JVM ya caliente, tres tandas de peticiones):

| `size` | InsertionSort (ms) | Tamaño respecto al anterior | Tiempo respecto al anterior |
|---|---|---|---|
| 100 | 0,007 – 0,010 | — | — |
| 1 000 | 0,10 – 0,11 | ×10 | ×~11 |
| 5 000 | 2,39 – 2,50 | ×5 | ×~24 |
| 10 000 | 8,44 – 8,55 | ×2 | ×~3,5 |

De 1 000 a 5 000 y de 5 000 a 10 000 el tiempo crece aproximadamente con el cuadrado del tamaño (×25 y ×4 esperados). Entre 100 y 1 000 el aumento es menor porque el término n² todavía no domina. Como referencia, con `size` 10 000 QuickSort y MergeSort tardaron en torno a 1,2–1,3 ms (ver [QuickSort](quicksort.md)), y la diferencia aumenta rápidamente con `n`.

## 6. Pruebas

- **Archivos de tests:** `src/test/java/um/practicascontinuas/benchmark/algorithm/InsertionSortAlgorithmTest.java`, `src/test/java/um/practicascontinuas/benchmark/service/BenchmarkServiceIntegrationTest.java` y `src/test/java/um/practicascontinuas/benchmark/controller/BenchmarkControllerIntegrationTest.java`.
- **Casos comprobados:** nombre del algoritmo, array vacío y de un elemento, dos elementos, ya ordenado, orden inverso, duplicados, todos iguales, negativos y extremos (`Integer.MIN_VALUE`/`MAX_VALUE`), ordenación del mismo array, arrays aleatorios de tamaño par e impar con semilla fija (todos comparados con `Arrays.sort`), registro del algoritmo en el servicio de Spring y presencia en `GET /api/v1/algorithms`. Los tamaños de prueba se limitan a unos 5 000 elementos por ser O(n²). La estabilidad no se puede observar con `int`, ya que dos enteros iguales son indistinguibles; queda garantizada por la comparación estricta `>`.
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"insertionsort","size":1000}'
```

El resultado respeta el contrato común: `language`, `algorithm`, `size`, `timeMs` y `runs`.
