package com.jdots.paint.tools.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public interface ShapeDrawable {
	void draw(Canvas canvas, RectF shapeRect, Paint drawPaint);
}
