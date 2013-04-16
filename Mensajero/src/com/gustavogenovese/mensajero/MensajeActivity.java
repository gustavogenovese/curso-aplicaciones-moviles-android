package com.gustavogenovese.mensajero;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MensajeActivity extends Activity {

	private TextView tvDe;
	private TextView tvFecha;

	private Button botonLlamar;
	private Button botonSMS;
	private Button botonBuscar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensaje);

		tvDe = (TextView)findViewById(R.id.textViewDe);
		tvFecha = (TextView)findViewById(R.id.textViewFecha);

		Intent intent = getIntent();
		tvDe.setText(intent.getExtras().getString("de"));
		tvFecha.setText(intent.getExtras().getString("fecha"));

		botonLlamar = (Button)findViewById(R.id.botonLlamar);
		botonSMS = (Button)findViewById(R.id.botonMensaje);
		botonBuscar = (Button)findViewById(R.id.botonBuscarGoogle);

		botonLlamar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent llamadaIntent = new Intent(Intent.ACTION_CALL );
				llamadaIntent.setData(Uri.parse("tel:"+tvDe.getText()));
				startActivity(llamadaIntent);
			}
		});

		botonSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("sms:"+tvDe.getText()));
				smsIntent.putExtra("sms_body", "texto de prueba");
				startActivity(smsIntent);
			}
		});

		botonBuscar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent webIntent = new Intent(Intent.ACTION_VIEW);
				webIntent.setData(Uri.parse("http://www.google.com/search?q=" + tvDe.getText()));
				startActivity(webIntent);
			}
		});
	}

}
