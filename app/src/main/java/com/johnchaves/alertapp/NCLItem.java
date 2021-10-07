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

public class NCLItem {

    public String codart, desart, ncl, codbod, sigdoc, numvisacion, numitem, anio;

    public NCLItem(String codart, String desart, String ncl, String codbod, String sigdoc,
                   String numvisacion, String numitem, String anio)
    {
        this.codart = codart;
        this.desart = desart;
        this.ncl = ncl;
        this.codbod = codbod;
        this.sigdoc = sigdoc;
        this.numvisacion = numvisacion;
        this.numitem = numitem;
        this.anio = anio;
    }

    public String getCodart() {
        return codart;
    }

    public String getDesart() {
        return desart;
    }

    public String getNcl(){
        return ncl;
    }

    public String getCodbod(){
        return codbod;
    }

    public String getSigdoc(){
        return sigdoc;
    }

    public String getNumvisacion(){
        return numvisacion;
    }

    public String getNumitem(){
        return numitem;
    }
    public String getAnio(){
        return anio;
    }
}
