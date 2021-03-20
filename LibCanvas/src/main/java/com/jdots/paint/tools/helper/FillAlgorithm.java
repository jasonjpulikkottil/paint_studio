package com.jdots.paint.tools.helper;

import android.graphics.Bitmap;
import android.graphics.Point;

public interface FillAlgorithm {
	void setParameters(Bitmap bitmap, Point clickedPixel, int targetColor, int replacementColor, float colorToleranceThreshold);

	void performFilling();
}
