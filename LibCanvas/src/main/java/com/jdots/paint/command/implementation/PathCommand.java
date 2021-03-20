package com.jdots.paint.command.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import androidx.annotation.VisibleForTesting;

public class PathCommand implements Command {
	@VisibleForTesting
	public Paint paint;
	@VisibleForTesting
	public Path path;

	public PathCommand(Paint paint, Path path) {
		this.paint = paint;
		this.path = path;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		canvas.drawPath(path, paint);
	}

	@Override
	public void freeResources() {
	}
}
