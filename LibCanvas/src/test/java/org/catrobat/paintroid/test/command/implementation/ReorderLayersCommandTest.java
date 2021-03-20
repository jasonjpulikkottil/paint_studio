package com.jdots.paint.test.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.implementation.ReorderLayersCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ReorderLayersCommandTest {
	@Mock
	private LayerContracts.Layer firstLayer;

	@Mock
	private LayerContracts.Layer secondLayer;

	@Mock
	private LayerContracts.Layer thirdLayer;

	private LayerModel layerModel;
	private ReorderLayersCommand command;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.addLayerAt(0, firstLayer);
		layerModel.addLayerAt(1, secondLayer);
		layerModel.addLayerAt(2, thirdLayer);
	}

	@Test
	public void testRunReorderToBack() {
		command = new ReorderLayersCommand(0, 2);

		command.run(mock(Canvas.class), layerModel);

		assertThat(layerModel.getLayerAt(0), is(secondLayer));
		assertThat(layerModel.getLayerAt(1), is(thirdLayer));
		assertThat(layerModel.getLayerAt(2), is(firstLayer));
		assertThat(layerModel.getLayerCount(), is(3));
	}

	@Test
	public void testRunReorderToFront() {
		command = new ReorderLayersCommand(2, 0);

		command.run(mock(Canvas.class), layerModel);

		assertThat(layerModel.getLayerAt(0), is(thirdLayer));
		assertThat(layerModel.getLayerAt(1), is(firstLayer));
		assertThat(layerModel.getLayerAt(2), is(secondLayer));
		assertThat(layerModel.getLayerCount(), is(3));
	}

	@Test
	public void testRunDoesNotAffectCurrentLayer() {
		command = new ReorderLayersCommand(2, 0);

		command.run(mock(Canvas.class), layerModel);

		assertNull(layerModel.getCurrentLayer());
	}
}
