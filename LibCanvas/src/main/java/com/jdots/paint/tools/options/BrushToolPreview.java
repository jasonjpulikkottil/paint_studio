package com.jdots.paint.tools.options;

public interface BrushToolPreview {
	void setListener(BrushToolOptionsView.OnBrushPreviewListener callback);

	void invalidate();
}
