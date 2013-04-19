package com.gustavogenovese.mensajero;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MensajeroApp extends Application {

	//public final static String SERVER_URL = "http://192.168.1.60:8080";
	public final static String SERVER_URL = "http://servermensajeria-gcesarmza.rhcloud.com";

	public final static String LOGIN_ENDPOINT = "/servicios/usuarios/login?usuario={usuario}&password={password}";
	public final static String REGISTRO_ENDPOINT = "/servicios/usuarios/registrarusuario?usuario={usuario}&password={password}";

	public final static String NUEVO_MENSAJE_ENDPOINT = "/servicios/mensajes/nuevomensaje?sesion={token}&destinatario={destinatario}";
	public final static String MENSAJES_ENDPOINT = "/servicios/mensajes/listamensajes?sesion={token}&ultimomensajeid={idultimomensaje}";
	public final static String MENSAJES_ENVIADOS_ENDPOINT = "/servicios/mensajes/listamensajesenviados?sesion={token}";

	public final static String REGISTRAR_TOKEN_ENDPOINT=" /servicios/usuarios/registrartoken?sesion={token}&token={tokenpush}";
	private String token;
	@Override
	public void onCreate() {
		super.onCreate();
		mensajeroDB = new MensajeroDB(this);

		new NetworkChangeReceiver().registrarAplicacion(this);
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	private MensajeroDB mensajeroDB;

	public MensajeroDB getMensajeroDB() {
		return mensajeroDB;
	}

	public boolean verificarConexion(){
		ConnectivityManager cm = (ConnectivityManager)
									getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo redActiva = cm.getActiveNetworkInfo();
		return redActiva != null && redActiva.isConnected();
	}

}
