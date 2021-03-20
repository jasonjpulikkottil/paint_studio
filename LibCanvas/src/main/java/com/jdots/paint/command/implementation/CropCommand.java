package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import java.util.ListIterator;

public class CropCommand implements Command {
	private final int resizeCoordinateXLeft;
	private final int resizeCoordinateYTop;
	private final int resizeCoordinateXRight;
	private final int resizeCoordinateYBottom;
	private final int maximumBitmapResolution;

	public CropCommand(int resizeCoordinateXLeft, int resizeCoordinateYTop,
			int resizeCoordinateXRight, int resizeCoordinateYBottom,
			int maximumBitmapResolution) {
		this.resizeCoordinateXLeft = resizeCoordinateXLeft;
		this.resizeCoordinateYTop = resizeCoordinateYTop;
		this.resizeCoordinateXRight = resizeCoordinateXRight;
		this.resizeCoordinateYBottom = resizeCoordinateYBottom;
		this.maximumBitmapResolution = maximumBitmapResolution;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		if (layerModel == null) {
			return;
		}

		if (resizeCoordinateXRight < resizeCoordinateXLeft) {
			return;
		}
		if (resizeCoordinateYBottom < resizeCoordinateYTop) {
			return;
		}
		if (resizeCoordinateXLeft >= layerModel.getWidth() || resizeCoordinateXRight < 0
				|| resizeCoordinateYTop >= layerModel.getHeight() || resizeCoordinateYBottom < 0) {
			return;
		}
		if (resizeCoordinateXLeft == 0
				&& resizeCoordinateXRight == layerModel.getWidth() - 1
				&& resizeCoordinateYBottom == layerModel.getHeight() - 1
				&& resizeCoordinateYTop == 0) {
			return;
		}
		if ((resizeCoordinateXRight + 1 - resizeCoordinateXLeft)
				* (resizeCoordinateYBottom + 1 - resizeCoordinateYTop) > maximumBitmapResolution) {
			return;
		}

		int width = resizeCoordinateXRight + 1 - resizeCoordinateXLeft;
		int height = resizeCoordinateYBottom + 1 - resizeCoordinateYTop;

		ListIterator<LayerContracts.Layer> iterator = layerModel.listIterator(0);
		while (iterator.hasNext()) {
			LayerContracts.Layer currentLayer = iterator.next();

			Bitmap currentBitmap = currentLayer.getBitmap();
			Bitmap resizedBitmap = Bitmap.createBitmap(width, height, currentBitmap.getConfig());
			Canvas resizedCanvas = new Canvas(resizedBitmap);
			resizedCanvas.drawBitmap(currentBitmap, -resizeCoordinateXLeft, -resizeCoordinateYTop, null);

			currentLayer.setBitmap(resizedBitmap);
		}

		layerModel.setHeight(height);
		layerModel.setWidth(width);
	}

	@Override
	public void freeResources() {
	}
}
