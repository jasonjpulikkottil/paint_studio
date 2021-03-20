package com.jdots.paint.test.espresso.util.wrappers;

import org.hamcrest.Matcher;

import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.Root;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

public abstract class CustomViewInteraction {
	protected ViewInteraction viewInteraction;

	protected CustomViewInteraction(ViewInteraction viewInteraction) {
		this.viewInteraction = viewInteraction;
	}

	public final ViewInteraction perform(final ViewAction... viewActions) {
		return viewInteraction.perform(viewActions);
	}

	public final ViewInteraction withFailureHandler(FailureHandler var1) {
		return viewInteraction.withFailureHandler(var1);
	}

	public final ViewInteraction inRoot(Matcher<Root> var1) {
		return viewInteraction.inRoot(var1);
	}

	public final ViewInteraction noActivity() {
		return viewInteraction.noActivity();
	}

	public final ViewInteraction check(final ViewAssertion viewAssert) {
		return viewInteraction.check(viewAssert);
	}
}
