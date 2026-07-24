package cr.utn.helpdesk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Servicio encargado de registrar incidencias.
 */
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

    public List<Incidencia> listar() {
        return Collections.unmodifiableList(incidencias);
    }

    public int cantidadRegistrada() {
        return incidencias.size();
    }
}