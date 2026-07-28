package cr.utn.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * HU-05: Metricas basicas de flujo.
 */
class MetricasFlujoServiceTest {

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
                () -> new MetricasFlujoService(null));
    }

    // -----------------------------------------------------------------
    // Totales
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Total de incidencias")
    void totalIncidencias_cuentaTodas() {
        List<Incidencia> lista = new ArrayList<>(List.of(critica(), normal()));
        assertEquals(2, new MetricasFlujoService(lista).totalIncidencias());
    }

    @Test
    @DisplayName("Una lista vacia tiene total cero")
    void listaVacia_totalCero() {
        assertEquals(0, new MetricasFlujoService(new ArrayList<>()).totalIncidencias());
    }

    // -----------------------------------------------------------------
    // Abiertas vs finalizadas
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Cuenta finalizadas y abiertas correctamente")
    void cuentaAbiertasYFinalizadas() {
        List<Incidencia> lista = new ArrayList<>();
        lista.add(finalizada(critica()));
        lista.add(normal());
        Incidencia enDesarrollo = normal();
        enDesarrollo.avanzarA(Estado.LISTA);
        enDesarrollo.avanzarA(Estado.EN_DESARROLLO);
        lista.add(enDesarrollo);

        MetricasFlujoService m = new MetricasFlujoService(lista);

        assertEquals(1, m.cantidadFinalizadas());
        assertEquals(2, m.cantidadAbiertas());
        assertEquals(3, m.totalIncidencias());
    }

    @Test
    @DisplayName("Una incidencia no finalizada cuenta como abierta")
    void incidenciaNoFinalizada_esAbierta() {
        List<Incidencia> lista = new ArrayList<>(List.of(normal()));
        MetricasFlujoService m = new MetricasFlujoService(lista);
        assertEquals(1, m.cantidadAbiertas());
        assertEquals(0, m.cantidadFinalizadas());
    }

    // -----------------------------------------------------------------
    // Lead time (por propiedades, no por valor exacto)
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Lead time de lista vacia es cero")
    void leadTimeListaVacia_esCero() {
        assertEquals(0.0,
                new MetricasFlujoService(new ArrayList<>()).leadTimePromedioMinutos());
    }

    @Test
    @DisplayName("Lead time ignora las incidencias no finalizadas")
    void leadTimeSinFinalizadas_esCero() {
        List<Incidencia> lista = new ArrayList<>(List.of(normal(), critica()));
        assertEquals(0.0,
                new MetricasFlujoService(lista).leadTimePromedioMinutos());
    }

    @Test
    @DisplayName("Lead time con finalizadas es no negativo")
    void leadTimeConFinalizadas_esNoNegativo() {
        List<Incidencia> lista = new ArrayList<>(List.of(finalizada(critica())));
        assertTrue(new MetricasFlujoService(lista).leadTimePromedioMinutos() >= 0.0);
    }

    // -----------------------------------------------------------------
    // Throughput
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Throughput cuenta finalizadas dentro del rango")
    void throughput_cuentaFinalizadasEnRango() {
        List<Incidencia> lista = new ArrayList<>(List.of(finalizada(critica()), normal()));
        MetricasFlujoService m = new MetricasFlujoService(lista);

        LocalDateTime ayer = LocalDateTime.now().minusDays(1);
        LocalDateTime manana = LocalDateTime.now().plusDays(1);

        assertEquals(1, m.throughput(ayer, manana));
    }

    @Test
    @DisplayName("Throughput fuera del rango es cero")
    void throughput_fueraDeRango_esCero() {
        List<Incidencia> lista = new ArrayList<>(List.of(finalizada(critica())));
        MetricasFlujoService m = new MetricasFlujoService(lista);

        LocalDateTime hace3 = LocalDateTime.now().minusDays(3);
        LocalDateTime hace2 = LocalDateTime.now().minusDays(2);

        assertEquals(0, m.throughput(hace3, hace2));
    }

    @Test
    @DisplayName("Throughput con rango nulo lanza excepcion")
    void throughput_rangoNulo_lanzaExcepcion() {
        MetricasFlujoService m = new MetricasFlujoService(new ArrayList<>());
        assertThrows(IllegalArgumentException.class,
                () -> m.throughput(null, LocalDateTime.now()));
    }

    // -----------------------------------------------------------------
    // Conteo por prioridad
    // -----------------------------------------------------------------

    @Test
    @DisplayName("Conteo por prioridad agrupa correctamente")
    void conteoPorPrioridad_agrupa() {
        List<Incidencia> lista = new ArrayList<>(List.of(
                critica(), critica(), normal()));
        Map<Prioridad, Long> conteo =
                new MetricasFlujoService(lista).conteoPorPrioridad();

        assertEquals(2L, conteo.get(Prioridad.CRITICA));
        assertEquals(1L, conteo.get(Prioridad.NORMAL));
    }

    @Test
    @DisplayName("Conteo por prioridad de lista vacia es un mapa vacio")
    void conteoPorPrioridad_listaVacia_mapaVacio() {
        Map<Prioridad, Long> conteo =
                new MetricasFlujoService(new ArrayList<>()).conteoPorPrioridad();
        assertTrue(conteo.isEmpty());
    }
}
