package com.gustavogenovese.monedas;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ConversorMonedasActivity extends Activity {

	private EditText texto;
	private TextView textView;
	private RadioGroup eleccion;
	private double cotizacion = 5.1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversor_monedas);

		texto = (EditText) findViewById(R.id.textoCotizacion);
		textView = (TextView) findViewById(R.id.textView1);
		
		eleccion = (RadioGroup)findViewById(R.id.eleccion);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversor_monedas, menu);
		return true;
	}

	public void calcular(View v) {
		String a = texto.getText().toString();
		try {
			double valor = Double.parseDouble(a);
			double valorCotizado = 0;
			String moneda = "";
			
			switch (eleccion.getCheckedRadioButtonId()){
			case R.id.peso_a_dolar:
				valorCotizado = valor / cotizacion;
				moneda = "U$S";
				break;
				
			case R.id.dolar_a_peso:
				valorCotizado = valor * cotizacion;
				moneda = "$";
				break;
			}
			
			textView.setText(String.format("%s %.02f", moneda, valorCotizado));

		} catch (NumberFormatException ex) {
			Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
			textView.setText("");
		}
	}
}
