package cl.inefable.jazb.inefable.Modelo.DATA;

public class O_Pais {
    private int ID;
    private String Nombre;
    private String Carrier;

    public O_Pais(int ID, String nombre, String carrier) {
        this.ID = ID;
        Nombre = nombre;
        Carrier = carrier;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCarrier() {
        return Carrier;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
