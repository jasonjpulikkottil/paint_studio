
package com.jdots.paint.test.espresso;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.espresso.util.EspressoUtils;

import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static com.jdots.paint.test.espresso.util.wrappers.LayerMenuViewInteraction.onLayerMenuView;
import static com.jdots.paint.test.espresso.util.wrappers.TopBarViewInteraction.onTopBarView;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OraFileFormatIntegrationTest {

	private static ArrayList<File> deletionFileList = null;

	@Rule
	public IntentsTestRule<MainActivity> launchActivityRule = new IntentsTestRule<>(MainActivity.class);

	@ClassRule
	public static GrantPermissionRule grantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck();

	private MainActivity activity;

	@Before
	public void setUp() {
		deletionFileList = new ArrayList<>();
		activity = launchActivityRule.getActivity();
	}

	@After
	public void tearDown() {
		for (File file : deletionFileList) {
			if (file != null && file.exists()) {
				assertTrue(file.delete());
			}
		}
	}

	@Test
	public void testSaveAsOraFile() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onTopBarView()
				.performOpenMoreOptions();
		onView(withText(R.string.menu_save_image))
				.perform(click());

		onView(withId(R.id.paint_studio_save_dialog_spinner))
				.perform(click());
		onData(allOf(is(instanceOf(String.class)),
				is("ora"))).inRoot(isPlatformPopup()).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("test1337"));

		onView(withText(R.string.save_button_text))
				.perform(click());

		assertNotNull(activity.model.getSavedPictureUri());
		addUriToDeletionFileList(activity.model.getSavedPictureUri());
	}

	@Test
	public void testSaveAndOverrideOraFile() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onTopBarView()
				.performOpenMoreOptions();
		onView(withText(R.string.menu_save_image))
				.perform(click());

		onView(withId(R.id.paint_studio_save_dialog_spinner))
				.perform(click());
		onData(allOf(is(instanceOf(String.class)),
				is("ora"))).inRoot(isPlatformPopup()).perform(click());

		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("OraOverride"));

		onView(withText(R.string.save_button_text))
				.perform(click());

		onView(withText(R.string.paint_studio_no)).perform(click());
		onView(withText(R.string.paint_studio_ok)).perform(click());

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.BOTTOM_MIDDLE));

		onTopBarView()
				.performOpenMoreOptions();
		onView(withText(R.string.menu_save_image))
				.perform(click());

		onView(withText(R.string.save_button_text))
				.perform(click());

		onView(withText(R.string.paint_studio_overwrite_title))
				.check(matches(isDisplayed()));

		onView(withText(R.string.overwrite_button_text))
				.perform(click());

		Uri imageUri = activity.model.getSavedPictureUri();

		assertNotNull(imageUri);
		addUriToDeletionFileList(imageUri);
	}

	@Test
	public void testOraFileWithMultipleLayersSaveAndLoad() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		onLayerMenuView()
				.performOpen()
				.performAddLayer()
				.checkLayerCount(2)
				.performClose();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.TOP_MIDDLE));

		onLayerMenuView()
				.performOpen()
				.performAddLayer()
				.checkLayerCount(3)
				.performClose();

		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.BOTTOM_MIDDLE));

		onTopBarView()
				.performOpenMoreOptions();

		onView(withText(R.string.menu_save_image))
				.perform(click());

		onView(withId(R.id.paint_studio_save_dialog_spinner))
				.perform(click());
		onData(AllOf.allOf(is(instanceOf(String.class)),
				is("ora"))).inRoot(isPlatformPopup()).perform(click());
		onView(withId(R.id.paint_studio_image_name_save_text))
				.perform(replaceText("MoreLayersOraTest"));

		onView(withText(R.string.save_button_text))
				.perform(click());

		Uri fileUri = activity.model.getSavedPictureUri();

		assertNotNull(fileUri);
		addUriToDeletionFileList(fileUri);

		Intent intent = new Intent();
		intent.setData(fileUri);
		Instrumentation.ActivityResult resultOK = new Instrumentation.ActivityResult(AppCompatActivity.RESULT_OK, intent);
		intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultOK);

		onTopBarView()
				.performOpenMoreOptions();

		onView(withText(R.string.menu_load_image))
				.perform(click());

		onLayerMenuView()
				.performOpen()
				.checkLayerCount(3);
	}

	private void addUriToDeletionFileList(Uri uri) {
		deletionFileList.add(new File(Objects.requireNonNull(uri.getPath())));
	}
}
