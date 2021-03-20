package com.jdots.paint.tools.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class OvalDrawable implements ShapeDrawable {
	@Override
	public void draw(Canvas canvas, RectF shapeRect, Paint drawPaint) {
		canvas.drawOval(shapeRect, drawPaint);
	}
}
