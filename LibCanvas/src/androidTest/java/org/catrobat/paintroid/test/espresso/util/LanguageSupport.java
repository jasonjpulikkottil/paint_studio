package com.jdots.paint.test.espresso.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public final class LanguageSupport {
	private LanguageSupport() {
		throw new IllegalArgumentException();
	}

	public static void setLocale(Context context, Locale locale) {
		if (Build.VERSION.SDK_INT >= 24) {
			Locale.setDefault(Locale.Category.DISPLAY, locale);
		} else {
			Locale.setDefault(locale);
		}

		Resources resources = context.getResources();
		Configuration conf = resources.getConfiguration();
		conf.setLocale(locale);
		resources.updateConfiguration(conf, resources.getDisplayMetrics());
	}
}
