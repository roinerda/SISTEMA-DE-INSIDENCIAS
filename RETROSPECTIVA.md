# Retrospectiva

Proyecto HelpDesk Flow — ITI-822 Metodologías Ágiles de Desarrollo de Software.
Integrantes: Roiner y Brandon Campos.

## 1. ¿Qué aportó Kanban al trabajo de la pareja?

El tablero nos dio una imagen honesta del estado real del trabajo, en lugar de
una idea vaga de "cómo vamos". Al tener cada historia como una tarjeta con sus
criterios de aceptación, ninguno de los dos empezaba a programar algo sin saber
cuándo se consideraba terminado. También hizo visible la dependencia entre
historias: fue evidente que las transiciones (HU-03) tenían que estar antes que
el cambio EXPEDITE (HU-06), porque este último se apoya en ellas. Ver las
tarjetas moverse de columna en columna nos obligó a cerrar una cosa antes de
abrir la siguiente, en vez de dejar muchos frentes a medias.

## 2. ¿Qué dificultad generó el límite WIP?

El límite de uno en "En desarrollo" fue incómodo al principio, porque la tentación
natural era avanzar dos historias a la vez. Pero justamente esa incomodidad fue
útil: nos forzó a terminar e integrar antes de empezar otra cosa. La dificultad
más real apareció cuando una rama quedó bloqueada esperando revisión: la regla de
Kanban dice que en ese caso la prioridad es desbloquear lo comprometido antes que
empezar nuevo, y respetarlo significó a veces esperar en lugar de adelantar. A
cambio, evitó que se acumulara trabajo sin integrar.

## 3. ¿Qué errores fueron detectados mediante TDD?

Escribir las pruebas primero destapó varios detalles que de otro modo habrían
pasado. En el registro, el orden de las validaciones importaba: validar el título
con `isBlank()` antes de comprobar `null` habría lanzado la excepción equivocada,
y la prueba lo evidenció. En la descripción, la prueba del límite exacto de diez
caracteres obligó a decidir entre `<` y `<=`, un error de borde clásico. En las
transiciones, las pruebas de saltos y retrocesos confirmaron que la máquina de
estados rechazaba lo que debía. Y en EXPEDITE, la prueba de regresión aseguró que
las incidencias estándar seguían comportándose igual después del cambio.

## 4. ¿Qué parte del código fue refactorizada?

La refactorización principal fue la consolidación del dominio. Al desarrollar
HU-01 y HU-02 en ramas paralelas, terminamos con dos versiones duplicadas de la
clase Incidencia en paquetes distintos. El proyecto compilaba, pero eran dos
dominios contradictorios. Las unificamos en una sola clase en el paquete `domain`,
movimos la lógica a `service`, y eliminamos los duplicados. Las 26 pruebas que
estaban en verde antes siguieron en verde después sin modificarse, lo que
confirmó que no cambiamos el comportamiento. Está documentada en REFACTORING.md.

## 5. ¿Cómo afectó el cambio de requerimiento?

El cambio EXPEDITE puso a prueba si nuestro diseño era adaptable. La clave fue
darnos cuenta de que la regla tenía dos mitades: "solo una crítica puede ser
EXPEDITE", que una incidencia sabe por sí misma, y "solo una EXPEDITE activa a la
vez", que es sobre el conjunto y ninguna incidencia individual puede verificar.
Esa distinción nos llevó a poner la primera regla en la incidencia y la segunda en
un servicio nuevo. Lo más importante fue que no tuvimos que reescribir las
transiciones: el nuevo gestor las reutiliza. El cambio se integró agregando
código, no rehaciendo el existente, y todas las pruebas previas siguieron pasando.

## 6. ¿En qué ayudó la IA?

La IA fue útil para acelerar la escritura de pruebas y de código repetitivo, y
sobre todo para discutir decisiones de diseño antes de comprometerlas. Plantearle
alternativas (por ejemplo, dónde ubicar la lógica de transiciones) nos ayudó a ver
las consecuencias de cada opción. También fue valiosa para detectar problemas de
integración, como el dominio duplicado.

## 7. ¿En qué se equivocó o fue insuficiente la IA?

La IA se equivocó varias veces en detalles concretos: propuso un conteo de pruebas
incorrecto, sugirió un diseño con estado global que rechazamos por romper el
aislamiento de las pruebas, y en una ocasión asumió un nombre de usuario de GitHub
que no existía. Esto confirmó que no se puede confiar en sus resultados sin
verificarlos: cada sugerencia hubo que ejecutarla y revisarla. Fue insuficiente
cuando el contexto real (el estado exacto del repositorio) difería de lo que ella
suponía.

## 8. ¿Qué cambiarían en una siguiente versión?

Coordinaríamos mejor las ramas desde el inicio para evitar el dominio duplicado,
partiendo siempre de la rama principal actualizada. Agregaríamos persistencia real
en base de datos, que esta versión no incluye. Y mejoraríamos la interfaz para no
depender de identificadores largos, quizás con códigos cortos y legibles, lo que
haría el uso más cómodo sin cambiar la lógica.
