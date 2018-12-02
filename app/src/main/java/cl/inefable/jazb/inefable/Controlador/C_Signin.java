package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

public class C_Signin extends AppCompatActivity {
    public static final int ActCode = 0123;

    private Button Crear;
    private TextView reqMayus, reqMinus, reqNum, reqMin8;
    private TextInputLayout Usuario, Clave1, Clave2;
    private Switch Tipo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signin);
        InicializarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Crear.setOnClickListener(btn_Click());

        Usuario.getEditText().setOnFocusChangeListener(et_Focus());
        Clave1.getEditText().addTextChangedListener(et_TextHandler(1));
        Clave2.getEditText().addTextChangedListener(et_TextHandler(2));
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_signin_CrearCuenta:
                        String usu;
                        usu = Usuario.getEditText().getText().toString().trim();
                        if (Tipo.isChecked()) {
                            usu = Funciones.formatearRUT(usu);
                        } else {
                            usu = usu.replace(".", "");
                        }
                        int tipo = (Tipo.isChecked()) ? 1 : 0;
                        Usuario.getEditText().setText(usu);
                        String cl1 = Clave1.getEditText().getText().toString().trim();
                        String cl2 = Clave2.getEditText().getText().toString().trim();
                        O_Alerta alerta;
                        if (usu.equals("") || cl1.equals("") || cl2.equals("")) {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_PRECAUCION,
                                    "Campos Vacios",
                                    "Completar todos los campos antes de contnuar.",
                                    false,
                                    2000,
                                    O_Alerta.RES_ICO_PRECAUCION
                            );
                            MostrarAlerta(alerta);
                        } else if (ComprobarUsuario(usu) == 0 &&
                                ComprobarSeguridadClave(cl1) == 4 &&
                                ComprobarIgualdadClaves(cl1, cl2)) {
                            F_Usuario func_Usuario = new F_Usuario();
                            O_Usuario usuarioNuevo = new O_Usuario(
                                    usu,
                                    cl1,
                                    tipo
                            );
                            if (func_Usuario.Crear(usuarioNuevo) == 1) {
                                Intent destino = new Intent();
                                destino.putExtra("Username", usu);
                                destino.putExtra("Password", cl1);
                                destino.putExtra("Type", Tipo.isChecked());
                                setResult(Activity.RESULT_OK, destino);
                                finish();
                            } else {
                                alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Error de Creación",
                                        "Hubo un error al crear la cuenta, intente denuevo mas tarde.",
                                        false,
                                        1500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            }
                        } else {
                            alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Error de comprobación",
                                    "Corregir los campos.",
                                    false,
                                    1500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        }
                        break;
                }
            }
        };
    }

    private View.OnFocusChangeListener et_Focus() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String valor = ((EditText) v).getText().toString().trim();
                    if (!valor.equals("")) {
                        boolean comp = true;
                        if (Tipo.isChecked()) {
                            comp = Funciones.esRUT(valor);
                            Log.d("ISRUT?", comp + "");
                            valor = Funciones.formatearRUT(valor);
                        } else {
                            valor = valor.replace(".", "");
                            comp = Funciones.esCliente(valor);
                        }
                        ((EditText) v).setText(valor);
                        if (comp) {
                            if (ComprobarUsuario(valor) == 0) {
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_CORRECTO,
                                        "Comprobación Usuario",
                                        "Nombre de usuario disponible para su uso.",
                                        false,
                                        1500,
                                        O_Alerta.RES_ICO_CORRECTO
                                );
                                MostrarAlerta(alerta);
                            } else {
                                O_Alerta alerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Comprobación Usuario",
                                        "Nombre de usuario no disponible.",
                                        false,
                                        1500,
                                        O_Alerta.RES_ICO_ERROR
                                );
                                MostrarAlerta(alerta);
                            }
                        } else {
                            Usuario.setErrorEnabled(true);
                            Usuario.setError("Usuario no válido.");
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_ERROR,
                                    "Comprobación Usuario",
                                    "Nombre de usuario no válido.",
                                    false,
                                    1500,
                                    O_Alerta.RES_ICO_ERROR
                            );
                            MostrarAlerta(alerta);
                        }
                    }
                }
            }
        };
    }

    private int ComprobarUsuario(String nombreUsuario) {
        F_Usuario func_Usuario = new F_Usuario();
        O_Usuario temp = new O_Usuario(nombreUsuario);
        boolean correcto = true;
        if (Tipo.isChecked()) {
            correcto = Funciones.esRUT(nombreUsuario);
        } else {
            nombreUsuario = nombreUsuario.replace(".", "");
            correcto = Funciones.esCliente(nombreUsuario);
        }
        int resCode;
        if (!correcto) {
            Log.d("SETT ERROR RUT", "SET ERROR USER; ON COMPUSU");
            Usuario.setErrorEnabled(true);
            Usuario.setError("Usuario no válido.");
            resCode = -2;
        } else {
            resCode = func_Usuario.comprobarUsuario(temp);
            if (resCode == 0) {
                Usuario.setErrorEnabled(false);
            } else if (resCode < 0) {
                Usuario.setErrorEnabled(true);
                Usuario.setError("Hubo un error al comprobar, intentar nuevamente.");
            } else {
                Usuario.setErrorEnabled(true);
                Usuario.setError("Nombre de usuario No disponible.");
            }
        }
        return resCode;
    }

    private TextWatcher et_TextHandler(final int et) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (et) {
                    case 1:
                        int cont = ComprobarSeguridadClave(s.toString());
                        if (cont == 4) {
                            Clave1.setErrorEnabled(false);
                            O_Alerta alerta = new O_Alerta(
                                    O_Alerta.TIPO_CORRECTO,
                                    "Requisitos Clave",
                                    "La clave ingresada cumple los requisitos mínimos.",
                                    false,
                                    1500,
                                    O_Alerta.RES_ICO_CORRECTO
                            );
                            MostrarAlerta(alerta);
                        } else {
                            Clave1.setErrorEnabled(true);
                            Clave1.setError("Clave no cumple con los requisitos mínimos de seguridad.");
                        }
                        break;
                    case 2:
                        String clave1 = Clave1.getEditText().getText().toString().trim();
                        String clave2 = s.toString();

                        if (!ComprobarIgualdadClaves(clave1, clave2)) {
                            Clave2.setErrorEnabled(true);
                            Clave2.setError("Claves no coinciden.");
                        } else {
                            Clave2.setErrorEnabled(false);
                        }
                        break;
                }
            }
        };
    }

    private boolean ComprobarIgualdadClaves(String clave1, String clave2) {
        return clave1.equals(clave2);
    }

    private int ComprobarSeguridadClave(String clave) {
        int cont = 0;
        if (Funciones.cumple(clave, Funciones.nivelesClave[0])) {
            reqMayus.setTextColor(getColor(R.color.Correcto));
            cont++;
        } else {
            reqMayus.setTextColor(getColor(R.color.Info));
        }
        if (Funciones.cumple(clave, Funciones.nivelesClave[1])) {
            reqMinus.setTextColor(getColor(R.color.Correcto));
            cont++;
        } else {
            reqMinus.setTextColor(getColor(R.color.Info));
        }
        if (Funciones.cumple(clave, Funciones.nivelesClave[2])) {
            reqNum.setTextColor(getColor(R.color.Correcto));
            cont++;
        } else {
            reqNum.setTextColor(getColor(R.color.Info));
        }
        if (Funciones.cumple(clave, Funciones.nivelesClave[3])) {
            reqMin8.setTextColor(getColor(R.color.Correcto));
            cont++;
        } else {
            reqMin8.setTextColor(getColor(R.color.Info));
        }
        return cont;
    }

    private void InicializarComponentes() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Crear = findViewById(R.id.btn_signin_CrearCuenta);

        reqMayus = findViewById(R.id.tv_signin_mayus);
        reqMinus = findViewById(R.id.tv_signin_minus);
        reqNum = findViewById(R.id.tv_signin_num);
        reqMin8 = findViewById(R.id.tv_signin_min8);

        Usuario = findViewById(R.id.til_signin_Usuario);
        Clave1 = findViewById(R.id.til_signin_Clave1);
        Clave2 = findViewById(R.id.til_signin_Clave2);

        Tipo = findViewById(R.id.sw_signin_tipousuario);
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
