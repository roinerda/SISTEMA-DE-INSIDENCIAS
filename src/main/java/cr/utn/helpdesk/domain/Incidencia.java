package cr.utn.helpdesk.domain;

import cr.utn.helpdesk.service.CalculadoraPrioridad;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Incidencia tecnica registrada en el sistema.
 *
 * Combina el registro con validaciones (HU-01) y el calculo automatico
 * de prioridad (HU-02).
 *
 * Decisiones de diseno:
 *
 *  - Las validaciones viven en el constructor, de modo que no puede existir
 *    una incidencia en estado invalido, ni siquiera de forma transitoria.
 *
 *  - La prioridad se calcula dentro del constructor mediante
 *    CalculadoraPrioridad. El enunciado la define como "prioridad calculada"
 *    (dato minimo 1.2) y pide que el sistema la calcule para evitar
 *    decisiones arbitrarias (HU-02). Calcularla aqui garantiza que no exista
 *    forma de asignarla manualmente: no hay metodo setter para ella.
 */
public class Incidencia {

    private static final int LONGITUD_MINIMA_DESCRIPCION = 10;
    private static final String CATEGORIA_POR_DEFECTO = "GENERAL";

    private final String id;
    private final String titulo;
    private final String descripcion;
    private final String categoria;
    private final Impacto impacto;
    private final Urgencia urgencia;
    private final Prioridad prioridad;

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
            throw new IllegalArgumentException(
                    "El titulo no puede estar vacio.");
        }
        if (descripcion == null
                || descripcion.length() < LONGITUD_MINIMA_DESCRIPCION) {
            throw new IllegalArgumentException(
                    "La descripcion debe contener al menos "
                            + LONGITUD_MINIMA_DESCRIPCION + " caracteres.");
        }
        if (impacto == null) {
            throw new IllegalArgumentException(
                    "El impacto de la incidencia es obligatorio.");
        }
        if (urgencia == null) {
            throw new IllegalArgumentException(
                    "La urgencia de la incidencia es obligatoria.");
        }

        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = (categoria == null || categoria.isBlank())
                ? CATEGORIA_POR_DEFECTO
                : categoria;
        this.impacto = impacto;
        this.urgencia = urgencia;
        this.prioridad = CalculadoraPrioridad.calcular(impacto, urgencia);

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

    public Prioridad getPrioridad() {
        return prioridad;
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
