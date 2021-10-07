package com.johnchaves.alertapp;

import android.content.Context;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem {

    public String fecha, usuario, observacion, accion, descripcion;

    public ListItem(String fecha, String usuario, String observacion, String accion, String descripcion )
    {
        this.fecha = fecha;
        this.usuario = usuario;
        this.observacion = observacion;
        this.accion = accion;
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getObservacion(){
        return observacion;
    }

    public String getAccion(){
        return accion;
    }

    public String getDescripcion(){ return descripcion; }
}
