package com.jdots.paint.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class RateUsDialog extends MainActivityDialogFragment {
	public static RateUsDialog newInstance() {
		return new RateUsDialog();
	}

	@NonNull
	@Override
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getContext(), R.style.paint_studioAlertDialog)
				.setMessage(getString(R.string.paint_studio_rate_us))
				.setTitle(getString(R.string.paint_studio_rate_us_title))
				.setPositiveButton(R.string.paint_studio_yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getPresenter().rateUsClicked();
						dismiss();
					}
				})
				.setNegativeButton(R.string.paint_studio_not_now, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dismiss();
					}
				})
				.create();
	}
}
