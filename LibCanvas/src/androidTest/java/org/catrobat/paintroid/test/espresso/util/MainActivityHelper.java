package com.jdots.paint.test.espresso.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.view.View;

import com.jdots.paint.MainActivity;

public class MainActivityHelper {
	private final MainActivity activity;

	public MainActivityHelper(MainActivity activity) {
		this.activity = activity;
	}

	public Point getDisplaySize() {
		Point displaySize = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
		return displaySize;
	}

	public int getDisplayWidth() {
		return getDisplaySize().x;
	}

	public int getDisplayHeight() {
		return getDisplaySize().y;
	}

	public int getScreenOrientation() {
		return activity.getRequestedOrientation();
	}

	public void setScreenOrientation(int orientation) {
		activity.setRequestedOrientation(orientation);
	}

	public static MainActivity getMainActivityFromView(View view) {
		Context context = view.getContext();
		while (context instanceof ContextWrapper) {
			if (context instanceof MainActivity) {
				return (MainActivity) context;
			}
			context = ((ContextWrapper) context).getBaseContext();
		}
		throw new RuntimeException("View context does not implement MainActivity");
	}
}
