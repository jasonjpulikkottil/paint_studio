package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class BrushPickerViewInteraction extends CustomViewInteraction {
	private BrushPickerViewInteraction() {
		super(onView(withId(R.id.paint_studio_main_tool_options)));
	}

	public static BrushPickerViewInteraction onBrushPickerView() {
		return new BrushPickerViewInteraction();
	}

	public ViewInteraction onStrokeWidthSeekBar() {
		return onView(withId(R.id.paint_studio_stroke_width_seek_bar));
	}

	public ViewInteraction onStrokeWidthTextView() {
		return onView(withId(R.id.paint_studio_stroke_width_width_text));
	}

	public ViewInteraction onStrokeCapSquareView() {
		return onView(withId(R.id.paint_studio_stroke_ibtn_rect));
	}

	public ViewInteraction onStrokeCapRoundView() {
		return onView(withId(R.id.paint_studio_stroke_ibtn_circle));
	}
}
