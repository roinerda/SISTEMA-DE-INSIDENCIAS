package cr.utn.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;
import org.junit.jupiter.api.Test;

class CalculadoraPrioridadTest {

    @Test
    void impactoAltoUrgenciaAlta_prioridadCritica() {
        assertEquals(Prioridad.CRITICA,
                CalculadoraPrioridad.calcular(Impacto.ALTO, Urgencia.ALTA));
    }

    @Test
    void impactoAltoUrgenciaMedia_prioridadAlta() {
        assertEquals(Prioridad.ALTA,
                CalculadoraPrioridad.calcular(Impacto.ALTO, Urgencia.MEDIA));
    }

    @Test
    void impactoBajoUrgenciaAlta_prioridadAlta() {
        assertEquals(Prioridad.ALTA,
                CalculadoraPrioridad.calcular(Impacto.BAJO, Urgencia.ALTA));
    }

    @Test
    void impactoMedioUrgenciaMedia_prioridadNormal() {
        assertEquals(Prioridad.NORMAL,
                CalculadoraPrioridad.calcular(Impacto.MEDIO, Urgencia.MEDIA));
    }

    @Test
    void impactoAltoUrgenciaBaja_prioridadAlta() {
        assertEquals(Prioridad.ALTA,
                CalculadoraPrioridad.calcular(Impacto.ALTO, Urgencia.BAJA));
    }

    @Test
    void impactoMedioUrgenciaAlta_prioridadAlta() {
        assertEquals(Prioridad.ALTA,
                CalculadoraPrioridad.calcular(Impacto.MEDIO, Urgencia.ALTA));
    }

    @Test
    void impactoBajoUrgenciaBaja_prioridadNormal() {
        assertEquals(Prioridad.NORMAL,
                CalculadoraPrioridad.calcular(Impacto.BAJO, Urgencia.BAJA));
    }

    @Test
    void impactoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> CalculadoraPrioridad.calcular(null, Urgencia.ALTA));
    }

    @Test
    void urgenciaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> CalculadoraPrioridad.calcular(Impacto.ALTO, null));
    }
}
