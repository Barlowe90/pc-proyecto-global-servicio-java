# Microservicio Java — Proyecto Global de Prácticas Continuas

[![CI](https://github.com/Barlowe90/pc-proyecto-global-servicio-java/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/Barlowe90/pc-proyecto-global-servicio-java/actions/workflows/ci.yml)

Microservicio REST desarrollado en **Java 21 y Spring Boot 3** para ejecutar algoritmos y medir su rendimiento. Forma parte del proyecto global de la asignatura **Prácticas Continuas** del Máster en Ingeniería del Software de la Universidad de Murcia, en el que distintos equipos implementan una API común en diferentes lenguajes.

El primer algoritmo disponible es **MergeSort**.

## 1. Tecnologías y requisitos

- Java 21 y Spring Boot 3
- Maven
- Spring Boot Actuator (estado de salud)
- JUnit 5 y Spring Boot Test / MockMvc
- Spotless con Google Java Format
- Docker y Docker Compose (opcional para desarrollo local)
- Git y GitHub Actions (CI/CD)

Para ejecutar el proyecto desde el código fuente necesitas **JDK 21**, **Maven** y **Git**. Si utilizas contenedores, basta con disponer de **Docker** y **Docker Compose**.

Comprobar el entorno:

```bash
java -version
mvn -version
git --version
docker compose version
```

## 2. Instalación y ejecución local

Clonar el repositorio:

```bash
git clone https://github.com/Barlowe90/pc-proyecto-global-servicio-java.git
cd pc-proyecto-global-servicio-java
```

Compilar y ejecutar las pruebas:

```bash
mvn clean verify
```

Arrancar el servidor:

```bash
mvn spring-boot:run
```

La API escucha en **http://localhost:8080**.

También puedes generar y ejecutar el JAR:

```bash
mvn clean package
java -jar target/benchmark-0.0.1-SNAPSHOT.jar
```

## 3. API REST

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/actuator/health` | Comprueba el estado de la aplicación |
| `GET` | `/api/v1/algorithms` | Lista los algoritmos disponibles |
| `POST` | `/api/v1/benchmark` | Ejecuta un algoritmo y devuelve el tiempo medido |

### Estado de salud

```bash
curl http://localhost:8080/actuator/health
```

Respuesta:

```json
{"status":"UP"}
```

### Algoritmos disponibles

```bash
curl http://localhost:8080/api/v1/algorithms
```

Respuesta con los algoritmos implementados actualmente:

```json
{"algorithms":["mergesort"]}
```

La lista se genera a partir de las implementaciones `Algorithm` registradas como componentes de Spring.

### Ejecutar un benchmark

```bash
curl -X POST http://localhost:8080/api/v1/benchmark \
  -H "Content-Type: application/json" \
  -d '{"algorithm":"mergesort","size":10000}'
```

Ejemplo de respuesta (**el tiempo es ilustrativo y depende del equipo**):

```json
{
  "language": "java",
  "algorithm": "mergesort",
  "size": 10000,
  "timeMs": 2.34,
  "runs": 5
}
```

| Campo | Significado |
| --- | --- |
| `language` | Lenguaje de implementación: `java` |
| `algorithm` | Nombre canónico del algoritmo ejecutado |
| `size` | Tamaño de la entrada solicitada |
| `timeMs` | Mediana de los tiempos medidos, en milisegundos |
| `runs` | Número de ejecuciones medidas: `5` |

**Validación actual:** `algorithm` es obligatorio y debe corresponder a un algoritmo registrado; `size` debe ser un entero entre **1 y 1 000 000**, ambos inclusive.

Las entradas inválidas devuelven **HTTP 400**; un algoritmo desconocido devuelve **HTTP 404**. Los errores se expresan en JSON mediante el manejador global de excepciones. Los errores internos no exponen sus detalles al cliente.

### Protocolo de medición actual

`BenchmarkService` realiza **cinco ejecuciones**, cronometra cada llamada con `System.nanoTime()` y devuelve la **mediana** convertida a milisegundos.

En la implementación actual de MergeSort, `Algorithm.run(size)` genera un array aleatorio y lo ordena. Por tanto, **la generación de la entrada está incluida en el intervalo medido**. No se realiza una ejecución de calentamiento separada. Cualquier cambio de este protocolo para mejorar la comparabilidad con otros lenguajes debe acordarse entre los equipos.

## 4. Docker y Docker Compose

El proyecto incluye un **Dockerfile multi-stage**: compila con Maven y Java 21 y ejecuta el JAR sobre una imagen JRE 21 con un usuario sin privilegios. La imagen incorpora un `HEALTHCHECK` basado en `/actuator/health`.

### Construir y ejecutar una imagen local

```bash
docker build -t benchmark-java .
docker run --rm -p 8080:8080 benchmark-java
```

### Ejecutar con Docker Compose

```bash
docker compose up -d --build
docker compose ps
```

Para comprobar el servicio:

```bash
curl http://localhost:8080/actuator/health
```

Para detenerlo:

```bash
docker compose down
```

Si el puerto 8080 está ocupado, cambia el mapeo de puertos antes de arrancar.

## 5. CI/CD y GitHub Container Registry

Los workflows se encuentran en `.github/workflows/`:

| Workflow | Cuándo se ejecuta | Función |
| --- | --- | --- |
| `ci.yml` | Push a `main`, cada PR o ejecución manual | Comprueba formato (`lint`), ejecuta tests (`test`), genera el JAR (`package`) y lo conserva como artefacto |
| `ia.yml` | Pull requests | Genera un comentario con un resumen de cambios mediante IA; necesita el secreto `OLLAMA_API_KEY` |
| `release.yml` | Push a `main` y tags `v*.*.*` | Construye la imagen Docker y la publica en GitHub Container Registry (GHCR) |

La imagen se identifica como:

```text
ghcr.io/barlowe90/pc-proyecto-global-servicio-java
```

El workflow de publicación utiliza `GITHUB_TOKEN` con permiso `packages: write`. Los pushes a `main` generan las etiquetas `latest` y `sha-<commit>`; las versiones etiquetadas (por ejemplo, `v1.0.0`) generan `1.0.0`, `1.0` y `1`, además de la etiqueta del commit.

Cuando la imagen esté disponible y tengas permiso para acceder al paquete:

```bash
docker pull ghcr.io/barlowe90/pc-proyecto-global-servicio-java:latest
docker run --rm -p 8080:8080 ghcr.io/barlowe90/pc-proyecto-global-servicio-java:latest
```

**Nota:** si el paquete GHCR se publica con visibilidad privada, será necesario autenticarse antes de descargarlo.

### Versionado

Para crear una nueva versión a partir de un commit de `main`:

```bash
git switch main
git pull --ff-only origin main
git tag v1.0.0
git push origin v1.0.0
```

Usa una etiqueta que aún no exista y que corresponda a la versión que el equipo haya acordado publicar.

## 6. Tests, formato y hooks

Los tests unitarios y de integración utilizan **JUnit 5**, **Spring Boot Test** y **MockMvc**. Incluyen comprobaciones del algoritmo, servicio, gestión de errores y respuestas HTTP de los endpoints.

```bash
mvn clean verify
mvn spotless:check
```

Aplicar el formato automáticamente:

```bash
mvn spotless:apply
```

Activar los hooks versionados tras clonar el repositorio:

```bash
git config core.hooksPath .githooks
chmod +x .githooks/pre-commit
```

El hook `pre-commit` ejecuta Spotless antes de confirmar cambios. La CI vuelve a verificar los controles aunque se omita el hook local.

## 7. Arquitectura y documentación

Paquete base: `um.practicascontinuas.benchmark`.

```text
src/main/java/um/practicascontinuas/benchmark/
├── BenchmarkApplication.java
├── algorithm/     # Contrato Algorithm y algoritmos (MergeSort)
├── controller/    # Endpoints HTTP
├── dto/           # Contratos de entrada y salida
├── exception/     # Excepciones y respuestas de error
└── service/       # Validación y medición del rendimiento
```

Documentación adicional:

- [Arquitectura](docs/arquitectura.md)
- [Algoritmo MergeSort](docs/mergesort.md)
- [Plantilla para documentar nuevos algoritmos](docs/plantilla-algoritmo.md)

## 8. Colaboración

Todas las contribuciones deben seguir [CONTRIBUTING.md](CONTRIBUTING.md). Los cambios se incorporan mediante Pull Requests; `main` está protegida y requiere revisión de CODEOWNERS y que pasen los checks obligatorios de `lint`, `test` y `package`.

Para proponer una nueva funcionalidad o algoritmo, crea o consulta un issue utilizando las [plantillas disponibles](.github/ISSUE_TEMPLATE/) y coordina su asignación con el equipo antes de empezar.

Cada algoritmo nuevo debe incluir implementación, tests y documentación conforme a la [plantilla de algoritmos](docs/plantilla-algoritmo.md), respetando el contrato REST común entre grupos.

El proyecto sigue **Conventional Commits** para los mensajes de commit y títulos de cambios.
