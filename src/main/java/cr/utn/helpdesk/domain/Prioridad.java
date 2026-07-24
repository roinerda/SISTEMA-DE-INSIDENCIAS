package cr.utn.helpdesk.domain;

/**
 * Prioridad calculada por el sistema (HU-02).
 *
 * Nunca se asigna manualmente: siempre es resultado de aplicar
 * las reglas de negocio sobre {@link Impacto} y {@link Urgencia}.
 */
public enum Prioridad {
    NORMAL,
    ALTA,
    CRITICA
}
