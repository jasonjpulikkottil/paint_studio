package com.jdots.paint.ui.tools;

import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.jdots.paint.R;
import com.jdots.paint.tools.helper.DefaultNumberRangeFilter;
import com.jdots.paint.tools.options.BrushToolOptionsView;
import com.jdots.paint.tools.options.BrushToolPreview;

import java.util.Locale;

import androidx.annotation.VisibleForTesting;

public final class DefaultBrushToolOptionsView implements BrushToolOptionsView {
	private static final int MIN_BRUSH_SIZE = 1;
	private static final String TAG = DefaultBrushToolOptionsView.class.getSimpleName();

	private final EditText brushSizeText;
	private final SeekBar brushWidthSeekBar;
	private final ImageButton buttonCircle;
	private final ImageButton buttonRect;
	private final BrushToolPreview brushToolPreview;
	@VisibleForTesting
	public BrushToolOptionsView.OnBrushChangedListener brushChangedListener;

	public DefaultBrushToolOptionsView(ViewGroup rootView) {
		LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
		View brushPickerView = inflater.inflate(R.layout.dialog_paint_studio_stroke, rootView, true);

		buttonCircle = brushPickerView.findViewById(R.id.paint_studio_stroke_ibtn_circle);
		buttonRect = brushPickerView.findViewById(R.id.paint_studio_stroke_ibtn_rect);
		brushWidthSeekBar = brushPickerView.findViewById(R.id.paint_studio_stroke_width_seek_bar);
		brushWidthSeekBar.setOnSeekBarChangeListener(new DefaultBrushToolOptionsView.OnBrushChangedWidthSeekBarListener());
		brushSizeText = brushPickerView.findViewById(R.id.paint_studio_stroke_width_width_text);
		brushSizeText.setFilters(new InputFilter[]{new DefaultNumberRangeFilter(1, 100)});
		brushToolPreview = brushPickerView.findViewById(R.id.paint_studio_brush_tool_preview);

		buttonCircle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCircleButtonClicked();
			}
		});
		buttonRect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRectButtonClicked();
			}
		});

		brushSizeText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				String sizeText = brushSizeText.getText().toString();
				int sizeTextInt;
				try {
					sizeTextInt = Integer.parseInt(sizeText);
				} catch (NumberFormatException exp) {
					Log.d(TAG, exp.getLocalizedMessage());
					sizeTextInt = MIN_BRUSH_SIZE;
				}
				brushWidthSeekBar.setProgress(sizeTextInt);
			}
		});
	}

	private void onRectButtonClicked() {
		updateStrokeCap(Cap.SQUARE);
		buttonRect.setSelected(true);
		buttonCircle.setSelected(false);
		invalidate();
	}

	private void onCircleButtonClicked() {
		updateStrokeCap(Cap.ROUND);
		buttonCircle.setSelected(true);
		buttonRect.setSelected(false);
		invalidate();
	}

	public void setCurrentPaint(Paint currentPaint) {
		if (currentPaint.getStrokeCap() == Cap.ROUND) {
			buttonCircle.setSelected(true);
			buttonRect.setSelected(false);
		} else {
			buttonCircle.setSelected(false);
			buttonRect.setSelected(true);
		}
		brushWidthSeekBar.setProgress((int) currentPaint.getStrokeWidth());
		brushSizeText.setText(String.format(Locale.getDefault(), "%d", (int) currentPaint.getStrokeWidth()));
	}

	@Override
	public void setBrushChangedListener(BrushToolOptionsView.OnBrushChangedListener brushChangedListener) {
		this.brushChangedListener = brushChangedListener;
	}

	@Override
	public void setBrushPreviewListener(OnBrushPreviewListener onBrushPreviewListener) {
		brushToolPreview.setListener(onBrushPreviewListener);
		brushToolPreview.invalidate();
	}

	private void updateStrokeWidthChange(int strokeWidth) {
		if (brushChangedListener != null) {
			brushChangedListener.setStrokeWidth(strokeWidth);
		}
	}

	private void updateStrokeCap(Cap cap) {
		if (brushChangedListener != null) {
			brushChangedListener.setCap(cap);
		}
	}

	public void invalidate() {
		brushToolPreview.invalidate();
	}

	public class OnBrushChangedWidthSeekBarListener implements SeekBar.OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (progress < MIN_BRUSH_SIZE) {
				progress = MIN_BRUSH_SIZE;
				seekBar.setProgress(progress);
			}
			updateStrokeWidthChange(progress);
			if (fromUser) {
				brushSizeText.setText(String.format(Locale.getDefault(), "%d", progress));
			}

			brushToolPreview.invalidate();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			brushSizeText.setText(String.format(Locale.getDefault(), "%d", seekBar.getProgress()));
		}
	}
}
