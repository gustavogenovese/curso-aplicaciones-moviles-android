package com.gustavogenovese.mensajero;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	private final String TAG="GCMIntentService";

	private String tokenRecibido;
	@Override
	protected void onRegistered(Context contexto, String token) {
		Log.i(TAG, "Registrado en google con token " + token);
		tokenRecibido = token;
		new EnviarTokenServidor().execute();
	}
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Mensaje recibido");
		String tipoMensaje = intent.getExtras().getString("tipoMensaje");
		String mensajeId = intent.getExtras().getString("mensajeId");

		//TODO: pedir mensajes al servidor!
	}
	@Override
	protected void onError(Context arg0, String error) {
		Log.e(TAG, "Error gcm: " + error);
	}
	@Override
	protected void onUnregistered(Context arg0, String token) {
		Log.w(TAG, "OnUnregistered: " + token);
	}

	class EnviarTokenServidor extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			boolean exito;
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");

			String url = MensajeroApp.SERVER_URL +
							MensajeroApp.REGISTRAR_TOKEN_ENDPOINT;
			url = url.replace("{token}", ((MensajeroApp)getApplication()).getToken()) ;
			url = url.replace("{tokenpush}", tokenRecibido);
			HttpPost httpPost = new HttpPost(url);

			Log.i(TAG, "Enviando token a " + url);
			try {
				HttpResponse respuesta = httpClient.execute(httpPost);
				int status = respuesta.getStatusLine().getStatusCode();
				Log.i(TAG, "Respuesta  del servidor: " + status);
				exito = status == 200;

			} catch (IOException e) {
				Log.e(TAG, "Error en la peticion HTTP", e);
			}finally{
				httpClient.close();
			}
			return null;
		}
	}
}
