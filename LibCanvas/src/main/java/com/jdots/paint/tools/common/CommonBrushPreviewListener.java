package com.jdots.paint.tools.common;

import android.graphics.Paint;

import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.options.BrushToolOptionsView;

public class CommonBrushPreviewListener implements BrushToolOptionsView.OnBrushPreviewListener {
	private ToolType toolType;
	private ToolPaint toolPaint;

	public CommonBrushPreviewListener(ToolPaint toolPaint, ToolType toolType) {
		this.toolType = toolType;
		this.toolPaint = toolPaint;
	}

	@Override
	public float getStrokeWidth() {
		return toolPaint.getStrokeWidth();
	}

	@Override
	public Paint.Cap getStrokeCap() {
		return toolPaint.getStrokeCap();
	}

	@Override
	public int getColor() {
		return toolPaint.getColor();
	}

	@Override
	public ToolType getToolType() {
		return toolType;
	}
}
