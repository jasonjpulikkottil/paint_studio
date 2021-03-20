package com.jdots.paint.test.junit.tools;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.command.implementation.PathCommand;
import com.jdots.paint.command.implementation.PointCommand;
import com.jdots.paint.test.junit.stubs.PathStub;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.Tool.StateChange;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.common.Constants;
import com.jdots.paint.tools.implementation.BrushTool;
import com.jdots.paint.tools.implementation.DefaultToolPaint;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.jdots.paint.test.utils.PaintStudioAsserts.assertPaintEquals;
import static com.jdots.paint.test.utils.PaintStudioAsserts.assertPathEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrushToolTest {
	private static final float MOVE_TOLERANCE = Constants.MOVE_TOLERANCE;
	@Mock
	private CommandManager commandManager;
	@Mock
	private ToolPaint toolPaint;
	@Mock
	private BrushToolOptionsView brushToolOptionsView;
	@Mock
	private ToolOptionsVisibilityController toolOptionsViewController;
	@Mock
	private Workspace workspace;
	@Mock
	private ContextCallback contextCallback;

	private Paint paint;
	private BrushTool toolToTest;

	@Before
	public void setUp() {
		toolToTest = new BrushTool(brushToolOptionsView, contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(DefaultToolPaint.STROKE_25);
	}

	@Test
	public void testShouldReturnCorrectToolType() {
		ToolType toolType = toolToTest.getToolType();

		assertEquals(ToolType.BRUSH, toolType);
	}

	@Test
	public void testShouldMovePathOnDownEvent() {
		PointF event = new PointF(0, 0);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		boolean returnValue = toolToTest.handleDown(event);

		assertTrue(returnValue);
		Path stub = pathStub.getStub();
		verify(stub).moveTo(event.x, event.y);
	}

	@Test
	public void testShouldNotAddCommandOnDownEvent() {
		PointF event = new PointF(0, 0);

		boolean returnValue = toolToTest.handleDown(event);

		assertTrue(returnValue);
		verify(commandManager, never()).addCommand(any(Command.class));
	}

	@Test
	public void testShouldNotStartPathIfNoCoordinateOnDownEvent() {
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		boolean returnValue = toolToTest.handleDown(null);

		assertFalse(returnValue);
		Path stub = pathStub.getStub();
		verify(stub, never()).reset();
		verify(stub, never()).moveTo(anyFloat(), anyFloat());
	}

	@Test
	public void testShouldMovePathOnMoveEvent() {
		PointF event1 = new PointF(0, 0);
		PointF event2 = new PointF(5, 6);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.handleDown(event1);
		boolean returnValue = toolToTest.handleMove(event2);

		assertTrue(returnValue);
		Path stub = pathStub.getStub();
		verify(stub).moveTo(anyFloat(), anyFloat());
		verify(stub).quadTo(event1.x, event1.y, event2.x, event2.y);
	}

	@Test
	public void testShouldNotAddCommandOnMoveEvent() {
		PointF event = new PointF(0, 0);

		toolToTest.handleDown(event);
		boolean returnValue = toolToTest.handleMove(event);

		assertTrue(returnValue);
		verify(commandManager, never()).addCommand(any(Command.class));
	}

	@Test
	public void testShouldNotMovePathIfNoCoordinateOnMoveEvent() {
		PointF event = new PointF(0, 0);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.handleDown(event);
		boolean returnValue = toolToTest.handleMove(null);

		assertFalse(returnValue);
		verify(pathStub.getStub(), never()).quadTo(anyFloat(), anyFloat(), anyFloat(), anyFloat());
	}

	@Test
	public void testShouldMovePathOnUpEvent() {
		when(workspace.contains(any(PointF.class))).thenReturn(true);
		when(toolPaint.getPaint()).thenReturn(paint);
		PointF event1 = new PointF(0, 0);
		PointF event2 = new PointF(MOVE_TOLERANCE, MOVE_TOLERANCE);
		PointF event3 = new PointF(MOVE_TOLERANCE * 2, -MOVE_TOLERANCE);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.handleDown(event1);
		toolToTest.handleMove(event2);
		boolean returnValue = toolToTest.handleUp(event3);

		assertTrue(returnValue);
		Path stub = pathStub.getStub();
		verify(stub).moveTo(anyFloat(), anyFloat());
		verify(stub).quadTo(anyFloat(), anyFloat(), anyFloat(), anyFloat());
		verify(stub).lineTo(event3.x, event3.y);
	}

	@Test
	public void testShouldNotMovePathIfNoCoordinateOnUpEvent() {
		PointF event = new PointF(0, 0);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.handleDown(event);
		toolToTest.handleMove(event);
		boolean returnValue = toolToTest.handleUp(null);

		assertFalse(returnValue);
		verify(pathStub.getStub(), never()).lineTo(anyFloat(), anyFloat());
	}

	@Test
	public void testShouldAddCommandOnUpEvent() {
		when(workspace.contains(any(PointF.class))).thenReturn(true);
		when(toolPaint.getPaint()).thenReturn(paint);
		PointF event = new PointF(0, 0);
		PointF event1 = new PointF(MOVE_TOLERANCE + 0.1f, 0);
		PointF event2 = new PointF(MOVE_TOLERANCE + 2, MOVE_TOLERANCE + 2);
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.handleDown(event);
		toolToTest.handleMove(event1);
		boolean returnValue = toolToTest.handleUp(event2);

		assertTrue(returnValue);
		ArgumentCaptor<PathCommand> argument = ArgumentCaptor.forClass(PathCommand.class);
		verify(commandManager).addCommand(argument.capture());
		PathCommand command = argument.getValue();
		assertPathEquals(pathStub, command.path);
		assertPaintEquals(this.paint, command.paint);
	}

	@Test
	public void testShouldNotAddCommandIfNoCoordinateOnUpEvent() {
		PointF event = new PointF(0, 0);

		toolToTest.handleDown(event);
		toolToTest.handleMove(event);
		boolean returnValue = toolToTest.handleUp(null);

		assertFalse(returnValue);
		verify(commandManager, never()).addCommand(any(Command.class));
	}

	@Test
	public void testShouldAddCommandOnTapEvent() {
		when(workspace.contains(any(PointF.class))).thenReturn(true);
		when(toolPaint.getPaint()).thenReturn(paint);
		PointF tap = new PointF(5, 5);

		boolean returnValue1 = toolToTest.handleDown(tap);
		boolean returnValue2 = toolToTest.handleUp(tap);

		assertTrue(returnValue1);
		assertTrue(returnValue2);
		ArgumentCaptor<PointCommand> argument = ArgumentCaptor.forClass(PointCommand.class);
		verify(commandManager).addCommand(argument.capture());
		PointCommand command = argument.getValue();
		assertEquals(tap, command.point);
		assertPaintEquals(paint, command.paint);
	}

	@Test
	public void testShouldAddCommandOnTapWithinToleranceEvent() {
		PointF tap1 = new PointF(0, 0);
		PointF tap2 = new PointF(MOVE_TOLERANCE - 0.1f, 0);
		PointF tap3 = new PointF(MOVE_TOLERANCE - 0.1f, MOVE_TOLERANCE - 0.1f);
		when(workspace.contains(any(PointF.class))).thenReturn(true);
		when(toolPaint.getPaint()).thenReturn(paint);

		boolean returnValue1 = toolToTest.handleDown(tap1);
		boolean returnValue2 = toolToTest.handleMove(tap2);
		boolean returnValue3 = toolToTest.handleUp(tap3);

		assertTrue(returnValue1);
		assertTrue(returnValue2);
		assertTrue(returnValue3);
		ArgumentCaptor<PointCommand> argument = ArgumentCaptor.forClass(PointCommand.class);
		verify(commandManager).addCommand(argument.capture());
		PointCommand command = argument.getValue();
		assertEquals(tap1, command.point);
		assertPaintEquals(paint, command.paint);
	}

	@Test
	public void testShouldAddPathCommandOnMultipleMovesWithinToleranceEvent() {
		when(workspace.contains(any(PointF.class))).thenReturn(true);
		when(toolPaint.getPaint()).thenReturn(paint);
		PointF tap1 = new PointF(7, 7);
		PointF tap2 = new PointF(7, MOVE_TOLERANCE - 0.1f);
		PointF tap3 = new PointF(7, 7);
		PointF tap4 = new PointF(7, -MOVE_TOLERANCE + 0.1f);
		PointF tap5 = new PointF(7, 7);

		toolToTest.handleDown(tap1);
		toolToTest.handleMove(tap2);
		toolToTest.handleMove(tap3);
		toolToTest.handleMove(tap4);
		toolToTest.handleUp(tap5);

		verify(commandManager).addCommand(isA(PathCommand.class));
	}

	@Test
	public void testShouldRewindPathOnAppliedToBitmap() {
		PathStub pathStub = new PathStub();
		toolToTest.pathToDraw = pathStub;

		toolToTest.resetInternalState(StateChange.RESET_INTERNAL_STATE);

		verify(pathStub.getStub()).rewind();
	}

	@Test
	public void testShouldChangePaintFromBrushPicker() {
		ArgumentCaptor<BrushToolOptionsView.OnBrushChangedListener> argumentCaptor =
				ArgumentCaptor.forClass(BrushToolOptionsView.OnBrushChangedListener.class);
		verify(brushToolOptionsView).setBrushChangedListener(argumentCaptor.capture());
		BrushToolOptionsView.OnBrushChangedListener onBrushChangedListener = argumentCaptor.getValue();

		onBrushChangedListener.setCap(Paint.Cap.ROUND);
		onBrushChangedListener.setStrokeWidth(15);

		verify(toolPaint).setStrokeCap(Paint.Cap.ROUND);
		verify(toolPaint).setStrokeWidth(15f);
	}
}
