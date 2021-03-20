package com.jdots.paint.test.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.implementation.AddLayerCommand;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AddLayerCommandTest {

	@Mock
	private CommonFactory commonFactory;

	@Mock
	private Canvas canvas;

	@InjectMocks
	private AddLayerCommand command;

	@Test
	public void testSetUp() {
		verifyZeroInteractions(commonFactory);
	}

	@Test
	public void testAddOneLayer() {
		LayerModel layerModel = new LayerModel();
		layerModel.setWidth(3);
		layerModel.setHeight(5);

		command.run(canvas, layerModel);

		verify(commonFactory).createBitmap(3, 5, Bitmap.Config.ARGB_8888);
		assertEquals(1, layerModel.getLayerCount());

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		assertEquals(currentLayer, layerModel.getLayerAt(0));
	}

	@Test
	public void testAddTwoLayersAddsToFront() {
		LayerModel layerModel = new LayerModel();

		command.run(canvas, layerModel);
		LayerContracts.Layer firstLayer = layerModel.getLayerAt(0);
		command.run(canvas, layerModel);

		assertEquals(2, layerModel.getLayerCount());

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		assertEquals(currentLayer, layerModel.getLayerAt(0));
		assertEquals(firstLayer, layerModel.getLayerAt(1));
	}

	@Test
	public void testAddMultipleLayersWillUseSameArguments() {
		LayerModel layerModel = new LayerModel();
		layerModel.setWidth(7);
		layerModel.setHeight(11);

		command.run(canvas, layerModel);
		command.run(canvas, layerModel);
		command.run(canvas, layerModel);
		command.run(canvas, layerModel);

		verify(commonFactory, times(4)).createBitmap(7, 11, Bitmap.Config.ARGB_8888);
	}
}
