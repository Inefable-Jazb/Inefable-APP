package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Valoracion;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

public class C_ValorarServicio extends AppCompatActivity {
    public static final int ActCode = 13265;

    private O_Usuario Usuario;

    private O_Reserva Reserva;

    private TextView Patente;

    private RatingBar Puntaje;

    private EditText Comentario;

    private Button Guardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_valorarservicio);

        InicializarComponentes();
        ConfigurarComponentes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        Guardar.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float puntaje = Puntaje.getRating();
                String comentario = Comentario.getText().toString().trim();

                O_Valoracion valoracion = new O_Valoracion(
                        puntaje,
                        comentario,
                        Reserva.getID()
                );
                if (F_Usuario.ValorarServicios(valoracion) <= 0) {
                    O_Alerta alerta = new O_Alerta(
                            O_Alerta.TIPO_ERROR,
                            "Gestión de Reservas",
                            "Hubo un error al valorar la reserva, intentar nuevamente.",
                            false,
                            2500,
                            O_Alerta.RES_ICO_ERROR
                    );
                    MostrarAlerta(alerta);
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        };
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
        Usuario = (O_Usuario) getIntent().getSerializableExtra("USUARIO");
        Reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");

        Patente.setText("Patente del vehículo: " + Reserva.getVehiculo().getPatente());
    }

    private void InicializarComponentes() {
        Patente = findViewById(R.id.tv_valorarservicio_Patente);
        Puntaje = findViewById(R.id.rb_valorarservicio_Puntuacion);
        Comentario = findViewById(R.id.et_valorarservicio_Comentario);
        Guardar = findViewById(R.id.btn_valorarservicio_Valorar);
    }
}
