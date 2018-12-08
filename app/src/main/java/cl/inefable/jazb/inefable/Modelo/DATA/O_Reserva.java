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
    private String DireccionInicio;
    private double LatInicio, LongInicio;
    private String DireccionDestino;
    private double LatDestino, LongDestino;
    private int ValorTotal, Distancia, Duracion;

    /**
     * UTIIZAR PARA CREAR LAS RESERVAS QUE VENGAN DESDE INTERNET
     *
     * @param ID
     * @param usuario
     * @param vehiculo
     * @param tipo
     * @param estado
     * @param fecha
     * @param direccionInicio
     * @param latInicio
     * @param longInicio
     * @param direccionDestino
     * @param latDestino
     * @param longDestino
     * @param valorTotal
     * @param distancia
     * @param duracion
     */
    public O_Reserva(int ID, O_Usuario usuario, O_Vehiculo vehiculo, int tipo, O_EstadoReserva estado, String fecha, String direccionInicio, double latInicio, double longInicio, String direccionDestino, double latDestino, double longDestino, int valorTotal, int distancia, int duracion) {
        this.ID = ID;
        Usuario = usuario;
        Vehiculo = vehiculo;
        Tipo = tipo;
        Estado = estado;
        Fecha = fecha;
        DireccionInicio = direccionInicio;
        LatInicio = latInicio;
        LongInicio = longInicio;
        DireccionDestino = direccionDestino;
        LatDestino = latDestino;
        LongDestino = longDestino;
        ValorTotal = valorTotal;
        Distancia = distancia;
        Duracion = duracion;
    }

    /**
     * UTILIZAR DURANTE EL PROCESO DE RESERVA
     */
    public O_Reserva() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public O_Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(O_Usuario usuario) {
        Usuario = usuario;
    }

    public O_Vehiculo getVehiculo() {
        return Vehiculo;
    }

    public void setVehiculo(O_Vehiculo vehiculo) {
        Vehiculo = vehiculo;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public O_EstadoReserva getEstado() {
        return Estado;
    }

    public void setEstado(O_EstadoReserva estado) {
        Estado = estado;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getDireccionInicio() {
        return DireccionInicio;
    }

    public void setDireccionInicio(String direccionInicio) {
        DireccionInicio = direccionInicio;
    }

    public double getLatInicio() {
        return LatInicio;
    }

    public void setLatInicio(double latInicio) {
        LatInicio = latInicio;
    }

    public double getLongInicio() {
        return LongInicio;
    }

    public void setLongInicio(double longInicio) {
        LongInicio = longInicio;
    }

    public String getDireccionDestino() {
        return DireccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        DireccionDestino = direccionDestino;
    }

    public double getLatDestino() {
        return LatDestino;
    }

    public void setLatDestino(double latDestino) {
        LatDestino = latDestino;
    }

    public double getLongDestino() {
        return LongDestino;
    }

    public void setLongDestino(double longDestino) {
        LongDestino = longDestino;
    }

    public int getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(int valorTotal) {
        ValorTotal = valorTotal;
    }

    public int getDistancia() {
        return Distancia;
    }

    public void setDistancia(int distancia) {
        Distancia = distancia;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int duracion) {
        Duracion = duracion;
    }
}