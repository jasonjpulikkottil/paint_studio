package com.jdots.paint.test.junit.tools;

import android.util.DisplayMetrics;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.implementation.ShapeTool;
import com.jdots.paint.tools.options.ShapeToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import com.jdots.paint.ui.Perspective;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ShapeToolTest {
	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();
	@Parameter
	public DrawableShape shape;
	@Mock
	private CommandManager commandManager;
	@Mock
	private ShapeToolOptionsView shapeToolOptions;
	@Mock
	private ToolOptionsVisibilityController toolOptionsViewController;
	@Mock
	private ContextCallback contextCallback;
	@Mock
	private Workspace workspace;
	@Mock
	private ToolPaint toolPaint;
	@Mock
	private DisplayMetrics displayMetrics;
	private ShapeTool shapeTool;

	@Parameters(name = "{0}")
	public static Iterable<DrawableShape> data() {
		return Arrays.asList(
				DrawableShape.RECTANGLE,
				DrawableShape.OVAL,
				DrawableShape.HEART,
				DrawableShape.STAR
		);
	}

	@Before
	public void setUp() {
		when(workspace.getWidth()).thenReturn(100);
		when(workspace.getHeight()).thenReturn(100);
		when(workspace.getScale()).thenReturn(1f);
		when(workspace.getPerspective()).thenReturn(new Perspective(100, 100));

		when(contextCallback.getDisplayMetrics()).thenReturn(displayMetrics);
		displayMetrics.widthPixels = 100;
		displayMetrics.heightPixels = 100;

		shapeTool = new ShapeTool(shapeToolOptions, contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		shapeTool.setBaseShape(shape);
	}

	@Test
	public void testShouldReturnCorrectToolType() {
		ToolType toolType = shapeTool.getToolType();
		assertEquals(ToolType.SHAPE, toolType);
	}

	@Test
	public void testShouldReturnCorrectBaseShape() {
		DrawableShape baseShape = shapeTool.getBaseShape();
		assertEquals(shape, baseShape);
	}
}
