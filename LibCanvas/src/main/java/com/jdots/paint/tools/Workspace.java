package com.jdots.paint.tools;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;

import com.jdots.paint.ui.Perspective;

import java.util.List;

public interface Workspace {
	boolean contains(PointF point);

	boolean intersectsWith(RectF rectF);

	int getHeight();

	int getWidth();

	int getSurfaceWidth();

	int getSurfaceHeight();

	Bitmap getBitmapOfAllLayers();

	List<Bitmap> getBitmapLisOfAllLayers();

	Bitmap getBitmapOfCurrentLayer();

	int getCurrentLayerIndex();

	int getPixelOfCurrentLayer(PointF coordinate);

	void resetPerspective();

	void setScale(float zoomFactor);

	float getScaleForCenterBitmap();

	float getScale();

	PointF getSurfacePointFromCanvasPoint(PointF coordinate);

	PointF getCanvasPointFromSurfacePoint(PointF surfacePoint);

	void invalidate();

	Perspective getPerspective();
}
