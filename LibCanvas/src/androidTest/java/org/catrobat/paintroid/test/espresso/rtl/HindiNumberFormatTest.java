package com.jdots.paint.test.espresso.rtl;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.rtl.util.RtlActivityTestRule;
import com.jdots.paint.tools.ToolType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiMatcher.withBackground;
import static com.jdots.paint.test.espresso.util.wrappers.ColorPickerViewInteraction.onColorPickerView;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class HindiNumberFormatTest {
	private static final String EXPECTED_RED_VALUE = "٢٤٠";
	private static final String EXPECTED_GREEN_VALUE = "٢٢٨";
	private static final String EXPECTED_BLAU_VALUE = "١٦٨";
	private static final String EXPECTED_ALFA_VALUE = "١٠٠";
	private static final String EXPECTED_STROKE_WIDTH_VALUE = "٢٥";
	private static final String EXPECTED_COLOR_TOLERANCE_VALUE = "١٢";
	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new RtlActivityTestRule<>(MainActivity.class, "ar");

	@Test
	public void testHindiNumberAtTool() {
		onToolBarView()
				.performSelectTool(ToolType.BRUSH);
		onView(withId(R.id.paint_studio_stroke_width_width_text))
				.check(matches(withText(containsString(EXPECTED_STROKE_WIDTH_VALUE))));

		onToolBarView()
				.performSelectTool(ToolType.LINE);
		onView(withId(R.id.paint_studio_stroke_width_width_text))
				.check(matches(withText(containsString(EXPECTED_STROKE_WIDTH_VALUE))));

		onToolBarView()
				.performSelectTool(ToolType.CURSOR);
		onView(withId(R.id.paint_studio_stroke_width_width_text))
				.check(matches(withText(containsString(EXPECTED_STROKE_WIDTH_VALUE))));

		onToolBarView()
				.performSelectTool(ToolType.FILL);
		onView(withId(R.id.paint_studio_fill_tool_dialog_color_tolerance_input))
				.check(matches(withText(containsString(EXPECTED_COLOR_TOLERANCE_VALUE))));

		onToolBarView()
				.performSelectTool(ToolType.ERASER);
		onView(withId(R.id.paint_studio_stroke_width_width_text))
				.check(matches(withText(containsString(EXPECTED_STROKE_WIDTH_VALUE))));
	}

	@Test
	public void testHindiNumberAtColorDialog() {
		onColorPickerView()
				.performOpenColorPicker()
				.performClickColorPickerPresetSelectorButton(7);
		onView(allOf(withId(R.id.color_picker_tab_icon), withBackground(R.drawable.ic_color_picker_tab_rgba)))
				.perform(click());
		onView(withId(R.id.color_picker_rgb_red_value))
				.check(matches(withText(containsString(EXPECTED_RED_VALUE))));
		onView(withId(R.id.color_picker_rgb_green_value))
				.check(matches(withText(containsString(EXPECTED_GREEN_VALUE))));
		onView(withId(R.id.color_picker_rgb_blue_value))
				.check(matches(withText(containsString(EXPECTED_BLAU_VALUE))));
		onView(withId(R.id.color_picker_rgb_alpha_value))
				.check(matches(withText(containsString(EXPECTED_ALFA_VALUE))));
	}
}
