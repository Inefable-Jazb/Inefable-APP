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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
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

import java.util.ArrayList;
import java.util.Locale;

public class C_Mapa_DefinirRuta extends AppCompatActivity implements OnMapReadyCallback {

    public static final int ActCode = 12;
    private GoogleMap mMap;

    private Marker inicio, destino;

    private boolean configInicio = true, configDestino = false;

    private TextView Distancia;

    private FloatingActionButton FiltrarVehiculo;

    private AutoCompleteTextView DirInicio, DirDestino;

    private LinearLayout PanelInicio, PanelDestino;

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
        FiltrarVehiculo.setOnClickListener(btn_Click());

        ArrayList<String> lista = new ArrayList<>();
        lista.add("Uno");
        lista.add("Dos");
        lista.add("Tres");
        lista.add("Cuatro");
        lista.add("Cinco");
        lista.add("Seis");
        lista.add("Siete");
        lista.add("Ocho");
        lista.add("Nueve");
        lista.add("Diez");
        ArrayAdapter adaptador = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, lista);
        DirInicio.setAdapter(adaptador);
        DirInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                HabilitarDestino(!hasFocus);
            }
        });
        DirInicio.setOnEditorActionListener(actv_SearchListener());
        DirDestino.setOnEditorActionListener(actv_SearchListener());
    }

    private TextView.OnEditorActionListener actv_SearchListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String dir = v.getText().toString().trim();
                    Geocoder geo = new Geocoder(getApplicationContext());
                    LatLng coor = Funciones.ObtenerLatLngporDireccion(geo, dir);
                    if (coor != null) {
                        AgregarMarcador(coor);
                    } else {
                        O_Alerta alerta = new O_Alerta(
                                O_Alerta.TIPO_ERROR,
                                "Agregar Marcador",
                                "No se ha podido encontrar la dirección, revisar e intentar nuevamente.",
                                false,
                                1500,
                                O_Alerta.RES_ICO_ERROR
                        );
                        MostrarAlerta(alerta);
                    }
                    return true;
                }
                return false;
            }
        };
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                O_Alerta alerta;
                switch (v.getId()) {
                    case R.id.fab_mapa_definirruta_filtrarvehiculo:
                        if (inicio == null) {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Definir ruta",
                                    "Seleccionar punto de inicio.",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                            break;
                        } else if (destino == null) {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Definir ruta",
                                    "Seleccionar punto de destino.",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                            break;
                        }
                        O_Direccion dirInicio = AgregarDireccion(inicio);

                        O_Reserva reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
                        if (reserva == null) {
                            reserva = new O_Reserva();
                            reserva.setUsuario(Funciones.UsuarioActual);
                        }
                        reserva.setLatDes(destino.getPosition().latitude);
                        reserva.setLongDes(destino.getPosition().longitude);
                        reserva.setCalleInicio(DirInicio.getText().toString().trim());
                        reserva.setCalleDestino(DirDestino.getText().toString().trim());
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
        PanelInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirInicio.showDropDown();
                DirInicio.requestFocus();
            }
        });
    }

    private GoogleMap.OnMarkerClickListener marker_click() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                marker.showInfoWindow();
                return true;
            }
        };
    }

    private void InicializarComponentes() {
        Distancia = findViewById(R.id.tv_mapa_definirruta_distancia);

        DirInicio = findViewById(R.id.actv_mapa_definirruta_inicio);
        DirDestino = findViewById(R.id.actv_mapa_definirruta_destino);

        FiltrarVehiculo = findViewById(R.id.fab_mapa_definirruta_filtrarvehiculo);
        PanelInicio = findViewById(R.id.ll_mapa_definirruta_inicio);
        PanelDestino = findViewById(R.id.ll_mapa_definirruta_destino);
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
            mMap.setOnMarkerClickListener(marker_click());
        }
    }

    private void HabilitarDestino(boolean habilitado) {
        O_Alerta alerta;
        ImageButton inicio = findViewById(R.id.ib_mapa_definirruta_iconinicio);
        ImageButton destino = findViewById(R.id.ib_mapa_definirruta_icondestino);
        if (!habilitado) {
            alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Definir ruta",
                    "Selecciona el lugar donde el conductor deberá ir a buscar la carga O escribe la dirección en su lugar.",
                    false,
                    1500,
                    O_Alerta.RES_ICO_INFO
            );
            inicio.getDrawable().setTint(getColor(R.color.color_marker_start_default));
            destino.getDrawable().setTint(getColor(R.color.icon_disbled_color));
        } else {
            destino.getDrawable().setTint(getColor(R.color.color_marker_end_default));
            inicio.getDrawable().setTint(getColor(R.color.icon_disbled_color));
            alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Definir ruta",
                    "Ahora indicanos a donde deberá llegar el conductor o escribe la dirección en su lugar.",
                    false,
                    1500,
                    O_Alerta.RES_ICO_INFO
            );
        }
        configDestino = habilitado;
        configInicio = !habilitado;

        MostrarAlerta(alerta);
    }

    private GoogleMap.OnMapLongClickListener map_Click() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                AgregarMarcador(latLng);
            }
        };
    }

    private void AgregarMarcador(LatLng latLng) {
        MarkerOptions marker;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String[] direccion = Funciones.obtenerDireccion(geocoder, latLng);
        String dir = direccion[6] + " " + direccion[7] + ", " + direccion[1];

        if (configInicio) {
            if (inicio != null) {
                inicio.setPosition(latLng);
                String aux = DirInicio.getText().toString().trim();
                if (aux.equals("")) {
                    DirInicio.setText(dir);
                }
            } else {
                marker = new MarkerOptions();
                String aux = DirInicio.getText().toString().trim();
                if (aux.equals("")) {
                    DirInicio.setText(dir);
                }
                marker.title("Inicio");
                marker.position(latLng);
                inicio = mMap.addMarker(marker);
                inicio.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        } else if (configDestino) {
            if (destino != null) {
                destino.setPosition(latLng);
                String aux = DirDestino.getText().toString().trim();
                if (aux.equals("")) {
                    DirDestino.setText(dir);
                }
            } else {
                marker = new MarkerOptions();
                String aux = DirDestino.getText().toString().trim();
                if (aux.equals("")) {
                    DirDestino.setText(dir);
                }
                marker.title("Destino");
                marker.position(latLng);
                destino = mMap.addMarker(marker);
                destino.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
        if (inicio != null && destino != null) {
            O_Direccion oDireccion = AgregarDireccion(inicio);
            O_Reserva reserva = new O_Reserva();
            reserva.setInicio(oDireccion);
            reserva.setLongDes(destino.getPosition().longitude);
            reserva.setLatDes(destino.getPosition().latitude);
            O_Ruta ruta = Funciones.CalcularDistancia(reserva);
            if (ruta == null) {
                O_Alerta alerta = new O_Alerta(
                        O_Alerta.TIPO_ERROR,
                        "Calcular distancia",
                        "No hay una ruta por tierra disponible, seleccionar otra ubicación.",
                        false,
                        2000,
                        O_Alerta.RES_ICO_ERROR
                );
                MostrarAlerta(alerta);
                return;
            }
            Distancia.setText("Distancia: " + ruta.getDistanciaAprox() + " (" + ruta.getDistanciaReal() + " m)");
            Distancia.setVisibility(View.VISIBLE);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(inicio.getPosition());
            builder.include(destino.getPosition());
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
            mMap.animateCamera(cu);
        }
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
                    Toast.makeText(this, "Autorizar permisos par acceder a la funcion reserva.", Toast.LENGTH_SHORT).show();
                    finish();
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
