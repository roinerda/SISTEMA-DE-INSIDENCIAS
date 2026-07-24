package cr.utn.helpdesk.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Incidencia tecnica registrada en el sistema (HU-01).
 *
 * Las validaciones viven en el constructor para que no pueda existir
 * una incidencia en estado invalido, ni siquiera de forma transitoria.
 */
public class Incidencia {

    private static final int LONGITUD_MINIMA_DESCRIPCION = 10;

    private final String id;
    private final String titulo;
    private final String descripcion;
    private final String categoria;
    private final Impacto impacto;
    private final Urgencia urgencia;

    private Estado estado;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaCierre;
    private String descripcionSolucion;

    public Incidencia(String titulo,
                      String descripcion,
                      String categoria,
                      Impacto impacto,
                      Urgencia urgencia) {

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }

        if (descripcion == null || descripcion.length() < LONGITUD_MINIMA_DESCRIPCION) {
            throw new IllegalArgumentException(
                "La descripcion debe tener al menos " + LONGITUD_MINIMA_DESCRIPCION + " caracteres");
        }

        if (impacto == null) {
            throw new IllegalArgumentException("El impacto no puede ser nulo");
        }

        if (urgencia == null) {
            throw new IllegalArgumentException("La urgencia no puede ser nula");
        }

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.impacto = impacto;
        this.urgencia = urgencia;

        this.id = UUID.randomUUID().toString();

        this.estado = Estado.REGISTRADA;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCierre = null;
        this.descripcionSolucion = null;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public Urgencia getUrgencia() {
        return urgencia;
    }

    public Estado getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public String getDescripcionSolucion() {
        return descripcionSolucion;
    }
}
