package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;

import static com.jdots.paint.test.espresso.util.UiInteractions.setProgress;
import static org.hamcrest.Matchers.not;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class TransformToolOptionsViewInteraction extends CustomViewInteraction {
	private TransformToolOptionsViewInteraction() {
		super(onView(withId(R.id.paint_studio_main_tool_options)));
	}

	public static TransformToolOptionsViewInteraction onTransformToolOptionsView() {
		return new TransformToolOptionsViewInteraction();
	}

	public TransformToolOptionsViewInteraction performAutoCrop() {
		onView(withId(R.id.paint_studio_transform_auto_crop_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction checkAutoDisplayed() {
		onView(withText(R.string.transform_auto_crop_text))
				.check(matches(isDisplayed()));
		return this;
	}

	public TransformToolOptionsViewInteraction performRotateClockwise() {
		onView(withId(R.id.paint_studio_transform_rotate_right_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction performRotateCounterClockwise() {
		onView(withId(R.id.paint_studio_transform_rotate_left_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction performFlipVertical() {
		onView(withId(R.id.paint_studio_transform_flip_vertical_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction performFlipHorizontal() {
		onView(withId(R.id.paint_studio_transform_flip_horizontal_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction moveSliderTo(int moveTo) {
		onView(withId(R.id.paint_studio_transform_resize_seekbar))
				.perform(setProgress(moveTo));
		return this;
	}

	public TransformToolOptionsViewInteraction performApplyResize() {
		onView(withId(R.id.paint_studio_transform_apply_resize_btn))
				.perform(click());
		return this;
	}

	public TransformToolOptionsViewInteraction checkPercentageTextMatches(int expected) {
		onView(withId(R.id.paint_studio_transform_resize_percentage_text))
				.check(matches(withText(Integer.toString(expected))));
		return this;
	}

	public TransformToolOptionsViewInteraction checkIsDisplayed() {
		onView(withId(R.id.paint_studio_main_tool_options))
				.check(matches(isDisplayed()));
		return this;
	}

	public TransformToolOptionsViewInteraction checkIsNotDisplayed() {
		onView(withId(R.id.paint_studio_main_tool_options))
				.check(matches(not(isDisplayed())));
		return this;
	}
}
