package com.jdots.paint.test.junit.command;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import com.jdots.paint.PaintStudioApplication;
import com.jdots.paint.command.Command;
import com.jdots.paint.command.implementation.BaseCommand;
import com.jdots.paint.command.implementation.StampCommand;
import com.jdots.paint.model.Layer;
import com.jdots.paint.model.LayerModel;
import com.jdots.paint.test.utils.PaintStudioAsserts;
import org.junit.Before;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StampCommandTest {

	private Bitmap stampBitmapUnderTest;

	private static final int BITMAP_BASE_COLOR = Color.GREEN;
	private static final int BITMAP_REPLACE_COLOR = Color.CYAN;
	private static final int INITIAL_HEIGHT = 80;
	private static final int INITIAL_WIDTH = 80;

	private Command commandUnderTest;
	private Command commandUnderTestNull; // can be used to pass null to constructor
	private PointF pointUnderTest;
	private Canvas canvasUnderTest;
	private Bitmap canvasBitmapUnderTest;
	private LayerModel layerModel;

	@Before
	public void setUp() {
		layerModel = new LayerModel();
		layerModel.setWidth(INITIAL_WIDTH);
		layerModel.setHeight(INITIAL_HEIGHT);

		canvasBitmapUnderTest = Bitmap.createBitmap(INITIAL_WIDTH, INITIAL_HEIGHT, Config.ARGB_8888);
		canvasBitmapUnderTest.eraseColor(BITMAP_BASE_COLOR);
		Bitmap bitmapUnderTest = canvasBitmapUnderTest.copy(Config.ARGB_8888, true);
		Layer layerUnderTest = new Layer(bitmapUnderTest);
		canvasUnderTest = new Canvas();
		canvasUnderTest.setBitmap(canvasBitmapUnderTest);
		pointUnderTest = new PointF(INITIAL_WIDTH / 2, INITIAL_HEIGHT / 2);
		layerModel.addLayerAt(0, layerUnderTest);
		layerModel.setCurrentLayer(layerUnderTest);

		PaintStudioApplication.cacheDir = InstrumentationRegistry.getInstrumentation().getTargetContext().getCacheDir();

		stampBitmapUnderTest = canvasBitmapUnderTest.copy(Config.ARGB_8888, true);
		stampBitmapUnderTest.eraseColor(BITMAP_REPLACE_COLOR);
		commandUnderTest = new StampCommand(stampBitmapUnderTest, new Point(canvasBitmapUnderTest.getWidth() / 2,
				canvasBitmapUnderTest.getHeight() / 2), canvasBitmapUnderTest.getWidth(),
				canvasBitmapUnderTest.getHeight(), 0);
		commandUnderTestNull = new StampCommand(null, null, 0, 0, 0);
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
	public void testRun() {
		Layer layer = new Layer(Bitmap.createBitmap(1, 1, Config.ARGB_8888));
		LayerModel model = new LayerModel();
		model.addLayerAt(0, layer);
		model.setCurrentLayer(layer);
		commandUnderTest.run(canvasUnderTest, model);
		PaintStudioAsserts.assertBitmapEquals(stampBitmapUnderTest, canvasBitmapUnderTest);

		assertNull("Stamp bitmap not recycled.", ((BaseCommand) commandUnderTest).bitmap);
		assertNotNull("Bitmap not stored", ((BaseCommand) commandUnderTest).fileToStoredBitmap);
		Layer secondLayer = new Layer(Bitmap.createBitmap(10, 10, Config.ARGB_8888));
		LayerModel secondModel = new LayerModel();
		secondModel.addLayerAt(0, secondLayer);
		secondModel.setCurrentLayer(secondLayer);
		commandUnderTest.run(canvasUnderTest, secondModel);
		PaintStudioAsserts.assertBitmapEquals(stampBitmapUnderTest, canvasBitmapUnderTest);
	}

	@Test
	public void testRunRotateStamp() {
		stampBitmapUnderTest.setPixel(0, 0, Color.GREEN);
		commandUnderTest = new StampCommand(stampBitmapUnderTest, new Point((int) pointUnderTest.x,
				(int) pointUnderTest.y), canvasBitmapUnderTest.getWidth(), canvasBitmapUnderTest.getHeight(), 180);
		commandUnderTest.run(canvasUnderTest, null);
		stampBitmapUnderTest.setPixel(0, 0, Color.CYAN);
		stampBitmapUnderTest.setPixel(stampBitmapUnderTest.getWidth() - 1, stampBitmapUnderTest.getHeight() - 1,
				Color.GREEN);
		PaintStudioAsserts.assertBitmapEquals(stampBitmapUnderTest, canvasBitmapUnderTest);
		assertNull("Stamp bitmap not recycled.", ((BaseCommand) commandUnderTest).bitmap);
		assertNotNull("Bitmap not stored", ((BaseCommand) commandUnderTest).fileToStoredBitmap);
	}
}
