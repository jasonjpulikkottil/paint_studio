package com.jdots.paint.test.espresso;

import android.util.DisplayMetrics;

import com.jdots.paint.MainActivity;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BitmapIntegrationTest {
	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void drawingSurfaceBitmapIsDisplaySize() {
		MainActivity activity = launchActivityRule.getActivity();
		Workspace workspace = activity.workspace;
		final int bitmapWidth = workspace.getWidth();
		final int bitmapHeight = workspace.getHeight();

		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		assertEquals(metrics.widthPixels, bitmapWidth);
		assertEquals(metrics.heightPixels, bitmapHeight);
	}
}
