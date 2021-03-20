package com.jdots.paint.test.espresso.tools;

import android.graphics.Color;
import android.graphics.Paint.Cap;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.EspressoUtils.DEFAULT_STROKE_WIDTH;
import static com.jdots.paint.test.espresso.util.UiInteractions.swipe;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolPropertiesInteraction.onToolProperties;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LineToolIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.LINE);
	}

	@Test
	public void testVerticalLineColor() {
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);

		onDrawingSurfaceView()
				.perform(swipe(DrawingSurfaceLocationProvider.HALFWAY_TOP_MIDDLE, DrawingSurfaceLocationProvider.HALFWAY_BOTTOM_MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testHorizontalLineColor() {
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);

		onDrawingSurfaceView()
				.perform(swipe(DrawingSurfaceLocationProvider.HALFWAY_LEFT_MIDDLE, DrawingSurfaceLocationProvider.HALFWAY_RIGHT_MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testDiagonalLineColor() {
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);

		onDrawingSurfaceView()
				.perform(swipe(DrawingSurfaceLocationProvider.HALFWAY_TOP_LEFT, DrawingSurfaceLocationProvider.HALFWAY_BOTTOM_RIGHT));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testChangeLineToolForm() {
		onToolBarView()
				.performOpenToolOptionsView();

		onView(withId(R.id.paint_studio_stroke_ibtn_rect))
				.perform(click());

		onToolProperties()
				.checkStrokeWidth(DEFAULT_STROKE_WIDTH)
				.checkCap(Cap.SQUARE);
	}
}
