package com.jdots.paint.tools.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class HeartDrawable implements ShapeDrawable {
	private Path path = new Path();
	private Paint paint = new Paint();

	@Override
	public void draw(Canvas canvas, RectF shapeRect, Paint drawPaint) {
		paint.set(drawPaint);

		float midWidth = shapeRect.width() / 2;
		float height = shapeRect.height();
		float width = shapeRect.width();

		path.reset();
		path.moveTo(midWidth, height);
		path.cubicTo(-0.2f * width, 4.5f * height / 8,
				0.8f * width / 8, -1.5f * height / 8,
				midWidth, 1.5f * height / 8);
		path.cubicTo(7.2f * width / 8, -1.5f * height / 8,
				1.2f * width, 4.5f * height / 8,
				midWidth, height);
		path.close();

		path.offset(shapeRect.left, shapeRect.top);
		canvas.drawPath(path, paint);
	}
}
