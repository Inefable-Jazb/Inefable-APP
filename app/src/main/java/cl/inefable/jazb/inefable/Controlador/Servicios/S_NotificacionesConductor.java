package cl.inefable.jazb.inefable.Controlador.Servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import cl.inefable.jazb.inefable.Controlador.Funciones;
import org.json.JSONObject;

import java.net.URL;

public class S_NotificacionesConductor extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class BGT extends AsyncTask<String, String, String> {
        private String Direccion = "https://inefable.cl/BD/API.php?TIPO=reserva&OP=:OP:&CONDUCTOR=:conductor:";

        @Override
        protected String doInBackground(String... args) {
            String idConductor = args[0];
            String op = args[1];
            Direccion = Direccion.replace(":OP:", op)
                    .replace(":conductor:", idConductor);
            try {
                URL url = new URL(Direccion);
                String res = Funciones.descargarURL(url);
                JSONObject respuesta = new JSONObject(res);
            } catch (Exception e) {
                Log.w("ERROR NOTI COND", e.getMessage());
            }
            return null;
        }
    }
}
