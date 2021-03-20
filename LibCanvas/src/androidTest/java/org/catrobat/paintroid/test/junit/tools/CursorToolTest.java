package com.jdots.paint.test.junit.tools;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.command.implementation.PointCommand;
import com.jdots.paint.test.junit.stubs.PathStub;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.common.Constants;
import com.jdots.paint.tools.implementation.CursorTool;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import com.jdots.paint.ui.Perspective;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CursorToolTest {
	private static final float MOVE_TOLERANCE = Constants.MOVE_TOLERANCE;
	@Mock
	private CommandManager commandManager;
	@Mock
	private ToolPaint toolPaint;
	@Mock
	private Workspace workspace;
	@Mock
	private BrushToolOptionsView brushToolOptionsView;
	@Mock
	private ToolOptionsVisibilityController toolOptionsViewController;
	@Mock
	private ContextCallback contextCallback;

	private CursorTool toolToTest;

	@Before
	public void setUp() {
		Paint paint = new Paint();
		when(toolPaint.getPaint()).thenReturn(paint);
		when(workspace.getHeight()).thenReturn(1920);
		when(workspace.getWidth()).thenReturn(1080);
		when(workspace.getPerspective()).thenReturn(new Perspective(1080, 1920));

		toolToTest = new CursorTool(brushToolOptionsView, contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
	}

	@Test
	public void testShouldReturnCorrectToolType() {
		ToolType toolType = toolToTest.getToolType();

		assertEquals(ToolType.CURSOR, toolType);
	}

	@Test
	public void testShouldActivateCursorOnTapEvent() {
		PointF point = new PointF(5, 5);
		when(workspace.contains(any(PointF.class))).thenReturn(true);

		assertTrue(toolToTest.handleDown(point));
		assertTrue(toolToTest.handleUp(point));

		verify(commandManager).addCommand(isA(PointCommand.class));
		assertTrue(toolToTest.toolInDrawMode);
	}

	@Test
	public void testShouldActivateCursorOnTapEventOutsideDrawingSurface() {
		when(workspace.contains(toolToTest.toolPosition)).thenReturn(true);
		PointF point = new PointF(-5, -5);

		assertTrue(toolToTest.handleDown(point));
		assertTrue(toolToTest.handleUp(point));

		verify(commandManager).addCommand(isA(PointCommand.class));
		assertTrue(toolToTest.toolInDrawMode);
	}

	@Test
	public void testShouldNotActivateCursorOnTapEvent() {
		PointF pointDown = new PointF(0, 0);
		PointF pointUp = new PointF(pointDown.x + MOVE_TOLERANCE + 1, pointDown.y + MOVE_TOLERANCE + 1);

		// +/+
		assertTrue(toolToTest.handleDown(pointDown));
		assertTrue(toolToTest.handleUp(pointUp));

		verify(commandManager, never()).addCommand(any(Command.class));
		assertFalse(toolToTest.toolInDrawMode);

		// +/0
		pointUp.set(pointDown.x + MOVE_TOLERANCE + 1, pointDown.y);

		assertTrue(toolToTest.handleDown(pointDown));
		assertTrue(toolToTest.handleUp(pointUp));

		verify(commandManager, never()).addCommand(any(Command.class));

		assertFalse(toolToTest.toolInDrawMode);

		// 0/+
		pointUp.set(pointDown.x, pointDown.y + MOVE_TOLERANCE + 1);

		assertTrue(toolToTest.handleDown(pointDown));
		assertTrue(toolToTest.handleUp(pointUp));

		verify(commandManager, never()).addCommand(any(Command.class));
		assertFalse(toolToTest.toolInDrawMode);

		// -/-
		pointUp.set(pointDown.x - MOVE_TOLERANCE - 1, pointDown.y - MOVE_TOLERANCE - 1);

		assertTrue(toolToTest.handleDown(pointDown));
		assertTrue(toolToTest.handleUp(pointUp));

		verify(commandManager, never()).addCommand(any(Command.class));
		assertFalse(toolToTest.toolInDrawMode);
	}

	private static PointF copyPointF(PointF point) {
		return new PointF(point.x, point.y);
	}

	@Test
	public void testShouldMovePathOnUpEvent() {
		when(workspace.getSurfaceHeight()).thenReturn(1920);
		when(workspace.getSurfaceWidth()).thenReturn(1080);
		when(workspace.getSurfacePointFromCanvasPoint(any(PointF.class))).thenAnswer(new Answer<PointF>() {
			@Override
			public PointF answer(InvocationOnMock invocation) {
				return copyPointF((PointF) invocation.getArgument(0));
			}
		});

		PointF event1 = new PointF(0, 0);
		PointF event2 = new PointF(MOVE_TOLERANCE, MOVE_TOLERANCE);
		PointF event3 = new PointF(MOVE_TOLERANCE * 2, -MOVE_TOLERANCE);
		PointF testCursorPosition = new PointF();
		PointF actualCursorPosition = toolToTest.toolPosition;
		assertNotNull(actualCursorPosition);
		testCursorPosition.set(actualCursorPosition);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;
		assertFalse(toolToTest.toolInDrawMode);

		// e1
		boolean returnValue = toolToTest.handleDown(event1);
		assertTrue(returnValue);
		assertFalse(toolToTest.toolInDrawMode);
		returnValue = toolToTest.handleUp(event1);
		assertTrue(toolToTest.toolInDrawMode);
		assertTrue(returnValue);
		assertEquals(testCursorPosition.x, actualCursorPosition.x, Double.MIN_VALUE);
		assertEquals(testCursorPosition.y, actualCursorPosition.y, Double.MIN_VALUE);
		// e2
		returnValue = toolToTest.handleMove(event2);
		float vectorCX = event2.x - event1.x;
		float vectorCY = event2.y - event1.y;
		testCursorPosition.set(testCursorPosition.x + vectorCX, testCursorPosition.y + vectorCY);
		assertEquals(testCursorPosition.x, actualCursorPosition.x, Double.MIN_VALUE);
		assertEquals(testCursorPosition.y, actualCursorPosition.y, Double.MIN_VALUE);
		assertTrue(toolToTest.toolInDrawMode);
		assertTrue(returnValue);
		// e3
		returnValue = toolToTest.handleUp(event3);
		assertTrue(toolToTest.toolInDrawMode);
		assertTrue(returnValue);
		assertEquals(testCursorPosition.x, actualCursorPosition.x, Double.MIN_VALUE);
		assertEquals(testCursorPosition.y, actualCursorPosition.y, Double.MIN_VALUE);

		Path stub = pathStub.getStub();
		verify(stub).moveTo(anyFloat(), anyFloat());
		verify(stub).quadTo(anyFloat(), anyFloat(), anyFloat(), anyFloat());
		verify(stub).lineTo(testCursorPosition.x, testCursorPosition.y);
	}

	@Test
	public void testShouldCheckIfColorChangesIfToolIsActive() {
		when(workspace.contains(toolToTest.toolPosition)).thenReturn(true);
		when(toolPaint.getColor()).thenReturn(Color.RED);

		assertFalse(toolToTest.toolInDrawMode);

		PointF point = new PointF(200, 200);
		toolToTest.handleDown(point);
		toolToTest.handleUp(point);

		assertTrue(toolToTest.toolInDrawMode);
		assertEquals(Color.RED, toolToTest.cursorToolSecondaryShapeColor);

		toolToTest.handleDown(point);
		toolToTest.handleUp(point);

		assertFalse(toolToTest.toolInDrawMode);
		assertEquals(Color.LTGRAY, toolToTest.cursorToolSecondaryShapeColor);

		when(toolPaint.getColor()).thenReturn(Color.GREEN);
		toolToTest.handleDown(point);
		toolToTest.handleUp(point);

		assertTrue(toolToTest.toolInDrawMode);
		assertEquals(Color.GREEN, toolToTest.cursorToolSecondaryShapeColor);

		toolToTest.handleDown(point);
		toolToTest.handleUp(point);

		assertFalse(toolToTest.toolInDrawMode);
		assertEquals(Color.LTGRAY, toolToTest.cursorToolSecondaryShapeColor);

		// test if color also changes if cursor already active
		toolToTest.handleDown(point);
		toolToTest.handleUp(point);
		assertTrue(toolToTest.toolInDrawMode);

		when(toolPaint.getColor()).thenReturn(Color.CYAN);
		toolToTest.changePaintColor(Color.CYAN);

		assertEquals(Color.CYAN, toolToTest.cursorToolSecondaryShapeColor);
	}
}
