package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class FlipCommand implements Command {
	private FlipDirection flipDirection;

	public FlipCommand(FlipDirection flipDirection) {
		this.flipDirection = flipDirection;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		if (flipDirection == null) {
			return;
		}

		Matrix flipMatrix = new Matrix();

		switch (flipDirection) {
			case FLIP_HORIZONTAL:
				flipMatrix.setScale(1, -1);
				flipMatrix.postTranslate(0, layerModel.getHeight());
				break;
			case FLIP_VERTICAL:
				flipMatrix.setScale(-1, 1);
				flipMatrix.postTranslate(layerModel.getWidth(), 0);
				break;
		}

		Bitmap bitmap = layerModel.getCurrentLayer().getBitmap();
		Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
		Canvas flipCanvas = new Canvas(bitmap);
		bitmap.eraseColor(Color.TRANSPARENT);

		flipCanvas.drawBitmap(bitmapCopy, flipMatrix, new Paint());
	}

	@Override
	public void freeResources() {
	}

	public enum FlipDirection {
		FLIP_HORIZONTAL, FLIP_VERTICAL
	}
}
