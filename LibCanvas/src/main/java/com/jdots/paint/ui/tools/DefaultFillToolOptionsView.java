package com.jdots.paint.ui.tools;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.jdots.paint.R;
import com.jdots.paint.tools.helper.DefaultNumberRangeFilter;
import com.jdots.paint.tools.implementation.FillTool;
import com.jdots.paint.tools.options.FillToolOptionsView;

import java.util.Locale;

public class DefaultFillToolOptionsView implements FillToolOptionsView {
	private SeekBar colorToleranceSeekBar;
	private EditText colorToleranceEditText;
	private Callback callback;

	public DefaultFillToolOptionsView(ViewGroup toolSpecificOptionsLayout) {
		LayoutInflater inflater = LayoutInflater.from(toolSpecificOptionsLayout.getContext());
		View fillToolOptionsView = inflater.inflate(R.layout.dialog_paint_studio_fill_tool, toolSpecificOptionsLayout);

		colorToleranceSeekBar = fillToolOptionsView.findViewById(R.id.paint_studio_color_tolerance_seek_bar);
		colorToleranceEditText = fillToolOptionsView.findViewById(R.id.paint_studio_fill_tool_dialog_color_tolerance_input);
		colorToleranceEditText.setFilters(new InputFilter[]{new DefaultNumberRangeFilter(0, 100)});
		colorToleranceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					setColorToleranceText(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		colorToleranceEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					int colorToleranceInPercent = Integer.parseInt(s.toString());
					colorToleranceSeekBar.setProgress(colorToleranceInPercent);
					updateColorTolerance(colorToleranceInPercent);
				} catch (NumberFormatException e) {
					Log.e("Error parsing tolerance", "result was null");
				}
			}
		});
		setColorToleranceText(FillTool.DEFAULT_TOLERANCE_IN_PERCENT);
	}

	private void updateColorTolerance(int colorTolerance) {
		if (callback != null) {
			callback.onColorToleranceChanged(colorTolerance);
		}
	}

	private void setColorToleranceText(int toleranceInPercent) {
		colorToleranceEditText.setText(String.format(Locale.getDefault(), "%d", toleranceInPercent));
	}

	@Override
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
}
