package cl.inefable.jazb.inefable.Controlador.Servicios;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import cl.inefable.jazb.inefable.R;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class S_Notificador extends Service {


    private boolean Continue = false;
    private String URL_NOTIFICATION_LISTENER = "https://inefable.cl/BD/API.php?TIPO=reserva&OP=traeridreservaconductor&CONDUCTOR=";
    private ArrayList<String> reservas;
    private int NOTIFICACIONES_CONDUCTOR = 100;
    //private Comprobador c;
    private Handler BackgroundTask;

    @Override
    public void onCreate() {
        Notificaciones_Conductor();
        super.onCreate();
    }

    private Runnable handler_Looper(final int idConductor) {
        return new Runnable() {
            @Override
            public void run() {
                if (Continue) {
                    String resultado = "";
                    try {
                        resultado = new Comprobador().execute(String.valueOf(idConductor)).get();
                        if (resultado != null) {
                            for (String i : resultado.split("%%")) {
                                if (!reservas.contains(i)) {
                                    reservas.add(i);
                                    MandarNotificacionNuevaReservaSolicitada(i, reservas.size());
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BackgroundTask.postDelayed(this, 1000);
                }
            }
        };
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

    @Override
    public void onDestroy() {
        Continue = false;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.cancelAll();
        //c.cancel(true);
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
        reservas = new ArrayList<>();
        Continue = true;
        BackgroundTask = new Handler();
        BackgroundTask.post(handler_Looper(idConductor));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class Comprobador extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                cancel(true);
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... idConductor) {
            if (!isCancelled()) {
                try {
                    String fullURL = URL_NOTIFICATION_LISTENER + idConductor[0];
                    URL url = null;
                    try {
                        url = new URL(fullURL);
                        String resultado = descargarURL(url);
                        Log.d("AAASSSDDD", fullURL + " ");
                        if (resultado != null) {
                            return resultado;
                        } else {
                            throw new IOException("Sin respuesta desde " + fullURL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private String descargarURL(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(300000);
                connection.setConnectTimeout(300000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                stream = connection.getInputStream();
                if (stream != null) {
                    result = leerRespuesta(stream, 50000);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private String leerRespuesta(InputStream stream, int maxLengthResponse) throws IOException, UnsupportedEncodingException {
            Reader r = null;
            r = new InputStreamReader(stream, "UTF-8");
            char[] rawBuffer = new char[maxLengthResponse];
            int tamañoLectura;
            StringBuffer sb = new StringBuffer();
            while (((tamañoLectura = r.read(rawBuffer)) != -1) && maxLengthResponse > 0) {
                if (tamañoLectura > maxLengthResponse) {
                    tamañoLectura = maxLengthResponse;
                }
                sb.append(rawBuffer, 0, tamañoLectura);
                maxLengthResponse -= tamañoLectura;
            }
            return sb.toString();
        }
    }
}
