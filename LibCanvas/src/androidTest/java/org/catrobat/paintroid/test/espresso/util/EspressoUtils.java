package com.jdots.paint.test.espresso.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.jdots.paint.MainActivity;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static com.jdots.paint.test.espresso.util.UiMatcher.isToast;
import static org.junit.Assert.assertNotEquals;

public final class EspressoUtils {

	public static final int DEFAULT_STROKE_WIDTH = 25;

	public static MainActivity getMainActivity() {
		final List<AppCompatActivity> resumedActivities = new ArrayList<>();
		InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				Collection<Activity> activitiesInStage = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
				resumedActivities.addAll(activitiesInStage);
			}
		});
		return (MainActivity) resumedActivities.get(0);
	}

	private EspressoUtils() {
	}

	/**
	 * @deprecated Do not use this in new tests
	 */
	@Deprecated
	public static float getActionbarHeight() {
		Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
		TypedValue tv = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return TypedValue.complexToDimensionPixelSize(tv.data, metrics);
	}

	/**
	 * @deprecated Do not use this in new tests
	 */
	@Deprecated
	public static float getStatusbarHeight() {
		Resources resources = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		assertNotEquals(0, resourceId);
		return resources.getDimensionPixelSize(resourceId);
	}

	/**
	 * @deprecated Do not use this in new tests
	 */
	@Deprecated
	public static PointF getSurfacePointFromScreenPoint(PointF screenPoint) {
		return new PointF(screenPoint.x, screenPoint.y - getActionbarHeight() - getStatusbarHeight());
	}

	/**
	 * @deprecated Do not use this in new tests
	 */
	@Deprecated
	public static PointF getScreenPointFromSurfaceCoordinates(float pointX, float pointY) {
		return new PointF(pointX, pointY + getStatusbarHeight() + getActionbarHeight());
	}

	public static void waitForToast(Matcher<View> viewMatcher, int duration) {
		final long waitTime = System.currentTimeMillis() + duration;
		final ViewInteraction viewInteraction = onView(viewMatcher).inRoot(isToast());

		while (System.currentTimeMillis() < waitTime) {
			try {
				viewInteraction.check(matches(isDisplayed()));
				return;
			} catch (NoMatchingViewException e) {
				waitMillis(250);
			}
		}

		viewInteraction.check(matches(isDisplayed()));
	}

	public static void waitMillis(final long millis) {
		onView(isRoot()).perform(UiInteractions.waitFor(millis));
	}

	public static Configuration getConfiguration() {
		return InstrumentationRegistry.getInstrumentation().getTargetContext().getResources().getConfiguration();
	}

	public static GrantPermissionRule grantPermissionRulesVersionCheck() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			return GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
		} else {
			return GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE);
		}
	}
}
