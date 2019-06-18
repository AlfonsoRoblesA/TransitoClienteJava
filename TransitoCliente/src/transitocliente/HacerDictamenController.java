/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.Dictamen;
import Thrift.Incidente;
import Thrift.UsuarioClienteEscritorio;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import transitocliente.entities.Respuesta;

/**
 * FXML Controller class
 *
 * @author Foncho
 */
public class HacerDictamenController extends ControladorPadre implements Initializable {
    
    @FXML
    private TextArea txt_descripcion;
    @FXML
    private TextField txt_folio;
    @FXML
    private Label lbl_error;
    @FXML
    private Button btn_dictaminar;
    @FXML
    private Button btn_cancelar;
    private UsuarioClienteEscritorio usuario;
    private Incidente incidente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbl_error.setVisible(false);
    }
    
    @FXML
    private void clickDictaminar(ActionEvent event) {
        Respuesta r = validarDatos();
        if (r.isError()) {
            lbl_error.setText(r.getMensaje());
            lbl_error.setVisible(true);
        } else {
            Alert cancela = new Alert(Alert.AlertType.CONFIRMATION);
            cancela.setTitle("Confirmar");
            cancela.setHeaderText(null);
            cancela.initStyle(StageStyle.UTILITY);
            cancela.setContentText("Una vez realizado el dictamen no se puede modificar, ¿Desea continuar con esta accion?");
            Optional<ButtonType> result = cancela.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    Dictamen dic = new Dictamen();
                    dic.setDescripcion(txt_descripcion.getText().trim());
                    dic.setFolio(txt_folio.getText().trim());
                    dic.setIdIncidente(incidente.getIdIncidente());
                    dic.setIdUsuario(usuario.getIdUsuario());
                    ClienteServicios cliente = new ClienteServicios();
                    cliente.dictaminarIncidente(dic);
                } catch (TTransportException ex) {
                    lbl_error.setText("Se ha perdido la conexion con el servidor");
                    lbl_error.setVisible(true);
                } catch (TException ex) {
                    lbl_error.setText("Se ha perdido la conexion con el servidor");
                    lbl_error.setVisible(true);
                }
            }
            this.abrirMenuPrincipal(usuario);
            ((Node) btn_dictaminar).getScene().getWindow().hide();
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event
    ) {
        Alert cancela = new Alert(Alert.AlertType.CONFIRMATION);
        cancela.setTitle("Cancelar proceso");
        cancela.setHeaderText(null);
        cancela.initStyle(StageStyle.UTILITY);
        cancela.setContentText("Ha cancelado, ¿Desea continuar con esta accion?");
        Optional<ButtonType> result = cancela.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.abrirVerDetalleIncidente(usuario, incidente);
            ((Node) btn_cancelar).getScene().getWindow().hide();
        }
        
    }
    
    public void setParametros(UsuarioClienteEscritorio usuario, Incidente incidente) {
        this.usuario = usuario;
        this.incidente = incidente;
    }
    
    public Respuesta validarDatos() {
        Respuesta r = new Respuesta();
        if (txt_folio.getText().trim().isEmpty()) {
            r.setError(true);
            r.setMensaje("Escribe un folio");
            r.setErrorcode(1);
            return r;
        }
        if (txt_descripcion.getText().trim().isEmpty()) {
            r.setError(true);
            r.setMensaje("Escribe una descripcion");
            r.setErrorcode(2);
            return r;
        }
        try {
            ClienteServicios cliente = new ClienteServicios();
            if (!cliente.validarFolio(txt_folio.getText())) {
                r.setMensaje("Folio ya utilizado");
                r.setError(true);
                r.setErrorcode(3);
                return r;
            }
        } catch (TTransportException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        } catch (TException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        }
        r.setError(false);
        r.setMensaje("Exitoso");
        r.setErrorcode(0);
        return r;
    }
    
}
