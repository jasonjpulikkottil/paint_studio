package com.jdots.paint.tools.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

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

import com.jdots.paint.tools.common.Constants;

public class BrushTool extends BaseTool {

	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public Path pathToDraw;
	private final PointF drawToolMovedDistance;
	private PointF initialEventCoordinate;
	private boolean pathInsideBitmap;
	private BrushToolOptionsView brushToolOptionsView;

	public BrushTool(BrushToolOptionsView brushToolOptionsView, ContextCallback contextCallback,
                     ToolOptionsVisibilityController toolOptionsViewController, ToolPaint toolPaint, Workspace workspace,
                     CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		this.brushToolOptionsView = brushToolOptionsView;

		pathToDraw = new Path();
		pathToDraw.incReserve(1);
		drawToolMovedDistance = new PointF(0f, 0f);
		pathInsideBitmap = false;

		brushToolOptionsView.setBrushChangedListener(new CommonBrushChangedListener(this));
		brushToolOptionsView.setBrushPreviewListener(new CommonBrushPreviewListener(toolPaint, getToolType()));
		brushToolOptionsView.setCurrentPaint(toolPaint.getPaint());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.clipRect(0, 0, workspace.getWidth(), workspace.getHeight());
		canvas.drawPath(pathToDraw, getPreviewPaint());
		canvas.restore();
	}

	@Override
	public ToolType getToolType() {
		return ToolType.BRUSH;
	}

	@Override
	public boolean handleDown(PointF coordinate) {
		if (coordinate == null) {
			return false;
		}
		initialEventCoordinate = new PointF(coordinate.x, coordinate.y);
		previousEventCoordinate = new PointF(coordinate.x, coordinate.y);
		pathToDraw.moveTo(coordinate.x, coordinate.y);
		drawToolMovedDistance.set(0, 0);
		pathInsideBitmap = false;

		pathInsideBitmap = workspace.contains(coordinate);
		return true;
	}

	@Override
	public boolean handleMove(PointF coordinate) {
		if (initialEventCoordinate == null || previousEventCoordinate == null || coordinate == null) {
			return false;
		}
		pathToDraw.quadTo(previousEventCoordinate.x,
				previousEventCoordinate.y, coordinate.x, coordinate.y);
		pathToDraw.incReserve(1);
		drawToolMovedDistance.set(
				drawToolMovedDistance.x + Math.abs(coordinate.x - previousEventCoordinate.x),
				drawToolMovedDistance.y + Math.abs(coordinate.y - previousEventCoordinate.y));
		previousEventCoordinate.set(coordinate.x, coordinate.y);

		if (!pathInsideBitmap && workspace.contains(coordinate)) {
			pathInsideBitmap = true;
		}
		return true;
	}

	@Override
	public boolean handleUp(PointF coordinate) {
		if (initialEventCoordinate == null || previousEventCoordinate == null || coordinate == null) {
			return false;
		}

		if (!pathInsideBitmap && workspace.contains(coordinate)) {
			pathInsideBitmap = true;
		}

		drawToolMovedDistance.set(
				drawToolMovedDistance.x + Math.abs(coordinate.x - previousEventCoordinate.x),
				drawToolMovedDistance.y + Math.abs(coordinate.y - previousEventCoordinate.y));
		boolean returnValue;
		if (Constants.MOVE_TOLERANCE < drawToolMovedDistance.x || Constants.MOVE_TOLERANCE < drawToolMovedDistance.y) {
			returnValue = addPathCommand(coordinate);
		} else {
			returnValue = addPointCommand(initialEventCoordinate);
		}
		return returnValue;
	}

	protected Paint getPreviewPaint() {
		return toolPaint.getPreviewPaint();
	}

	protected Paint getBitmapPaint() {
		return toolPaint.getPaint();
	}

	private boolean addPathCommand(PointF coordinate) {
		pathToDraw.lineTo(coordinate.x, coordinate.y);
		if (!pathInsideBitmap) {
			resetInternalState(StateChange.RESET_INTERNAL_STATE);
			return false;
		}
		Command command = commandFactory.createPathCommand(getBitmapPaint(), pathToDraw);
		commandManager.addCommand(command);
		return true;
	}

	private boolean addPointCommand(PointF coordinate) {
		if (!pathInsideBitmap) {
			resetInternalState(StateChange.RESET_INTERNAL_STATE);
			return false;
		}
		Command command = commandFactory.createPointCommand(getBitmapPaint(), coordinate);
		commandManager.addCommand(command);
		return true;
	}

	@Override
	public void resetInternalState() {
		pathToDraw.rewind();
		initialEventCoordinate = null;
		previousEventCoordinate = null;
	}

	@Override
	public void changePaintColor(int color) {
		super.changePaintColor(color);
		brushToolOptionsView.invalidate();
	}
}
