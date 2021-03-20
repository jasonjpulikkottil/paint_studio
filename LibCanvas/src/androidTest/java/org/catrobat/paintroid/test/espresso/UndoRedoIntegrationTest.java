package com.jdots.paint.test.espresso;

import android.content.pm.ActivityInfo;
import android.graphics.Color;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.espresso.util.MainActivityHelper;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.ui.Perspective;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.UiMatcher.withDrawable;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UndoRedoIntegrationTest {
	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	private MainActivityHelper activityHelper;

	private Perspective perspective;

	@Before
	public void setUp() {
		activityHelper = new MainActivityHelper(launchActivityRule.getActivity());

		activityHelper.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		perspective = launchActivityRule.getActivity().perspective;

		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void testUndoRedoIconsWhenSwitchToLandscapeMode() {
		assertEquals(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, activityHelper.getScreenOrientation());

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo_disabled), not(isEnabled()))));
		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo_disabled), not(isEnabled()))));

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.HALFWAY_TOP_LEFT));
		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_TOP_LEFT);

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo), isEnabled())));

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE))
				.perform(touchAt(DrawingSurfaceLocationProvider.HALFWAY_BOTTOM_RIGHT));
		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE)
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_BOTTOM_RIGHT);

		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo_disabled), not(isEnabled()))));

		onTopBarView()
				.performUndo();

		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo), isEnabled())));

		activityHelper.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		assertEquals(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, activityHelper.getScreenOrientation());

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo), isEnabled())));
		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo), isEnabled())))
				.perform(click())
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo_disabled), not(isEnabled()))));

		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_BOTTOM_RIGHT)
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE)
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.HALFWAY_TOP_LEFT);

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo), isEnabled())))
				.perform(click());

		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_BOTTOM_RIGHT);
		onTopBarView()
				.performUndo();
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);
		onTopBarView()
				.performUndo();
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.HALFWAY_TOP_LEFT);

		onView(withId(R.id.paint_studio_btn_top_undo))
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo_disabled), not(isEnabled()))));
	}

	@Test
	public void testDisableEnableUndo() {
		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo_disabled), not(isEnabled()))));

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo), isEnabled())));

		onTopBarView()
				.performUndo();

		onTopBarView().onUndoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_undo_disabled), not(isEnabled()))));
	}

	@Test
	public void testDisableEnableRedo() {
		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo_disabled), not(isEnabled()))));

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo_disabled), not(isEnabled()))));

		onTopBarView()
				.performUndo();

		onTopBarView().onRedoButton()
				.check(matches(allOf(withDrawable(R.drawable.ic_paint_studio_redo), isEnabled())));
	}

	@Test
	public void testPreserveZoomAndMoveAfterUndo() {

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		float scale = .5f;
		int translationX = 10;
		int translationY = 15;

		perspective.setScale(scale);
		perspective.setSurfaceTranslationX(translationX);
		perspective.setSurfaceTranslationY(translationY);

		onTopBarView()
				.performUndo();

		assertEquals(scale, perspective.getScale(), Float.MIN_VALUE);
		assertEquals(translationX, perspective.getSurfaceTranslationX(), Float.MIN_VALUE);
		assertEquals(translationY, perspective.getSurfaceTranslationY(), Float.MIN_VALUE);
	}

	@Test
	public void testPreserveZoomAndMoveAfterRedo() {

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onTopBarView()
				.performUndo();

		float scale = .5f;
		int translationX = 10;
		int translationY = 15;

		perspective.setScale(scale);
		perspective.setSurfaceTranslationX(translationX);
		perspective.setSurfaceTranslationY(translationY);

		onTopBarView()
				.performRedo();

		assertEquals(scale, perspective.getScale(), Float.MIN_VALUE);
		assertEquals(translationX, perspective.getSurfaceTranslationX(), Float.MIN_VALUE);
		assertEquals(translationY, perspective.getSurfaceTranslationY(), Float.MIN_VALUE);
	}
}
