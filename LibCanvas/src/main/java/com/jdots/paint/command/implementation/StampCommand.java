package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.jdots.paint.FileIO;
import com.jdots.paint.PaintStudioApplication;
import com.jdots.paint.contract.LayerContracts;

public class StampCommand extends BaseCommand {
	private final Point coordinates;
	private final float boxRotation;
	private final RectF boxRect;

	public StampCommand(Bitmap bitmap, Point position, float width, float height, float rotation) {
		super(new Paint(Paint.DITHER_FLAG));

		if (position != null) {
			coordinates = new Point(position.x, position.y);
		} else {
			coordinates = null;
		}
		if (bitmap != null) {
			this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
		}

		boxRotation = rotation;
		boxRect = new RectF(-width / 2f, -height / 2f, width / 2f,
				height / 2f);
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {

		if (fileToStoredBitmap != null) {
			bitmap = FileIO.getBitmapFromFile(fileToStoredBitmap);
		}

		if (bitmap == null) {
			return;
		}

		canvas.save();
		canvas.translate(coordinates.x, coordinates.y);
		canvas.rotate(boxRotation);
		canvas.drawBitmap(bitmap, null, boxRect, paint);

		canvas.restore();

		if (fileToStoredBitmap == null) {
			storeBitmap(PaintStudioApplication.cacheDir);
		} else {
			bitmap.recycle();
			bitmap = null;
		}
	}
}
