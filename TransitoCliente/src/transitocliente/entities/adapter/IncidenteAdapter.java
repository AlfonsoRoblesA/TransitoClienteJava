/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transitocliente.entities.adapter;

import java.util.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Foncho
 */
public class IncidenteAdapter {
    
    public SimpleIntegerProperty idIncidente = new SimpleIntegerProperty();
    public SimpleObjectProperty<Date> fecha = new SimpleObjectProperty<>();
    public SimpleStringProperty ciudad = new SimpleStringProperty();
    public SimpleStringProperty estado = new SimpleStringProperty();

    public IncidenteAdapter() {
    }

    public Integer getIdIncidente() {
        return idIncidente.get();
    }

    public Date getFecha() {
        return fecha.get();
    }

    public String getCiudad() {
        return ciudad.get();
    }

    public String getEstado() {
        return estado.get();
    }
    
}
