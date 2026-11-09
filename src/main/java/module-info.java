module ga.rafaela.doo_vivero_gr_parte_3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires com.google.gson;
    requires java.desktop;

    opens ga.rafaela.doo_vivero_gr_parte_3 to javafx.fxml;
    opens ga.rafaela.doo_vivero_gr_parte_3.controller to javafx.fxml;


    opens ga.rafaela.doo_vivero_gr_parte_3.dao to com.google.gson;
    opens ga.rafaela.doo_vivero_gr_parte_3.model to com.google.gson;


    exports ga.rafaela.doo_vivero_gr_parte_3;
    exports ga.rafaela.doo_vivero_gr_parte_3.controller;
}