package com.jdots.paint.tools;

import com.jdots.paint.command.CommandManager;

import com.jdots.paint.colorpicker.OnColorPickedListener;

import com.jdots.paint.tools.options.ToolOptionsViewController;

public interface ToolFactory {
	Tool createTool(ToolType toolType, ToolOptionsViewController toolOptionsViewController, CommandManager commandManager,
			Workspace workspace, ToolPaint toolPaint, ContextCallback contextCallback,
			OnColorPickedListener onColorPickedListener);
}
