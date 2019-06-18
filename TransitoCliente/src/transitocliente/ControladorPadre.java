/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.Incidente;
import Thrift.UsuarioClienteEscritorio;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ponch
 */
public class ControladorPadre {

    public void abrirMenuPrincipal(UsuarioClienteEscritorio usuario) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MenuPrincipal.fxml"));
            Parent responder = loader.load();
            MenuPrincipalController cam = loader.getController();
            cam.recibirParametros(usuario);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Menú principal");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirActualizarContrasena(UsuarioClienteEscritorio usuario) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ActualizarContrasena.fxml"));
            Parent responder = loader.load();
            ActualizarContrasenaController cam = loader.getController();
            cam.recibirParametros(usuario);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Actualizar contraseña");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirHacerDictamen(UsuarioClienteEscritorio usuario, Incidente incidente) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("HacerDictamen.fxml"));
            Parent responder = loader.load();
            HacerDictamenController cam = loader.getController();
            cam.setParametros(usuario, incidente);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Hacer dictamen");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void abrirIniciarSesion() {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("IniciarSesion.fxml"));
            Parent responder = loader.load();
            IniciarSesionController cam = loader.getController();
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Iniciar sesión");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirRegistrarUsuario(UsuarioClienteEscritorio usuario) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("RegistrarUsuario.fxml"));
            Parent responder = loader.load();
            RegistrarUsuarioController cam = loader.getController();
            cam.recibirParametros(usuario);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Registrar usuario");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirVerDetalleIncidente(UsuarioClienteEscritorio usuario, Incidente incidente) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VerDetalleIncidente.fxml"));
            Parent responder = loader.load();
            VerDetalleIncidenteController cam = loader.getController();
            cam.recibirParametros(incidente, usuario);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Detalle de incidente");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirVerIncidentes(UsuarioClienteEscritorio usuario) {
        try {
            Locale.setDefault(new Locale("es"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("VerIncidentes.fxml"));
            Parent responder = loader.load();
            VerIncidentesController cam = loader.getController();
            cam.setParametros(usuario);
            Scene scene = new Scene(responder);
            Stage stage = new Stage();
            stage.setTitle("Incidentes");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ControladorPadre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String encriptar(String texto) {

        String secretKey = "qualityinfosolutions"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

}
