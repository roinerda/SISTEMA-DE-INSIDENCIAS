# Bitácora de uso de Inteligencia Artificial

Requisito 6.3 del enunciado. Mínimo tres interacciones relevantes, de las cuales:

- al menos una respuesta debe haber sido **modificada** por la pareja,
- al menos una sugerencia debe haber sido **rechazada**, con su razón técnica,
- toda entrada debe indicar **cómo se verificó** el resultado utilizado.

> **Regla de la pareja:** cada entrada se escribe el mismo día en que ocurre la interacción.
> Una bitácora reconstruida al final se nota y no es evidencia válida.

---

## Registro

| # | Fecha | Herramienta | Objetivo | Resultado usado | Verificación | Cambios humanos |
|---|---|---|---|---|---|---|
| 1 | | | | | | |
| 2 | | | | | | |
| 3 | | | | | | |

---

## Detalle de interacciones

### Interacción 1 — (título)

**Fecha:** 
**Herramienta:** 
**Quién la realizó:** 

**Objetivo.** Qué se le pidió y por qué.

**Qué respondió.** Resumen de la propuesta.

**Qué se usó.** Aceptada / aceptada con cambios / rechazada.

**Cambios humanos.** Qué se modificó respecto de la propuesta original y por qué.

**Verificación.** Cómo se comprobó que el resultado era correcto (pruebas ejecutadas, revisión del
compañero, consulta a documentación oficial).

---

### Interacción 2 — (título)

**Fecha:** 
**Herramienta:** 
**Quién la realizó:** 

**Objetivo.** 

**Qué respondió.** 

**Qué se usó.** 

**Cambios humanos.** 

**Verificación.** 

---

### Interacción 3 — Sugerencia rechazada

**Fecha:** 
**Herramienta:** 
**Quién la realizó:** 

**Objetivo.** 

**Qué respondió.** 

**Por qué se rechazó (razón técnica).** Esta es la casilla que el docente va a leer con más atención.
Debe explicar un problema concreto: acoplamiento, estado global, imposibilidad de probar, violación de
una regla del enunciado, rendimiento, etc. No basta con "no nos gustó".

**Qué se hizo en su lugar.** 

**Verificación.** 

# Bitácora de uso de Inteligencia Artificial

Requisito 6.3 del enunciado. Se documentan tres interacciones relevantes realizadas durante el desarrollo de la historia de usuario relacionada con el cálculo automático de la prioridad de una incidencia.

Las interacciones incluyen:

* una respuesta aceptada con modificaciones realizadas por el equipo;
* una sugerencia rechazada por una razón técnica;
* la forma en que se verificó cada resultado utilizado.

> **Regla de la pareja:** cada entrada se registra el mismo día en que ocurre la interacción.

---

## Registro

| # | Fecha      | Herramienta | Objetivo                                                                              | Resultado usado        | Verificación                                                                          | Cambios humanos                                                                                                              |
| - | ---------- | ----------- | ------------------------------------------------------------------------------------- | ---------------------- | ------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| 1 | 23/07/2026 | ChatGPT     | Corregir y completar las pruebas unitarias de la calculadora de prioridad             | Aceptado con cambios   | Ejecución de pruebas con Maven y revisión de los resultados                           | Se añadieron las anotaciones `@Test`, importaciones de JUnit y se adaptó el paquete a la estructura real del proyecto        |
| 2 | 23/07/2026 | ChatGPT     | Implementar las reglas de negocio para calcular la prioridad según impacto y urgencia | Aceptado con cambios   | Pruebas unitarias para las cuatro combinaciones principales y ejecución de `mvn test` | Se simplificaron las condiciones y se utilizaron los `enum` existentes del proyecto                                          |
| 3 | 23/07/2026 | ChatGPT     | Determinar cómo integrar el cálculo durante el registro de una incidencia             | Rechazado parcialmente | Revisión de la arquitectura existente y pruebas del flujo de registro                 | Se rechazó crear un servicio y una lista de incidencias adicionales porque el proyecto ya poseía su propio flujo de registro |

---

## Detalle de interacciones

### Interacción 1 — Corrección de las pruebas unitarias

