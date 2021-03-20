package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.Log;

import com.jdots.paint.command.Command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import androidx.annotation.VisibleForTesting;

public abstract class BaseCommand implements Command {
	private static final String TAG = BaseCommand.class.getSimpleName();
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public Paint paint;
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public Bitmap bitmap;
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public File fileToStoredBitmap;

	public BaseCommand() {
	}

	public BaseCommand(Paint paint) {
		if (paint != null) {
			this.paint = new Paint(paint);
		} else {
			Log.w(TAG, "Object is null falling back to default object in " + this.toString());
			this.paint = new Paint();
			this.paint.setColor(Color.BLACK);
			this.paint.setStrokeWidth(1);
			this.paint.setStrokeCap(Cap.SQUARE);
		}
	}

	@Override
	public void freeResources() {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		if (fileToStoredBitmap != null && fileToStoredBitmap.exists()) {
			fileToStoredBitmap.delete();
		}
	}

	protected final void storeBitmap(File cacheDir) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		fileToStoredBitmap = new File(cacheDir.getAbsolutePath(),
				Long.toString(random.nextLong()));
		try {
			FileOutputStream fos = new FileOutputStream(fileToStoredBitmap);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			Log.e(TAG, "Cannot store bitmap. ", e);
		}
		bitmap.recycle();
		bitmap = null;
	}

	public enum NotifyStates {
		COMMAND_STARTED, COMMAND_DONE, COMMAND_FAILED
	}
}
