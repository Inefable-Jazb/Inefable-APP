package cl.inefable.jazb.inefable.Modelo.DATA;

import cl.inefable.jazb.inefable.Modelo.POJO.O_Ruta;

import java.io.Serializable;

public class O_Reserva implements Serializable {
    private int ID;
    private O_Usuario Usuario;
    private O_Vehiculo Vehiculo;
    private int Tipo;
    private O_EstadoReserva Estado;
    private String Fecha;
    private O_Direccion Inicio;
    private double latDes, longDes;
    private String CalleDestino, CalleInicio;
    private int ValorTotal;
    private O_Ruta Ruta;

    public O_Reserva() {

    }

    public String getCalleDestino() {
        return CalleDestino;
    }

    public void setCalleDestino(String calleDestino) {
        CalleDestino = calleDestino;
    }

    public String getCalleInicio() {
        return CalleInicio;
    }

    public void setCalleInicio(String calleInicio) {
        CalleInicio = calleInicio;
    }

    public double getLatDes() {
        return latDes;
    }

    public void setLatDes(double latDes) {
        this.latDes = latDes;
    }

    public double getLongDes() {
        return longDes;
    }

    public void setLongDes(double longDes) {
        this.longDes = longDes;
    }

    public int getID() {
        return ID;
    }

    public O_Usuario getUsuario() {
        return Usuario;
    }

    public O_Vehiculo getVehiculo() {
        return Vehiculo;
    }

    public int getTipo() {
        return Tipo;
    }

    public O_EstadoReserva getEstado() {
        return Estado;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUsuario(O_Usuario usuario) {
        this.Usuario = usuario;
    }

    public void setVehiculo(O_Vehiculo vehiculo) {
        this.Vehiculo = vehiculo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public void setEstado(O_EstadoReserva estado) {
        Estado = estado;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setInicio(O_Direccion inicio) {
        Inicio = inicio;
    }

    public String getFecha() {
        return Fecha;
    }

    public O_Direccion getInicio() {
        return Inicio;
    }

    public O_Ruta getRuta() {
        return Ruta;
    }

    public void setRuta(O_Ruta ruta) {
        Ruta = ruta;
    }

    public int getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(int valorTotal) {
        ValorTotal = valorTotal;
    }

}