package com.johnchaves.alertapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.alertapp.R;

import java.util.ArrayList;

public class PopModo extends Activity {
    ArrayList<String> selection = new ArrayList<String>();
    TextView modo = MainActivity.getModo();
    TextView    fechaold = MainActivity.getFechaold();
    TextView    fechanew = MainActivity.getFechanew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_modo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.85),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);
    }

    public void selectItem(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.acepta_desp_picking:
                if(checked){
                    selection.add("ACEPTA DESP_PICKING");
                }
                else{
                    selection.remove("ACEPTA DESP_PICKING");
                }
                break;
            case R.id.acepta_despacho_item:
                if(checked){
                    selection.add("ACEPTA DESPACHO ITEM");
                }
                else{
                    selection.remove("ACEPTA DESPACHO ITEM");
                }
                break;
            case R.id.acepta_tr:
                if(checked){
                    selection.add("ACEPTA TR");
                }
                else{
                    selection.remove("ACEPTA TR");
                }
                break;
            case R.id.anula_itemizada:
                if(checked){
                    selection.add("ANULA ITEMIZADA");
                }
                else{
                    selection.remove("ANULA ITEMIZADA");
                }
                break;
            case R.id.anulacion:
                if(checked){
                    selection.add("ANULACION");
                }
                else{
                    selection.remove("ANULACION");
                }
                break;
            case R.id.aumenta_saldo:
                if(checked){
                    selection.add("AUMENTA SALDO");
                }
                else{
                    selection.remove("AUMENTA SALDO");
                }
                break;
            case R.id.cambio_oficina:
                if(checked){
                    selection.add("CAMBIO OFICINA");
                }
                else{
                    selection.remove("CAMBIO OFICINA");
                }
                break;
            case R.id.capac_cajas:
                if(checked){
                    selection.add("CAPAC. CAJAS");
                }
                else{
                    selection.remove("CAPAC. CAJAS");
                }
                break;
            case R.id.eliminacion:
                if(checked){
                    selection.add("ELIMINACION");
                }
                else{
                    selection.remove("ELIMINACION");
                }
                break;
            case R.id.liberacion:
                if(checked){
                    selection.add("LIBERACIÓN");
                }
                else{
                    selection.remove("LIBERACIÓN");
                }
                break;
            case R.id.log_in:
                if(checked){
                    selection.add("LOG IN");
                }
                else{
                    selection.remove("LOG IN");
                }
                break;
            case R.id.modifica_items:
                if(checked){
                    selection.add("MODIFICA ITEMS");
                }
                else{
                    selection.remove("MODIFICA ITEMS");
                }
                break;
            case R.id.modifica_oferta:
                if(checked){
                    selection.add("MODIFICA OFERTA");
                }
                else{
                    selection.remove("MODIFICA OFERTA");
                }
                break;
            case R.id.modifica_tr:
                if(checked){
                    selection.add("MODIFICA TR");
                }
                else{
                    selection.remove("MODIFICA TR");
                }
                break;
            case R.id.modifica_visacion:
                if(checked){
                    selection.add("MODIFICA VISACION");
                }
                else{
                    selection.remove("MODIFICA VISACION");
                }
                break;
            case R.id.nueva_oferta:
                if(checked){
                    selection.add("NUEVA OFERTA");
                }
                else{
                    selection.remove("NUEVA OFERTA");
                }
                break;
            case R.id.nuevo_tr:
                if(checked){
                    selection.add("NUEVO TR");
                }
                else{
                    selection.remove("NUEVO TR");
                }
                break;
            case R.id.nuevo_tr_picking:
                if(checked){
                    selection.add("NUEVO TR_PICKING");
                }
                else{
                    selection.remove("NUEVO TR_PICKING");
                }
                break;
            case R.id.solic_pendientes:
                if(checked){
                    selection.add("SOLIC. PENDIENTES");
                }
                else{
                    selection.remove("SOLIC. PENDIENTES");
                }
                break;
            case R.id.tr_pendientes:
                if(checked){
                    selection.add("TR PENDIENTES");
                }
                else{
                    selection.remove("TR PENDIENTES");
                }
                break;
        }
    }

    public void finalSelection(View view){
        String final_modo = "-";

        for(String Selections : selection){
            final_modo = final_modo + Selections + "-";
        }
        modo.setText(final_modo);

        finish();
    }
}