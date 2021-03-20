package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements Command {
	private final List<Command> commands = new ArrayList<>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		for (Command command : commands) {
			if (layerModel.getCurrentLayer() != null) {
				canvas.setBitmap(layerModel.getCurrentLayer().getBitmap());
			}
			command.run(canvas, layerModel);
		}
	}

	@Override
	public void freeResources() {
		for (Command command : commands) {
			command.freeResources();
		}
	}
}
