package com.jdots.paint.command;

public interface CommandManager {

	void addCommandListener(CommandListener commandListener);

	void removeCommandListener(CommandListener commandListener);

	boolean isUndoAvailable();

	boolean isRedoAvailable();

	void addCommand(Command command);

	void undo();

	void redo();

	void reset();

	void shutdown();

	void setInitialStateCommand(Command command);

	boolean isBusy();

	interface CommandListener {
		void commandPostExecute();
	}
}
