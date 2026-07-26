package cr.utn.helpdesk.service;

import cr.utn.helpdesk.domain.ClaseServicio;
import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Incidencia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestor de la clase de servicio EXPEDITE (HU-06, cambio de requerimiento).
 *
 * Administra el conjunto de incidencias y hace cumplir la politica de cupo
 * unico: solo una incidencia EXPEDITE puede estar en EN_DESARROLLO o
 * EN_VALIDACION de forma simultanea.
 *
 * Las transiciones NO se reimplementan: el gestor verifica el cupo y luego
 * delega en el avanzarA() ya probado de la incidencia (HU-03). Las incidencias
 * estandar no pasan por aqui; siguen usando avanzarA() directamente.
 */
public class GestorExpedite {

    private final List<Incidencia> incidencias;

    public GestorExpedite() {
        this.incidencias = new ArrayList<>();
    }

    public void agregar(Incidencia incidencia) {
        if (incidencia == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }
        incidencias.add(incidencia);
    }

    /**
     * Avanza una incidencia a su siguiente estado aplicando la politica de
     * cupo EXPEDITE.
     *
     * @throws IllegalStateException si se viola el cupo unico de EXPEDITE.
     */
    public void avanzar(Incidencia incidencia, Estado destino) {
        if (incidencia == null) {
            throw new IllegalArgumentException("La incidencia es obligatoria.");
        }

        boolean destinoActivo =
                destino == Estado.EN_DESARROLLO || destino == Estado.EN_VALIDACION;

        if (incidencia.getClaseServicio() == ClaseServicio.EXPEDITE
                && destinoActivo
                && existeOtraExpediteActiva(incidencia)) {
            throw new IllegalStateException(
                    "Ya existe una incidencia EXPEDITE activa en EN_DESARROLLO "
                            + "o EN_VALIDACION. Solo se permite una a la vez.");
        }

        incidencia.avanzarA(destino);
    }

    public boolean existeExpediteActiva() {
        return existeOtraExpediteActiva(null);
    }

    private boolean existeOtraExpediteActiva(Incidencia excluida) {
        for (Incidencia i : incidencias) {
            if (i == excluida) {
                continue;
            }
            if (i.getClaseServicio() == ClaseServicio.EXPEDITE
                    && (i.getEstado() == Estado.EN_DESARROLLO
                        || i.getEstado() == Estado.EN_VALIDACION)) {
                return true;
            }
        }
        return false;
    }

    public List<Incidencia> listar() {
        return Collections.unmodifiableList(incidencias);
    }
}
