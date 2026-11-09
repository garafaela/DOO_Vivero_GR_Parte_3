package ga.rafaela.doo_vivero_gr_parte_3.model;

public interface Despachable {

    /**
     * Indica si la planta está lista para ser despachada.
     *
     * @return true si está lista para despacho
     */
    boolean estaListaParaDespacho();

    /**
     * Registra que la planta está lista para despacho.
     */
    void marcarListaParaDespacho();
}