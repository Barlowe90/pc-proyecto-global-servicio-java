# Algoritmos propuestos para contribuciones externas

Este documento recoge las dos propuestas de algoritmos abiertas para que estudiantes de otros equipos contribuyan al microservicio Java. **No están implementadas por este cambio**.

| Algoritmo | Issue | Descripción |
| --- | --- | --- |
| QuickSort | [#14 — QuickSort](https://github.com/Barlowe90/pc-proyecto-global-servicio-java/issues/14) | Ordenación in situ con particionado; complejidad esperada O(n log n), peor caso O(n²) |
| InsertionSort | [#15 — InsertionSort](https://github.com/Barlowe90/pc-proyecto-global-servicio-java/issues/15) | Ordenación estable e in situ; mejor caso O(n), caso medio y peor O(n²) |

## Cómo contribuir

1. Abrir el issue correspondiente, comentar el interés en implementarlo y **esperar a que un miembro del equipo lo asigne**.
2. Crear una rama de trabajo desde `main` o un fork si no se dispone de permisos de escritura.
3. Implementar el algoritmo siguiendo la interfaz `Algorithm` vigente y registrarlo como componente de Spring.
4. Añadir tests de casos límite y comparar el resultado de la ordenación con `Arrays.sort`.
5. Documentar el algoritmo en `docs/<nombre>.md` siguiendo [la plantilla](plantilla-algoritmo.md).
6. Comprobar `mvn spotless:check` y `mvn clean verify`; probar los endpoints REST y aportar resultados de dos o tres tamaños de benchmark.
7. Abrir una Pull Request enlazando el issue mediante `Closes #14` o `Closes #15`.

Consultar [CONTRIBUTING.md](../CONTRIBUTING.md) para el flujo de revisión y las normas del repositorio.

## Contrato y protocolo compartido

- `POST /api/v1/benchmark`: petición `{"algorithm":"quicksort","size":1000}` (o `insertionsort`).
- `GET /api/v1/algorithms`: debe incluir el nombre del algoritmo una vez incorporado.
- Cada benchmark ejecuta **cinco mediciones y devuelve la mediana en milisegundos**.
- El protocolo detallado de generación de entradas y calentamiento debe seguir el acuerdo vigente entre equipos. La contribución de un algoritmo no debe modificar unilateralmente la API compartida.

**Los requisitos completos, los criterios de aceptación y la coordinación de asignaciones se mantienen en los propios issues #14 y #15.**
