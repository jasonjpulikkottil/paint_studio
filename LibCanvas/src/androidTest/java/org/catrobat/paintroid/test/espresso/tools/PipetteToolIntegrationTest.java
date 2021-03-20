/*

 */package com.jdots.paint.test.espresso.tools;

import android.graphics.Color;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.LayerMenuViewInteraction.onLayerMenuView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolPropertiesInteraction.onToolProperties;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;

@RunWith(AndroidJUnit4.class)
public class PipetteToolIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Ignore("Fail due to Pipette")
	@Test
	public void testOnEmptyBitmap() {
		onToolProperties()
				.checkMatchesColor(Color.BLACK);

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);

		onToolBarView()
				.performSelectTool(ToolType.PIPETTE);

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolProperties()
				.checkMatchesColor(Color.TRANSPARENT);
	}

	@Ignore("Fail due to Pipette")
	@Test
	public void testPipetteAfterBrushOnSingleLayer() {
		onToolProperties()
				.setColor(Color.RED);
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.RED, BitmapLocationProvider.MIDDLE);

		onToolProperties()
				.setColorResource(R.color.paint_studio_color_picker_transparent)
				.checkMatchesColor(Color.TRANSPARENT);
		onToolBarView()
				.performSelectTool(ToolType.PIPETTE);

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolProperties()
				.checkMatchesColor(Color.RED);
	}

	@Ignore("Fail due to Pipette")
	@Test
	public void testPipetteAfterBrushOnMultiLayer() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);

		onLayerMenuView()
				.performOpen()
				.performAddLayer()
				.performClose();

		onToolProperties()
				.setColor(Color.TRANSPARENT);
		onToolBarView()
				.performSelectTool(ToolType.PIPETTE);

		onToolProperties()
				.checkMatchesColor(Color.TRANSPARENT);

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE)
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolProperties()
				.checkMatchesColor(Color.BLACK);
	}

	@Ignore("Flaky on Jenkins, for further information https://github.com/Catrobat/PaintStudio/pull/794")
	@Test
	public void testPipetteAfterUndo() {

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolBarView()
				.performSelectTool(ToolType.PIPETTE);

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolProperties()
				.checkMatchesColor(Color.BLACK);

		onTopBarView()
				.performUndo();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));
		onToolProperties()
				.checkMatchesColor(Color.TRANSPARENT);
	}

	@Ignore("Flaky on Jenkins, for further information https://github.com/Catrobat/PaintStudio/pull/794")
	@Test
	public void testPipetteAfterRedo() {

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onToolBarView()
				.performSelectTool(ToolType.PIPETTE);

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));
		onToolProperties()
				.checkMatchesColor(Color.BLACK);

		onTopBarView()
				.performUndo();

		onTopBarView()
				.performRedo();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));
		onToolProperties()
				.checkMatchesColor(Color.BLACK);
	}
}
