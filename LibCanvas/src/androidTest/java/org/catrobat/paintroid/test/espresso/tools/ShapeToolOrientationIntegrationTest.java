/*

 */package com.jdots.paint.test.espresso.tools;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.drawable.DrawableStyle;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ShapeToolOptionsViewInteraction.onShapeToolOptionsView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(Parameterized.class)
public class ShapeToolOrientationIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	@Parameter
	public DrawableShape shape;
	@Parameter(1)
	public int shapeId;

	private Workspace workspace;

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{DrawableShape.RECTANGLE, R.id.paint_studio_shapes_square_btn},
				{DrawableShape.OVAL, R.id.paint_studio_shapes_circle_btn},
				{DrawableShape.HEART, R.id.paint_studio_shapes_heart_btn},
				{DrawableShape.STAR, R.id.paint_studio_shapes_star_btn}
		});
	}

	@Before
	public void setUp() {
		workspace = activityTestRule.getActivity().workspace;
		onToolBarView()
				.performSelectTool(ToolType.SHAPE);
	}

	@Test
	public void testRememberShapeAfterOrientationChange() {
		onShapeToolOptionsView()
				.performSelectShape(shape);
		onView(withId(shapeId))
				.check(matches(isSelected()));
		onToolBarView()
				.performCloseToolOptionsView();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.TOOL_POSITION));

		Bitmap expectedBitmap = workspace.getBitmapOfCurrentLayer();

		onTopBarView()
				.performUndo();

		activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		onView(withId(shapeId))
				.check(matches(isSelected()));

		onToolBarView()
				.performCloseToolOptionsView();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.TOOL_POSITION));

		assertTrue(expectedBitmap.sameAs(workspace.getBitmapOfCurrentLayer()));
	}

	@Test
	public void testRememberOutlineShapeAfterOrientationChange() {
		onShapeToolOptionsView()
				.performSelectShape(shape)
				.performSelectShapeDrawType(DrawableStyle.STROKE);
		onView(withId(shapeId))
				.check(matches(isSelected()));
		onView(withId(R.id.paint_studio_shape_ibtn_outline))
				.check(matches(isSelected()));
		onToolBarView()
				.performCloseToolOptionsView();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.TOOL_POSITION));

		Bitmap expectedBitmap = workspace.getBitmapOfCurrentLayer();
		onTopBarView()
				.performUndo();

		activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		onView(withId(shapeId))
				.check(matches(isSelected()));
		onView(withId(R.id.paint_studio_shape_ibtn_outline))
				.check(matches(isSelected()));

		onToolBarView()
				.performCloseToolOptionsView();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.TOOL_POSITION));

		assertTrue(expectedBitmap.sameAs(workspace.getBitmapOfCurrentLayer()));
	}
}
