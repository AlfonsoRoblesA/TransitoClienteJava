/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.UsuarioClienteEscritorio;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import transitocliente.entities.Respuesta;

/**
 * FXML Controller class
 *
 * @author ponch
 */
public class RegistrarUsuarioController extends ControladorPadre implements Initializable {

    @FXML
    private Button btn_registrar;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_usuario;
    @FXML
    private PasswordField txt_contrasena;
    @FXML
    private PasswordField txt_confirmarC;
    @FXML
    private ComboBox<String> cb_cargo;
    @FXML
    private Button btn_cancelar;
    private UsuarioClienteEscritorio usuario;
    private UsuarioClienteEscritorio usu2 = null;
    @FXML
    private Label lbl_error;
    ObservableList<String> clasif = FXCollections.observableArrayList(
            "Perito",
            "Administrador");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbl_error.setVisible(false);
        cb_cargo.setItems(clasif);
    }

    @FXML
    private void registrar(ActionEvent event) {
        Respuesta r = validarDatos();
        if (r.isError()) {
            lbl_error.setText(r.getMensaje());
            lbl_error.setVisible(true);
        } else {
            usu2 = new UsuarioClienteEscritorio();
            usu2.setCargo(cb_cargo.getValue());
            usu2.setContrasena(this.encriptar(txt_contrasena.getText()));
            usu2.setNombre(txt_nombre.getText().trim());
            usu2.setUsuario(txt_usuario.getText().trim());
            try {
                ClienteServicios cliente = new ClienteServicios();
                cliente.registrarUsuario(usu2);
            } catch (TTransportException ex) {
                lbl_error.setText("Error al conectar con el servidor");
                lbl_error.setVisible(true);
            } catch (TException ex) {
                lbl_error.setText("Error al conectar con el servidor");
                lbl_error.setVisible(true);
            }
            this.abrirMenuPrincipal(usuario);
            ((Node) btn_registrar).getScene().getWindow().hide();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        this.abrirMenuPrincipal(usuario);
        ((Node) btn_cancelar).getScene().getWindow().hide();
    }

    public Respuesta validarDatos() {
        Respuesta r = new Respuesta();
        if (txt_nombre.getText().isEmpty()
                || txt_usuario.getText().isEmpty()
                || txt_contrasena.getText().isEmpty()
                || txt_confirmarC.getText().isEmpty()
                || cb_cargo.getSelectionModel().isEmpty()) {
            r.setMensaje("Hay campos vacios");
            r.setError(true);
            r.setErrorcode(1);
            return r;
        }
        if (txt_contrasena.getText().length() < 6) {
            r.setMensaje("La contraseña debe tener al menos 6 caracteres");
            r.setError(true);
            r.setErrorcode(1);
            return r;
        }
        if (!txt_contrasena.getText().equals(txt_confirmarC.getText())) {
            r.setMensaje("Contraseñas diferentes");
            r.setErrorcode(2);
            r.setError(true);
            return r;
        }
        try {
            ClienteServicios cliente = new ClienteServicios();
            if (!cliente.validarUsuario(txt_usuario.getText().trim())) {
                r.setMensaje("Usuario ya utilizado");
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
        r.setMensaje("Exitoso");
        r.setError(false);
        r.setErrorcode(0);
        return r;
    }

    public void recibirParametros(UsuarioClienteEscritorio usuario) {
        this.usuario = usuario;
    }

}
