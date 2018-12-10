package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import cl.inefable.jazb.inefable.Controlador.Servicios.S_Notificador;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class C_Principal extends AppCompatActivity {
    private O_Usuario UsuarioActual;

    private FloatingActionButton Agregar, Pendientes;

    private TextView Saludo, Titulo;

    private RecyclerView Lista;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pricinpal);

        InicializarComponenetes();
        ConfigurarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarComponentes() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Funciones.UsuarioActual == null) {
            int IDU = getIntent().getIntExtra("IDUSUARIO", -1);
            Funciones.UsuarioActual = F_Usuario.TraerDatosUsuario(IDU);
        }
        UsuarioActual = Funciones.UsuarioActual;
        Saludo.setText(UsuarioActual.getNombres() + " " + UsuarioActual.getApellidos());

        if (UsuarioActual.getTipo() == 1) {
            Titulo.setText("Camiones Registrados");
            CargarListaCamionesAgregados();
            Pendientes.show();
            if (!isMyServiceRunning(S_Notificador.class)) {
                //startService(new Intent(this, S_Notificador.class).putExtra("CONDUCTORID", UsuarioActual.getID()));
            }
        } else {
            Titulo.setText("Camiones Reservados");
            CargarListaReservas();
        }
    }

    private void CargarListaReservas() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        Lista.setLayoutManager(llm);

        Lista_Reservados lista = new Lista_Reservados(F_Usuario.TaaerReservasCliente(UsuarioActual.getID()));
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
        Lista.setAdapter(lista);
    }

    private void CargarListaCamionesAgregados() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        Lista.setLayoutManager(llm);

        Lista_Registrados lista = new Lista_Registrados(F_Vehiculo.TraerVehiculosConductor(UsuarioActual.getID()));
        if (lista.getItemCount() == 0) {
            O_Alerta alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Mis Camiones",
                    "Aún no tienes camiones registrados, comienza agregando uno con el boton en la parte inferior de la pantalla.",
                    false,
                    4500,
                    O_Alerta.RES_ICO_INFO
            );
            MostrarAlerta(alerta);
        }
        Lista.setAdapter(lista);
    }

    private void ConfigurarListeners() {
        Agregar.setOnClickListener(btn_Click());
        Pendientes.setOnClickListener(btn_Click());
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

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_principal_agregar:
                        if (UsuarioActual.getTipo() == 1) {
                            Intent destino = new Intent(C_Principal.this, C_AgregarVehiculo.class);
                            destino.putExtra("IDUSUARIO", UsuarioActual.getID());
                            startActivityForResult(destino, C_AgregarVehiculo.ActCode);
                        } else {
                            O_Reserva reserva = new O_Reserva();
                            reserva.setSolicitante(UsuarioActual);
                            Intent destino = new Intent(C_Principal.this, C_Mapa_DefinirRuta.class);
                            destino.putExtra("RESERVA", reserva);
                            startActivityForResult(destino, C_Mapa_DefinirRuta.ActCode);
                        }
                        break;
                    case R.id.fab_principal_pendientes:
                        if (UsuarioActual.getTipo() == 1) {
                            Intent destino = new Intent(C_Principal.this, C_ReservasPendientes.class);
                            destino.putExtra("USUARIO", UsuarioActual);
                            startActivityForResult(destino, C_ReservasPendientes.ActCode);
                        }
                        break;
                }
            }
        };
    }

    private void InicializarComponenetes() {
        Agregar = findViewById(R.id.fab_principal_agregar);
        Pendientes = findViewById(R.id.fab_principal_pendientes);

        Saludo = findViewById(R.id.tv_principal_saludo);
        Titulo = findViewById(R.id.tv_principal_titulo);

        Lista = findViewById(R.id.rv_principal_listavehiculos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent destino;
        switch (item.getItemId()) {
            case R.id.item_menu_cerrarsesion:
                Funciones.UsuarioActual = null;
                destino = new Intent(C_Principal.this, C_Login.class);
                stopService(new Intent(C_Principal.this, S_Notificador.class));
                startActivity(destino);
                finish();
                break;
            case R.id.item_menu_modificarperfil:
                destino = new Intent(C_Principal.this, C_ModificarPerfil.class);
                destino.putExtra("USUARIO", UsuarioActual);
                startActivityForResult(destino, C_ModificarPerfil.ActCode);
                break;
            case R.id.item_menu_cambiarclave:
                destino = new Intent(C_Principal.this, C_CambiarClave.class);
                destino.putExtra("USUARIO", UsuarioActual);
                startActivityForResult(destino, C_CambiarClave.ActCode);
                break;
            case R.id.item_menu_actualizarlista:
                if (UsuarioActual.getTipo() == 1) {
                    CargarListaCamionesAgregados();
                } else {
                    CargarListaReservas();
                }
                break;
        }
        return true;
    }

    class Lista_Registrados extends RecyclerView.Adapter<Lista_Registrados.VehicleViewHolder> {
        private ArrayList<O_Vehiculo> vehiculos;

        public Lista_Registrados(ArrayList<O_Vehiculo> vehiculos) {
            this.vehiculos = vehiculos;
        }

        @NonNull
        @Override
        public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_conductor, parent, false);
            VehicleViewHolder pvh = new VehicleViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position) {
            holder.asd.setTag(vehiculos.get(position));
            holder.asd.setOnClickListener(rv_ItemClick());
            holder.Patente.setText(vehiculos.get(position).getPatente());
            String estado = (vehiculos.get(position).getDisponibilidad() == 0) ? "No Disponible" : "Disponible";
            holder.Estado.setText("Estado: " + estado);
            holder.Tipo.setText("Tipo: " + vehiculos.get(position).getTipo());
        }

        @Override
        public int getItemCount() {
            return vehiculos.size();
        }

        public class VehicleViewHolder extends RecyclerView.ViewHolder {
            View asd;
            RecyclerView lista;
            TextView Patente, Estado, Tipo;

            VehicleViewHolder(View itemView) {
                super(itemView);
                asd = itemView;
                lista = itemView.findViewById(R.id.rv_principal_listavehiculos);
                Patente = itemView.findViewById(R.id.tv_listaconductor_patente);
                Estado = itemView.findViewById(R.id.tv_listadoconductor_estado);
                Tipo = itemView.findViewById(R.id.tv_listadoconductor_tipo);
            }
        }
    }

    class Lista_Reservados extends RecyclerView.Adapter<Lista_Reservados.VehicleViewHolder> {
        private ArrayList<O_Reserva> reservas;

        public Lista_Reservados(ArrayList<O_Reserva> reserva) {
            this.reservas = reserva;
        }

        @NonNull
        @Override
        public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_vehiculoscliente, parent, false);
            VehicleViewHolder pvh = new VehicleViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position) {
            holder.asd.setTag(reservas.get(position));
            holder.asd.setOnClickListener(rv_ItemClick());
            holder.Patente.setText(reservas.get(position).getVehiculo().getPatente());
            holder.Estado.setText("Estado Reserva: " + reservas.get(position).getEstado().getNombre());

            //O_Ruta ruta = Funciones.CalcularDistancia(reservas.get(position));
            //reservas.get(position).setRuta(ruta);
            String dueño = reservas.get(position).getVehiculo().getDueño().getNombres() + " " + reservas.get(position).getVehiculo().getDueño().getApellidos();
            holder.Dueño.setText("Dueño del vehículo: " + dueño);
            holder.Desde.setText("Dirección de Inicio: " + reservas.get(position).getDireccionInicio());
        }

        @Override
        public int getItemCount() {
            return reservas.size();
        }

        public class VehicleViewHolder extends RecyclerView.ViewHolder {
            View asd;
            RecyclerView lista;
            TextView Patente, Estado, Dueño, Desde;

            VehicleViewHolder(View itemView) {
                super(itemView);
                asd = itemView;
                lista = itemView.findViewById(R.id.rv_principal_listavehiculos);
                Patente = itemView.findViewById(R.id.tv_listavehiculoscliente_patente);
                Estado = itemView.findViewById(R.id.tv_listavehiculoscliente_estado);
                Dueño = itemView.findViewById(R.id.tv_listavehiculoscliente_dueño);
                Desde = itemView.findViewById(R.id.tv_listavehiculoscliente_desde);
            }
        }
    }

    private View.OnClickListener rv_ItemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UsuarioActual.getTipo() != 1) {
                    O_Reserva reserva = (O_Reserva) v.getTag();
                    Intent destino = new Intent(C_Principal.this, C_EstadoReserva.class);
                    Log.d("Llendo a:", "->" + C_EstadoReserva.class.getSimpleName());
                    destino.putExtra("USUARIO", UsuarioActual);
                    destino.putExtra("RESERVA", reserva);
                    startActivityForResult(destino, C_EstadoReserva.ActCode);
                    return;
                } else {
                    O_Vehiculo vehiculo = (O_Vehiculo) v.getTag();
                    Intent destino = new Intent(C_Principal.this, C_DetalleVehiculo.class);
                    Log.d("Llendo a:", "->" + C_DetalleVehiculo.class.getSimpleName());
                    destino.putExtra("VEHICULO", vehiculo);
                    startActivityForResult(destino, C_DetalleVehiculo.ActCode);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case C_AgregarVehiculo.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    CargarListaCamionesAgregados();
                    O_Alerta alerta = new O_Alerta(
                            O_Alerta.TIPO_CORRECTO,
                            "Agregar vehículo",
                            "Vehículo agregado correctamente, lista actualizada.",
                            false,
                            2500,
                            O_Alerta.RES_ICO_CORRECTO
                    );
                    MostrarAlerta(alerta);
                }
                break;
            case C_ModificarPerfil.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    boolean actualizar = data.getBooleanExtra("ACTUALIZAR", false);
                    O_Alerta alerta;
                    if (actualizar) {
                        Funciones.UsuarioActual = F_Usuario.TraerDatosUsuario(UsuarioActual.getID());
                        ConfigurarComponentes();
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_CORRECTO,
                                "Modificar Perfil",
                                "Datos modificados exitosamente.",
                                false,
                                2500,
                                O_Alerta.RES_ICO_CORRECTO
                        );
                    } else {
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_INFO,
                                "Modificar Perfil",
                                "Sin cambios.",
                                false,
                                2500,
                                O_Alerta.RES_ICO_INFO
                        );
                    }
                    MostrarAlerta(alerta);
                }
                break;
            case C_Mapa_DefinirRuta.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    O_Alerta alerta = new O_Alerta(
                            O_Alerta.TIPO_CORRECTO,
                            "Reservar Vehiculo",
                            "TU reservas está lista, te recomendamos seguir buscando a tus padres",
                            false,
                            2000,
                            O_Alerta.RES_ICO_CORRECTO
                    );
                    MostrarAlerta(alerta);
                    CargarListaReservas();
                }
                break;
            case C_CambiarClave.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    O_Alerta alerta = new O_Alerta(
                            O_Alerta.TIPO_CORRECTO,
                            "Cambio de clave",
                            "Clave cambiada correctamente",
                            false,
                            2000,
                            O_Alerta.RES_ICO_CORRECTO
                    );
                    MostrarAlerta(alerta);
                }
                break;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
