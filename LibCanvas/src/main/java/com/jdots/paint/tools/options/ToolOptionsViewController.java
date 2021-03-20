package com.jdots.paint.tools.options;

import android.view.ViewGroup;

public interface ToolOptionsViewController extends ToolOptionsVisibilityController {
	void disable();

	void enable();

	void resetToOrigin();

	void removeToolViews();

	void showCheckmark();

	void hideCheckmark();

	ViewGroup getToolSpecificOptionsLayout();
}
