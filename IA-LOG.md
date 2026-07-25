# Bitácora de uso de Inteligencia Artificial

Requisito 6.3 del enunciado. Mínimo tres interacciones relevantes, de las cuales:

- al menos una respuesta debe haber sido **modificada** por la pareja,
- al menos una sugerencia debe haber sido **rechazada**, con su razón técnica,
- toda entrada debe indicar **cómo se verificó** el resultado utilizado.

> **Regla de la pareja:** cada entrada se escribe el mismo día en que ocurre la interacción.

---

## Registro

| # | Fecha | Herramienta | Objetivo | Resultado usado | Verificación | Cambios humanos |
|---|---|---|---|---|---|---|
| 1 | 23/07/2026 | Claude | Generar el backlog inicial de issues con GitHub CLI | Sí, con correcciones | La API de GitHub rechazó una asignación; se consultaron los colaboradores reales | Se corrigió el usuario del segundo integrante y se reasignaron cinco issues |
| 2 | 23/07/2026 | Claude / Claude Code | Implementar la clase `Incidencia` (HU-01) | Sí, revisado línea por línea | `mvn test`: pruebas en verde | Se corrigió un dato de la especificación (conteo de pruebas) |
| 3 | 24/07/2026 | Claude / Claude Code | Consolidar el dominio duplicado tras el merge paralelo de HU-01 y HU-02 | Sí, revisado | `mvn clean test`: 26 pruebas en verde | Se eligió `domain/` como paquete único; prioridad calculada en el constructor |
| 4 | 24/07/2026 | Claude | Diseño de la máquina de estados (HU-03): dónde ubicar la lógica de transiciones | **Rechazada** la primera propuesta | Análisis de la interacción con HU-06 | Se rechazó el validador externo; se optó por la opción A |
| 5 | 24/07/2026 | Claude / Claude Code | Implementar transiciones de estado (HU-03) con ciclo RED-GREEN | Sí, revisado | `mvn clean test`: 43 pruebas en verde | Se mantuvo el ciclo en dos commits separados (test y feat) |

---

## Detalle de interacciones

### Interacción 1 — Generación del backlog con GitHub CLI

**Fecha:** 23/07/2026
**Herramienta:** Claude
**Quién la realizó:** Roiner

**Objetivo.** Crear las once tarjetas del backlog (seis historias y cinco tareas técnicas) como
issues de GitHub, cada una con criterios de aceptación y responsable, en lugar de crearlas a mano.

**Qué respondió.** Un script de PowerShell que usa GitHub CLI para crear seis etiquetas y once
issues, con las asignaciones repartidas entre los dos integrantes.

**Qué se usó.** Aceptada con correcciones.

**Cambios humanos.** El script asumía que el usuario del segundo integrante era `HealingSinger`,
que era en realidad el nombre para mostrar del perfil, no el identificador de la cuenta. GitHub
rechazó las cinco asignaciones con el error `Could not resolve to a user or bot with the login
'HealingSinger'`. Se corrigió consultando los colaboradores reales con
`gh api repos/.../collaborators --jq ".[].login"`, que devolvió `BrandonCampos`, y se reasignaron
los issues 2, 4, 5, 9 y 11.

**Verificación.** Se revisó cada criterio de aceptación del script contra el enunciado antes de
ejecutarlo. Tras la corrección se verificó en la pestaña Issues que los once quedaran creados,
etiquetados y con responsable.

---

### Interacción 2 — Implementación de `Incidencia` (HU-01)

**Fecha:** 23/07/2026
**Herramienta:** Claude / Claude Code
**Quién la realizó:** Roiner

**Objetivo.** Implementar la clase `Incidencia` de modo que satisficiera las pruebas de
`IncidenciaTest`, ya escritas y en rojo (fase RED del ciclo TDD).

**Qué respondió.** La clase con los campos del dominio, las validaciones en el constructor y los
getters requeridos por el contrato de las pruebas.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** La especificación indicaba trece pruebas cuando el archivo contenía doce; se
detectó al ejecutar la batería. Se revisó el orden de las validaciones: el chequeo de `null` del
título debe ir antes de `isBlank()`, o se lanzaría `NullPointerException` en lugar de
`IllegalArgumentException`.

**Verificación.** `mvn test` en verde. Se leyó la clase completa antes de integrarla y se comprobó
que la explicación del diseño fuera defendible: impacto y urgencia se validan solo contra `null`
porque, al ser enumeraciones, el compilador impide cualquier valor fuera del conjunto declarado.

