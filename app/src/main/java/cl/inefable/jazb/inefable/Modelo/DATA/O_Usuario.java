package cl.inefable.jazb.inefable.Modelo.DATA;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class O_Usuario implements Serializable {
    private int ID, IDInfo;
    private String Usuario, Clave;

    private String Nombres, Apellidos, Correo, Telefono;
    private int Tipo;
    private O_Pais Pais;

    /**
     * Constructor para fines de verificacion de usuario;
     *
     * @param usuario
     */
    public O_Usuario(String usuario) {
        Usuario = usuario;
    }

    /**
     * Para la creadión de los datos personales del usuario
     *
     * @param ID
     * @param nombres
     * @param apellidos
     * @param correo
     * @param telefono
     */
    public O_Usuario(int ID, String nombres, String apellidos, String correo, String telefono, O_Pais pais) {
        this.ID = ID;
        Nombres = nombres;
        Apellidos = apellidos;
        Correo = correo;
        Telefono = telefono;
        Pais = pais;
    }

    /**
     * Constructor con fnes d Login.
     *
     * @param usuario
     * @param clave
     */
    public O_Usuario(String usuario, String clave) {
        Usuario = usuario;
        Clave = clave;
    }

    /**
     * Constructor con fines de Crear Cuenta.
     *
     * @param usuario
     * @param clave
     */
    public O_Usuario(String usuario, String clave, int tipo) {
        Usuario = usuario;
        Clave = clave;
        Tipo = tipo;
    }

    /**
     * COnstrucctor con fines de representacion de usuario BD
     *
     * @param ID
     * @param usuario
     * @param clave
     */
    public O_Usuario(int ID, String usuario, String clave, int tipo, int idInfo) {
        this.ID = ID;
        Usuario = usuario;
        Clave = clave;
        Tipo = tipo;
        IDInfo = idInfo;
    }

    /**
     * Constructor con fines de usuario completo
     *
     * @param ID
     * @param usuario
     * @param clave
     * @param nombres
     * @param apellidos
     * @param correo
     * @param telefono
     * @param tipo
     */
    public O_Usuario(int ID, String usuario, String clave, int tipo, String nombres, String apellidos, String correo, String telefono) {
        this.ID = ID;
        Usuario = usuario;
        Clave = clave;
        Nombres = nombres;
        Apellidos = apellidos;
        Correo = correo;
        Telefono = telefono;
        Tipo = tipo;
    }

    public O_Usuario(int ID, String clave) {
        this.ID = ID;
        Clave = clave;
    }

    public int getID() {
        return ID;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getClave() {
        return Clave;
    }

    public String getNombres() {
        return Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public int getTipo() {
        return Tipo;
    }

    public int getIDInfo() {
        return IDInfo;
    }

    public O_Pais getPais() {
        return Pais;
    }
}
