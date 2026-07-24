package cr.utn.helpdesk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cr.utn.helpdesk.domain.Estado;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Prueba de arranque del proyecto.
 *
 * Confirma dos cosas el dia 1: que JUnit 5 esta correctamente enlazado
 * y que la maquina de estados tiene exactamente los cinco estados que
 * define el enunciado. Si alguien agrega o quita un estado sin discutirlo
 * con la pareja, esta prueba falla y obliga a la conversacion.
 */
class ToolchainTest {

    @Test
    @DisplayName("El flujo define exactamente cinco estados")
    void elFlujoDefineExactamenteCincoEstados() {
        assertEquals(5, Estado.values().length);
    }

    @Test
    @DisplayName("El primer estado del flujo es REGISTRADA")
    void elPrimerEstadoDelFlujoEsRegistrada() {
        assertEquals(Estado.REGISTRADA, Estado.values()[0]);
    }

    @Test
    @DisplayName("El ultimo estado del flujo es FINALIZADA")
    void elUltimoEstadoDelFlujoEsFinalizada() {
        Estado[] estados = Estado.values();
        assertEquals(Estado.FINALIZADA, estados[estados.length - 1]);
    }
}
