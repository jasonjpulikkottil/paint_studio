package com.jdots.paint.ui.tools;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jdots.paint.R;
import com.jdots.paint.tools.drawable.DrawableShape;
import com.jdots.paint.tools.drawable.DrawableStyle;
import com.jdots.paint.tools.helper.DefaultNumberRangeFilter;
import com.jdots.paint.tools.options.ShapeToolOptionsView;

import java.util.Locale;

public class DefaultShapeToolOptionsView implements ShapeToolOptionsView {
	private static final int MIN_STROKE_WIDTH = 1;

	private Callback callback;
	private ImageButton squareButton;
	private ImageButton circleButton;
	private ImageButton heartButton;
	private ImageButton starButton;
	private ImageButton fillButton;
	private ImageButton outlineButton;
	private View outlineView;
	private TextView outlineTextView;
	private SeekBar outlineWidthSeekBar;
	private EditText outlineWidthEditText;
	private TextView shapeToolDialogTitle;
	private TextView shapeToolFillOutline;

	public DefaultShapeToolOptionsView(ViewGroup rootView) {
		LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
		View shapeToolView = inflater.inflate(R.layout.dialog_paint_studio_shapes, rootView);

		squareButton = shapeToolView.findViewById(R.id.paint_studio_shapes_square_btn);
		circleButton = shapeToolView.findViewById(R.id.paint_studio_shapes_circle_btn);
		heartButton = shapeToolView.findViewById(R.id.paint_studio_shapes_heart_btn);
		starButton = shapeToolView.findViewById(R.id.paint_studio_shapes_star_btn);
		fillButton = shapeToolView.findViewById(R.id.paint_studio_shape_ibtn_fill);
		outlineButton = shapeToolView.findViewById(R.id.paint_studio_shape_ibtn_outline);
		shapeToolDialogTitle = shapeToolView.findViewById(R.id.paint_studio_shape_tool_dialog_title);
		shapeToolFillOutline = shapeToolView.findViewById(R.id.paint_studio_shape_tool_fill_outline);

		outlineView = shapeToolView.findViewById(R.id.paint_studio_outline_view_border);
		outlineTextView = shapeToolView.findViewById(R.id.paint_studio_outline_view_text_view);

		outlineWidthSeekBar = shapeToolView.findViewById(R.id.paint_studio_shape_stroke_width_seek_bar);
		outlineWidthEditText = shapeToolView.findViewById(R.id.paint_studio_shape_outline_edit);
		outlineWidthEditText.setFilters(new InputFilter[]{new DefaultNumberRangeFilter(1, 100)});

		int startingOutlineWidth = 25;
		outlineWidthEditText.setText(String.valueOf(startingOutlineWidth));
		outlineWidthSeekBar.setProgress(startingOutlineWidth);

		initializeListeners();
		setShapeActivated(DrawableShape.RECTANGLE);
		setDrawTypeActivated(DrawableStyle.FILL);
	}

	private void initializeListeners() {
		squareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onShapeClicked(DrawableShape.RECTANGLE);
			}
		});
		circleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onShapeClicked(DrawableShape.OVAL);
			}
		});
		heartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onShapeClicked(DrawableShape.HEART);
			}
		});
		starButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onShapeClicked(DrawableShape.STAR);
			}
		});
		fillButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDrawTypeClicked(DrawableStyle.FILL);
			}
		});
		outlineButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDrawTypeClicked(DrawableStyle.STROKE);
			}
		});
		outlineWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (progress < MIN_STROKE_WIDTH) {
					progress = MIN_STROKE_WIDTH;
				}
				if (fromUser) {
					seekBar.setProgress(progress);
				}
				outlineWidthEditText.setText(String.valueOf(progress));
				onOutlineWidthChanged(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		outlineWidthEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				String sizeText = outlineWidthEditText.getText().toString();
				int sizeTextInt;
				try {
					sizeTextInt = Integer.parseInt(sizeText);
				} catch (NumberFormatException exp) {
					sizeTextInt = MIN_STROKE_WIDTH;
				}
				outlineWidthSeekBar.setProgress(sizeTextInt);
			}
		});
	}

	private void onShapeClicked(DrawableShape shape) {
		callback.setToolType(shape);
		setShapeActivated(shape);
	}

	private void onDrawTypeClicked(DrawableStyle drawType) {
		callback.setDrawType(drawType);
		setDrawTypeActivated(drawType);
	}

	private void onOutlineWidthChanged(int outlineWidth) {
		callback.setOutlineWidth(outlineWidth);
	}

	private void resetShapeActivated() {
		View[] buttons = {squareButton, circleButton, heartButton, starButton};
		for (View button : buttons) {
			button.setSelected(false);
		}
	}

	private void resetDrawTypeActivated() {
		fillButton.setSelected(false);
		outlineButton.setSelected(false);
	}

	@Override
	public void setShapeActivated(DrawableShape shape) {
		resetShapeActivated();
		switch (shape) {
			case RECTANGLE:
				squareButton.setSelected(true);
				shapeToolDialogTitle.setText(R.string.shape_tool_dialog_rect_title);
				break;
			case OVAL:
				circleButton.setSelected(true);
				shapeToolDialogTitle.setText(R.string.shape_tool_dialog_ellipse_title);
				break;
			case HEART:
				heartButton.setSelected(true);
				shapeToolDialogTitle.setText(R.string.shape_tool_dialog_heart_title);
				break;
			case STAR:
				starButton.setSelected(true);
				shapeToolDialogTitle.setText(R.string.shape_tool_dialog_star_title);
				break;
			default:
				squareButton.setSelected(true);
				break;
		}
	}

	@Override
	public void setDrawTypeActivated(DrawableStyle drawType) {
		resetDrawTypeActivated();
		switch (drawType) {
			case FILL:
				fillButton.setSelected(true);
				shapeToolFillOutline.setText(R.string.shape_tool_dialog_fill_title);
				squareButton.setImageResource(R.drawable.ic_paint_studio_rectangle);
				circleButton.setImageResource(R.drawable.ic_paint_studio_circle);
				heartButton.setImageResource(R.drawable.ic_paint_studio_heart);
				starButton.setImageResource(R.drawable.ic_paint_studio_star);
				outlineWidthSeekBar.setVisibility(View.GONE);
				outlineWidthEditText.setVisibility(View.GONE);
				outlineView.setVisibility(View.GONE);
				outlineTextView.setVisibility(View.GONE);
				break;
			case STROKE:
				outlineButton.setSelected(true);
				shapeToolFillOutline.setText(R.string.shape_tool_dialog_outline_title);
				squareButton.setImageResource(R.drawable.ic_paint_studio_rectangle_out);
				circleButton.setImageResource(R.drawable.ic_paint_studio_circle_out);
				heartButton.setImageResource(R.drawable.ic_paint_studio_heart_out);
				starButton.setImageResource(R.drawable.ic_paint_studio_star_out);
				outlineWidthSeekBar.setVisibility(View.VISIBLE);
				outlineWidthEditText.setVisibility(View.VISIBLE);
				outlineView.setVisibility(View.VISIBLE);
				outlineTextView.setVisibility(View.VISIBLE);
				break;
			default:
				fillButton.setSelected(true);
				break;
		}
	}

	@Override
	public void setShapeOutlineWidth(int outlineWidth) {
		outlineWidthSeekBar.setProgress(outlineWidth);
		outlineWidthEditText.setText(String.format(Locale.getDefault(), "%d", (int) outlineWidth));
	}

	@Override
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
}