**Fecha:** 23/07/2026
**Herramienta:** ChatGPT
**Quién la realizó:** Integrante encargado de la historia de usuario de prioridad

**Objetivo.** Se solicitó ayuda para revisar la clase `CalculadoraPrioridadTest`, debido a que los métodos de prueba estaban creados, pero no eran reconocidos ni ejecutados correctamente por JUnit.

**Qué respondió.** La herramienta indicó que era necesario agregar la anotación `@Test` sobre cada método, importar `assertEquals` desde JUnit Jupiter y mantener la clase de pruebas dentro de la carpeta `src/test/java`. También propuso pruebas para validar las cuatro reglas principales del cálculo de prioridad.

**Qué se usó.** La respuesta fue **aceptada con cambios**.

**Cambios humanos.** Se adaptó el nombre del paquete a `cr.utn.helpdesk`, porque era el paquete utilizado en el proyecto. También se conservaron únicamente las pruebas relacionadas directamente con los criterios de aceptación solicitados. No se copiaron automáticamente todas las pruebas adicionales propuestas, ya que primero se revisó cuáles eran necesarias para la historia de usuario.

La clase quedó con pruebas equivalentes a las siguientes:

```java
@Test
void impactoAltoUrgenciaAlta_prioridadCritica() {
    assertEquals(
        Prioridad.CRITICA,
        CalculadoraPrioridad.calcular(Impacto.ALTO, Urgencia.ALTA)
    );
}

@Test
void impactoAltoUrgenciaMedia_prioridadAlta() {
    assertEquals(
        Prioridad.ALTA,
        CalculadoraPrioridad.calcular(Impacto.ALTO, Urgencia.MEDIA)
    );
}

@Test
void impactoBajoUrgenciaAlta_prioridadAlta() {
    assertEquals(
        Prioridad.ALTA,
        CalculadoraPrioridad.calcular(Impacto.BAJO, Urgencia.ALTA)
    );
}

@Test
void impactoMedioUrgenciaMedia_prioridadNormal() {
    assertEquals(
        Prioridad.NORMAL,
        CalculadoraPrioridad.calcular(Impacto.MEDIO, Urgencia.MEDIA)
    );
}
```

**Verificación.** Se ejecutaron las pruebas mediante el comando:

```bash
mvn test
```

También se verificó que JUnit reconociera los cuatro métodos como pruebas y que no aparecieran errores de compilación relacionados con importaciones, paquetes o anotaciones.

---

### Interacción 2 — Implementación de la calculadora de prioridad

**Fecha:** 23/07/2026
**Herramienta:** ChatGPT
**Quién la realizó:** Integrante encargado de la historia de usuario de prioridad

**Objetivo.** Se solicitó una propuesta para implementar las reglas de negocio que determinan automáticamente la prioridad de una incidencia según su impacto y urgencia.

Las reglas indicadas fueron:

| Impacto                    | Urgencia       | Prioridad |
| -------------------------- | -------------- | --------- |
| ALTO                       | ALTA           | CRITICA   |
| ALTO                       | MEDIA o BAJA   | ALTA      |
| MEDIO o BAJO               | ALTA           | ALTA      |
| Cualquier otra combinación | Cualquier otra | NORMAL    |

**Qué respondió.** La herramienta propuso crear una clase estática llamada `CalculadoraPrioridad`, encargada exclusivamente de recibir un `Impacto` y una `Urgencia` y devolver la prioridad correspondiente. También sugirió validar que ninguno de los dos valores fuera nulo.

La lógica propuesta fue similar a la siguiente:

```java
public static Prioridad calcular(Impacto impacto, Urgencia urgencia) {
    if (impacto == null || urgencia == null) {
        throw new IllegalArgumentException(
            "El impacto y la urgencia son obligatorios"
        );
    }

    if (impacto == Impacto.ALTO && urgencia == Urgencia.ALTA) {
        return Prioridad.CRITICA;
    }

    if (impacto == Impacto.ALTO) {
        return Prioridad.ALTA;
    }

    if (urgencia == Urgencia.ALTA) {
        return Prioridad.ALTA;
    }

    return Prioridad.NORMAL;
}
```

