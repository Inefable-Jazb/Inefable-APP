package cl.inefable.jazb.inefable.Modelo.DATA;

import java.io.Serializable;

public class O_EstadoReserva implements Serializable {
    private int ID;
    private String Nombre;
    private String Descripcion;

    public O_EstadoReserva(int ID, String nombre, String descripcion) {
        this.ID = ID;
        Nombre = nombre;
        Descripcion = descripcion;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }
}
