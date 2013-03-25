package com.gustavogenovese.calculadora;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Calculadora extends Activity {

	private ExpertoCalculadora experto;
	
	
	
	public Calculadora(){
		experto = new ExpertoCalculadora();
	}
	
	private TextView display;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculadora);
		
		display = (TextView) findViewById(R.id.display);
	}

	public void digitoPresionado(View v){
		experto.digitoPresionado(getViewTag(v));
		actualizarDisplay();
	}
	
	public void operacionPresionada(View v){
		experto.operacionPresionada(getViewTag(v));
	}
	
	public void igualPresionado(View v){
		experto.realizarOperacion();
		actualizarDisplay();
	}
	
	public void cancelPresionado(View v){
		experto.limpiarBuffer();
		actualizarDisplay();
	}
	
	private int getViewTag(View v){
		String tag = v.getTag().toString();
		try {
			return Integer.parseInt(tag);
		}catch(NumberFormatException ex){
			return -1;
		}
	}
	
	public void actualizarDisplay(){
		display.setText(Integer.toString(experto.getBuffer()));
	}
}
