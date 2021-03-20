package com.jdots.paint.tools.implementation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import com.jdots.paint.command.CommandFactory;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.command.implementation.DefaultCommandFactory;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.Tool;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.common.PointScrollBehavior;
import com.jdots.paint.tools.common.ScrollBehavior;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

import androidx.annotation.ColorInt;

public abstract class BaseTool implements Tool {
	protected final PointF movedDistance;
	protected ScrollBehavior scrollBehavior;
	protected PointF previousEventCoordinate;
	protected CommandFactory commandFactory = new DefaultCommandFactory();
	protected CommandManager commandManager;
	protected Workspace workspace;
	protected ContextCallback contextCallback;
	protected ToolOptionsVisibilityController toolOptionsViewController;
	protected ToolPaint toolPaint;

	public BaseTool(ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController,
			ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		this.contextCallback = contextCallback;
		this.toolOptionsViewController = toolOptionsViewController;
		this.toolPaint = toolPaint;
		this.workspace = workspace;
		this.commandManager = commandManager;

		int scrollTolerance = contextCallback.getScrollTolerance();
		scrollBehavior = new PointScrollBehavior(scrollTolerance);

		movedDistance = new PointF(0f, 0f);
		previousEventCoordinate = new PointF(0f, 0f);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
	}

	@Override
	public void changePaintColor(@ColorInt int color) {
		toolPaint.setColor(color);
	}

	@Override
	public void changePaintStrokeWidth(int strokeWidth) {
		toolPaint.setStrokeWidth(strokeWidth);
	}

	@Override
	public void changePaintStrokeCap(Cap cap) {
		toolPaint.setStrokeCap(cap);
	}

	@Override
	public Paint getDrawPaint() {
		return new Paint(toolPaint.getPaint());
	}

	@Override
	public abstract void draw(Canvas canvas);

	protected void resetInternalState() {
	}

	@Override
	public void resetInternalState(StateChange stateChange) {
		if (getToolType().shouldReactToStateChange(stateChange)) {
			resetInternalState();
		}
	}

	@Override
	public Point getAutoScrollDirection(float pointX, float pointY, int viewWidth, int viewHeight) {
		return scrollBehavior.getScrollDirection(pointX, pointY, viewWidth, viewHeight);
	}

	public boolean handToolMode() {
		return false;
	}
}
