package com.jdots.paint;

import android.content.SharedPreferences;

import com.jdots.paint.common.Constants;

public class UserPreferences {
	SharedPreferences preferences;

	public UserPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public boolean getPreferenceLikeUsDialogValue() {
		return preferences.getBoolean(Constants.SHOW_LIKE_US_DIALOG_SHARED_PREFERENCES_TAG, false);
	}

	public int getPreferenceImageNumber() {
		return preferences.getInt(Constants.IMAGE_NUMBER_SHARED_PREFERENCES_TAG, 0);
	}

	public void setPreferenceImageNumber(int value) {
		preferences
			.edit()
			.putInt(Constants.IMAGE_NUMBER_SHARED_PREFERENCES_TAG, value)
			.apply();
	}

	public void setPreferenceLikeUsDialogValue() {
		preferences
			.edit()
			.putBoolean(Constants.SHOW_LIKE_US_DIALOG_SHARED_PREFERENCES_TAG, true)
			.apply();
	}
}
