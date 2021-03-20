package com.jdots.paint.test.espresso.util.wrappers;

import android.graphics.Bitmap;
import android.view.View;

import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.contract.LayerContracts;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import static com.jdots.paint.test.espresso.util.MainActivityHelper.getMainActivityFromView;
import static org.hamcrest.Matchers.is;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class DrawingSurfaceInteraction extends CustomViewInteraction {

	private DrawingSurfaceInteraction() {
		super(onView(withId(R.id.paint_studio_drawing_surface_view)));
	}

	public static DrawingSurfaceInteraction onDrawingSurfaceView() {
		return new DrawingSurfaceInteraction();
	}

	public DrawingSurfaceInteraction checkPixelColor(@ColorInt final int expectedColor, final CoordinatesProvider coordinateProvider) {
		check(matches(new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Color at coordinates is " + Integer.toHexString(expectedColor));
			}

			@Override
			protected boolean matchesSafely(View view) {
				MainActivity activity = getMainActivityFromView(view);
				LayerContracts.Layer currentLayer = activity.layerModel.getCurrentLayer();
				float[] coordinates = coordinateProvider.calculateCoordinates(view);
				int actualColor = currentLayer.getBitmap().getPixel((int) coordinates[0], (int) coordinates[1]);
				return expectedColor == actualColor;
			}
		}));
		return this;
	}

	public DrawingSurfaceInteraction checkPixelColor(@ColorInt final int expectedColor, final float x, final float y) {
		check(matches(new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Color at coordinates is " + Integer.toHexString(expectedColor));
			}

			@Override
			protected boolean matchesSafely(View view) {
				MainActivity activity = getMainActivityFromView(view);
				LayerContracts.Layer currentLayer = activity.layerModel.getCurrentLayer();
				int actualColor = currentLayer.getBitmap().getPixel((int) x, (int) y);
				return expectedColor == actualColor;
			}
		}));
		return this;
	}

	public DrawingSurfaceInteraction checkPixelColorResource(@ColorRes int expectedColorRes, CoordinatesProvider coordinateProvider) {
		int expectedColor = ContextCompat.getColor(InstrumentationRegistry.getInstrumentation().getTargetContext(), expectedColorRes);
		return checkPixelColor(expectedColor, coordinateProvider);
	}

	public DrawingSurfaceInteraction checkBitmapDimension(final int expectedWidth, final int expectedHeight) {
		check(matches(new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Bitmap has is size "
						+ expectedWidth + "x and "
						+ expectedHeight + "y");
			}

			@Override
			protected boolean matchesSafely(View view) {
				MainActivity activity = getMainActivityFromView(view);
				LayerContracts.Model layerModel = activity.layerModel;
				Bitmap bitmap = layerModel.getCurrentLayer().getBitmap();
				return expectedWidth == bitmap.getWidth() && expectedHeight == bitmap.getHeight();
			}
		}));
		return this;
	}

	public DrawingSurfaceInteraction checkLayerDimensions(int expectedWidth, int expectedHeight) {
		checkThatLayerDimensions(is(expectedWidth), is(expectedHeight));
		return this;
	}

	public DrawingSurfaceInteraction checkThatLayerDimensions(final Matcher<Integer> matchesWidth, final Matcher<Integer> matchesHeight) {
		check(matches(new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("All layers have expected size");
			}

			@Override
			protected boolean matchesSafely(View view) {
				MainActivity activity = getMainActivityFromView(view);
				LayerContracts.Model layerModel = activity.layerModel;
				for (LayerContracts.Layer layer : layerModel.getLayers()) {
					Bitmap bitmap = layer.getBitmap();
					if (!matchesWidth.matches(bitmap.getWidth()) || !matchesHeight.matches(bitmap.getHeight())) {
						return false;
					}
				}
				return true;
			}
		}));

		return this;
	}
}
