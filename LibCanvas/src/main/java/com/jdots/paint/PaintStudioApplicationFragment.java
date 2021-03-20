package com.jdots.paint;

import android.os.Bundle;

import com.jdots.paint.command.CommandManager;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.tools.ToolPaint;
import com.jdots.paint.tools.ToolReference;
import com.jdots.paint.ui.Perspective;

import androidx.fragment.app.Fragment;

public class PaintStudioApplicationFragment extends Fragment {
	private CommandManager commandManager;
	private ToolReference currentTool;
	private Perspective perspective;
	private LayerContracts.Model layerModel;
	private ToolPaint toolPaint;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setRetainInstance(true);
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public ToolReference getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(ToolReference currentTool) {
		this.currentTool = currentTool;
	}

	public Perspective getPerspective() {
		return perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	public LayerContracts.Model getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerContracts.Model layerModel) {
		this.layerModel = layerModel;
	}

	public ToolPaint getToolPaint() {
		return toolPaint;
	}

	public void setToolPaint(ToolPaint toolPaint) {
		this.toolPaint = toolPaint;
	}
}
