package cr.utn.helpdesk.domain;

/**
 * Estados posibles de una incidencia (HU-03).
 *
 * El flujo autorizado es estrictamente secuencial:
 * REGISTRADA -> LISTA -> EN_DESARROLLO -> EN_VALIDACION -> FINALIZADA
 *
 * Nota de diseno: este enum NO valida transiciones a proposito.
 * Esa responsabilidad vive en {@code service.ValidadorTransiciones},
 * para poder probarla de forma aislada.
 */
public enum Estado {
    REGISTRADA,
    LISTA,
    EN_DESARROLLO,
    EN_VALIDACION,
    FINALIZADA
}
