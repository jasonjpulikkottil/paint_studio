package com.jdots.paint.ui;

import android.net.Uri;

import com.jdots.paint.contract.MainActivityContracts;
import com.jdots.paint.iotasks.CreateFileAsync;
import com.jdots.paint.iotasks.LoadImageAsync;
import com.jdots.paint.iotasks.SaveImageAsync;
import com.jdots.paint.tools.Workspace;

public class MainActivityInteractor implements MainActivityContracts.Interactor {

	@Override
	public void saveCopy(SaveImageAsync.SaveImageCallback callback, int requestCode, Workspace workspace) {
		new SaveImageAsync(callback, requestCode, workspace, null, true).execute();
	}

	@Override
	public void createFile(CreateFileAsync.CreateFileCallback callback, int requestCode, String filename) {
		new CreateFileAsync(callback, requestCode, filename).execute();
	}

	@Override
	public void saveImage(SaveImageAsync.SaveImageCallback callback, int requestCode, Workspace workspace, Uri uri) {
		new SaveImageAsync(callback, requestCode, workspace, uri, false).execute();
	}

	@Override
	public void loadFile(LoadImageAsync.LoadImageCallback callback, int requestCode, Uri uri) {
		new LoadImageAsync(callback, requestCode, uri).execute();
	}

	@Override
	public void loadFile(LoadImageAsync.LoadImageCallback callback, int requestCode, int maxWidth, int maxHeight, Uri uri) {
		new LoadImageAsync(callback, requestCode, maxWidth, maxHeight, uri).execute();
	}
}
