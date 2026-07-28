# Bitácora de uso de Inteligencia Artificial

Requisito 6.3 del enunciado. Mínimo tres interacciones relevantes, de las cuales:

- al menos una respuesta debe haber sido **modificada** por la pareja,
- al menos una sugerencia debe haber sido **rechazada**, con su razón técnica,
- toda entrada debe indicar **cómo se verificó** el resultado utilizado.

> **Regla de la pareja:** cada entrada se escribe el mismo día en que ocurre la interacción.

Herramientas utilizadas: Claude (asistente de diseño y generación) y Claude Code
(ejecución asistida en el repositorio). En todos los casos el resultado se revisó
y se verificó ejecutando la batería de pruebas antes de integrar.

---

## Registro

| # | Fecha | Herramienta | Objetivo | Resultado usado | Verificación | Cambios humanos |
|---|---|---|---|---|---|---|
| 1 | 23/07/2026 | Claude | Generar el backlog inicial de issues con GitHub CLI | Sí, con correcciones | La API rechazó una asignación; se consultaron los colaboradores reales | Se corrigió el usuario del segundo integrante y se reasignaron cinco issues |
| 2 | 23/07/2026 | Claude / Claude Code | Implementar la clase `Incidencia` (HU-01) | Sí, revisado | `mvn test` en verde | Se corrigió el conteo de pruebas y el orden de validaciones |
| 3 | 24/07/2026 | Claude / Claude Code | Consolidar el dominio duplicado (HU-01 + HU-02) | Sí, revisado | `mvn clean test`: 26 pruebas en verde | Paquete único `domain/`; prioridad calculada en el constructor |
| 4 | 24/07/2026 | Claude | Diseño de la máquina de estados (HU-03) | **Rechazada** la primera propuesta | Análisis de la interacción con HU-06 | Se rechazó el validador externo; se optó por la opción A |
| 5 | 24/07/2026 | Claude / Claude Code | Implementar transiciones de estado (HU-03) | Sí, revisado | `mvn clean test` en verde | Ciclo TDD en dos commits separados (test y feat) |
| 6 | 25/07/2026 | Claude / Claude Code | Clase de servicio EXPEDITE (HU-06, cambio de requerimiento) | Sí, revisado | `mvn clean test`: 51 pruebas en verde, con regresión | Regla dividida en dos capas: individual en `Incidencia`, de conjunto en `GestorExpedite` |
| 7 | 27/07/2026 | Claude / Claude Code | Reconstruir métricas de flujo (HU-05) desde una rama desactualizada | Sí, adaptado | `mvn clean test` en verde con 15 pruebas nuevas | Se adaptó al dominio actual y se cambió la unidad del lead time a minutos |
| 8 | 27/07/2026 | Claude / Claude Code | Implementar consulta y filtros (HU-04) desde cero | Sí, revisado | `mvn clean test`: 78 pruebas en verde | `buscarPorId` devuelve `Optional`; listas de resultado inmutables |
| 9 | 27/07/2026 | Claude / Claude Code | Menú de consola que integra las seis historias (TEC-03) | Sí, revisado | `mvn clean package` en verde; ejecución manual del menú | La consola no contiene lógica; coordina una colección central y el gestor de EXPEDITE |

---

## Detalle de interacciones

### Interacción 1 — Generación del backlog con GitHub CLI

**Fecha:** 23/07/2026 · **Herramienta:** Claude · **Realizó:** Roiner

**Objetivo.** Crear las once tarjetas del backlog como issues de GitHub, con
criterios de aceptación y responsable, en lugar de crearlas a mano.

**Qué respondió.** Un script de PowerShell con GitHub CLI que crea seis etiquetas
y once issues con las asignaciones repartidas.

**Qué se usó.** Aceptada con correcciones.

**Cambios humanos.** El script asumía que el usuario del segundo integrante era
`HealingSinger`, que era el nombre para mostrar, no el login. GitHub rechazó las
cinco asignaciones con `Could not resolve to a user`. Se corrigió consultando los
colaboradores reales con `gh api repos/.../collaborators` (login real:
`BrandonCampos`) y se reasignaron los issues.

