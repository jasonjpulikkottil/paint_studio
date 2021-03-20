package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.Layer;

public class AddLayerCommand implements Command {
	private CommonFactory commonFactory;

	public AddLayerCommand(CommonFactory commonFactory) {
		this.commonFactory = commonFactory;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		Layer layer = new Layer(commonFactory.createBitmap(layerModel.getWidth(), layerModel.getHeight(),
				Bitmap.Config.ARGB_8888));
		layerModel.addLayerAt(0, layer);
		layerModel.setCurrentLayer(layer);
	}

	@Override
	public void freeResources() {
	}
}
