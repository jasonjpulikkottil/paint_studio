package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class SetDimensionCommand implements Command {

	private final int width;
	private final int height;

	public SetDimensionCommand(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		layerModel.setWidth(width);
		layerModel.setHeight(height);
	}

	@Override
	public void freeResources() {
	}
}
