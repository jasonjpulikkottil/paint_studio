package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class RemoveLayerCommand implements Command {
	private int position;

	public RemoveLayerCommand(int position) {
		this.position = position;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		layerModel.getLayers().remove(position);
		layerModel.setCurrentLayer(layerModel.getLayers().get(0));
	}

	@Override
	public void freeResources() {
	}
}
