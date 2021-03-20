package com.jdots.paint.test.command.implementation;

import com.jdots.paint.command.implementation.SetDimensionCommand;
import com.jdots.paint.model.LayerModel;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SetDimensionCommandTest {

	@Test
	public void testRun() {
		LayerModel layerModel = new LayerModel();
		SetDimensionCommand command = new SetDimensionCommand(3, 4);

		command.run(null, layerModel);

		assertThat(layerModel.getWidth(), is(3));
		assertThat(layerModel.getHeight(), is(4));
	}
}
