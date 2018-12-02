package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Pais;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Pais;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class C_ModificarPerfil extends AppCompatActivity {
    public static final int ActCode = 9;

    private O_Usuario Usuario;

    private TextInputLayout Nombres, Apellidos, Correo, Teléfono;
    private Spinner Pais;

    private TextView Prefijo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_modificarperfil);

        InicializarComponenetes();
        ConfigurarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Nombres.getEditText().setOnFocusChangeListener(et_Focus(0));
        Apellidos.getEditText().setOnFocusChangeListener(et_Focus(1));
        Correo.getEditText().setOnFocusChangeListener(et_Focus(2));
        Teléfono.getEditText().setOnFocusChangeListener(et_Focus(3));
        Pais.setOnItemSelectedListener(spn_ItemSelected());
    }

    private View.OnFocusChangeListener et_Focus(final int comp) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switch (comp) {
                    case 0:
                        if (!hasFocus) {
                            EditText aux = (EditText) v;
                            String valor = aux.getText().toString().trim();
                            if (!Funciones.ValidarNombres(valor)) {
                                String error = "Cada nombre debe contener al menos 2 caracteres.";
                                Nombres.setErrorEnabled(true);
                                Nombres.setError(error);
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Validación de nombres",
                                        error,
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            } else {
                                Nombres.setErrorEnabled(false);
                            }
                        }
                        break;
                    case 1:
                        if (!hasFocus) {
                            EditText aux = (EditText) v;
                            String valor = aux.getText().toString().trim();
                            int res = Funciones.ValidarApellidos(valor);
                            if (res == 0) {
                                Apellidos.setErrorEnabled(false);
                            } else if (res == -1) {
                                String error = "Debes escribir al menos 2 apellidos.";
                                Apellidos.setErrorEnabled(true);
                                Apellidos.setError(error);
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Validación de nombres",
                                        error,
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            } else if (res == -2) {
                                String error = "Cada apellido debe contener al menos 2 caracteres.";
                                Apellidos.setErrorEnabled(true);
                                Apellidos.setError(error);
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Validación de apellidos",
                                        error,
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            }
                        }
                        break;
                    case 2:
                        if (!hasFocus) {
                            EditText aux = (EditText) v;
                            String valor = aux.getText().toString().trim();
                            if (!Funciones.ValidarCorreo(valor)) {
                                String error = "Formato de correo no válido.";
                                Correo.setErrorEnabled(true);
                                Correo.setError(error);
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Validación de correo",
                                        error,
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            } else {
                                Correo.setErrorEnabled(false);
                            }
                        }
                        break;
                    case 3:
                        if (!hasFocus) {
                            EditText aux = (EditText) v;
                            String valor = aux.getText().toString().trim();
                            if (!Funciones.ValidarTelefono(valor)) {
                                String error = "Formato de Teléfono incorrecto.";
                                Teléfono.setErrorEnabled(true);
                                Teléfono.setError(error);
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Validación de teléfono",
                                        error,
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            } else {
                                Teléfono.setErrorEnabled(false);
                            }
                        }
                        break;
                }
            }
        };
    }

    private void ConfigurarComponentes() {
        ArrayList<O_Pais> paises = F_Pais.TraerListaPaises();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, paises);
        Pais.setAdapter(adapter);
        Nombres.getEditText().setText(Usuario.getNombres());
        Apellidos.getEditText().setText(Usuario.getApellidos());
        Correo.getEditText().setText(Usuario.getCorreo());
        String pref = Usuario.getTelefono().substring(0, 3);
        String tel = Usuario.getTelefono().substring(3);
        Teléfono.getEditText().setText(tel);

        int total = Pais.getCount();
        for (int i = 0; i < total; i++) {
            O_Pais aux = (O_Pais) Pais.getItemAtPosition(i);
            if (aux.getCarrier().equals(pref)) {
                Pais.setSelection(i);
                break;
            }
        }
    }

    private AdapterView.OnItemSelectedListener spn_ItemSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                O_Pais aux = (O_Pais) Pais.getAdapter().getItem(position);
                Prefijo.setText(aux.getCarrier());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void InicializarComponenetes() {
        Usuario = (O_Usuario) getIntent().getSerializableExtra("USUARIO");

        Nombres = findViewById(R.id.til_modificarperfil_nombres);
        Apellidos = findViewById(R.id.til_modificarperfil_apellidos);
        Correo = findViewById(R.id.til_modificarperfil_correo);
        Teléfono = findViewById(R.id.til_modificarperfil_telefono);

        Prefijo = findViewById(R.id.tv_modificarperfil_prefijo);

        Pais = findViewById(R.id.spn_modificarperfil_pais);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater in = getMenuInflater();
        in.inflate(R.menu.menu_modificarperfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_modificarperfil_guardarcambios:
                String nombres = Nombres.getEditText().getText().toString().trim();
                String apellidos = Apellidos.getEditText().getText().toString().trim();
                String correo = Correo.getEditText().getText().toString().trim();
                String telefono = Teléfono.getEditText().getText().toString().trim();
                O_Pais pais = (O_Pais) Pais.getSelectedItem();

                String error;
                O_Alerta alerta;
                if (!Funciones.ValidarNombres(nombres)) {
                    error = "Cada nombre debe contener al menos 2 caracteres.";
                    Nombres.setErrorEnabled(true);
                    Nombres.setError(error);
                    alerta = new O_Alerta(
                            O_Alerta.TIPO_ERROR,
                            "Validación de nombres",
                            error,
                            false,
                            2500,
                            O_Alerta.RES_ICO_ERROR
                    );
                    MostrarAlerta(alerta);
                } else if (Funciones.ValidarApellidos(apellidos) != 0) {
                    Nombres.setErrorEnabled(false);

                    int res = Funciones.ValidarApellidos(apellidos);

                    if (res == -1) {
                        error = "Debes escribir al menos 2 apellidos.";
                    } else {
                        error = "Cada apellido debe contener al menos 2 caracteres y cracteres de la 'a' a la 'z'.";
                    }
                    Apellidos.setErrorEnabled(true);
                    Apellidos.setError(error);
                    alerta = new O_Alerta(
                            O_Alerta.TIPO_ERROR,
                            "Validación de apellidos",
                            error,
                            false,
                            2500,
                            O_Alerta.RES_ICO_ERROR
                    );
                    MostrarAlerta(alerta);
                } else if (!Funciones.ValidarCorreo(correo)) {
                    Nombres.setErrorEnabled(false);
                    Apellidos.setErrorEnabled(false);

                    error = "Formato de correo no válido.";
                    Correo.setErrorEnabled(true);
                    Correo.setError(error);
                    alerta = new O_Alerta(
                            O_Alerta.TIPO_ERROR,
                            "Validación de correo",
                            error,
                            false,
                            2500,
                            O_Alerta.RES_ICO_ERROR
                    );
                    MostrarAlerta(alerta);
                } else if (!Funciones.ValidarTelefono(telefono)) {
                    Nombres.setErrorEnabled(false);
                    Apellidos.setErrorEnabled(false);
                    Correo.setErrorEnabled(false);

                    error = "Formato de Teléfono incorrecto.";
                    Teléfono.setErrorEnabled(true);
                    Teléfono.setError(error);
                    alerta = new O_Alerta(
                            O_Alerta.TIPO_ERROR,
                            "Validación de teléfono",
                            error,
                            false,
                            2500,
                            O_Alerta.RES_ICO_ERROR
                    );
                    MostrarAlerta(alerta);
                } else {
                    Nombres.setErrorEnabled(false);
                    Apellidos.setErrorEnabled(false);
                    Correo.setErrorEnabled(false);
                    Teléfono.setErrorEnabled(false);
                    telefono = pais.getCarrier() + telefono;
                    O_Usuario aux = new O_Usuario(Usuario.getID(), nombres, apellidos, correo, telefono, pais);
                    int res = F_Usuario.ModificarDatosPersonales(aux);
                    if (res != -1) {
                        Intent destino = new Intent();
                        if (res == 1) {
                            destino.putExtra("ACTUALIZAR", true);
                        } else {
                            destino.putExtra("ACTUALIZAR", false);
                        }
                        setResult(Activity.RESULT_OK, destino);
                        finish();
                    } else {
                        alerta = new O_Alerta(
                                O_Alerta.TIPO_ERROR,
                                "Error al guardar",
                                "Hubo un error al guardar, intentar nuevamente.",
                                false,
                                2500,
                                O_Alerta.RES_ICO_ERROR
                        );
                        MostrarAlerta(alerta);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
