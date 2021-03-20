package com.jdots.paint.test.junit.algorithm;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import com.jdots.paint.tools.helper.JavaCropAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class JavaCropAlgorithmTest {

	private JavaCropAlgorithm cropAlgorithm;
	private Bitmap bitmap;

	@Before
	public void setUp() {
		cropAlgorithm = new JavaCropAlgorithm();
		bitmap = Bitmap.createBitmap(3, 3, Bitmap.Config.ARGB_8888);
	}

	@Test
	public void testJavaCropAlgorithmWhenMiddlePixelFilled() {
		bitmap.setPixel(1, 1, Color.BLACK);
		Rect expectedBounds = new Rect(1, 1, 1, 1);

		Rect bounds = cropAlgorithm.crop(bitmap);

		assertEquals(expectedBounds, bounds);
	}

	@Test
	public void testJavaCropAlgorithmWhenTopRightPixelFilled() {
		bitmap.setPixel(2, 2, Color.BLACK);
		Rect expectedBounds = new Rect(2, 2, 2, 2);

		Rect bounds = cropAlgorithm.crop(bitmap);

		assertEquals(expectedBounds, bounds);
	}

	@Test
	public void testJavaCropAlgorithmWhenBitmapTransparentThenReturnNull() {
		Rect bounds = cropAlgorithm.crop(bitmap);

		assertNull(bounds);
	}

	@Test
	public void testJavaCropAlgorithmWhenBitmapFilled() {
		bitmap.eraseColor(Color.BLACK);
		Rect expectedBounds = new Rect(0, 0, 2, 2);

		Rect bounds = cropAlgorithm.crop(bitmap);

		assertEquals(expectedBounds, bounds);
	}
}
