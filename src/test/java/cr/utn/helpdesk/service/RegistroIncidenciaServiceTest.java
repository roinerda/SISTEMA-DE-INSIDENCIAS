package cr.utn.helpdesk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;
import org.junit.jupiter.api.Test;

class RegistroIncidenciaServiceTest {

    @Test
    void registrarIncidencia_calculaPrioridadAutomaticamente() {
        RegistroIncidenciaService servicio = new RegistroIncidenciaService();

        Incidencia incidencia = servicio.registrar(
                "Servidor principal caido",
                "Los usuarios no pueden acceder al sistema.",
                Impacto.ALTO,
                Urgencia.ALTA);

        assertNotNull(incidencia);
        assertEquals(Prioridad.CRITICA, incidencia.getPrioridad());
        assertEquals(1, servicio.cantidadRegistrada());
    }

    @Test
    void registrarIncidenciaNormal_guardaPrioridadNormal() {
        RegistroIncidenciaService servicio = new RegistroIncidenciaService();

        Incidencia incidencia = servicio.registrar(
                "Cambio de fondo de pantalla",
                "El usuario solicita cambiar el fondo.",
                Impacto.BAJO,
                Urgencia.BAJA);

        assertEquals(Prioridad.NORMAL, incidencia.getPrioridad());
    }
}
