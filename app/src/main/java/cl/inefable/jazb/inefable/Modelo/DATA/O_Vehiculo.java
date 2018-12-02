package cl.inefable.jazb.inefable.Modelo.DATA;

import java.io.Serializable;

public class O_Vehiculo implements Serializable {
    private int ID;
    private String Patente;
    private String Marca;
    private double Altura, Largo, Ancho;
    private int CargaMax;
    private String Tipo;
    private int Valor;
    private int Propietario;
    private int Disponibilidad;
    private double Valoracion;
    private double cantidadValoraciones;
    private int ValorBase;

    public double getValoracion() {
        return Valoracion;
    }

    public double getCantidadValoraciones() {
        return cantidadValoraciones;
    }

    public O_Vehiculo(int ID, String patente, String marca, double altura, double largo, double ancho, int cargaMax, String tipo, int valor, int propietario, int disponibilidad, double valoracion, double cantidadValoraciones, int valorbase) {
        this.ID = ID;
        Patente = patente;
        Marca = marca;
        Altura = altura;
        Largo = largo;
        Ancho = ancho;
        CargaMax = cargaMax;
        Tipo = tipo;
        Valor = valor;
        Propietario = propietario;
        Disponibilidad = disponibilidad;
        Valoracion = valoracion;
        this.cantidadValoraciones = cantidadValoraciones;
        ValorBase = valorbase;
    }

    public O_Vehiculo(int ID, String patente, String marca, double altura, double largo, double ancho, int cargaMax, String tipo, int valor, int propietario, int disponibilidad) {
        this.ID = ID;
        Patente = patente;
        Marca = marca;
        Altura = altura;
        Largo = largo;
        Ancho = ancho;
        CargaMax = cargaMax;
        Tipo = tipo;
        Valor = valor;
        Propietario = propietario;
        Disponibilidad = disponibilidad;
    }

    public O_Vehiculo(String patente, String marca, double altura, double largo, double ancho, int cargaMax, String tipo, int valor, int propietario) {
        Patente = patente;
        Marca = marca;
        Altura = altura;
        Largo = largo;
        Ancho = ancho;
        CargaMax = cargaMax;
        Tipo = tipo;
        Valor = valor;
        Propietario = propietario;
    }

    public int getID() {
        return ID;
    }

    public String getPatente() {
        return Patente;
    }

    public String getMarca() {
        return Marca;
    }

    public double getAltura() {
        return Altura;
    }

    public double getLargo() {
        return Largo;
    }

    public double getAncho() {
        return Ancho;
    }

    public int getCargaMax() {
        return CargaMax;
    }

    public String getTipo() {
        return Tipo;
    }

    public int getValor() {
        return Valor;
    }

    public int getValorBase() {
        return ValorBase;
    }

    public int getPropietario() {
        return Propietario;
    }

    public int getDisponibilidad() {
        return Disponibilidad;
    }
}
