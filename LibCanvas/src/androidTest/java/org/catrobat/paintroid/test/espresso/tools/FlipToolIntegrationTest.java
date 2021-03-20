/*

 */package com.jdots.paint.test.espresso.tools;

import android.graphics.Color;

import com.jdots.paint.MainActivity;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.utils.ScreenshotOnFailRule;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.TransformToolOptionsViewInteraction.onTransformToolOptionsView;

@RunWith(AndroidJUnit4.class)
public class FlipToolIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Rule
	public ScreenshotOnFailRule screenshotOnFailRule = new ScreenshotOnFailRule();

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void testHorizontalFlip() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.HALFWAY_TOP_MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_TOP_MIDDLE)
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_BOTTOM_MIDDLE);

		onToolBarView()
				.performSelectTool(ToolType.TRANSFORM);

		onTransformToolOptionsView()
				.performFlipHorizontal();

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_TOP_MIDDLE)
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_BOTTOM_MIDDLE);
	}

	@Test
	public void testVerticalFlip() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.HALFWAY_LEFT_MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_LEFT_MIDDLE)
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_RIGHT_MIDDLE);

		onToolBarView()
				.performSelectTool(ToolType.TRANSFORM);

		onTransformToolOptionsView()
				.performFlipVertical();

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_LEFT_MIDDLE)
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_RIGHT_MIDDLE);
	}
}
