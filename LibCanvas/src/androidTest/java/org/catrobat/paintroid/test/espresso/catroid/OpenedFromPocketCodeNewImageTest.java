package com.jdots.paint.test.espresso.catroid;

import android.content.Intent;

import com.jdots.paint.FileIO;
import com.jdots.paint.MainActivity;
import com.jdots.paint.common.Constants;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.espresso.util.EspressoUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;
import static com.jdots.paint.test.espresso.util.wrappers.DrawingSurfaceInteraction.onDrawingSurfaceView;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;

@RunWith(AndroidJUnit4.class)
public class OpenedFromPocketCodeNewImageTest {

	private static final String IMAGE_NAME = "testFile";

	@Rule
	public IntentsTestRule<MainActivity> launchActivityRule = new IntentsTestRule<>(MainActivity.class, false, false);

	@ClassRule
	public static GrantPermissionRule grantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck();

	private File imageFile = null;

	@Before
	public void setUp() {
		Intent intent = new Intent();
		intent.putExtra(Constants.PaintStudio_PICTURE_PATH, "");
		intent.putExtra(Constants.PaintStudio_PICTURE_NAME, IMAGE_NAME);

		launchActivityRule.launchActivity(intent);

		imageFile = getImageFile(IMAGE_NAME);
	}

	@After
	public void tearDown() {
		if (imageFile.exists()) {
			imageFile.delete();
		}
	}

	@Test
	public void testSave() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		Espresso.pressBackUnconditionally();

		String path = launchActivityRule.getActivityResult().getResultData().getStringExtra(Constants.PaintStudio_PICTURE_PATH);
		assertEquals(imageFile.getAbsolutePath(), path);

		assertTrue(imageFile.exists());
		assertThat(imageFile.length(), greaterThan(0L));
	}

	private File getImageFile(String filename) {
		try {
			return FileIO.createNewEmptyPictureFile(filename, launchActivityRule.getActivity());
		} catch (NullPointerException e) {
			throw new AssertionError("Could not create temp file", e);
		}
	}
}
