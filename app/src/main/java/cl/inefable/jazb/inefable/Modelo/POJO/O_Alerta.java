package cl.inefable.jazb.inefable.Modelo.POJO;

import cl.inefable.jazb.inefable.R;

public class O_Alerta {

    public static int RES_ICO_CORRECTO = R.drawable.ico_correcto;
    public static int RES_ICO_INFO = R.drawable.ico_info;
    public static int RES_ICO_PRECAUCION = R.drawable.ico_precaucion;
    public static int RES_ICO_ERROR = R.drawable.ico_error;

    public static int TIPO_CORRECTO = R.color.Correcto;
    public static int TIPO_INFO = R.color.Info;
    public static int TIPO_PRECAUCION = R.color.Precaucion;
    public static int TIPO_ERROR = R.color.Error;

    private int Tipo;
    private String Titulo;
    private String Mensaje;
    private boolean Infinito;
    private int Duracion;
    private int Icono;

    public int getTipo() {
        return Tipo;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public boolean isInfinito() {
        return Infinito;
    }

    public int getDuracion() {
        return Duracion;
    }

    public int getIcono() {
        return Icono;
    }

    public O_Alerta(int tipo, String titulo, String mensaje, boolean infinito, int duracion, int icono) {

        Tipo = tipo;
        Titulo = titulo;
        Mensaje = mensaje;
        Infinito = infinito;
        Duracion = duracion;
        Icono = icono;
    }
}
