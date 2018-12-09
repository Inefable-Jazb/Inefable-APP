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
                    "IDUSUARIO=" + reserva.getSolicitante().getID() + "&" +
                    "IDCAMION=" + reserva.getVehiculo().getID() + "&" +
                    "TIPORESERVA=1&" +
                    "DIRECCION_ORIGEN=" + URLEncoder.encode(reserva.getDireccionInicio() + "", "utf-8") + "&" +
                    "LATITUD_ORIGEN=" + URLEncoder.encode(reserva.getLatInicio() + "", "utf-8") + "&" +
                    "LONGITUD_ORIGEN=" + URLEncoder.encode(reserva.getLongInicio() + "", "utf-8") + "&" +
                    "DIRECCION_DESTINO=" + URLEncoder.encode(reserva.getDireccionDestino(), "utf-8") + "&" +
                    "LATITUD_DESTINO=" + URLEncoder.encode(reserva.getLatDestino() + "", "utf-8") + "&" +
                    "LONGITUD_DESTINO=" + URLEncoder.encode(reserva.getLongDestino() + "", "utf-8") + "&" +
                    "VALORTOTAL=" + reserva.getValorTotal() + "&" +
                    "DISTANCIA=" + URLEncoder.encode(reserva.getDistancia() + "", "utf-8") + "&" +
                    "DURACION=" + URLEncoder.encode(reserva.getDuracion() + "", "utf-8");
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

                    O_Reserva reserva = new O_Reserva(
                            r.getInt(0),
                            new O_Usuario(
                                    r.getInt(1),
                                    r.getString(2),
                                    r.getString(3),
                                    r.getString(4),
                                    r.getString(5),
                                    new O_Pais(
                                            r.getInt(6),
                                            r.getString(7),
                                            r.getString(8))
                            ),
                            new O_Vehiculo(
                                    r.getInt(9),
                                    r.getString(10),
                                    r.getString(11),
                                    r.getDouble(12),
                                    r.getDouble(13),
                                    r.getDouble(14),
                                    r.getInt(15),
                                    r.getString(16),
                                    r.getInt(17),
                                    r.getInt(18),
                                    new O_Usuario(
                                            r.getInt(19),
                                            r.getString(20),
                                            r.getString(21),
                                            r.getString(22),
                                            r.getString(23),
                                            new O_Pais(
                                                    r.getInt(24),
                                                    r.getString(25),
                                                    r.getString(26)
                                            )
                                    )
                            ),
                            r.getInt(27),
                            new O_EstadoReserva(
                                    r.getInt(28),
                                    r.getString(29),
                                    r.getString(30)
                            ),
                            r.getString(31),
                            r.getString(32),
                            r.getDouble(33),
                            r.getDouble(34),
                            r.getString(35),
                            r.getDouble(36),
                            r.getDouble(37),
                            r.getInt(38),
                            r.getInt(39),
                            r.getInt(40)
                    );
                    lista.add(reserva);
                }
                return lista;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static int AceptarReserva(O_Reserva reserva) {
        String params = null;
        params = "TIPO=usuario&OP=aceptarreserva" +
                "&IDRESERVA=" + reserva.getID();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public static int RechazarReserva(O_Reserva reserva) {
        String params = null;
        params = "TIPO=usuario&OP=rechazarreserva" +
                "&IDRESERVA=" + reserva.getID();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public static ArrayList<O_Reserva> TaaerReservasPendientesConductor(O_Usuario usuario) {
        String params = "TIPO=reserva&OP=traerreservaspendienteconductor" +
                "&USUARIOID=" + usuario.getID();
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

                    O_Reserva reserva = new O_Reserva(
                            r.getInt(0),
                            new O_Usuario(
                                    r.getInt(1),
                                    r.getString(2),
                                    r.getString(3),
                                    r.getString(4),
                                    r.getString(5),
                                    new O_Pais(
                                            r.getInt(6),
                                            r.getString(7),
                                            r.getString(8))
                            ),
                            new O_Vehiculo(
                                    r.getInt(9),
                                    r.getString(10),
                                    r.getString(11),
                                    r.getDouble(12),
                                    r.getDouble(13),
                                    r.getDouble(14),
                                    r.getInt(15),
                                    r.getString(16),
                                    r.getInt(17),
                                    r.getInt(18),
                                    new O_Usuario(
                                            r.getInt(19),
                                            r.getString(20),
                                            r.getString(21),
                                            r.getString(22),
                                            r.getString(23),
                                            new O_Pais(
                                                    r.getInt(24),
                                                    r.getString(25),
                                                    r.getString(26)
                                            )
                                    )
                            ),
                            r.getInt(27),
                            new O_EstadoReserva(
                                    r.getInt(28),
                                    r.getString(29),
                                    r.getString(30)
                            ),
                            r.getString(31),
                            r.getString(32),
                            r.getDouble(33),
                            r.getDouble(34),
                            r.getString(35),
                            r.getDouble(36),
                            r.getDouble(37),
                            r.getInt(38),
                            r.getInt(39),
                            r.getInt(40)
                    );
                    lista.add(reserva);
                }
                return lista;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static int ComenzarReserva(O_Reserva reserva) {
        String params = null;
        params = "TIPO=usuario&OP=comenzarreserva" +
                "&IDRESERVA=" + reserva.getID();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public static int FinalizarReserva(O_Reserva reserva) {
        String params = null;
        params = "TIPO=usuario&OP=finalizarreserva" +
                "&IDRESERVA=" + reserva.getID();
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
