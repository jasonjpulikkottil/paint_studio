/*

 */package com.jdots.paint.test.espresso.util.wrappers;

import android.widget.Button;

import com.jdots.paint.R;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

import static org.hamcrest.Matchers.allOf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class ConfirmQuitDialogInteraction extends CustomViewInteraction {
	private ConfirmQuitDialogInteraction() {
		super(onView(withText(R.string.closing_security_question)).inRoot(isDialog()));
	}

	public static ConfirmQuitDialogInteraction onConfirmQuitDialog() {
		return new ConfirmQuitDialogInteraction();
	}

	public ViewInteraction onPositiveButton() {
		return onView(allOf(withId(android.R.id.button1), withText(R.string.save_button_text), isAssignableFrom(Button.class)));
	}

	public ConfirmQuitDialogInteraction checkPositiveButton(ViewAssertion matcher) {
		onPositiveButton()
				.check(matcher);
		return this;
	}

	public ViewInteraction onNegativeButton() {
		return onView(allOf(withId(android.R.id.button2), withText(R.string.discard_button_text), isAssignableFrom(Button.class)));
	}

	public ConfirmQuitDialogInteraction checkNegativeButton(ViewAssertion matcher) {
		onNegativeButton()
				.check(matcher);
		return this;
	}

	public ConfirmQuitDialogInteraction checkNeutralButton(ViewAssertion matcher) {
		onView(withId(android.R.id.button3))
				.check(matcher);
		return this;
	}

	public ConfirmQuitDialogInteraction checkMessage(ViewAssertion matcher) {
		onView(withText(R.string.closing_security_question))
				.check(matcher);
		return this;
	}

	public ConfirmQuitDialogInteraction checkTitle(ViewAssertion matcher) {
		onView(withText(R.string.closing_security_question_title))
				.check(matcher);
		return this;
	}
}
