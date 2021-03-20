package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.Layer;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class LoadCommand implements Command {
	private Bitmap loadedImage;

	public LoadCommand(Bitmap newBitmap) {
		loadedImage = newBitmap;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		Layer currentLayer = new Layer(loadedImage.copy(ARGB_8888, true));
		layerModel.addLayerAt(0, currentLayer);
		layerModel.setCurrentLayer(currentLayer);
	}

	@Override
	public void freeResources() {
	}
}
