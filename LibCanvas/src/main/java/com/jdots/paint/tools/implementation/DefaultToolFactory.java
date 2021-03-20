package com.jdots.paint.tools.implementation;

import android.view.ViewGroup;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.Tool;
import com.jdots.paint.tools.ToolFactory;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.FillToolOptionsView;
import com.jdots.paint.tools.options.ShapeToolOptionsView;
import com.jdots.paint.tools.options.SprayToolOptionsView;
import com.jdots.paint.tools.options.StampToolOptionsView;
import com.jdots.paint.tools.options.TextToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsViewController;
import com.jdots.paint.tools.options.TransformToolOptionsView;
import com.jdots.paint.ui.tools.DefaultBrushToolOptionsView;
import com.jdots.paint.ui.tools.DefaultFillToolOptionsView;
import com.jdots.paint.ui.tools.DefaultShapeToolOptionsView;
import com.jdots.paint.ui.tools.DefaultSprayToolOptionsView;
import com.jdots.paint.ui.tools.DefaultStampToolOptionsView;
import com.jdots.paint.ui.tools.DefaultTextToolOptionsView;
import com.jdots.paint.ui.tools.DefaultTransformToolOptionsView;

import com.jdots.paint.colorpicker.OnColorPickedListener;

public class DefaultToolFactory implements ToolFactory {

	@Override
	public Tool createTool(ToolType toolType, ToolOptionsViewController toolOptionsViewController, CommandManager commandManager, Workspace workspace, ToolPaint toolPaint, ContextCallback contextCallback, OnColorPickedListener onColorPickedListener) {
		ViewGroup toolLayout = toolOptionsViewController.getToolSpecificOptionsLayout();

		switch (toolType) {
			case CURSOR:
				return new CursorTool(createBrushToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case STAMP:
				return new StampTool(createStampToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case IMPORTPNG:
				return new ImportTool(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case PIPETTE:
				return new PipetteTool(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager, onColorPickedListener);
			case FILL:
				return new FillTool(createFillToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case TRANSFORM:
				return new TransformTool(createTransformToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case SHAPE:
				return new ShapeTool(createShapeToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case ERASER:
				return new EraserTool(createBrushToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case LINE:
				return new LineTool(createBrushToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case TEXT:
				return new TextTool(createTextToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case HAND:
				return new HandTool(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			case SPRAY:
				return new SprayTool(createSprayToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
			default:
				return new BrushTool(createBrushToolOptionsView(toolLayout), contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		}
	}

	private BrushToolOptionsView createBrushToolOptionsView(ViewGroup toolLayout) {
		return new DefaultBrushToolOptionsView(toolLayout);
	}

	private FillToolOptionsView createFillToolOptionsView(ViewGroup toolLayout) {
		return new DefaultFillToolOptionsView(toolLayout);
	}

	private ShapeToolOptionsView createShapeToolOptionsView(ViewGroup toolLayout) {
		return new DefaultShapeToolOptionsView(toolLayout);
	}

	private TextToolOptionsView createTextToolOptionsView(ViewGroup toolLayout) {
		return new DefaultTextToolOptionsView(toolLayout);
	}

	private TransformToolOptionsView createTransformToolOptionsView(ViewGroup toolLayout) {
		return new DefaultTransformToolOptionsView(toolLayout);
	}

	private StampToolOptionsView createStampToolOptionsView(ViewGroup toolLayout) {
		return new DefaultStampToolOptionsView(toolLayout);
	}

	private SprayToolOptionsView createSprayToolOptionsView(ViewGroup toolLayout) {
		return new DefaultSprayToolOptionsView(toolLayout);
	}
}
