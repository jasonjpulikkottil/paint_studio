package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class MergeLayersCommand implements Command {
	private final int position;
	private final int mergeWith;

	public MergeLayersCommand(int position, int mergeWith) {
		this.position = position;
		this.mergeWith = mergeWith;
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		LayerContracts.Layer sourceLayer = layerModel.getLayerAt(position);
		LayerContracts.Layer destinationLayer = layerModel.getLayerAt(mergeWith);

		Bitmap destinationBitmap = destinationLayer.getBitmap();
		Bitmap copyBitmap = destinationBitmap.copy(destinationBitmap.getConfig(), true);

		Canvas copyCanvas = new Canvas(copyBitmap);
		copyCanvas.drawBitmap(sourceLayer.getBitmap(), 0, 0, null);
		destinationLayer.setBitmap(copyBitmap);
		layerModel.removeLayerAt(position);

		if (sourceLayer == layerModel.getCurrentLayer()) {
			layerModel.setCurrentLayer(destinationLayer);
		}
	}

	@Override
	public void freeResources() {
	}
}
