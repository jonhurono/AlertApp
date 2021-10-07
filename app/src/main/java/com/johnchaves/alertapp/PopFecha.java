package com.johnchaves.alertapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.alertapp.R;

import java.util.Calendar;

public class PopFecha extends Activity {
    DatePickerDialog picker1, picker2;
    TextView    fecha1, fecha2;
    Button      boton;
    TextView    fechaold = MainActivity.getFechaold();
    TextView    fechanew = MainActivity.getFechanew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_fecha);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        getWindow().setAttributes(params);

        fecha1  = (TextView) findViewById(R.id.fecha1);
        fecha2  = (TextView) findViewById(R.id.fecha2);
        //picker1 = (DatePicker) findViewById(R.id.datePicker1);
        //picker2 = (DatePicker) findViewById(R.id.datePicker2);
        boton   = (Button) findViewById(R.id.button);

        fecha1.setInputType(InputType.TYPE_NULL);
        fecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr1 = Calendar.getInstance();
                int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr1.get(Calendar.MONTH);
                int year1 = cldr1.get(Calendar.YEAR);

                //datepicker dialogo
                picker1 = new DatePickerDialog(PopFecha.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha1.setText(dayOfMonth+"/"+(month + 1)+"/"+ year);
                    }
                }, year1,month1,day1);
                picker1.show();
            }
        });
        fecha2.setInputType(InputType.TYPE_NULL);
        fecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr1 = Calendar.getInstance();
                int day2 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month2 = cldr1.get(Calendar.MONTH);
                int year2 = cldr1.get(Calendar.YEAR);

                //datepicker dialogo
                picker2 = new DatePickerDialog(PopFecha.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha2.setText(dayOfMonth+"/"+(month+1)+"/"+ year);
                    }
                }, year2,month2,day2);
                picker2.show();
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaold.setText(fecha1.getText());
                fechanew.setText(fecha2.getText());
                finish();
            }
        });
    }
}
