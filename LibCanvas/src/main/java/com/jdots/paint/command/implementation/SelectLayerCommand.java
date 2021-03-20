package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class SelectLayerCommand implements Command {
	private final int position;

	public SelectLayerCommand(int position) {
		this.position = position;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		layerModel.setCurrentLayer(layerModel.getLayers().get(position));
	}

	@Override
	public void freeResources() {
	}
}
