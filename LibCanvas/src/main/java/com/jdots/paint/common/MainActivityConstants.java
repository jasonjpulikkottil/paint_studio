package com.jdots.paint.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public final class MainActivityConstants {
	public static final int SAVE_IMAGE_DEFAULT = 1;
	public static final int SAVE_IMAGE_NEW_EMPTY = 2;
	public static final int SAVE_IMAGE_LOAD_NEW = 3;
	public static final int SAVE_IMAGE_FINISH = 4;

	public static final int LOAD_IMAGE_DEFAULT = 1;
	public static final int LOAD_IMAGE_IMPORTPNG = 2;
	public static final int LOAD_IMAGE_CATROID = 3;

	public static final int CREATE_FILE_DEFAULT = 1;

	public static final int REQUEST_CODE_IMPORTPNG = 1;
	public static final int REQUEST_CODE_LOAD_PICTURE = 2;
	public static final int REQUEST_CODE_INTRO = 3;

	public static final int PERMISSION_EXTERNAL_STORAGE_SAVE = 1;
	public static final int PERMISSION_EXTERNAL_STORAGE_SAVE_COPY = 2;
	public static final int PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_LOAD_NEW = 3;
	public static final int PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_NEW_EMPTY = 4;
	public static final int PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_FINISH = 5;
	public static final int PERMISSION_REQUEST_CODE_LOAD_PICTURE = 6;

	public static final int RESULT_INTRO_MW_NOT_SUPPORTED = 10;

	@IntDef({SAVE_IMAGE_DEFAULT, SAVE_IMAGE_NEW_EMPTY, SAVE_IMAGE_LOAD_NEW, SAVE_IMAGE_FINISH})
	@Retention(RetentionPolicy.SOURCE)
	public @interface SaveImageRequestCode {
	}

	@IntDef({LOAD_IMAGE_DEFAULT, LOAD_IMAGE_IMPORTPNG, LOAD_IMAGE_CATROID})
	@Retention(RetentionPolicy.SOURCE)
	public @interface LoadImageRequestCode {
	}

	@IntDef({CREATE_FILE_DEFAULT})
	@Retention(RetentionPolicy.SOURCE)
	public @interface CreateFileRequestCode {
	}

	@IntDef({REQUEST_CODE_IMPORTPNG, REQUEST_CODE_LOAD_PICTURE, REQUEST_CODE_INTRO})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ActivityRequestCode {
	}

	@IntDef({PERMISSION_EXTERNAL_STORAGE_SAVE,
			PERMISSION_EXTERNAL_STORAGE_SAVE_COPY,
			PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_LOAD_NEW,
			PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_NEW_EMPTY,
			PERMISSION_EXTERNAL_STORAGE_SAVE_CONFIRMED_FINISH,
			PERMISSION_REQUEST_CODE_LOAD_PICTURE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface PermissionRequestCode {
	}

	private MainActivityConstants() {
		throw new AssertionError();
	}
}
