package cr.utn.helpdesk;

import java.util.List;
import java.util.Optional;


public class ConsultaIncidenciasService {


    private final IncidenciaRepository repository;


    public ConsultaIncidenciasService(
            IncidenciaRepository repository
    ) {
        this.repository = repository;
    }



    public List<Incidencia> mostrarTodas(){

        return repository.buscarTodas();

    }



    public Optional<Incidencia> buscarPorId(Long id){

        return repository.buscarPorId(id);

    }



    public List<Incidencia> filtrarPorEstado(
            Estado estado
    ){

        return repository.buscarTodas()
                .stream()
                .filter(i -> i.getEstado() == estado)
                .toList();

    }



    public List<Incidencia> filtrarPorPrioridad(
            Prioridad prioridad
    ){

        return repository.buscarTodas()
                .stream()
                .filter(i -> i.getPrioridad() == prioridad)
                .toList();

    }



    public List<Incidencia> abiertas(){

        return filtrarPorEstado(
                Estado.ABIERTA
        );

    }



    public List<Incidencia> finalizadas(){

        return filtrarPorEstado(
                Estado.FINALIZADA
        );

    }
}