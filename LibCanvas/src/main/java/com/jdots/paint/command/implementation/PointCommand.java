package com.jdots.paint.command.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import androidx.annotation.VisibleForTesting;

public class PointCommand implements Command {
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public Paint paint;
	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public PointF point;

	public PointCommand(Paint paint, PointF point) {
		this.paint = paint;
		this.point = point;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		canvas.drawPoint(point.x, point.y, paint);
	}

	@Override
	public void freeResources() {
	}
}
