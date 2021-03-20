package com.jdots.paint.ui;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public final class ToastFactory {
	private static Toast currentToast;

	private ToastFactory() {
	}

	public static Toast makeText(Context context, @StringRes int resId, int duration) {
		if (currentToast != null) {
			currentToast.cancel();
		}

		currentToast = Toast.makeText(context, resId, duration);
		return currentToast;
	}
}
