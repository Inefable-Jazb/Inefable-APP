package cl.inefable.jazb.inefable.Controlador;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.POJO.O_Ruta;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Funciones {
    public static O_Usuario UsuarioActual;

    private static String regexUsuarioMIN4 = "^[a-zA-Z\\d\\-ñ]{4,}$";
    private static String regexUsuarioClave = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private static String regexPatente = "^[BCDFGHJKLPRSTVWXY]{4}$";
    private static final String regexCorreo = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static String regexNombreApellidoMarca = "^[a-zA-Zñá-úÁ-Ú]{2,}$";

    public void tuViejaEstuvoAqui(){
        System.out.println("polloputo");
    }

    public static String[] nivelesClave = new String[]{
            "^(?=.*[A-Z])[a-zA-Z\\d]+$",
            "^(?=.*[a-z])[a-zA-Z\\d]+$",
            "^(?=.*[\\d])[a-zA-Z\\d]+$",
            "^[a-zA-Z\\d]{8,}$"
    };

    public static boolean esRUT(String rut) {
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (Exception e) {
        }
        return validacion;
    }

    public static boolean esCliente(String usuario) {
        Pattern p = Pattern.compile(regexUsuarioMIN4);
        Matcher m = p.matcher(usuario);
        return m.matches();
    }

    public static String formatearRUT(String rut) {
        rut = rut.replace(".", "")
                .replace("-", "")
                .replace(" ", "");
        String verificador = rut.charAt(rut.length() - 1) + "";
        char[] cadena = rut.substring(0, rut.length() - 1).toCharArray();
        String cuerpo = "";
        String aux = "";
        for (char c : cadena) {
            aux = c + aux;
        }
        cadena = aux.toCharArray();
        int cont = 1;
        for (char c : cadena) {
            if (cont % 3 == 0 && cont != cadena.length) {
                cuerpo += c + ".";
            } else {
                cuerpo += c;
            }
            cont++;
        }
        cadena = cuerpo.toCharArray();
        aux = "";
        for (char c : cadena) {
            aux = c + aux;
        }
        return aux + "-" + verificador;
    }

    public static boolean cumple(String valor, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(valor);
        return m.matches();
    }

    public static boolean ValidarNombres(String Nombres) {
        String[] nombres = Nombres.split(" ");
        for (String nombre :
                nombres) {
            if (!cumple(nombre, regexNombreApellidoMarca)) {
                return false;
            }
        }
        return true;
    }

    public static int ValidarApellidos(String Apellidos) {
        String[] apellidos = Apellidos.split(" ");
        if (apellidos.length < 2) {
            return -1;
        } else {
            for (String apellido :
                    apellidos) {
                if (!cumple(apellido, regexNombreApellidoMarca)) {
                    return -2;
                }
            }
        }
        return 0;
    }

    public static boolean ValidarCorreo(String Correo) {
        return cumple(Correo, regexCorreo);
    }

    public static boolean ValidarTelefono(String Telefono) {
        if (Telefono.length() == 9) {
            try {
                int num = Integer.parseInt(Telefono);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna:
     * 0: En caso de ser una patente correcta.
     * -1: En caso de que no contenga un -.
     * -2: En caso de que el conjunto de letras no sea el correcto.
     * -3: En caso de que el usuario haya escrito letras en la sección de los números.
     * -4: En caso de que el número no esté entre 10 y 99.
     *
     * @param Patente
     * @return
     */
    public static boolean ValidarPatente(String Patente) {
        if (Patente.contains("-")) {
            String[] partes = Patente.split("-");
            if (!cumple(partes[0], regexPatente)) {
                return false;
            } else {
                try {
                    int numero = Integer.parseInt(partes[1]);
                    if (numero <= 99 & numero >= 10) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static boolean ValidarMarca(String Marca) {
        return cumple(Marca, regexNombreApellidoMarca);
    }

    public static String[] obtenerDireccion(Geocoder geocoder, LatLng latlng) {
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String[] address = {
                addresses.get(0).getAddressLine(0), // [0] -> Dirección completa
                addresses.get(0).getLocality(), // [1] -> Ciudad
                addresses.get(0).getAdminArea(), // [2] -> Región
                addresses.get(0).getCountryName(), // [3] -> País
                addresses.get(0).getPostalCode(),
                addresses.get(0).getFeatureName(),
                addresses.get(0).getThoroughfare(), // [6] -> Calle
                addresses.get(0).getSubThoroughfare(), // [7] -> Número
        };
        return address;
    }

    public static String NombreDIreccionLatLon(Geocoder geocoder, LatLng latlng) {
        String[] direccion = obtenerDireccion(geocoder, latlng);
        return direccion[6] + " " + direccion[7] + ", " + direccion[1];
    }
    public static O_Ruta CalcularDistancia(O_Reserva reserva) {
        String auxxx;
        try {
            auxxx = new MatrixAsync().execute(reserva).get().Respuesta;
            JSONObject matrixDATA = new JSONObject(auxxx);
            Log.d("JSON MAP", auxxx);

            JSONObject rows = matrixDATA.getJSONArray("rows").getJSONObject(0);
            JSONObject elements = rows.getJSONArray("elements").getJSONObject(0);
            JSONObject distance = elements.getJSONObject("distance");
            JSONObject duration = elements.getJSONObject("duration");

            O_Ruta ruta = new O_Ruta(
                    matrixDATA.getJSONArray("origin_addresses").getString(0),
                    matrixDATA.getJSONArray("destination_addresses").getString(0),
                    distance.getString("text"),
                    distance.getInt("value"),
                    duration.getString("text"),
                    duration.getInt("value")
            );
            return ruta;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LatLng ObtenerLatLngporDireccion(Geocoder coder, String strAddress) {
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }



    private static class MatrixAsync extends AsyncTask<O_Reserva, Integer, MatrixAsync.RespuetaHTTP> {

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
        protected RespuetaHTTP doInBackground(O_Reserva... Url) {
            RespuetaHTTP respuesta = null;
            if (!isCancelled() || Url != null && Url.length > 0) {
                String BaseIP = "https://maps.googleapis.com/maps/api/distancematrix/json";
                String inicio = "?origins=" + Url[0].getInicio().getLatitud() + "," + Url[0].getInicio().getLongitud();
                String destino = "&destinations=" + Url[0].getLatDes() + "," + Url[0].getLongDes();
                String googleKey = "&key=AIzaSyDtiJomi-vfspYmxWGLO0cISVMRZaDQYGE";
                String full = BaseIP + inicio + destino + googleKey;
                Log.d("Starting Download...", "...Connecting to " + full);
                try {
                    URL url = new URL(full);
                    String resultado = descargarURL(url);
                    if (resultado != null) {
                        respuesta = new RespuetaHTTP(resultado);
                    } else {
                        throw new IOException("Sin respuesta desde " + full);
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
            HttpsURLConnection coneccion = null;
            String respuesta = null;

            try {
                coneccion = (HttpsURLConnection) url.openConnection();
                coneccion.setReadTimeout(3000);
                coneccion.setConnectTimeout(3000);
                coneccion.setRequestMethod("GET");
                coneccion.setDoInput(true);
                coneccion.connect();
                int codigoRespuesta = coneccion.getResponseCode();
                if (codigoRespuesta != HttpsURLConnection.HTTP_OK) {
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
}
