package com.rafaels.widgetescritorio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Rafael S Martin on 11/02/2018.
 */

public class MiAppWidgetProvider extends AppWidgetProvider {

    // id de anuncio broadCast
    public static final String ACCION_INCR = "com.rafaels.widgetescritorio.ACCION_INCR";
    // tag para extras
    public static final String EXTRA_PARAM = "com.rafaels.widgetescritorio.EXTRA_ID";


    //int[] widgetIds es un array por si hay varios widgets instalados del mismo tipo
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] widgetIds) {
        for (int widgetId: widgetIds) {
            actualizaWidget(context, widgetId);
        }
    }

    public static void actualizaWidget(Context context, int widgetId) {
        int cont = incrementaContador(context, widgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.textView1, "Contador: " + cont);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.analogClock1, pendingIntent);

        intent = new Intent(context, MiAppWidgetProvider.class);
        intent.setAction(ACCION_INCR);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.putExtra(EXTRA_PARAM, "otro parámetro");
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.textView1, pendingIntent);

        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);
    }

    private static int incrementaContador(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("contadores",
                Context.MODE_PRIVATE);
        int cont = prefs.getInt("cont_" + widgetId, 0);
        cont++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cont_" + widgetId, cont);
        editor.commit();
        return cont;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACCION_INCR)) {
            int widgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            String param = intent.getStringExtra(EXTRA_PARAM);
            Toast.makeText(context,"Parámetro:"+param, Toast.LENGTH_LONG).show();
            actualizaWidget(context, widgetId);
        }
        super.onReceive(context, intent);
    }

}
