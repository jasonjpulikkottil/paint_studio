package com.jdots.paint.tools;

import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.StringRes;

public interface ContextCallback {
	void showNotification(@StringRes int resId);

	void showNotification(@StringRes int resId, NotificationDuration duration);

	int getScrollTolerance();

	ScreenOrientation getOrientation();

	Typeface getFont(@FontRes int id);

	DisplayMetrics getDisplayMetrics();

	@ColorInt
	int getColor(@ColorRes int id);

	Shader getCheckeredBitmapShader();

	Drawable getDrawable(@DrawableRes int resource);

	enum ScreenOrientation {
		PORTRAIT,
		LANDSCAPE
	}

	enum NotificationDuration {
		SHORT,
		LONG
	}
}
