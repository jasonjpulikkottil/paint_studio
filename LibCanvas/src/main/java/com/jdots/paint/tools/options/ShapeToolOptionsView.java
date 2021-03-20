package com.jdots.paint.tools.options;

import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.drawable.DrawableStyle;

public interface ShapeToolOptionsView {
	void setShapeActivated(DrawableShape shape);

	void setDrawTypeActivated(DrawableStyle drawType);

	void setShapeOutlineWidth(int outlineWidth);

	void setCallback(Callback callback);

	interface Callback {
		void setToolType(DrawableShape shape);

		void setDrawType(DrawableStyle drawType);

		void setOutlineWidth(int outlineWidth);
	}
}
