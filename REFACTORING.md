# Refactorizaciones

Requisito 5.4 del enunciado. Cada refactorización documenta cuatro puntos:
problema encontrado, cambio realizado, pruebas que lo protegieron y resultado obtenido.

Una refactorización válida **no cambia el comportamiento**. Las mismas pruebas deben estar en verde
antes y después, sin modificarlas.

---

## Refactorización 1 — Consolidación del dominio duplicado

**Commit:** `9bc137b` (rama `refactor/consolidar-dominio`, PR #14)
**Fecha:** 24/07/2026
**Realizada por:** Roiner

### 1. Problema encontrado

Las historias HU-01 (registro) y HU-02 (prioridad) se desarrollaron en ramas paralelas y se
integraron por separado. Cada una creó su propia versión de las clases del dominio en paquetes
distintos: una `Incidencia` en el paquete raíz `cr.utn.helpdesk` (con prioridad y fecha de registro)
y otra `Incidencia` en `cr.utn.helpdesk.domain` (con identificador, estado y fechas), cada una con su
propio juego de enums.

El proyecto compilaba y las pruebas pasaban porque Java trataba ambas clases como distintas al estar
en paquetes separados. Pero eran dos dominios paralelos que representaban el mismo concepto. El
problema era concreto y del momento: la siguiente historia (HU-03, transiciones) tendría que elegir
sobre cuál de las dos `Incidencia` construir, y la otra quedaría como código muerto que contradice y
confunde. Continuar así bloqueaba el avance ordenado del resto del proyecto.

### 2. Cambio realizado

Se unificó todo en una sola estructura de paquetes. Se sustituyeron las dos `Incidencia` por una
única en `cr.utn.helpdesk.domain`, que combina los campos de ambas (identificador, título,
descripción, categoría, impacto, urgencia, prioridad, estado, fecha de creación, fecha de cierre y
descripción de la solución). Se movieron `CalculadoraPrioridad` y `RegistroIncidenciaService` al
paquete `cr.utn.helpdesk.service`, corrigiendo sus imports. Los enums quedaron únicamente en
`domain`. Se eliminaron las clases duplicadas del paquete raíz.

Antes: dos paquetes con clases homónimas y contradictorias.
Después: `domain/` con la entidad y los enums, `service/` con la lógica.

Durante la unificación se decidió que la prioridad se calcule dentro del constructor de `Incidencia`,
en lugar de asignarse desde un servicio, para que no exista forma de fijarla manualmente (criterio de
HU-02).

### 3. Pruebas que protegieron la refactorización

Las 26 pruebas existentes se ejecutaron en verde antes del cambio y siguieron en verde después, sin
modificar ninguna:

- `IncidenciaTest` (registro y validaciones de HU-01)
- `CalculadoraPrioridadTest` (reglas de prioridad de HU-02)
- `RegistroIncidenciaServiceTest` (servicio de registro de HU-02)
- `ToolchainTest` (verificación del entorno)

Las pruebas de HU-02 invocaban `registrar` sin categoría. Para no romperlas se conservó una
sobrecarga de `registrar` sin ese parámetro, que aplica una categoría por defecto.

### 4. Resultado obtenido

El proyecto pasó de dos dominios paralelos y contradictorios a uno solo coherente, sin cambiar el
comportamiento observable (las mismas 26 pruebas seguían en verde). La responsabilidad quedó
separada: entidad en `domain`, lógica en `service`. Esto permitió que HU-03, HU-05 y HU-06 se
construyeran sobre una base única y sin ambigüedad. En particular, agregar después la clase de
servicio EXPEDITE (HU-06) consistió en extender esa única `Incidencia`, en lugar de tener que
reconciliar dos versiones distintas.
