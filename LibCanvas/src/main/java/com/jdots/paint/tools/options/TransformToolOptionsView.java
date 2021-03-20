package com.jdots.paint.tools.options;

import com.jdots.paint.ui.tools.NumberRangeFilter;

public interface TransformToolOptionsView {
	void setWidthFilter(NumberRangeFilter numberRangeFilter);

	void setHeightFilter(NumberRangeFilter numberRangeFilter);

	void setCallback(Callback callback);

	void setWidth(int width);

	void setHeight(int height);

	interface Callback {
		void autoCropClicked();

		void rotateCounterClockwiseClicked();

		void rotateClockwiseClicked();

		void flipHorizontalClicked();

		void flipVerticalClicked();

		void applyResizeClicked(int resizePercentage);

		void setBoxWidth(float boxWidth);

		void setBoxHeight(float boxHeight);

		void hideToolOptions();
	}
}
