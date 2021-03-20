package com.jdots.paint.tools.implementation;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.jdots.paint.R;
import com.jdots.paint.tools.ContextCallback;
import com.jdots.paint.tools.common.Constants;
import com.jdots.paint.ui.ToastFactory;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class DefaultContextCallback implements ContextCallback {
	private Shader checkeredBitmapShader;
	private Context context;

	public DefaultContextCallback(Context context) {
		this.context = context;

		Resources resources = context.getResources();
		Bitmap checkerboard = BitmapFactory.decodeResource(resources, R.drawable.paint_studio_checkeredbg);
		checkeredBitmapShader = new BitmapShader(checkerboard, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
	}

	@Override
	public void showNotification(@StringRes int resId) {
		showNotification(resId, NotificationDuration.SHORT);
	}

	@Override
	public void showNotification(@StringRes int resId, NotificationDuration duration) {
		int toastDuration = duration == NotificationDuration.SHORT ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
		ToastFactory.makeText(context, resId, toastDuration).show();
	}

	@Override
	public int getScrollTolerance() {
		return (int) (context.getResources().getDisplayMetrics().widthPixels
				* Constants.SCROLL_TOLERANCE_PERCENTAGE);
	}

	@Override
	public ScreenOrientation getOrientation() {
		int orientation = context.getResources().getConfiguration().orientation;
		return orientation == Configuration.ORIENTATION_LANDSCAPE ? ScreenOrientation.LANDSCAPE : ScreenOrientation.PORTRAIT;
	}

	@Override
	public Typeface getFont(@FontRes int id) {
		return ResourcesCompat.getFont(context, id);
	}

	@Override
	public DisplayMetrics getDisplayMetrics() {
		return context.getResources().getDisplayMetrics();
	}

	@ColorInt
	@Override
	public int getColor(@ColorRes int id) {
		return ContextCompat.getColor(context, id);
	}

	@Override
	public Shader getCheckeredBitmapShader() {
		return checkeredBitmapShader;
	}

	@Override
	public Drawable getDrawable(@DrawableRes int resource) {
		return AppCompatResources.getDrawable(context, resource);
	}
}
