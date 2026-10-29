# Arquitectura del microservicio Java

## Objetivo

Desarrollar una API REST que permita ejecutar algoritmos clásicos en Java y obtener su tiempo de ejecución.

## Tecnologías

- Java 21
- Spring Boot 3
- Maven

## Organización propuesta

Paquete base: `um.practicascontinuas.benchmark`

```text
benchmark/
├── BenchmarkApplication.java
├── controller/
├── dto/
├── service/
├── algorithm/
└── exception/
```

### Responsabilidades

| Paquete      | Responsabilidad                                     |
| ------------ | --------------------------------------------------- |
| `controller` | Recibir solicitudes HTTP y delegar en los servicios |
| `dto`        | Definir los objetos de entrada y salida             |
| `service`    | Coordinar las ejecuciones, mediciones y resultados  |
| `algorithm`  | Implementar los algoritmos clásicos                 |
| `exception`  | Centralizar el tratamiento de errores               |

La organización puede ajustarse durante el desarrollo.

## Protocolo de benchmarking

Según el boletín 0:

- El primer algoritmo implementado será MergeSort.
- Las entradas de los algoritmos de ordenación se generarán aleatoriamente.
- Se realizarán cinco mediciones.
- Se devolverá la mediana de los tiempos medidos.

El contrato REST definitivo, los errores y los detalles adicionales del protocolo deberán acordarse con los demás grupos.

## Reparto de responsabilidades

- Integrante A: inicialización del proyecto, configuración del repositorio y documentación de arquitectura.
- Integrante B: algoritmos, ejecución y medición del rendimiento.
- Integrante C: pruebas unitarias y de integración.
- Integrante D: Docker y automatización CI/CD.

## Estado

La arquitectura está documentada, pero las capas funcionales todavía están pendientes de implementación.
