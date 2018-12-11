package cl.inefable.jazb.inefable.Modelo.DATA;

import java.io.Serializable;

public class O_Monitoreo implements Serializable {
    private int IDReserva;
    private double Lat, Lon;
    private String UltimaFecha;

    public O_Monitoreo(int IDReserva, double lat, double lon, String ultimaFecha) {
        this.IDReserva = IDReserva;
        Lat = lat;
        Lon = lon;
        UltimaFecha = ultimaFecha;
    }

    public int getIDReserva() {
        return IDReserva;
    }

    public double getLat() {
        return Lat;
    }

    public double getLon() {
        return Lon;
    }

    public String getUltimaFecha() {
        return UltimaFecha;
    }
}
