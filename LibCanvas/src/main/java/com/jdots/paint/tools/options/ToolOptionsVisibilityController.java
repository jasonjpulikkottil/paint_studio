package com.jdots.paint.tools.options;

import androidx.annotation.Nullable;

public interface ToolOptionsVisibilityController {
	void hide();

	boolean isVisible();

	void setCallback(@Nullable Callback callback);

	void show();

	void showDelayed();

	interface Callback {
		void onHide();

		void onShow();
	}
}
