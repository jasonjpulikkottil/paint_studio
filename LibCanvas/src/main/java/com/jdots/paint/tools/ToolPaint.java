package com.jdots.paint.tools;

import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import androidx.annotation.ColorInt;

public interface ToolPaint {

	Paint getPaint();

	void setPaint(Paint paint);

	Paint getPreviewPaint();

	int getColor();

	void setColor(@ColorInt int color);

	PorterDuffXfermode getEraseXfermode();

	int getPreviewColor();

	float getStrokeWidth();

	void setStrokeWidth(float strokeWidth);

	Paint.Cap getStrokeCap();

	void setStrokeCap(Paint.Cap strokeCap);

	Shader getCheckeredShader();
}
