package com.jdots.paint.tools.options;

public interface FillToolOptionsView {
	void setCallback(Callback callback);

	interface Callback {
		void onColorToleranceChanged(int colorTolerance);
	}
}
