package cl.inefable.jazb.inefable.Modelo.FUNCIONES;

import android.util.Log;
import cl.inefable.jazb.inefable.Modelo.CONN.Enlace;
import cl.inefable.jazb.inefable.Modelo.DATA.O_TipoVehiculo;
import cl.inefable.jazb.inefable.Modelo.DATA.O_Vehiculo;
import kotlin.Function;
import org.json.JSONArray;

import java.util.ArrayList;

public class F_Vehiculo {
    public static int Crear(O_Vehiculo vehiculo) {
        String params = "TIPO=vehiculo&OP=agregar&PATENTE=" + vehiculo.getPatente() + "&MARCA=" + vehiculo.getMarca() + "&ALTURA=" + vehiculo.getAltura() + "&LARGO=" + vehiculo.getLargo() + "&ANCHO=" + vehiculo.getAncho() + "&CARGAMAX=" + vehiculo.getCargaMax() + "&TIP=" + vehiculo.getTipo() + "&VALOR=" + vehiculo.getValor() + "&PROPIETARIO=" + vehiculo.getPropietario() + "&VALORBASE=" + vehiculo.getValorBase();
        Enlace.RespuetaHTTP respueta;
        try {
            respueta = new Enlace().execute(params).get();
            return Integer.parseInt(respueta.getRespuesta());
        } catch (Exception e) {
            return -1;
        }
    }

    public static ArrayList<O_Vehiculo> TraerVehiculosConductor(int Conductor) {
        String params = "TIPO=vehiculo&OP=traerconductor&USUARIO=" + Conductor;
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_Vehiculo> lista;

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
                    O_Vehiculo aux = new O_Vehiculo(
                            r.getInt(0),
                            r.getString(1),
                            r.getString(2),
                            r.getDouble(3),
                            r.getDouble(4),
                            r.getDouble(5),
                            r.getInt(6),
                            r.getString(7),
                            r.getInt(8),
                            r.getInt(9),
                            r.getInt(10),
                            r.getDouble(12),
                            r.getInt(13),
                            r.getInt(11)
                    );
                    lista.add(aux);
                }
                return lista;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<O_TipoVehiculo> TraerListaTipoCamiones() {
        String params = "TIPO=vehiculo&OP=traertipos";
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_TipoVehiculo> lista;

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
                    O_TipoVehiculo aux = new O_TipoVehiculo(
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

    public static ArrayList<O_Vehiculo> TraerVehiculosCliente(int id) {
        String params = "TIPO=vehiculo&OP=traervehiculoscliente&USUARIO=" + id;
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_Vehiculo> lista;
        ;
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
                    O_Vehiculo aux = new O_Vehiculo(
                            r.getInt(0),
                            r.getString(1),
                            r.getString(2),
                            r.getDouble(3),
                            r.getDouble(4),
                            r.getDouble(5),
                            r.getInt(6),
                            r.getString(7),
                            r.getInt(8),
                            r.getInt(9),
                            r.getInt(10),
                            r.getDouble(12),
                            r.getInt(13),
                            r.getInt(11)

                    );
                    lista.add(aux);
                }
                return lista;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<O_Vehiculo> FiltrarVehiculos(String filtros, int Usuario) {
        String params = "TIPO=vehiculo&OP=traervehiculos" + filtros + "&USUARIO=" + Usuario;
        Enlace.RespuetaHTTP respueta;
        ArrayList<O_Vehiculo> lista;

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
                    O_Vehiculo aux = new O_Vehiculo(
                            r.getInt(0),
                            r.getString(1),
                            r.getString(2),
                            r.getDouble(3),
                            r.getDouble(4),
                            r.getDouble(5),
                            r.getInt(6),
                            r.getString(7),
                            r.getInt(8),
                            r.getInt(9),
                            r.getInt(10),
                            r.getDouble(12),
                            r.getInt(13),
                            r.getInt(11)
                    );
                    lista.add(aux);
                }
                return lista;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
