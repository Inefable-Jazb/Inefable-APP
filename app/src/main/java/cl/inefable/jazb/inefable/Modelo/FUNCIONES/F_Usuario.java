package cl.inefable.jazb.inefable.Modelo.FUNCIONES;

import android.util.Log;
import cl.inefable.jazb.inefable.Modelo.CONN.Enlace;
import cl.inefable.jazb.inefable.Modelo.DATA.*;
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
                    "LATITUDDESTINO=" + URLEncoder.encode(reserva.getLatDes() + "", "utf-8") + "&" +
                    "LONGITUDDESTINO=" + URLEncoder.encode(reserva.getLongDes() + "", "utf-8") + "&" +
                    "VALORTOTAL=" + reserva.getValorTotal() + "&" +
                    "DIRECCIONFULL=" + URLEncoder.encode(reserva.getRuta().getDireccionInicio(), "utf-8") + "&" +
                    "LATITUD=" + URLEncoder.encode(reserva.getInicio().getLatitud() + "", "utf-8") + "&" +
                    "LONGITUD=" + URLEncoder.encode(reserva.getInicio().getLongitud() + "", "utf-8");
            Enlace.RespuetaHTTP respueta;
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ArrayList<O_Reserva> TaaerReservasCliente(int id) {
        String params = "TIPO=reserva&OP=traerreservascliente&ID=" + id;
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_Reserva> lista;

        try {
            lista = new ArrayList<>();
            respueta = new Enlace().execute(params).get();
            Log.d("HTTP RESPONSE DATA", respueta.getRespuesta());
            if (respueta.getRespuesta().equals("0")) {
                return new ArrayList<>();
            } else {
                String[] filas = respueta.getRespuesta().split("%%");
                for (String fila :
                        filas) {
                    JSONArray r = new JSONArray(fila);
                    O_Reserva aux = new O_Reserva();
                    aux.setID(r.getInt(0));
                    aux.setVehiculo(new O_Vehiculo(
                            r.getInt(1),
                            r.getString(2),
                            r.getString(3),
                            r.getDouble(4),
                            r.getDouble(5),
                            r.getDouble(6),
                            r.getInt(7),
                            r.getString(8),
                            r.getInt(9),
                            r.getInt(10),
                            new O_Usuario(
                                    r.getInt(11),
                                    r.getString(12),
                                    r.getString(13),
                                    r.getString(14),
                                    r.getString(15),
                                    new O_Pais(
                                            r.getInt(16),
                                            r.getString(17),
                                            r.getString(18)
                                    )
                            )
                    ));
                    aux.setTipo(r.getInt(19));
                    aux.setEstado(new O_EstadoReserva(
                            r.getInt(20),
                            r.getString(21),
                            r.getString(22)
                    ));
                    aux.setFecha(r.getString(23));
                    aux.setInicio(new O_Direccion(
                            r.getString(24),
                            r.getDouble(25),
                            r.getDouble(26),
                            r.getInt(27)
                    ));
                    aux.setLatDes(r.getDouble(28));
                    aux.setLongDes(r.getDouble(29));
                    aux.setValorTotal(r.getInt(30));
                    lista.add(aux);
                }
                String inicios = "", destinos = "";
                for (O_Reserva reserva : lista) {
                    inicios += "|" + reserva.getInicio().getLatitud() + "," + reserva.getInicio().getLongitud();
                    destinos += "|" + reserva.getLatDes() + "," + reserva.getLongDes();
                }
                Log.d("INICIOS", inicios);
                Log.d("DESTINOS", destinos);
                //System.exit(0);

                //O_Ruta ruta = Funciones.CalcularDistancia(reservas.get(position));
                //reservas.get(position).setRuta(ruta);
                return lista;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static int CambiarClave(O_Usuario usuario_old, O_Usuario usuario_new) {
        String params = null;
        try {
            params = "TIPO=usuario&OP=cambiarclave&IDUSUARIO=" + usuario_old.getID() +
                    "&CLAVEACTUAL=" + URLEncoder.encode(usuario_old.getClave(), "UTF-8") +
                    "&CLAVENUEVA=" + URLEncoder.encode(usuario_new.getClave(), "UTF-8");
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
