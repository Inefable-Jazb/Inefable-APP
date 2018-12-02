package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

public class C_Login extends AppCompatActivity {

    private Button Autenticar, CrearCuenta;
    private TextInputLayout Usuario, Clave;
    private RadioGroup TipoUsuario;
    private O_Alerta MensajeAlerta;
    private LinearLayout LoadingPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        InicializarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Autenticar.setOnClickListener(btn_Click());
        CrearCuenta.setOnClickListener(btn_Click());

        Usuario.getEditText().setOnFocusChangeListener(et_FocusChange());
        Clave.getEditText().setOnFocusChangeListener(et_FocusChange());
    }

    private View.OnFocusChangeListener et_FocusChange() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String valor = ((EditText) v).getText().toString().trim();
                if (!hasFocus && !valor.equals("")) {
                    switch (v.getTag().toString()) {
                        case "Usuario":
                            String usuario = valor;
                            if (ValidarUsuario(usuario)) {
                                Usuario.setErrorEnabled(false);
                            } else {
                                Usuario.setErrorEnabled(true);
                                Usuario.setError(MensajeAlerta.getMensaje());
                            }
                            MostrarAlerta(MensajeAlerta);
                            break;
                        case "Clave":
                            String clave = valor;
                            if (ValidarClave(clave)) {
                                Clave.setErrorEnabled(false);
                            } else {
                                Clave.setErrorEnabled(true);
                                Clave.setError(MensajeAlerta.getMensaje());
                            }
                            MostrarAlerta(MensajeAlerta);
                            break;
                    }
                }
            }
        };
    }

    private void ActivarControles(boolean activo) {
        Usuario.getEditText().setEnabled(activo);
        Clave.getEditText().setEnabled(activo);
        if (activo) {
            LoadingPanel.setVisibility(View.GONE);
            Autenticar.setVisibility(View.VISIBLE);
        } else {
            LoadingPanel.setVisibility(View.VISIBLE);
            Autenticar.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_login_autenticar:
                        String usuarioValor = Usuario.getEditText().getText().toString().trim().replace(".", "");
                        String claveValor = Clave.getEditText().getText().toString().trim();
                        ActivarControles(false);

                        if (ValidarUsuario(usuarioValor) && ValidarClave(claveValor)) {
                            if (TipoUsuario.getCheckedRadioButtonId() == R.id.rb_login_conductor) {
                                usuarioValor = Funciones.formatearRUT(usuarioValor);
                            }

                            try {
                                F_Usuario func_Usuario = new F_Usuario();
                                O_Usuario usuariobd, usuarioactual;
                                usuarioactual = new O_Usuario(usuarioValor, claveValor);
                                usuariobd = func_Usuario.TraerDatosLogin(usuarioactual);
                                if (usuariobd != null) {
                                    int info = usuariobd.getIDInfo();
                                    Intent destino;
                                    if (info == 0) {
                                        destino = new Intent(C_Login.this, C_FirstLogin.class);
                                    } else {
                                        destino = new Intent(C_Login.this, C_Principal.class);
                                    }
                                    destino.putExtra("IDUSUARIO", usuariobd.getID());
                                    startActivity(destino);
                                    finish();
                                } else {
                                    MensajeAlerta = new O_Alerta(
                                            O_Alerta.TIPO_ERROR,
                                            "Autenticación Usuario",
                                            "Login Fallido. Revisar Nombre de usuario y/o clave.",
                                            false,
                                            3500,
                                            O_Alerta.RES_ICO_ERROR
                                    );
                                    MostrarAlerta(MensajeAlerta);
                                    ActivarControles(true);
                                }
                            } catch (Exception e) {
                                MensajeAlerta = new O_Alerta(
                                        O_Alerta.TIPO_ERROR,
                                        "Autenticación Usuario",
                                        "Hubo un error al conectar, intente nuevamente.",
                                        false,
                                        2500,
                                        O_Alerta.RES_ICO_INFO
                                );
                                MostrarAlerta(MensajeAlerta);
                                ActivarControles(true);
                            }
                        }
                        break;
                    case R.id.btn_login_crearcuenta:
                        startActivityForResult(new Intent(C_Login.this, C_Signin.class), C_Signin.ActCode);
                        break;
                }
            }
        };
    }

    private boolean ValidarClave(String claveValor) {
        boolean correcto = false;
        if (!claveValor.equals("")) {
            MensajeAlerta = new O_Alerta(
                    O_Alerta.TIPO_CORRECTO,
                    "Comprobación Clave",
                    "Es una clave válida.",
                    false,
                    3500,
                    O_Alerta.RES_ICO_CORRECTO
            );
            int largo = claveValor.length();
            if (largo >= 8) {
                correcto = true;
            } else {
                MensajeAlerta = new O_Alerta(
                        O_Alerta.TIPO_ERROR,
                        "Comprobación Clave",
                        "No es una clave válida. Mímino 8 caracteres.",
                        false,
                        3500,
                        O_Alerta.RES_ICO_ERROR
                );
            }
        } else {
            MensajeAlerta = new O_Alerta(
                    O_Alerta.TIPO_PRECAUCION,
                    "Comprobación Clave",
                    "Campo Clave Vacío",
                    false,
                    3500,
                    O_Alerta.RES_ICO_PRECAUCION
            );
        }
        return correcto;
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
    protected void onResume() {
        super.onResume();
        Funciones.UsuarioActual = null;
    }

    private boolean ValidarUsuario(String usuarioValor) {
        usuarioValor = usuarioValor.replace(".", "");
        boolean correcto = false;
        if (!usuarioValor.equals("")) {
            MensajeAlerta = new O_Alerta(
                    O_Alerta.TIPO_CORRECTO,
                    "Comprobación Usuario",
                    "'" + usuarioValor + "' Es un usuario válido.",
                    false,
                    3500,
                    O_Alerta.RES_ICO_CORRECTO
            );
            int actual = TipoUsuario.getCheckedRadioButtonId();
            Log.d("TIPO USUARIO", "//" + actual);
            switch (actual) {
                case R.id.rb_login_cliente:
                    if (Funciones.esCliente(usuarioValor)) {
                        Usuario.getEditText().setText(usuarioValor);
                        correcto = true;
                    } else {
                        MensajeAlerta = new O_Alerta(
                                O_Alerta.TIPO_ERROR,
                                "Comprobación Usuario",
                                "'" + usuarioValor + "' No es un usuario 'Cliente' válido. Mínimo 4 caracteres.",
                                false,
                                3500,
                                O_Alerta.RES_ICO_ERROR
                        );
                    }
                    break;
                case R.id.rb_login_conductor:
                    if (Funciones.esRUT(usuarioValor)) {
                        Usuario.getEditText().setText(Funciones.formatearRUT(usuarioValor));
                        correcto = true;
                    } else {
                        MensajeAlerta = new O_Alerta(
                                O_Alerta.TIPO_ERROR,
                                "Comprobación F_Usuario",
                                "Usuario 'Conductor' Ingresado no es un RUT",
                                false,
                                3500,
                                O_Alerta.RES_ICO_ERROR
                        );
                    }
                    break;
            }
        } else {
            MensajeAlerta = new O_Alerta(
                    O_Alerta.TIPO_PRECAUCION,
                    "Error F_Usuario",
                    "Campo F_Usuario Vacio",
                    false,
                    3500,
                    O_Alerta.RES_ICO_PRECAUCION
            );
        }
        return correcto;
    }

    private void InicializarComponentes() {
        Autenticar = findViewById(R.id.btn_login_autenticar);
        CrearCuenta = findViewById(R.id.btn_login_crearcuenta);

        Usuario = findViewById(R.id.til_login_usuario);
        Clave = findViewById(R.id.til_login_clave);

        TipoUsuario = findViewById(R.id.rg_login_tipousuario);
        LoadingPanel = findViewById(R.id.ll_login_cargando);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case C_Signin.ActCode:
                if (resultCode == Activity.RESULT_OK) {
                    Usuario.getEditText().setText(data.getStringExtra("Username"));
                    Clave.getEditText().setText(data.getStringExtra("Password"));
                    if (data.getBooleanExtra("Type", false)) {
                        TipoUsuario.check(R.id.rb_login_conductor);
                    }
                    O_Alerta alerta = new O_Alerta(
                            O_Alerta.TIPO_CORRECTO,
                            "Creación Correcta",
                            "Presione el botón Autenticar para entrar con su nueva cuenta.",
                            false,
                            2500,
                            O_Alerta.RES_ICO_CORRECTO
                    );
                    MostrarAlerta(alerta);
                }
                break;
        }
    }
}
