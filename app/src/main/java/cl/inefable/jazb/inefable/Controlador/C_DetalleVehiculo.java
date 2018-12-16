package cl.inefable.jazb.inefable.Controlador;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Valoracion;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

import static cl.inefable.jazb.inefable.Controlador.Funciones.UsuarioActual;

public class C_DetalleVehiculo extends AppCompatActivity {

    private O_Vehiculo vehiculo;
    public final static int ActCode = 112;

    private TextView Patente, Marca, Volumen, CargaMax, Tipo, Propietario, Valor, ValorBase, Estado;
    private RatingBar PuntuacionPromedio;

    private RecyclerView ListaComentarios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detallevehiculo);

        InicializarComponenetes();
        ConfigurarComponentes();
        //ConfigurarListeners();
    }

    private void ConfigurarComponentes() {
        vehiculo = (O_Vehiculo) getIntent().getSerializableExtra("VEHICULO");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        double volumen = vehiculo.getAltura() / 100 * vehiculo.getAncho() / 100 * vehiculo.getLargo() / 100;

        Patente.setText("Patente: " + vehiculo.getPatente());
        Marca.setText("Marca: " + vehiculo.getMarca());
        Volumen.setText("Volumen: " + volumen + " m³");
        CargaMax.setText("Carga Máx.: " + vehiculo.getCargaMax() + " Kg");
        Tipo.setText("Tipo de vehículo: " + vehiculo.getTipo());
        Valor.setText("Valor ($/Km): $" + vehiculo.getValor());
        ValorBase.setText("Valor Base: $" + vehiculo.getValorBase());
        String estado = (vehiculo.getDisponibilidad() == 1) ? "Disponible" : "No disponible";
        Estado.setText("Estado: " + estado);
        if (vehiculo.getDueño() != null) {
            Propietario.setText("Propietario: " + vehiculo.getDueño().getNombres() + " " + vehiculo.getDueño().getApellidos());
        } else {
            Propietario.setText("Propietario: " + UsuarioActual.getNombres() + " " + UsuarioActual.getApellidos());
        }
        PuntuacionPromedio.setRating((float) vehiculo.getValoracion());
        CargarComentarios();
    }

    private void CargarComentarios() {
        ArrayList<O_Valoracion> listado = F_Vehiculo.TraerValoraciones(vehiculo);
        if (listado == null || listado.size() == 0) {
            return;
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        ListaComentarios.setLayoutManager(llm);
        Lista_Comentarios lista = new Lista_Comentarios(listado);
        if (lista.getItemCount() == 0) {
            O_Alerta alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Mis Camiones",
                    "Aún no tienes camiones reservados, comienza a reservar con el botón en la parte inferior de la pantalla.",
                    false,
                    3500,
                    O_Alerta.RES_ICO_INFO
            );
            MostrarAlerta(alerta);
        }
        ListaComentarios.setAdapter(lista);
    }

    private void MostrarAlerta(O_Alerta alerta) {
        Alerter.create(this)
                .setTitle(alerta.getTitulo())
                .setText(alerta.getMensaje())
                .setBackgroundColorRes(alerta.getTipo())
                .enableInfiniteDuration(alerta.isInfinito())
                .enableIconPulse(true)
                .setIcon(alerta.getIcono())
                .setDuration(alerta.getDuracion())
                .enableSwipeToDismiss()
                .show();
    }

    class Lista_Comentarios extends RecyclerView.Adapter<Lista_Comentarios.VehicleViewHolder> {
        private ArrayList<O_Valoracion> Valoraciones;

        public Lista_Comentarios(ArrayList<O_Valoracion> valoraciones) {
            this.Valoraciones = valoraciones;
        }

        @NonNull
        @Override
        public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_comentarios, parent, false);
            VehicleViewHolder pvh = new VehicleViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position) {
            holder.Comentario.setText(Valoraciones.get(position).getComentario());
            holder.Fecha.setText(Valoraciones.get(position).getFecha());
            holder.Puntaje.setRating(Valoraciones.get(position).getValoracion());
        }

        @Override
        public int getItemCount() {
            return Valoraciones.size();
        }

        public class VehicleViewHolder extends RecyclerView.ViewHolder {
            TextView Comentario, Fecha;
            RatingBar Puntaje;

            VehicleViewHolder(View itemView) {
                super(itemView);
                Comentario = itemView.findViewById(R.id.item_comentario_comentario);
                Fecha = itemView.findViewById(R.id.item_comentario_fecha);
                Puntaje = itemView.findViewById(R.id.item_comentario_valoracion);
            }
        }
    }

    private void InicializarComponenetes() {
        Patente = findViewById(R.id.tv_detallevehiculo_patente);
        Marca = findViewById(R.id.tv_detallevehiculo_marca);
        Volumen = findViewById(R.id.tv_detallevehiculo_volumen);
        CargaMax = findViewById(R.id.tv_detallevehiculo_cargamax);
        Tipo = findViewById(R.id.tv_detallevehiculo_tipo);
        Propietario = findViewById(R.id.tv_detallevehiculo_propietario);
        Valor = findViewById(R.id.tv_detallevehiculo_valor);
        ValorBase = findViewById(R.id.tv_detallevehiculo_valorbase);
        PuntuacionPromedio = findViewById(R.id.rb_detallevehiculo_valoracionpromedio);
        Estado = findViewById(R.id.tv_detallevehiculo_estado);

        ListaComentarios = findViewById(R.id.rv_detallevehiculo_comentarios);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_detallevehiculo, menu);
        return true;
    }

    */
}
