# HelpDesk Flow

![CI](https://github.com/roinerda/SISTEMA-DE-INSIDENCIAS/actions/workflows/ci.yml/badge.svg?branch=main)

Sistema de registro, priorización, atención, validación y cierre de incidencias técnicas.
Trabajo del curso **ITI-822 Metodologías Ágiles de Desarrollo de Software**, Universidad Técnica Nacional.

---

## Integrantes

| Nombre | Carné  Usuario de GitHub | Rol principal |
|---|---|---|
| Roiner | [roinerda](https://github.com/roinerda) | Dominio, transiciones, CI, EXPEDITE |
| Brandon Campos | [BrandonCampos](https://github.com/BrandonCampos) | Prioridad, consultas, métricas, consola |

Ambos integrantes participaron en todas las etapas alternando los roles de *driver* y *navigator*
según la práctica de Ping-Pong TDD: un integrante escribe la prueba que falla y el otro implementa
lo mínimo para hacerla pasar.

---

## Descripción del sistema

HelpDesk Flow permite registrar incidencias técnicas, calcular su prioridad de forma automática a
partir del impacto y la urgencia, hacerlas avanzar por un flujo de estados controlado, consultarlas
mediante filtros y obtener métricas básicas de flujo (throughput y lead time).

Incorpora además la clase de servicio **EXPEDITE**, que permite atender una incidencia crítica de
forma prioritaria bajo una política de cupo único.

### Historias implementadas

| ID | Historia | Estado |
|---|---|---|
| HU-01 | Registrar una incidencia con validaciones | En progreso |
| HU-02 | Cálculo automático de prioridad | Pendiente |
| HU-03 | Gestión del flujo de estados | Pendiente |
| HU-04 | Consulta y filtrado de incidencias | Pendiente |
| HU-05 | Métricas básicas de flujo | Pendiente |
| HU-06 | Clase de servicio EXPEDITE (cambio de requerimiento) | Pendiente |

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
├── repository/                    persistencia
└── ui/                            interfaz de consola
src/test/java/cr/utn/helpdesk/     pruebas automatizadas (equivale a /tests)
.github/workflows/ci.yml           integración continua
```

> El enunciado solicita las carpetas `/src` y `/tests`. Se utiliza la estructura estándar de Maven
> (`src/main/java` y `src/test/java`), que separa fuentes y pruebas exactamente con ese propósito y
> es la que espera la herramienta de construcción.

---

## Decisiones principales de diseño

1. **El cálculo de prioridad no vive en la entidad.** `CalculadoraPrioridad` es una clase sin estado.
   La prioridad es una regla de negocio que cambió durante el proyecto (EXPEDITE), y aislarla permite
   probarla directamente y modificarla sin tocar `Incidencia`.

2. **Las transiciones las decide un validador, no los `setters`.** `ValidadorTransiciones` conoce el
   grafo de estados completo. Evita condicionales dispersos y concentra en un solo lugar las
   restricciones del enunciado (sin saltos, sin retrocesos, sin cierre sin solución).

3. **Las validaciones viven en el constructor de `Incidencia`.** No existe un instante en que haya
   una incidencia en estado inválido. La alternativa —un método `validar()` separado— permite
   construir el objeto roto y olvidarse de invocarlo.

4. **Impacto y urgencia se validan únicamente contra `null`.** Al ser enumeraciones, el compilador ya
   impide cualquier valor fuera del conjunto declarado. El único caso inválido posible en tiempo de
   ejecución es la referencia nula; validar otra cosa sería código inalcanzable.

5. **El repositorio está detrás de una interfaz.** `RepositorioIncidencias` permite que hoy exista una
   implementación en memoria y que mañana se agregue persistencia en base de datos sin modificar
   ningún servicio.

6. **La consola no contiene lógica.** `ConsolaHelpDesk` solo lee entrada e imprime salida. Todo lo que
   se prueba está fuera de la interfaz, lo que hace posible tener pruebas funcionales sin simular
   entrada de teclado.

---

## Estado de la integración continua

El pipeline se ejecuta en cada `push` a `main` y en cada *pull request*. Compila el proyecto, ejecuta
la totalidad de las pruebas y falla si alguna no pasa. El estado actual de `main` es el que muestra el
badge en la parte superior de este documento.

Los reportes de JUnit quedan disponibles como artefacto descargable en cada ejecución, en la pestaña
Actions del repositorio.

---

## Tablero Kanban

Enlace: `PEGAR AQUÍ EL ENLACE DEL TABLERO`

| Columna | Límite WIP |
|---|---|
| Opciones/Backlog | sin límite |
| Preparado | 3 |
| En desarrollo | 1 |
| Validación | 1 |
| Hecho | sin límite |

Las tarjetas del tablero son los *issues* del repositorio. Cada una contiene sus criterios de
aceptación verificables y la definición de hecho.

---

## Documentos relacionados

| Archivo | Contenido |
|---|---|
| `IA-LOG.md` | Bitácora de interacciones con herramientas de IA |
| `RETROSPECTIVA.md` | Retrospectiva de la pareja |
| `REFACTORING.md` | Refactorizaciones realizadas y pruebas que las protegieron |
| `CONTRIBUTING.md` | Convenciones de ramas, commits y revisión |
