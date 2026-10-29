# Microservicio Java — Proyecto Global de Prácticas Continuas

Microservicio REST desarrollado con Java y Spring Boot para medir los tiempos de ejecución de algoritmos clásicos y permitir comparaciones de rendimiento entre diferentes lenguajes de programación.

Proyecto desarrollado para la asignatura **Prácticas Continuas del Máster en Ingeniería del Software (Universidad de Murcia)**.

## 1. Tecnologías

- Java 21
- Spring Boot 3
- Maven
- Spring Boot Actuator
- JUnit 5
- Spotless (Google Java Format)
- Git y GitHub

## 2. Requisitos previos

- JDK 21
- Maven
- Git

Comprobar la instalación:

```bash
java -version
mvn -version
git --version
```

## 3. Instalación

Clonar el repositorio:

```bash
git clone https://github.com/Barlowe90/pc-proyecto-global-servicio-java.git
cd pc-proyecto-global-servicio-java
```

## 4. Compilación y ejecución

Compilar y ejecutar los tests:

```bash
mvn clean verify
```

Arrancar la aplicación:

```bash
mvn spring-boot:run
```

Por defecto, la aplicación utiliza el puerto `8080`.

También puede generarse y ejecutarse el JAR:

```bash
mvn clean package
java -jar target/benchmark-0.0.1-SNAPSHOT.jar
```

## 5. Comprobación de funcionamiento

Con la aplicación arrancada:

```bash
curl http://localhost:8080/actuator/health
```

Respuesta esperada:

```json
{ "status": "UP" }
```

## 6. Formato del código

El proyecto utiliza Spotless con Google Java Format.

Comprobar el formato:

```bash
mvn spotless:check
```

Aplicar el formato:

```bash
mvn spotless:apply
```

## 7. Hooks de Git

Después de clonar el repositorio, activar los hooks:

```bash
git config core.hooksPath .githooks
```

El hook pre-commit ejecuta Spotless antes de confirmar cambios.

## 8. Arquitectura

La estructura de paquetes y sus responsabilidades están documentadas en `docs/arquitectura.md`.

El microservicio seguirá una separación entre capa REST, lógica de benchmarking y algoritmos.

## 9. Estado del proyecto

**Fase actual: inicialización del microservicio.**

- Proyecto Spring Boot inicializado.
- Configuración Maven preparada.
- Endpoint de salud disponible.
- Arquitectura definida a nivel de documentación.
- Implementación de algoritmos pendiente.
- Tests funcionales y de integración pendientes.
- Docker y pipelines CI/CD pendientes.

## 10. Colaboraciones

Las contribuciones deberán seguir las instrucciones de `CONTRIBUTING.md` cuando dicho archivo quede incorporado a `main`.

Todas las modificaciones de la rama principal se realizarán mediante Pull Requests.

No se abrirán issues para colaboraciones externas hasta disponer de las pipelines mínimas requeridas.

## 11. Contrato REST

El contrato de entrada y salida deberá consensuarse con los demás grupos, conforme al boletín 0.

El servicio implementará inicialmente MergeSort, con cinco mediciones por ejecución y cálculo de la mediana.
