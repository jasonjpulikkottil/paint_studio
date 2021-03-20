package com.jdots.paint.tools.implementation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import com.jdots.paint.tools.ToolPaint;

import com.jdots.paint.R;

public class DefaultToolPaint implements ToolPaint {
	//public static final int STROKE_25 = 25;
	public static final int STROKE_5 = 5;

	private static final PorterDuffXfermode ERASE_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

	private final Shader checkeredShader;
	private final Paint bitmapPaint = new Paint();
	private final Paint previewPaint = new Paint();

	public DefaultToolPaint(Context context) {
		Resources resources = context.getResources();
		Bitmap checkerboard = BitmapFactory.decodeResource(resources, R.drawable.paint_studio_checkeredbg);
		checkeredShader = new BitmapShader(checkerboard, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

		bitmapPaint.reset();
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setColor(Color.BLACK);
		bitmapPaint.setStyle(Paint.Style.STROKE);
		bitmapPaint.setStrokeJoin(Paint.Join.ROUND);
		bitmapPaint.setStrokeCap(Paint.Cap.ROUND);
		bitmapPaint.setStrokeWidth(STROKE_5);
		previewPaint.set(bitmapPaint);
	}

	@Override
	public PorterDuffXfermode getEraseXfermode() {
		return ERASE_XFERMODE;
	}

	@Override
	public int getPreviewColor() {
		return previewPaint.getColor();
	}

	@Override
	public int getColor() {
		return bitmapPaint.getColor();
	}

	@Override
	public void setColor(int color) {
		bitmapPaint.setColor(color);
		previewPaint.set(bitmapPaint);
		previewPaint.setXfermode(null);
		if (Color.alpha(color) == 0) {
			previewPaint.setShader(checkeredShader);
			previewPaint.setColor(Color.BLACK);

			bitmapPaint.setXfermode(ERASE_XFERMODE);
			bitmapPaint.setAlpha(0);
		} else {
			bitmapPaint.setXfermode(null);
		}
	}

	@Override
	public float getStrokeWidth() {
		return bitmapPaint.getStrokeWidth();
	}

	@Override
	public void setStrokeWidth(float strokeWidth) {
		bitmapPaint.setStrokeWidth(strokeWidth);
		previewPaint.setStrokeWidth(strokeWidth);
		boolean antiAliasing = (strokeWidth > 1);
		bitmapPaint.setAntiAlias(antiAliasing);
		previewPaint.setAntiAlias(antiAliasing);
	}

	@Override
	public Paint.Cap getStrokeCap() {
		return bitmapPaint.getStrokeCap();
	}

	@Override
	public void setStrokeCap(Paint.Cap strokeCap) {
		bitmapPaint.setStrokeCap(strokeCap);
		previewPaint.setStrokeCap(strokeCap);
	}

	@Override
	public Paint getPaint() {
		return bitmapPaint;
	}

	@Override
	public void setPaint(Paint paint) {
		bitmapPaint.set(paint);
		previewPaint.set(paint);
	}

	@Override
	public Paint getPreviewPaint() {
		return previewPaint;
	}

	@Override
	public Shader getCheckeredShader() {
		return checkeredShader;
	}
}
