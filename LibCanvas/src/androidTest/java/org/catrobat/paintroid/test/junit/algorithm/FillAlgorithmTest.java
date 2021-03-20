package com.jdots.paint.test.junit.algorithm;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.jdots.paint.tools.helper.JavaFillAlgorithm;
import com.jdots.paint.tools.implementation.FillTool;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Queue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FillAlgorithmTest {
	private static final float HALF_TOLERANCE = FillTool.MAX_ABSOLUTE_TOLERANCE / 2.0f;

	@Test
	public void testFillAlgorithmMembers() {
		int width = 10;
		int height = 20;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Point clickedPixel = new Point(width / 2, height / 2);
		int targetColor = 16777215;
		int replacementColor = 0;

		JavaFillAlgorithm fillAlgorithm = new JavaFillAlgorithm();
		fillAlgorithm.setParameters(bitmap, clickedPixel, targetColor, replacementColor, HALF_TOLERANCE);

		int[][] algorithmPixels = fillAlgorithm.pixels;
		assertEquals("Wrong array size", height, algorithmPixels.length);
		assertEquals("Wrong array size", width, algorithmPixels[0].length);

		int algorithmTargetColor = fillAlgorithm.targetColor;
		int algorithmReplacementColor = fillAlgorithm.replacementColor;
		int algorithmColorTolerance = fillAlgorithm.colorToleranceThresholdSquared;
		assertEquals("Wrong target color", targetColor, algorithmTargetColor);
		assertEquals("Wrong replacement color", replacementColor, algorithmReplacementColor);
		assertEquals("Wrong color tolerance", (int) (HALF_TOLERANCE * HALF_TOLERANCE), algorithmColorTolerance);

		Point algorithmClickedPixel = fillAlgorithm.clickedPixel;
		assertEquals("Wrong point for clicked pixel", clickedPixel, algorithmClickedPixel);

		Queue algorithmRanges = fillAlgorithm.ranges;
		assertTrue("Queue for ranges should be empty", algorithmRanges.isEmpty());
	}
}
