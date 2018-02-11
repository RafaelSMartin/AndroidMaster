package com.rafaels.notificacionpersonalizada;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String ACCION_DEMO = "com.rafaels.notificacionpersonalizada.ACCION_DEMO";
    public static final String EXTRA_PARAM = "com.rafaels.notificacionpersonalizada.EXTRA_PARAM";
    private int contador = 0;


    private static final int ID_NOTIFICACION = 1;
    static final String ID_CANAL = "channel_id";
    private NotificationManager notificManager;
    private NotificationCompat.Builder notificacion;
    private RemoteViews remoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.reproducir, android.R.drawable.ic_media_play);
        remoteViews.setImageViewResource(R.id.imagen, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.titulo, "Notificatión personalizada");
        remoteViews.setTextColor(R.id.titulo, Color.BLACK);
        remoteViews.setTextViewText(R.id.texto, "Texto de la notificación.");
        remoteViews.setTextColor(R.id.texto, Color.BLACK);


        Intent intent = new Intent();
        intent.setAction(ACCION_DEMO);
        intent.putExtra(EXTRA_PARAM, "otro parámetro");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.reproducir, pendingIntent);

        notificacion = new NotificationCompat.Builder(this, ID_CANAL)
                .setContent(remoteViews)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificación personalizada");
        notificManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26){
            NotificationChannel channel = new NotificationChannel(ID_CANAL,"Nombre del canal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Descripción del canal");
            notificManager.createNotificationChannel(channel);
        }
        notificManager.notify(ID_NOTIFICACION, notificacion.build());


        IntentFilter filtro = new IntentFilter(ACCION_DEMO);
        registerReceiver(new ReceptorAnuncio(), filtro);


    }

    public class ReceptorAnuncio extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String param = intent.getStringExtra(EXTRA_PARAM);
            Toast.makeText(context, "Parámetro:"+param, Toast.LENGTH_LONG).show();
            contador++;
            remoteViews.setTextViewText(R.id.texto, "Contador: "+contador);
            notificManager.notify(ID_NOTIFICACION, notificacion.build());
        }
    }


}
