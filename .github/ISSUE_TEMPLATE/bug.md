---
name: Informe de error
about: Notificar un fallo reproducible en el microservicio Java
title: "[BUG] "
labels: ["bug"]
---

## Descripción del problema

<!-- ¿Qué falla? Explica el comportamiento observado sin incluir información sensible. -->

## Pasos para reproducirlo

1.
2.
3.

## Resultado esperado

<!-- ¿Qué debería ocurrir? -->

## Resultado actual

<!-- Código HTTP, respuesta JSON o resultado obtenido. -->

## Petición de ejemplo (si aplica)

```bash
curl -i -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"mergesort","size":1000}'
```

## Entorno

- Java (`java -version`):
- Ejecución: local / Docker / imagen publicada
- Commit o versión (`git rev-parse --short HEAD`):
- Sistema operativo:

## Logs o evidencias

<!-- Añade fragmentos relevantes, eliminando contraseñas, tokens y otros datos sensibles. -->
