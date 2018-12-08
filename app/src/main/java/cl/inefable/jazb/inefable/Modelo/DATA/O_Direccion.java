package cl.inefable.jazb.inefable.Modelo.DATA;

import java.io.Serializable;

public class O_Direccion implements Serializable {
    private String Calle;
    private int Numero;
    private String Ciudad, Region, Pais;

    private String Nombre;
    private double Latitud, Longitud;
    private int IDUsuario;

    public String getNombre() {
        return Nombre;
    }

    public O_Direccion(String nombre, double latitud, double longitud, int IDUsuario) {
        Nombre = nombre;
        Latitud = latitud;
        Longitud = longitud;
        this.IDUsuario = IDUsuario;
    }

    public O_Direccion(String calle, int numero, String ciudad, String region, String pais, double latitud, double longitud, int IDUsuario) {
        Calle = calle;
        Numero = numero;
        Ciudad = ciudad;
        Region = region;
        Pais = pais;
        Latitud = latitud;
        Longitud = longitud;
        this.IDUsuario = IDUsuario;
    }

    public O_Direccion() {

    }

    public String getCalle() {
        return Calle;
    }

    public int getNumero() {
        return Numero;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public String getRegion() {
        return Region;
    }

    public String getPais() {
        return Pais;
    }

    public double getLatitud() {
        return Latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }
}
