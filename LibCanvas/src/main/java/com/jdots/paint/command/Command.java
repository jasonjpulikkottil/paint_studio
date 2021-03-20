package com.jdots.paint.command;

import android.graphics.Canvas;

import com.jdots.paint.contract.LayerContracts;

public interface Command {

	void run(Canvas canvas, LayerContracts.Model layerModel);

	void freeResources();
}
