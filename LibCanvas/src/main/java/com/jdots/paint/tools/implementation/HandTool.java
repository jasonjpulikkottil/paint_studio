package com.jdots.paint.tools.implementation;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.ToolOptionsViewController;

public class HandTool extends BaseTool {
	public HandTool(ContextCallback contextCallback, ToolOptionsViewController toolOptionsViewController,
                    ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
	}

	@Override
	public void draw(Canvas canvas) { }

	@Override
	public void resetInternalState() { }

	@Override
	public boolean handleDown(PointF coordinate) {
		return true;
	}

	@Override
	public boolean handleMove(PointF coordinate) {
		return true;
	}

	@Override
	public boolean handleUp(PointF coordinate) {
		return true;
	}

	@Override
	public ToolType getToolType() {
		return ToolType.HAND;
	}

	@Override
	public boolean handToolMode() {
		return true;
	}
}
