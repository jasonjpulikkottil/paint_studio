package com.jdots.paint.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PngInfoDialog extends AppCompatDialogFragment {
	public static PngInfoDialog newInstance() {
		return new PngInfoDialog();
	}

	@NonNull
	@Override
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getContext(), R.style.paint_studioAlertDialog)
				.setMessage(R.string.paint_studio_png_message_dialog)
				.setTitle(R.string.paint_studio_png_title_dialog)
				.setPositiveButton(R.string.paint_studio_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				})
				.create();
	}
}
