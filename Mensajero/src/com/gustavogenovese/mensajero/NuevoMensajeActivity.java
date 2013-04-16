package com.gustavogenovese.mensajero;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuevoMensajeActivity extends Activity {
	private final String TAG = "NuevoMensajeActivity";

	private EditText editTextDestinatario;
	private EditText editTextMensaje;
	private Button botonEnviar;
	private Button botonContactos;
	private Button botonContactos2;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.activity_nuevo_mensaje);
		editTextDestinatario = (EditText) findViewById(R.id.editTextDestinatario);
		editTextMensaje = (EditText) findViewById(R.id.editTextMensaje);
		botonEnviar = (Button) findViewById(R.id.botonEnviar);

		botonEnviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(NuevoMensajeActivity.this,
						"Nuevo mensaje", "Enviando mensaje");
				new RealizarEnvioMensaje(NuevoMensajeActivity.this).execute(
						((MensajeroApp) getApplication()).getToken(),
						editTextDestinatario.getText().toString(),
						editTextMensaje.getText().toString());
			}
		});

		botonContactos = (Button) findViewById(R.id.botonContactos);
		botonContactos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NuevoMensajeActivity.this,
						ContactosActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		botonContactos2 = (Button) findViewById(R.id.botonContactos2);
		botonContactos2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 2);
			}
		});
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				editTextDestinatario.setText(
										data.getExtras().getString(
										"numero"));
			}
		} else if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = managedQuery(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					String tieneTelefono = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (tieneTelefono.equals("1")) {
						Cursor telefonos = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						telefonos.moveToFirst();
						String numero = telefonos
								.getString(telefonos
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						telefonos.close();
						editTextDestinatario.setText(numero);
					}
				}
			}
		}
	}

	public void terminoEnvio(boolean exito) {
		if (exito) {
			progressDialog.cancel();
			finish();
		} else {
			Toast.makeText(this, "Error al enviar", Toast.LENGTH_LONG).show();
		}
	}
}
