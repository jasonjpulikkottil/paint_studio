package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;
import com.jdots.paint.tools.ToolType;

import androidx.test.espresso.ViewInteraction;

import static com.jdots.paint.test.espresso.util.EspressoUtils.getMainActivity;
import static com.jdots.paint.test.espresso.util.wrappers.BottomNavigationViewInteraction.onBottomNavigationView;
import static org.hamcrest.Matchers.not;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class ToolBarViewInteraction extends CustomViewInteraction {

	private ToolBarViewInteraction() {
		super(onView(withId(R.id.paint_studio_toolbar)));
	}

	public static ToolBarViewInteraction onToolBarView() {
		return new ToolBarViewInteraction();
	}

	public ViewInteraction onSelectedToolButton() {
		return onView(withId(getCurrentToolType().getToolButtonID()));
	}

	public ViewInteraction onToolOptionsView() {
		return onView(withId(R.id.paint_studio_layout_tool_specific_options));
	}

	public ToolBarViewInteraction performClickSelectedToolButton() {
		onBottomNavigationView()
				.onToolsClicked();
		onSelectedToolButton()
				.perform(click());
		return this;
	}

	public ToolBarViewInteraction onToolsClicked() {
		onBottomNavigationView()
				.onToolsClicked();
		return this;
	}

	public ToolBarViewInteraction performSelectTool(ToolType toolType) {
		if (getCurrentToolType() != toolType) {
			onBottomNavigationView()
					.onToolsClicked();
			onView(withId(toolType.getToolButtonID()))
					.perform(click());
		}
		return this;
	}

	private ToolType getCurrentToolType() {
		return getMainActivity().toolReference.get().getToolType();
	}

	public ToolBarViewInteraction performOpenToolOptionsView() {
		onToolOptionsView()
				.check(matches(not(isDisplayed())));
		onBottomNavigationView()
				.onCurrentClicked();
		return this;
	}

	public ToolBarViewInteraction performCloseToolOptionsView() {
		onToolOptionsView()
				.check(matches(isDisplayed()));
		onBottomNavigationView()
				.onCurrentClicked();
		return this;
	}
}
