package com.jdots.paint.test.espresso.rtl.util;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.jdots.paint.test.espresso.util.LanguageSupport;

import java.util.Locale;

public class RtlActivityTestRule<T extends AppCompatActivity> extends ActivityTestRule<T> {
	private final String language;

	public RtlActivityTestRule(Class<T> activityClass, String language) {
		super(activityClass);
		this.language = language;
	}

	@Override
	protected void beforeActivityLaunched() {
		super.beforeActivityLaunched();

		Locale locale = new Locale(language);
		Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		LanguageSupport.setLocale(targetContext, locale);
	}

	@Override
	protected void afterActivityFinished() {
		super.afterActivityFinished();

		Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		LanguageSupport.setLocale(targetContext, new Locale("en"));
	}
}
