package cl.inefable.jazb.inefable.Controlador;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.tapadoo.alerter.Alerter;

public class C_EstadoReserva extends AppCompatActivity {
    public static final int ActCode = 7854;
    private O_Reserva Reserva;

    private O_Usuario Usuario;

    private Button Cancelar, Monitorear, Valorar;

    private TextView Patente, Propietario, Inicio, Destino, Distancia, CostoTotal, TiempoEstimado, Estado;

    private GoogleMap Mapa;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_estadoreserva);
        InicializarComponentes();
        ConfigurarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Cancelar.setOnClickListener(btn_Click());
        Monitorear.setOnClickListener(btn_Click());
        Valorar.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_estadoreservacliente_cancelar:
                        if (F_Usuario.CancelarReserva(Reserva) <= 0) {
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Gestión de Reservas",
                                    "Hubo un error al cancelar la reserva, intentar nuevamente.",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            finish();
                        }
                        break;
                    case R.id.btn_estadoreservacliente_monitorear:
                        HabilitarMapa();
                        break;
                    case R.id.btn_estadoreservacliente_valorar:
                        Intent intent = new Intent(C_EstadoReserva.this, C_ValorarServicio.class);
                        intent.putExtra("USUARIO", Usuario);
                        intent.putExtra("RESERVA", Reserva);
                        startActivityForResult(intent, C_ValorarServicio.ActCode);
                        break;
                }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    private void HabilitarMapa() {
        mapFragment.getView().setVisibility(View.VISIBLE);
    }

    private void ConfigurarComponentes() {
        Usuario = (O_Usuario) getIntent().getSerializableExtra("USUARIO");
        Reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_estadoreservacliente_ruta);
        mapFragment.getMapAsync(map_ready());
        mapFragment.getView().setVisibility(View.GONE);
        Patente.setText("Patente Vehículo: " + Reserva.getVehiculo().getPatente());
        String dueño = Reserva.getVehiculo().getDueño().getNombres() + " " + Reserva.getVehiculo().getDueño().getApellidos();
        Propietario.setText("Dueño vehículo: " + dueño);
        Inicio.setText("Dirección Inicio: " + Reserva.getDireccionInicio());
        Destino.setText("Dirección Destino: " + Reserva.getDireccionDestino());
        Distancia.setText("Distancia aprox.: " + ((float) Reserva.getDistancia() / 1000) + " Km.");
        int horas, min, seg;
        seg = Reserva.getDuracion();
        horas = seg / (60 * 60);
        seg = seg % (60 * 60);
        min = seg / 60;
        seg -= min * 60;
        TiempoEstimado.setText("Tiempo estimado: " + horas + " hora(s) y " + min + " minuto(s) y " + seg + " segundo(s) aprox.");
        Estado.setText("Estado de la Reserva: " + Reserva.getEstado().getNombre());
        CostoTotal.setText("Costo total sugerido: $" + Reserva.getValorTotal());
        HabilitarControles(Reserva.getEstado().getID());
    }

    private void HabilitarControles(int estado) {
        switch (estado) {
            case 1:
                Cancelar.setVisibility(View.GONE);
                Monitorear.setVisibility(View.GONE);
                Valorar.setVisibility(View.VISIBLE);
                break;
            case 2:
                Cancelar.setVisibility(View.VISIBLE);
                Monitorear.setVisibility(View.GONE);
                Valorar.setVisibility(View.GONE);
                break;
            case 3:
                Cancelar.setVisibility(View.GONE);
                Monitorear.setVisibility(View.GONE);
                Valorar.setVisibility(View.GONE);
                break;
            case 4:
                Cancelar.setVisibility(View.GONE);
                Monitorear.setVisibility(View.GONE);
                Valorar.setVisibility(View.GONE);
                break;
            case 5:
                Cancelar.setVisibility(View.GONE);
                Monitorear.setVisibility(View.GONE);
                Valorar.setVisibility(View.GONE);
                break;
            case 6:
                Cancelar.setVisibility(View.GONE);
                Monitorear.setVisibility(View.VISIBLE);
                Valorar.setVisibility(View.GONE);
                break;
        }

        if (F_Usuario.ReservaValorada(Reserva) == 1) {
            Cancelar.setVisibility(View.GONE);
            Monitorear.setVisibility(View.GONE);
            Valorar.setVisibility(View.GONE);
        }
    }

    private OnMapReadyCallback map_ready() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                Mapa = googleMap;
                if (ActivityCompat.checkSelfPermission(C_EstadoReserva.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(C_EstadoReserva.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(C_EstadoReserva.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                } else {
                    Mapa.setMyLocationEnabled(true);
                    Mapa.getUiSettings().setMyLocationButtonEnabled(true);
                    Mapa.setBuildingsEnabled(true);
                    Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Mapa.getUiSettings().setMapToolbarEnabled(false);
                    final FusedLocationProviderClient flpc = new FusedLocationProviderClient(C_EstadoReserva.this);
                    flpc.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            double lat = task.getResult().getLatitude();
                            double lon = task.getResult().getLongitude();

                            LatLng actual = new LatLng(lat, lon);
                            Mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 16));
                        }
                    });
                    flpc.getLastLocation().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                    Mapa.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            AgrearPuntos();
                        }
                    });
                }
            }
        };
    }

    private void AgrearPuntos() {
        LatLng inicio, destino;
        MarkerOptions INICIO, DESTINO;
        Marker Inicio, Destino;

        inicio = new LatLng(Reserva.getLatInicio(), Reserva.getLongInicio());
        INICIO = new MarkerOptions();
        INICIO.title("Inicio - " + Reserva.getDireccionInicio());
        INICIO.position(inicio);

        Inicio = Mapa.addMarker(INICIO);
        Inicio.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        /*
        DESTINO
         */

        destino = new LatLng(Reserva.getLatDestino(), Reserva.getLongDestino());
        DESTINO = new MarkerOptions();
        DESTINO.title("Destino - " + Reserva.getDireccionDestino());
        DESTINO.position(destino);
        Destino = Mapa.addMarker(DESTINO);
        Destino.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(inicio);
        builder.include(destino);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        Mapa.animateCamera(cu);
    }

    private void InicializarComponentes() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Patente = findViewById(R.id.tv_estadoreservacliente_patente);
        Propietario = findViewById(R.id.tv_estadoreservacliente_dueño);
        Inicio = findViewById(R.id.tv_estadoreservacliente_direccioninicio);
        Destino = findViewById(R.id.tv_estadoreservacliente_direcciondestino);
        Distancia = findViewById(R.id.tv_estadoreservacliente_distancia);
        CostoTotal = findViewById(R.id.tv_estadoreservacliente_costototal);
        Estado = findViewById(R.id.tv_estadoreservacliente_estado);
        TiempoEstimado = findViewById(R.id.tv_estadoreservacliente_tiempoestimado);

        Cancelar = findViewById(R.id.btn_estadoreservacliente_cancelar);
        Monitorear = findViewById(R.id.btn_estadoreservacliente_monitorear);
        Valorar = findViewById(R.id.btn_estadoreservacliente_valorar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C_ValorarServicio.ActCode) {
            if (resultCode == Activity.RESULT_OK) {
                O_Alerta alerta = new O_Alerta(
                        O_Alerta.TIPO_CORRECTO,
                        "Valorar Reserva",
                        "Valoración guardada exitósamente.",
                        false,
                        1500,
                        O_Alerta.RES_ICO_CORRECTO
                );
                MostrarAlerta(alerta);
            }
        }
    }
}