**Verificación.** Se revisó cada criterio contra el enunciado antes de ejecutar,
y se confirmó en la pestaña Issues que los once quedaran creados y asignados.

---

### Interacción 2 — Implementación de `Incidencia` (HU-01)

**Fecha:** 23/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Implementar `Incidencia` para satisfacer las pruebas de
`IncidenciaTest`, ya escritas y en rojo (fase RED).

**Qué respondió.** La clase con campos, validaciones en el constructor y getters.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** La especificación indicaba trece pruebas cuando eran doce; se
detectó al ejecutar. Se revisó el orden de validaciones: el chequeo de `null` del
título debe ir antes de `isBlank()` o se lanzaría `NullPointerException`.

**Verificación.** `mvn test` en verde. Se leyó la clase completa y se confirmó la
justificación de validar impacto y urgencia solo contra `null` (son enumeraciones;
el compilador impide otros valores).

---

### Interacción 3 — Consolidación del dominio duplicado

**Fecha:** 24/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Unificar las dos versiones de `Incidencia` surgidas del desarrollo
paralelo de HU-01 y HU-02 en dos paquetes distintos.

**Qué respondió.** Una consolidación con un único dominio en `domain/`, los
servicios en `service/`, y la prioridad calculada en el constructor.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se eligió `domain/` como paquete correcto y calcular la
prioridad en el constructor (no desde un servicio), para impedir su asignación
manual. Se conservó una sobrecarga de `registrar` sin categoría para no romper
las pruebas de HU-02.

**Verificación.** `mvn clean test` con 26 pruebas en verde, iguales a las de antes
de la consolidación. (Esta refactorización está documentada en detalle en
`REFACTORING.md`.)

---

### Interacción 4 — Sugerencia rechazada: ubicación de las transiciones (HU-03)

**Fecha:** 24/07/2026 · **Herramienta:** Claude · **Realizó:** Roiner

**Objetivo.** Decidir dónde ubicar la lógica de transiciones de estado.

**Qué respondió.** El plan original proponía una clase `ValidadorTransiciones`
externa a la incidencia (opción B).

**Por qué se rechazó (razón técnica).** Un validador externo obliga a exponer un
`setEstado` público, reabriendo la posibilidad de cambiar el estado sin validar
—el mismo hueco evitado con la prioridad—. Además, la regla de cupo de HU-06 es
sobre el conjunto de incidencias y de todos modos debía vivir en un servicio.
Mantener la transición simple dentro de la incidencia (opción A) resultó más
coherente y no impidió la política de conjunto posterior.

**Qué se hizo en su lugar.** Opción A: `Incidencia` gobierna su ciclo con
`avanzarA(Estado)` y `finalizar(String)` como único camino a FINALIZADA, lo que
hace imposible por construcción cerrar sin descripción de solución.

**Verificación.** Las pruebas de `TransicionEstadoTest` en verde (válidas, saltos,
retrocesos y cierre).

---

### Interacción 5 — Implementación de transiciones (HU-03)

**Fecha:** 24/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Implementar `avanzarA` y `finalizar` respetando el ciclo TDD en dos
commits (RED y GREEN).

**Qué respondió.** Los dos métodos con las guardas de transición.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se verificó que el commit de prueba (RED, con el build
fallando) quedara separado del de implementación (GREEN). En una historia previa
se habían combinado en un solo commit, lo que borra la evidencia del ciclo; aquí
se corrigió la práctica.

**Verificación.** `mvn clean test` en verde. Se documentó una limitación conocida:
`avanzarA` usa `ordinal()`, que acopla la lógica al orden del enum; aceptable para
el alcance, con un mapa de transiciones como alternativa más robusta.

---

### Interacción 6 — Clase de servicio EXPEDITE (HU-06)

**Fecha:** 25/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Implementar el cambio obligatorio de requerimiento (EXPEDITE) sin
romper el comportamiento validado.

