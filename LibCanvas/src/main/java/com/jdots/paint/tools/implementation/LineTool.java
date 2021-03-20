package com.jdots.paint.tools.implementation;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.common.CommonBrushChangedListener;
import com.jdots.paint.tools.common.CommonBrushPreviewListener;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

import androidx.annotation.VisibleForTesting;

public class LineTool extends BaseTool {

	private PointF initialEventCoordinate;
	private PointF currentCoordinate;
	private BrushToolOptionsView brushToolOptionsView;

	public LineTool(BrushToolOptionsView brushToolOptionsView, ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController,
                    ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		this.brushToolOptionsView = brushToolOptionsView;

		brushToolOptionsView.setBrushChangedListener(new CommonBrushChangedListener(this));
		brushToolOptionsView.setBrushPreviewListener(new CommonBrushPreviewListener(toolPaint, getToolType()));
		brushToolOptionsView.setCurrentPaint(toolPaint.getPaint());
	}

	@Override
	public void draw(Canvas canvas) {
		if (initialEventCoordinate == null || currentCoordinate == null) {
			return;
		}

		canvas.save();
		canvas.clipRect(0, 0, workspace.getWidth(), workspace.getHeight());
		canvas.drawLine(initialEventCoordinate.x,
				initialEventCoordinate.y, currentCoordinate.x,
				currentCoordinate.y, toolPaint.getPreviewPaint());
		canvas.restore();
	}

	@Override
	public ToolType getToolType() {
		return ToolType.LINE;
	}

	@Override
	public boolean handleDown(PointF coordinate) {
		if (coordinate == null) {
			return false;
		}
		initialEventCoordinate = new PointF(coordinate.x, coordinate.y);
		previousEventCoordinate = new PointF(coordinate.x, coordinate.y);
		return true;
	}

	@Override
	public boolean handleMove(PointF coordinate) {
		currentCoordinate = new PointF(coordinate.x, coordinate.y);
		return true;
	}

	@Override
	public boolean handleUp(PointF coordinate) {
		if (initialEventCoordinate == null || previousEventCoordinate == null
				|| coordinate == null) {
			return false;
		}
		Path finalPath = new Path();
		finalPath.moveTo(initialEventCoordinate.x, initialEventCoordinate.y);
		finalPath.lineTo(coordinate.x, coordinate.y);

		RectF bounds = new RectF();
		finalPath.computeBounds(bounds, true);
		bounds.inset(-toolPaint.getStrokeWidth(), -toolPaint.getStrokeWidth());

		if (workspace.intersectsWith(bounds)) {
			Command command = commandFactory.createPathCommand(toolPaint.getPaint(), finalPath);
			commandManager.addCommand(command);
		}
		resetInternalState();
		return true;
	}

	@Override
	public void resetInternalState() {
		initialEventCoordinate = null;
		currentCoordinate = null;
	}

	@Override
	public void changePaintColor(int color) {
		super.changePaintColor(color);
		brushToolOptionsView.invalidate();
	}

	@VisibleForTesting (otherwise = VisibleForTesting.NONE)
	public PointF getInitialEventCoordinate() {
		return initialEventCoordinate;
	}

	@VisibleForTesting (otherwise = VisibleForTesting.NONE)
	public PointF getCurrentCoordinate() {
		return currentCoordinate;
	}
}
