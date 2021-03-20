package com.jdots.paint.test.listener;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;

import com.jdots.paint.listener.DrawingSurfaceListener.AutoScrollTask;
import com.jdots.paint.listener.DrawingSurfaceListener.AutoScrollTaskCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.jdots.paint.test.utils.PointFAnswer.setPointFTo;
import static com.jdots.paint.test.utils.PointFMatcher.pointFEquals;
import static com.jdots.paint.tools.ToolType.FILL;
import static com.jdots.paint.tools.ToolType.TRANSFORM;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AutoScrollTaskTest {

	@Mock
	private Handler handler;

	@Mock
	private AutoScrollTaskCallback callback;

	@InjectMocks
	private AutoScrollTask autoScrollTask;

	@Test
	public void testSetUp() {
		verifyZeroInteractions(handler, callback);
		assertFalse(autoScrollTask.isRunning());
	}

	@Test
	public void testRun() {
		Point autoScrollDirection = mock(Point.class);
		when(callback.getToolAutoScrollDirection(anyFloat(), anyFloat(), anyInt(), anyInt()))
				.thenReturn(autoScrollDirection);

		autoScrollTask.run();

		verify(callback).getToolAutoScrollDirection(anyFloat(), anyFloat(), anyInt(), anyInt());
		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
	}

	@Test
	public void testRunAutoScrollLeft() {
		Point autoScrollDirection = mock(Point.class);
		autoScrollDirection.x = -1;
		when(callback.getToolAutoScrollDirection(3f, 5f, 39, 42))
				.thenReturn(autoScrollDirection);
		when(callback.isPointOnCanvas(7, 11)).thenReturn(true);
		setPointFTo(7f, 11f).when(callback).convertToCanvasFromSurface(pointFEquals(3f, 5f));

		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);
		autoScrollTask.run();

		verify(callback).translatePerspective(-1 * 2f, 0);
		verify(callback).handleToolMove(pointFEquals(7f, 11f));
		verify(callback).refreshDrawingSurface();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
	}

	@Test
	public void testRunAutoScrollLeftWhenNotOnCanvas() {
		Point autoScrollDirection = mock(Point.class);
		autoScrollDirection.x = -1;
		when(callback.getToolAutoScrollDirection(3f, 5f, 39, 42))
				.thenReturn(autoScrollDirection);
		setPointFTo(7f, 11f).when(callback).convertToCanvasFromSurface(pointFEquals(3f, 5f));

		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);
		autoScrollTask.run();

		verify(callback, never()).translatePerspective(anyFloat(), anyFloat());
		verify(callback, never()).handleToolMove(any(PointF.class));
		verify(callback, never()).refreshDrawingSurface();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
	}

	@Test
	public void testRunAutoScrollUp() {
		Point autoScrollDirection = mock(Point.class);
		autoScrollDirection.y = -1;
		when(callback.getToolAutoScrollDirection(3f, 5f, 39, 42))
				.thenReturn(autoScrollDirection);
		when(callback.isPointOnCanvas(7, 11)).thenReturn(true);
		setPointFTo(7f, 11f).when(callback).convertToCanvasFromSurface(pointFEquals(3f, 5f));

		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);
		autoScrollTask.run();

		verify(callback).translatePerspective(0, -1 * 2f);
		verify(callback).handleToolMove(pointFEquals(7f, 11f));
		verify(callback).refreshDrawingSurface();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
	}

	@Test
	public void testRunAutoScrollUpWhenNotOnCanvas() {
		Point autoScrollDirection = mock(Point.class);
		autoScrollDirection.y = -1;
		when(callback.getToolAutoScrollDirection(3f, 5f, 39, 42))
				.thenReturn(autoScrollDirection);
		setPointFTo(7f, 11f).when(callback).convertToCanvasFromSurface(pointFEquals(3f, 5f));

		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);
		autoScrollTask.run();

		verify(callback, never()).translatePerspective(anyFloat(), anyFloat());
		verify(callback, never()).handleToolMove(any(PointF.class));
		verify(callback, never()).refreshDrawingSurface();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
	}

	@Test
	public void testStop() {
		autoScrollTask.stop();
		verifyZeroInteractions(handler, callback);

		assertFalse(autoScrollTask.isRunning());
	}

	@Test
	public void testStart() {
		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);

		Point autoScrollDirection = mock(Point.class);
		when(callback.getToolAutoScrollDirection(anyFloat(), anyFloat(), anyInt(), anyInt()))
				.thenReturn(autoScrollDirection);

		autoScrollTask.start();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
		assertTrue(autoScrollTask.isRunning());
	}

	@Test(expected = IllegalStateException.class)
	public void testStartWhenAlreadyRunning() {
		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);

		Point autoScrollDirection = mock(Point.class);
		when(callback.getToolAutoScrollDirection(anyFloat(), anyFloat(), anyInt(), anyInt()))
				.thenReturn(autoScrollDirection);

		autoScrollTask.start();
		autoScrollTask.start();
	}

	@Test(expected = IllegalStateException.class)
	public void testStartWithZeroWidth() {
		autoScrollTask.setViewDimensions(0, 42);
		autoScrollTask.start();
	}

	@Test(expected = IllegalStateException.class)
	public void testStartWithZeroHeight() {
		autoScrollTask.setViewDimensions(42, 0);
		autoScrollTask.start();
	}

	@Test
	public void testStartIgnoredTools() {
		when(callback.getCurrentToolType()).thenReturn(FILL, TRANSFORM);

		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);

		autoScrollTask.start();
		autoScrollTask.start();
		autoScrollTask.start();

		assertFalse(autoScrollTask.isRunning());
		verifyZeroInteractions(handler);
	}

	@Test
	public void testStopAfterStart() {
		autoScrollTask.setEventPoint(3f, 5f);
		autoScrollTask.setViewDimensions(39, 42);

		Point autoScrollDirection = mock(Point.class);
		when(callback.getToolAutoScrollDirection(anyFloat(), anyFloat(), anyInt(), anyInt()))
				.thenReturn(autoScrollDirection);

		autoScrollTask.start();
		autoScrollTask.stop();

		verify(handler).postDelayed(eq(autoScrollTask), anyLong());
		verify(handler).removeCallbacks(autoScrollTask);
		assertFalse(autoScrollTask.isRunning());
	}
}
