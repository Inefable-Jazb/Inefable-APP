package cl.inefable.jazb.inefable.Controlador;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.R;

public class C_DetalleVehiculo extends AppCompatActivity {

    private O_Vehiculo vehiculo;
    public final static int ActCode = 112;

    private TextView Patente, Marca, Volumen, CargaMax, Tipo, Rendimiento, Propietario;

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

        double volumen = (vehiculo.getAltura() * vehiculo.getAncho() * vehiculo.getLargo()) / 100;

        Patente.setText("Patente: " + vehiculo.getPatente());
        Marca.setText("Marca: " + vehiculo.getMarca());
        Volumen.setText("Volumen: " + volumen + " m³");
        CargaMax.setText("Carga Máx .: " + vehiculo.getCargaMax() + " Kg");
        Tipo.setText("Marca: " + vehiculo.getTipo());
        Rendimiento.setText("Rendimiento: " + vehiculo.getValor() + " Km/L");
        if (vehiculo.getDueño() != null) {
            Propietario.setText("Propietario: " + vehiculo.getDueño().getNombres() + " " + vehiculo.getDueño().getApellidos());

        } else {
            Propietario.setText("Propietario: " + vehiculo.getPropietario());
        }
    }

    private void InicializarComponenetes() {
        Patente = findViewById(R.id.tv_detallevehiculo_patente);
        Marca = findViewById(R.id.tv_detallevehiculo_marca);
        Volumen = findViewById(R.id.tv_detallevehiculo_volumen);
        CargaMax = findViewById(R.id.tv_detallevehiculo_cargamax);
        Tipo = findViewById(R.id.tv_detallevehiculo_tipo);
        Rendimiento = findViewById(R.id.tv_detallevehiculo_rendimiento);
        Propietario = findViewById(R.id.tv_detallevehiculo_propietario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_detallevehiculo, menu);
        return true;
    }

}
