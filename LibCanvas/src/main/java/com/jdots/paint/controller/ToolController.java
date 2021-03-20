package com.jdots.paint.controller;

import android.graphics.Bitmap;

import com.jdots.paint.colorpicker.OnColorPickedListener;
import com.jdots.paint.tools.ToolType;

public interface ToolController {
	void setOnColorPickedListener(OnColorPickedListener onColorPickedListener);

	void switchTool(ToolType toolType, boolean backPressed);

	boolean isDefaultTool();

	void hideToolOptionsView();

	void showToolOptionsView();

	boolean toolOptionsViewVisible();

	void resetToolInternalState();

	void resetToolInternalStateOnImageLoaded();

	int getToolColor();

	ToolType getToolType();

	void disableToolOptionsView();

	void enableToolOptionsView();

	void createTool();

	void toggleToolOptionsView();

	boolean hasToolOptionsView();

	void setBitmapFromSource(Bitmap bitmap);
}
