package com.jdots.paint.test.espresso.dialog;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.dialog.IndeterminateProgressDialog;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static com.jdots.paint.test.espresso.util.UiInteractions.touchAt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IndeterminateProgressDialogIntegrationTest {
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	private AlertDialog dialog;

	@UiThreadTest
	@Before
	public void setUp() {
		dialog = IndeterminateProgressDialog.newInstance(activityTestRule.getActivity());
		dialog.show();
	}

	@UiThreadTest
	@After
	public void tearDown() {
		dialog.dismiss();
	}

	@Test
	public void testDialogIsShown() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			onView(withId(R.id.paint_studio_progress_bar))
					.check(matches(isDisplayed()));
		}
	}

	@RequiresApi(Build.VERSION_CODES.N)
	@Test
	public void testDialogIsNotCancelableOnBack() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			pressBack();

			onView(withId(R.id.paint_studio_progress_bar))
					.check(matches(isDisplayed()));
		}
	}

	@RequiresApi(Build.VERSION_CODES.N)
	@Test
	public void testDialogIsNotCancelable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
			PointF point = new PointF((float) -metrics.widthPixels / 4, (float) -metrics.heightPixels / 4);

			onView(withId(R.id.paint_studio_progress_bar))
					.perform(touchAt(point))
					.check(matches(isDisplayed()));
		}
	}
}
