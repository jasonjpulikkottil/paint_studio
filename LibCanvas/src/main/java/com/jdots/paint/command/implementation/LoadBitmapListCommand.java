
package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.Layer;

import java.util.List;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class LoadBitmapListCommand implements Command {
	private List<Bitmap> loadedImageList;

	public LoadBitmapListCommand(List<Bitmap> newBitmapList) {
		loadedImageList = newBitmapList;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {

		int counter = 0;
		for (Bitmap current : loadedImageList) {
			Layer currentLayer = new Layer(current.copy(ARGB_8888, true));
			layerModel.addLayerAt(counter, currentLayer);

			counter++;
		}

		layerModel.setCurrentLayer(layerModel.getLayerAt(0));
	}

	@Override
	public void freeResources() {
	}
}
