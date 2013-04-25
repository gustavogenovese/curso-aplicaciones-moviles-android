package com.gustavogenovese.mensajero;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MensajeroDB {

	private final static String TAG = "MensajeroDB";

	private final static String NOMBRE_DB = "MensajeroDB";
	private final static int VERSION_DB = 1;

	public final static String TABLA_MENSAJES = "mensajes";
	public final static String TABLA_MENSAJES_ID = "_id";
	public final static String TABLA_MENSAJES_DE = "de";
	public final static String TABLA_MENSAJES_MENSAJE = "mensaje";
	public final static String TABLA_MENSAJES_FECHA = "fecha";

	private Context context;
	private AyudanteSQL ayudanteSQL;
	private DateFormat formatoFechas;

	@SuppressLint("SimpleDateFormat")
	public MensajeroDB(Context context){
		this.context = context;
		formatoFechas = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ayudanteSQL = new AyudanteSQL();
	}

	public void guardarMensaje(Mensaje m){
		SQLiteDatabase db = ayudanteSQL.getWritableDatabase();
		ContentValues valores = new ContentValues();
		valores.put(TABLA_MENSAJES_ID, m.getId());
		valores.put(TABLA_MENSAJES_DE, m.getDe());
		valores.put(TABLA_MENSAJES_MENSAJE, m.getMensaje());

		try {
			Date fecha = formatoFechas.parse(m.getFecha());
			valores.put(TABLA_MENSAJES_FECHA,fecha.getTime());
		}catch(ParseException ex){
			Log.e(TAG, "Error parseando fecha de mensaje", ex);
			return;
		}
		db.insert(TABLA_MENSAJES, null, valores);
	}

	public String idUltimoMensajeGuardado(){
		SQLiteDatabase db = ayudanteSQL.getReadableDatabase();
		Cursor c = db.query(TABLA_MENSAJES, null, null, null, null, null, TABLA_MENSAJES_FECHA + " desc", "1" );
		while (c.moveToNext()){
			String mensajeId = c.getString(c.getColumnIndex(TABLA_MENSAJES_ID));
			c.close();
			return mensajeId;
		}
		c.close();
		return null;
	}

	public String contenidoUltimoMensajeGuardado(){
		SQLiteDatabase db = ayudanteSQL.getReadableDatabase();
		Cursor c = db.query(TABLA_MENSAJES, null, null, null, null, null,
								TABLA_MENSAJES_FECHA + " desc", "1" );
		while (c.moveToNext()){
			String mensaje = c.getString(c.getColumnIndex(TABLA_MENSAJES_MENSAJE));
			c.close();
			return mensaje;
		}
		c.close();
		return "No hay mensajes";
	}

	public Cursor getAllMessages(){
		SQLiteDatabase db = ayudanteSQL.getReadableDatabase();
		Cursor c = db.query(TABLA_MENSAJES, null, null, null, null, null, TABLA_MENSAJES_FECHA + " desc");
		return c;
	}

	class AyudanteSQL extends SQLiteOpenHelper{

		public AyudanteSQL() {
			super(context, NOMBRE_DB, null, VERSION_DB);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "onCreate");
			String sql = String.format("create table %s " +
									   "(%s varchar primary key, " +
									   "%s varchar," +
									   "%s text," +
									   "%s int)",
										TABLA_MENSAJES,
										TABLA_MENSAJES_ID,
										TABLA_MENSAJES_DE,
										TABLA_MENSAJES_MENSAJE,
										TABLA_MENSAJES_FECHA);
			Log.d(TAG, "SQL de creacion: " + sql);
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = "drop table if exists " + TABLA_MENSAJES;
			db.execSQL(sql);
			onCreate(db);
		}
	}
}
