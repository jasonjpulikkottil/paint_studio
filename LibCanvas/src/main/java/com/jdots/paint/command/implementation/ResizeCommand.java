package com.jdots.paint.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import java.util.ListIterator;

public class ResizeCommand implements Command {
	private final int newWidth;
	private final int newHeight;
	public ResizeCommand(int newWidth, int newHeight) {
		this.newWidth = newWidth;
		this.newHeight = newHeight;
	}
	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		if (layerModel == null) {
			return;
		}
		ListIterator<LayerContracts.Layer> iterator = layerModel.listIterator(0);
		while (iterator.hasNext()) {
			LayerContracts.Layer currentLayer = iterator.next();
			Bitmap currentBitmap = currentLayer.getBitmap();
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(currentBitmap, newWidth, newHeight, true);
			currentLayer.setBitmap(resizedBitmap);
		}
		layerModel.setHeight(newHeight);
		layerModel.setWidth(newWidth);
	}
	@Override
	public void freeResources() {
	}
}
