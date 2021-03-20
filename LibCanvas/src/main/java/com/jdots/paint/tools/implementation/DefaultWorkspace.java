package com.jdots.paint.tools.implementation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;

import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.ui.Perspective;

import java.util.List;

public class DefaultWorkspace implements Workspace {

	private final LayerContracts.Model layerModel;
	private final Perspective perspective;
	private final Listener listener;

	public DefaultWorkspace(LayerContracts.Model layerModel, Perspective perspective, Listener listener) {
		this.layerModel = layerModel;
		this.perspective = perspective;
		this.listener = listener;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	@Override
	public boolean contains(PointF point) {
		return point.x < getWidth() && point.x >= 0 && point.y < getHeight() && point.y >= 0;
	}

	@Override
	public boolean intersectsWith(RectF rectangle) {
		return 0 < rectangle.right
				&& rectangle.left < getWidth()
				&& 0 < rectangle.bottom
				&& rectangle.top < getHeight();
	}

	@Override
	public int getHeight() {
		return layerModel.getHeight();
	}

	@Override
	public int getWidth() {
		return layerModel.getWidth();
	}

	@Override
	public int getSurfaceWidth() {
		return perspective.getSurfaceWidth();
	}

	@Override
	public int getSurfaceHeight() {
		return perspective.getSurfaceHeight();
	}

	@Override
	public Bitmap getBitmapOfAllLayers() {
		return LayerModel.getBitmapOfAllLayersToSave(layerModel.getLayers());
	}

	@Override
	public List<Bitmap> getBitmapLisOfAllLayers() {
		return LayerModel.getBitmapListOfAllLayers(layerModel.getLayers());
	}

	@Override
	public Bitmap getBitmapOfCurrentLayer() {
		return Bitmap.createBitmap(layerModel.getCurrentLayer().getBitmap());
	}

	@Override
	public int getCurrentLayerIndex() {
		return layerModel.getLayerIndexOf(layerModel.getCurrentLayer());
	}

	@Override
	public void resetPerspective() {
		perspective.setBitmapDimensions(getWidth(), getHeight());
		perspective.resetScaleAndTranslation();
	}

	@Override
	public PointF getCanvasPointFromSurfacePoint(PointF surfacePoint) {
		return perspective.getCanvasPointFromSurfacePoint(surfacePoint);
	}

	@Override
	public void invalidate() {
		listener.invalidate();
	}

	@Override
	public int getPixelOfCurrentLayer(PointF coordinate) {
		if (coordinate.x >= 0 && coordinate.y >= 0 && coordinate.x < getWidth() && coordinate.y < getHeight()) {
			Bitmap bitmap = layerModel.getCurrentLayer().getBitmap();
			return bitmap.getPixel((int) coordinate.x, (int) coordinate.y);
		}
		return Color.TRANSPARENT;
	}

	@Override
	public void setScale(float zoomFactor) {
		perspective.setScale(zoomFactor);
	}

	@Override
	public float getScaleForCenterBitmap() {
		return perspective.getScaleForCenterBitmap();
	}

	@Override
	public float getScale() {
		return perspective.getScale();
	}

	@Override
	public PointF getSurfacePointFromCanvasPoint(PointF canvasPoint) {
		return perspective.getSurfacePointFromCanvasPoint(canvasPoint);
	}

	public interface Listener {
		void invalidate();
	}
}
