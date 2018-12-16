package cl.inefable.jazb.inefable.Modelo.CONN;

import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class Enlace extends AsyncTask<String, Integer, Enlace.RespuetaHTTP> {
    public static String IpActual = "https://inefable.cl/BD/API.php?";

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
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            stream = connection.getInputStream();
            if (stream != null) {
                result = leerRespuesta(stream, 50000);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
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
