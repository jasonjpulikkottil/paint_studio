package com.jdots.paint.test.junit.command;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;

import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.CropCommand;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CropCommandTest {

	private static final int MAXIMUM_BITMAP_RESOLUTION_FACTOR = 4;
	private static final int BITMAP_BASE_COLOR = Color.GREEN;
	private static final int INITIAL_HEIGHT = 80;
	private static final int INITIAL_WIDTH = 80;

	private int resizeCoordinateXLeft;
	private int resizeCoordinateYTop;
	private int resizeCoordinateXRight;
	private int resizeCoordinateYBottom;
	private int maximumBitmapResolution;

	private Command commandUnderTest;
	private Command commandUnderTestNull; // can be used to pass null to constructor
	private Canvas canvasUnderTest;
	private Bitmap bitmapUnderTest;
	private Layer layerUnderTest;
	private LayerModel layerModel;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.setWidth(INITIAL_WIDTH);
		layerModel.setHeight(INITIAL_HEIGHT);

		Bitmap canvasBitmapUnderTest = Bitmap.createBitmap(INITIAL_WIDTH, INITIAL_HEIGHT, Config.ARGB_8888);
		canvasBitmapUnderTest.eraseColor(BITMAP_BASE_COLOR);
		bitmapUnderTest = canvasBitmapUnderTest.copy(Config.ARGB_8888, true);
		layerUnderTest = new Layer(bitmapUnderTest);
		canvasUnderTest = new Canvas();
		canvasUnderTest.setBitmap(canvasBitmapUnderTest);
		layerModel.addLayerAt(0, layerUnderTest);
		layerModel.setCurrentLayer(layerUnderTest);

		resizeCoordinateXLeft = 0;
		resizeCoordinateYTop = 0;
		resizeCoordinateXRight = bitmapUnderTest.getWidth() - 1;
		resizeCoordinateYBottom = bitmapUnderTest.getHeight() - 1;
		maximumBitmapResolution = bitmapUnderTest.getWidth() * bitmapUnderTest.getHeight()
				* MAXIMUM_BITMAP_RESOLUTION_FACTOR;
		commandUnderTest = new CropCommand(resizeCoordinateXLeft, resizeCoordinateYTop,
				resizeCoordinateXRight, resizeCoordinateYBottom, maximumBitmapResolution);
		commandUnderTestNull = new CropCommand(1, 1, 2, 2, maximumBitmapResolution);
	}

	@Test
	public void testRunWithNullParameters() {
		commandUnderTestNull.run(null, null);
		commandUnderTestNull.run(null, null);
		commandUnderTestNull.run(canvasUnderTest, null);
		commandUnderTestNull.run(null, new LayerModel());
		commandUnderTestNull.run(null, layerModel);
	}

	@Test
	public void testIfBitmapIsCropped() {
		int widthOriginal = bitmapUnderTest.getWidth();
		int heightOriginal = bitmapUnderTest.getHeight();
		resizeCoordinateXLeft = 1;
		resizeCoordinateYTop = 1;
		resizeCoordinateXRight = bitmapUnderTest.getWidth() - 2;
		resizeCoordinateYBottom = bitmapUnderTest.getHeight() - 2;
		commandUnderTest = new CropCommand(resizeCoordinateXLeft, resizeCoordinateYTop,
				resizeCoordinateXRight, resizeCoordinateYBottom, maximumBitmapResolution);

		commandUnderTest.run(canvasUnderTest, layerModel);

		Bitmap croppedBitmap = layerUnderTest.getBitmap();
		assertEquals("Cropping failed, width not correct ", widthOriginal - resizeCoordinateXLeft
				- (widthOriginal - (resizeCoordinateXRight + 1)), croppedBitmap.getWidth());
		assertEquals("Cropping failed, height not correct ", heightOriginal - resizeCoordinateYTop
				- (widthOriginal - (resizeCoordinateYBottom + 1)), croppedBitmap.getHeight());
		croppedBitmap.recycle();
	}

	@Test
	public void testIfBitmapIsEnlarged() {
		int widthOriginal = bitmapUnderTest.getWidth();
		int heightOriginal = bitmapUnderTest.getHeight();
		resizeCoordinateXLeft = -1;
		resizeCoordinateYTop = -1;
		resizeCoordinateXRight = bitmapUnderTest.getWidth();
		resizeCoordinateYBottom = bitmapUnderTest.getHeight();
		commandUnderTest = new CropCommand(resizeCoordinateXLeft, resizeCoordinateYTop,
				resizeCoordinateXRight, resizeCoordinateYBottom, maximumBitmapResolution);

		commandUnderTest.run(canvasUnderTest, layerModel);

		Bitmap enlargedBitmap = layerUnderTest.getBitmap();
		assertEquals("Enlarging failed, width not correct ", widthOriginal - resizeCoordinateXLeft
				- (widthOriginal - (resizeCoordinateXRight + 1)), enlargedBitmap.getWidth());
		assertEquals("Enlarging failed, height not correct ", heightOriginal - resizeCoordinateYTop
				- (widthOriginal - (resizeCoordinateYBottom + 1)), enlargedBitmap.getHeight());
		enlargedBitmap.recycle();
	}

	@Test
	public void testIfBitmapIsShifted() {
		int widthOriginal = bitmapUnderTest.getWidth();
		int heightOriginal = bitmapUnderTest.getHeight();
		resizeCoordinateXLeft = bitmapUnderTest.getWidth() / 2 - 1;
		resizeCoordinateYTop = bitmapUnderTest.getHeight() / 2 - 1;
		resizeCoordinateXRight = resizeCoordinateXLeft + bitmapUnderTest.getWidth() - 1;
		resizeCoordinateYBottom = resizeCoordinateYTop + bitmapUnderTest.getHeight() - 1;
		commandUnderTest = new CropCommand(resizeCoordinateXLeft, resizeCoordinateYTop,
				resizeCoordinateXRight, resizeCoordinateYBottom, maximumBitmapResolution);

		commandUnderTest.run(canvasUnderTest, layerModel);

		Bitmap enlargedBitmap = layerUnderTest.getBitmap();
		assertEquals("Enlarging failed, width not correct ", widthOriginal, enlargedBitmap.getWidth());
		assertEquals("Enlarging failed, height not correct ", heightOriginal, enlargedBitmap.getHeight());
		enlargedBitmap.recycle();
	}

	@Test
	public void testIfMaximumResolutionIsRespected() {
		int widthOriginal = bitmapUnderTest.getWidth();
		int heightOriginal = bitmapUnderTest.getHeight();
		commandUnderTest = new CropCommand(0, 0, widthOriginal * 2, heightOriginal * 2, maximumBitmapResolution);

		commandUnderTest.run(canvasUnderTest, layerModel);

		assertEquals("Width should not have changed", widthOriginal, layerUnderTest.getBitmap().getWidth());
		assertEquals("Height should not have changed", heightOriginal, layerUnderTest.getBitmap().getHeight());
	}

	@Test
	public void testIfBitmapIsNotResizedWithInvalidBounds() {
		Bitmap originalBitmap = layerUnderTest.getBitmap();
		commandUnderTest = new CropCommand(bitmapUnderTest.getWidth(), 0, bitmapUnderTest.getWidth(),
				0, maximumBitmapResolution);

		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change if X left is larger than bitmap scope", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(-1, 0, -1, 0, maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change if X right is smaller than bitmap scope", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(0, bitmapUnderTest.getHeight(), 0,
				bitmapUnderTest.getHeight(), maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change if Y top is larger than bitmap scope", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(0, -1, 0, -1, maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change if Y bottom is smaller than bitmap scope", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(1, 0, 0, 0, maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change with widthXRight < widthXLeft bound", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(0, 1, 0, 0, maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change with widthYBottom < widthYTop bound", originalBitmap.sameAs(layerUnderTest.getBitmap()));

		commandUnderTest = new CropCommand(0, 0, bitmapUnderTest.getWidth() - 1,
				bitmapUnderTest.getHeight() - 1, maximumBitmapResolution);
		commandUnderTest.run(canvasUnderTest, layerModel);
		assertTrue("bitmap must not change because bounds are the same as original bitmap", originalBitmap.sameAs(layerUnderTest.getBitmap()));
	}
}
