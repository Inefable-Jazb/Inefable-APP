package cl.inefable.jazb.inefable.Modelo.FUNCIONES;

import android.util.Log;
import cl.inefable.jazb.inefable.Modelo.CONN.Enlace;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Pais;
import org.json.JSONArray;

import java.util.ArrayList;

public class F_Pais {
    public static ArrayList<O_Pais> TraerListaPaises() {
        String params = "TIPO=pais&OP=traertodos";
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_Pais> lista;

        try {
            lista = new ArrayList<>();
            respueta = new Enlace().execute(params).get();
            Log.d("HTTP RESPONSE DATA", respueta.getRespuesta());
            if (respueta.getRespuesta().equals("0")) {
                return null;
            } else {
                String[] filas = respueta.getRespuesta().split("%%");
                for (String fila :
                        filas) {
                    JSONArray r = new JSONArray(fila);
                    O_Pais aux = new O_Pais(
                            r.getInt(0),
                            r.getString(1),
                            r.getString(2)
                    );
                    lista.add(aux);
                }
                return lista;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
