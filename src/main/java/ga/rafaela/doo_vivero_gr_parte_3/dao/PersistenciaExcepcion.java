package ga.rafaela.doo_vivero_gr_parte_3.dao;

public class PersistenciaExcepcion extends Exception{

    public PersistenciaExcepcion(String mensaje, Throwable causa){
        super(mensaje, causa);
    }

    public PersistenciaExcepcion(String mensaje){
        super(mensaje);
    }


}
