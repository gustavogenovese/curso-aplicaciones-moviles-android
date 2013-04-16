package com.gustavogenovese.mensajero;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private final static String TAG = "Login";

	private EditText nombreUsuario;
	private EditText password;
	private Button botonLogin;
	private ProgressBar spinner;

	private RealizarLogin realizarLogin;
	private RealizarRegistro realizarRegistro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.activity_login);

		nombreUsuario = (EditText) findViewById(R.id.nombreusuario);
		password = (EditText) findViewById(R.id.password);
		botonLogin = (Button) findViewById(R.id.boton_login);
		spinner = (ProgressBar) findViewById(R.id.spinner);
		botonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				botonLogin.setClickable(false);
				spinner.setVisibility(View.VISIBLE);
				realizarLogin = new RealizarLogin(LoginActivity.this);
				realizarLogin.execute(nombreUsuario.getText().toString(),
						password.getText().toString());
			}
		});
	}

	public void terminoLogin() {
		spinner.setVisibility(View.INVISIBLE);

		if (realizarLogin.isExito()) {
			Toast.makeText(this, "Login exitoso\n" + realizarLogin.getToken(),
					Toast.LENGTH_LONG).show();
			((MensajeroApp)getApplication()).setToken(realizarLogin.getToken());
			
			Intent intent = new Intent(this, MensajesActivity.class);
			startActivity(intent);
			finish();
		} else {
			botonLogin.setClickable(true);
			manejarFalloLogin();
		}
	}

	private void manejarFalloLogin() {
		AlertDialog.Builder constructorDialog = new AlertDialog.Builder(this);
		constructorDialog.setTitle(R.string.login_fallido_pregunta);

		constructorDialog.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						botonLogin.setClickable(false);
						spinner.setVisibility(View.VISIBLE);
						realizarRegistro = new RealizarRegistro(LoginActivity.this);
						realizarRegistro.execute(
								nombreUsuario.getText().toString(), 
								password.getText().toString());
					}
				});

		constructorDialog.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		constructorDialog.create().show();
	}

	public void terminoRegistro() {
		spinner.setVisibility(View.INVISIBLE);

		if (realizarRegistro.isExito()) {
			Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
			botonLogin.performClick();
		} else {
			botonLogin.setClickable(true);
			Toast.makeText(this, "Fallo el registro", Toast.LENGTH_LONG).show();
		}
	}

}
