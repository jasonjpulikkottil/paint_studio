/*

 */package com.jdots.paint.test.espresso.tools;

import android.graphics.Color;

import com.jdots.paint.MainActivity;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.drawable.DrawableShape;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ShapeToolOptionsViewInteraction.onShapeToolOptionsView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolPropertiesInteraction.onToolProperties;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;

@RunWith(Parameterized.class)
public class ShapeToolEraseIntegrationTest {

	@Parameter
	public DrawableShape shape;

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{DrawableShape.RECTANGLE},
				{DrawableShape.OVAL},
				{DrawableShape.HEART},
				{DrawableShape.STAR}
		});
	}

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void testEraseWithFilledShape() {

		onToolBarView()
				.performSelectTool(ToolType.SHAPE)
				.performCloseToolOptionsView();

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);

		onToolProperties()
				.setColor(Color.TRANSPARENT);

		onToolBarView()
				.performOpenToolOptionsView();

		onShapeToolOptionsView()
				.performSelectShape(shape);

		onToolBarView()
				.performCloseToolOptionsView();

		onTopBarView()
				.performClickCheckmark();

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);
	}
}
