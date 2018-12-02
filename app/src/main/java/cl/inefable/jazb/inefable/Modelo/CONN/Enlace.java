package cl.inefable.jazb.inefable.Modelo.CONN;

import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Enlace extends AsyncTask<String, Integer, Enlace.RespuetaHTTP> {
    public static String IpActual = "http://inefable.cl/BD/API.php?";

    public class RespuetaHTTP {
        private String Respuesta;
        private Exception Excepcion;

        public RespuetaHTTP(String respuesta) {
            Respuesta = respuesta;
        }

        public RespuetaHTTP(Exception excepcion) {
            Excepcion = excepcion;
        }

        public Exception getExcepcion() {
            return Excepcion;
        }

        public String getRespuesta() {
            return Respuesta;
        }
    }

    @Override
    protected RespuetaHTTP doInBackground(String... Url) {
        RespuetaHTTP respuesta = null;
        if (!isCancelled() || Url != null && Url.length > 0) {
            String parametros = Url[0];
            String fullURL = null;
            fullURL = IpActual + parametros;
            Log.d("Starting Download...", "...Connecting to " + fullURL);
            try {
                URL url = new URL(fullURL);
                String resultado = descargarURL(url);
                if (resultado != null) {
                    respuesta = new RespuetaHTTP(resultado);
                } else {
                    throw new IOException("Sin respuesta desde " + fullURL);
                }
            } catch (Exception e) {
                e.printStackTrace();
                respuesta = new RespuetaHTTP(e);
            }
        }
        return respuesta;
    }

    private String descargarURL(URL url) throws IOException {
        InputStream is = null;
        HttpURLConnection coneccion = null;
        String respuesta = null;

        try {
            coneccion = (HttpURLConnection) url.openConnection();
            coneccion.setReadTimeout(3000);
            coneccion.setConnectTimeout(3000);
            coneccion.setRequestMethod("GET");
            coneccion.setDoInput(true);
            coneccion.connect();
            int codigoRespuesta = coneccion.getResponseCode();
            if (codigoRespuesta != HttpURLConnection.HTTP_OK) {
                throw new IOException("ERROR HTTP CÓDIGO: " + codigoRespuesta);
            }
            is = coneccion.getInputStream();
            if (is != null) {
                respuesta = leerRespuesta(is, 100000);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (coneccion != null) {
                coneccion.disconnect();
            }
        }
        return respuesta;
    }

    private String leerRespuesta(InputStream stream, int maxLengthResponse) throws IOException, UnsupportedEncodingException {
        Reader r = null;
        r = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxLengthResponse];
        int tamañoLectura;
        StringBuffer sb = new StringBuffer();
        while (((tamañoLectura = r.read(rawBuffer)) != -1) && maxLengthResponse > 0) {
            if (tamañoLectura > maxLengthResponse) {
                tamañoLectura = maxLengthResponse;
            }
            sb.append(rawBuffer, 0, tamañoLectura);
            maxLengthResponse -= tamañoLectura;
        }
        return sb.toString();
    }
}
