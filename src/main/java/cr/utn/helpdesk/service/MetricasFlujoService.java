package cr.utn.helpdesk.service;

import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Metricas basicas de flujo (HU-05).
 *
 * Opera sobre una coleccion de incidencias y calcula indicadores del tablero:
 * totales, throughput, lead time promedio y conteo por prioridad.
 *
 * Una incidencia se considera "abierta" cuando no esta FINALIZADA. El lead
 * time se mide en minutos para ofrecer precision en incidencias de corta
 * duracion.
 */
public class MetricasFlujoService {

    private final List<Incidencia> incidencias;

    public MetricasFlujoService(List<Incidencia> incidencias) {
        if (incidencias == null) {
            throw new IllegalArgumentException("La lista de incidencias es obligatoria.");
        }
        this.incidencias = incidencias;
    }

    public long totalIncidencias() {
        return incidencias.size();
    }

    public long cantidadAbiertas() {
        return incidencias.stream()
                .filter(i -> i.getEstado() != Estado.FINALIZADA)
                .count();
    }

    public long cantidadFinalizadas() {
        return incidencias.stream()
                .filter(i -> i.getEstado() == Estado.FINALIZADA)
                .count();
    }

    public long throughput(LocalDateTime desde, LocalDateTime hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("El rango de fechas es obligatorio.");
        }
        return incidencias.stream()
                .filter(i -> i.getEstado() == Estado.FINALIZADA)
                .filter(i -> {
                    LocalDateTime cierre = i.getFechaCierre();
                    return cierre != null
                            && !cierre.isBefore(desde)
                            && !cierre.isAfter(hasta);
                })
                .count();
    }

    public double leadTimePromedioMinutos() {
        List<Incidencia> finalizadas = incidencias.stream()
                .filter(i -> i.getEstado() == Estado.FINALIZADA)
                .filter(i -> i.getFechaCierre() != null)
                .toList();

        if (finalizadas.isEmpty()) {
            return 0.0;
        }

        return finalizadas.stream()
                .mapToLong(i -> Duration.between(
                        i.getFechaCreacion(),
                        i.getFechaCierre()).toMinutes())
                .average()
                .orElse(0.0);
    }

    public Map<Prioridad, Long> conteoPorPrioridad() {
        Map<Prioridad, Long> conteo = new EnumMap<>(Prioridad.class);
        for (Incidencia i : incidencias) {
            conteo.merge(i.getPrioridad(), 1L, Long::sum);
        }
        return conteo;
    }
}
