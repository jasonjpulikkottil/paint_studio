package com.jdots.paint.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

public class CommonFactory {
	public Canvas createCanvas() {
		return new Canvas();
	}

	public Bitmap createBitmap(int width, int height, Config config) {
		return Bitmap.createBitmap(width, height, config);
	}

	public Paint createPaint(Paint paint) {
		return new Paint(paint);
	}

	public PointF createPointF(PointF point) {
		return new PointF(point.x, point.y);
	}

	public Point createPoint(int x, int y) {
		return new Point(x, y);
	}

	public Path createPath(Path path) {
		return new Path(path);
	}

	public RectF createRectF(RectF rect) {
		return new RectF(rect);
	}
}
