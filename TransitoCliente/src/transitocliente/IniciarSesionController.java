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
import javafx.scene.control.TextField;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import transitocliente.entities.Respuesta;

/**
 * FXML Controller class
 *
 * @author ponch
 */
public class IniciarSesionController extends ControladorPadre implements Initializable {

    @FXML
    private Button btn_iniciar;
    @FXML
    private Label lbl_error;
    @FXML
    private PasswordField txt_contrasena;
    @FXML
    private TextField txt_usuario;
    UsuarioClienteEscritorio usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lbl_error.setVisible(false);

    }

    @FXML
    private void iniciar(ActionEvent event) {
        Respuesta r = validarCampos();
        if (r.isError()) {
            lbl_error.setVisible(true);
            lbl_error.setText(r.getMensaje());
        } else {
            lbl_error.setVisible(true);
            lbl_error.setText(r.getMensaje());
            this.recuperarUsuario();
            this.abrirMenuPrincipal(usuario);
            ((Node) btn_iniciar).getScene().getWindow().hide();
        }
    }

    public void recuperarUsuario() {
        try {
            ClienteServicios cliente = new ClienteServicios();
            usuario = cliente.recuperarUsuario(txt_usuario.getText().trim());
        } catch (TTransportException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        } catch (TException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        }
    }

    public Respuesta validarCampos() {
        Respuesta r = new Respuesta();
        if (txt_usuario.getText().trim().isEmpty() || txt_contrasena.getText().isEmpty()) {
            r.setMensaje("Favor de llenar los campos");
            r.setError(true);
            r.setErrorcode(1);
            return r;
        }
        try {
            ClienteServicios cliente = new ClienteServicios();
            if (!cliente.validarCredenciales(txt_usuario.getText().trim(), this.encriptar(txt_contrasena.getText().trim()))) {
                r.setMensaje("Credenciales invalidas");
                r.setError(true);
                r.setErrorcode(2);
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
