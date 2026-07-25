package cr.utn.helpdesk.service;

import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;

/**
 * Calculo automatico de la prioridad de una incidencia (HU-02).
 *
 * Clase sin estado: recibe impacto y urgencia y devuelve la prioridad
 * segun las reglas del enunciado. Al no depender de ninguna instancia,
 * puede probarse de forma completamente aislada.
 */
public final class CalculadoraPrioridad {

    private CalculadoraPrioridad() {
        // Clase de utilidad: no se instancia.
    }

    public static Prioridad calcular(Impacto impacto, Urgencia urgencia) {
        validarDatos(impacto, urgencia);

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

    private static void validarDatos(Impacto impacto, Urgencia urgencia) {
        if (impacto == null) {
            throw new IllegalArgumentException(
                    "El impacto de la incidencia es obligatorio.");
        }
        if (urgencia == null) {
            throw new IllegalArgumentException(
                    "La urgencia de la incidencia es obligatoria.");
        }
    }
}
