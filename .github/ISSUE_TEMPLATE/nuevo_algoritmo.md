---
name: Nuevo algoritmo
about: Proponer la implementación de un algoritmo por un colaborador
title: "[ALGO] "
labels: ["enhancement"]
---

## Algoritmo

- **Nombre canónico (valor de `algorithm`):**
- **Objetivo y comportamiento esperado:**
- **Familia:** ordenación / cálculo numérico / matrices / otra

## Contrato de entrada

- **Significado del parámetro `size`:**
- **Datos de entrada (tipo, rango y distribución):**
- **Resultado esperado y casos límite:**
- **Restricciones de tamaño:**

## Variante y complejidad

- **Variante o estrategia de implementación:**
- **Complejidad temporal esperada:**
- **Memoria adicional esperada:**

## Protocolo de medición

<!-- El boletín 0 exige 5 mediciones y mediana. Acordar con el grupo cómo
se generan las entradas y qué parte se cronometra para mantener comparabilidad. -->

- **Generación de entradas:**
- **Qué se incluye en el tiempo medido:**
- **Calentamiento previo, si se ha acordado:**

## Criterios de aceptación

- [ ] Implementa el contrato `Algorithm` vigente en el proyecto Java.
- [ ] Se incorpora al mecanismo de registro de algoritmos del servicio.
- [ ] Se ejecutan cinco mediciones y se devuelve su mediana según el contrato común.
- [ ] Incluye tests de corrección y casos límite.
- [ ] Incluye documentación `docs/<nombre-canonico>.md` basada en [la plantilla de algoritmos](../../docs/plantilla-algoritmo.md).
- [ ] No modifica unilateralmente el contrato REST compartido.
- [ ] `mvn clean verify` termina correctamente.

## Colaboración

<!-- Antes de implementar, pedir asignación a los responsables del repositorio.
Las contribuciones externas empezarán cuando estén preparadas las pipelines mínimas. -->
