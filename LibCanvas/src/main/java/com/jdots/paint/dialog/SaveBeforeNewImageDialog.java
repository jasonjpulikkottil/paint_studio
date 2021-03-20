package com.jdots.paint.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class SaveBeforeNewImageDialog extends MainActivityDialogFragment {
	public static SaveBeforeNewImageDialog newInstance() {
		return new SaveBeforeNewImageDialog();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity(), R.style.paint_studioAlertDialog)
				.setTitle(R.string.menu_new_image)
				.setMessage(R.string.dialog_warning_new_image)
				.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						getPresenter().saveBeforeNewImage();
					}
				})
				.setNegativeButton(R.string.discard_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						getPresenter().onNewImage();
					}
				})
				.create();
	}
}
