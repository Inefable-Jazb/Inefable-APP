package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class C_FiltrarVehículos extends AppCompatActivity {

    public static final int ActCode = 74;

    private TextInputLayout CargaMax, Altura, Ancho, Largo;

    private LinearLayout PanelFiltros;
    private RecyclerView ListaFiltrada;

    private CheckBox MostrarFiltros;

    private O_Reserva reserva;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_filtrarvehiculos);

        InicializarComponentes();
        ConfigurarComponenetes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        MostrarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MostrarFiltros.isChecked()) {
                    PanelFiltros.setVisibility(View.VISIBLE);
                } else {
                    PanelFiltros.setVisibility(View.GONE);
                }
            }
        });
    }

    private void ConfigurarComponenetes() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void InicializarComponentes() {
        CargaMax = findViewById(R.id.til_filtrarvehículos_cargamax);
        Altura = findViewById(R.id.til_filtrarvehículos_altura);
        Ancho = findViewById(R.id.til_filtrarvehículos_ancho);
        Largo = findViewById(R.id.til_filtrarvehículos_largo);

        PanelFiltros = findViewById(R.id.ll_filtrarvehículos_panelfiltros);
        ListaFiltrada = findViewById(R.id.rv_filtrarvehiculos_listafiltrada);

        MostrarFiltros = findViewById(R.id.id_filtrarvehiculos_mostrarfiltros);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_filtrarvehiculos, menu);
        return true;
    }

    private void CargarVehiculosFiltrados(String filtros) {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        ListaFiltrada.setLayoutManager(llm);
        O_Reserva res = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
        Lista_Filtrada lista = new Lista_Filtrada(F_Vehiculo.FiltrarVehiculos(filtros, res.getUsuario().getID()));
        if (lista.getItemCount() == 0) {
            O_Alerta alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Mis Camiones",
                    "No hay camiones disponibles, intenta nuevamente.",
                    false,
                    4500,
                    O_Alerta.RES_ICO_INFO
            );
            MostrarAlerta(alerta);
        }
        ListaFiltrada.setAdapter(lista);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res;
        switch (item.getItemId()) {
            case R.id.menu_filtrarvehiculos_buscar:
                String variables = "";
                String cargamax = CargaMax.getEditText().getText().toString().trim();
                if (!cargamax.equals("")) {
                    variables += "&CARGAMAX=" + cargamax;
                }
                String altura = Altura.getEditText().getText().toString().trim();
                if (!altura.equals("")) {
                    variables += "&ALTURA=" + altura;
                }
                String ancho = Ancho.getEditText().getText().toString().trim();
                if (!altura.equals("")) {
                    variables += "&ANCHO=" + ancho;
                }
                String largo = Largo.getEditText().getText().toString().trim();
                if (!altura.equals("")) {
                    variables += "&LARGO=" + largo;
                }
                CargarVehiculosFiltrados(variables);
                res = super.onOptionsItemSelected(item);
                break;
            case android.R.id.home:
                this.onBackPressed();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    class Lista_Filtrada extends RecyclerView.Adapter<Lista_Filtrada.VehicleViewHolder> {
        private ArrayList<O_Vehiculo> vehiculos;

        public Lista_Filtrada(ArrayList<O_Vehiculo> vehiculos) {
            this.vehiculos = vehiculos;
        }

        @NonNull
        @Override
        public Lista_Filtrada.VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_resultadofiltro, parent, false);
            Lista_Filtrada.VehicleViewHolder pvh = new Lista_Filtrada.VehicleViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final Lista_Filtrada.VehicleViewHolder holder, final int position) {
            holder.asd.setTag(vehiculos.get(position));
            holder.asd.setOnClickListener(rv_ItemClick());
            holder.Patente.setText(vehiculos.get(position).getPatente());
            holder.Tipo.setText("Marca: " + vehiculos.get(position).getTipo());
            holder.Costo.setText("Valor: $" + vehiculos.get(position).getValor() + " /km");
            holder.Valoracion.setRating((float) vehiculos.get(position).getValoracion());
        }

        @Override
        public int getItemCount() {
            return vehiculos.size();
        }

        public class VehicleViewHolder extends RecyclerView.ViewHolder {
            View asd;
            RecyclerView lista;
            TextView Patente, Costo, Tipo;
            RatingBar Valoracion;

            VehicleViewHolder(View itemView) {
                super(itemView);
                asd = itemView;
                lista = itemView.findViewById(R.id.rv_filtrarvehiculos_listafiltrada);
                Costo = itemView.findViewById(R.id.tv_resultadofiltro_Costo);
                Patente = itemView.findViewById(R.id.tv_resultadofiltro_Patente);
                Tipo = itemView.findViewById(R.id.tv_resultadofiltro_Tipo);
                Valoracion = itemView.findViewById(R.id.rb_resultadofiltro_valoracion);
            }
        }
    }

    private View.OnClickListener rv_ItemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                O_Vehiculo vehiculo = (O_Vehiculo) v.getTag();
                O_Reserva reserva;
                reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
                if (reserva == null) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    return;
                }
                reserva.setVehiculo(vehiculo);
                Intent destino = new Intent(C_FiltrarVehículos.this, C_DetalleReserva.class);
                destino.putExtra("RESERVA", reserva);
                destino.putExtra("VEHICULO", vehiculo);
                startActivityForResult(destino, C_DetalleReserva.ActCode);
            }
        };
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case C_DetalleReserva.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                    finish();
                    return;
                }
                O_Reserva aux = (O_Reserva) data.getSerializableExtra("RESERVA");
                getIntent().putExtra("RESERVA", aux);
                break;
        }
    }

}
