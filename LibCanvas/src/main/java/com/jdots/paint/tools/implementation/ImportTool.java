package com.jdots.paint.tools.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

public class ImportTool extends BaseToolWithRectangleShape {
	private static final String BUNDLE_TOOL_DRAWING_BITMAP = "BUNDLE_TOOL_DRAWING_BITMAP";

	public ImportTool(ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController,
                      ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		rotationEnabled = true;
	}

	@Override
	public void drawShape(Canvas canvas) {
		if (drawingBitmap != null) {
			super.drawShape(canvas);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelable(BUNDLE_TOOL_DRAWING_BITMAP, drawingBitmap);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		Bitmap bitmap = bundle.getParcelable(BUNDLE_TOOL_DRAWING_BITMAP);
		if (bitmap != null) {
			drawingBitmap = bitmap;
		}
	}

	@Override
	public void onClickOnButton() {
		highlightBox();
		Command command = commandFactory.createStampCommand(drawingBitmap, toolPosition, boxWidth, boxHeight, boxRotation);
		commandManager.addCommand(command);
	}

	public void setBitmapFromSource(Bitmap bitmap) {
		super.setBitmap(bitmap);

		final float maximumBorderRatioWidth = MAXIMUM_BORDER_RATIO * workspace.getWidth();
		final float maximumBorderRatioHeight = MAXIMUM_BORDER_RATIO * workspace.getHeight();

		final float minimumSize = DEFAULT_BOX_RESIZE_MARGIN;

		boxWidth = Math.max(minimumSize, Math.min(maximumBorderRatioWidth, bitmap.getWidth()));
		boxHeight = Math.max(minimumSize, Math.min(maximumBorderRatioHeight, bitmap.getHeight()));
	}

	@Override
	public ToolType getToolType() {
		return ToolType.IMPORTPNG;
	}
}
