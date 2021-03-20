/*

 */package com.jdots.paint.test.espresso.catroid;

import android.content.Intent;
import android.graphics.Color;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.common.Constants;
import com.jdots.paint.test.espresso.util.BitmapLocationProvider;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.espresso.util.EspressoUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.OptionsMenuViewInteraction.onOptionsMenu;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MoreOptionsIntegrationTest {

	@Rule
	public IntentsTestRule<MainActivity> launchActivityRule = new IntentsTestRule<>(MainActivity.class, false, false);

	@ClassRule
	public static GrantPermissionRule grantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck();

	@Before
	public void setUp() {
		Intent intent = new Intent();
		intent.putExtra(Constants.PaintStudio_PICTURE_PATH, "");
		intent.putExtra(Constants.PaintStudio_PICTURE_NAME, "testFile");
		launchActivityRule.launchActivity(intent);
	}

	@Test
	public void testMoreOptionsAllItemsExist() {
		onTopBarView()
				.performOpenMoreOptions();

		onOptionsMenu()
				.checkItemExists(R.string.menu_load_image)
				.checkItemExists(R.string.menu_hide_menu)
				.checkItemExists(R.string.help_title)
				.checkItemExists(R.string.paint_studio_menu_about)
				.checkItemExists(R.string.share_image_menu)

				.checkItemDoesNotExist(R.string.menu_save_image)
				.checkItemDoesNotExist(R.string.menu_save_copy)
				.checkItemDoesNotExist(R.string.menu_new_image)
				.checkItemDoesNotExist(R.string.menu_rate_us)

				.checkItemExists(R.string.menu_discard_image)
				.checkItemExists(R.string.menu_export);
	}

	@Test
	public void testMoreOptionsDiscardImage() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));
		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
		onTopBarView()
				.performOpenMoreOptions();
		onView(withText(R.string.menu_discard_image))
				.perform(click());
		onDrawingSurfaceView()
				.checkPixelColor(Color.TRANSPARENT, BitmapLocationProvider.MIDDLE);
	}

	@Test
	public void testMoreOptionsShareImageClick() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));
		onDrawingSurfaceView()
				.checkPixelColor(Color.BLACK, BitmapLocationProvider.MIDDLE);
		onTopBarView()
				.performOpenMoreOptions();
		onView(withText("Share image"))
				.perform(click());
		UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		UiObject uiObject = mDevice.findObject(new UiSelector());
		assertTrue(uiObject.exists());
		mDevice.pressBack();
	}
}
