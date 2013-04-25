package com.gustavogenovese.mensajero;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MensajeroWidget extends AppWidgetProvider{

	private final String TAG="MensajeroWidget";

	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "onEnabled");
		//se llama cuando se crea la primera instancia del widget
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context,
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		//se llama cada vez que se debe actualizar
		Log.i(TAG, "onUpdate");

		for (int widgetId: appWidgetIds){
			actualizarWidget(context, appWidgetManager, widgetId);
		}
	}

	private void actualizarWidget(Context context,
			AppWidgetManager appWidgetManager, int widgetId){
		String textoMensaje =
				MensajeroApp.getInstance()
								.getMensajeroDB()
								.contenidoUltimoMensajeGuardado();

		RemoteViews vistasRemotas = new RemoteViews(context.getPackageName(),
											R.layout.widget_mensajero);
		vistasRemotas.setTextViewText(R.id.textViewWidget, textoMensaje);

		Intent intentActividad = new Intent(context, LoginActivity.class);
		PendingIntent intent = PendingIntent.getActivity(context, 0, intentActividad, 0);
		vistasRemotas.setOnClickPendingIntent(R.id.textViewWidget, intent);
		appWidgetManager.updateAppWidget(widgetId, vistasRemotas);

		Intent intentActualizar = new Intent("com.gustavogenovese.mensajero.ACTUALIZAR_WIDGET");
		intentActualizar.putExtra("widgetId", widgetId);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
									  	0,
									  	intentActualizar,
									  	PendingIntent.FLAG_UPDATE_CURRENT);
		vistasRemotas.setOnClickPendingIntent(R.id.botonWidget, pendingIntent2);

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		//se llama cada vez que se elimina una instancia del widget
		Log.i(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		//se llama cuando se elimina la ultima instancia del widget
		Log.i(TAG, "onDisabled");
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.i(TAG, "onReceive: " + intent.getAction());

		if (intent.getAction().equals("com.gustavogenovese.mensajero.ACTUALIZAR_WIDGET")) {
	        int widgetId = intent.getIntExtra("widgetId", -1);

	        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

	        if (widgetId != -1) {
	            actualizarWidget(context, widgetManager, widgetId);
	        }
		}
	}
}
