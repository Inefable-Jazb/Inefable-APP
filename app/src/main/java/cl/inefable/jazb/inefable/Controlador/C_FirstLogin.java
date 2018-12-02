package cl.inefable.jazb.inefable.Controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

public class C_FirstLogin extends AppCompatActivity {
    private int IDusuario;

    private TextInputLayout Nombres, Apellidos, Correo, Teléfono;
    private Spinner Pais;
    private Button Guardar, Logout;

    private TextView Prefijo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_firstlogin);

        InicializarComponenetes();
        ConfigurarComponenetes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Nombres.getEditText().setOnFocusChangeListener(et_Focus(0));
        Apellidos.getEditText().setOnFocusChangeListener(et_Focus(1));
        Correo.getEditText().setOnFocusChangeListener(et_Focus(2));
        Teléfono.getEditText().setOnFocusChangeListener(et_Focus(3));

        Guardar.setOnClickListener(btn_Click());
        Logout.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_firstlogin_guardar:
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
                            Toast.makeText(C_FirstLogin.this, res + "", Toast.LENGTH_SHORT).show();
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

                            F_Usuario func_Usuario = new F_Usuario();
                            telefono = pais.getCarrier() + telefono;
                            O_Usuario aux = new O_Usuario(IDusuario, nombres, apellidos, correo, telefono, pais);
                            boolean res = func_Usuario.AgregarDatosPersonales(aux);
                            if (res) {
                                Intent destino = new Intent(C_FirstLogin.this, C_Principal.class);
                                destino.putExtra("IDUSUARIO", IDusuario);
                                startActivity(destino);
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
                    case R.id.btn_firstlogin_cerrarsesion:
                        Intent destino = new Intent(C_FirstLogin.this, C_Login.class);
                        startActivity(destino);
                        finish();
                        break;
                }
            }
        };
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

    private void ConfigurarComponenetes() {
        ArrayList<O_Pais> paises = F_Pais.TraerListaPaises();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, paises);
        Pais.setAdapter(adapter);
        Pais.setOnItemSelectedListener(spn_ItemSelected());
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
        IDusuario = getIntent().getIntExtra("IDUSUARIO", -1);

        Nombres = findViewById(R.id.til_firstlogin_nombres);
        Apellidos = findViewById(R.id.til_firstlogin_apellidos);
        Correo = findViewById(R.id.til_firstlogin_correo);
        Teléfono = findViewById(R.id.til_firstlogin_telefono);

        Prefijo = findViewById(R.id.tv_firstlogin_prefijo);

        Guardar = findViewById(R.id.btn_firstlogin_guardar);
        Logout = findViewById(R.id.btn_firstlogin_cerrarsesion);

        Pais = findViewById(R.id.spn_firstlogin_pais);
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
