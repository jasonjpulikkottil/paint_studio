package com.jdots.paint.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

public class SaveBeforeFinishDialog extends MainActivityDialogFragment {
	private static final String DIALOG_TYPE = "argDialogType";
	private SaveBeforeFinishDialogType dialogType;

	public static SaveBeforeFinishDialog newInstance(SaveBeforeFinishDialogType dialogType) {
		Bundle args = new Bundle();
		args.putSerializable(DIALOG_TYPE, dialogType);

		SaveBeforeFinishDialog dialog = new SaveBeforeFinishDialog();
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();
		dialogType = (SaveBeforeFinishDialogType) arguments.getSerializable(DIALOG_TYPE);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity(), R.style.paint_studioAlertDialog)
				.setTitle(dialogType.getTitleResource())
				.setMessage(dialogType.getMessageResource())
				.setPositiveButton(R.string.save_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						getPresenter().saveBeforeFinish();
					}
				})
				.setNegativeButton(R.string.discard_button_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						getPresenter().finishActivity();
					}
				})
				.create();
	}

	public enum SaveBeforeFinishDialogType {
		FINISH(R.string.closing_security_question_title, R.string.closing_security_question);

		private final int titleResource;
		private final int messageResource;

		SaveBeforeFinishDialogType(@StringRes int titleResource, @StringRes int messageResource) {
			this.titleResource = titleResource;
			this.messageResource = messageResource;
		}

		public int getTitleResource() {
			return titleResource;
		}

		public int getMessageResource() {
			return messageResource;
		}
	}
}
