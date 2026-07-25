package cr.utn.helpdesk.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HU-03: Gestionar el flujo de la incidencia.
 *
 * Flujo autorizado:
 *   REGISTRADA -> LISTA -> EN_DESARROLLO -> EN_VALIDACION -> FINALIZADA
 *
 * Contrato de la API (opcion A: la incidencia gobierna su propio ciclo):
 *
 *   - avanzarA(Estado)        cubre las tres transiciones consecutivas
 *                             normales. NO acepta FINALIZADA.
 *   - finalizar(String)       unico camino hacia FINALIZADA. Exige la
 *                             descripcion de la solucion. Solo valido
 *                             desde EN_VALIDACION.
 *
 * Al separar el cierre en finalizar(), resulta imposible por diseno pasar
 * a FINALIZADA sin solucion: no existe otra puerta hacia ese estado.
 */
class TransicionEstadoTest {

    private Incidencia nueva() {
        return new Incidencia(
                "Impresora sin responder",
                "La impresora del laboratorio 3 no responde al enviar trabajos",
                "HARDWARE",
                Impacto.MEDIO,
                Urgencia.MEDIA);
    }

    // Lleva la incidencia hasta EN_VALIDACION recorriendo el flujo valido.
    private Incidencia hastaValidacion() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        i.avanzarA(Estado.EN_DESARROLLO);
        i.avanzarA(Estado.EN_VALIDACION);
        return i;
    }

    // -----------------------------------------------------------------
    // Transiciones consecutivas validas
    // -----------------------------------------------------------------

    @Test
    @DisplayName("REGISTRADA avanza a LISTA")
    void registradaALista_esValida() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        assertEquals(Estado.LISTA, i.getEstado());
    }

    @Test
    @DisplayName("LISTA avanza a EN_DESARROLLO")
    void listaAEnDesarrollo_esValida() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        i.avanzarA(Estado.EN_DESARROLLO);
        assertEquals(Estado.EN_DESARROLLO, i.getEstado());
    }

    @Test
    @DisplayName("EN_DESARROLLO avanza a EN_VALIDACION")
    void enDesarrolloAEnValidacion_esValida() {
        Incidencia i = hastaValidacion();
        assertEquals(Estado.EN_VALIDACION, i.getEstado());
    }

    // -----------------------------------------------------------------
    // Saltos: invalidos
    // -----------------------------------------------------------------

    @Test
    @DisplayName("REGISTRADA no puede saltar a EN_DESARROLLO")
    void registradaAEnDesarrollo_esInvalida() {
        Incidencia i = nueva();
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.EN_DESARROLLO));
    }

    @Test
    @DisplayName("REGISTRADA no puede saltar directo a FINALIZADA con avanzarA")
    void registradaAFinalizada_esInvalida() {
        Incidencia i = nueva();
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.FINALIZADA));
    }

    @Test
    @DisplayName("LISTA no puede saltar a EN_VALIDACION")
    void listaAEnValidacion_esInvalida() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.EN_VALIDACION));
    }

    // -----------------------------------------------------------------
    // Retrocesos: invalidos
    // -----------------------------------------------------------------

    @Test
    @DisplayName("EN_DESARROLLO no puede retroceder a LISTA")
    void enDesarrolloALista_esInvalida() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        i.avanzarA(Estado.EN_DESARROLLO);
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.LISTA));
    }

    @Test
    @DisplayName("Una incidencia no puede avanzar al mismo estado en que esta")
    void avanzarAlMismoEstado_esInvalida() {
        Incidencia i = nueva();
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.REGISTRADA));
    }

    // -----------------------------------------------------------------
    // avanzarA no es una puerta a FINALIZADA
    // -----------------------------------------------------------------

    @Test
    @DisplayName("avanzarA(FINALIZADA) desde EN_VALIDACION es invalido: hay que usar finalizar()")
    void avanzarAFinalizadaDesdeValidacion_esInvalida() {
        Incidencia i = hastaValidacion();
        assertThrows(IllegalStateException.class,
                () -> i.avanzarA(Estado.FINALIZADA));
    }

    // -----------------------------------------------------------------
    // Cierre con finalizar()
    // -----------------------------------------------------------------

    @Test
    @DisplayName("finalizar desde EN_VALIDACION con solucion cierra la incidencia")
    void finalizarConSolucion_cierraLaIncidencia() {
        Incidencia i = hastaValidacion();
        i.finalizar("Se reemplazo el toner y se reinicio la cola de impresion");
        assertEquals(Estado.FINALIZADA, i.getEstado());
    }

    @Test
    @DisplayName("finalizar registra la fecha de cierre")
    void finalizar_registraLaFechaDeCierre() {
        Incidencia i = hastaValidacion();

        LocalDateTime antes = LocalDateTime.now();
        i.finalizar("Se reemplazo el toner");
        LocalDateTime despues = LocalDateTime.now();

        assertNotNull(i.getFechaCierre());
        assertEquals(true, !i.getFechaCierre().isBefore(antes));
        assertEquals(true, !i.getFechaCierre().isAfter(despues));
    }

    @Test
    @DisplayName("finalizar guarda la descripcion de la solucion")
    void finalizar_guardaLaDescripcionDeSolucion() {
        Incidencia i = hastaValidacion();
        i.finalizar("Se reemplazo el toner");
        assertEquals("Se reemplazo el toner", i.getDescripcionSolucion());
    }

    @Test
    @DisplayName("finalizar sin solucion lanza excepcion")
    void finalizarSinSolucion_lanzaExcepcion() {
        Incidencia i = hastaValidacion();
        assertThrows(IllegalArgumentException.class,
                () -> i.finalizar(null));
    }

    @Test
    @DisplayName("finalizar con solucion en blanco lanza excepcion")
    void finalizarConSolucionEnBlanco_lanzaExcepcion() {
        Incidencia i = hastaValidacion();
        assertThrows(IllegalArgumentException.class,
                () -> i.finalizar("    "));
    }

    @Test
    @DisplayName("No se puede finalizar antes de llegar a EN_VALIDACION")
    void finalizarAntesDeValidacion_esInvalida() {
        Incidencia i = nueva();
        i.avanzarA(Estado.LISTA);
        assertThrows(IllegalStateException.class,
                () -> i.finalizar("Solucion prematura"));
    }

    @Test
    @DisplayName("Una incidencia recien creada no tiene fecha de cierre")
    void incidenciaNueva_noTieneFechaDeCierre() {
        assertNull(nueva().getFechaCierre());
    }
}