---

### Interacción 3 — Consolidación del dominio duplicado

**Fecha:** 24/07/2026
**Herramienta:** Claude / Claude Code
**Quién la realizó:** Roiner

**Objetivo.** Tras integrar HU-01 y HU-02 en ramas paralelas, el repositorio quedó con la clase
`Incidencia` y los enums duplicados en dos paquetes distintos (`cr.utn.helpdesk` y
`cr.utn.helpdesk.domain`). Unificar el dominio en una sola estructura.

**Qué respondió.** Una consolidación que deja un único dominio en `domain/`, mueve
`CalculadoraPrioridad` y el servicio de registro a `service/`, y unifica las dos versiones de
`Incidencia` en una sola con la prioridad calculada en el constructor.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se tomaron dos decisiones de diseño. Primero, `domain/` como paquete correcto
(y no la raíz), por separar dominio de servicios. Segundo, calcular la prioridad dentro del
constructor de `Incidencia` en lugar de asignarla desde un servicio, para que no exista forma de
fijarla manualmente, tal como exige HU-02. Además, las pruebas de HU-02 llamaban al registro sin
categoría; se conservó una sobrecarga del método `registrar` sin categoría para no romperlas.

**Verificación.** `mvn clean test` con 26 pruebas en verde, las mismas que antes de la
consolidación, confirmando que el comportamiento no cambió.

---

### Interacción 4 — Sugerencia rechazada: ubicación de la lógica de transiciones (HU-03)

**Fecha:** 24/07/2026
**Herramienta:** Claude
**Quién la realizó:** Roiner

**Objetivo.** Decidir dónde ubicar la lógica de transiciones de estado de la incidencia antes de
implementar HU-03.

**Qué respondió.** El plan original proponía una clase separada `ValidadorTransiciones` que
gobernara los cambios de estado desde fuera de la incidencia (opción B).

**Por qué se rechazó (razón técnica).** Un validador externo obliga a exponer un `setEstado`
público en la incidencia, lo que reabre la posibilidad de cambiar el estado saltándose la
validación —el mismo hueco que se había evitado con la prioridad—. Además, al revisar el cambio
obligatorio HU-06 (EXPEDITE) se observó que su regla de cupo es sobre el **conjunto** de
incidencias, no sobre una sola, por lo que de todos modos deberá vivir en un servicio. Mantener la
transición simple dentro de la propia incidencia (opción A) resultó más coherente con el diseño ya
existente y no impide que la política de conjunto de HU-06 se implemente después en un servicio.

**Qué se hizo en su lugar.** Se implementó la opción A: `Incidencia` gobierna su propio ciclo con
`avanzarA(Estado)` para las transiciones consecutivas y `finalizar(String)` como único camino a
FINALIZADA. Esto hace imposible por construcción cerrar una incidencia sin descripción de solución.

**Verificación.** Las 17 pruebas de `TransicionEstadoTest` en verde, cubriendo transiciones
válidas, saltos, retrocesos y todos los casos de `finalizar()`.

---

### Interacción 5 — Implementación de transiciones (HU-03)

**Fecha:** 24/07/2026
**Herramienta:** Claude / Claude Code
**Quién la realizó:** Roiner

**Objetivo.** Implementar los métodos `avanzarA` y `finalizar` en `Incidencia` para satisfacer las
pruebas de HU-03, respetando el ciclo TDD en dos commits separados (RED y GREEN).

**Qué respondió.** Los dos métodos con las guardas de transición: `avanzarA` acepta solo el estado
consecutivo inmediato y rechaza FINALIZADA; `finalizar` valida el estado de origen y la presencia
de la solución.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** Se verificó que el commit de la prueba (fase RED, con el build fallando por no
existir aún los métodos) quedara **separado** del commit de la implementación (fase GREEN). En una
historia anterior la prueba y la implementación se habían combinado en un solo commit, lo que
borra la evidencia del ciclo TDD; aquí se corrigió esa práctica.

**Verificación.** `mvn clean test` con `Tests run: 43, Failures: 0, Errors: 0`. Se revisó una
limitación conocida del diseño: `avanzarA` usa `ordinal()` del enum, lo que acopla la lógica al
orden de declaración de los estados; se documentó como aceptable para el alcance del proyecto, con
un mapa explícito de transiciones como alternativa más robusta si el flujo creciera.
