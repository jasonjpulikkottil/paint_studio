package com.jdots.paint.test.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.CompositeCommand;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CompositeCommandTest {
	@Test
	public void testRunEmpty() {
		Canvas canvas = mock(Canvas.class);
		LayerModel layerModel = new LayerModel();

		CompositeCommand command = new CompositeCommand();
		command.run(canvas, layerModel);

		verifyZeroInteractions(canvas);
		assertThat(layerModel.getLayerCount(), is(0));
		assertNull(layerModel.getCurrentLayer());
	}

	@Test
	public void testRunAfterAddWithoutCurrentLayer() {
		Canvas canvas = mock(Canvas.class);
		LayerModel layerModel = new LayerModel();
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);

		CompositeCommand command = new CompositeCommand();
		command.addCommand(firstCommand);
		command.addCommand(secondCommand);
		command.run(canvas, layerModel);

		verifyZeroInteractions(canvas);
		InOrder inOrder = inOrder(firstCommand, secondCommand);
		inOrder.verify(firstCommand).run(canvas, layerModel);
		inOrder.verify(secondCommand).run(canvas, layerModel);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testRunAfterAddWithCurrentLayerSet() {
		Canvas canvas = mock(Canvas.class);
		LayerModel layerModel = new LayerModel();
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);

		Layer currentLayer = mock(Layer.class);
		Bitmap currentBitmap = mock(Bitmap.class);
		when(currentLayer.getBitmap()).thenReturn(currentBitmap);
		layerModel.setCurrentLayer(currentLayer);

		CompositeCommand command = new CompositeCommand();
		command.addCommand(firstCommand);
		command.addCommand(secondCommand);
		command.run(canvas, layerModel);

		InOrder inOrder = inOrder(firstCommand, secondCommand, canvas);
		inOrder.verify(canvas).setBitmap(currentBitmap);
		inOrder.verify(firstCommand).run(canvas, layerModel);
		inOrder.verify(canvas).setBitmap(currentBitmap);
		inOrder.verify(secondCommand).run(canvas, layerModel);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testFreeResourcesEmpty() {
		CompositeCommand command = new CompositeCommand();
		command.freeResources();
	}

	@Test
	public void testFreeResourcesAfterAdd() {
		Command firstCommand = mock(Command.class);
		Command secondCommand = mock(Command.class);

		CompositeCommand command = new CompositeCommand();
		command.addCommand(firstCommand);
		command.addCommand(secondCommand);
		command.freeResources();

		InOrder inOrder = inOrder(firstCommand, secondCommand);
		inOrder.verify(firstCommand).freeResources();
		inOrder.verify(secondCommand).freeResources();
		inOrder.verifyNoMoreInteractions();
	}
}
