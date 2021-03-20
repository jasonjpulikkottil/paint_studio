package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import java.util.Iterator;

public class RotateCommand implements Command {
	private static final float ANGLE = 90;
	private RotateDirection rotateDirection;

	public RotateCommand(RotateDirection rotateDirection) {
		this.rotateDirection = rotateDirection;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		if (rotateDirection == null) {
			return;
		}

		Matrix rotateMatrix = new Matrix();
		switch (rotateDirection) {
			case ROTATE_RIGHT:
				rotateMatrix.postRotate(ANGLE);
				break;
			case ROTATE_LEFT:
				rotateMatrix.postRotate(-ANGLE);
				break;
		}

		Iterator<LayerContracts.Layer> iterator = layerModel.listIterator(0);
		while (iterator.hasNext()) {
			LayerContracts.Layer currentLayer = iterator.next();
			Bitmap rotatedBitmap = Bitmap.createBitmap(currentLayer.getBitmap(), 0, 0,
					layerModel.getWidth(), layerModel.getHeight(), rotateMatrix, true);
			currentLayer.setBitmap(rotatedBitmap);
		}

		int width = layerModel.getWidth();
		layerModel.setWidth(layerModel.getHeight());
		layerModel.setHeight(width);
	}

	@Override
	public void freeResources() {
	}

	public enum RotateDirection {
		ROTATE_LEFT, ROTATE_RIGHT
	}
}
