package com.gustavogenovese.monedas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VisorCotizacion extends Activity implements OnClickListener{

	private Button botonCerrar;
	
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visor_cotizacion);
		
		textView = (TextView) findViewById(R.id.textViewCotizacion);
		String valor = getIntent().getExtras().getString("valor");
		textView.setText(valor);
		
		botonCerrar = (Button)findViewById(R.id.botonCerrar);
		
		botonCerrar.setOnClickListener(this);
		
		botonCerrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VisorCotizacion.this.finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
