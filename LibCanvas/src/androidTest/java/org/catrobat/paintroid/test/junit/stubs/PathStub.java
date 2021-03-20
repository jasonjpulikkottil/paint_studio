package com.jdots.paint.test.junit.stubs;

import android.graphics.Path;

import static org.mockito.Mockito.mock;

public final class PathStub extends Path {
	private Path stub;

	public PathStub() {
		super();
		stub = mock(Path.class);
	}

	public Path getStub() {
		return stub;
	}

	@Override
	public void reset() {
		stub.reset();
	}

	@Override
	public void rewind() {
		stub.rewind();
	}

	@Override
	public void moveTo(float x, float y) {
		stub.moveTo(x, y);
	}

	@Override
	public void quadTo(float x1, float y1, float x2, float y2) {
		stub.quadTo(x1, y1, x2, y2);
	}

	@Override
	public void lineTo(float x, float y) {
		stub.lineTo(x, y);
	}
}
