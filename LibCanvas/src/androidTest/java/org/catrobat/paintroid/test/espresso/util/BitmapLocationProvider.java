/*

 */package com.jdots.paint.test.espresso.util;

import android.view.View;

import com.jdots.paint.MainActivity;
import com.jdots.paint.tools.Workspace;

import androidx.test.espresso.action.CoordinatesProvider;

import static com.jdots.paint.test.espresso.util.MainActivityHelper.getMainActivityFromView;

public enum BitmapLocationProvider implements CoordinatesProvider{
	MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .5f);
		}
	},
	MIDDLE_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, 1f, .5f);
		}
	},
	OUTSIDE_MIDDLE_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, 1.5f, .5f);
		}
	},
	HALFWAY_RIGHT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .75f, .5f);
		}
	},
	HALFWAY_LEFT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .25f, .5f);
		}
	},
	HALFWAY_BOTTOM_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .75f);
		}
	},
	HALFWAY_TOP_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .25f);
		}
	},
	HALFWAY_TOP_LEFT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .25f, .25f);
		}
	},
	HALFWAY_BOTTOM_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .75f, .75f);
		}
	};

	private static float[] calculatePercentageOffset(View view, float percentageX, float percentageY) {
		MainActivity mainActivity = getMainActivityFromView(view);
		Workspace workspace = mainActivity.workspace;
		float pointX = (workspace.getWidth() - 1) * percentageX;
		float pointY = workspace.getHeight() * percentageY;
		return new float[] {pointX, pointY};
	}
}
