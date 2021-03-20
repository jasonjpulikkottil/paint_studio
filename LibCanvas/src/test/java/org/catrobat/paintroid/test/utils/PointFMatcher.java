package com.jdots.paint.test.utils;

import android.graphics.PointF;

import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public final class PointFMatcher implements ArgumentMatcher<PointF> {
	private final float pointX;
	private final float pointY;

	public PointFMatcher(float x, float y) {
		this.pointX = x;
		this.pointY = y;
	}

	@Override
	public boolean matches(PointF argument) {
		return argument.x == pointX && argument.y == pointY;
	}

	@Override
	public String toString() {
		return "PointF(" + pointX + ", " + pointY + ")";
	}

	public static PointF pointFEquals(float x, float y) {
		return argThat(new PointFMatcher(x, y));
	}
}
