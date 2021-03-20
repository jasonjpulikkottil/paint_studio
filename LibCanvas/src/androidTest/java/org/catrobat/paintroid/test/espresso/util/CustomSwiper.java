package com.jdots.paint.test.espresso.util;

import android.os.SystemClock;
import android.view.MotionEvent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.action.MotionEvents;
import androidx.test.espresso.action.Swiper;

public enum CustomSwiper implements Swiper {
	ACCURATE {
		@Override
		public Status sendSwipe(UiController uiController, float[] startCoordinates, float[] endCoordinates, float[] precision) {
			final float[][] steps = interpolate(startCoordinates, endCoordinates, STEPS);
			final int delayBetweenMovements = DURATION / steps.length;

			MotionEvent downEvent = MotionEvents.sendDown(uiController, startCoordinates, precision).down;
			try {
				for (int i = 0; i < steps.length; i++) {
					MotionEvents.sendMovement(uiController, downEvent, steps[i]);
					long desiredTime = downEvent.getDownTime() + delayBetweenMovements * i;
					long timeUntilDesired = desiredTime - SystemClock.uptimeMillis();
					if (timeUntilDesired > 10) {
						uiController.loopMainThreadForAtLeast(timeUntilDesired);
					}
				}
				MotionEvents.sendUp(uiController, downEvent, endCoordinates);
			} finally {
				downEvent.recycle();
			}
			return Status.SUCCESS;
		}
	};

	private static final int STEPS = 10;
	private static final int DURATION = 500;

	private static float[][] interpolate(float[] start, float[] end, int steps) {
		float[][] res = new float[steps][2];

		res[0][0] = start[0];
		res[0][1] = start[1];

		for (int i = 2; i < steps; i++) {
			res[i - 1][0] = start[0] + (end[0] - start[0]) * i / (steps + 2f);
			res[i - 1][1] = start[1] + (end[1] - start[1]) * i / (steps + 2f);
		}

		res[steps - 1][0] = end[0];
		res[steps - 1][1] = end[1];

		return res;
	}
}
