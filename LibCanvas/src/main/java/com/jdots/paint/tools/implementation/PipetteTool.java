package com.jdots.paint.tools.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

import com.jdots.paint.colorpicker.OnColorPickedListener;

public class PipetteTool extends BaseTool {

	private Bitmap surfaceBitmap;
	private OnColorPickedListener listener;

	public PipetteTool(ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController,
                       ToolPaint toolPaint, Workspace workspace, CommandManager commandManager, OnColorPickedListener listener) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		this.listener = listener;

		updateSurfaceBitmap();
	}

	@Override
	public void draw(Canvas canvas) {
	}

	@Override
	public ToolType getToolType() {
		return ToolType.PIPETTE;
	}

	@Override
	public boolean handleDown(PointF coordinate) {
		return setColor(coordinate);
	}

	@Override
	public boolean handleMove(PointF coordinate) {
		return setColor(coordinate);
	}

	@Override
	public boolean handleUp(PointF coordinate) {
		return setColor(coordinate);
	}

	protected boolean setColor(PointF coordinate) {
		if (coordinate == null || surfaceBitmap == null) {
			return false;
		}

		if (!workspace.contains(coordinate)) {
			return false;
		}

		int color = surfaceBitmap.getPixel((int) coordinate.x, (int) coordinate.y);

		listener.colorChanged(color);
		changePaintColor(color);
		return true;
	}

	public void updateSurfaceBitmap() {
		surfaceBitmap = workspace.getBitmapOfAllLayers();
	}

	@Override
	public void resetInternalState() {
		updateSurfaceBitmap();
	}
}
