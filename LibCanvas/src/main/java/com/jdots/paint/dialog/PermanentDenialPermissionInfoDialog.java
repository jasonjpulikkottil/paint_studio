package com.jdots.paint.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.jdots.paint.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PermanentDenialPermissionInfoDialog extends AppCompatDialogFragment {

	private String context;

	public static PermanentDenialPermissionInfoDialog newInstance(String context) {

		PermanentDenialPermissionInfoDialog permissionInfoDialog = new PermanentDenialPermissionInfoDialog();
		Bundle bundle = new Bundle();
		bundle.putString("context", context);
		permissionInfoDialog.setArguments(bundle);
		Log.d("context:", context);
		return permissionInfoDialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		context = arguments.getString("context");
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getContext(), R.style.paint_studioAlertDialog)
				.setMessage(R.string.permission_info_permanent_denial_text)
				.setPositiveButton(R.string.dialog_settings, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context)));
					}
				})
				.setNegativeButton(android.R.string.cancel, null)
				.create();
	}
}

