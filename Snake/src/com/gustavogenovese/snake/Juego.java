package com.gustavogenovese.snake;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Juego extends Activity {
	private final String TAG = "Juego";

	private Panel panel;
	private HiloJuego hiloJuego;
	private Vibora vibora;
	private List<Bloque> obstaculos;
	private Bloque objetivo;

	int anchoPantalla, altoPantalla;
	int bloquesAncho, bloquesAlto;
	int anchoBloque, altoBloque;
	private int velocidad;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");
		panel = new Panel(this);
		setContentView(panel);

		handler = new Handler();
		vibora = new Vibora();
		velocidad=250;

		hiloJuego = new HiloJuego();
	}

	private void crearObstaculos(){
		obstaculos = new LinkedList<Bloque>();

		for (int i=0; i<bloquesAncho; i++){
			obstaculos.add(new Bloque(i,0));
			obstaculos.add(new Bloque(i, bloquesAlto-1));
		}

		for (int i=1; i<bloquesAlto-1; i++){
			obstaculos.add(new Bloque(0, i));
			obstaculos.add(new Bloque(bloquesAncho -1, i));
		}
	}
	private void crearObjetivos(){
		int x, y;
		boolean conflicto = false;
		do {
			x=(int)Math.floor(Math.random()*bloquesAncho);
			y= (int)Math.floor(Math.random()*bloquesAlto);
			conflicto=false;
			for (int i=0; i<vibora.tamanio(); i++){
				if (vibora.getBloque(i).x == x && vibora.getBloque(i).y == y){
					conflicto = true;
					break;
				}
			}
			for (Bloque obstaculo: obstaculos){
				if (obstaculo.x == x && obstaculo.y==y){
					conflicto = true;
					break;
				}
			}
		}while(conflicto);
		objetivo = new Bloque(x, y);
	}


	class Panel extends SurfaceView implements SurfaceHolder.Callback{

		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			anchoPantalla = holder.getSurfaceFrame().width();
			altoPantalla = holder.getSurfaceFrame().height();

			bloquesAncho = 16;
			anchoBloque = anchoPantalla / bloquesAncho;

			altoBloque = anchoBloque;
			bloquesAlto = altoPantalla / altoBloque;

			crearObstaculos();
			crearObjetivos();

			if (!hiloJuego.isAlive()){
				hiloJuego = new HiloJuego();
				Log.d(TAG, "Iniciando hilo de juego");
				hiloJuego.start();
			}
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.i(TAG, "Surface destroyed");
			hiloJuego.detener();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.i(TAG, "Surface changed");
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawColor(Color.BLACK);

			Paint pinturaVibora = new Paint();
			pinturaVibora.setColor(Color.RED);
			pinturaVibora.setStrokeWidth(1);

			//dibujar vibora
			for (int i=0; i<vibora.tamanio(); i++){
				Bloque bloque = vibora.getBloque(i);
				canvas.drawRect(
						new Rect(bloque.x * anchoBloque,
								bloque.y * altoBloque,
								(bloque.x + 1) * anchoBloque,
								(bloque.y + 1) * altoBloque),
						pinturaVibora);
			}

			//dibujar obstaculos
			Paint pinturaObstaculos = new Paint();
			pinturaObstaculos.setColor(Color.GRAY);
			pinturaObstaculos.setStrokeWidth(1);
			for (Bloque obstaculo: obstaculos){

				canvas.drawRect(
						new Rect(obstaculo.x * anchoBloque,
								obstaculo.y * altoBloque,
								(obstaculo.x + 1) * anchoBloque,
								(obstaculo.y + 1) * altoBloque),
						pinturaObstaculos);
			}

			//dibujar objetivo
			Paint pinturaObjetivo = new Paint();
			pinturaObjetivo.setColor(Color.BLUE);
			pinturaObjetivo.setStrokeWidth(1);
			canvas.drawRect(
					new Rect(objetivo.x * anchoBloque,
							objetivo.y * altoBloque,
							(objetivo.x + 1) * anchoBloque,
							(objetivo.y + 1) * altoBloque),
					pinturaObjetivo);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			Direccion nuevaDireccion;
			if (event.getY() > 0 && event.getY()<= altoPantalla/3){
				nuevaDireccion = Direccion.ARRIBA;
			}else if (event.getY() > altoPantalla * 2/3){
				nuevaDireccion = Direccion.ABAJO;
			}else{
				if (event.getX()>0 && event.getX()<= anchoPantalla/2){
					nuevaDireccion = Direccion.IZQUIERDA;
				}else{
					nuevaDireccion = Direccion.DERECHA;
				}
			}
			vibora.cambiarDireccion(nuevaDireccion);
			Log.d(TAG, "Nueva direccion: " + nuevaDireccion);
			return super.onTouchEvent(event);
		}
	}

	class HiloJuego extends Thread{
		private boolean ejecutando;
		@Override
		public synchronized void start() {
			ejecutando = true;
			super.start();
		}
		public void detener() {
			ejecutando = false;
		}
		@Override
		public void run() {
			while(ejecutando){
				vibora.avanzar();
				if (vibora.verificarObstaculos(obstaculos)){
					ejecutando = false;
					Log.i(TAG, "Obstaculo!");

					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(Juego.this, "Obstaculo!!",
									Toast.LENGTH_LONG).show();
						}
					});
				}

				if (vibora.verificarObjetivo(objetivo)){
					crearObjetivos();
					velocidad-=25;
				}

				Canvas canvas = panel.getHolder().lockCanvas();
				if (canvas != null) {
					panel.draw(canvas);
					panel.getHolder().unlockCanvasAndPost(canvas);
				}

				try{
					Thread.sleep(velocidad);
				}catch(InterruptedException ex){}
			}
			Log.i(TAG, "Terminando hilo");
		}
	}
}
