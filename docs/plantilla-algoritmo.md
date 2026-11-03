# Nombre del algoritmo

<!-- Copiar como docs/<nombre-canonico>.md y sustituir los apartados de ejemplo. -->

## 1. Resumen

<!-- Qué problema resuelve el algoritmo y por qué se incluye en el microservicio. -->

## 2. Contrato

- **Nombre canónico:** `nombre` (valor del campo `algorithm`).
- **Significado de `size`:** <!-- Número de elementos, dimensión de matriz, etc. -->
- **Tipo y distribución de los datos:** <!-- Tipo, rango y aleatoriedad si procede. -->
- **Resultado esperado:** <!-- Qué produce o modifica el algoritmo. -->
- **Casos límite y restricciones:** <!-- Valores especiales y límites soportados. -->

## 3. Funcionamiento e implementación

<!-- Explicar de forma breve la estrategia empleada y su variante concreta. -->

- **Clase Java:** `src/main/java/um/practicascontinuas/benchmark/algorithm/<Nombre>Algorithm.java` (adaptar a la ubicación real).
- **Registro en el servicio:** <!-- Explicar cómo se descubre o registra la implementación. -->
- **Decisiones técnicas:** <!-- Uso de buffers, recursividad, estructuras auxiliares, etc. -->

## 4. Complejidad

| Caso | Complejidad temporal | Memoria adicional |
|---|---|---|
| Mejor | Pendiente | Pendiente |
| Medio | Pendiente | Pendiente |
| Peor | Pendiente | Pendiente |

<!-- Justificar las complejidades y las propiedades relevantes, como estabilidad o uso de memoria. -->

## 5. Protocolo de benchmarking

El boletín 0 establece **cinco mediciones** y la **mediana** como resultado. Para algoritmos de ordenación, las entradas se generan aleatoriamente.

Documentar los demás detalles de forma explícita y conforme al acuerdo entre grupos:

- **Generación de datos de cada medición:**
- **Qué operaciones se incluyen/excluyen del cronómetro:**
- **Reloj utilizado:**
- **Calentamiento de la JVM (si se ha acordado):**
- **Unidad devuelta:** milisegundos (`timeMs`).
- **Consideraciones sobre reproducibilidad y comparación entre lenguajes:**

## 6. Pruebas

<!-- Enlazar los tests Java correspondientes y resumir los casos cubiertos:
entradas pequeñas, datos aleatorios, duplicados, errores y otros relevantes. -->

- **Archivos de tests:**
- **Casos comprobados:**
- **Cómo ejecutarlos:** `mvn clean verify`.

## 7. Ejemplo de uso

<!-- Actualizar el nombre del algoritmo conforme a su implementación y al contrato REST acordado. -->

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"nombre","size":1000}'
```

El resultado, cuando el algoritmo esté disponible, debe respetar el contrato común:
`language`, `algorithm`, `size`, `timeMs` y `runs`.

## 8. Referencias (opcional)

<!-- Enlaces a material consultado o decisiones compartidas entre grupos. -->
