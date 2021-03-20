package com.jdots.paint.tools.drawable;

public class DrawableFactory {
	public ShapeDrawable createDrawable(DrawableShape shape) {
		switch (shape) {
			case RECTANGLE:
				return new RectangleDrawable();
			case OVAL:
				return new OvalDrawable();
			case HEART:
				return new HeartDrawable();
			case STAR:
				return new StarDrawable();
		}
		throw new IllegalArgumentException();
	}
}
