package ga.rafaela.doo_vivero_gr_parte_3.repository;

import ga.rafaela.doo_vivero_gr_parte_3.dao.PersistenciaExcepcion;
import ga.rafaela.doo_vivero_gr_parte_3.model.Planta;

import java.util.List;

public interface Repository<T extends Planta> {

    void cargar() throws PersistenciaExcepcion;

    void agregar(T nuevoElemento) throws PersistenciaExcepcion;

    void eliminar(T elementoAEliminar) throws PersistenciaExcepcion;

    List<T> listar();

}
