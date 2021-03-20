package com.jdots.paint.test.command.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.jdots.paint.command.implementation.LoadCommand;
import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoadCommandTest {
	@Mock
	Bitmap bitmap;

	@Mock
	Canvas canvas;

	LayerContracts.Model layerModel;

	@InjectMocks
	LoadCommand command;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
	}

	@Test
	public void testSetUp() {
		verifyZeroInteractions(bitmap);
	}

	@Test
	public void testRunCopiesImage() {
		Bitmap copy = mock(Bitmap.class);
		when(bitmap.copy(Bitmap.Config.ARGB_8888, true)).thenReturn(copy);

		command.run(canvas, layerModel);

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		assertThat(currentLayer.getBitmap(), is(copy));
	}

	@Test
	public void testRunAddsOneLayer() {
		Bitmap clone = mock(Bitmap.class);
		when(bitmap.copy(Bitmap.Config.ARGB_8888, true)).thenReturn(clone);

		command.run(canvas, layerModel);

		assertThat(layerModel.getLayerCount(), is(1));
	}

	@Test
	public void testRunSetsCurrentLayer() {
		Bitmap clone = mock(Bitmap.class);
		when(bitmap.copy(Bitmap.Config.ARGB_8888, true)).thenReturn(clone);

		command.run(canvas, layerModel);

		LayerContracts.Layer currentLayer = layerModel.getCurrentLayer();
		assertThat(layerModel.getLayerAt(0), is(currentLayer));
	}
}
