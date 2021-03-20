package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;
import com.jdots.paint.tools.ToolType;

import androidx.test.espresso.ViewInteraction;

import static com.jdots.paint.test.espresso.util.UiMatcher.withDrawable;
import static org.hamcrest.Matchers.allOf;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public final class BottomNavigationViewInteraction extends CustomViewInteraction {
	private BottomNavigationViewInteraction() {
		super(onView(withId(R.id.paint_studio_bottom_navigation)));
	}

	public static BottomNavigationViewInteraction onBottomNavigationView() {
		return new BottomNavigationViewInteraction();
	}

	public ViewInteraction onToolsClicked() {
		return onView(allOf(withId(R.id.icon), isDescendantOfA(withId(R.id.action_tools))))
				.perform(click());
	}

	public ViewInteraction onCurrentClicked() {
		return onView(allOf(withId(R.id.icon), isDescendantOfA(withId(R.id.action_current_tool))))
				.perform(click());
	}

	public ViewInteraction checkShowsCurrentTool(ToolType toolType) {
		onView(allOf(withId(R.id.icon), isDescendantOfA(withId(R.id.action_current_tool))))
				.check(matches(withDrawable(toolType.getDrawableResource())));

		return onView(withId(R.id.action_current_tool))
				.check(matches(hasDescendant(withText(toolType.getNameResource()))));
	}

	public ViewInteraction onColorClicked() {
		return onView(allOf(withId(R.id.icon), isDescendantOfA(withId(R.id.action_color_picker))))
				.perform(click());
	}

	public ViewInteraction onLayersClicked() {
		return onView(allOf(withId(R.id.icon), isDescendantOfA(withId(R.id.action_layers))))
				.perform(click());
	}
}
