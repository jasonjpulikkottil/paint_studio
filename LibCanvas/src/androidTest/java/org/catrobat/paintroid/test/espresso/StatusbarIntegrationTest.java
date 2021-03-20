/**

 */package com.jdots.paint.test.espresso;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StatusbarIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void statusBarButtonsShouldAllBeVisible() {
		onView(withId(R.id.paint_studio_btn_top_undo)).check(matches(isDisplayed()));
		onView(withId(R.id.paint_studio_btn_top_redo)).check(matches(isDisplayed()));
	}
}
