package com.jdots.paint.tools.implementation;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.StampToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

import com.jdots.paint.R;

public class StampTool extends BaseToolWithRectangleShape {

	private static final String BUNDLE_TOOL_READY_FOR_PASTE = "BUNDLE_TOOL_READY_FOR_PASTE";
	private static final String BUNDLE_TOOL_DRAWING_BITMAP = "BUNDLE_TOOL_DRAWING_BITMAP";
	private static final boolean ROTATION_ENABLED = true;
	private final StampToolOptionsView stampToolOptionsView;
	protected boolean readyForPaste;

	public StampTool(StampToolOptionsView stampToolOptionsView, ContextCallback contextCallback, ToolOptionsVisibilityController toolOptionsViewController,
                     ToolPaint toolPaint, Workspace workspace, CommandManager commandManager) {
		super(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
		readyForPaste = false;
		this.rotationEnabled = ROTATION_ENABLED;
		this.stampToolOptionsView = stampToolOptionsView;

		setBitmap(Bitmap.createBitmap((int) boxWidth, (int) boxHeight, Config.ARGB_8888));

		if (stampToolOptionsView != null) {
			StampToolOptionsView.Callback callback =
					new StampToolOptionsView.Callback() {
						@Override
						public void copyClicked() {
							highlightBox();
							copyBoxContent();
							StampTool.this.stampToolOptionsView.enablePaste(true);
						}

						@Override
						public void cutClicked() {
							highlightBox();
							copyBoxContent();
							cutBoxContent();
							StampTool.this.stampToolOptionsView.enablePaste(true);
						}

						@Override
						public void pasteClicked() {
							highlightBox();
							pasteBoxContent();
						}
					};

			stampToolOptionsView.setCallback(callback);
		}

		toolOptionsViewController.showDelayed();
	}

	public void copyBoxContent() {
		if (isDrawingBitmapReusable()) {
			drawingBitmap.eraseColor(Color.TRANSPARENT);
		} else {
			drawingBitmap = Bitmap.createBitmap((int) boxWidth, (int) boxHeight, Config.ARGB_8888);
		}

		Bitmap layerBitmap = workspace.getBitmapOfCurrentLayer();

		Canvas canvas = new Canvas(drawingBitmap);
		canvas.translate(-toolPosition.x + boxWidth / 2, -toolPosition.y + boxHeight / 2);
		canvas.rotate(-boxRotation, toolPosition.x, toolPosition.y);
		canvas.drawBitmap(layerBitmap, 0, 0, null);

		readyForPaste = true;
	}

	private void pasteBoxContent() {
		Command command = commandFactory.createStampCommand(drawingBitmap, toolPosition, boxWidth, boxHeight, boxRotation);
		commandManager.addCommand(command);
	}

	private void cutBoxContent() {
		Command command = commandFactory.createCutCommand(toolPosition, boxWidth, boxHeight, boxRotation);
		commandManager.addCommand(command);
	}

	@Override
	public ToolType getToolType() {
		return ToolType.STAMP;
	}

	@Override
	public void onClickOnButton() {
		if (!readyForPaste || drawingBitmap == null) {
			contextCallback.showNotification(R.string.stamp_tool_copy_hint);
		} else if (boxIntersectsWorkspace()) {
			pasteBoxContent();
			highlightBox();
		}
	}

	@Override
	public void resetInternalState() {
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putParcelable(BUNDLE_TOOL_DRAWING_BITMAP, drawingBitmap);
		bundle.putBoolean(BUNDLE_TOOL_READY_FOR_PASTE, readyForPaste);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);
		readyForPaste = bundle.getBoolean(BUNDLE_TOOL_READY_FOR_PASTE, readyForPaste);
		Bitmap bitmap = bundle.getParcelable(BUNDLE_TOOL_DRAWING_BITMAP);
		if (bitmap != null) {
			drawingBitmap = bitmap;
		}

		stampToolOptionsView.enablePaste(readyForPaste);
	}

	private boolean isDrawingBitmapReusable() {
		return drawingBitmap != null
				&& drawingBitmap.getWidth() == (int) boxWidth
				&& drawingBitmap.getHeight() == (int) boxHeight;
	}
}
