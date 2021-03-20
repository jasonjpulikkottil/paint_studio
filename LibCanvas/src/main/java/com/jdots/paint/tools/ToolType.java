package com.jdots.paint.tools;

import com.jdots.paint.R;

import java.util.EnumSet;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import com.jdots.paint.common.Constants;

public enum ToolType {
	PIPETTE(R.string.button_pipette, R.string.help_content_eyedropper, R.drawable.ic_paint_studio_tool_pipette, EnumSet.of(Tool.StateChange.ALL), 1, Constants.INVALID_RESOURCE_ID, false),
	BRUSH(R.string.button_brush, R.string.help_content_brush, R.drawable.ic_paint_studio_tool_brush, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_brush, Constants.INVALID_RESOURCE_ID, true),
	UNDO(R.string.button_undo, R.string.help_content_undo, R.drawable.ic_paint_studio_undo, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_btn_top_undo, Constants.INVALID_RESOURCE_ID, false),
	REDO(R.string.button_redo, R.string.help_content_redo, R.drawable.ic_paint_studio_redo, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_btn_top_redo, Constants.INVALID_RESOURCE_ID, false),
	FILL(R.string.button_fill, R.string.help_content_fill, R.drawable.ic_paint_studio_tool_fill, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_fill, Constants.INVALID_RESOURCE_ID, true),
	STAMP(R.string.button_stamp, R.string.help_content_stamp, R.drawable.ic_paint_studio_tool_stamp, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_stamp, R.drawable.paint_studio_stamp_tool_overlay, true),
	LINE(R.string.button_line, R.string.help_content_line, R.drawable.ic_paint_studio_tool_line, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_line, Constants.INVALID_RESOURCE_ID, true),
	CURSOR(R.string.button_cursor, R.string.help_content_cursor, R.drawable.ic_paint_studio_tool_cursor, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_cursor, Constants.INVALID_RESOURCE_ID, true),
	IMPORTPNG(R.string.button_import_image, R.string.help_content_import_png, R.drawable.ic_paint_studio_tool_import, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_import, R.drawable.paint_studio_import_tool_overlay, false),
	TRANSFORM(R.string.button_transform, R.string.help_content_transform, R.drawable.ic_paint_studio_tool_transform, EnumSet.of(Tool.StateChange.RESET_INTERNAL_STATE, Tool.StateChange.NEW_IMAGE_LOADED), R.id.paint_studio_tools_transform, Constants.INVALID_RESOURCE_ID, true),
	ERASER(R.string.button_eraser, R.string.help_content_eraser, R.drawable.ic_paint_studio_tool_eraser, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_eraser, Constants.INVALID_RESOURCE_ID, true),
	SHAPE(R.string.button_shape, R.string.help_content_shape, R.drawable.ic_paint_studio_tool_rectangle, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_rectangle, R.drawable.paint_studio_rectangle_tool_overlay, true),
	TEXT(R.string.button_text, R.string.help_content_text, R.drawable.ic_paint_studio_tool_text, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_text, R.drawable.paint_studio_text_tool_overlay, true),
	LAYER(R.string.layers_title, R.string.help_content_layer, R.drawable.ic_paint_studio_layers, EnumSet.of(Tool.StateChange.ALL), Constants.INVALID_RESOURCE_ID, Constants.INVALID_RESOURCE_ID, false),
	COLORCHOOSER(R.string.color_picker_title, R.string.help_content_color_chooser, R.drawable.ic_paint_studio_color_palette, EnumSet.of(Tool.StateChange.ALL), Constants.INVALID_RESOURCE_ID, Constants.INVALID_RESOURCE_ID, false),
	HAND(R.string.button_hand, R.string.help_content_hand, R.drawable.ic_paint_studio_tool_hand, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_hand, Constants.INVALID_RESOURCE_ID, false),
	SPRAY(R.string.button_spray_can, R.string.help_content_spray_can, R.drawable.ic_paint_studio_tool_spray_can, EnumSet.of(Tool.StateChange.ALL), R.id.paint_studio_tools_spray_can, Constants.INVALID_RESOURCE_ID, true);

	private int nameResource;
	private int helpTextResource;
	private int drawableResource;
	private EnumSet<Tool.StateChange> stateChangeBehaviour;
	private int toolButtonID;
	private int overlayDrawableResource;
	private boolean hasOptions;

	ToolType(int nameResource, int helpTextResource, int drawableResource, EnumSet<Tool.StateChange> stateChangeBehaviour,
			int toolButtonID, int overlayDrawableResource, boolean hasOptions) {
		this.nameResource = nameResource;
		this.helpTextResource = helpTextResource;
		this.drawableResource = drawableResource;
		this.stateChangeBehaviour = stateChangeBehaviour;
		this.toolButtonID = toolButtonID;
		this.overlayDrawableResource = overlayDrawableResource;
		this.hasOptions = hasOptions;
	}

	public @StringRes int getNameResource() {
		return nameResource;
	}

	public @StringRes int getHelpTextResource() {
		return helpTextResource;
	}

	public @DrawableRes int getOverlayDrawableResource() {
		return overlayDrawableResource;
	}

	public boolean shouldReactToStateChange(Tool.StateChange stateChange) {
		return stateChangeBehaviour.contains(Tool.StateChange.ALL) || stateChangeBehaviour.contains(stateChange);
	}

	public @IdRes int getToolButtonID() {
		return toolButtonID;
	}

	public boolean hasOptions() {
		return hasOptions;
	}

	public @DrawableRes int getDrawableResource() {
		return drawableResource;
	}
}
