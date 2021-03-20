package com.jdots.paint.test.junit.command;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.AddLayerCommand;
import com.jdots.paint.command.implementation.ResizeCommand;
import com.jdots.paint.common.CommonFactory;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ResizeCommandTest {

	private static final int INITIAL_HEIGHT = 80;
	private static final int INITIAL_WIDTH = 80;
	private static final int NEW_WIDTH = 40;
	private static final int NEW_HEIGHT = 40;

	private Canvas canvasUnderTest;
	private LayerModel layerModel;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.setWidth(INITIAL_WIDTH);
		layerModel.setHeight(INITIAL_HEIGHT);

		Bitmap bitmapUnderTest = Bitmap.createBitmap(INITIAL_WIDTH, INITIAL_HEIGHT, Bitmap.Config.ARGB_8888);
		Layer layerUnderTest = new Layer(bitmapUnderTest);
		canvasUnderTest = new Canvas();
		canvasUnderTest.setBitmap(bitmapUnderTest);
		layerModel.addLayerAt(0, layerUnderTest);
		layerModel.setCurrentLayer(layerUnderTest);
	}

	@Test
	public void testResizeCommand() {
		Command commandUnderTest = new ResizeCommand(NEW_WIDTH, NEW_HEIGHT);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertEquals(40, layerModel.getHeight());
		assertEquals(40, layerModel.getWidth());
	}

	@Test
	public void testBitmapKeepsDrawings() {
		layerModel.getCurrentLayer().getBitmap().setPixel(0, 0, Color.BLACK);
		int currentPixel = layerModel.getCurrentLayer().getBitmap().getPixel(0, 0);
		assertNotEquals(currentPixel, 0);

		Command commandUnderTest = new ResizeCommand(NEW_WIDTH, NEW_HEIGHT);
		commandUnderTest.run(canvasUnderTest, layerModel);
		currentPixel = layerModel.getCurrentLayer().getBitmap().getPixel(0, 0);
		assertNotEquals(currentPixel, 0);
	}

	@Test
	public void testLayerStayInSameOrderOnResize() {
		layerModel.getLayerAt(0).getBitmap().eraseColor(Color.GREEN);

		Command addLayerCommand = new AddLayerCommand(new CommonFactory());
		addLayerCommand.run(canvasUnderTest, layerModel);

		layerModel.getLayerAt(0).getBitmap().eraseColor(Color.YELLOW);

		addLayerCommand = new AddLayerCommand(new CommonFactory());
		addLayerCommand.run(canvasUnderTest, layerModel);

		layerModel.getLayerAt(0).getBitmap().eraseColor(Color.BLUE);

		Command commandUnderTest = new ResizeCommand(NEW_WIDTH, NEW_HEIGHT);
		commandUnderTest.run(canvasUnderTest, layerModel);

		assertEquals(layerModel.getLayerAt(2).getBitmap().getPixel(0, 0), Color.GREEN);
		assertEquals(layerModel.getLayerAt(1).getBitmap().getPixel(0, 0), Color.YELLOW);
		assertEquals(layerModel.getLayerAt(0).getBitmap().getPixel(0, 0), Color.BLUE);
	}

	@Test
	public void testAllLayersAreResized() {
		Command addLayerCommand = new AddLayerCommand(new CommonFactory());
		addLayerCommand.run(canvasUnderTest, layerModel);

		addLayerCommand = new AddLayerCommand(new CommonFactory());
		addLayerCommand.run(canvasUnderTest, layerModel);

		Command commandUnderTest = new ResizeCommand(NEW_WIDTH, NEW_HEIGHT);
		commandUnderTest.run(canvasUnderTest, layerModel);

		assertEquals(layerModel.getLayerAt(2).getBitmap().getHeight(), NEW_HEIGHT);
		assertEquals(layerModel.getLayerAt(2).getBitmap().getWidth(), NEW_WIDTH);
		assertEquals(layerModel.getLayerAt(1).getBitmap().getHeight(), NEW_HEIGHT);
		assertEquals(layerModel.getLayerAt(1).getBitmap().getWidth(), NEW_WIDTH);
		assertEquals(layerModel.getLayerAt(0).getBitmap().getHeight(), NEW_HEIGHT);
		assertEquals(layerModel.getLayerAt(0).getBitmap().getWidth(), NEW_WIDTH);
	}
}
