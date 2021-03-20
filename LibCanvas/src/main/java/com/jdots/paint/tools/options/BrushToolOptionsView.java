package com.jdots.paint.tools.options;

import android.graphics.Paint;

import com.jdots.paint.tools.ToolType;

public interface BrushToolOptionsView {
	void invalidate();

	void setCurrentPaint(Paint paint);

	void setBrushChangedListener(OnBrushChangedListener onBrushChangedListener);

	void setBrushPreviewListener(OnBrushPreviewListener onBrushPreviewListener);

	interface OnBrushChangedListener {
		void setCap(Paint.Cap strokeCap);

		void setStrokeWidth(int strokeWidth);
	}

	interface OnBrushPreviewListener {
		float getStrokeWidth();

		Paint.Cap getStrokeCap();

		int getColor();

		ToolType getToolType();
	}
}
