package cl.inefable.jazb.inefable.Controlador;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
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
import android.widget.Toast;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.tapadoo.alerter.Alerter;

import java.util.Locale;
import java.util.regex.Pattern;

public class C_DetalleReservaConductor extends AppCompatActivity implements OnMapReadyCallback {
    public static final int ActCode = 4789;

    private TextView Solicitante, Patente, DireccionInicio, DireccionDestino, Distancia, TiempoEstimado, CostoTotal;
    private Button Aceptar, Rechazar, Comenzar, Finalizar;
    private GoogleMap Mapa;

    private O_Reserva Reserva;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detallereservaconductor);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_detallereservaconductor_ruta);
        mapFragment.getMapAsync(this);

        InicilizarComponentes();
        ConfigurarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Aceptar.setOnClickListener(btn_Click());
        Rechazar.setOnClickListener(btn_Click());
        Finalizar.setOnClickListener(btn_Click());
        Comenzar.setOnClickListener(btn_Click());
    }

    private void HabilitarBotones(String estado) {
        switch (estado) {
            case "Completado":
                Aceptar.setVisibility(View.GONE);
                Rechazar.setVisibility(View.GONE);
                Comenzar.setVisibility(View.GONE);
                Finalizar.setVisibility(View.GONE);
                break;
            case "En Espera":
                Aceptar.setVisibility(View.VISIBLE);
                Rechazar.setVisibility(View.VISIBLE);
                Comenzar.setVisibility(View.GONE);
                Finalizar.setVisibility(View.GONE);
                break;
            case "Cancelado":
                Aceptar.setVisibility(View.GONE);
                Rechazar.setVisibility(View.GONE);
                Comenzar.setVisibility(View.GONE);
                Finalizar.setVisibility(View.GONE);
                break;
            case "Rechazado":
                Aceptar.setVisibility(View.GONE);
                Rechazar.setVisibility(View.GONE);
                Comenzar.setVisibility(View.GONE);
                Finalizar.setVisibility(View.GONE);
                break;
            case "Aceptado":
                Aceptar.setVisibility(View.GONE);
                Rechazar.setVisibility(View.GONE);
                Comenzar.setVisibility(View.VISIBLE);
                Finalizar.setVisibility(View.GONE);
                break;
            case "En Curso":
                Aceptar.setVisibility(View.GONE);
                Rechazar.setVisibility(View.GONE);
                Comenzar.setVisibility(View.GONE);
                Finalizar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("RESERVA", Reserva);
                switch (view.getId()) {
                    case R.id.btn_detallereservaconductor_aceptar:
                        if (F_Usuario.AceptarReserva(Reserva) == 0) {
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Gestión de Reservas",
                                    "Hubo un error al aceptar la reserva, intentar nuevamente.",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        break;
                    case R.id.btn_detallereservaconductor_rechazar:
                        if (F_Usuario.RechazarReserva(Reserva) == 0) {
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Gestión de Reservas",
                                    "Hubo un error al rechazar la reserva, intentar nuevamente.",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            backCancel();
                        }
                        break;
                    case R.id.btn_detallereservaconductor_comenzar:
                        if (F_Usuario.ComenzarReserva(Reserva) == 0) {
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Gestión de Reservas",
                                    "Hubo un error al comenzar la reserva, intentar nuevamente.",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        break;
                    case R.id.btn_detallereservaconductor_finalizar:
                        if (F_Usuario.FinalizarReserva(Reserva) == 0) {
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Gestión de Reservas",
                                    "Hubo un error al finalizar la reserva, intentar nuevamente.",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        break;
                }
            }
        };
    }

    private void backCancel() {
        Intent intent = new Intent();
        intent.putExtra("RESERVA", Reserva);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backCancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res;
        switch (item.getItemId()) {
            case android.R.id.home:
                backCancel();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
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

    private void ConfigurarComponentes() {
        String solicitante = Reserva.getSolicitante().getNombres() + " " + Reserva.getSolicitante().getApellidos();
        HabilitarBotones(Reserva.getEstado().getNombre());
        Solicitante.setText("Solicitante: " + solicitante);
        Patente.setText("Vehículo: " + Reserva.getVehiculo().getPatente() + ", " + Reserva.getVehiculo().getMarca() + ".");
        DireccionInicio.setText("Inicio: " + Reserva.getDireccionInicio());
        DireccionDestino.setText("Destino: " + Reserva.getDireccionDestino());
        Distancia.setText("Distancia aprox.: " + ((float) Reserva.getDistancia() / 1000) + " Km.");
        int horas, min, seg;
        seg = Reserva.getDuracion();
        horas = seg / (60 * 60);
        seg = seg % (60 * 60);
        min = seg / 60;
        seg -= min * 60;
        TiempoEstimado.setText("Tiempo estimado: " + horas + " hora(s) y " + min + " minuto(s) y " + seg + " segundo(s) aprox.");
        CostoTotal.setText("Costo total sugerido: $" + Reserva.getValorTotal());
    }

    private void InicilizarComponentes() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Solicitante = findViewById(R.id.tv_detallereservaconductor_solicitante);
        Patente = findViewById(R.id.tv_detallereservaconductor_patente);
        DireccionInicio = findViewById(R.id.tv_detallereservaconductor_direccioninicio);
        DireccionDestino = findViewById(R.id.tv_detallereservaconductor_direciondestino);
        Distancia = findViewById(R.id.tv_detallereservaconductor_distancia);
        TiempoEstimado = findViewById(R.id.tv_detallereservaconductor_duracion);
        CostoTotal = findViewById(R.id.tv_detallereservaconductor_costototal);

        Aceptar = findViewById(R.id.btn_detallereservaconductor_aceptar);
        Rechazar = findViewById(R.id.btn_detallereservaconductor_rechazar);
        Comenzar = findViewById(R.id.btn_detallereservaconductor_comenzar);
        Finalizar = findViewById(R.id.btn_detallereservaconductor_finalizar);

        Reserva = (O_Reserva) getIntent().getExtras().getSerializable("RESERVA");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Mapa.setMyLocationEnabled(true);
            Mapa.getUiSettings().setMyLocationButtonEnabled(true);
            Mapa.setBuildingsEnabled(true);
            Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Mapa.getUiSettings().setMapToolbarEnabled(false);
            final FusedLocationProviderClient flpc = new FusedLocationProviderClient(this);
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
}
