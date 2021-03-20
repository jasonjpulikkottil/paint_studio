/*

 */package com.jdots.paint.test.espresso.catroid;

import android.net.Uri;
import android.os.Environment;

import com.jdots.paint.MainActivity;
import com.jdots.paint.common.Constants;
import com.jdots.paint.test.espresso.util.DrawingSurfaceLocationProvider;
import com.jdots.paint.test.espresso.util.EspressoUtils;
import com.jdots.paint.tools.ToolType;
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
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OpenedFromPocketCodeWithImageTest {

	private static final String IMAGE_NAME = "testFile";
	private static final String FILE_ENDING = ".png";

	@Rule
	public IntentsTestRule<MainActivity> launchActivityRule = new IntentsTestRule<>(MainActivity.class, false, true);

	@ClassRule
	public static GrantPermissionRule grantPermissionRule = EspressoUtils.grantPermissionRulesVersionCheck();

	private File imageFile = null;

	@Before
	public void setUp() {
		String pathToFile =
				launchActivityRule.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
						+ File.separator
						+ IMAGE_NAME
						+ FILE_ENDING;

		imageFile = new File(pathToFile);
		launchActivityRule.getActivity().model.setSavedPictureUri(Uri.fromFile(imageFile));
		launchActivityRule.getActivity().model.setOpenedFromCatroid(true);

		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@After
	public void tearDown() {
		if (imageFile != null && imageFile.exists()) {
			assertTrue(imageFile.delete());
		}
	}

	@Test
	public void testSave() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		long lastModifiedBefore = imageFile.lastModified();
		long fileSizeBefore = imageFile.length();

		Espresso.pressBackUnconditionally();

		String path = launchActivityRule.getActivityResult().getResultData().getStringExtra(Constants.PaintStudio_PICTURE_PATH);
		assertEquals(imageFile.getAbsolutePath(), path);

		assertThat("Image modification not saved", imageFile.lastModified(), greaterThan(lastModifiedBefore));
		assertThat("Saved image length not changed", imageFile.length(), greaterThan(fileSizeBefore));
	}

	@Test
	public void testBackToPocketCode() {
		onDrawingSurfaceView()
				.perform(touchAt(DrawingSurfaceLocationProvider.MIDDLE));

		Espresso.pressBackUnconditionally();

		long lastModifiedBefore = imageFile.lastModified();
		long fileSizeBefore = imageFile.length();

		assertThat("Image modified", imageFile.lastModified(), equalTo(lastModifiedBefore));
		assertThat("Saved image length changed", imageFile.length(), equalTo(fileSizeBefore));
	}
}
