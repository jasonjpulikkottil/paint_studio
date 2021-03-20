package com.jdots.paint.tools.helper;

import android.text.Spanned;
import android.util.Log;

import com.jdots.paint.ui.tools.NumberRangeFilter;

public class DefaultNumberRangeFilter implements NumberRangeFilter {
	private static final String TAG = DefaultNumberRangeFilter.class.getSimpleName();
	private int min;

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public void setMax(int max) {
		this.max = max;
	}

	private int max;

	public DefaultNumberRangeFilter(int minVal, int maxVal) {
		this.min = minVal;
		this.max = maxVal;
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		try {
			int input = Integer.parseInt(dest.toString() + source.toString());
			if (input <= max && input >= min) {
				return null;
			}
		} catch (NumberFormatException nfe) {
			Log.d(TAG, nfe.getLocalizedMessage());
		}
		return "";
	}
}
