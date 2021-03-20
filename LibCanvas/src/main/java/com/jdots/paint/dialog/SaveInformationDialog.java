package com.jdots.paint.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jdots.paint.FileIO;
import com.jdots.paint.R;
import com.jdots.paint.common.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.jdots.paint.common.MainActivityConstants;

public class SaveInformationDialog extends MainActivityDialogFragment implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

	private Spinner mySpinner;
	private LayoutInflater inflater;
	private ViewGroup specificFormatLayout;
	private View jpgView;
	private SeekBar mySeekbar;
	private TextView percentage;
	private ImageButton informationButton;
	private TextView imageName;

	private int permission;
	private String setName;

	public static SaveInformationDialog newInstance(int permissionCode, int imageNumber, boolean isStandard) {
		if (isStandard) {
			FileIO.isCatrobatImage = false;
			FileIO.filename = "image";
			FileIO.compressFormat = Bitmap.CompressFormat.JPEG;
			FileIO.ending = ".jpg";
			FileIO.compressQuality = 100;
		}

		SaveInformationDialog dialog = new SaveInformationDialog();
		Bundle bundle = new Bundle();

		if (FileIO.filename.equals("image")) {
			bundle.putString("setName", FileIO.filename + imageNumber);
		} else {
			bundle.putString("setName", FileIO.filename);
		}

		bundle.putInt("permission", permissionCode);
		dialog.setArguments(bundle);

		return dialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		permission = arguments.getInt("permission");
		setName = arguments.getString("setName");
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initializeViews(view);
		initializeFunctioning();

		if (FileIO.isCatrobatImage) {
			mySpinner.setSelection(2);
		} else if (FileIO.compressFormat == Bitmap.CompressFormat.PNG) {
			mySpinner.setSelection(1);
		} else {
			mySpinner.setSelection(0);
		}
	}

	@NonNull
	@Override
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		inflater = getActivity().getLayoutInflater();
		View customLayout = inflater.inflate(R.layout.dialog_paint_studio_save, null);
		onViewCreated(customLayout, savedInstanceState);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.paint_studioAlertDialog);
		builder.setTitle(R.string.dialog_save_image_title);
		builder.setView(customLayout);
		builder.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				FileIO.filename = imageName.getText().toString();

				if (permission != MainActivityConstants.PERMISSION_EXTERNAL_STORAGE_SAVE_COPY && FileIO.checkIfDifferentFile(FileIO.getDefaultFileName()) != Constants.IS_NO_FILE) {
					getPresenter().showOverwriteDialog(permission);
				} else {
					getPresenter().switchBetweenVersions(permission);
				}

				dismiss();
			}
		});

		builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});

		return builder.create();
	}

	private void initializeViews(View customLayout) {
		specificFormatLayout = customLayout.findViewById(R.id.paint_studio_save_format_specific_options);

		jpgView = inflater.inflate(R.layout.dialog_paint_studio_save_jpg_sub_dialog, specificFormatLayout, false);
		mySeekbar = jpgView.findViewById(R.id.paint_studio_jpg_seekbar_save_info);
		percentage = jpgView.findViewById(R.id.paint_studio_percentage_save_info);
		mySpinner = customLayout.findViewById(R.id.paint_studio_save_dialog_spinner);
		informationButton = customLayout.findViewById(R.id.paint_studio_btn_save_info);
		imageName = customLayout.findViewById(R.id.paint_studio_image_name_save_text);
	}

	private void initializeFunctioning() {
		List<String> spinnerArray = new ArrayList<>();
		spinnerArray.add("jpg");
		spinnerArray.add("png");
		spinnerArray.add("ora");

		ArrayAdapter<String> adapter = new ArrayAdapter<>(mySpinner.getContext(),
				android.R.layout.simple_spinner_item, spinnerArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(adapter);

		String percentageString = Integer.toString(FileIO.compressQuality);
		percentage.setText(percentageString);
		imageName.setText(setName);
		mySeekbar.setProgress(FileIO.compressQuality);

		mySeekbar.setOnSeekBarChangeListener(this);
		mySpinner.setOnItemSelectedListener(this);

		informationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (FileIO.isCatrobatImage) {
					getPresenter().showOraInformationDialog();
				} else if (FileIO.compressFormat == Bitmap.CompressFormat.JPEG) {
					getPresenter().showJpgInformationDialog();
				} else {
					getPresenter().showPngInformationDialog();
				}
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String selectedItem = parent.getItemAtPosition(position).toString();

		switch (selectedItem) {
			case "jpg":
				specificFormatLayout.removeAllViews();
				specificFormatLayout.addView(jpgView);
				FileIO.compressFormat = Bitmap.CompressFormat.JPEG;
				FileIO.isCatrobatImage = false;
				FileIO.ending = ".jpg";
				break;
			case "png":
				specificFormatLayout.removeAllViews();
				FileIO.compressFormat = Bitmap.CompressFormat.PNG;
				FileIO.isCatrobatImage = false;
				FileIO.ending = ".png";
				break;
			case "ora":
				specificFormatLayout.removeAllViews();
				FileIO.compressFormat = Bitmap.CompressFormat.PNG;
				FileIO.isCatrobatImage = true;
				FileIO.ending = ".ora";
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		String stringToInsert = progress + "%";
		percentage.setText(stringToInsert);
		FileIO.compressQuality = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}
}
