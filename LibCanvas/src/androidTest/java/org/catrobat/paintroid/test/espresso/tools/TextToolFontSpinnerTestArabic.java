package com.jdots.paint.test.espresso.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.test.espresso.rtl.util.RtlActivityTestRule;
import com.jdots.paint.tools.ToolType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import androidx.core.content.res.ResourcesCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.rtl.util.RtlUiTestUtils.checkTextDirection;
import static com.jdots.paint.test.espresso.util.EspressoUtils.getConfiguration;
import static com.jdots.paint.test.espresso.util.UiMatcher.hasTypeFace;
import static com.jdots.paint.test.espresso.util.UiMatcher.withIndex;
import static com.jdots.paint.test.espresso.util.wrappers.ToolBarViewInteraction.onToolBarView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class TextToolFontSpinnerTestArabic {
	private int normalStyle = Typeface.NORMAL;
	private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
	private Typeface sansSerifFontFace = Typeface.create(Typeface.SANS_SERIF, normalStyle);
	private Typeface serifFontFace = Typeface.create(Typeface.SERIF, normalStyle);
	private Typeface monospaceFontFace = Typeface.create(Typeface.MONOSPACE, normalStyle);
	private Typeface stcFontFace = ResourcesCompat.getFont(context, R.font.stc_regular);
	private Typeface dubaiFontFace = ResourcesCompat.getFont(context, R.font.dubai);

	@Rule
	public ActivityTestRule<MainActivity> launchActivityRule = new RtlActivityTestRule<>(MainActivity.class, "ar");

	@Test
	public void testTextFontFaceOfFontSpinnerArabic() {
		assertEquals(View.LAYOUT_DIRECTION_RTL, getConfiguration().getLayoutDirection());
		assertTrue(checkTextDirection(Locale.getDefault().getDisplayName()));

		onToolBarView()
				.performSelectTool(ToolType.TEXT);
		onView(withId(R.id.paint_studio_text_tool_dialog_spinner_font))
				.perform(click());
		onView(withIndex(withId(android.R.id.text1), 0))
				.check(matches(hasTypeFace(sansSerifFontFace)));

		onView(withIndex(withId(android.R.id.text1), 1))
				.check(matches(hasTypeFace(monospaceFontFace)));

		onView(withIndex(withId(android.R.id.text1), 2))
				.check(matches(hasTypeFace(serifFontFace)));

		onView(withIndex(withId(android.R.id.text1), 3))
				.check(matches(hasTypeFace(dubaiFontFace)));

		onView(withIndex(withId(android.R.id.text1), 4))
				.check(matches(hasTypeFace(stcFontFace)));
	}
}
