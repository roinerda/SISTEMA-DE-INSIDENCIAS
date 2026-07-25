package cr.utn.helpdesk.service;

import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Urgencia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Servicio encargado de registrar incidencias (HU-01 + HU-02).
 *
 * La prioridad no se asigna aqui: la calcula la propia Incidencia en su
 * constructor. El servicio solo coordina la creacion y el almacenamiento.
 */
public class RegistroIncidenciaService {

    private final List<Incidencia> incidencias;

    public RegistroIncidenciaService() {
        this.incidencias = new ArrayList<>();
    }

    public Incidencia registrar(String titulo,
                                String descripcion,
                                String categoria,
                                Impacto impacto,
                                Urgencia urgencia) {
        Incidencia incidencia =
                new Incidencia(titulo, descripcion, categoria, impacto, urgencia);
        incidencias.add(incidencia);
        return incidencia;
    }

    /**
     * Registro sin categoria: la Incidencia aplica la categoria por defecto.
     * Se conserva para compatibilidad con las pruebas iniciales de HU-02.
     */
    public Incidencia registrar(String titulo,
                                String descripcion,
                                Impacto impacto,
                                Urgencia urgencia) {
        return registrar(titulo, descripcion, null, impacto, urgencia);
    }

    public List<Incidencia> listar() {
        return Collections.unmodifiableList(incidencias);
    }

    public int cantidadRegistrada() {
        return incidencias.size();
    }
}
