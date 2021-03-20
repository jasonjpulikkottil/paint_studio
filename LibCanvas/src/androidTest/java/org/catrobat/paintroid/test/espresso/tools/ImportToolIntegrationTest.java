package com.jdots.paint.test.espresso.tools;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.rtl.util.RtlActivityTestRule;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.junit.Assert.assertEquals;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ImportToolIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new RtlActivityTestRule<>(MainActivity.class, "ar");
	private MainActivity mainActivity;

	@Before
	public void setUp() {
		mainActivity = launchActivityRule.getActivity();
		onToolBarView()
				.performSelectTool(ToolType.IMPORTPNG);
	}

	@Test
	public void testImportDialogShownOnImportToolSelected() {
		onView(withId(R.id.paint_studio_dialog_import_stickers)).check(matches(isDisplayed()));
		onView(withId(R.id.paint_studio_dialog_import_gallery)).check(matches(isDisplayed()));
	}

	@Test
	public void testImportDialogDismissedOnCancelClicked() {
		onView(withText(R.string.paint_studio_cancel)).perform(click());

		onView(withId(R.id.paint_studio_dialog_import_stickers)).check(doesNotExist());
		onView(withId(R.id.paint_studio_dialog_import_gallery)).check(doesNotExist());
	}

	@Test
	public void testImportDoesNotResetPerspectiveScale() {
		onView(withText(R.string.paint_studio_cancel)).perform(click());

		onToolBarView()
				.performSelectTool(ToolType.BRUSH);

		float scale = 2.0f;
		mainActivity.perspective.setScale(scale);
		mainActivity.refreshDrawingSurface();

		onToolBarView()
				.performSelectTool(ToolType.IMPORTPNG);

		onView(withText(R.string.paint_studio_cancel)).perform(click());

		assertEquals(scale, mainActivity.perspective.getScale(), Float.MIN_VALUE);
	}
}
