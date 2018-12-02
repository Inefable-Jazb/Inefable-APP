package cl.inefable.jazb.inefable.Modelo.POJO;

import java.io.Serializable;

public class O_Ruta implements Serializable {
    private String DireccionInicio;
    private String DireccionDestino;
    private String DistanciaAprox;
    private int DistanciaReal;
    private String DuracionAprox;
    private int DuracionReal;

    public String getDireccionInicio() {
        return DireccionInicio;
    }

    public String getDireccionDestino() {
        return DireccionDestino;
    }

    public String getDistanciaAprox() {
        return DistanciaAprox;
    }

    public int getDistanciaReal() {
        return DistanciaReal;
    }

    public String getDuracionAprox() {
        return DuracionAprox;
    }

    public int getDuracionReal() {
        return DuracionReal;
    }

    public O_Ruta(String direccionInicio, String direccionDestino, String distanciaAprox, int distanciaReal, String duracionAprox, int duracionReal) {
        DireccionInicio = direccionInicio;
        DireccionDestino = direccionDestino;
        DistanciaAprox = distanciaAprox;
        DistanciaReal = distanciaReal;
        DuracionAprox = duracionAprox;
        DuracionReal = duracionReal;
    }
}
