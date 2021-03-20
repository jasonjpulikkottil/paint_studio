package com.jdots.paint.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class SaveBeforeLoadImageDialog extends MainActivityDialogFragment {
	public static SaveBeforeLoadImageDialog newInstance() {
		return new SaveBeforeLoadImageDialog();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity(), R.style.paint_studioAlertDialog)
				.setTitle(R.string.menu_load_image)
				.setMessage(R.string.dialog_warning_new_image)
				.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						getPresenter().saveBeforeLoadImage();
					}
				})
				.setNegativeButton(R.string.discard_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						getPresenter().loadNewImage();
					}
				})
				.create();
	}
}