**Qué se usó.** La respuesta fue **aceptada con cambios**.

**Cambios humanos.** Se revisó manualmente el orden de las condiciones para evitar que la combinación `ALTO + ALTA` fuera clasificada solamente como `ALTA`. Por esta razón, la condición de prioridad crítica se dejó antes de las reglas generales.

Además, se utilizaron los `enum` ya existentes en el proyecto en lugar de crear duplicados. También se adaptaron los mensajes de error y comentarios al estilo utilizado por el resto del código.

La clase se mantuvo sin estado interno y con un constructor privado, debido a que su única responsabilidad es calcular un resultado.

**Verificación.** Se verificó el resultado mediante las cuatro pruebas principales definidas en los criterios de aceptación:

* `impactoAltoUrgenciaAlta_prioridadCritica`;
* `impactoAltoUrgenciaMedia_prioridadAlta`;
* `impactoBajoUrgenciaAlta_prioridadAlta`;
* `impactoMedioUrgenciaMedia_prioridadNormal`.

Posteriormente se ejecutó:

```bash
mvn test
```

Se comprobó que todas las pruebas terminaran correctamente y que las combinaciones no contempladas por las tres primeras reglas devolvieran `Prioridad.NORMAL`.

---

### Interacción 3 — Sugerencia de servicio adicional rechazada

**Fecha:** 23/07/2026
**Herramienta:** ChatGPT
**Quién la realizó:** Integrante encargado de la historia de usuario de prioridad

**Objetivo.** Se solicitó ayuda para identificar todas las clases necesarias para integrar el cálculo de prioridad en el registro de incidencias.

**Qué respondió.** La herramienta sugirió crear, además de la calculadora, una nueva clase llamada `RegistroIncidenciaService`. Esta clase mantenía una lista interna de incidencias y ofrecía métodos como `registrar`, `listar` y `cantidadRegistrada`.

Ejemplo de la sugerencia:

```java
public class RegistroIncidenciaService {

    private final List<Incidencia> incidencias;

    public RegistroIncidenciaService() {
        this.incidencias = new ArrayList<>();
    }

    public Incidencia registrar(
        String titulo,
        String descripcion,
        Impacto impacto,
        Urgencia urgencia
    ) {
        Incidencia incidencia = new Incidencia(
            titulo,
            descripcion,
            impacto,
            urgencia
        );

        incidencias.add(incidencia);
        return incidencia;
    }
}
```

**Por qué se rechazó (razón técnica).** La sugerencia fue rechazada porque introducía una segunda forma de registrar y almacenar incidencias, aunque el proyecto ya contaba con una estructura para realizar ese proceso.

Agregar una lista interna dentro de un servicio nuevo habría provocado duplicación de responsabilidades y riesgo de mantener dos fuentes de datos diferentes. También habría aumentado el acoplamiento, ya que las pruebas de prioridad quedarían relacionadas con una implementación temporal de almacenamiento que no era necesaria para cumplir la historia de usuario.

La historia requería calcular la prioridad al registrar la incidencia, pero no solicitaba crear un nuevo repositorio en memoria ni modificar toda la arquitectura existente.

**Qué se hizo en su lugar.** Se integró directamente la llamada a `CalculadoraPrioridad.calcular(impacto, urgencia)` dentro del flujo de creación o registro ya existente.

La prioridad dejó de recibirse como un dato digitado por el usuario y pasó a obtenerse automáticamente:

```java
this.prioridad = CalculadoraPrioridad.calcular(
    impacto,
    urgencia
);
```

También se verificó que no existiera un método público como:

```java
setPrioridad(...)
```

ni un parámetro de prioridad en el proceso de registro, porque cualquiera de estas opciones permitiría asignarla manualmente y violaría los criterios de aceptación.

**Verificación.** Se revisó el constructor o método encargado de registrar la incidencia y se comprobó que únicamente recibiera el impacto y la urgencia necesarios para el cálculo.

Después se ejecutaron nuevamente las pruebas automáticas con:

```bash
mvn test
```

Finalmente, el código fue revisado por el otro integrante antes de realizar la integración mediante un Pull Request hacia la rama `main`.
