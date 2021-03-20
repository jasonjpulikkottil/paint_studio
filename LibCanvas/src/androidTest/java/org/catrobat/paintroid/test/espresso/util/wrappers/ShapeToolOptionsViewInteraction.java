package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;
import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.drawable.DrawableStyle;

import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class ShapeToolOptionsViewInteraction extends CustomViewInteraction {
	private ShapeToolOptionsViewInteraction() {
		super(onView(withId(R.id.paint_studio_main_tool_options)));
	}

	public static ShapeToolOptionsViewInteraction onShapeToolOptionsView() {
		return new ShapeToolOptionsViewInteraction();
	}

	private int getButtonIdFromBaseShape(DrawableShape baseShape) {
		switch (baseShape) {
			case RECTANGLE:
				return R.id.paint_studio_shapes_square_btn;
			case OVAL:
				return R.id.paint_studio_shapes_circle_btn;
			case HEART:
				return R.id.paint_studio_shapes_heart_btn;
			case STAR:
				return R.id.paint_studio_shapes_star_btn;
		}
		throw new IllegalArgumentException();
	}

	private int getButtonIdFromShapeDrawType(DrawableStyle shapeDrawType) {
		switch (shapeDrawType) {
			case STROKE:
				return R.id.paint_studio_shape_ibtn_outline;
			case FILL:
				return R.id.paint_studio_shape_ibtn_fill;
		}
		throw new IllegalArgumentException();
	}

	public ShapeToolOptionsViewInteraction performSelectShape(DrawableShape shape) {
		onView(withId(getButtonIdFromBaseShape(shape)))
				.perform(click());
		return this;
	}

	public ShapeToolOptionsViewInteraction performSelectShapeDrawType(DrawableStyle shapeDrawType) {
		onView(withId(getButtonIdFromShapeDrawType(shapeDrawType)))
				.perform(click());
		return this;
	}

	public void performSetOutlineWidth(ViewAction setWidth) {
		onView(withId(R.id.paint_studio_shape_stroke_width_seek_bar))
				.perform(setWidth);
	}
}
