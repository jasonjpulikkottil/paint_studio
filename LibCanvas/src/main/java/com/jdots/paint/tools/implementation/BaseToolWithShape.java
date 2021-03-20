package com.jdots.paint.tools.implementation;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.jdots.paint.R;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;
import com.jdots.paint.ui.Perspective;

import androidx.annotation.VisibleForTesting;

public abstract class BaseToolWithShape extends BaseTool {

	private static final String BUNDLE_TOOL_POSITION_X = "TOOL_POSITION_X";
	private static final String BUNDLE_TOOL_POSITION_Y = "TOOL_POSITION_Y";

	@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
	public final PointF toolPosition;

	int primaryShapeColor;
	int secondaryShapeColor;

	final Paint linePaint;
	final DisplayMetrics metrics;

	@SuppressLint("VisibleForTests")
	public BaseToolWithShape(ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController, ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);

		metrics = contextCallback.getDisplayMetrics();

		primaryShapeColor = contextCallback.getColor(R.color.paint_studio_main_rectangle_tool_primary_color);
		secondaryShapeColor = contextCallback.getColor(R.color.paint_studio_colorAccent);
		Perspective perspective = workspace.getPerspective();

		if (perspective.getScale() > 1) {
			toolPosition = new PointF(perspective.surfaceCenterX - perspective.surfaceTranslationX, perspective.surfaceCenterY - perspective.surfaceTranslationY);
		} else {
			toolPosition = new PointF(workspace.getWidth() / 2f, workspace.getHeight() / 2f);
		}

		linePaint = new Paint();
		linePaint.setColor(primaryShapeColor);
	}

	public abstract void drawShape(Canvas canvas);

	float getStrokeWidthForZoom(float defaultStrokeWidth, float minStrokeWidth, float maxStrokeWidth) {
		float strokeWidth = (defaultStrokeWidth * metrics.density) / workspace.getScale();
		return Math.min(maxStrokeWidth, Math.max(minStrokeWidth, strokeWidth));
	}

	float getInverselyProportionalSizeForZoom(float defaultSize) {
		float applicationScale = workspace.getScale();
		return (defaultSize * metrics.density) / applicationScale;
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);

		bundle.putFloat(BUNDLE_TOOL_POSITION_X, toolPosition.x);
		bundle.putFloat(BUNDLE_TOOL_POSITION_Y, toolPosition.y);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);

		toolPosition.x = bundle.getFloat(BUNDLE_TOOL_POSITION_X, toolPosition.x);
		toolPosition.y = bundle.getFloat(BUNDLE_TOOL_POSITION_Y, toolPosition.y);
	}

	@Override
	public Point getAutoScrollDirection(float pointX, float pointY, int viewWidth, int viewHeight) {
		PointF surfaceToolPosition = workspace.getSurfacePointFromCanvasPoint(toolPosition);
		return scrollBehavior.getScrollDirection(surfaceToolPosition.x, surfaceToolPosition.y, viewWidth, viewHeight);
	}

	public abstract void onClickOnButton();

	protected void drawToolSpecifics(Canvas canvas, float boxWidth, float boxHeight) {
	}
}
