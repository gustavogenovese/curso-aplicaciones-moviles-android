package com.gustavogenovese.mensajero;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class NetworkChangeReceiver extends BroadcastReceiver {

	private final String TAG = "NetworkChangeReceiver";
	private final static String SENDER_ID="347223524006";
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean sinConexion =
				intent.getBooleanExtra(
							ConnectivityManager.EXTRA_NO_CONNECTIVITY,
							false);
		Log.d(TAG, "Conectado a internet: " + !sinConexion);
		boolean conectado = !sinConexion;

		if (conectado){
			//registrar aplicacion para recibir push notifications
			registrarAplicacion(context);
		}
	}

	public void registrarAplicacion(Context contexto){
		GCMRegistrar.checkDevice(contexto);
		GCMRegistrar.checkManifest(contexto);
		final String regId = GCMRegistrar.getRegistrationId(contexto);
		if (regId.equals("")) {
		  GCMRegistrar.register(contexto, SENDER_ID);
		} else {
		  Log.v(TAG, "Ya registrado");
		}
	}
}