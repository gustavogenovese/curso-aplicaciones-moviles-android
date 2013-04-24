package com.gustavogenovese.prueba2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

	private final String TAG = "Panel";

	private Bitmap mario;

	private ViewThread viewThread;
	private int posX = 10;
	private int posY = 10;

	public Panel(Context context) {
		super(context);

		mario = BitmapFactory.decodeResource(getResources(), R.drawable.mario);

		getHolder().addCallback(this);

		viewThread = new ViewThread();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "Surface created");
		if (!viewThread.isAlive()) {
			viewThread = new ViewThread();
			viewThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Surface destroyed");
		if (viewThread.isAlive()) {
			viewThread.detener();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i(TAG, "Surface changed");
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.BLUE);
		canvas.drawBitmap(mario, posX++, posY++, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		posX = (int) event.getX() - mario.getWidth() / 2;
		posY = (int) event.getY() - mario.getHeight() / 2;
		return super.onTouchEvent(event);
	}

	class ViewThread extends Thread {
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
			while (ejecutando) {
				Canvas canvas = Panel.this.getHolder().lockCanvas();
				if (canvas != null) {
					Panel.this.draw(canvas);
					Panel.this.getHolder().unlockCanvasAndPost(canvas);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e(TAG, "Interrumpido");
				}
			}
			Log.i(TAG, "Terminando hilo");
		}
	}
}
