# Guía de contribución

Gracias por tu interés en contribuir al microservicio Java del proyecto global de Prácticas Continuas.

Este proyecto implementa una API REST con Java y Spring Boot para medir el rendimiento de algoritmos clásicos.

## 1. Requisitos

Para desarrollar localmente se necesita:

- JDK 21.
- Maven.
- Git.
- Un editor compatible con Java.

## 2. Preparación del entorno

Clonar el repositorio:

```bash
git clone https://github.com/barlowe90/pc-proyecto-global-servicio-java.git
cd pc-proyecto-global-servicio-java
```

Una vez inicializado el proyecto Spring Boot, se podrá compilar y ejecutar los tests con:

```bash
mvn clean verify
```

## 3. Flujo de trabajo

Se seguirá GitHub Flow:

1. Partir de la última versión de `main`.
2. Crear una rama para el cambio.
3. Implementar la funcionalidad.
4. Añadir o actualizar los tests correspondientes.
5. Crear commits siguiendo Conventional Commits.
6. Subir la rama y abrir una Pull Request.
7. Solicitar revisión a los propietarios del repositorio.
8. Incorporar los cambios solicitados.
9. Fusionar la PR únicamente cuando se cumplan las reglas de protección.

Ejemplo de creación de rama:

```bash
git switch main
git pull origin main
git switch -c feat/nombre-funcionalidad
```

## 4. Convenciones de ramas

- `feat/`: nuevas funcionalidades.
- `fix/`: correcciones de errores.
- `test/`: pruebas.
- `docs/`: documentación.
- `chore/`: configuración y mantenimiento.
- `ci/`: integración continua.

## 5. Convenciones de commits

Se utilizarán Conventional Commits.

Ejemplos:

```text
feat(api): añade endpoint de benchmark
fix(service): corrige validación del tamaño
test(service): añade pruebas de MergeSort
docs(readme): documenta ejecución local
ci(actions): configura pipeline de integración continua
```

Cada commit debe identificar correctamente a su autor.

## 6. Pull Requests

Toda modificación de `main` debe realizarse mediante Pull Request.

Las PRs deben:

- Describir los cambios realizados.
- Indicar cómo se han probado.
- Incluir tests cuando corresponda.
- Recibir al menos una aprobación válida de un propietario del repositorio.
- Superar los controles automáticos de CI cuando estén configurados.
- Resolver las conversaciones de revisión pendientes.

No se permiten pushes directos ni force pushes a `main`.

## 7. Formato del código

El proyecto utilizará Spotless con Google Java Format, siguiendo las prácticas de la asignatura.

Una vez configurado el plugin:

```bash
mvn spotless:apply
mvn spotless:check
```

Los colaboradores también deberán activar los hooks de Git cuando estén disponibles:

```bash
git config core.hooksPath .githooks
```

## 8. Contribuciones externas

El repositorio aceptará contribuciones de otros grupos cuando estén implementadas las pipelines mínimas necesarias.

Los colaboradores externos podrán trabajar mediante forks y Pull Requests.

Los nuevos algoritmos deberán cumplir el contrato REST consensuado entre los grupos y disponer de pruebas unitarias.

## 9. Estrategia de integración

La estrategia de merge será definida por el equipo y documentada antes de integrar las primeras Pull Requests.
