package cr.utn.helpdesk;

import cr.utn.helpdesk.ui.ConsolaHelpDesk;
import java.util.Scanner;

/**
 * Punto de entrada de HelpDesk Flow.
 *
 * No contiene logica de negocio: solo crea la consola y la arranca.
 * Toda la logica vive en los paquetes domain y service.
 */
public final class Main {

    private Main() {
        // Clase de utilidad: no se instancia.
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            new ConsolaHelpDesk(sc).iniciar();
        }
    }
}
