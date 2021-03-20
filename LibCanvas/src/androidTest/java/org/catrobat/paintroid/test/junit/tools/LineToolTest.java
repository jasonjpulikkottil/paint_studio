package com.jdots.paint.test.junit.tools;

import android.graphics.PointF;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.implementation.LineTool;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LineToolTest {

	private LineTool tool;
	@Mock
	private CommandManager commandManager;
	@Mock
	private ToolPaint toolPaint;
	@Mock
	private BrushToolOptionsView brushToolOptions;
	@Mock
	private ToolOptionsVisibilityController toolOptionsController;
	@Mock
	private Workspace workspace;
	@Mock
	private ContextCallback contextCallback;

	@Before
	public void setUp() {
		tool = new LineTool(brushToolOptions, contextCallback, toolOptionsController, toolPaint, workspace, commandManager);
	}

	@Test
	public void testInternalStateGetsResetWithPathOuterWorkspace() {
		tool.handleDown(new PointF(-1, -1));
		tool.handleUp(new PointF(-2, -2));

		assertEquals(tool.getCurrentCoordinate(), null);
		assertEquals(tool.getInitialEventCoordinate(), null);
	}

	@Test
	public void testInternalStateGetsResetWithPathInWorkspace() {
		tool.handleDown(new PointF(1, 1));
		tool.handleUp(new PointF(2, 2));

		assertEquals(tool.getCurrentCoordinate(), null);
		assertEquals(tool.getInitialEventCoordinate(), null);
	}
}
