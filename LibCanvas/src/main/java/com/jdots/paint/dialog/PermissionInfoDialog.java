package com.jdots.paint.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jdots.paint.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

public class PermissionInfoDialog extends AppCompatDialogFragment {

	private static final String PERMISSION_TYPE_KEY = "permissionTypeKey";
	private static final String PERMISSIONS_KEY = "permissionsKey";
	private static final String REQUEST_CODE_KEY = "requestCodeKey";

	private int requestCode;
	private String[] permissions;
	private PermissionType permissionType;

	public static PermissionInfoDialog newInstance(PermissionType permissionType, String[] permissions, int requestCode) {
		PermissionInfoDialog permissionInfoDialog = new PermissionInfoDialog();

		Bundle bundle = new Bundle();
		bundle.putSerializable(PERMISSION_TYPE_KEY, permissionType);
		bundle.putStringArray(PERMISSIONS_KEY, permissions);
		bundle.putInt(REQUEST_CODE_KEY, requestCode);
		permissionInfoDialog.setArguments(bundle);
		return permissionInfoDialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		requestCode = arguments.getInt(REQUEST_CODE_KEY);
		permissions = arguments.getStringArray(PERMISSIONS_KEY);
		permissionType = (PermissionType) arguments.getSerializable(PERMISSION_TYPE_KEY);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getContext(), R.style.paint_studioAlertDialog)
				.setIcon(permissionType.getIconResource())
				.setMessage(permissionType.getMessageResource())
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
					}
				})
				.setNegativeButton(android.R.string.cancel, null)
				.create();
	}

	public enum PermissionType {
		EXTERNAL_STORAGE(R.drawable.ic_paint_studio_dialog_info,
				R.string.permission_info_external_storage_text);

		private int iconResource;
		private int messageResource;

		PermissionType(@DrawableRes int iconResource, @StringRes int messageResource) {
			this.iconResource = iconResource;
			this.messageResource = messageResource;
		}

		public @DrawableRes int getIconResource() {
			return iconResource;
		}
		public @StringRes int getMessageResource() {
			return messageResource;
		}
	}
}
