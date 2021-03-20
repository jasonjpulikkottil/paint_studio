package com.jdots.paint.tools.common;

import android.graphics.Paint;

import com.jdots.paint.tools.Tool;
import com.jdots.paint.tools.options.BrushToolOptionsView;

public class CommonBrushChangedListener implements BrushToolOptionsView.OnBrushChangedListener {
	private Tool tool;

	public CommonBrushChangedListener(Tool tool) {
		this.tool = tool;
	}

	@Override
	public void setCap(Paint.Cap strokeCap) {
		tool.changePaintStrokeCap(strokeCap);
	}

	@Override
	public void setStrokeWidth(int strokeWidth) {
		tool.changePaintStrokeWidth(strokeWidth);
	}
}
