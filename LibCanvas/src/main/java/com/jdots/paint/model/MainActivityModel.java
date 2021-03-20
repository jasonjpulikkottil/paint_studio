package com.jdots.paint.model;

import android.net.Uri;

import com.jdots.paint.contract.MainActivityContracts;

public class MainActivityModel implements MainActivityContracts.Model {
	private boolean openedFromCatroid;
	private boolean isFullscreen;
	private boolean isSaved;
	private boolean wasInitialAnimationPlayed;
	private Uri savedPictureUri;
	private Uri cameraImageUri;

	@Override
	public Uri getCameraImageUri() {
		return cameraImageUri;
	}

	@Override
	public void setCameraImageUri(Uri cameraImageUri) {
		this.cameraImageUri = cameraImageUri;
	}

	@Override
	public Uri getSavedPictureUri() {
		return savedPictureUri;
	}

	@Override
	public void setSavedPictureUri(Uri savedPictureUri) {
		this.savedPictureUri = savedPictureUri;
	}

	@Override
	public boolean isSaved() {
		return isSaved;
	}

	@Override
	public void setSaved(boolean saved) {
		isSaved = saved;
	}

	@Override
	public boolean isFullscreen() {
		return isFullscreen;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		isFullscreen = fullscreen;
	}

	@Override
	public boolean isOpenedFromCatroid() {
		return openedFromCatroid;
	}

	@Override
	public void setOpenedFromCatroid(boolean openedFromCatroid) {
		this.openedFromCatroid = openedFromCatroid;
	}

	@Override
	public boolean wasInitialAnimationPlayed() {
		return wasInitialAnimationPlayed;
	}

	@Override
	public void setInitialAnimationPlayed(boolean wasInitialAnimationPlayed) {
		this.wasInitialAnimationPlayed = wasInitialAnimationPlayed;
	}
}
