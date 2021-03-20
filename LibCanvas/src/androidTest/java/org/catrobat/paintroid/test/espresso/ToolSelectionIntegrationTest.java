package com.jdots.paint.test.espresso;

import com.jdots.paint.MainActivity;
import com.jdots.paint.tools.ToolType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.EspressoUtils.waitForToast;
import static com.jdots.paint.test.espresso.util.wrappers.BottomNavigationViewInteraction.onBottomNavigationView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;

import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ToolSelectionIntegrationTest {
	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Before
	public void setUp() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
	}

	@Test
	public void testToolSelectionToast() {
		ToolType toolType = ToolType.CURSOR;
		onToolBarView()
				.performSelectTool(toolType);

		waitForToast(withText(toolType.getNameResource()), 1000);
	}

	@Test
	public void testIfCurrentToolIsShownInBottomNavigation() {

		for (ToolType toolType : ToolType.values()) {
			if (toolType == ToolType.IMPORTPNG
					|| toolType == ToolType.COLORCHOOSER
					|| toolType == ToolType.REDO
					|| toolType == ToolType.UNDO
					|| toolType == ToolType.PIPETTE
					|| toolType == ToolType.LAYER) {
				continue;
			}

			onToolBarView()
					.performSelectTool(toolType);
			onBottomNavigationView()
					.checkShowsCurrentTool(toolType);
		}
	}
}
