package cr.utn.helpdesk.ui;

import cr.utn.helpdesk.domain.ClaseServicio;
import cr.utn.helpdesk.domain.Estado;
import cr.utn.helpdesk.domain.Impacto;
import cr.utn.helpdesk.domain.Incidencia;
import cr.utn.helpdesk.domain.Prioridad;
import cr.utn.helpdesk.domain.Urgencia;
import cr.utn.helpdesk.service.ConsultaIncidenciasService;
import cr.utn.helpdesk.service.GestorExpedite;
import cr.utn.helpdesk.service.MetricasFlujoService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interfaz de consola de HelpDesk Flow (TEC-03).
 *
 * La consola NO contiene logica de negocio: solo lee entrada, la valida
 * minimamente e invoca a los servicios ya probados. Toda la logica vive en
 * los paquetes domain y service, cubierta por pruebas automatizadas.
 *
 * Mantiene una unica coleccion central de incidencias sobre la que operan
 * todos los servicios, y el GestorExpedite para aplicar la politica EXPEDITE.
 */
public class ConsolaHelpDesk {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", new Locale("es", "CR"));

    private final List<Incidencia> incidencias = new ArrayList<>();
    private final GestorExpedite gestor = new GestorExpedite();
    private final Scanner sc;

    public ConsolaHelpDesk(Scanner sc) {
        this.sc = sc;
    }

