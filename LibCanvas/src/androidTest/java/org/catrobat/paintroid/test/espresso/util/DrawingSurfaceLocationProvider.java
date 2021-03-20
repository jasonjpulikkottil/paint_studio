package com.jdots.paint.test.espresso.util;

import android.graphics.PointF;
import android.view.View;

import com.jdots.paint.MainActivity;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.tools.implementation.BaseToolWithShape;

import androidx.test.espresso.action.CoordinatesProvider;

import static com.jdots.paint.test.espresso.util.MainActivityHelper.getMainActivityFromView;
import static com.jdots.paint.test.espresso.util.PositionCoordinatesProvider.calculateViewOffset;

public enum DrawingSurfaceLocationProvider implements CoordinatesProvider {
	MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .5f);
		}
	},
	LEFT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .0f, .5f);
		}
	},
	HALFWAY_LEFT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .25f, .5f);
		}
	},
	RIGHT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, 1f, .5f);
		}
	},
	HALFWAY_RIGHT_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .75f, .5f);
		}
	},
	TOP_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, 0f);
		}
	},
	HALFWAY_TOP_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .25f);
		}
	},
	HALFWAY_BOTTOM_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, .75f);
		}
	},
	HALFWAY_TOP_LEFT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .25f, .25f);
		}
	},
	HALFWAY_TOP_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .75f, .25f);
		}
	},
	HALFWAY_BOTTOM_LEFT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .25f, .75f);
		}
	},
	HALFWAY_BOTTOM_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .75f, .75f);
		}
	},
	BOTTOM_MIDDLE {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, 1f);
		}
	},
	OUTSIDE_MIDDLE_RIGHT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, 1.5f, .5f);
		}
	},
	OUTSIDE_MIDDLE_LEFT {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, -.3f, .5f);
		}
	},
	OUTSIDE_MIDDLE_BOTTOM {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, 1.3f, .5f);
		}
	},
	OUTSIDE_MIDDLE_TOP {
		@Override
		public float[] calculateCoordinates(View view) {
			return calculatePercentageOffset(view, .5f, -.3f);
		}
	},
	TOOL_POSITION {
		@Override
		public float[] calculateCoordinates(View view) {
			MainActivity mainActivity = getMainActivityFromView(view);
			Workspace workspace = mainActivity.workspace;
			PointF toolPosition = ((BaseToolWithShape) mainActivity.toolReference.get()).toolPosition;
			PointF point = workspace.getSurfacePointFromCanvasPoint(toolPosition);
			return calculateViewOffset(view, point.x, point.y);
		}
	};

	private static float[] calculatePercentageOffset(View view, float percentageX, float percentageY) {
		MainActivity mainActivity = getMainActivityFromView(view);
		Workspace workspace = mainActivity.workspace;
		float pointX = workspace.getWidth() * percentageX;
		float pointY = workspace.getHeight() * percentageY;
		PointF point = workspace.getSurfacePointFromCanvasPoint(new PointF(pointX, pointY));
		return calculateViewOffset(view, point.x, point.y);
	}
}
