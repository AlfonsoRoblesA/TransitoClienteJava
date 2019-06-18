/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.Foto;
import Thrift.Incidente;
import Thrift.Reporte;
import Thrift.UsuarioClienteEscritorio;
import Thrift.Vehiculo;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.javafx.MapView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import javafx.scene.image.ImageView;
import javax.xml.bind.DatatypeConverter;

/**
 * FXML Controller class
 *
 * @author Foncho
 */
public class VerDetalleIncidenteController extends ControladorPadre implements Initializable {

    @FXML
    private Button btn_regresaar;
    @FXML
    private Label lbl_estado;
    @FXML
    private Label lbl_error;
    @FXML
    private Button btn_dictaminar;
    @FXML
    private ListView<ImageView> lst_imagenes;
    @FXML
    private Label lbl_marca1;
    @FXML
    private Label lbl_modelo1;
    @FXML
    private Label lbl_anio1;
    @FXML
    private Label lbl_color1;
    @FXML
    private Label lbl_aseguradora1;
    @FXML
    private Label lbl_poliza1;
    @FXML
    private Label lbl_placas1;
    @FXML
    private Label lbl_marca2;
    @FXML
    private Label lbl_modelo2;
    @FXML
    private Label lbl_anio2;
    @FXML
    private Label lbl_color2;
    @FXML
    private Label lbl_aseguradora2;
    @FXML
    private Label lbl_poliza2;
    @FXML
    private Label lbl_placas2;
    @FXML
    private Label lbl_titulo;
    @FXML
    private BorderPane pane_map;
    private Incidente incidente;
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
    private void clickRegresar(ActionEvent event) {
        this.abrirVerIncidentes(usuario);
        ((Node) btn_regresaar).getScene().getWindow().hide();
    }

    @FXML
    private void clickDictaminar(ActionEvent event) {
        this.abrirHacerDictamen(usuario, incidente);
        ((Node) btn_dictaminar).getScene().getWindow().hide();
    }

