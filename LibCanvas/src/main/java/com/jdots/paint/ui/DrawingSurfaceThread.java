package com.jdots.paint.ui;

import android.util.Log;

class DrawingSurfaceThread {
	private static final String TAG = DrawingSurfaceThread.class.getSimpleName();
	private Thread internalThread;
	private DrawingSurface drawingSurface;
	private Runnable threadRunnable;
	private boolean running;

	DrawingSurfaceThread(DrawingSurface drawingSurface, Runnable runnable) {
		this.drawingSurface = drawingSurface;
		threadRunnable = runnable;
		internalThread = new Thread(new InternalRunnable(),
				"DrawingSurfaceThread");
		internalThread.setDaemon(true);
	}

	private void internalRun() {
		while (running) {
			threadRunnable.run();
		}
	}

	synchronized void start() {
		Log.d(TAG, "DrawingSurfaceThread.start");
		if (running || threadRunnable == null || internalThread == null
				|| internalThread.getState().equals(Thread.State.TERMINATED)) {
			Log.d(TAG, "DrawingSurfaceThread.start returning");
			return;
		}
		if (!internalThread.isAlive()) {
			running = true;
			internalThread.start();
		}
		drawingSurface.refreshDrawingSurface();
	}

	synchronized void stop() {
		Log.d(TAG, "DrawingSurfaceThread.stop");
		running = false;
		if (internalThread != null) {
			Log.d(TAG, "DrawingSurfaceThread.join");
			while (internalThread.isAlive()) {
				try {
					internalThread.interrupt();
					internalThread.join();
					Log.d(TAG, "DrawingSurfaceThread.stopped");
				} catch (InterruptedException e) {
					Log.e(TAG, "Interrupt while joining DrawingSurfaceThread\n", e);
				}
			}
		}
	}

	private class InternalRunnable implements Runnable {
		@Override
		public void run() {
			Thread.yield();
			internalRun();
		}
	}
}
