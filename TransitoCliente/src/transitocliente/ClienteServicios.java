/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente;

import Thrift.Conductor;
import Thrift.Dictamen;
import Thrift.Foto;
import Thrift.Incidente;
import Thrift.Reporte;
import Thrift.Servicios;
import Thrift.UsuarioClienteEscritorio;
import Thrift.Vehiculo;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author ponch
 */
public class ClienteServicios {

    private Servicios.Client cliente;
    private TSocket transport;

    public ClienteServicios() {

    }

    public void iniciarCliente() throws TTransportException {
        transport = new TSocket("192.168.43.191", 9090);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        cliente = new Servicios.Client(protocol);
    }

    public boolean validarCredenciales(String usuario, String contrasena) throws TException {
        this.iniciarCliente();
        boolean resultado = cliente.validarCredenciales(usuario, contrasena);
        transport.close();
        return resultado;
    }

    public UsuarioClienteEscritorio recuperarUsuario(String usuario) throws TException {
        this.iniciarCliente();
        UsuarioClienteEscritorio resultado = cliente.recuperarUsuario(usuario);
        transport.close();
        return resultado;
    }

    public void registrarUsuario(UsuarioClienteEscritorio usuario) throws TException {
        this.iniciarCliente();
        cliente.registrarUsuario(usuario);
        transport.close();
    }

    public List<Incidente> recuperarIncidentes() throws TException {
        this.iniciarCliente();
        List<Incidente> incidentes = cliente.recuperarIncidentes();
        transport.close();
        return incidentes;
    }

    public List<Reporte> recuperarReportes(int idIncidente) throws TException {
        this.iniciarCliente();
        List<Reporte> reportes = cliente.recuperarReportes(idIncidente);
        transport.close();
        return reportes;
    }

    public Vehiculo recuperarVehiculo(int idVehiculo) throws TException {
        this.iniciarCliente();
        Vehiculo vehiculo = cliente.recuperarVehiculoInvolucrado(idVehiculo);
        transport.close();
        return vehiculo;
    }

    public Conductor recuperarConductor(int idConductor) throws TException {
        this.iniciarCliente();
        Conductor conductor = cliente.recuperarConductorInvolucrado(idConductor);
        transport.close();
        return conductor;
    }

    public List<Foto> recuperarFotosReportadas(int idReporte) throws TException {
        this.iniciarCliente();
        List<Foto> fotos = cliente.recuperarFotosReportadas(idReporte);
        transport.close();
        return fotos;
    }

    public void dictaminarIncidente(Dictamen dic) throws TException {
        this.iniciarCliente();
        cliente.dictaminarIncidente(dic);
        transport.close();
    }

    public boolean validarUsuario(String usuario) throws TException {
        this.iniciarCliente();
        boolean resultado = cliente.validarUsuario(usuario);
        transport.close();
        return resultado;
    }

    public boolean validarFolio(String folio) throws TException {
        this.iniciarCliente();
        boolean resultado = cliente.validarFolio(folio);
        transport.close();
        return resultado;
    }

    public void actualizarContrasena(int idUsuario, String contrasena) throws TException {
        this.iniciarCliente();
        cliente.actualizarContrasena(idUsuario, contrasena);
        transport.close();
    }
}
