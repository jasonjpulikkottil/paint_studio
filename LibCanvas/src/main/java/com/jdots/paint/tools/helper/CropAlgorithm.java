package com.jdots.paint.tools.helper;

import android.graphics.Bitmap;
import android.graphics.Rect;

public interface CropAlgorithm {
	Rect crop(Bitmap bitmap);
}
