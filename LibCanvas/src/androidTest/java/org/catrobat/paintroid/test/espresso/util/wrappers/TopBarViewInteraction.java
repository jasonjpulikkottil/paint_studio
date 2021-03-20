package com.jdots.paint.test.espresso.util.wrappers;

import com.jdots.paint.R;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class TopBarViewInteraction extends CustomViewInteraction {
	private TopBarViewInteraction() {
		super(onView(withId(R.id.paint_studio_layout_top_bar)));
	}

	public static TopBarViewInteraction onTopBarView() {
		return new TopBarViewInteraction();
	}

	public ViewInteraction onUndoButton() {
		return onView(withId(R.id.paint_studio_btn_top_undo));
	}

	public ViewInteraction onRedoButton() {
		return onView(withId(R.id.paint_studio_btn_top_redo));
	}

	public ViewInteraction onCheckmarkButton() {
		return onView(withId(R.id.paint_studio_btn_top_checkmark));
	}

	public TopBarViewInteraction performUndo() {
		onUndoButton()
				.perform(click());
		return this;
	}

	public TopBarViewInteraction performRedo() {
		onRedoButton()
				.perform(click());
		return this;
	}

	public TopBarViewInteraction performClickCheckmark() {
		onCheckmarkButton()
				.perform(click());
		return this;
	}

	public TopBarViewInteraction performOpenMoreOptions() {
		openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
		return this;
	}

	public TopBarViewInteraction onHomeClicked() {
		Espresso.pressBack();
		return this;
	}
}
