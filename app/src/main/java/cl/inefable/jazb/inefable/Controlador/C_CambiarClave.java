package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Ruta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

public class C_CambiarClave extends AppCompatActivity {

    public static final int ActCode = 200;
    private TextInputLayout tilActual, tilNueva, tilConfirmar;
    private Button btnConfirmar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cambiarclave);

        initializeComponents();
        setListeners();
    }

    private void initializeComponents(){
        tilActual = findViewById(R.id.til_cambiarclave_claveactual);
        tilNueva = findViewById(R.id.til_cambiarclave_nuevaclave);
        tilConfirmar = findViewById(R.id.til_cambiarclave_confirmarclave);
        btnConfirmar = findViewById(R.id.btn_cambiarclave_confirmar);
    }

    private void setListeners(){
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarClave();
            }
        });
    }

    private void cambiarClave(){

        if (!validarClavesIguales(tilNueva, tilConfirmar)){
            return;
        }
        if (!validarEditTextClave(tilActual) | !validarEditTextClave(tilNueva) | !validarEditTextClave(tilConfirmar)){
            return;
        }

        String claveActual = tilActual.getEditText().getText().toString().trim();
        String claveNueva = tilNueva.getEditText().getText().toString().trim();

        int id = Funciones.UsuarioActual.getID();

        O_Usuario usuario_old = new O_Usuario(id, claveActual);
        O_Usuario usuario_new = new O_Usuario(id, claveNueva);

        int res = F_Usuario.CambiarClave(usuario_old, usuario_new);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private boolean validarEditTextClave(TextInputLayout til){
        String texto = til.getEditText().getText().toString().trim();
        if (texto.isEmpty()){
            til.setError("Debe ingresar una clave");
            return false;
        }
        if (texto.length() < 8){
            til.setError("La clave es demasiado corta");
            return false;
        }
        til.setError(null);
        return true;
    }

    private boolean validarClavesIguales(TextInputLayout til1, TextInputLayout til2){
        String text1 = til1.getEditText().getText().toString().trim();
        String text2 = til2.getEditText().getText().toString().trim();

        if (!text1.equals(text2)){
            til1.setError("Las claves no coinciden");
            til2.setError("Las claves no coinciden");
            return false;
        }

        til1.setError(null);
        til2.setError(null);
        return true;
    }
}
