package com.jdots.paint.command.implementation;

import android.os.AsyncTask;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.contract.LayerContracts;

import java.util.ArrayList;
import java.util.List;

public class AsyncCommandManager implements CommandManager {
	private List<CommandListener> commandListeners = new ArrayList<>();
	private CommandManager commandManager;
	private final LayerContracts.Model layerModel;
	private boolean shuttingDown;
	private boolean busy;

	public AsyncCommandManager(CommandManager commandManager, LayerContracts.Model layerModel) {
		this.commandManager = commandManager;
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
		return commandManager.isUndoAvailable();
	}

	@Override
	public boolean isRedoAvailable() {
		return commandManager.isRedoAvailable();
	}

	@Override
	public void addCommand(final Command command) {
		if (busy) {
			return;
		}

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				notifyCommandPreExecute();
			}

			@Override
			protected Void doInBackground(Void... voids) {
				if (!shuttingDown) {
					synchronized (layerModel) {
						commandManager.addCommand(command);
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				notifyCommandPostExecute();
			}
		}.execute();
	}

	@Override
	public void undo() {
		if (busy) {
			return;
		}

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				busy = true;
			}

			@Override
			protected Void doInBackground(Void... voids) {
				if (!shuttingDown) {
					synchronized (layerModel) {
						commandManager.undo();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				notifyCommandPostExecute();
			}
		}.execute();
	}

	@Override
	public void redo() {
		if (busy) {
			return;
		}

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				busy = true;
			}

			@Override
			protected Void doInBackground(Void... voids) {
				if (!shuttingDown) {
					synchronized (layerModel) {
						commandManager.redo();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				notifyCommandPostExecute();
			}
		}.execute();
	}

	@Override
	public void reset() {
		synchronized (layerModel) {
			commandManager.reset();
		}
		notifyCommandPostExecute();
	}

	@Override
	public void shutdown() {
		shuttingDown = true;
	}

	@Override
	public void setInitialStateCommand(Command command) {
		synchronized (layerModel) {
			commandManager.setInitialStateCommand(command);
		}
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	private void notifyCommandPreExecute() {
		busy = true;
	}

	private void notifyCommandPostExecute() {
		busy = false;
		if (!shuttingDown) {
			for (CommandListener listener : commandListeners) {
				listener.commandPostExecute();
			}
		}
	}
}
