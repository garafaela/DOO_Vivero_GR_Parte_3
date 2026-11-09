package ga.rafaela.doo_vivero_gr_parte_3.repository;

import ga.rafaela.doo_vivero_gr_parte_3.dao.PersistenciaExcepcion;
import ga.rafaela.doo_vivero_gr_parte_3.dao.PlantaDAO;
import ga.rafaela.doo_vivero_gr_parte_3.model.Planta;
import ga.rafaela.doo_vivero_gr_parte_3.model.PlantaExterior;
import ga.rafaela.doo_vivero_gr_parte_3.model.PlantaInterior;

import java.util.ArrayList;
import java.util.List;

public class PlantaRepository implements Repository<Planta> {

    private final List<Planta> todasPlantas = new ArrayList<>();
    private final PlantaDAO<PlantaInterior> daoPlantasInteriores;
    private final PlantaDAO<PlantaExterior> daoPlantasExteriores;

    public PlantaRepository(PlantaDAO<PlantaInterior> daoPlantasInteriores, PlantaDAO<PlantaExterior> daoPlantasExteriores){
        this.daoPlantasInteriores = daoPlantasInteriores;
        this.daoPlantasExteriores = daoPlantasExteriores;
    }


    @Override
    public void cargar() throws PersistenciaExcepcion{
        todasPlantas.clear();
        todasPlantas.addAll(daoPlantasInteriores.cargarDAO()); //esto carga las plantas interiores del dao y retorna una lista, que luego se agrega a la lista local
        todasPlantas.addAll(daoPlantasExteriores.cargarDAO());
    }

    @Override
    public void agregar(Planta nuevaPlanta) throws PersistenciaExcepcion{
        todasPlantas.add(nuevaPlanta);
        guardarJSON();
    }

    @Override
    public void eliminar(Planta plantaAEliminar) throws PersistenciaExcepcion{
        todasPlantas.remove(plantaAEliminar);
        guardarJSON();
    }

    @Override
    public List<Planta> listar(){
        return new ArrayList<>(todasPlantas);
    }

    public void guardarJSON() throws PersistenciaExcepcion{
        List<PlantaInterior> interiores = new ArrayList<>();
        List<PlantaExterior> exteriores = new ArrayList<>();

        for (Planta p : todasPlantas){
            if (p instanceof PlantaInterior){
                interiores.add((PlantaInterior) p);

            } else if (p instanceof PlantaExterior){
                exteriores.add((PlantaExterior) p);
            }
        }

        daoPlantasInteriores.guardarDAO(interiores);
        daoPlantasExteriores.guardarDAO(exteriores);
    }


}
