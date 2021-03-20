package com.jdots.paint.test.espresso.util.wrappers;

import android.view.Gravity;
import android.view.View;

import com.jdots.paint.R;
import com.jdots.paint.model.Layer;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;

import static com.jdots.paint.test.espresso.util.UiInteractions.assertListViewCount;
import static com.jdots.paint.test.espresso.util.wrappers.BottomNavigationViewInteraction.onBottomNavigationView;
import static org.hamcrest.Matchers.instanceOf;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class LayerMenuViewInteraction extends CustomViewInteraction {
	private LayerMenuViewInteraction() {
		super(onView(withId(R.id.paint_studio_nav_view_layer)));
	}

	public static LayerMenuViewInteraction onLayerMenuView() {
		return new LayerMenuViewInteraction();
	}

	public ViewInteraction onButtonAdd() {
		return onView(withId(R.id.paint_studio_layer_side_nav_button_add));
	}

	public ViewInteraction onButtonDelete() {
		return onView(withId(R.id.paint_studio_layer_side_nav_button_delete));
	}

	public ViewInteraction onLayerList() {
		return onView(withId(R.id.paint_studio_layer_side_nav_list));
	}

	public LayerMenuViewInteraction checkLayerCount(int count) {
		onLayerList()
				.check(assertListViewCount(count));
		return this;
	}

	public DataInteraction onLayerAt(int listPosition) {
		return onData(instanceOf(Layer.class))
				.inAdapterView(withId(R.id.paint_studio_layer_side_nav_list))
				.atPosition(listPosition);
	}

	public LayerMenuViewInteraction performOpen() {
		onBottomNavigationView()
				.onLayersClicked();
		check(matches(isDisplayed()));
		return this;
	}

	public LayerMenuViewInteraction performClose() {
		check(matches(isDisplayed()));
		onView(withId(R.id.paint_studio_drawer_layout))
			.perform(DrawerActions.close(Gravity.END));
		return this;
	}

	public LayerMenuViewInteraction performSelectLayer(int listPosition) {
		check(matches(isDisplayed()));
		onLayerAt(listPosition)
				.perform(click());
		return this;
	}

	public LayerMenuViewInteraction performLongClickLayer(int listPosition) {
		check(matches(isDisplayed()));
		onLayerAt(listPosition)
				.perform(longClick());
		return this;
	}

	public LayerMenuViewInteraction performAddLayer() {
		check(matches(isDisplayed()));
		onButtonAdd()
				.perform(click());
		return this;
	}

	public LayerMenuViewInteraction performDeleteLayer() {
		check(matches(isDisplayed()));
		onButtonDelete()
				.perform(click());
		return this;
	}

	public LayerMenuViewInteraction perfomToggleLayerVisibility(int position) {
		check(matches(isDisplayed()));
		onView(withIndex(withId(R.id.paint_studio_checkbox_layer), position)).perform(click());
		return this;
	}

	public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
		return new TypeSafeMatcher<View>() {
			int currentIndex = 0;

			@Override
			public void describeTo(Description description) {
				description.appendText("with index: ");
				description.appendValue(index);
				matcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				return matcher.matches(view) && currentIndex++ == index;
			}
		};
	}
}
