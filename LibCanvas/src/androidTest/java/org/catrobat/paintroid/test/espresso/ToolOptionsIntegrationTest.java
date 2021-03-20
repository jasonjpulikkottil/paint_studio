package com.jdots.paint.test.espresso;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import com.jdots.paint.MainActivity;
import com.jdots.paint.tools.ToolType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ToolOptionsIntegrationTest {

	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new IntentsTestRule<>(MainActivity.class);

	@Parameter
	public ToolType toolType;

	@Parameter(1)
	public boolean toolOptionsShownInitially;

	@Parameter(2)
	public boolean hasToolOptionsView;

	private File testImageFile;

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{ToolType.BRUSH, false, true},
				{ToolType.SHAPE, true, true},
				{ToolType.TRANSFORM, true, true},
				{ToolType.LINE, false, true},
				{ToolType.CURSOR, false, true},
				{ToolType.FILL, false, true},
				{ToolType.STAMP, true, true},
				{ToolType.ERASER, false, true},
				{ToolType.TEXT, true, true},
				{ToolType.HAND, false, false}
		});
	}

	@Before
	public void setUp() {
		try {
			testImageFile = File.createTempFile("paint_studioTest", ".png");
			Bitmap bitmap = Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_8888);
			OutputStream outputStream = new FileOutputStream(testImageFile);
			assertTrue(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream));
			outputStream.close();
		} catch (IOException e) {
			throw new AssertionError("Could not create temp file", e);
		}

		Intent intent = new Intent();
		intent.setData(Uri.fromFile(testImageFile));
		Instrumentation.ActivityResult resultOK = new Instrumentation.ActivityResult(AppCompatActivity.RESULT_OK, intent);
		intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultOK);
	}

	@After
	public void tearDown() {
		assertTrue(testImageFile.delete());
	}

	@Test
	public void testToolOptions() {
		onToolBarView()
				.performSelectTool(toolType);

		if (!toolOptionsShownInitially) {
			onToolBarView()
					.performOpenToolOptionsView();
		}

		if (hasToolOptionsView) {
			onToolBarView().onToolOptionsView()
					.check(matches(isDisplayed()));
		} else {
			onToolBarView().onToolOptionsView()
					.check(matches(not(isDisplayed())));
		}
	}
}
