package cr.utn.helpdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class CalculadoraPrioridadTest {

    @Test
    void impactoAltoUrgenciaAlta_prioridadCritica() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.ALTO,
            Urgencia.ALTA
        );

        assertEquals(Prioridad.CRITICA, resultado);
    }

    @Test
    void impactoAltoUrgenciaMedia_prioridadAlta() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.ALTO,
            Urgencia.MEDIA
        );

        assertEquals(Prioridad.ALTA, resultado);
    }

    @Test
    void impactoBajoUrgenciaAlta_prioridadAlta() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.BAJO,
            Urgencia.ALTA
        );

        assertEquals(Prioridad.ALTA, resultado);
    }

    @Test
    void impactoMedioUrgenciaMedia_prioridadNormal() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.MEDIO,
            Urgencia.MEDIA
        );

        assertEquals(Prioridad.NORMAL, resultado);
    }

    @Test
    void impactoAltoUrgenciaBaja_prioridadAlta() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.ALTO,
            Urgencia.BAJA
        );

        assertEquals(Prioridad.ALTA, resultado);
    }

    @Test
    void impactoMedioUrgenciaAlta_prioridadAlta() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.MEDIO,
            Urgencia.ALTA
        );

        assertEquals(Prioridad.ALTA, resultado);
    }

    @Test
    void impactoBajoUrgenciaBaja_prioridadNormal() {
        Prioridad resultado = CalculadoraPrioridad.calcular(
            Impacto.BAJO,
            Urgencia.BAJA
        );

        assertEquals(Prioridad.NORMAL, resultado);
    }

    @Test
    void impactoNulo_lanzaExcepcion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> CalculadoraPrioridad.calcular(
                null,
                Urgencia.ALTA
            )
        );
    }

    @Test
    void urgenciaNula_lanzaExcepcion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> CalculadoraPrioridad.calcular(
                Impacto.ALTO,
                null
            )
        );
    }
}