package com.jdots.paint.test.controller;

import android.graphics.Color;
import android.graphics.Paint;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.controller.DefaultToolController;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.Tool;
import com.jdots.paint.tools.ToolFactory;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolReference;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.ToolOptionsViewController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.jdots.paint.tools.Tool.StateChange.NEW_IMAGE_LOADED;
import static com.jdots.paint.tools.Tool.StateChange.RESET_INTERNAL_STATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ToolControllerTest {
	@Mock
	public ToolReference toolReference;
	@Mock
	public ToolOptionsViewController toolOptionsViewController;
	@Mock
	public ToolFactory toolFactory;
	@Mock
	public CommandManager commandManager;
	@Mock
	public Workspace workspace;
	@Mock
	public ToolPaint toolPaint;
	@Mock
	public ContextCallback contextCallback;

	@InjectMocks
	public DefaultToolController toolController;

	@Test
	public void testSetUp() {
		verifyZeroInteractions(toolReference, toolOptionsViewController, toolFactory, commandManager, workspace, toolPaint,
				contextCallback);
		assertNotNull(toolController);
	}

	@Test
	public void testIsBrushDefaultTool() {
		Tool tool = mock(Tool.class);
		when(tool.getToolType()).thenReturn(ToolType.BRUSH);
		when(toolReference.get()).thenReturn(tool);

		assertTrue(toolController.isDefaultTool());
	}

	@Test
	public void testAllOtherToolsAreNotDefault() {
		Tool tool = mock(Tool.class);
		when(tool.getToolType()).thenReturn(
				ToolType.PIPETTE,
				ToolType.UNDO,
				ToolType.REDO,
				ToolType.FILL,
				ToolType.STAMP,
				ToolType.LINE,
				ToolType.CURSOR,
				ToolType.IMPORTPNG,
				ToolType.TRANSFORM,
				ToolType.ERASER,
				ToolType.SHAPE,
				ToolType.TEXT,
				ToolType.LAYER,
				ToolType.COLORCHOOSER,
				ToolType.HAND
		);
		when(toolReference.get()).thenReturn(tool);

		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
		assertFalse(toolController.isDefaultTool());
	}

	@Test
	public void testHideToolOptionsCallsToolOptionsViewController() {
		toolController.hideToolOptionsView();

		verify(toolOptionsViewController).hide();
		verifyNoMoreInteractions(toolOptionsViewController);
	}

	@Test
	public void testToolOptionsViewControllerWhenOptionsVisibleReturnsTrue() {
		when(toolOptionsViewController.isVisible()).thenReturn(true);

		assertTrue(toolController.toolOptionsViewVisible());
	}

	@Test
	public void testToolOptionsViewControllerWhenOptionsNotVisibleReturnsFalse() {
		assertFalse(toolController.toolOptionsViewVisible());
	}

	@Test
	public void testResetToolInternalStateCallsResetInternalState() {
		Tool tool = mock(Tool.class);
		when(toolReference.get()).thenReturn(tool);

		toolController.resetToolInternalState();

		verify(tool).resetInternalState(RESET_INTERNAL_STATE);
	}

	@Test
	public void testResetToolInternalStateOnImageLoadedCallsResetInternalState() {
		Tool tool = mock(Tool.class);
		when(toolReference.get()).thenReturn(tool);

		toolController.resetToolInternalStateOnImageLoaded();

		verify(tool).resetInternalState(NEW_IMAGE_LOADED);
	}

	@Test
	public void testGetToolColorReturnsColor() {
		Tool tool = mock(Tool.class);
		Paint paint = mock(Paint.class);
		when(toolReference.get()).thenReturn(tool);
		when(tool.getDrawPaint()).thenReturn(paint);
		when(paint.getColor()).thenReturn(Color.CYAN);

		assertEquals(Color.CYAN, toolController.getToolColor());
	}

	@Test
	public void testGetToolTypeReturnsToolType() {
		Tool tool = mock(Tool.class);
		when(toolReference.get()).thenReturn(tool);
		when(tool.getToolType()).thenReturn(ToolType.BRUSH, ToolType.ERASER);

		assertEquals(ToolType.BRUSH, toolController.getToolType());
		assertEquals(ToolType.ERASER, toolController.getToolType());
	}

	@Test
	public void testDisableToolOptionsCallsOptions() {
		toolController.disableToolOptionsView();

		verify(toolOptionsViewController).disable();
	}

	@Test
	public void testEnableToolOptionsCallsOptions() {
		toolController.enableToolOptionsView();

		verify(toolOptionsViewController).enable();
	}

	@Test
	public void testToggleToolOptionsWhenNotVisibleThenShowOptions() {
		toolController.toggleToolOptionsView();

		verify(toolOptionsViewController).show();
	}

	@Test
	public void testToggleToolOptionsWhenVisibleThenHideOptions() {
		when(toolOptionsViewController.isVisible()).thenReturn(true);

		toolController.toggleToolOptionsView();

		verify(toolOptionsViewController).hide();
	}

	@Test
	public void testHasToolOptions() {
		Tool mock = mock(Tool.class);
		when(toolReference.get()).thenReturn(mock);
		when(mock.getToolType()).thenReturn(ToolType.BRUSH, ToolType.IMPORTPNG);

		assertTrue(toolController.hasToolOptionsView());
		assertFalse(toolController.hasToolOptionsView());
	}
}
