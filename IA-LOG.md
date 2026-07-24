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
| 1 | 23/07/2026 | Claude | Generar el backlog inicial de issues con GitHub CLI | Sí, con correcciones | La API de GitHub rechazó una asignación; se consultaron los colaboradores reales del repositorio | Se corrigió el usuario del segundo integrante y se reasignaron cinco issues |
| 2 | 23/07/2026 | Claude | Implementar la clase `Incidencia` a partir del contrato definido por las pruebas | Sí, revisado línea por línea | `mvn test`: 15 pruebas en verde (12 de `IncidenciaTest` + 3 de `ToolchainTest`) | Se detectó un dato incorrecto en la especificación (decía 13 pruebas cuando son 12) |
| 3 | | | | | | |

---

## Detalle de interacciones

### Interacción 1 — Generación del backlog con GitHub CLI

**Fecha:** 23/07/2026
**Herramienta:** Claude
**Quién la realizó:** Roiner

**Objetivo.** Crear las once tarjetas del backlog (seis historias de usuario y cinco tareas técnicas)
como issues de GitHub, cada una con sus criterios de aceptación y su responsable asignado, en lugar
de crearlas una por una desde el navegador.

**Qué respondió.** Un script de PowerShell que utiliza GitHub CLI para crear seis etiquetas y los
once issues, con el cuerpo de cada tarjeta escrito en Markdown y las asignaciones repartidas entre
los dos integrantes.

**Qué se usó.** Aceptada con correcciones.

**Cambios humanos.** El script asumía que el nombre de usuario del segundo integrante era
`HealingSinger`, que era en realidad el nombre para mostrar del perfil y no el identificador de la
cuenta. GitHub rechazó las cinco asignaciones correspondientes con el error
`Could not resolve to a user or bot with the login 'HealingSinger'`. Se corrigió consultando los
colaboradores reales del repositorio con `gh api repos/.../collaborators --jq ".[].login"`, que
devolvió el login correcto `BrandonCampos`, y se reasignaron los issues 2, 4, 5, 9 y 11.

**Verificación.** Se revisó cada criterio de aceptación del script contra el enunciado antes de
ejecutarlo. Después de la corrección se verificó en la pestaña Issues del repositorio que los once
issues quedaran creados, etiquetados y con responsable visible.

---

### Interacción 2 — Implementación de la clase `Incidencia` (HU-01)

**Fecha:** 23/07/2026
**Herramienta:** Claude / Claude Code
**Quién la realizó:** Roiner

**Objetivo.** Implementar la clase `Incidencia` de modo que satisficiera las doce pruebas de
`IncidenciaTest`, que ya estaban escritas y en rojo (fase RED del ciclo TDD).

**Qué respondió.** La clase con los campos del dominio, las validaciones en el constructor y los
getters requeridos por el contrato de las pruebas.

**Qué se usó.** Aceptada tras revisión.

**Cambios humanos.** La especificación entregada indicaba trece pruebas cuando el archivo contenía
doce. Se detectó al ejecutar la batería y se corrigió el conteo esperado a quince pruebas totales.
Se revisó además el orden de las validaciones: el chequeo de `null` del título debe ejecutarse antes
de `isBlank()`, ya que en el orden inverso se lanzaría `NullPointerException` en lugar de
`IllegalArgumentException` y la prueba correspondiente fallaría.

**Verificación.** `mvn test` con resultado `Tests run: 15, Failures: 0, Errors: 0`. Se leyó la clase
completa antes de integrarla y se comprobó que la explicación del diseño fuera defendible: impacto y
urgencia se validan únicamente contra `null` porque, al ser enumeraciones, el compilador ya impide
cualquier valor fuera del conjunto declarado; validar otra cosa sería código inalcanzable.

---

### Interacción 3 — Sugerencia rechazada

**Fecha:**
**Herramienta:**
**Quién la realizó:**

**Objetivo.**

**Qué respondió.**

**Por qué se rechazó (razón técnica).** Debe describir un problema concreto y verificable:
acoplamiento innecesario, estado global, imposibilidad de probar de forma aislada, violación de una
regla del enunciado, complejidad injustificada. No basta con una preferencia estética.

**Qué se hizo en su lugar.**

**Verificación.**
