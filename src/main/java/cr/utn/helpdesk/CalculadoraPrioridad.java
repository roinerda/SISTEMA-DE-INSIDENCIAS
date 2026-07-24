package cr.utn.helpdesk;


 
public final class CalculadoraPrioridad {

    private CalculadoraPrioridad() {
       
    }

    public static Prioridad calcular(Impacto impacto, Urgencia urgencia) {
        validarDatos(impacto, urgencia);

        // ALTO + ALTA = CRITICA
        if (impacto == Impacto.ALTO && urgencia == Urgencia.ALTA) {
            return Prioridad.CRITICA;
        }

        // ALTO + MEDIA o BAJA = ALTA
        if (impacto == Impacto.ALTO) {
            return Prioridad.ALTA;
        }

        // MEDIO o BAJO + ALTA = ALTA
        if (urgencia == Urgencia.ALTA) {
            return Prioridad.ALTA;
        }

        // Cualquier otra combinación = NORMAL
        return Prioridad.NORMAL;
    }

    private static void validarDatos(Impacto impacto, Urgencia urgencia) {
        if (impacto == null) {
            throw new IllegalArgumentException(
                "El impacto de la incidencia es obligatorio."
            );
        }

        if (urgencia == null) {
            throw new IllegalArgumentException(
                "La urgencia de la incidencia es obligatoria."
            );
        }
    }
}
