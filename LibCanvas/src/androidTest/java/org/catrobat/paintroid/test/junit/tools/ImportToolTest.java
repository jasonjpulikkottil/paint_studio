package com.jdots.paint.test.junit.tools;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.implementation.ImportTool;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import com.jdots.paint.ui.Perspective;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.test.annotation.UiThreadTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ImportToolTest {
	@Mock
	private CommandManager commandManager;
	@Mock
	private Workspace workspace;
	@Mock
	private ToolPaint toolPaint;
	@Mock
	private ToolOptionsVisibilityController toolOptionsViewController;
	@Mock
	private ContextCallback contextCallback;
	@Mock
	private DisplayMetrics displayMetrics;

	private int drawingSurfaceWidth;
	private int drawingSurfaceHeight;

	private ImportTool tool;

	@UiThreadTest
	@Before
	public void setUp() {
		drawingSurfaceWidth = 20;
		drawingSurfaceHeight = 30;

		displayMetrics.heightPixels = 1920;
		displayMetrics.widthPixels = 1080;
		displayMetrics.density = 1;

		when(contextCallback.getDisplayMetrics()).thenReturn(displayMetrics);
		when(workspace.getWidth()).thenReturn(20);
		when(workspace.getHeight()).thenReturn(30);
		when(workspace.getScale()).thenReturn(1f);
		when(workspace.getPerspective()).thenReturn(new Perspective(20, 30));

		tool = new ImportTool(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
	}

	@Test
	public void testImport() {
		final int width = drawingSurfaceWidth;
		final int height = drawingSurfaceHeight;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromSource(bitmap);

		assertEquals(width, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(height, tool.boxHeight, Float.MIN_VALUE);
	}

	@Test
	public void testImportTooSmall() {
		final int width = 1;
		final int height = 1;
		final int minSize = ImportTool.DEFAULT_BOX_RESIZE_MARGIN;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromSource(bitmap);

		assertEquals(minSize, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(minSize, tool.boxHeight, Float.MIN_VALUE);
	}

	@Test
	public void testImportTooLarge() {
		final int width = (int) (drawingSurfaceWidth * ImportTool.MAXIMUM_BORDER_RATIO);
		final int height = (int) (drawingSurfaceHeight * ImportTool.MAXIMUM_BORDER_RATIO);

		Bitmap bitmap = Bitmap.createBitmap(width + 1, height + 1, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromSource(bitmap);

		assertEquals(width, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(height, tool.boxHeight, Float.MIN_VALUE);
	}
}
