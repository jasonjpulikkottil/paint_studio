package com.jdots.paint.test.espresso.util.wrappers;

import android.widget.TableLayout;
import android.widget.TableRow;

import com.jdots.paint.R;
import com.jdots.paint.PresetSelectorView;

import static com.jdots.paint.test.espresso.util.UiMatcher.hasTablePosition;
import static com.jdots.paint.test.espresso.util.UiMatcher.withBackgroundColor;
import static com.jdots.paint.test.espresso.util.wrappers.BottomNavigationViewInteraction.onBottomNavigationView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class ColorPickerViewInteraction extends CustomViewInteraction {
	private static final int COLOR_PICKER_BUTTONS_PER_ROW = 4;

	protected ColorPickerViewInteraction() {
		super(onView(withId(R.id.color_picker_view)));
	}

	public static ColorPickerViewInteraction onColorPickerView() {
		return new ColorPickerViewInteraction();
	}

	public ViewInteraction onPositiveButton() {
		return onView(withId(android.R.id.button1))
				// to avoid following exception when running on emulator:
				// Caused by: java.lang.SecurityException:
				// Injecting to another application requires INJECT_EVENTS permission
				.perform(closeSoftKeyboard());
	}

	public ColorPickerViewInteraction performOpenColorPicker() {
		onBottomNavigationView()
				.onColorClicked();
		return this;
	}

	public ViewInteraction onNegativeButton() {
		return onView(withId(android.R.id.button2))
				// to avoid following exception when running on emulator:
				// Caused by: java.lang.SecurityException:
				// Injecting to another application requires INJECT_EVENTS permission
				.perform(closeSoftKeyboard());
	}

	public void checkCurrentViewColor(int color) {
		onView(withId(R.id.color_picker_current_color_view))
				.check(matches(withBackgroundColor(color)));
	}

	public void checkNewColorViewColor(int color) {
		onView(withId(R.id.color_picker_new_color_view))
				.check(matches(withBackgroundColor(color)));
	}

	public ColorPickerViewInteraction performCloseColorPickerWithDialogButton() {
		check(matches(isDisplayed()));
		onPositiveButton()
				.perform(click());
		return this;
	}

	public ColorPickerViewInteraction performClickColorPickerPresetSelectorButton(int buttonPosition) {
		final int colorButtonRowPosition = (buttonPosition / COLOR_PICKER_BUTTONS_PER_ROW);
		final int colorButtonColPosition = buttonPosition % COLOR_PICKER_BUTTONS_PER_ROW;

		onView(allOf(isDescendantOfA(withClassName(containsString(PresetSelectorView.class.getSimpleName()))),
				isDescendantOfA(isAssignableFrom(TableLayout.class)),
				isDescendantOfA(isAssignableFrom(TableRow.class)),
				hasTablePosition(colorButtonRowPosition, colorButtonColPosition)))
				.perform(closeSoftKeyboard())
				.perform(scrollTo())
				.perform(click());
		return this;
	}
}
