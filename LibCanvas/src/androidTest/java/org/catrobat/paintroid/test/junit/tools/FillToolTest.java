package com.jdots.paint.test.junit.tools;

import com.jdots.paint.command.CommandFactory;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.implementation.FillTool;
import com.jdots.paint.tools.options.FillToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FillToolTest {
	@Mock
	public FillToolOptionsView fillToolOptions;
	@Mock
	public ContextCallback contextCallback;
	@Mock
	public ToolOptionsVisibilityController toolOptionsViewController;
	@Mock
	public Workspace workspace;
	@Mock
	public ToolPaint toolPaint;
	@Mock
	public CommandManager commandManager;
	@Mock
	public CommandFactory commandFactory;
	@Mock
	public LayerContracts.Model layerModel;
	@InjectMocks
	public FillTool toolToTest;

	@Test
	public void testShouldReturnCorrectToolType() {
		ToolType toolType = toolToTest.getToolType();
		assertEquals(ToolType.FILL, toolType);
	}
}