    private void iniciarMapa(Reporte r) {
        MapViewOptions options = new MapViewOptions();
        options.importPlaces();
        options.setApiKey("AQUI VA LA CLAVE DE GOOGLE MAPS");
        final MapView mapView = new MapView(options);

        mapView.setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                if (status == MapStatus.MAP_STATUS_OK) {
                    final Map map = mapView.getMap();
                    map.setZoom(5.0);
                    GeocoderRequest request = new GeocoderRequest();
                    LatLng ll = new LatLng(r.getLatitud(), r.getLongitud());
                    request.setLocation(ll);

                    mapView.getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
                        @Override
                        public void onComplete(GeocoderResult[] result, GeocoderStatus status) {
                            if (status == GeocoderStatus.OK) {
                                map.setCenter(result[0].getGeometry().getLocation());
                                Marker marker = new Marker(map);
                                marker.setPosition(result[0].getGeometry().getLocation());

                                final InfoWindow window = new InfoWindow(map);
                                window.setContent("Ubicacion");
                                window.open(map, marker);
                            }
                        }
                    });
                }
            }
        });
        pane_map.setCenter(mapView);
    }

    public void iniciarPantalla() {
        List<Reporte> reportes = null;
        try {
            ClienteServicios cliente = new ClienteServicios();
            reportes = cliente.recuperarReportes(incidente.getIdIncidente());
        } catch (TTransportException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        } catch (TException ex) {
            lbl_error.setText("Error al conectar con el servidor");
            lbl_error.setVisible(true);
        }
        for (int i = 0; i < reportes.size(); i++) {
            if (i == 0) {
                MapView.InitJavaFX();
                iniciarMapa(reportes.get(i));
                lbl_marca1.setText(reportes.get(i).getMarca());
                lbl_modelo1.setText(reportes.get(i).getModelo());
                lbl_color1.setText(reportes.get(i).getColor());
                lbl_aseguradora1.setText(reportes.get(i).getNombreAseguradora());
                lbl_poliza1.setText(reportes.get(i).getNumPoliza());
                lbl_placas1.setText(reportes.get(i).getPlaca());
            } else {
                lbl_marca2.setText(reportes.get(i).getMarca());
                lbl_modelo2.setText(reportes.get(i).getModelo());
                lbl_color2.setText(reportes.get(i).getColor());
                lbl_aseguradora2.setText(reportes.get(i).getNombreAseguradora());
                lbl_poliza2.setText(reportes.get(i).getNumPoliza());
                lbl_placas2.setText(reportes.get(i).getPlaca());
            }
        }

        if (reportes.size() == 1) {
            try {
                ClienteServicios cliente = new ClienteServicios();
                Vehiculo vel = cliente.recuperarVehiculo(reportes.get(0).getIdVehiculo());
                lbl_marca2.setText(vel.getMarca());
                lbl_modelo2.setText(vel.getModelo());
                lbl_color2.setText(vel.getColor());
                lbl_aseguradora2.setText(vel.getNombreAseguradora());
                lbl_poliza2.setText(vel.getNumPoliza());
                lbl_placas2.setText(vel.getPlaca());
                lbl_anio2.setText(String.valueOf(vel.getAnio()));
            } catch (TTransportException ex) {
                lbl_error.setText("Error al conectar con el servidor");
                lbl_error.setVisible(true);
            } catch (TException ex) {
                lbl_error.setText("Error al conectar con el servidor");
                lbl_error.setVisible(true);
            }
        }
        //   recuperarImagenes(reportes);
    }

    void recibirParametros(Incidente incidenteSeleccionado, UsuarioClienteEscritorio usuario) {
        this.incidente = incidenteSeleccionado;
        lbl_titulo.setText("Detalle de incidente " + incidenteSeleccionado.getIdIncidente());
        this.usuario = usuario;
        if (incidente.getEstado() == 0) {
            lbl_estado.setText("Pendiente");
        } else {
            lbl_estado.setText("Dictaminado");
            btn_dictaminar.setDisable(true);
        }
        iniciarPantalla();
    }

    void recuperarImagenes(List<Reporte> reportes) {
        List<Foto> fotosThrift1 = null;
        try {
            ClienteServicios cliente = new ClienteServicios();
            fotosThrift1 = cliente.recuperarFotosReportadas(reportes.get(0).getIdReporte());
            if (reportes.size() > 1) {
                cliente = new ClienteServicios();
                List<Foto> fotosThrift2 = cliente.recuperarFotosReportadas(reportes.get(1).getIdReporte());
                for (int i = 0; i < fotosThrift2.size(); i++) {
                    fotosThrift1.add(fotosThrift2.get(i));
                }
            }
        } catch (TTransportException ex) {
            lbl_error.setText("Error al recuperar las imagenes");
            lbl_error.setVisible(true);
        } catch (TException ex) {
            lbl_error.setText("Error al recuperar las imagenes");
            lbl_error.setVisible(true);
        }

        List<byte[]> imagenesBytes = new ArrayList();
        System.out.print(fotosThrift1.size());
        for (int i = 0; i < fotosThrift1.size(); i++) {
            System.out.println(fotosThrift1.get(i).getFoto());
            byte[] hola = DatatypeConverter.parseBase64Binary(fotosThrift1.get(i).getFoto());

            imagenesBytes.add(hola);
        }
        for (int i = 0; i < imagenesBytes.size(); i++) {
            String ruta = "fotos\\ima" + String.valueOf(i) + ".jpg";
            crearImagen(ruta, imagenesBytes.get(i));
        }

        ObservableList<ImageView> imagenes = null;
        for (int i = 0; i < imagenesBytes.size(); i++) {
            String ruta = "fotos\\ima" + String.valueOf(i) + ".jpg";
            File fi = new File(ruta);
            Image im = new Image(fi.toURI().toString());
            ImageView m = new ImageView();
            m.setImage(im);
            if (i == 0) {
                imagenes = FXCollections.observableArrayList(m);
            } else {
                imagenes.add(m);
            }
        }
        lst_imagenes.setItems(imagenes);
    }

    private void crearImagen(String ruta, byte[] imagen) {
        OutputStream out;
        try {
            out = new BufferedOutputStream(new FileOutputStream(ruta));
            out.write(imagen);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerDetalleIncidenteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerDetalleIncidenteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
