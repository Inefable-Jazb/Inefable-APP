package cl.inefable.jazb.inefable.Controlador.Servicios;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import cl.inefable.jazb.inefable.Controlador.Funciones;
import cl.inefable.jazb.inefable.R;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class S_Notificador extends Service {

    private ArrayList<String> reservas;
    private int NOTIFICACIONES_CONDUCTOR = 100;
    private Comprobador c;

    @Override
    public void onCreate() {
        //Notificaciones_Conductor();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
        c.cancel(true);
        super.onDestroy();
    }

    private void Notificaciones_Conductor() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification summaryNotification = new Notification.Builder(this)
                .setContentTitle("Inefable")
                .setContentText("¡Tienes Nuevas Reservas!")
                .setSmallIcon(R.drawable.ic_exchange_icon)
                .setGroup("RESERVAS")
                .setGroupSummary(true)
                .build();
        notificationManager.notify(NOTIFICACIONES_CONDUCTOR, summaryNotification);
    }

    private void send(int i) {
        Notification.Builder not = new Notification.Builder(this);
        not.setSmallIcon(R.drawable.ic_exchange_icon);
        not.setContentTitle("Holaa !!");
        not.setContentText("FInalizado.");
        not.setStyle(new Notification.BigTextStyle().bigText("Te he dejado un lindo mensaje en el LOGCAT :)"));
        not.setGroup("SETTINGS");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        not.setSound(alarmSound);
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify((102 + i), not.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int idConductor = intent.getIntExtra("CONDUCTORID", -1);
        c = new Comprobador();
        c.execute(idConductor + "");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class Comprobador extends AsyncTask<String, String, String> {
        String url = "https://inefable.cl/BD/API.php?TIPO=reserva&OP=traeridreservaconductor&CONDUCTOR=";

        @Override
        protected String doInBackground(String... Usuario) {
            reservas = new ArrayList<>();
            Log.d("CHECKESSSDSADSD", url + Usuario[0]);
            while (true) {
                if (isCancelled()) {
                    Log.d("CANCELADO PETE", "");
                    return "";
                }
                try {
                    String fullURL = null;
                    fullURL = url + Usuario[0];
                    try {
                        URL url = new URL(fullURL);
                        String resultado = Funciones.descargarURL(url);
                        if (resultado != null) {
                            for (String i : resultado.split("%%")) {
                                if (!reservas.contains(i)) {
                                    reservas.add(i);
                                    MandarNotificacionNuevaReservaSolicitada(i, reservas.size());
                                }
                            }
                        } else {
                            throw new IOException("Sin respuesta desde " + fullURL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
        private void MandarNotificacionNuevaReservaSolicitada(String i, int ID) {
            int finalID = ID + NOTIFICACIONES_CONDUCTOR;

            Notificaciones_Conductor();
            Notification.Builder builder = new Notification.Builder(S_Notificador.this);
            builder.setSmallIcon(R.drawable.ic_exchange_icon);
            builder.setGroup("RESERVAS");
            builder.setContentTitle("Nueva Solicitud de Reserva!");
            builder.setContentText("Tienes una nueva solicitud de reserva.");
            builder.setSubText("Reserva: " + i);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);

            NotificationManagerCompat nmc = NotificationManagerCompat.from(S_Notificador.this);
            nmc.notify(finalID, builder.build());
        }
    }
}
