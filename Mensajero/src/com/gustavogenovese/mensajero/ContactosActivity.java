package com.gustavogenovese.mensajero;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactosActivity extends Activity {

	private final String TAG = "ContactosActivity";

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactos);

		listView = (ListView) findViewById(R.id.listViewContactos);
		final List<Contacto> contactos = obtenerContactos();
		List<String> contactosString = obtenerListaContactos(contactos);

		listView.setAdapter(new ArrayAdapter<String>
								(this,
								 android.R.layout.simple_list_item_1,
								 contactosString));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "Posicion seleccionada: " + position);

				Intent resultado = new Intent();
				resultado.putExtra("numero", contactos.get(position).telefono);
				setResult(RESULT_OK, resultado);
				finish();
			}
		});
	}

	private List<String> obtenerListaContactos(List<Contacto> contactos) {
		List<String> strings = new LinkedList<String>();
		if (contactos == null || contactos.isEmpty())
			return strings;

		for (Contacto c: contactos){
			strings.add(c.nombre + "(" + c.telefono + ")");
		}
		return strings;
	}

	private List<Contacto> obtenerContactos(){
		List<Contacto> contactos = new LinkedList<Contacto>();

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String nombre = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				Contacto c = new Contacto();
				c.nombre = nombre;

				if (Integer.parseInt(cur.getString(
						cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur =
						cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			 		    null,
			 		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
			 		    new String[] {id}, null);

		 	        while (pCur.moveToNext()) {
		 	        	c.telefono = pCur.getString(
		 	        			pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		 	        	contactos.add(c);
		 	        	break;
		 	        }
		 	        pCur.close();
				}
			}
		}
		cur.close();
		return contactos;
	}


}
