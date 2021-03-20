package com.jdots.paint.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.contract.LayerContracts;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LayerModel implements LayerContracts.Model {
	private final List<LayerContracts.Layer> layers;
	private LayerContracts.Layer currentLayer;
	private int width;
	private int height;

	public LayerModel() {
		this.layers = new ArrayList<>();
		this.currentLayer = null;
	}

	@Override
	public List<LayerContracts.Layer> getLayers() {
		return layers;
	}

	@Override
	public LayerContracts.Layer getCurrentLayer() {
		return currentLayer;
	}

	@Override
	public void setCurrentLayer(LayerContracts.Layer layer) {
		this.currentLayer = layer;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void reset() {
		layers.clear();
	}

	@Override
	public int getLayerCount() {
		return layers.size();
	}

	@Override
	public LayerContracts.Layer getLayerAt(int index) {
		return layers.get(index);
	}

	@Override
	public int getLayerIndexOf(LayerContracts.Layer layer) {
		return layers.indexOf(layer);
	}

	@Override
	public void addLayerAt(int index, LayerContracts.Layer layer) {
		layers.add(index, layer);
	}

	@Override
	public ListIterator<LayerContracts.Layer> listIterator(int index) {
		return layers.listIterator(index);
	}

	@Override
	public void setLayerAt(int position, LayerContracts.Layer layer) {
		layers.set(position, layer);
	}

	@Override
	public void removeLayerAt(int position) {
		layers.remove(position);
	}

	public static Bitmap getBitmapOfAllLayersToSave(List<LayerContracts.Layer> layers) {
		if (layers.size() == 0) {
			return null;
		}
		Bitmap referenceBitmap = layers.get(0).getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(referenceBitmap.getWidth(), referenceBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		ListIterator<LayerContracts.Layer> layerListIterator = layers.listIterator(layers.size());

		while (layerListIterator.hasPrevious()) {
			LayerContracts.Layer layer = layerListIterator.previous();
			canvas.drawBitmap(layer.getBitmap(), 0, 0, null);
		}

		return bitmap;
	}

	public static List<Bitmap> getBitmapListOfAllLayers(List<LayerContracts.Layer> layers) {
		List<Bitmap> bitmapList = new ArrayList<>();

		for (LayerContracts.Layer layer : layers) {
			bitmapList.add(layer.getBitmap());
		}

		return bitmapList;
	}
}
