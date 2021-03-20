package com.jdots.paint.test.espresso.util.wrappers;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

import com.jdots.paint.R;
import com.jdots.paint.tools.Tool;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.test.platform.app.InstrumentationRegistry;

import static com.jdots.paint.test.espresso.util.EspressoUtils.getMainActivity;
import static org.junit.Assert.assertEquals;

public final class ToolPropertiesInteraction extends CustomViewInteraction {
	private ToolPropertiesInteraction() {
		super(null);
	}
	public static ToolPropertiesInteraction onToolProperties() {
		return new ToolPropertiesInteraction();
	}

	public ToolPropertiesInteraction checkMatchesColor(@ColorInt int expectedColor) {
		assertEquals(expectedColor, getCurrentTool().getDrawPaint().getColor());
		return this;
	}

	public ToolPropertiesInteraction checkMatchesColorResource(@ColorRes int expectedColorRes) {
		int expectedColor = ContextCompat.getColor(InstrumentationRegistry.getInstrumentation().getTargetContext(), expectedColorRes);
		return checkMatchesColor(expectedColor);
	}

	public ToolPropertiesInteraction checkCap(Cap expectedCap) {
		Paint strokePaint = getCurrentTool().getDrawPaint();
		assertEquals(expectedCap, strokePaint.getStrokeCap());
		return this;
	}

	public ToolPropertiesInteraction checkStrokeWidth(float expectedStrokeWidth) {
		Paint strokePaint = getCurrentTool().getDrawPaint();
		assertEquals(expectedStrokeWidth, strokePaint.getStrokeWidth(), Float.MIN_VALUE);
		return this;
	}

	public ToolPropertiesInteraction setColor(int color) {
		getCurrentTool().changePaintColor(color);
		return this;
	}

	public Tool getCurrentTool() {
		return getMainActivity().toolReference.get();
	}

	public ToolPropertiesInteraction setColorResource(@ColorRes int colorResource) {
		int color = ContextCompat.getColor(InstrumentationRegistry.getInstrumentation().getTargetContext(), colorResource);
		return setColor(color);
	}

	public ToolPropertiesInteraction setColorPreset(int colorPresetPosition) {
		Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		int[] presetColors = targetContext.getResources().getIntArray(R.array.paint_studio_color_picker_preset_colors);
		return setColor(presetColors[colorPresetPosition]);
	}
}
