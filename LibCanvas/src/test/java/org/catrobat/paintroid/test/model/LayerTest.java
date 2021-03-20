package com.jdots.paint.test.model;

import android.graphics.Bitmap;

import com.jdots.paint.model.Layer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LayerTest {

	@Mock
	private Bitmap firstBitmap;

	@Mock
	private Bitmap secondBitmap;

	@Test
	public void testGetBitmap() {
		Layer firstLayer = new Layer(firstBitmap);
		Layer secondLayer = new Layer(secondBitmap);

		assertEquals(firstBitmap, firstLayer.getBitmap());
		assertEquals(secondBitmap, secondLayer.getBitmap());
		assertTrue(firstLayer.getCheckBox());
		assertTrue(secondLayer.getCheckBox());
		verify(secondBitmap).getWidth();
		verify(secondBitmap).getHeight();
		verify(firstBitmap).getWidth();
		verify(firstBitmap).getHeight();
	}

	@Test
	public void testSetBitmap() {
		Layer layer = new Layer(firstBitmap);

		layer.setBitmap(secondBitmap);

		assertEquals(secondBitmap, layer.getBitmap());
		assertTrue(layer.getCheckBox());
		verify(firstBitmap).getWidth();
		verify(firstBitmap).getHeight();
	}
}
