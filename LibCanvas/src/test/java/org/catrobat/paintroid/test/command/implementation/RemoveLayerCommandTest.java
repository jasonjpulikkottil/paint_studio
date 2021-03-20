package com.jdots.paint.test.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.implementation.RemoveLayerCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RemoveLayerCommandTest {

	@Mock
	private LayerContracts.Layer firstLayer;

	@Mock
	private LayerContracts.Layer secondLayer;

	private LayerModel layerModel;
	private RemoveLayerCommand command;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.addLayerAt(0, firstLayer);
		layerModel.addLayerAt(1, secondLayer);
	}

	@Test
	public void testRunRemovesOneLayer() {
		command = new RemoveLayerCommand(1);

		command.run(mock(Canvas.class), layerModel);

		assertThat(layerModel.getLayerCount(), is(1));
		assertThat(layerModel.getLayerAt(0), is(firstLayer));
	}

	@Test
	public void testRunSetsCurrentLayer() {
		command = new RemoveLayerCommand(0);

		command.run(mock(Canvas.class), layerModel);

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		assertThat(layerModel.getLayerAt(0), is(currentLayer));
		assertThat(layerModel.getLayerAt(0), is(secondLayer));
	}
}
