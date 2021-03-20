package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.tools.helper.FillAlgorithm;
import com.jdots.paint.tools.helper.FillAlgorithmFactory;

public class FillCommand implements Command {
	private Paint paint;
	private float colorTolerance;
	private FillAlgorithmFactory fillAlgorithmFactory;
	private Point clickedPixel;

	public FillCommand(FillAlgorithmFactory fillAlgorithmFactory, Point clickedPixel, Paint paint, float colorTolerance) {
		this.fillAlgorithmFactory = fillAlgorithmFactory;
		this.clickedPixel = clickedPixel;
		this.paint = paint;
		this.colorTolerance = colorTolerance;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		Bitmap bitmap = layerModel.getCurrentLayer().getBitmap();

		int replacementColor = bitmap.getPixel(clickedPixel.x, clickedPixel.y);
		int targetColor = paint.getColor();

		FillAlgorithm fillAlgorithm = fillAlgorithmFactory.createFillAlgorithm();

		fillAlgorithm.setParameters(bitmap, clickedPixel, targetColor, replacementColor, colorTolerance);
		fillAlgorithm.performFilling();
	}

	@Override
	public void freeResources() {
	}
}
