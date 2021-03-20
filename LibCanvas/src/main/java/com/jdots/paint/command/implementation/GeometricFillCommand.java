package com.jdots.paint.command.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.tools.drawable.ShapeDrawable;

public class GeometricFillCommand implements Command {
	private final float boxRotation;
	private final RectF boxRect;
	private final int pointX;
	private final int pointY;
	private final Paint paint;
	private final ShapeDrawable shapeDrawable;

	public GeometricFillCommand(ShapeDrawable shapeDrawable, int pointX, int pointY, RectF boxRect,
			float boxRotation, Paint paint) {
		this.pointX = pointX;
		this.pointY = pointY;
		this.boxRect = boxRect;
		this.shapeDrawable = shapeDrawable;
		this.boxRotation = boxRotation;
		this.paint = paint;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		canvas.save();
		canvas.translate(pointX, pointY);
		canvas.rotate(boxRotation);
		shapeDrawable.draw(canvas, boxRect, paint);
		canvas.restore();
	}

	@Override
	public void freeResources() {
		//No resources to free
	}
}
