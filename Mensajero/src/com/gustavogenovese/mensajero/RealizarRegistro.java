package com.gustavogenovese.mensajero;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class RealizarRegistro extends AsyncTask<String, Void, Boolean>{

	private final static String TAG = "RealizarRegistro";
	
	private LoginActivity login;
	private boolean exito;
	
	public RealizarRegistro(LoginActivity login) {
		super();
		this.login = login;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String usuario=params[0];
		String password=params[1];
		
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		
		String url = MensajeroApp.SERVER_URL + MensajeroApp.REGISTRO_ENDPOINT;
		url = url.replace("{usuario}", usuario);
		url = url.replace("{password}", password);
		HttpPost httpPost = new HttpPost(url);
		
		Log.i(TAG, "Intentando registro de usuario en " + url);
		try {
			HttpResponse respuesta = httpClient.execute(httpPost);
			int status = respuesta.getStatusLine().getStatusCode();
			Log.i(TAG, "Respuesta  del servidor: " + status);
			exito =  status == 201;
			return exito;
		} catch (IOException e) {
			Log.e(TAG, "Error en la peticion HTTP", e);
			return false;
		}finally{
			httpClient.close();
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		login.terminoRegistro();
	}

	public boolean isExito() {
		return exito;
	}

}
