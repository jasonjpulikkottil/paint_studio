package com.jdots.paint.test.junit.command;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.FlipCommand;
import com.jdots.paint.command.implementation.FlipCommand.FlipDirection;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FlipCommandTest {

	private static final int BITMAP_BASE_COLOR = Color.GREEN;
	private static final int PAINT_BASE_COLOR = Color.BLUE;
	private static final int INITIAL_HEIGHT = 80;
	private static final int INITIAL_WIDTH = 80;

	private Command commandUnderTest;
	private Canvas canvasUnderTest;
	private Bitmap bitmapUnderTest;
	private LayerModel layerModel;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.setWidth(INITIAL_WIDTH);
		layerModel.setHeight(INITIAL_HEIGHT);

		Bitmap canvasBitmapUnderTest = Bitmap.createBitmap(INITIAL_WIDTH, INITIAL_HEIGHT, Config.ARGB_8888);
		canvasBitmapUnderTest.eraseColor(BITMAP_BASE_COLOR);
		bitmapUnderTest = canvasBitmapUnderTest.copy(Config.ARGB_8888, true);
		Layer layerUnderTest = new Layer(bitmapUnderTest);
		canvasUnderTest = new Canvas();
		canvasUnderTest.setBitmap(canvasBitmapUnderTest);
		layerModel.addLayerAt(0, layerUnderTest);
		layerModel.setCurrentLayer(layerUnderTest);
	}

	@Test
	public void testVerticalFlip() {
		commandUnderTest = new FlipCommand(FlipDirection.FLIP_VERTICAL);
		bitmapUnderTest.setPixel(0, INITIAL_HEIGHT / 2, PAINT_BASE_COLOR);
		commandUnderTest.run(canvasUnderTest, layerModel);
		int pixel = bitmapUnderTest.getPixel(INITIAL_WIDTH - 1, INITIAL_WIDTH / 2);
		assertEquals(PAINT_BASE_COLOR, pixel);
	}

	@Test
	public void testHorizontalFlip() {
		commandUnderTest = new FlipCommand(FlipDirection.FLIP_HORIZONTAL);
		bitmapUnderTest.setPixel(INITIAL_WIDTH / 2, 0, PAINT_BASE_COLOR);
		commandUnderTest.run(canvasUnderTest, layerModel);
		int pixel = bitmapUnderTest.getPixel(INITIAL_WIDTH / 2, INITIAL_WIDTH - 1);
		assertEquals(PAINT_BASE_COLOR, pixel);
	}
}
