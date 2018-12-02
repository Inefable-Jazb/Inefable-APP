package cl.inefable.jazb.inefable.Controlador;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Direccion;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Ruta;
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

public class C_Mapa_DefinirRuta extends AppCompatActivity implements OnMapReadyCallback {

    public static final int ActCode = 12;
    private GoogleMap mMap;

    private Marker inicio, destino;

    private boolean configInicio = true, configDestino = false;

    private TextView Inicio, Destino, Distancia;

    private Button ConfirmarInicio, ConfirmarDestino;

    private FloatingActionButton FiltrarVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mapa_definirruta);

        InicializarComponentes();
        ConfigurarComponenetes();
        ConfigurarListeners();
        ConfigurarMapa();
    }

    private void ConfigurarListeners() {
        ConfirmarInicio.setOnClickListener(btn_Click());
        ConfirmarDestino.setOnClickListener(btn_Click());
        FiltrarVehiculo.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                O_Alerta alerta;
                switch (v.getId()) {
                    case R.id.btn_mapa_definirruta_confirmarinicio:
                        if (inicio == null) {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Definir ruta",
                                    "Seleccione la ubicación de inicio de la ruta.",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        } else {
                            HabilitarDestino(true);
                        }
                        FiltrarVehiculo.setVisibility(View.GONE);
                        break;
                    case R.id.btn_mapa_definirruta_confirmardestino:
                        if (destino == null) {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Definir ruta",
                                    "Seleccione la ubicación de destino de la ruta.",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            FiltrarVehiculo.setVisibility(View.GONE);
                        } else {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_CORRECTO,
                                    "Definir ruta",
                                    "Ruta definida con exito",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_CORRECTO
                            );
                            FiltrarVehiculo.setVisibility(View.VISIBLE);
                        }
                        MostrarAlerta(alerta);
                        break;
                    case R.id.fab_mapa_definirruta_filtrarvehiculo:
                        O_Direccion dirInicio = AgregarDireccion(inicio);

                        O_Reserva reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
                        if (reserva == null) {
                            reserva = new O_Reserva();
                            reserva.setUsuario(Funciones.UsuarioActual);
                        }
                        reserva.setLatDes(destino.getPosition().latitude);
                        reserva.setLongDes(destino.getPosition().longitude);
                        reserva.setCalleInicio(Inicio.getText().toString());
                        reserva.setCalleDestino(Destino.getText().toString());
                        reserva.setInicio(dirInicio);
                        O_Ruta ruta = Funciones.CalcularDistancia(reserva);
                        reserva.setRuta(ruta);
                        Intent actDestino = new Intent(C_Mapa_DefinirRuta.this, C_FiltrarVehículos.class);
                        actDestino.putExtra("RESERVA", reserva);
                        startActivityForResult(actDestino, C_FiltrarVehículos.ActCode);
                        break;
                }
            }
        };
    }

    private O_Direccion AgregarDireccion(Marker posicion) {
        O_Direccion direccion = new O_Direccion();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String[] dirDATA = Funciones.obtenerDireccion(geocoder, posicion.getPosition());
        direccion.setCalle(dirDATA[6]);
        int num;
        try {
            num = Integer.parseInt(dirDATA[7]);
        } catch (Exception e) {
            num = 0;
        }
        direccion.setNumero(num);
        direccion.setCiudad(dirDATA[1]);
        direccion.setLatitud(posicion.getPosition().latitude);
        direccion.setLongitud(posicion.getPosition().longitude);
        return direccion;
    }

    private void ConfigurarComponenetes() {
        HabilitarDestino(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void InicializarComponentes() {
        Inicio = findViewById(R.id.tv_mapa_definirruta_inicio);
        Destino = findViewById(R.id.tv_mapa_definirruta_destino);
        Distancia = findViewById(R.id.tv_mapa_definirruta_distancia);

        ConfirmarInicio = findViewById(R.id.btn_mapa_definirruta_confirmarinicio);
        ConfirmarDestino = findViewById(R.id.btn_mapa_definirruta_confirmardestino);

        FiltrarVehiculo = findViewById(R.id.fab_mapa_definirruta_filtrarvehiculo);
    }

    private void ConfigurarMapa() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_mapa_definirruta_mapa);
        mapFragment.getMapAsync(this);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setOnMapLongClickListener(map_Click());
            //mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            final FusedLocationProviderClient flpc = new FusedLocationProviderClient(this);
            flpc.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    double lat = task.getResult().getLatitude();
                    double lon = task.getResult().getLongitude();

                    LatLng actual = new LatLng(lat, lon);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 16));
                }
            });
            flpc.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
            flpc.getLastLocation().addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(C_Mapa_DefinirRuta.this, "Cancelado", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void HabilitarDestino(boolean habilitado) {
        O_Alerta alerta;
        if (!habilitado) {
            alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Definir ruta",
                    "Selecciona el lugar donde el conductor deberá ir a buscar la carga.",
                    false,
                    2000,
                    O_Alerta.RES_ICO_INFO
            );
        } else {
            alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Definir ruta",
                    "Ahora indicanos a donde deberá llegar el conductor.",
                    false,
                    2000,
                    O_Alerta.RES_ICO_INFO
            );
        }
        ConfirmarDestino.setEnabled(habilitado);
        configDestino = habilitado;

        ConfirmarInicio.setEnabled(!habilitado);
        configInicio = !habilitado;

        MostrarAlerta(alerta);
    }

    private GoogleMap.OnMapLongClickListener map_Click() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                MarkerOptions marker;
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String[] direccion = Funciones.obtenerDireccion(geocoder, latLng);
                String dir = direccion[6] + " " + direccion[7] + ", " + direccion[1];

                if (configInicio) {
                    if (inicio != null) {
                        //inicio.remove();
                        inicio.setPosition(latLng);
                        Inicio.setText(dir);
                    } else {
                        marker = new MarkerOptions();
                        Inicio.setText(dir);
                        marker.title("Inicio");
                        marker.position(latLng);
                        inicio = mMap.addMarker(marker);
                        inicio.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                } else if (configDestino) {
                    if (destino != null) {
                        //inicio.remove();
                        destino.setPosition(latLng);
                        Destino.setText(dir);
                    } else {
                        marker = new MarkerOptions();
                        Destino.setText(dir);
                        marker.title("Destino");
                        marker.position(latLng);
                        destino = mMap.addMarker(marker);
                        destino.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    O_Direccion oDireccion = AgregarDireccion(inicio);
                    O_Reserva reserva = new O_Reserva();
                    reserva.setInicio(oDireccion);
                    reserva.setLongDes(destino.getPosition().longitude);
                    reserva.setLatDes(destino.getPosition().latitude);
                    O_Ruta ruta = Funciones.CalcularDistancia(reserva);
                    Distancia.setText("Distancia: " + ruta.getDistanciaAprox() + " (" + ruta.getDistanciaReal() + " m)");
                    Distancia.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ConfigurarMapa();
                } else {
                    Toast.makeText(this, "No me dieron permiso ggggg.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case C_FiltrarVehículos.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK, data);
                    finish();
                    return;
                }
                break;
        }
    }
}
