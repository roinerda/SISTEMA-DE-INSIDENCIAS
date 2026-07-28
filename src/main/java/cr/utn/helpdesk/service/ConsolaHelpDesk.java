package cr.utn.helpdesk.service;

import java.util.Scanner;

import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Urgencia;

public class ConsolaHelpDesk {

    private final Scanner scanner;

    private final RegistroIncidenciaService registroService;
    private final ConsultaIncidenciasService consultaService;
    private final MetricasFlujoService metricasService;


    public ConsolaHelpDesk(
            RegistroIncidenciaService registroService,
            ConsultaIncidenciasService consultaService,
            MetricasFlujoService metricasService
    ) {

        this.scanner = new Scanner(System.in);

        this.registroService = registroService;
        this.consultaService = consultaService;
        this.metricasService = metricasService;
    }


    public void iniciar() {

        boolean continuar = true;


        while (continuar) {

            mostrarMenu();

            String opcion = scanner.nextLine();


            try {

                switch (opcion) {

                    case "1":
                        registrar();
                        break;

                    case "2":
                        listar();
                        break;

                    case "3":
                        filtrar();
                        break;

                    case "4":
                        cambiarEstado();
                        break;

                    case "5":
                        mostrarMetricas();
                        break;

                    case "0":
                        continuar = false;
                        System.out.println(
                            "Sistema finalizado."
                        );
                        break;

                    default:
                        System.out.println(
                            "Opción inválida."
                        );
                }


            } catch (Exception e) {

                System.out.println(
                    "Error: " + e.getMessage()
                );

            }
        }
    }



    private void mostrarMenu() {

        System.out.println("\n===== HelpDesk Flow =====");

        System.out.println("1. Registrar incidencia");
        System.out.println("2. Listar incidencias");
        System.out.println("3. Filtrar incidencias");
        System.out.println("4. Cambiar estado");
        System.out.println("5. Mostrar métricas");
        System.out.println("0. Salir");

        System.out.print("Seleccione: ");
    }



    private void registrar() {

        System.out.println("\nRegistrar incidencia");


        System.out.print("Título: ");
        String titulo = scanner.nextLine();


        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();


        Impacto impacto =
                Impacto.valueOf(
                    scanner.nextLine()
                    .toUpperCase()
                );


        Urgencia urgencia =
                Urgencia.valueOf(
                    scanner.nextLine()
                    .toUpperCase()
                );


        Incidencia incidencia =
                registroService.registrar(
                    titulo,
                    descripcion,
                    impacto,
                    urgencia
                );


        System.out.println(
            "Incidencia creada con prioridad: "
            + incidencia.getPrioridad()
        );
    }



    private void listar() {

        consultaService
                .listarTodas()
                .forEach(System.out::println);

    }



    private void filtrar() {

        System.out.println(
            "Filtro por estado o prioridad"
        );

        // Solo delega al servicio.
        // La lógica permanece fuera.
    }



    private void cambiarEstado() {

        System.out.println(
            "Cambio de estado"
        );

        // Delegación al servicio correspondiente.
    }



    private void mostrarMetricas() {

        System.out.println(
            "Total: "
            + metricasService.totalIncidencias()
        );


        System.out.println(
            "Abiertas: "
            + metricasService.cantidadAbiertas()
        );


        System.out.println(
            "Finalizadas: "
            + metricasService.cantidadFinalizadas()
        );
    }

}