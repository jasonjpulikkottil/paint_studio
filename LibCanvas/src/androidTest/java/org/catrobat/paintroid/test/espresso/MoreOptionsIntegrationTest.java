package com.jdots.paint.test.espresso;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.common.Constants;
import com.jdots.paint.test.espresso.util.EspressoUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jdots.paint.test.espresso.util.wrappers.OptionsMenuViewInteraction.onOptionsMenu;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MoreOptionsIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	@ClassRule
	public static GrantPermissionRule grantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck();

	@Before
	public void setUp() {
		onTopBarView()
				.performOpenMoreOptions();

		activityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE)
				.edit()
				.clear()
				.commit();
	}

	@After
	public void tearDown() {
		activityTestRule.getActivity().getPreferences(Context.MODE_PRIVATE)
				.edit()
				.clear()
				.commit();
	}

	@Test
	public void testMoreOptionsCloseOnBack() {
		onView(withText(R.string.menu_load_image))
				.check(matches(isDisplayed()));

		pressBack();
		onView(withText(R.string.menu_load_image))
				.check(doesNotExist());
	}

	@Test
	public void testMoreOptionsAllItemsExist() {
		onOptionsMenu()
				.checkItemExists(R.string.menu_load_image)
				.checkItemExists(R.string.menu_hide_menu)
				.checkItemExists(R.string.help_title)
				.checkItemExists(R.string.paint_studio_menu_about)
				.checkItemExists(R.string.menu_rate_us)
				.checkItemExists(R.string.menu_save_image)
				.checkItemExists(R.string.menu_save_copy)
				.checkItemExists(R.string.menu_new_image)
				.checkItemExists(R.string.menu_feedback)
				.checkItemExists(R.string.share_image_menu)

				.checkItemDoesNotExist(R.string.menu_discard_image)
				.checkItemDoesNotExist(R.string.menu_export);
	}

	@Test
	public void testMoreOptionsItemHelpClick() {
		onView(withText(R.string.help_title)).perform(click());
	}

	@Test
	public void testMoreOptionsItemAboutClick() {
		onView(withText(R.string.paint_studio_about_title)).perform(click());
	}

	@Test
	public void testMoreOptionsShareImageClicked() {
		onView(withText(R.string.share_image_menu)).perform(click());
		UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		UiObject uiObject = mDevice.findObject(new UiSelector());
		assertTrue(uiObject.exists());
		mDevice.pressBack();
	}

	@Test
	public void testMoreOptionsItemNewImageClick() {
		onView(withText(R.string.menu_new_image)).perform(click());
	}

	@Test
	public void testMoreOptionsItemMenuSaveClick() {
		onView(withText(R.string.menu_save_image)).perform(click());
	}

	@Test
	public void testMoreOptionsItemMenuCopyClick() {
		onView(withText(R.string.menu_save_copy)).perform(click());
	}
	@Test
	public void testMoreOptionsFeedbackClick() {
		Intent intent = new Intent();
		Intents.init();
		Instrumentation.ActivityResult intentResult = new Instrumentation.ActivityResult(AppCompatActivity.RESULT_OK, intent);

		Intents.intending(IntentMatchers.anyIntent()).respondWith(intentResult);

		onView(withText(R.string.menu_feedback)).perform(click());

		Intents.intended(IntentMatchers.hasAction(Intent.ACTION_SENDTO));
		Intents.release();
	}

	@Test
	public void testShowLikeUsDialogOnFirstSave() {
		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withText(R.string.save_button_text)).perform(click());
		onView(withText(R.string.paint_studio_like_us)).check(matches(isDisplayed()));
	}

	@Test
	public void testSaveDialogAppearsOnSaveImageClick() {
		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withText(R.string.dialog_save_image_name)).check(matches(isDisplayed()));
	}

	@Test
	public void testSaveDialogAppearsOnSaveCopyClick() {
		onView(withText(R.string.menu_save_copy)).perform(click());
		onView(withText(R.string.dialog_save_image_name)).check(matches(isDisplayed()));
	}

	@Test
	public void testSaveDialogSavesChanges() {
		onView(withText(R.string.menu_save_image)).perform(click());

		onView(withId(R.id.paint_studio_save_info_title)).check(matches(isDisplayed()));
		onView(withId(R.id.paint_studio_image_name_save_text)).check(matches(isDisplayed()));
		onView(withId(R.id.paint_studio_save_dialog_spinner)).check(matches(isDisplayed()));

		onView(withId(R.id.paint_studio_save_dialog_spinner))
				.perform(click());
		onData(allOf(is(instanceOf(String.class)),
				is("png"))).inRoot(isPlatformPopup()).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText(Constants.TEMP_PICTURE_NAME));

		onView(withText(R.string.save_button_text))
				.perform(click());
		pressBack();

		onTopBarView()
				.performOpenMoreOptions();

		onView(withText(R.string.menu_save_image)).perform(click());

		onView(withText("png")).check(matches(isDisplayed()));
		onView(withText(Constants.TEMP_PICTURE_NAME)).check(matches(isDisplayed()));
	}

	@Test
	public void testShowRateUsDialogOnLikeUsDialogPositiveButtonPressed() {
		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("1"));
		onView(withText(R.string.save_button_text)).perform(click());
		onView(withText(R.string.paint_studio_yes)).perform(click());
		onView(withText(R.string.paint_studio_rate_us)).check(matches(isDisplayed()));
	}

	@Test
	public void testShowFeedbackDialogOnLikeUsDialogNegativeButtonPressed() {
		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("12"));
		onView(withText(R.string.save_button_text)).perform(click());
		onView(withText(R.string.paint_studio_no)).perform(click());
		onView(withText(R.string.paint_studio_feedback)).check(matches(isDisplayed()));
	}

	@Test
	public void testLikeUsDialogNotShownOnSecondSave() {
		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("123"));
		onView(withText(R.string.save_button_text)).perform(click());

		onView(withText(R.string.paint_studio_like_us)).check(matches(isDisplayed()));
		pressBack();

		onTopBarView()
				.performOpenMoreOptions();

		onView(withText(R.string.menu_save_image)).perform(click());
		onView(withText(R.string.paint_studio_like_us)).check(doesNotExist());
		onView(withText(R.string.save_button_text)).perform(click());
	}
}
