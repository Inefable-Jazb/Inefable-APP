package cl.inefable.jazb.inefable.Modelo.FUNCIONES;

import android.util.Log;
import cl.inefable.jazb.inefable.Modelo.CONN.Enlace;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Reserva;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Usuario;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class F_Usuario {
    public static int ModificarDatosPersonales(O_Usuario usuario) {
        String params = null;
        try {
            params = "TIPO=usuario&OP=actualizardatospersonales&ID=" + usuario.getID() +
                    "&NOMBRE=" + URLEncoder.encode(usuario.getNombres(), "UTF-8") +
                    "&APELLIDO=" + URLEncoder.encode(usuario.getApellidos(), "UTF-8") +
                    "&CORREO=" + usuario.getCorreo() +
                    "&TELEFONO=" + URLEncoder.encode(usuario.getTelefono(), "UTF-8") +
                    "&PAIS=" + usuario.getPais().getID();
        } catch (UnsupportedEncodingException e) {
            return -1;
        }
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public static int AgregarReserva(O_Reserva reserva) {
        try {
            String params = "TIPO=reserva&" +
                    "OP=hacerreserva&" +
                    "IDUSUARIO=" + reserva.getUsuario().getID() + "&" +
                    "IDCAMION=" + reserva.getVehiculo().getID() + "&" +
                    "TIPORESERVA=1&" +
                    "LATITUDDESTINO=" + reserva.getLatDes() + "&" +
                    "LONGITUDDESTINO=" + reserva.getLongDes() + "&" +
                    "VALORTOTAL=" + reserva.getValorTotal() + "&" +
                    "DIRECCIONFULL=" + URLEncoder.encode(reserva.getRuta().getDireccionInicio(), "utf-8") + "&" +
                    "LATITUD=" + reserva.getInicio().getLatitud() + "&" +
                    "LONGITUD=" + reserva.getInicio().getLongitud();
            Enlace.RespuetaHTTP respueta;
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ArrayList<O_Reserva> TaaerReservasCliente(int id) {
        ArrayList<O_Reserva> listado = new ArrayList<>();

        return listado;
    }

    public int Crear(O_Usuario usuario) {
        String params = "TIPO=usuario&OP=agregar&USUARIO=" + usuario.getUsuario() + "&CLAVE=" + usuario.getClave() + "&TIPP=" + usuario.getTipo();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public List<O_Usuario> TraerTodos() {
        return null;
    }

    public static O_Usuario TraerDatosUsuario(int ID) {
        String params = "TIPO=usuario&OP=traertodo&USUARIO=" + ID;
        Enlace.RespuetaHTTP respueta;
        O_Usuario usuarioBD = null;
        try {
            respueta = new Enlace().execute(params).get();
            Log.d("HTTP RESPONSE DATA", respueta.getRespuesta());
            JSONArray res = new JSONArray(respueta.getRespuesta());
            if (respueta.getRespuesta().equals("0") || respueta.getRespuesta().equals("-1")) {
                return null;
            } else {
                usuarioBD = new O_Usuario(
                        res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getInt(3),
                        res.getString(4),
                        res.getString(5),
                        res.getString(6),
                        res.getString(7)
                );
            }
            return usuarioBD;
        } catch (Exception e) {
            return null;
        }
    }

    public O_Usuario TraerDatosLogin(O_Usuario usuario) {
        String params = "TIPO=usuario&OP=comprobarlogin&USUARIO=" + usuario.getUsuario() + "&CLAVE=" + usuario.getClave();
        Enlace.RespuetaHTTP respueta;
        O_Usuario usuarioBD = null;
        try {
            respueta = new Enlace().execute(params).get();
            Log.d("HTTP RESPONSE DATA", respueta.getRespuesta());
            JSONArray res = new JSONArray(respueta.getRespuesta());
            if (respueta.getRespuesta().equals("0") || respueta.getRespuesta().equals("-1")) {
                return null;
            } else {
                usuarioBD = new O_Usuario(
                        res.getInt(0),
                        res.getString(1),
                        res.getString(2),
                        res.getInt(3),
                        res.getInt(4)
                );
            }
            return usuarioBD;
        } catch (Exception e) {
            return null;
        }
    }

    public int comprobarUsuario(O_Usuario usuario) {
        String params = "TIPO=usuario&OP=comprobarusuario&USUARIO=" + usuario.getUsuario();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            Log.d("HTTP RESPONSE DATA", respueta.getRespuesta());
            int resCode = Integer.parseInt(respueta.getRespuesta());
            return resCode;
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean AgregarDatosPersonales(O_Usuario usuario) {
        String params = null;
        try {
            params = "TIPO=usuario&OP=datospersonales&ID=" +
                    usuario.getID() + "&NOMBRE=" +
                    URLEncoder.encode(usuario.getNombres(), "UTF-8") + "&APELLIDO=" +
                    URLEncoder.encode(usuario.getApellidos(), "UTF-8") + "&CORREO=" +
                    usuario.getCorreo() + "&TELEFONO=" +
                    URLEncoder.encode(usuario.getTelefono(), "UTF-8") + "&PAIS=" +
                    usuario.getPais().getID();
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta()) == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
