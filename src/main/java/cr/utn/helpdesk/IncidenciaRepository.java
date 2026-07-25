package cr.utn.helpdesk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class IncidenciaRepository {


    private final List<Incidencia> incidencias;


    public IncidenciaRepository() {
        this.incidencias = new ArrayList<>();
    }


    public void guardar(Incidencia incidencia) {
        incidencias.add(incidencia);
    }


    public List<Incidencia> buscarTodas() {
        return incidencias;
    }


    public Optional<Incidencia> buscarPorId(Long id) {

        return incidencias.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

    }
}