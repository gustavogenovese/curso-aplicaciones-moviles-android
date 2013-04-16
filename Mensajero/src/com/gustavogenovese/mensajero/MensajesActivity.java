package com.gustavogenovese.mensajero;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class MensajesActivity extends Activity implements OnItemClickListener{

	private final String TAG = "MensajesActivity";

	private Button botonNuevoMensaje;
	private ListView listView;
	private Button botonActualizar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensajes);

		listView = (ListView)findViewById(R.id.listViewMensajes);
		Cursor cursor = ((MensajeroApp)getApplication()).getMensajeroDB().getAllMessages();
		listView.setAdapter(getListViewAdapter(cursor));

		botonNuevoMensaje = (Button)findViewById(R.id.boton_nuevo_mensaje);

		botonNuevoMensaje.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MensajesActivity.this, NuevoMensajeActivity.class);
				startActivity(intent);
			}
		});

		botonActualizar = (Button)findViewById(R.id.boton_actualizar);
		botonActualizar.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				((SimpleCursorAdapter)listView.getAdapter()).getCursor().requery();
			}
		});

		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, ServicioMensajes.class);
		String token = ((MensajeroApp)getApplication()).getToken();
		intent.putExtra("token", token);

		switch (item.getItemId()){
		case R.id.iniciar_servicio_mensajes:
			startService(intent);
			return true;

		case R.id.detener_servicio_mensajes:
			stopService(intent);
			return true;

		default:
			return false;
		}
	}

	private ListAdapter getListViewAdapter(Cursor c){

		final String[] from = {MensajeroDB.TABLA_MENSAJES_FECHA, MensajeroDB.TABLA_MENSAJES_DE, MensajeroDB.TABLA_MENSAJES_MENSAJE};
		final int[] to = {R.id.textViewFecha, R.id.textViewDe, R.id.textViewMensaje};

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listview_item_mensaje, c, from, to);

		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex != cursor.getColumnIndex(MensajeroDB.TABLA_MENSAJES_FECHA))
					return false;

				CharSequence fecha = DateUtils.getRelativeTimeSpanString(cursor.getLong(columnIndex));
				((TextView)view).setText(fecha);
				return true;
			}
		});
		return adapter;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		Cursor c = (Cursor) ((SimpleCursorAdapter)listView
								.getAdapter()).getItem(position);
		String de = c.getString(c.getColumnIndex(MensajeroDB.TABLA_MENSAJES_DE));
		long milis = c.getLong(c.getColumnIndex(MensajeroDB.TABLA_MENSAJES_FECHA));
		Date fecha = new Date(milis);

		Log.i(TAG, "De: " + de);
		Log.i(TAG, "Fecha: " + fecha.toString());
		Intent intent = new Intent(this, MensajeActivity.class);
		intent.putExtra("de", de);
		intent.putExtra("fecha", fecha.toString());
		startActivity(intent);
	}
}
