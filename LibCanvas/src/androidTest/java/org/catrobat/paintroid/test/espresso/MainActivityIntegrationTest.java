/**

 */package com.jdots.paint.test.espresso;

import android.content.Context;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void testMoreOptionsMenuAboutTextIsCorrect() {

		onTopBarView()
				.performOpenMoreOptions();
		onView(withText(R.string.paint_studio_menu_about))
				.perform(click());

		Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
		String aboutTextExpected = context.getString(R.string.paint_studio_about_content,
				context.getString(R.string.paint_studio_about_license));

		onView(withText(aboutTextExpected))
				.check(matches(isDisplayed()));
	}

	@Test
	public void testMoreOptionsMenuAboutClosesMoreOptions() {

		onTopBarView()
				.performOpenMoreOptions();

		onView(withText(R.string.paint_studio_menu_about))
				.perform(click());

		pressBack();

		onView(withText(R.string.paint_studio_menu_about))
				.check(doesNotExist());
	}
}
