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

/**
 * FXML Controller class
 *
 * @author ponch
 */
public class MenuPrincipalController extends ControladorPadre implements Initializable {

    @FXML
    private Button btn_hacerDictamen;
    @FXML
    private Button btn_registrarUsuario;
    @FXML
    private Button btn_cerrar;
    private UsuarioClienteEscritorio usuario;
    @FXML
    private Button btn_cambiar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void hacerDictamen(ActionEvent event) {
        this.abrirVerIncidentes(usuario);
        ((Node) btn_hacerDictamen).getScene().getWindow().hide();
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        this.abrirRegistrarUsuario(usuario);
        ((Node) btn_hacerDictamen).getScene().getWindow().hide();

    }

    @FXML
    private void cerrar(ActionEvent event) {
        this.abrirIniciarSesion();
        ((Node) btn_cerrar).getScene().getWindow().hide();
    }

    void recibirParametros(UsuarioClienteEscritorio usuario) {
        this.usuario = usuario;
        if (usuario.getCargo().equals("Perito")) {
            btn_registrarUsuario.setVisible(false);
        } else {
            btn_hacerDictamen.setVisible(false);
        }
    }

    @FXML
    private void cambiarContrasena(ActionEvent event) {
        this.abrirActualizarContrasena(usuario);
        ((Node) btn_cambiar).getScene().getWindow().hide();
    }

}
