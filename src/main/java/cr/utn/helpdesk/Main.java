package cr.utn.helpdesk;

/**
 * Punto de entrada de HelpDesk Flow.
 *
 * Por decision de diseno esta clase no contiene logica de negocio:
 * unicamente arma las dependencias y arranca la interfaz de consola.
 * Toda la logica probable vive en el paquete {@code service}.
 */
public final class Main {

    private Main() {
        // Clase de utilidad: no se instancia.
    }

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("  HelpDesk Flow - UTN ITI-822");
        System.out.println("=================================");
        System.out.println("Proyecto base configurado correctamente.");

        // TODO (TEC-03): reemplazar por el arranque real de la consola.
        //   var repositorio = new RepositorioEnMemoria();
        //   var servicio    = new ServicioIncidencias(repositorio, new CalculadoraPrioridad());
        //   new ConsolaHelpDesk(servicio, new ServicioMetricas(repositorio)).iniciar();
    }
}
