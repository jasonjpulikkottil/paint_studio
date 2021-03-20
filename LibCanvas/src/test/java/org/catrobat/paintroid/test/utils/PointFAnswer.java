package com.jdots.paint.test.utils;

import android.graphics.PointF;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.doAnswer;

public final class PointFAnswer implements Answer {
	private final float pointX;
	private final float pointY;

	public PointFAnswer(float x, float y) {
		this.pointX = x;
		this.pointY = y;
	}

	@Override
	public Object answer(InvocationOnMock invocation) {
		PointF point = invocation.getArgument(0);
		point.x = pointX;
		point.y = pointY;
		return null;
	}

	public static Stubber setPointFTo(float x, float y) {
		return doAnswer(new PointFAnswer(x, y));
	}
}
