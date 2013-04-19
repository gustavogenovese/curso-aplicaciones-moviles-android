package com.gustavogenovese.pruebagps;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private final String TAG = "MainActivity";

	private static final long DISTANCIA_MINIMA = 1; // Metros
	private static final long TIEMPO_ENTRE_ACTUALIZACIONES = 1000; // Milisegundos

	protected LocationManager locationManager;

	private Button botonUbicacion;
	private TextView textoUbicacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textoUbicacion = (TextView)findViewById(R.id.textoUbicacion);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, // LocationManager.NETWORK_PROVIDER
				TIEMPO_ENTRE_ACTUALIZACIONES, DISTANCIA_MINIMA,
				new EscuchadorUbicacion());

		botonUbicacion = (Button) findViewById(R.id.botonUbicacion);
		botonUbicacion.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Location ubicacion = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (ubicacion != null) {
			String stringUbicacion = String.format(
					"Ubicacion actual: Longitud: %f Latitud: %f",
					ubicacion.getLongitude(), ubicacion.getLatitude());
			Log.i(TAG, stringUbicacion);
			Toast.makeText(this, stringUbicacion, Toast.LENGTH_SHORT).show();
			textoUbicacion.setText(stringUbicacion);
		}else{
			Toast.makeText(this, "No existe ultima ubicacion conocida",
							Toast.LENGTH_SHORT).show();
		}
	}
	class EscuchadorUbicacion implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			String stringUbicacion = String.format(
					"Nueva ubicacion Longitud: %f Latitud: %f",
					location.getLongitude(), location.getLatitude());
			Log.i(TAG, stringUbicacion);
			Toast.makeText(MainActivity.this, stringUbicacion, Toast.LENGTH_SHORT)
					.show();
		}
		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(MainActivity.this, "Estado cambiado: " + s,
					Toast.LENGTH_LONG).show();
		}
		public void onProviderDisabled(String s) {
			Toast.makeText(MainActivity.this, "Proveedor desactivado",
					Toast.LENGTH_LONG).show();
		}
		public void onProviderEnabled(String s) {
			Toast.makeText(MainActivity.this, "Proveedor activado",
					Toast.LENGTH_LONG).show();
		}
	}
}
