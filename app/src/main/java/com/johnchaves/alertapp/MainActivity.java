package com.johnchaves.alertapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alertapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton    acciones, fechas, search, ncl, oferta;
    //TextView                fecha, usuario, observacion, accion;
    //TextView                codart, desart, ncl, codbod, sigdoc, numvisacion, numitem, anio;
    SwipeRefreshLayout      swipeRefreshLayout;
    Spinner                 filtroUsuario;
    private static TextView modo, fechaold, fechanew;

    private ArrayList<ListItem> itemArrayList;  //List items Array
    private ArrayList<NCLItem> arreglinNCL;
    private ArrayList<OfertaItem> ofertaItemArrayList;
    private MyAppAdapter    myAppAdapter; //Array Adapter
    private MyNCLAdapter    myNCLAdapter;
    private MyOfertaAdapter myOfertaAdapter;
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean success = false; // boolean
    private ConnectionClass connectionClass; //Connection Class Variable


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fecha               = (TextView) findViewById(R.id.cab_fecha);
        //usuario             = (TextView) findViewById(R.id.cab_usuario);
        //observacion         = (TextView) findViewById(R.id.cab_observacion);
        //accion              = (TextView) findViewById(R.id.cab_accion);
        modo                = (TextView) findViewById(R.id.Modo);
        fechaold            = (TextView) findViewById(R.id.fechaold);
        fechanew            = (TextView) findViewById(R.id.fechanew);
        filtroUsuario       = (Spinner) findViewById(R.id.spinner);
        acciones            = (FloatingActionButton) findViewById(R.id.FiltroModo);
        fechas              = (FloatingActionButton) findViewById(R.id.FiltroFecha);
        ncl                 = (FloatingActionButton) findViewById(R.id.FiltroNCL);
        search              = (FloatingActionButton) findViewById(R.id.Search);
        oferta              = (FloatingActionButton) findViewById(R.id.FiltroOferta);
        recyclerView        = (RecyclerView) findViewById(R.id.recycler);
        swipeRefreshLayout  = (SwipeRefreshLayout) findViewById(R.id.swype);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(Calendar.getInstance().getTime());
        fechanew.setText(today);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        Date newDate = calendar.getTime();
        String lastweek = df.format(newDate);
        fechaold.setText(lastweek);

        connectionClass = new ConnectionClass();
        itemArrayList = new ArrayList<ListItem>();
        arreglinNCL = new ArrayList<NCLItem>();
        ofertaItemArrayList = new ArrayList<OfertaItem>();

        GetUsuarios();

        //Async Task
        //SyncData orderData = new SyncData();

        oferta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ofertaItemArrayList.clear();
                arreglinNCL.clear();
                itemArrayList.clear();
                consultaOfer();
                modo.setText(null);
            }
        });

        ncl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ofertaItemArrayList.clear();
                arreglinNCL.clear();
                itemArrayList.clear();
                consultaNCL();
                modo.setText(null);
            }
        });

        acciones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopModo.class);
                startActivity(i);
            }
        });
        fechas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PopFecha.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (fechanew.getText().toString() == ""){
                    fechanew.setText("2/9/2021");
                }
                if (fechaold.getText().toString() == ""){
                    fechaold.setText("25/8/2021");
                }
                ofertaItemArrayList.clear();
                arreglinNCL.clear();
                itemArrayList.clear();
                consultaM();
    //            orderData.execute("");
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ofertaItemArrayList.clear();
                arreglinNCL.clear();
                itemArrayList.clear();
                consultaM();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        filtroUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ofertaItemArrayList.clear();
                arreglinNCL.clear();
                itemArrayList.clear();
                consultaU();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //itemArrayList.clear();
                //arreglinNCL.clear();
            }

        });
    }

    public static TextView getModo() {
        return modo;
    }
    public static TextView getFechaold() {return fechaold; }
    public static TextView getFechanew() {return fechanew; }

    public void GetUsuarios(){
        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_AlertApp @Modo = 'V' ");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                ArrayList<String> data = new ArrayList<String>();
                while (rs.next()) {
                    String usuario = rs.getString(1);
                    data.add(usuario);
                }

                ArrayAdapter array = new ArrayAdapter(this, R.layout.spinnerlayout,data);
                filtroUsuario.setAdapter (array);
                success = true;

            } else {
                Toast.makeText(getApplicationContext(), "ERROR EN LA SELECCIÓN DE PARÁMETROS", Toast.LENGTH_SHORT).show();
                success = false;
            }
            myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
            recyclerView.setAdapter(myAppAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Async Task
    /*
    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(MainActivity.this, "Consultando registros",
                    "¡Cargando vista! Por favor espere...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            try {
                Connection conn = connectionClass.CONN(); //Connection Object
                if (conn == null) {
                    success = false;
                } else {
                    // Change below query according to your own database.
                    //String query = "EXEC Sp_C_AlertApp @Modo = 'C'";
                    String query = "EXEC Sp_C_AlertApp @Modo = 'A', @TipoAlerta = ' "+modo.getText().toString()+"' ";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next()) {
                            try {
                                itemArrayList.add(new ListItem(rs.getString(1), rs.getString(2),
                                        rs.getString(3), rs.getString(4)));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "BÚSQUEDA EXITOSA";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            Toast.makeText(MainActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }
            }
        }
    }
*/

    public Connection conexionDB(){
        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.11;databaseName=Terra;user=Movil;password=Mv2021;");

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"SIN CONEXIÓN A BASE DE DATOS",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    public void consultaM() {

        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_AlertApp @Modo = 'F', @TipoAlerta = '"+modo.getText().toString()+"', " +
                    "@Fecha1 = '"+fechanew.getText().toString()+"', @Fecha2 = '"+fechaold.getText().toString()+"' ");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                while (rs.next()) {
                    try {
                        itemArrayList.add(new ListItem(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                success = true;
            } else {
                Toast.makeText(getApplicationContext(), "ERROR EN LA SELECCIÓN DE PARÁMETROS", Toast.LENGTH_SHORT).show();
                success = false;
            }
            myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
            recyclerView.setAdapter(myAppAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultaU() {

        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_AlertApp @Modo = 'U', @User = '"+filtroUsuario.getSelectedItem().toString()+"' ");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                while (rs.next()) {
                    try {
                        itemArrayList.add(new ListItem(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                success = true;
            } else {
                Toast.makeText(getApplicationContext(), "ERROR EN LA SELECCIÓN DE PARÁMETROS", Toast.LENGTH_SHORT).show();
                success = false;
            }
            myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
            recyclerView.setAdapter(myAppAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultaOfer() {

        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_AlertApp @Modo = 'O' ");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                while (rs.next()) {
                    try {
                        ofertaItemArrayList.add(new OfertaItem (
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                "$"+rs.getString(4),
                                "$"+rs.getString(5),
                                rs.getString(6)));
                        //Toast.makeText(getApplicationContext(), "SE ENCONTRARON PRODUCTOS EN OFERTA", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON RESULTADOS", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
                success = true;
            } else {
                success = false;
                Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON RESULTADOS", Toast.LENGTH_SHORT).show();
            }
            myOfertaAdapter = new MyOfertaAdapter(ofertaItemArrayList, MainActivity.this);
            recyclerView.setAdapter(myOfertaAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void consultaNCL() {

        try {
            Statement stm = conexionDB().createStatement();
            ResultSet rs = stm.executeQuery("EXEC Sp_C_AlertApp @Modo = 'N' ");

            if (rs != null) // if resultset not null, I add items to itemArraylist using class created
            {
                while (rs.next()) {
                    try {
                        arreglinNCL.add(new NCLItem(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                rs.getString(7),
                                rs.getString(8)));
                        Toast.makeText(getApplicationContext(), "SE ENCONTRARON NUM_CANTIDAD_LINEA EN NEGATIVO", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON RESULTADOS", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
                success = true;
            } else {
                success = false;
                Toast.makeText(getApplicationContext(), "NO SE ENCONTRARON RESULTADOS", Toast.LENGTH_SHORT).show();
            }
            myNCLAdapter = new MyNCLAdapter(arreglinNCL, MainActivity.this);
            recyclerView.setAdapter(myNCLAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
        try {
            Connection conn = connectionClass.CONN(); //Connection Object
            if (conn == null) {
                success = false;
            } else {
                // Change below query according to your own database.
                String query = "EXEC Sp_C_AlertApp @Modo = 'A', @TipoAlerta = '"+modo.getText().toString()+"' ";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                {
                    while (rs.next()) {
                        try {
                            itemArrayList.add(new ListItem
                                            (rs.getString(1),
                                            rs.getString(2),
                                            rs.getString(3),
                                            rs.getString(4)));

                            myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
                            recyclerView.setAdapter(myAppAdapter);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    success = true;
                } else {
                    success = false;
                }
            }
            if (success == false) {
            } else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, MainActivity.this);
                    recyclerView.setAdapter(myAppAdapter);
                } catch (Exception ex) {

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            success = false;
        }*/

    public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.ViewHolder> {
        private List<ListItem> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView fecha, usuario, observacion, accion, descripcion;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                fecha = (TextView) v.findViewById(R.id.fecha);
                usuario = (TextView) v.findViewById(R.id.usuario);
                observacion = (TextView) v.findViewById(R.id.observacion);
                accion = (TextView) v.findViewById(R.id.accion);
                descripcion = (TextView) v.findViewById(R.id.descripcionart);
            }
        }

        // Constructor
        public MyAppAdapter(List<ListItem> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyAppAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.listlayout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final ListItem classListItems = values.get(position);
            holder.fecha.setText(classListItems.getFecha());
            holder.usuario.setText(classListItems.getUsuario());
            holder.observacion.setText(classListItems.getObservacion());
            holder.accion.setText(classListItems.getAccion());
            holder.descripcion.setText(classListItems.getDescripcion());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }
    }

    public class MyOfertaAdapter extends RecyclerView.Adapter<MyOfertaAdapter.ViewHolder> {
        private List<OfertaItem> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView codart, desart, stoart, precio, oferta, diff;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                codart = (TextView) v.findViewById(R.id.codart);
                desart = (TextView) v.findViewById(R.id.desart);
                stoart = (TextView) v.findViewById(R.id.stoart);
                precio = (TextView) v.findViewById(R.id.precio);
                oferta = (TextView) v.findViewById(R.id.oferta);
                diff   = (TextView) v.findViewById(R.id.diff);
            }
        }

        // Constructor
        public MyOfertaAdapter(List<OfertaItem> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyOfertaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.ofertaslayout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final OfertaItem classListItems = values.get(position);
            holder.codart.setText(classListItems.getCodart());
            holder.desart.setText(classListItems.getDesart());
            holder.stoart.setText(classListItems.getStoart());
            holder.precio.setText(classListItems.getPrecio());
            holder.oferta.setText(classListItems.getOferta());
            holder.diff.setText(classListItems.getDiff());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }
    }

    public class MyNCLAdapter extends RecyclerView.Adapter<MyNCLAdapter.ViewHolder> {
        private List<NCLItem> values;
        public Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // public image title and image url
            public TextView codart, desart, ncl, codbod, sigdoc, numvisacion, numitem, anio;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                codart = (TextView) v.findViewById(R.id.codart);
                desart = (TextView) v.findViewById(R.id.desart);
                ncl = (TextView) v.findViewById(R.id.oferta);
                codbod = (TextView) v.findViewById(R.id.stoart);
                sigdoc = (TextView) v.findViewById(R.id.precio);
                numvisacion = (TextView) v.findViewById(R.id.numvisacion);
                numitem = (TextView) v.findViewById(R.id.diff);
                anio = (TextView) v.findViewById(R.id.anio);
            }
        }

        // Constructor
        public MyNCLAdapter(List<NCLItem> myDataset, Context context) {
            values = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager) and inflates
        @Override
        public MyNCLAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.ncllayout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Binding items to the view
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final NCLItem classListItems = values.get(position);
            holder.codart.setText(classListItems.getCodart());
            holder.desart.setText(classListItems.getDesart());
            holder.ncl.setText(classListItems.getNcl());
            holder.codbod.setText(classListItems.getCodbod());
            holder.sigdoc.setText(classListItems.getSigdoc());
            holder.numvisacion.setText(classListItems.getNumvisacion());
            holder.numitem.setText(classListItems.getNumitem());
            holder.anio.setText(classListItems.getAnio());
        }

        // get item count returns the list item count
        @Override
        public int getItemCount() {
            return values.size();
        }
    }

}