package ga.rafaela.doo_vivero_gr_parte_3.controller;

import ga.rafaela.doo_vivero_gr_parte_3.dao.GsonPlantaDAO;
import ga.rafaela.doo_vivero_gr_parte_3.dao.PersistenciaExcepcion;
import ga.rafaela.doo_vivero_gr_parte_3.model.Planta;
import ga.rafaela.doo_vivero_gr_parte_3.model.PlantaExterior;
import ga.rafaela.doo_vivero_gr_parte_3.model.PlantaInterior;
import ga.rafaela.doo_vivero_gr_parte_3.repository.PlantaRepository;
import ga.rafaela.doo_vivero_gr_parte_3.repository.Repository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Path;

public class MainController {

    @FXML private TableView<Planta> tablaPlantas;
    @FXML private TableColumn<Planta, String> colTipo;
    @FXML private TableColumn<Planta, String> colNombre;
    @FXML private TableColumn<Planta, Integer> colAltura;
    @FXML private TableColumn<Planta, Integer> colCantidad;
    @FXML private TableColumn<Planta, String> colPrecio;

    @FXML private ComboBox<String> cmbTipoPlanta;
    @FXML private TextField txtNombre;
    @FXML private TextField txtAltura;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtVolumenMaceta;
    @FXML private TextField txtEspecie;
    @FXML private CheckBox chbResisteHeladas;
    @FXML private CheckBox chbListaParaDespacho;
    @FXML private Button btnRegistrar;

    @FXML private Label lblMensaje;
    @FXML private Button btnEliminar;

    private GsonPlantaDAO<PlantaInterior> interioresDAO = new GsonPlantaDAO<>(Path.of("interiores.json"),PlantaInterior.class);
    private GsonPlantaDAO<PlantaExterior> exterioresDAO = new GsonPlantaDAO<>(Path.of("exteriores.json"),PlantaExterior.class);
    private Repository<Planta> repoPlantas = new PlantaRepository(interioresDAO,exterioresDAO);
    private final ObservableList<Planta> todasPlantas = FXCollections.observableArrayList();



    public void iniciarTabla(){
        tablaPlantas.setItems(todasPlantas);
        colTipo.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getTipo()));

        colNombre.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getNombre()));
        colAltura.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(celda.getValue().getAlturaCm()));
        colCantidad.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(celda.getValue().getCantidad()));
        colPrecio.setCellValueFactory(celda -> new ReadOnlyStringWrapper(Planta.pesos(celda.getValue().calcularPrecio())));

        cmbTipoPlanta.getItems().setAll("Interior","Exterior");
    }

    public void actualizarObservableList(){
        todasPlantas.setAll(repoPlantas.listar());
    }

    public void limpiarReiniciarCampos(){
        cmbTipoPlanta.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtNombre.setDisable(true);
        txtAltura.clear();
        txtAltura.setDisable(true);
        txtCantidad.clear();
        txtCantidad.setDisable(true);
        txtVolumenMaceta.clear();
        txtVolumenMaceta.setDisable(true);
        txtEspecie.clear();
        txtEspecie.setDisable(true);
        chbResisteHeladas.setSelected(false);
        chbResisteHeladas.setDisable(true);
        chbListaParaDespacho.setSelected(false);
        chbListaParaDespacho.setDisable(true);
        btnRegistrar.setDisable(true);
    }



    @FXML void initialize(){
        iniciarTabla();

        try {
            repoPlantas.cargar(); //cargo info del .json al repo local
            actualizarObservableList(); //actualizo lista observable con lo que hay en el repo (repo.listar(), devuelve lista)

        }catch (PersistenciaExcepcion ignored){
        }
    }

    @FXML public void onEliminar(){
        Planta seleccion = tablaPlantas.getSelectionModel().getSelectedItem();

        if(seleccion == null){
            lblMensaje.setText("Debe seleccionar una planta para eliminar.");
            return;
        }

        try{
            repoPlantas.eliminar(seleccion);
            actualizarObservableList();
            lblMensaje.setText("¡Planta eliminada exitosamente!");

        }catch (PersistenciaExcepcion e){
            lblMensaje.setText("No se pudo eliminar la planta.");
        }  }

    @FXML public void onRegistrar(){
        String tipo = cmbTipoPlanta.getValue();
        Planta newPlanta = null;

        try{
            if (tipo.equals("Interior")) {

                if (txtNombre.getText().isEmpty() || txtAltura.getText().isEmpty() || txtCantidad.getText().isEmpty() || txtVolumenMaceta.getText().isEmpty()) {
                    lblMensaje.setText("Debe rellenar todos los campos.");
                    return;
                }

                newPlanta = new PlantaInterior(txtNombre.getText(), Integer.parseInt(txtAltura.getText()), Integer.parseInt(txtCantidad.getText()), Double.parseDouble(txtVolumenMaceta.getText()));


            } else if (tipo.equals("Exterior")) {
                if (txtNombre.getText().isEmpty() || txtAltura.getText().isEmpty() || txtCantidad.getText().isEmpty() || txtEspecie.getText().isEmpty()) {
                    lblMensaje.setText("Debe rellenar todos los campos.");
                    return;
                }

                newPlanta = new PlantaExterior(txtNombre.getText(), Integer.parseInt(txtAltura.getText()), Integer.parseInt(txtCantidad.getText()), txtEspecie.getText(), chbResisteHeladas.isSelected());
            }

            if (newPlanta != null) {
                repoPlantas.agregar(newPlanta);
                actualizarObservableList();
                limpiarReiniciarCampos();
                lblMensaje.setText("¡Planta agregada exitosamente!");
            }
        }catch (NumberFormatException e){
            lblMensaje.setText("Error en formato de números: \nDebe ingresar números positivos.\nLos decimales se ingresan con punto (ej. '2.5')");
        }catch (IllegalArgumentException e){
            lblMensaje.setText(e.getMessage());
        }catch (PersistenciaExcepcion e){
            lblMensaje.setText("No se pudo registrar la planta.");
        }

    }

    @FXML public void onTipoPlanta(){
        String tipo = cmbTipoPlanta.getValue();

        if(tipo == null){
            btnRegistrar.setDisable(true);
            return;

        } else if (tipo.equals("Interior")) {
            txtNombre.setDisable(false);
            txtAltura.setDisable(false);
            txtCantidad.setDisable(false);
            txtVolumenMaceta.setDisable(false);
            txtEspecie.setDisable(true);
            chbResisteHeladas.setDisable(true);
            chbListaParaDespacho.setDisable(true);
            btnRegistrar.setDisable(false);

        } else if (tipo.equals("Exterior")) {
            txtNombre.setDisable(false);
            txtAltura.setDisable(false);
            txtCantidad.setDisable(false);
            txtVolumenMaceta.setDisable(true);
            txtEspecie.setDisable(false);
            chbResisteHeladas.setDisable(false);
            chbListaParaDespacho.setDisable(false);
            btnRegistrar.setDisable(false);
        }
    }
}
