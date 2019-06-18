/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.UsuarioClienteEscritorio;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.apache.thrift.TException;
import transitocliente.entities.Respuesta;

/**
 * FXML Controller class
 *
 * @author ponch
 */
public class ActualizarContrasenaController extends ControladorPadre implements Initializable {

    @FXML
    private PasswordField txt_contrasena1;
    @FXML
    private PasswordField txt_contrasena2;
    @FXML
    private Label lbl_error;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_actualizar;
    private UsuarioClienteEscritorio usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbl_error.setVisible(false);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        this.abrirMenuPrincipal(usuario);
        ((Node) btn_cancelar).getScene().getWindow().hide();
    }

    @FXML
    private void actualizar(ActionEvent event) {
        Respuesta r = validarDatos();
        if (r.isError()) {
            lbl_error.setText(r.getMensaje());
            lbl_error.setVisible(true);
        } else {
            try {
                ClienteServicios cliente = new ClienteServicios();
                cliente.actualizarContrasena(usuario.getIdUsuario(), this.encriptar(txt_contrasena1.getText()));
            } catch (TException ex) {
                lbl_error.setText("Se ha perdido la conexion");
                lbl_error.setVisible(true);
            }
            lbl_error.setText("Existoso");
            lbl_error.setVisible(true);
            this.abrirMenuPrincipal(usuario);
            ((Node) btn_actualizar).getScene().getWindow().hide();
        }
    }

    public Respuesta validarDatos() {
        Respuesta r = new Respuesta();
        if (txt_contrasena1.getText().isEmpty()
                || txt_contrasena2.getText().isEmpty()) {
            r.setMensaje("Complete los campos vacios");
            r.setError(true);
            r.setErrorcode(1);
            return r;
        }
        if (txt_contrasena1.getText().length()<6) {
            r.setMensaje("La contraseña debe tener al menos 6 caracteres");
            r.setError(true);
            r.setErrorcode(1);
            return r;
        }
        if (!txt_contrasena1.getText().equals(txt_contrasena2.getText())) {
            r.setMensaje("Contraseñas diferentes");
            r.setErrorcode(2);
            r.setError(true);
            return r;
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
