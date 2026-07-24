package cr.utn.helpdesk;

import java.time.LocalDateTime;
import java.util.Objects;


public class Incidencia {

    private final String titulo;
    private final String descripcion;
    private final Impacto impacto;
    private final Urgencia urgencia;
    private final Prioridad prioridad;
    private final LocalDateTime fechaRegistro;

    public Incidencia(
        String titulo,
        String descripcion,
        Impacto impacto,
        Urgencia urgencia
    ) {
        validarTitulo(titulo);
        validarDescripcion(descripcion);

        this.titulo = titulo.trim();
        this.descripcion = descripcion.trim();
        this.impacto = Objects.requireNonNull(
            impacto,
            "El impacto es obligatorio."
        );
        this.urgencia = Objects.requireNonNull(
            urgencia,
            "La urgencia es obligatoria."
        );

        // La prioridad se calcula automáticamente.
        this.prioridad = CalculadoraPrioridad.calcular(
            impacto,
            urgencia
        );

        this.fechaRegistro = LocalDateTime.now();
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException(
                "El título de la incidencia es obligatorio."
            );
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException(
                "La descripción de la incidencia es obligatoria."
            );
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public Urgencia getUrgencia() {
        return urgencia;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return "Incidencia{" +
            "titulo='" + titulo + '\'' +
            ", descripcion='" + descripcion + '\'' +
            ", impacto=" + impacto +
            ", urgencia=" + urgencia +
            ", prioridad=" + prioridad +
            ", fechaRegistro=" + fechaRegistro +
            '}';
    }
}
