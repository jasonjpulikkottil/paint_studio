package com.jdots.paint.common;

import android.os.Environment;

import java.io.File;

public final class Constants {
	public static final String PaintStudio_PICTURE_PATH = "org.catrobat.extra.PaintStudio_PICTURE_PATH";
	public static final String PaintStudio_PICTURE_NAME = "org.catrobat.extra.PaintStudio_PICTURE_NAME";

	public static final String TEMP_PICTURE_NAME = "PaintStudioTemp";
	public static final File MEDIA_DIRECTORY = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/PaintStudio/");
	public static final String MEDIA_GALLEY_URL = "https://www.google.com";

	public static final String ABOUT_DIALOG_FRAGMENT_TAG = "aboutdialogfragment";
	public static final String LIKE_US_DIALOG_FRAGMENT_TAG = "likeusdialogfragment";
	public static final String RATE_US_DIALOG_FRAGMENT_TAG = "rateusdialogfragment";
	public static final String FEEDBACK_DIALOG_FRAGMENT_TAG = "feedbackdialogfragment";
	public static final String SAVE_DIALOG_FRAGMENT_TAG = "savedialogerror";
	public static final String LOAD_DIALOG_FRAGMENT_TAG = "loadbitmapdialogerror";
	public static final String COLOR_PICKER_DIALOG_TAG = "ColorPickerDialogTag";
	public static final String SAVE_QUESTION_FRAGMENT_TAG = "savebeforequitfragment";
	public static final String SAVE_INFORMATION_DIALOG_TAG = "saveinformationdialogfragment";
	public static final String OVERWRITE_INFORMATION_DIALOG_TAG = "saveinformationdialogfragment";
	public static final String PNG_INFORMATION_DIALOG_TAG = "pnginformationdialogfragment";
	public static final String JPG_INFORMATION_DIALOG_TAG = "jpginformationdialogfragment";
	public static final String ORA_INFORMATION_DIALOG_TAG = "orainformationdialogfragment";
	public static final String CATROID_MEDIA_GALLERY_FRAGMENT_TAG = "catroidmediagalleryfragment";

	public static final String PERMISSION_DIALOG_FRAGMENT_TAG = "permissiondialogfragment";

	public static final int INVALID_RESOURCE_ID = 0;

	public static final String SHOW_LIKE_US_DIALOG_SHARED_PREFERENCES_TAG = "showlikeusdialog";
	public static final String IMAGE_NUMBER_SHARED_PREFERENCES_TAG = "imagenumbertag";

	public static final int IS_JPG = 0;
	public static final int IS_PNG = 1;
	public static final int IS_ORA = 2;
	public static final int IS_NO_FILE = -1;

	public static final int MAX_LAYERS = 4;

	private Constants() {
		throw new AssertionError();
	}
}
