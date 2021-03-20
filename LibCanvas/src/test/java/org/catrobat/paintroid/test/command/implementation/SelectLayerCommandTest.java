package com.jdots.paint.test.command.implementation;

import android.graphics.Canvas;

import com.jdots.paint.command.implementation.SelectLayerCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SelectLayerCommandTest {

	@Test
	public void testRunWhenNoLayerSelected() {
		LayerModel model = new LayerModel();
		LayerContracts.Layer layer = mock(LayerContracts.Layer.class);
		model.addLayerAt(0, layer);

		SelectLayerCommand command = new SelectLayerCommand(0);
		command.run(mock(Canvas.class), model);

		assertThat(model.getCurrentLayer(), is(layer));
	}

	@Test
	public void testRunWhenOtherLayerSelected() {
		LayerModel model = new LayerModel();
		LayerContracts.Layer firstLayer = mock(LayerContracts.Layer.class);
		LayerContracts.Layer secondLayer = mock(LayerContracts.Layer.class);
		model.addLayerAt(0, firstLayer);
		model.addLayerAt(1, secondLayer);
		model.setCurrentLayer(firstLayer);

		SelectLayerCommand command = new SelectLayerCommand(1);
		command.run(mock(Canvas.class), model);

		assertThat(model.getCurrentLayer(), is(secondLayer));
	}
}
