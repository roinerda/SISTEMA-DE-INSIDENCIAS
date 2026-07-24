package cr.utn.helpdesk.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



class IncidenciaTest {

    // Datos validos reutilizables, para que cada prueba solo varie
    // el campo que esta poniendo a prueba.
    private static final String TITULO_VALIDO = "Impresora sin responder";
    private static final String DESCRIPCION_VALIDA = "La impresora del laboratorio 3 no responde al enviar trabajos";
    private static final String CATEGORIA_VALIDA = "HARDWARE";

    private Incidencia incidenciaValida() {
        return new Incidencia(
            TITULO_VALIDO,
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA);
    }

    // -----------------------------------------------------------------
    // Criterio: el titulo no puede estar vacio
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Un titulo vacio lanza excepcion")
    void tituloVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            "",
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA));
    }

    @Test
    @DisplayName("Un titulo con solo espacios lanza excepcion")
    void tituloConSoloEspacios_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            "     ",
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA));
    }

    @Test
    @DisplayName("Un titulo nulo lanza excepcion")
    void tituloNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            null,
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA));
    }

    // -----------------------------------------------------------------
    // Criterio: la descripcion debe contener al menos diez caracteres
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Una descripcion de menos de diez caracteres lanza excepcion")
    void descripcionMenorADiezCaracteres_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            TITULO_VALIDO,
            "muy corta",   // 9 caracteres
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA));
    }

    @Test
    @DisplayName("Una descripcion de exactamente diez caracteres es aceptada")
    void descripcionDeExactamenteDiezCaracteres_esAceptada() {
        Incidencia incidencia = new Incidencia(
            TITULO_VALIDO,
            "1234567890",   // 10 caracteres, el limite inferior
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            Urgencia.MEDIA);

        assertNotNull(incidencia);
    }

    // -----------------------------------------------------------------
    // Criterio: impacto y urgencia deben ser valores validos
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Un impacto nulo lanza excepcion")
    void impactoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            TITULO_VALIDO,
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            null,
            Urgencia.MEDIA));
    }

    @Test
    @DisplayName("Una urgencia nula lanza excepcion")
    void urgenciaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Incidencia(
            TITULO_VALIDO,
            DESCRIPCION_VALIDA,
            CATEGORIA_VALIDA,
            Impacto.MEDIO,
            null));
    }



    @Test
    @DisplayName("Una incidencia valida genera un identificador unico")
    void incidenciaValida_generaIdentificadorUnico() {
        Incidencia incidencia = incidenciaValida();

        assertAll(
            () -> assertNotNull(incidencia.getId()),
            () -> assertTrue(!incidencia.getId().isBlank()));
    }

    @Test
    @DisplayName("Dos incidencias tienen identificadores distintos")
    void dosIncidencias_tienenIdentificadoresDistintos() {
        Incidencia primera = incidenciaValida();
        Incidencia segunda = incidenciaValida();

        assertNotEquals(primera.getId(), segunda.getId());
    }



    @Test
    @DisplayName("Al registrar se guarda la fecha de creacion")
    void alRegistrar_seGuardaLaFechaDeCreacion() {
        LocalDateTime antes = LocalDateTime.now();
        Incidencia incidencia = incidenciaValida();
        LocalDateTime despues = LocalDateTime.now();

        assertAll(
            () -> assertNotNull(incidencia.getFechaCreacion()),
            () -> assertTrue(!incidencia.getFechaCreacion().isBefore(antes)),
            () -> assertTrue(!incidencia.getFechaCreacion().isAfter(despues)));
    }

    // -----------------------------------------------------------------
    // Estado inicial del flujo (HU-03 arranca desde aqui)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Una incidencia nueva queda en estado REGISTRADA")
    void incidenciaNueva_quedaEnEstadoRegistrada() {
        assertEquals(Estado.REGISTRADA, incidenciaValida().getEstado());
    }

    @Test
    @DisplayName("Una incidencia nueva no tiene fecha de cierre ni solucion")
    void incidenciaNueva_noTieneFechaDeCierreNiSolucion() {
        Incidencia incidencia = incidenciaValida();

        assertAll(
            () -> assertEquals(null, incidencia.getFechaCierre()),
            () -> assertEquals(null, incidencia.getDescripcionSolucion()));
    }
}
