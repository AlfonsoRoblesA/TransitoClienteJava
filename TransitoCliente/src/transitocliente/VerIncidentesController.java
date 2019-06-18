/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.Incidente;
import Thrift.UsuarioClienteEscritorio;
import transitocliente.entities.adapter.IncidenteAdapter;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author Foncho
 */
public class VerIncidentesController extends ControladorPadre implements Initializable {
    
    @FXML
    private Button btn_regresar;
    @FXML
    private Label lbl_error;
    @FXML
    private Button btn_consultar;
    @FXML
    private TableView<IncidenteAdapter> tbl_tabla;
    @FXML
    private TableColumn<IncidenteAdapter, Integer> clm_id;
    @FXML
    private TableColumn<IncidenteAdapter, Date> clm_fecha;
    @FXML
    private TableColumn<IncidenteAdapter, String> clm_estado;
    @FXML
    private TableColumn<IncidenteAdapter, String> clm_ciudad;
    ObservableList<IncidenteAdapter> incidentes;
    private int posicionEnTabla = -1;
    private Incidente incidenteSeleccionado;
    List<Incidente> incidente = null;
    private UsuarioClienteEscritorio usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.iniciarTabla();
        lbl_error.setVisible(false);
        final ObservableList<IncidenteAdapter> incidenteAdapterSel = tbl_tabla.getSelectionModel().getSelectedItems();
        incidenteAdapterSel.addListener(selectorTablaIncidentes);
    }
    
    @FXML
    private void regresar(ActionEvent event) {
        this.abrirMenuPrincipal(usuario);
        ((Node) btn_regresar).getScene().getWindow().hide();
    }
    
    @FXML
    private void consultar(ActionEvent event) {
        if (posicionEnTabla != -1) {
            this.abrirVerDetalleIncidente(usuario, incidenteSeleccionado);
                ((Node) btn_consultar).getScene().getWindow().hide();
        } else {
            lbl_error.setText("Seleccione un incidente");
            lbl_error.setVisible(true);
        }
    }
    
    private void iniciarTabla() {
        incidentes = FXCollections.observableArrayList();
        clm_id.setCellValueFactory(new PropertyValueFactory<>("idIncidente"));
        clm_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        clm_ciudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        clm_estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        try {
            ClienteServicios cliente = new ClienteServicios();
            incidente = cliente.recuperarIncidentes();
        } catch (TTransportException ex) {
            lbl_error.setText("Error al conectar al servidor");
            lbl_error.setVisible(true);
        } catch (TException ex) {
            lbl_error.setText("Error al conectar al servidor");
            lbl_error.setVisible(true);
        }
        
        for (int i = 0; i < incidente.size(); i++) {
            IncidenteAdapter adapter = new IncidenteAdapter();
            adapter.idIncidente.set(incidente.get(i).getIdIncidente());
            adapter.fecha.set(this.ParseFecha(incidente.get(i).getFecha()));
            if (incidente.get(i).getEstado() == 0) {
                adapter.estado.set("Pendiente");
            } else {
                adapter.estado.set("Dictaminado");
            }
            adapter.ciudad.set(incidente.get(i).getCiudad());
            incidentes.add(adapter);
        }
        tbl_tabla.setItems(incidentes);
    }
    
    private final ListChangeListener<IncidenteAdapter> selectorTablaIncidentes
            = (ListChangeListener.Change<? extends IncidenteAdapter> c) -> {
                crearIncidenteSeleccionado();
            };
    
    public IncidenteAdapter getIncidenteAdapterSeleccionado() {
        if (tbl_tabla != null) {
            List<IncidenteAdapter> l = tbl_tabla.getSelectionModel().getSelectedItems();
            if (l.size() == 1) {
                final IncidenteAdapter seleccionado = l.get(0);
                return seleccionado;
            }
        }
        return null;
    }
    
    public void crearIncidenteSeleccionado() {
        IncidenteAdapter adaptadorSeleccionado = getIncidenteAdapterSeleccionado();
        posicionEnTabla = incidentes.indexOf(adaptadorSeleccionado);
        if (adaptadorSeleccionado != null) {
            incidenteSeleccionado = incidente.get(posicionEnTabla);
            System.out.println(incidenteSeleccionado.getIdIncidente());
        }
    }
    
    public Date ParseFecha(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = formatter.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(VerIncidentesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
    void setParametros(UsuarioClienteEscritorio usuario) {
        this.usuario = usuario;
    }
    
}
