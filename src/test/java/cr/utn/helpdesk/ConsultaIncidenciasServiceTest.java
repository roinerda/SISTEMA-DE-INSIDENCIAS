package cr.utn.helpdesk;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ConsultaIncidenciasServiceTest {


    private ConsultaIncidenciasService service;


    @BeforeEach
    void setup(){

        IncidenciaRepository repository =
                new IncidenciaRepository();


        Incidencia una =
                new Incidencia(
                        1L,
                        "Servidor caído",
                        "No responde",
                        Impacto.ALTO,
                        Urgencia.ALTA
                );


        Incidencia dos =
                new Incidencia(
                        2L,
                        "Error usuario",
                        "Problema menor",
                        Impacto.BAJO,
                        Urgencia.BAJA
                );


        dos.finalizar();


        repository.guardar(una);
        repository.guardar(dos);


        service =
                new ConsultaIncidenciasService(
                        repository
                );
    }



    @Test
    void filtrarPorEstado_devuelveSoloEseEstado(){

        var resultado =
                service.filtrarPorEstado(
                        Estado.ABIERTA
                );


        assertEquals(
                1,
                resultado.size()
        );

    }



    @Test
    void filtrarPorPrioridad_devuelveSoloEsaPrioridad(){

        var resultado =
                service.filtrarPorPrioridad(
                        Prioridad.CRITICA
                );


        assertEquals(
                1,
                resultado.size()
        );

    }



    @Test
    void buscarPorIdInexistente_devuelveVacio(){

        var resultado =
                service.buscarPorId(
                        999L
                );


        assertTrue(
                resultado.isEmpty()
        );

    }

}