**Qué respondió.** Un campo `claseServicio` y `marcarComoExpedite()` en
`Incidencia`, más una clase nueva `GestorExpedite` para el cupo único.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se dividió la regla según qué información necesita cada mitad:
"solo crítica puede ser EXPEDITE" en la incidencia (que conoce su prioridad), y
"solo una EXPEDITE activa" en `GestorExpedite` (que conoce el conjunto). Se
descartó reimplementar las transiciones en el gestor: este verifica el cupo y
delega en el `avanzarA()` de HU-03, de modo que el comportamiento estándar no
cambia. La liberación del cupo no se programó como operación explícita: al
finalizar la EXPEDITE activa, sale de los estados activos y deja de contarse.

**Verificación.** `mvn clean test` con 51 pruebas en verde, incluida una de
regresión que confirma que las incidencias estándar conservan su comportamiento.

---

### Interacción 7 — Reconstrucción de métricas de flujo (HU-05)

**Fecha:** 27/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Integrar HU-05. La rama existente estaba escrita contra una versión
antigua del dominio: usaba `Estado.ABIERTA`, `getFechaRegistro()` y
`getFechaRegistroFinalizacion()`, ninguno de los cuales existe en el dominio
actual, y no tenía pruebas.

**Qué respondió.** Una versión reconstruida de `MetricasFlujoService` adaptada al
dominio consolidado, con pruebas.

**Qué se usó.** Aceptada con adaptaciones.

**Cambios humanos.** Se conservó la lógica original (throughput, lead time, conteo
por prioridad) pero corrigiendo los nombres al dominio real: "abierta" pasó a
significar "estado distinto de FINALIZADA", y los getters correctos son
`getFechaCreacion()` y `getFechaCierre()`. Se cambió la unidad del lead time de
horas truncadas a **minutos**, porque `Duration.toHours()` perdía precisión en
incidencias de corta duración. Se decidió probar el lead time por propiedades
(lista vacía da cero, ignora las no finalizadas, resultado no negativo) en lugar
de inyectar un reloj falso o abrir la entidad con un constructor de pruebas, lo
que habría sido sobreingeniería.

**Verificación.** `mvn clean test` en verde con 15 pruebas nuevas.

---

### Interacción 8 — Consulta y filtros de incidencias (HU-04)

**Fecha:** 27/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Implementar HU-04 desde cero (la rama existente tenía el archivo
vacío), respetando el ciclo TDD.

**Qué respondió.** `ConsultaIncidenciasService` con listar, buscar por id, filtrar
por estado y prioridad, y separar abiertas de finalizadas.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se decidió que `buscarPorId` devuelva `Optional<Incidencia>`,
de modo que un id inexistente produzca un `Optional` vacío en lugar de una
excepción, cumpliendo el criterio del enunciado. Las listas devueltas se hicieron
inmutables para que un consumidor no altere la colección interna. La clase se
diseñó como contraparte simétrica de `MetricasFlujoService`.

**Verificación.** `mvn clean test` con 78 pruebas en verde (16 nuevas). Los dos
commits (RED y GREEN) quedaron separados.

---

### Interacción 9 — Menú de consola (TEC-03)

**Fecha:** 27/07/2026 · **Herramienta:** Claude / Claude Code · **Realizó:** Roiner

**Objetivo.** Crear la interfaz de consola que integra las seis historias en una
aplicación ejecutable y demostrable.

**Qué respondió.** `ConsolaHelpDesk` con un menú numérico que expone registrar,
listar, buscar, filtrar, avanzar estado, finalizar, marcar EXPEDITE y ver
métricas, más la actualización de `Main` para arrancarla.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se estableció que la consola no contuviera lógica de negocio:
solo lee, valida mínimamente e invoca a los servicios ya probados. El punto de
diseño principal fue que la consola mantiene una única colección central de
incidencias sobre la que operan todos los servicios y el `GestorExpedite`, de modo
que la política EXPEDITE se respeta también desde la interfaz. No se agregaron
pruebas JUnit para la consola porque una interfaz de teclado no se presta a prueba
unitaria y el enunciado no lo exige; la lógica subyacente ya está cubierta.

**Verificación.** `mvn clean package` en verde con todas las pruebas, y ejecución
manual del menú recorriendo el flujo completo (registro con prioridad automática,
avance de estados, marca EXPEDITE, finalización con solución y métricas).
