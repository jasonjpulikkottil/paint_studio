package com.jdots.paint.tools.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class StarDrawable implements ShapeDrawable {
	private Path path = new Path();
	private Paint paint = new Paint();

	@Override
	public void draw(Canvas canvas, RectF shapeRect, Paint drawPaint) {
		paint.set(drawPaint);

		float midWidth = shapeRect.width() / 2;
		float midHeight = shapeRect.height() / 2;
		float height = shapeRect.height();
		float width = shapeRect.width();

		path.reset();
		path.moveTo(midWidth, 0);
		path.lineTo(midWidth + width / 8, midHeight - height / 8);
		path.lineTo(width, midHeight - height / 8);
		path.lineTo(midWidth + 1.8f * width / 8, midHeight + 1 * height / 8);
		path.lineTo(midWidth + 3 * width / 8, height);
		path.lineTo(midWidth, midHeight + 2 * height / 8);
		path.lineTo(midWidth - 3 * width / 8, height);
		path.lineTo(midWidth - 1.8f * width / 8, midHeight + 1 * height / 8);
		path.lineTo(0, midHeight - height / 8);
		path.lineTo(midWidth - width / 8, midHeight - height / 8);
		path.lineTo(midWidth, 0);
		path.close();

		path.offset(shapeRect.left, shapeRect.top);
		canvas.drawPath(path, paint);
	}
}
