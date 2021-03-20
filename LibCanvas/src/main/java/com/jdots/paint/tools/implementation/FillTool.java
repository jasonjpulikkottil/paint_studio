package com.jdots.paint.tools.implementation;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.FillToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

import androidx.annotation.VisibleForTesting;

public class FillTool extends BaseTool {

	public static final int DEFAULT_TOLERANCE_IN_PERCENT = 12;
	public static final int MAX_ABSOLUTE_TOLERANCE = 510;

	@VisibleForTesting
	public float colorTolerance = MAX_ABSOLUTE_TOLERANCE * DEFAULT_TOLERANCE_IN_PERCENT / 100.0f;

	public FillTool(FillToolOptionsView fillToolOptionsView, ContextCallback contextCallback,
                    ToolOptionsVisibilityController toolOptionsViewController, ToolPaint toolPaint, Workspace workspace,
                    CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);

		fillToolOptionsView.setCallback(new FillToolOptionsView.Callback() {
			@Override
			public void onColorToleranceChanged(int colorTolerance) {
				updateColorTolerance(colorTolerance);
			}
		});
	}

	public void updateColorTolerance(int colorToleranceInPercent) {
		colorTolerance = getToleranceAbsoluteValue(colorToleranceInPercent);
	}

	public float getToleranceAbsoluteValue(int toleranceInPercent) {
		return MAX_ABSOLUTE_TOLERANCE * toleranceInPercent / 100.0f;
	}

	@Override
	public boolean handleDown(PointF coordinate) {
		return false;
	}

	@Override
	public boolean handleMove(PointF coordinate) {
		return false;
	}

	@Override
	public boolean handleUp(PointF coordinate) {
		if (workspace.contains(coordinate)) {
			Command command = commandFactory.createFillCommand((int) coordinate.x, (int) coordinate.y, toolPaint.getPaint(), colorTolerance);
			commandManager.addCommand(command);
			return true;
		}

		return false;
	}

	@Override
	public void resetInternalState() {
	}

	@Override
	public void draw(Canvas canvas) {
	}

	@Override
	public ToolType getToolType() {
		return ToolType.FILL;
	}
}
