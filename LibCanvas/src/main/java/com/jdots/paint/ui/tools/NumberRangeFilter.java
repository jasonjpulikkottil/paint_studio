package com.jdots.paint.ui.tools;

import android.text.InputFilter;
import android.text.Spanned;

public interface NumberRangeFilter extends InputFilter {
	int getMax();

	void setMax(int max);

	@Override
	CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend);
}
