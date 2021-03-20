package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class ReorderLayersCommand implements Command {
	private final int position;
	private final int destination;

	public ReorderLayersCommand(int position, int destination) {
		this.position = position;
		this.destination = destination;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		LayerContracts.Layer tempLayer = layerModel.getLayerAt(position);
		layerModel.removeLayerAt(position);
		layerModel.addLayerAt(destination, tempLayer);
	}

	@Override
	public void freeResources() {
	}
}
