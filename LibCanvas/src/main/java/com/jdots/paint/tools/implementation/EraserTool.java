package com.jdots.paint.tools.implementation;

import android.graphics.Color;
import android.graphics.Paint;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.ToolOptionsVisibilityController;

public class EraserTool extends BrushTool {
	private Paint previewPaint = new Paint();
	private Paint bitmapPaint = new Paint();

	public EraserTool(BrushToolOptionsView brushToolOptionsView, ContextCallback contextCallback,
                      ToolOptionsVisibilityController toolOptionsViewController, ToolPaint toolPaint, Workspace workspace,
                      CommandManager commandManager) {
		super(brushToolOptionsView, contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager);
	}

	@Override
	protected Paint getPreviewPaint() {
		previewPaint.set(super.getPreviewPaint());
		previewPaint.setColor(Color.BLACK);
		previewPaint.setShader(toolPaint.getCheckeredShader());
		return previewPaint;
	}

	@Override
	protected Paint getBitmapPaint() {
		bitmapPaint.set(super.getBitmapPaint());
		bitmapPaint.setXfermode(toolPaint.getEraseXfermode());
		bitmapPaint.setAlpha(0);
		return bitmapPaint;
	}

	@Override
	public ToolType getToolType() {
		return ToolType.ERASER;
	}
}
