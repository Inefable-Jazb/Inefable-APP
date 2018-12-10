package cl.inefable.jazb.inefable.Modelo.DATA;

import java.io.Serializable;

public class O_Valoracion implements Serializable {
    private int ID;
    private float Valoracion;
    private String Comentario;
    private String Fecha;
    private int IDReserva;

    public O_Valoracion(int ID, float valoracion, String comentario, String fecha, int IDReserva) {
        this.ID = ID;
        Valoracion = valoracion;
        Comentario = comentario;
        Fecha = fecha;
        this.IDReserva = IDReserva;
    }

    public O_Valoracion(float valoracion, String comentario, int IDReserva) {
        Valoracion = valoracion;
        Comentario = comentario;
        this.IDReserva = IDReserva;
    }

    public int getID() {
        return ID;
    }

    public float getValoracion() {
        return Valoracion;
    }

    public String getComentario() {
        return Comentario;
    }

    public String getFecha() {
        return Fecha;
    }

    public int getIDReserva() {
        return IDReserva;
    }
}
