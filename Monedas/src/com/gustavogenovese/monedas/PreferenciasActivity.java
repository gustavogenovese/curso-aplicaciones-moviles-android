package com.gustavogenovese.monedas;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PreferenciasActivity 
			extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);

		
		EditTextPreference pref = (EditTextPreference) getPreferenceScreen()
				.findPreference("cotizacion");
		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				String nuevoValor = newValue.toString();
				try {
					Double.parseDouble(nuevoValor);
					return true;
				} catch (NumberFormatException ex) {
					Toast.makeText(PreferenciasActivity.this,
							"Error en este valor", Toast.LENGTH_LONG).show();
					return false;
				}
			}
		});
	}
}
