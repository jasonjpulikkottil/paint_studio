package com.jdots.paint.test.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.CommandManager.CommandListener;
import com.jdots.paint.command.implementation.DefaultCommandManager;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCommandManagerTest {
	@Mock
	private CommandListener commandListener;

	@Mock
	private CommonFactory commonFactory;

	private LayerModel layerModel;

	private DefaultCommandManager commandManager;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		commandManager = new DefaultCommandManager(commonFactory, layerModel);
	}

	@Test
	public void testAddCommandListener() {
		commandManager.addCommandListener(commandListener);

		verifyZeroInteractions(commandListener);
	}

	@Test
	public void testRemoveCommandListener() {
		commandManager.removeCommandListener(commandListener);

		verifyZeroInteractions(commandListener);
	}

	@Test
	public void testUndoInitiallyNotAvailable() {
		assertFalse(commandManager.isUndoAvailable());
	}

	@Test
	public void testRedoInitiallyNotAvailable() {
		assertFalse(commandManager.isRedoAvailable());
	}

	@Test
	public void testAddCommand() {
		Command command = mock(Command.class);
		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommandListener(commandListener);
		commandManager.addCommand(command);

		verify(command).run(any(Canvas.class), eq(layerModel));
		verify(commandListener).commandPostExecute();

		assertFalse(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());
	}

	@Test
	public void testUndoWhenOnlyOneCommand() {
		Command command = mock(Command.class);
		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommandListener(commandListener);
		commandManager.addCommand(command);
		commandManager.undo();

		InOrder inOrder = inOrder(command, commandListener);
		inOrder.verify(command).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(commandListener, times(2)).commandPostExecute();

		assertTrue(commandManager.isRedoAvailable());
		assertFalse(commandManager.isUndoAvailable());
	}

	@Test
	public void testUndoWithMultipleCommands() {
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);
		Command thirdCommand = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(firstCommand);
		commandManager.addCommand(secondCommand);
		commandManager.addCommand(thirdCommand);
		commandManager.undo();

		InOrder inOrder = inOrder(firstCommand, secondCommand, thirdCommand);
		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(thirdCommand).run(any(Canvas.class), eq(layerModel));

		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verifyNoMoreInteractions();

		assertTrue(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());
	}

	@Test
	public void testRedo() {
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);
		Command thirdCommand = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(firstCommand);
		commandManager.addCommand(secondCommand);
		commandManager.addCommand(thirdCommand);
		commandManager.undo();
		commandManager.redo();

		InOrder inOrder = inOrder(firstCommand, secondCommand, thirdCommand);
		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(thirdCommand).run(any(Canvas.class), eq(layerModel));

		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(thirdCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verifyNoMoreInteractions();

		assertFalse(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());
	}

	@Test
	public void testRedoMultipleCommands() {
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);
		Command thirdCommand = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(firstCommand);
		commandManager.addCommand(secondCommand);
		commandManager.addCommand(thirdCommand);

		commandManager.undo();
		commandManager.undo();
		commandManager.undo();

		commandManager.redo();
		commandManager.redo();

		InOrder inOrder = inOrder(firstCommand, secondCommand, thirdCommand);
		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(thirdCommand).run(any(Canvas.class), eq(layerModel));

		inOrder.verify(firstCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));

		inOrder.verify(firstCommand, times(2)).run(any(Canvas.class), eq(layerModel));

		inOrder.verify(secondCommand).run(any(Canvas.class), eq(layerModel));
		inOrder.verifyNoMoreInteractions();

		assertTrue(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());

		commandManager.redo();

		assertFalse(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());
	}

	@Test
	public void testRedoClearedOnCommand() {
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);
		Command thirdCommand = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(firstCommand);
		commandManager.addCommand(secondCommand);

		commandManager.undo();

		assertTrue(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());

		commandManager.addCommand(thirdCommand);

		assertFalse(commandManager.isRedoAvailable());
		assertTrue(commandManager.isUndoAvailable());
	}

	@Test
	public void testInitialStateCommandOnReset() {
		Command initialStateCommand = mock(Command.class);

		LayerContracts.Layer layer = mock(LayerContracts.Layer.class);
		layerModel.addLayerAt(0, layer);

		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommandListener(commandListener);
		commandManager.setInitialStateCommand(initialStateCommand);
		commandManager.reset();

		verify(commandListener).commandPostExecute();
		verify(initialStateCommand).run(any(Canvas.class), eq(layerModel));

		assertThat(layerModel.getLayerCount(), is(0));
	}

	@Test
	public void testInitialStateCommandOnUndo() {
		Command initialStateCommand = mock(Command.class);
		Command command = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);
		layerModel.addLayerAt(0, currentLayer);

		when(currentLayer.getBitmap()).thenReturn(mock(Bitmap.class));
		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.setInitialStateCommand(initialStateCommand);
		commandManager.addCommand(command);
		commandManager.undo();

		verify(initialStateCommand).run(any(Canvas.class), eq(layerModel));
	}

	@Test
	public void testUndoClearedOnReset() {
		Command command = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(command);
		commandManager.reset();

		assertFalse(commandManager.isRedoAvailable());
		assertFalse(commandManager.isUndoAvailable());
	}

	@Test
	public void testRedoClearedOnReset() {
		Command command = mock(Command.class);

		LayerContracts.Layer currentLayer = mock(LayerContracts.Layer.class);
		layerModel.setCurrentLayer(currentLayer);

		when(commonFactory.createCanvas()).thenReturn(mock(Canvas.class));

		commandManager.addCommand(command);
		commandManager.undo();
		commandManager.reset();

		assertFalse(commandManager.isRedoAvailable());
		assertFalse(commandManager.isUndoAvailable());
	}
}
