package com.jdots.paint.iotasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.jdots.paint.FileIO;

import java.io.File;
import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateFileAsync extends AsyncTask<Void, Void, File> {
	private static final String TAG = CreateFileAsync.class.getSimpleName();
	private WeakReference<CreateFileCallback> callbackRef;
	private int requestCode;
	private String filename;

	public CreateFileAsync(CreateFileCallback callback, int requestCode, @Nullable String filename) {
		this.callbackRef = new WeakReference<>(callback);
		this.requestCode = requestCode;
		this.filename = filename;
	}

	@Override
	protected File doInBackground(Void... voids) {
		CreateFileCallback callback = callbackRef.get();
		try {
			return FileIO.createNewEmptyPictureFile(filename, (AppCompatActivity) callback.getFileActivity());
		} catch (NullPointerException e) {
			Log.e(TAG, "Can't create file", e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(File file) {
		CreateFileCallback callback = callbackRef.get();
		if (callback != null && !callback.isFinishing()) {
			callback.onCreateFilePostExecute(requestCode, file);
		}
	}

	public interface CreateFileCallback {
		void onCreateFilePostExecute(int requestCode, File file);
		Activity getFileActivity();
		boolean isFinishing();
	}
}
