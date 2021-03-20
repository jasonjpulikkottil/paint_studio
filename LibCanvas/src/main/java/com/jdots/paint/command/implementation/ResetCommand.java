package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.contract.LayerContracts;

public class ResetCommand implements Command {
	public ResetCommand() {
	}

	@Override
	public void run(Canvas canvas, LayerContracts.Model layerModel) {
		layerModel.reset();
	}

	@Override
	public void freeResources() {
	}
}
