package com.gustavogenovese.monedas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ConversorMonedasActivity extends Activity {

	private EditText texto;
	private TextView textView;
	private RadioGroup eleccion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversor_monedas);

		texto = (EditText) findViewById(R.id.textoCotizacion);
		textView = (TextView) findViewById(R.id.textViewCotizacion);

		eleccion = (RadioGroup) findViewById(R.id.eleccion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversor_monedas, menu);
		return true;
	}

	public void calcular(View v) {
		String a = texto.getText().toString();
		double cotizacion = Double.parseDouble(
				PreferenceManager.getDefaultSharedPreferences(this)
				.getString("cotizacion", "0"));
		
		try {
			double valor = Double.parseDouble(a);
			double valorCotizado = 0;
			String moneda = "";

			switch (eleccion.getCheckedRadioButtonId()) {
			case R.id.peso_a_dolar:
				valorCotizado = valor / cotizacion;
				moneda = "U$S";
				break;

			case R.id.dolar_a_peso:
				valorCotizado = valor * cotizacion;
				moneda = "$";
				break;
			}

			String textoMostrar = String.format("%s %.02f", moneda, valorCotizado);
			textView.setText(textoMostrar);
			
			Intent intent = new Intent(this, VisorCotizacion.class);
			intent.putExtra("valor", textoMostrar);
			startActivity(intent);

		} catch (NumberFormatException ex) {
			Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
			textView.setText("");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, PreferenciasActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
