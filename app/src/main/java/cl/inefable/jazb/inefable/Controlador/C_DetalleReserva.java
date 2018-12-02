package cl.inefable.jazb.inefable.Controlador;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import cl.inefable.jazb.inefable.Modelo.FUNCIONES.F_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Alerta;
import cl.inefable.jazb.inefable.R;
import com.tapadoo.alerter.Alerter;

public class C_DetalleReserva extends AppCompatActivity {
    public static final int ActCode = 45;

    private TextView Patente, Marca, Volumen, CargaMax, Tipo, Valor, Propietario, DirInicio, DirDestino, Distancia, ValorBase, CostoTotal;

    private Button GuardarReserva;
    private O_Reserva reserva;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detallereserva);

        InicializarComponentes();
        ConfigurarComponenetes();
        ConfigurarListeners();
    }

    private void ConfigurarListeners() {
        GuardarReserva.setOnClickListener(btn_Click());
    }

    private View.OnClickListener btn_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_detallereserva_guardarreserva:
                        AlertDialog.Builder a = new AlertDialog.Builder(C_DetalleReserva.this);
                        a.setTitle("Confirmación Reserva.");
                        a.setMessage("Una ves que confirmes la reserva, deberás esperar que el conductor acepte tu solicitud.");
                        a.setPositiveButton("Reservar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (F_Usuario.AgregarReserva(reserva) == 1) {
                                    Intent data = new Intent();
                                    data.putExtra("CORRECTO", true);
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                } else {
                                    O_Alerta alerta = new O_Alerta(
                                            O_Alerta.TIPO_ERROR,
                                            "Reservar vehículo",
                                            "Hubo un error al procesar la reserva, intentar nuevamente mas tarde.",
                                            false,
                                            2500,
                                            O_Alerta.RES_ICO_ERROR
                                    );
                                    MostrarAlerta(alerta);
                                }
                            }
                        });
                        a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog ad = a.create();
                        ad.show();
                        break;
                }
            }
        };
    }

    private void ConfigurarComponenetes() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        O_Vehiculo vehiculo = (O_Vehiculo) getIntent().getSerializableExtra("VEHICULO");
        reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
        double volumen = vehiculo.getAltura() * vehiculo.getLargo() * vehiculo.getAncho();
        double meterscubics = Math.pow(100, 3);
        double dims;
        String msj;
        if (volumen >= meterscubics) {
            dims = volumen / meterscubics;
            msj = dims + " m³";
        } else {
            dims = volumen;
            msj = dims + " cm³";
        }
        int costoTotal = vehiculo.getValorBase() + (vehiculo.getValor() * (reserva.getRuta().getDistanciaReal() / 1000));
        reserva.setValorTotal(costoTotal);
        Patente.setText("Patente: " + vehiculo.getPatente());
        Marca.setText("Marca: " + vehiculo.getMarca());
        Volumen.setText("Volumen (alto, largo, ancho): " + msj + " (" + vehiculo.getAltura() + ", " + vehiculo.getLargo() + ", " + vehiculo.getAncho() + ")");
        CargaMax.setText("Carga Máx.: " + vehiculo.getCargaMax() + " Kg.");
        Tipo.setText("Marca: " + vehiculo.getTipo());
        Valor.setText("Valor estimado: $" + vehiculo.getValor() + "/km.");
        ValorBase.setText("Valor Base: $" + vehiculo.getValorBase());
        Propietario.setText("Propietario: " + vehiculo.getPropietario());
        DirInicio.setText("Dirección de Inicio: " + reserva.getRuta().getDireccionInicio() + " COOR: (" + reserva.getInicio().getLatitud() + " ; " + reserva.getInicio().getLongitud() + ")");
        DirDestino.setText("Dirección de destino: " + reserva.getRuta().getDireccionDestino() + " COOR: (" + reserva.getLatDes() + " ; " + reserva.getLongDes() + ")");
        Distancia.setText("Distancia: " + reserva.getRuta().getDistanciaAprox() + " (" + reserva.getRuta().getDistanciaReal() + " m)");
        CostoTotal.setText("Valor estimado del servicio: $" + costoTotal);
    }

    private void InicializarComponentes() {
        Patente = findViewById(R.id.tv_detallereserva_patente);
        Marca = findViewById(R.id.tv_detallereserva_marca);
        Volumen = findViewById(R.id.tv_detallereserva_volumen);
        CargaMax = findViewById(R.id.tv_detallereserva_cargamax);
        Tipo = findViewById(R.id.tv_detallereserva_tipo);
        Valor = findViewById(R.id.tv_detallereserva_valor);
        Propietario = findViewById(R.id.tv_detallereserva_propietario);
        DirInicio = findViewById(R.id.tv_detallereserva_direccioninicio);
        DirDestino = findViewById(R.id.tv_detallereserva_direciondestino);
        Distancia = findViewById(R.id.tv_detallereserva_distancia);
        CostoTotal = findViewById(R.id.tv_detallereserva_costototal);
        ValorBase = findViewById(R.id.tv_detallereserva_valorbase);

        GuardarReserva = findViewById(R.id.btn_detallereserva_guardarreserva);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean res;
        switch (item.getItemId()) {
            case android.R.id.home:
                goBackResult();
                finish();
                res = true;
                break;
            default:
                res = super.onOptionsItemSelected(item);
                break;
        }
        return res;
    }

    private void goBackResult() {
        O_Reserva reserva = (O_Reserva) getIntent().getSerializableExtra("RESERVA");
        Intent datos = new Intent();
        datos.putExtra("RESERVA", reserva);
        setResult(-1, datos);
    }

    @Override
    public void onBackPressed() {
        goBackResult();
        super.onBackPressed();
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
