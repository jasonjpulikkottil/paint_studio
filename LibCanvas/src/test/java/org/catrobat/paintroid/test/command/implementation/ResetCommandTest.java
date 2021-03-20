package com.jdots.paint.test.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.implementation.ResetCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ResetCommandTest {
	@Mock
	Canvas canvas;
	@InjectMocks
	ResetCommand command;
	private LayerContracts.Model layerModel;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
	}

	@Test
	public void testRunClearsLayers() {
		layerModel.addLayerAt(0, new Layer(null));
		layerModel.addLayerAt(1, new Layer(null));

		command.run(canvas, layerModel);

		assertThat(layerModel.getLayerCount(), is(0));
	}
}
