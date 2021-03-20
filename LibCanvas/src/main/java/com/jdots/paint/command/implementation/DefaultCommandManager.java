package com.jdots.paint.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.contract.LayerContracts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class DefaultCommandManager implements CommandManager {
	private List<CommandListener> commandListeners = new ArrayList<>();
	private Deque<Command> redoCommandList = new ArrayDeque<>();
	private Deque<Command> undoCommandList = new ArrayDeque<>();
	private Command initialStateCommand;

	private final CommonFactory commonFactory;
	private final LayerContracts.Model layerModel;

	public DefaultCommandManager(CommonFactory commonFactory, LayerContracts.Model layerModel) {
		this.commonFactory = commonFactory;
		this.layerModel = layerModel;
	}

	@Override
	public void addCommandListener(CommandListener commandListener) {
		commandListeners.add(commandListener);
	}

	@Override
	public void removeCommandListener(CommandListener commandListener) {
		commandListeners.remove(commandListener);
	}

	@Override
	public boolean isUndoAvailable() {
		return !undoCommandList.isEmpty();
	}

	@Override
	public boolean isRedoAvailable() {
		return !redoCommandList.isEmpty();
	}

	@Override
	public void addCommand(Command command) {
		redoCommandList.clear();
		undoCommandList.addFirst(command);

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		Canvas canvas = commonFactory.createCanvas();
		canvas.setBitmap(currentLayer.getBitmap());
		command.run(canvas, layerModel);

		notifyCommandExecuted();
	}

	@Override
	public void undo() {
		Command command = undoCommandList.pop();
		redoCommandList.addFirst(command);

		layerModel.reset();

		Canvas canvas = commonFactory.createCanvas();

		if (initialStateCommand != null) {
			initialStateCommand.run(canvas, layerModel);
		}

		Iterator<Command> iterator = undoCommandList.descendingIterator();
		while (iterator.hasNext()) {
			LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
			canvas.setBitmap(currentLayer.getBitmap());
			iterator.next().run(canvas, layerModel);
		}

		notifyCommandExecuted();
	}

	@Override
	public void redo() {
		Command command = redoCommandList.pop();
		undoCommandList.addFirst(command);

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		Canvas canvas = commonFactory.createCanvas();
		canvas.setBitmap(currentLayer.getBitmap());
		command.run(canvas, layerModel);

		notifyCommandExecuted();
	}

	@Override
	public void reset() {
		undoCommandList.clear();
		redoCommandList.clear();
		layerModel.reset();

		if (initialStateCommand != null) {
			Canvas canvas = commonFactory.createCanvas();
			initialStateCommand.run(canvas, layerModel);
		}

		notifyCommandExecuted();
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void setInitialStateCommand(Command command) {
		initialStateCommand = command;
	}

	@Override
	public boolean isBusy() {
		return false;
	}

	private void notifyCommandExecuted() {
		for (CommandListener listener : commandListeners) {
			listener.commandPostExecute();
		}
	}
}
