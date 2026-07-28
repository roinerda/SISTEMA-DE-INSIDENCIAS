package cr.utn.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HU-04: Consultar y filtrar incidencias.
 */
class ConsultaIncidenciasServiceTest {

    private Incidencia critica() {
        return new Incidencia("Servidor principal caido",
                "Los usuarios no pueden acceder al sistema entero",
                "INFRA", Impacto.ALTO, Urgencia.ALTA);
    }

    private Incidencia normal() {
        return new Incidencia("Cambio de fondo de pantalla",
                "El usuario solicita cambiar el fondo de pantalla",
                "SOPORTE", Impacto.BAJO, Urgencia.BAJA);
    }

    private Incidencia finalizada(Incidencia i) {
        i.avanzarA(Estado.LISTA);
        i.avanzarA(Estado.EN_DESARROLLO);
        i.avanzarA(Estado.EN_VALIDACION);
        i.finalizar("Resuelto y validado con el usuario");
        return i;
    }

    // -----------------------------------------------------------------
    // Construccion
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Construir con lista nula lanza excepcion")
    void construirConListaNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConsultaIncidenciasService(null));
    }

    // -----------------------------------------------------------------
    // Listar todas
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Listar todas devuelve la coleccion completa")
    void listarTodas_devuelveTodas() {
        List<Incidencia> lista = new ArrayList<>(List.of(critica(), normal()));
        assertEquals(2, new ConsultaIncidenciasService(lista).listarTodas().size());
    }

    @Test
    @DisplayName("Listar todas de una coleccion vacia devuelve lista vacia")
    void listarTodas_vacia() {
        assertEquals(0,
                new ConsultaIncidenciasService(new ArrayList<>()).listarTodas().size());
    }

    @Test
    @DisplayName("La lista devuelta por listarTodas es inmutable")
    void listarTodas_esInmutable() {
        ConsultaIncidenciasService s =
                new ConsultaIncidenciasService(new ArrayList<>(List.of(critica())));
        assertThrows(UnsupportedOperationException.class,
                () -> s.listarTodas().add(normal()));
    }

    // -----------------------------------------------------------------
    // Buscar por id
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Buscar por id encuentra la incidencia correcta")
    void buscarPorId_encuentra() {
        Incidencia c = critica();
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(c, normal())));

        Optional<Incidencia> resultado = s.buscarPorId(c.getId());

        assertTrue(resultado.isPresent());
        assertEquals(c.getId(), resultado.get().getId());
    }

    @Test
    @DisplayName("Buscar un id inexistente devuelve vacio, no lanza excepcion")
    void buscarPorId_inexistente_devuelveVacio() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(critica())));
        assertTrue(s.buscarPorId("id-que-no-existe").isEmpty());
    }

    @Test
    @DisplayName("Buscar con id nulo devuelve vacio")
    void buscarPorId_nulo_devuelveVacio() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(critica())));
        assertTrue(s.buscarPorId(null).isEmpty());
    }

    // -----------------------------------------------------------------
    // Filtrar por estado
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Filtrar por estado devuelve solo las de ese estado")
    void filtrarPorEstado_devuelveSoloEseEstado() {
        Incidencia enDesarrollo = normal();
        enDesarrollo.avanzarA(Estado.LISTA);
        enDesarrollo.avanzarA(Estado.EN_DESARROLLO);
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(critica(), enDesarrollo)));

        assertEquals(1, s.filtrarPorEstado(Estado.REGISTRADA).size());
        assertEquals(1, s.filtrarPorEstado(Estado.EN_DESARROLLO).size());
        assertEquals(0, s.filtrarPorEstado(Estado.FINALIZADA).size());
    }

    @Test
    @DisplayName("Filtrar por estado nulo lanza excepcion")
    void filtrarPorEstado_nulo_lanzaExcepcion() {
        ConsultaIncidenciasService s =
                new ConsultaIncidenciasService(new ArrayList<>());
        assertThrows(IllegalArgumentException.class,
                () -> s.filtrarPorEstado(null));
    }

    // -----------------------------------------------------------------
    // Filtrar por prioridad
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Filtrar por prioridad devuelve solo las de esa prioridad")
    void filtrarPorPrioridad_devuelveSoloEsaPrioridad() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(critica(), critica(), normal())));

        assertEquals(2, s.filtrarPorPrioridad(Prioridad.CRITICA).size());
        assertEquals(1, s.filtrarPorPrioridad(Prioridad.NORMAL).size());
        assertEquals(0, s.filtrarPorPrioridad(Prioridad.ALTA).size());
    }

    @Test
    @DisplayName("Filtrar por prioridad nula lanza excepcion")
    void filtrarPorPrioridad_nula_lanzaExcepcion() {
        ConsultaIncidenciasService s =
                new ConsultaIncidenciasService(new ArrayList<>());
        assertThrows(IllegalArgumentException.class,
                () -> s.filtrarPorPrioridad(null));
    }

    // -----------------------------------------------------------------
    // Abiertas y finalizadas
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Solo abiertas devuelve las que no estan finalizadas")
    void soloAbiertas_devuelveNoFinalizadas() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(finalizada(critica()), normal(), normal())));
        assertEquals(2, s.soloAbiertas().size());
    }

    @Test
    @DisplayName("Solo finalizadas devuelve unicamente las finalizadas")
    void soloFinalizadas_devuelveFinalizadas() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(finalizada(critica()), normal())));
        assertEquals(1, s.soloFinalizadas().size());
    }

    @Test
    @DisplayName("Sin finalizadas, soloFinalizadas devuelve lista vacia")
    void soloFinalizadas_sinFinalizadas_vacia() {
        ConsultaIncidenciasService s = new ConsultaIncidenciasService(
                new ArrayList<>(List.of(normal(), critica())));
        assertTrue(s.soloFinalizadas().isEmpty());
        assertFalse(s.soloAbiertas().isEmpty());
    }
}
