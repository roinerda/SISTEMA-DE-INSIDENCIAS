# Convenciones de trabajo

Acuerdos de la pareja. Existen para que el historial del repositorio sea evidencia legible del
proceso, que es lo que evalúa la rúbrica.

## Ramas

```
main                       siempre compilable y con las pruebas en verde
feat/hu-01-registro        una tarjeta = una rama = un pull request
fix/cierre-sin-solucion
refactor/extraer-calculadora
```

Nunca se hace `push --force` sobre `main`. El historial no se reescribe, ni siquiera para ocultar un
error: el enunciado lo penaliza explícitamente.

## Mensajes de commit

Formato: `tipo: descripción en infinitivo, en minúscula`

| Tipo | Cuándo |
|---|---|
| `test` | se agrega una prueba que falla (fase RED) |
| `feat` | se implementa lo mínimo para que pase (fase GREEN) |
| `refactor` | se mejora el diseño sin cambiar comportamiento |
| `fix` | se corrige un defecto |
| `ci` | cambios en el pipeline |
| `docs` | documentación |
| `chore` | configuración, dependencias |

Ejemplos reales esperados:

```
test: add failing tests for critical priority calculation
feat: implement automatic incident priority
refactor: extract incident state transition validator
fix: prevent closing incidents without solution
ci: execute JUnit tests on pull requests
```

Cada commit debe poder relacionarse con una tarjeta del tablero. Se referencia al final del mensaje:
`(HU-02)`.

## Ciclo Ping-Pong TDD

1. A escribe una prueba que falla → commit `test:` → push
2. B implementa lo mínimo para que pase → commit `feat:` → push
3. Cualquiera refactoriza → commit `refactor:`
4. Se intercambian roles en la siguiente historia

## Definición de "Hecho"

Una tarjeta llega a Hecho solo si:

- [ ] la integración continua está en verde
- [ ] los criterios de aceptación fueron verificados uno por uno
- [ ] el código está integrado en `main`
- [ ] el otro integrante revisó el cambio
- [ ] la documentación afectada quedó actualizada

## Coautoría

Cuando se programa en pareja en una sola máquina, el commit lleva al otro integrante como coautor:

```
git commit -m "feat: implement incident state machine

Co-authored-by: Nombre Apellido <correo@ejemplo.com>"
```
