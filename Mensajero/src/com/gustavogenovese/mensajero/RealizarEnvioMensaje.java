package com.gustavogenovese.mensajero;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class RealizarEnvioMensaje extends AsyncTask<String, Void, Boolean> {

	private static String TAG = "RealizarEnvioMensaje";
	
	private NuevoMensajeActivity actividad;
	private boolean exito;

	public RealizarEnvioMensaje(NuevoMensajeActivity actividad) {
		super();
		this.actividad = actividad;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String sesion = params[0];
		String destinatario = params[1];
		String mensaje = params[2];
		
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");

		String url = MensajeroApp.SERVER_URL + MensajeroApp.NUEVO_MENSAJE_ENDPOINT;
		url = url.replace("{token}", sesion);
		url = url.replace("{destinatario}", destinatario);
		HttpPost httpPost = new HttpPost(url);

		try {
			Log.i(TAG, "Enviando mensaje con url: " + url);
			
			List<NameValuePair> parametros = new LinkedList<NameValuePair>();
			parametros.add(new BasicNameValuePair("mensaje", mensaje));
	        httpPost.setEntity(new UrlEncodedFormEntity(parametros));

			HttpResponse respuesta = httpClient.execute(httpPost);
			int status = respuesta.getStatusLine().getStatusCode();
			Log.i(TAG, "Respuesta  del servidor: " + status);
			exito = status == 201;
			return exito;
		} catch (IOException e) {
			Log.e(TAG, "Error en la peticion HTTP", e);
			return false;
		} finally {
			httpClient.close();
		}
	}

	public boolean isExito() {
		return exito;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		actividad.terminoEnvio(exito);
	}
}