    public void iniciar() {
        boolean salir = false;
        System.out.println("=====================================");
        System.out.println("   HelpDesk Flow - UTN ITI-822");
        System.out.println("=====================================");

        while (!salir) {
            mostrarMenu();
            String opcion = leer("Opcion");
            switch (opcion) {
                case "1" -> registrar();
                case "2" -> listarTodas();
                case "3" -> buscarPorId();
                case "4" -> filtrarPorEstado();
                case "5" -> filtrarPorPrioridad();
                case "6" -> mostrarAbiertas();
                case "7" -> mostrarFinalizadas();
                case "8" -> avanzarEstado();
                case "9" -> finalizarIncidencia();
                case "10" -> marcarExpedite();
                case "11" -> mostrarMetricas();
                case "0" -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
            System.out.println();
        }
        System.out.println("Hasta luego.");
    }

    private void mostrarMenu() {
        System.out.println("--- Menu ---");
        System.out.println(" 1. Registrar incidencia");
        System.out.println(" 2. Listar todas");
        System.out.println(" 3. Buscar por identificador");
        System.out.println(" 4. Filtrar por estado");
        System.out.println(" 5. Filtrar por prioridad");
        System.out.println(" 6. Mostrar solo abiertas");
        System.out.println(" 7. Mostrar solo finalizadas");
        System.out.println(" 8. Avanzar estado de una incidencia");
        System.out.println(" 9. Finalizar una incidencia");
        System.out.println("10. Marcar incidencia como EXPEDITE");
        System.out.println("11. Ver metricas");
        System.out.println(" 0. Salir");
    }

    // ---------------- HU-01 + HU-02 ----------------
    private void registrar() {
        try {
            String titulo = leer("Titulo");
            String descripcion = leer("Descripcion (min. 10 caracteres)");
            String categoria = leer("Categoria");
            Impacto impacto = leerImpacto();
            Urgencia urgencia = leerUrgencia();

            Incidencia inc = new Incidencia(titulo, descripcion, categoria, impacto, urgencia);
            incidencias.add(inc);
            gestor.agregar(inc);

            System.out.println("Registrada con id " + inc.getId());
            System.out.println("Prioridad calculada: " + inc.getPrioridad());
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    // ---------------- HU-04 ----------------
    private void listarTodas() {
        imprimirLista(consulta().listarTodas(), "Todas las incidencias");
    }

    private void buscarPorId() {
        String id = leer("Identificador");
        Optional<Incidencia> res = consulta().buscarPorId(id);
        if (res.isPresent()) {
            imprimirDetalle(res.get());
        } else {
            System.out.println("No existe una incidencia con ese identificador.");
        }
    }

    private void filtrarPorEstado() {
        Estado estado = leerEstado();
        imprimirLista(consulta().filtrarPorEstado(estado), "Incidencias en estado " + estado);
    }

    private void filtrarPorPrioridad() {
        Prioridad prioridad = leerPrioridad();
        imprimirLista(consulta().filtrarPorPrioridad(prioridad),
                "Incidencias con prioridad " + prioridad);
    }

    private void mostrarAbiertas() {
        imprimirLista(consulta().soloAbiertas(), "Incidencias abiertas");
    }

    private void mostrarFinalizadas() {
        imprimirLista(consulta().soloFinalizadas(), "Incidencias finalizadas");
    }

    // ---------------- HU-03 ----------------
    private void avanzarEstado() {
        Incidencia inc = seleccionar();
        if (inc == null) {
            return;
        }
        System.out.println("Estado actual: " + inc.getEstado());
        Estado destino = leerEstado();
        try {
            // Se usa el gestor para respetar la politica EXPEDITE; para
            // incidencias estandar equivale a avanzarA directo.
            gestor.avanzar(inc, destino);
            System.out.println("Nuevo estado: " + inc.getEstado());
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Transicion no permitida: " + e.getMessage());
        }
    }

    private void finalizarIncidencia() {
        Incidencia inc = seleccionar();
        if (inc == null) {
            return;
        }
        String solucion = leer("Descripcion de la solucion aplicada");
        try {
            inc.finalizar(solucion);
            System.out.println("Incidencia finalizada el " + inc.getFechaCierre().format(FMT));
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("No se pudo finalizar: " + e.getMessage());
        }
    }

    // ---------------- HU-06 ----------------
    private void marcarExpedite() {
        Incidencia inc = seleccionar();
        if (inc == null) {
            return;
        }
        try {
            inc.marcarComoExpedite();
            System.out.println("Incidencia marcada como EXPEDITE.");
        } catch (IllegalStateException e) {
            System.out.println("No se pudo marcar: " + e.getMessage());
        }
    }

    // ---------------- HU-05 ----------------
    private void mostrarMetricas() {
        MetricasFlujoService m = new MetricasFlujoService(incidencias);
        System.out.println("--- Metricas de flujo ---");
        System.out.println("Total de incidencias: " + m.totalIncidencias());
        System.out.println("Abiertas:             " + m.cantidadAbiertas());
        System.out.println("Finalizadas:          " + m.cantidadFinalizadas());
        System.out.printf ("Lead time promedio:   %.1f minutos%n", m.leadTimePromedioMinutos());

        LocalDateTime desde = LocalDateTime.now().minusDays(30);
        LocalDateTime hasta = LocalDateTime.now();
        System.out.println("Throughput (30 dias): " + m.throughput(desde, hasta));

        System.out.println("Por prioridad:");
        Map<Prioridad, Long> conteo = m.conteoPorPrioridad();
        if (conteo.isEmpty()) {
            System.out.println("  (sin incidencias)");
        } else {
            conteo.forEach((p, c) -> System.out.println("  " + p + ": " + c));
        }
    }

    // ---------------- utilidades ----------------

    private ConsultaIncidenciasService consulta() {
        // Se crea sobre la coleccion central actual cada vez que se consulta.
        return new ConsultaIncidenciasService(incidencias);
    }

    private Incidencia seleccionar() {
        if (incidencias.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
            return null;
        }
        String id = leer("Identificador de la incidencia");
        Optional<Incidencia> res = consulta().buscarPorId(id);
        if (res.isEmpty()) {
            System.out.println("No existe una incidencia con ese identificador.");
            return null;
        }
        return res.get();
    }

    private void imprimirLista(List<Incidencia> lista, String titulo) {
        System.out.println("--- " + titulo + " (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("  (ninguna)");
            return;
        }
        for (Incidencia i : lista) {
            String exp = i.getClaseServicio() == ClaseServicio.EXPEDITE ? " [EXPEDITE]" : "";
            System.out.printf("  %s | %s | %s | %s%s%n",
                    i.getId().substring(0, 8), i.getTitulo(), i.getEstado(), i.getPrioridad(), exp);
        }
    }

    private void imprimirDetalle(Incidencia i) {
        System.out.println("--- Detalle ---");
        System.out.println("Id:          " + i.getId());
        System.out.println("Titulo:      " + i.getTitulo());
        System.out.println("Descripcion: " + i.getDescripcion());
        System.out.println("Categoria:   " + i.getCategoria());
        System.out.println("Impacto:     " + i.getImpacto());
        System.out.println("Urgencia:    " + i.getUrgencia());
        System.out.println("Prioridad:   " + i.getPrioridad());
        System.out.println("Estado:      " + i.getEstado());
        System.out.println("Clase:       " + i.getClaseServicio());
        System.out.println("Creada:      " + i.getFechaCreacion().format(FMT));
        if (i.getFechaCierre() != null) {
            System.out.println("Cerrada:     " + i.getFechaCierre().format(FMT));
            System.out.println("Solucion:    " + i.getDescripcionSolucion());
        }
    }

    private String leer(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return sc.nextLine().trim();
    }

    private Impacto leerImpacto() {
        while (true) {
            String v = leer("Impacto (BAJO/MEDIO/ALTO)").toUpperCase();
            try {
                return Impacto.valueOf(v);
            } catch (IllegalArgumentException e) {
                System.out.println("Valor no valido.");
            }
        }
    }

    private Urgencia leerUrgencia() {
        while (true) {
            String v = leer("Urgencia (BAJA/MEDIA/ALTA)").toUpperCase();
            try {
                return Urgencia.valueOf(v);
            } catch (IllegalArgumentException e) {
                System.out.println("Valor no valido.");
            }
        }
    }

    private Estado leerEstado() {
        while (true) {
            String v = leer("Estado (REGISTRADA/LISTA/EN_DESARROLLO/EN_VALIDACION/FINALIZADA)").toUpperCase();
            try {
                return Estado.valueOf(v);
            } catch (IllegalArgumentException e) {
                System.out.println("Valor no valido.");
            }
        }
    }

    private Prioridad leerPrioridad() {
        while (true) {
            String v = leer("Prioridad (NORMAL/ALTA/CRITICA)").toUpperCase();
            try {
                return Prioridad.valueOf(v);
            } catch (IllegalArgumentException e) {
                System.out.println("Valor no valido.");
            }
        }
    }
}
