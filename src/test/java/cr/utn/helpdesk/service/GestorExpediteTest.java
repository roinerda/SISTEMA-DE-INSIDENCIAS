package cr.utn.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cr.utn.helpdesk.domain.ClaseServicio;
import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Urgencia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HU-06: Clase de servicio EXPEDITE.
 *
 * Verifica la regla de conjunto (cupo unico) y la convivencia con las
 * incidencias estandar. La regla individual "solo CRITICA puede ser EXPEDITE"
 * se prueba tambien aqui a traves de la incidencia.
 */
class GestorExpediteTest {

    private Incidencia critica() {
        return new Incidencia(
                "Servidor principal caido",
                "Los usuarios no pueden acceder al sistema para nada",
                "INFRA",
                Impacto.ALTO,
                Urgencia.ALTA);
    }

    private Incidencia normal() {
        return new Incidencia(
                "Cambio de fondo de pantalla",
                "El usuario solicita cambiar el fondo de pantalla",
                "SOPORTE",
                Impacto.BAJO,
                Urgencia.BAJA);
    }

    // Lleva una incidencia a EN_DESARROLLO a traves del gestor.
    private void llevarADesarrollo(GestorExpedite gestor, Incidencia i) {
        gestor.avanzar(i, Estado.LISTA);
        gestor.avanzar(i, Estado.EN_DESARROLLO);
    }

    // -----------------------------------------------------------------
    // Regla individual: solo CRITICA puede ser EXPEDITE
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Toda incidencia nace como ESTANDAR")
    void incidenciaNueva_esEstandar() {
        assertEquals(ClaseServicio.ESTANDAR, critica().getClaseServicio());
    }

    @Test
    @DisplayName("Una incidencia critica puede marcarse como EXPEDITE")
    void incidenciaCritica_puedeMarcarseExpedite() {
        Incidencia i = critica();
        i.marcarComoExpedite();
        assertEquals(ClaseServicio.EXPEDITE, i.getClaseServicio());
    }

    @Test
    @DisplayName("Una incidencia no critica no puede marcarse como EXPEDITE")
    void incidenciaNoCritica_noPuedeMarcarseExpedite() {
        assertThrows(IllegalStateException.class,
                () -> normal().marcarComoExpedite());
    }

    // -----------------------------------------------------------------
    // Regla de conjunto: cupo unico
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Una EXPEDITE puede entrar a EN_DESARROLLO si no hay otra activa")
    void primeraExpedite_entraSinProblema() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia e = critica();
        e.marcarComoExpedite();
        gestor.agregar(e);

        llevarADesarrollo(gestor, e);

        assertEquals(Estado.EN_DESARROLLO, e.getEstado());
    }

    @Test
    @DisplayName("Solo una EXPEDITE puede estar activa en EN_DESARROLLO o EN_VALIDACION")
    void soloUnaExpedite_puedeEstarActiva() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia e1 = critica();
        Incidencia e2 = critica();
        e1.marcarComoExpedite();
        e2.marcarComoExpedite();
        gestor.agregar(e1);
        gestor.agregar(e2);

        llevarADesarrollo(gestor, e1);
        gestor.avanzar(e2, Estado.LISTA);

        assertThrows(IllegalStateException.class,
                () -> gestor.avanzar(e2, Estado.EN_DESARROLLO));
    }

    @Test
    @DisplayName("Al finalizar la EXPEDITE activa se libera el cupo")
    void alFinalizarExpedite_seLiberaElCupo() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia e1 = critica();
        Incidencia e2 = critica();
        e1.marcarComoExpedite();
        e2.marcarComoExpedite();
        gestor.agregar(e1);
        gestor.agregar(e2);

        gestor.avanzar(e1, Estado.LISTA);
        gestor.avanzar(e1, Estado.EN_DESARROLLO);
        gestor.avanzar(e1, Estado.EN_VALIDACION);
        e1.finalizar("Se restauro el servicio y se valido con el usuario");

        assertFalse(gestor.existeExpediteActiva());

        gestor.avanzar(e2, Estado.LISTA);
        gestor.avanzar(e2, Estado.EN_DESARROLLO);
        assertEquals(Estado.EN_DESARROLLO, e2.getEstado());
    }

    @Test
    @DisplayName("existeExpediteActiva refleja si hay una EXPEDITE en estado activo")
    void existeExpediteActiva_reflejaElEstado() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia e = critica();
        e.marcarComoExpedite();
        gestor.agregar(e);

        assertFalse(gestor.existeExpediteActiva());
        llevarADesarrollo(gestor, e);
        assertTrue(gestor.existeExpediteActiva());
    }

    // -----------------------------------------------------------------
    // Convivencia y regresion
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Una incidencia estandar avanza aunque haya una EXPEDITE activa")
    void estandarConvive_conExpediteActiva() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia ex = critica();
        ex.marcarComoExpedite();
        Incidencia st = normal();
        gestor.agregar(ex);
        gestor.agregar(st);

        llevarADesarrollo(gestor, ex);
        llevarADesarrollo(gestor, st);

        assertEquals(Estado.EN_DESARROLLO, st.getEstado());
    }

    @Test
    @DisplayName("Dos incidencias estandar pueden estar activas a la vez")
    void dosEstandar_puedenEstarActivas() {
        GestorExpedite gestor = new GestorExpedite();
        Incidencia s1 = normal();
        Incidencia s2 = normal();
        gestor.agregar(s1);
        gestor.agregar(s2);

        llevarADesarrollo(gestor, s1);
        llevarADesarrollo(gestor, s2);

        assertEquals(Estado.EN_DESARROLLO, s1.getEstado());
        assertEquals(Estado.EN_DESARROLLO, s2.getEstado());
    }
}
