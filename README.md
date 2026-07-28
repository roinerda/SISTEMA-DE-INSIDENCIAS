# HelpDesk Flow

![CI](https://github.com/roinerda/SISTEMA-DE-INSIDENCIAS/actions/workflows/ci.yml/badge.svg?branch=main)

Sistema de registro, priorización, atención, validación y cierre de incidencias técnicas.
Trabajo del curso **ITI-822 Metodologías Ágiles de Desarrollo de Software**, Universidad Técnica Nacional.

- **Repositorio:** https://github.com/roinerda/SISTEMA-DE-INSIDENCIAS
- **Tablero Kanban:** https://github.com/users/roinerda/projects/1

---

## Integrantes

| Nombre  | Usuario de GitHub | Rol principal |
|---|---|---|
| Roiner | [roinerda](https://github.com/roinerda) | Dominio, transiciones, CI, EXPEDITE |
| Brandon Campos  | [BrandonCampos](https://github.com/BrandonCampos) | Prioridad, consultas, métricas, consola |

Ambos integrantes participaron en todas las etapas alternando los roles de *driver* y *navigator*
según la práctica de Ping-Pong TDD: un integrante escribe la prueba que falla y el otro implementa
lo mínimo para hacerla pasar.

---

## Descripción del sistema

HelpDesk Flow permite registrar incidencias técnicas, calcular su prioridad de forma automática a
partir del impacto y la urgencia, hacerlas avanzar por un flujo de estados controlado, consultarlas
mediante filtros y obtener métricas básicas de flujo (throughput y lead time).

Incorpora además la clase de servicio **EXPEDITE**, que permite atender una incidencia crítica de
forma prioritaria bajo una política de cupo único: solo una incidencia EXPEDITE puede estar en
desarrollo o validación de forma simultánea.

Es una aplicación de consola escrita en Java, con la lógica de negocio separada de la interfaz y
cubierta por pruebas automatizadas.

### Historias implementadas

| ID | Historia | Estado |
|---|---|---|
| HU-01 | Registrar una incidencia con validaciones | Completada |
| HU-02 | Cálculo automático de prioridad | Completada |
| HU-03 | Gestión del flujo de estados | Completada |
| HU-04 | Consulta y filtrado de incidencias | Completada |
| HU-05 | Métricas básicas de flujo | Completada |
| HU-06 | Clase de servicio EXPEDITE (cambio de requerimiento) | Completada |

---

## Requisitos de ejecución

| Herramienta | Versión |
|---|---|
| JDK | 17 o superior (desarrollado con Temurin 21 LTS) |
| Apache Maven | 3.9 o superior |
| Git | cualquiera reciente |

El proyecto se compila apuntando a Java 17 (`maven.compiler.release`), de modo que el `.jar`
generado se ejecuta en cualquier máquina con JDK 17 o superior.

Verificación rápida del entorno:

```bash
java -version
mvn -v
```

---

## Compilar

```bash
mvn clean compile
```

Generar el ejecutable:

```bash
mvn clean package
```

Ejecutar la aplicación:

```bash
java -jar target/helpdesk-flow.jar
```

Al arrancar se muestra un menú de consola con las operaciones del sistema: registrar, listar, buscar,
filtrar, avanzar estado, finalizar, marcar EXPEDITE y ver métricas.

---

## Ejecutar las pruebas

```bash
mvn test
```

Ejecutar una sola clase de pruebas:

```bash
mvn test -Dtest=IncidenciaTest
```

Los reportes quedan en `target/surefire-reports/`.

---

## Estructura del repositorio

```
src/main/java/cr/utn/helpdesk/     código fuente (equivale a /src)
├── domain/                        entidades y enumeraciones
├── service/                       reglas de negocio
└── ui/                            interfaz de consola
src/test/java/cr/utn/helpdesk/     pruebas automatizadas (equivale a /tests)
.github/workflows/ci.yml           integración continua
```

> El enunciado solicita las carpetas `/src` y `/tests`. Se utiliza la estructura estándar de Maven
> (`src/main/java` y `src/test/java`), que separa fuentes y pruebas exactamente con ese propósito y
> es la que espera la herramienta de construcción.

---

## Decisiones principales de diseño

1. **La prioridad se calcula en el constructor de `Incidencia`.** El enunciado pide que el sistema
   calcule la prioridad para evitar decisiones arbitrarias. Al calcularla en el constructor, y no
   ofrecer ningún método para asignarla, resulta imposible fijarla manualmente o crear una incidencia
   con prioridad incoherente.

2. **Las validaciones viven en el constructor.** No existe un instante en que haya una incidencia en
   estado inválido. La alternativa —un método `validar()` separado— permitiría construir el objeto
   roto y olvidarse de invocarlo.

3. **Impacto y urgencia se validan solo contra `null`.** Al ser enumeraciones, el compilador impide
   cualquier valor fuera del conjunto declarado; el único caso inválido posible en ejecución es la
   referencia nula.

4. **La incidencia gobierna su propio ciclo de vida (HU-03).** Los métodos `avanzarA` y `finalizar`
   viven en `Incidencia`. `avanzarA` solo permite el estado consecutivo y nunca FINALIZADA: ese estado
   se alcanza únicamente con `finalizar`, que exige la descripción de la solución, haciendo imposible
   por diseño cerrar una incidencia sin solución.

5. **La regla de EXPEDITE está dividida según la información que necesita (HU-06).** "Solo una crítica
   puede ser EXPEDITE" vive en `Incidencia`, que conoce su prioridad. "Solo una EXPEDITE activa a la
   vez" vive en `GestorExpedite`, porque es una regla sobre el conjunto de incidencias. El gestor
   verifica el cupo y delega la transición en el `avanzarA` ya probado, sin reimplementarlo.

6. **La consola no contiene lógica de negocio.** `ConsolaHelpDesk` (paquete `ui`) solo lee entrada,
   la valida mínimamente e invoca a los servicios. Toda la lógica probable está en `domain` y
   `service`, cubierta por las pruebas automatizadas.

---

## Estado de la integración continua

El pipeline se ejecuta en cada `push` a `main` y en cada *pull request*. Compila el proyecto, ejecuta
la totalidad de las pruebas y falla si alguna no pasa. El estado actual de `main` es el que muestra el
badge en la parte superior de este documento. Los reportes de JUnit quedan disponibles como artefacto
descargable en cada ejecución, en la pestaña Actions del repositorio.

---

## Tablero Kanban

Enlace: https://github.com/users/roinerda/projects/1

| Columna | Límite WIP |
|---|---|
| Opciones/Backlog | sin límite |
| Preparado | 3 |
| En desarrollo | 1 |
| Validación | 1 |
| Hecho | sin límite |

Las tarjetas del tablero son los *issues* del repositorio. Cada una contiene sus criterios de
aceptación verificables y su definición de hecho.

---

## Documentos relacionados

| Archivo | Contenido |
|---|---|
| `IA-LOG.md` | Bitácora de interacciones con herramientas de IA |
| `RETROSPECTIVA.md` | Retrospectiva de la pareja |
| `REFACTORING.md` | Refactorizaciones realizadas y pruebas que las protegieron |
| `CONTRIBUTING.md` | Convenciones de ramas, commits y revisión |
