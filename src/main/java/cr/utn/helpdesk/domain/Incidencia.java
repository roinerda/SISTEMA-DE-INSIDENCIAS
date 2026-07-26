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
    private ClaseServicio claseServicio;

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
        this.claseServicio = ClaseServicio.ESTANDAR;

        this.estado = Estado.REGISTRADA;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCierre = null;
        this.descripcionSolucion = null;
    }

    /**
     * Avanza la incidencia al siguiente estado del flujo.
     *
     * Solo admite la transicion consecutiva inmediata y nunca FINALIZADA:
     * ese estado se alcanza exclusivamente mediante finalizar(), lo que hace
     * imposible cerrar una incidencia sin solucion.
     *
     * @throws IllegalStateException si la transicion es un salto, un retroceso,
     *         una permanencia en el mismo estado, o un intento de ir a FINALIZADA.
     */
    public void avanzarA(Estado nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El estado destino es obligatorio.");
        }
        if (nuevo == Estado.FINALIZADA) {
            throw new IllegalStateException(
                    "Para finalizar una incidencia use finalizar(descripcionSolucion).");
        }
        boolean esConsecutivo = nuevo.ordinal() == this.estado.ordinal() + 1;
        if (!esConsecutivo) {
            throw new IllegalStateException(
                    "Transicion invalida de " + this.estado + " a " + nuevo
                            + ". El flujo solo permite avanzar al estado siguiente.");
        }
        this.estado = nuevo;
    }

    /**
     * Finaliza la incidencia. Unico camino valido hacia FINALIZADA.
     *
     * @throws IllegalStateException    si la incidencia no esta en EN_VALIDACION.
     * @throws IllegalArgumentException si la solucion es nula o esta en blanco.
     */
    public void finalizar(String descripcionSolucion) {
        if (this.estado != Estado.EN_VALIDACION) {
            throw new IllegalStateException(
                    "Solo se puede finalizar una incidencia que esta en EN_VALIDACION. "
                            + "Estado actual: " + this.estado + ".");
        }
        if (descripcionSolucion == null || descripcionSolucion.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripcion de la solucion es obligatoria para finalizar.");
        }
        this.descripcionSolucion = descripcionSolucion;
        this.fechaCierre = LocalDateTime.now();
        this.estado = Estado.FINALIZADA;
    }

    /**
     * Marca la incidencia como EXPEDITE (clase de servicio de atencion
     * prioritaria, HU-06).
     *
     * Solo se permite si la prioridad es CRITICA. Esta es la mitad de la
     * regla que una incidencia puede verificar por si misma; la restriccion
     * de cupo unico (solo una EXPEDITE activa a la vez) es una regla de
     * conjunto y vive en GestorExpedite, porque una incidencia no conoce a
     * las demas.
     *
     * @throws IllegalStateException si la incidencia no es CRITICA.
     */
    public void marcarComoExpedite() {
        if (this.prioridad != Prioridad.CRITICA) {
            throw new IllegalStateException(
                    "Solo una incidencia CRITICA puede marcarse como EXPEDITE. "
                            + "Prioridad actual: " + this.prioridad + ".");
        }
        this.claseServicio = ClaseServicio.EXPEDITE;
    }

    public ClaseServicio getClaseServicio() {
        return claseServicio;
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
