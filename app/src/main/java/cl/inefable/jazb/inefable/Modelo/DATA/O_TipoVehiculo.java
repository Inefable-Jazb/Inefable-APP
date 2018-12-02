package cl.inefable.jazb.inefable.Modelo.DATA;

public class O_TipoVehiculo {
    private int ID;
    private String Nombre;
    private String Descripcón;

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcón() {
        return Descripcón;
    }

    public O_TipoVehiculo(int ID, String nombre, String descripcón) {

        this.ID = ID;
        Nombre = nombre;
        Descripcón = descripcón;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
