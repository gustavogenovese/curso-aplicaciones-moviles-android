package com.gustavogenovese.mensajero;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import android.app.Service;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServicioMensajes extends Service{

	private final static String TAG = "ServicioMensajes";
	private final static int SEGUNDOS = 30;
	private boolean ejecutando = false;


	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		ejecutando = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ejecutando = true;
		final String token = intent.getExtras().getString("token");

		new Thread(){

			@Override
			public void run() {
				while (ejecutando){
					String id = ((MensajeroApp)getApplication())
									.getMensajeroDB().idUltimoMensajeGuardado();

					AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");

					String url = MensajeroApp.SERVER_URL + MensajeroApp.MENSAJES_ENDPOINT;
					url = url.replace("{token}", token);
					url = url.replace("{idultimomensaje}", id != null ? id : "");

					HttpPost httpPost = new HttpPost(url);

					Log.i(TAG, "Recibiendo mensajes desde " + url);
					try {
						HttpResponse respuesta = httpClient.execute(httpPost);
						int status = respuesta.getStatusLine().getStatusCode();
						Log.i(TAG, "Respuesta  del servidor: " + status);

						if (status == 200){
							String mensajesJson = Utils.inputStreamToString(respuesta.getEntity().getContent());
							Log.d(TAG, mensajesJson);

							Type tipoDatos = new TypeToken<List<Mensaje>>(){}.getType();

							Gson gson = new Gson();
							List<Mensaje> mensajes = gson.fromJson(mensajesJson, tipoDatos);
							for (Mensaje m: mensajes){
				 				Log.d(TAG, String.format("%s (%s): %s", m.getDe(), m.getFecha(), m.getMensaje()));
								((MensajeroApp)getApplication()).getMensajeroDB().guardarMensaje(m);
							}
						}
						Thread.sleep(SEGUNDOS * 1000);
					} catch (IOException e) {
						Log.e(TAG, "Error en la peticion HTTP", e);

					} catch (InterruptedException e) {
						Log.d(TAG, "Servicio de mensajes interrumpido", e);

					} finally {
						httpClient.close();
					}
				}
			}

		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
