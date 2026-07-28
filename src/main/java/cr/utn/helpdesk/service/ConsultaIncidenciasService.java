package cr.utn.helpdesk.service;

import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import java.util.List;
import java.util.Optional;

/**
 * Consulta y filtrado de incidencias (HU-04).
 *
 * Opera sobre una coleccion de incidencias y devuelve subconjuntos segun
 * distintos criterios. Es la contraparte de MetricasFlujoService: donde aquel
 * cuenta, este selecciona. No modifica las incidencias; solo consulta.
 *
 * Una incidencia se considera "abierta" cuando no esta FINALIZADA.
 */
public class ConsultaIncidenciasService {

    private final List<Incidencia> incidencias;

    public ConsultaIncidenciasService(List<Incidencia> incidencias) {
        if (incidencias == null) {
            throw new IllegalArgumentException("La lista de incidencias es obligatoria.");
        }
        this.incidencias = incidencias;
    }

    public List<Incidencia> listarTodas() {
        return List.copyOf(incidencias);
    }

    /**
     * @return Optional con la incidencia si existe, o vacio si no. No lanza
     *         excepcion ante un id inexistente (criterio de HU-04).
     */
    public Optional<Incidencia> buscarPorId(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return incidencias.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    public List<Incidencia> filtrarPorEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        return incidencias.stream()
                .filter(i -> i.getEstado() == estado)
                .toList();
    }

    public List<Incidencia> filtrarPorPrioridad(Prioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }
        return incidencias.stream()
                .filter(i -> i.getPrioridad() == prioridad)
                .toList();
    }

    public List<Incidencia> soloAbiertas() {
        return incidencias.stream()
                .filter(i -> i.getEstado() != Estado.FINALIZADA)
                .toList();
    }

    public List<Incidencia> soloFinalizadas() {
        return incidencias.stream()
                .filter(i -> i.getEstado() == Estado.FINALIZADA)
                .toList();
    }
}
