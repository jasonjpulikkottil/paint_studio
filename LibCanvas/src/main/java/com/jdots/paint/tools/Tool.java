package com.jdots.paint.tools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

public interface Tool {

	boolean handToolMode();

	boolean handleDown(PointF coordinate);

	boolean handleMove(PointF coordinate);

	boolean handleUp(PointF coordinate);

	void changePaintColor(int color);

	void changePaintStrokeWidth(int strokeWidth);

	void changePaintStrokeCap(Cap cap);

	Paint getDrawPaint();

	void draw(Canvas canvas);

	ToolType getToolType();

	void resetInternalState(StateChange stateChange);

	Point getAutoScrollDirection(float pointX, float pointY, int screenWidth, int screenHeight);

	void onSaveInstanceState(Bundle bundle);

	void onRestoreInstanceState(Bundle bundle);

	enum StateChange {
		ALL, RESET_INTERNAL_STATE, NEW_IMAGE_LOADED, MOVE_CANCELED
	}
}
