package ga.rafaela.doo_vivero_gr_parte_3.dao;

import ga.rafaela.doo_vivero_gr_parte_3.model.Planta;

import java.util.List;

public interface PlantaDAO<T extends Planta> {

    List<T> cargarDAO() throws PersistenciaExcepcion;

    void guardarDAO(List<T> todo) throws PersistenciaExcepcion;

}
