package com.jdots.paint.tools.common;

import android.graphics.Point;

public interface ScrollBehavior {
	Point getScrollDirection(float pointX, float pointY, int viewWidth, int viewHeight);
}
