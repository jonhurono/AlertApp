package com.johnchaves.alertapp;

public class OfertaItem {

    public String codart, desart, stoart, precio, oferta, diff;

    public OfertaItem(String codart, String desart, String stoart,
                      String precio, String oferta, String diff)
    {
        this.codart = codart;
        this.desart = desart;
        this.stoart = stoart;
        this.precio = precio;
        this.oferta = oferta;
        this.diff   = diff;
    }

    public String getCodart() {
        return codart;
    }

    public String getDesart() {
        return desart;
    }

    public String getStoart(){
        return stoart;
    }

    public String getPrecio(){
        return precio;
    }

    public String getOferta(){
        return oferta;
    }

    public String getDiff()   { return diff; }

}
