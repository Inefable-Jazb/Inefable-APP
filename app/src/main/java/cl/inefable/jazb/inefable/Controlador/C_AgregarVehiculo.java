package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cl.inefable.jazb.inefable.Modelo.DATA.O_TipoVehiculo;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;

public class C_AgregarVehiculo extends AppCompatActivity {
    public static final int ActCode = 777;
    private int Usuario;

    private TextInputLayout Patente, Marca, Altura, Largo, Ancho, CargaMax, Rendimiento;

    private TextView Volumen;

    private Spinner Tipo;

    private Button Guardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_agregarvehiculo);

        InicializarComponenetes();
        ConfigurarComponenetes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Patente.getEditText().setOnFocusChangeListener(et_FocusChange("patente"));
        Marca.getEditText().setOnFocusChangeListener(et_FocusChange("marca"));
        Altura.getEditText().setOnFocusChangeListener(et_FocusChange("dims"));
        Ancho.getEditText().setOnFocusChangeListener(et_FocusChange("dims"));
        Largo.getEditText().setOnFocusChangeListener(et_FocusChange("dims"));
        CargaMax.getEditText().setOnFocusChangeListener(et_FocusChange("cargamax"));
        Rendimiento.getEditText().setOnFocusChangeListener(et_FocusChange("rendimiento"));

        Guardar.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_agregarvehiculo_guardar:
                        String patente, marca, altura, ancho, largo, cargamax, rendimiento;
                        patente = Patente.getEditText().getText().toString().trim();
                        marca = Marca.getEditText().getText().toString().trim();
                        altura = Altura.getEditText().getText().toString().trim();
                        ancho = Ancho.getEditText().getText().toString().trim();
                        largo = Largo.getEditText().getText().toString().trim();
                        cargamax = CargaMax.getEditText().getText().toString().trim();
                        rendimiento = Rendimiento.getEditText().getText().toString().trim();

                        O_TipoVehiculo tipo = (O_TipoVehiculo) Tipo.getSelectedItem();

                        if (ValidarPatente() && ValidarDimensiones() && ValidarMarca() && cargamax.length() > 0 && rendimiento.length() > 0) {
                            O_Alerta alerta;
                            O_Vehiculo vehiculo = new O_Vehiculo(
                                    patente,
                                    marca,
                                    Double.parseDouble(altura),
                                    Double.parseDouble(largo),
                                    Double.parseDouble(ancho),
                                    Integer.parseInt(cargamax),
                                    tipo.getID() + "",
                                    Integer.parseInt(rendimiento),
                                    Usuario
                            );
                            if (F_Vehiculo.Crear(vehiculo) == 1) {
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Error de Creación",
                                        "Hubo un error al crear el vehículo, intente denuevo mas tarde.",
                                        false,
                                        1500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            }
                        } else {
                            O_Alerta alerta;
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_PRECAUCION,
                                    "Agregar Vehículo",
                                    "Corregir todos los campos",
                                    false,
                                    2500,
                                    O_Alerta.RES_ICO_PRECAUCION
                            );
                            MostrarAlerta(alerta);
                        }
                        break;
                }
            }
        };
    }

    private View.OnFocusChangeListener et_FocusChange(final String op) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText e = (EditText) v;
                    String valor = e.getText().toString().trim();
                    if (!valor.equals("")) {
                        switch (op) {
                            case "patente":
                                ValidarPatente();
                                break;
                            case "marca":
                                ValidarMarca();
                                break;
                            case "dims":
                                ValidarDimensiones();
                                break;
                            case "cargamax":

                                break;
                            case "rendimiento":

                                break;
                        }
                    }
                }
            }
        };
    }

    private boolean ValidarPatente() {
        String valor = Patente.getEditText().getText().toString().trim();
        O_Alerta alerta;
        String mensaje;

        if (!Funciones.ValidarPatente(valor)) {
            mensaje = "Patente no válida.";
            alerta = new O_Alerta(
                    O_Alerta.TIPO_ERROR,
                    "Validación de Patente",
                    mensaje,
                    false,
                    3500,
                    O_Alerta.RES_ICO_ERROR
            );
            Patente.setErrorEnabled(true);
            Patente.setError(mensaje);
        } else {
            Patente.setErrorEnabled(false);

            mensaje = "Patente válidada correctamente.";
            alerta = new O_Alerta(
                    O_Alerta.TIPO_CORRECTO,
                    "Validación de Patente",
                    mensaje,
                    false,
                    3500,
                    O_Alerta.RES_ICO_CORRECTO
            );
        }
        MostrarAlerta(alerta);
        return !Patente.isErrorEnabled();
    }

    private boolean ValidarMarca() {
        O_Alerta alerta;
        String mensaje;
        String valor = Marca.getEditText().getText().toString().trim();

        if (!Funciones.ValidarMarca(valor)) {
            mensaje = "Marca debe contener al menos 2 caracteres de la 'a' a la 'z'.";
            alerta = new O_Alerta(
                    O_Alerta.TIPO_ERROR,
                    "Validación de Marca",
                    mensaje,
                    false,
                    3500,
                    O_Alerta.RES_ICO_ERROR
            );
            Marca.setErrorEnabled(true);
            Marca.setError(mensaje);
        } else {
            Marca.setErrorEnabled(false);

            mensaje = "Marca validada correctamente.";
            alerta = new O_Alerta(
                    O_Alerta.TIPO_CORRECTO,
                    "Validación de Marca",
                    mensaje,
                    false,
                    3500,
                    O_Alerta.RES_ICO_CORRECTO
            );
        }
        MostrarAlerta(alerta);
        return !Marca.isErrorEnabled();
    }

    private boolean ValidarDimensiones() {
        int al, an, la;
        String Al, An, La;
        O_Alerta alerta;

        Al = Altura.getEditText().getText().toString().trim();
        An = Ancho.getEditText().getText().toString().trim();
        La = Largo.getEditText().getText().toString().trim();
        if (Al.length() != 0 &&
                An.length() != 0 &&
                La.length() != 0) {
            try {
                al = Integer.parseInt(Al);
                an = Integer.parseInt(An);
                la = Integer.parseInt(La);
                if (al != 0 && an != 0 & la != 0) {
                    int total = (al * an * la);
                    double meterscubics = Math.pow(100, 3);
                    double dims;
                    if (total >= meterscubics) {
                        dims = total / meterscubics;
                        Volumen.setText(dims + " m³");
                    } else {
                        dims = total;
                        Volumen.setText(dims + " cm³");
                    }
                    return true;
                } else {
                    alerta = new O_Alerta(
                            O_Alerta.TIPO_INFO,
                            "Validación Dimensiones Vehículo",
                            "Los valores deben ser superiores a 0.",
                            false,
                            1000,
                            O_Alerta.RES_ICO_INFO
                    );
                }
            } catch (NumberFormatException e) {
                alerta = new O_Alerta(
                        O_Alerta.TIPO_PRECAUCION,
                        "Validación Dimensiones Vehículo",
                        "Solo se pueden ingresar números.",
                        false,
                        1000,
                        O_Alerta.RES_ICO_PRECAUCION
                );
            }
        } else {
            alerta = new O_Alerta(
                    O_Alerta.TIPO_INFO,
                    "Validación Dimensiones Vehículo",
                    "Debe completar los 3 campos para calcular el volumen.",
                    false,
                    1000,
                    O_Alerta.RES_ICO_INFO
            );
        }
        MostrarAlerta(alerta);
        return false;
    }

    private void ConfigurarComponenetes() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Usuario = getIntent().getIntExtra("IDUSUARIO", -1);

        ArrayList<O_TipoVehiculo> listaTipos = F_Vehiculo.TraerListaTipoCamiones();

        ArrayAdapter adaptador = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listaTipos);
        Tipo.setAdapter(adaptador);

        Patente.setCounterEnabled(true);
        Patente.setCounterMaxLength(7);
    }

    private void InicializarComponenetes() {
        Patente = findViewById(R.id.til_agregarvehiculo_patente);
        Marca = findViewById(R.id.til_agregarvehiculo_marca);
        Altura = findViewById(R.id.til_agregarvehiculo_altura);
        Largo = findViewById(R.id.til_agregarvehiculo_largo);
        Ancho = findViewById(R.id.til_agregarvehiculo_ancho);
        CargaMax = findViewById(R.id.til_agregarvehiculo_cargamax);
        Rendimiento = findViewById(R.id.til_agregarvehiculo_rendimiento);

        Volumen = findViewById(R.id.tv_agregarvehiculo_volumen);

        Tipo = findViewById(R.id.spn_agregarvehiculo_tipovehiculo);

        Guardar = findViewById(R.id.btn_agregarvehiculo_guardar);
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
}
