package cr.utn.helpdesk.domain;

/**
 * Clase de servicio de la incidencia (cambio de requerimiento, HU-06).
 *
 * ESTANDAR: comportamiento por defecto, sin restriccion de cupo.
 * EXPEDITE: atencion prioritaria. Solo una incidencia EXPEDITE puede
 *           estar en EN_DESARROLLO o EN_VALIDACION simultaneamente.
 */
public enum ClaseServicio {
    ESTANDAR,
    EXPEDITE
}
