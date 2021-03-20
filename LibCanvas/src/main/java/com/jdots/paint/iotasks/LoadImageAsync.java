package com.jdots.paint.iotasks;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.jdots.paint.FileIO;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class LoadImageAsync extends AsyncTask<Void, Void, BitmapReturnValue> {
	private static final String TAG = LoadImageAsync.class.getSimpleName();
	private WeakReference<LoadImageCallback> callbackRef;
	private int maxWidth;
	private int maxHeight;
	private int requestCode;
	private Uri uri;
	private boolean scaleImage;

	public LoadImageAsync(LoadImageCallback callback, int requestCode, int maxWidth, int maxHeight, Uri uri) {
		this.callbackRef = new WeakReference<>(callback);
		this.requestCode = requestCode;
		this.uri = uri;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.scaleImage = true;
	}

	public LoadImageAsync(LoadImageCallback callback, int requestCode, Uri uri) {
		this.callbackRef = new WeakReference<>(callback);
		this.requestCode = requestCode;
		this.uri = uri;
		this.scaleImage = false;
	}

	@Override
	protected void onPreExecute() {
		LoadImageCallback callback = callbackRef.get();
		if (callback == null || callback.isFinishing()) {
			cancel(false);
		} else {
			callback.onLoadImagePreExecute(requestCode);
		}
	}

	@Override
	protected BitmapReturnValue doInBackground(Void... voids) {
		LoadImageCallback callback = callbackRef.get();
		if (callback == null || callback.isFinishing()) {
			return null;
		}

		if (uri == null) {
			Log.e(TAG, "Can't load image file, uri is null");
			return null;
		}

		try {
			ContentResolver resolver = callback.getContentResolver();
			FileIO.filename = "image";

			String mimeType;
			if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
				mimeType = resolver.getType(uri);
			} else {
				String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
				mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase(Locale.US));
			}
			BitmapReturnValue returnValue;

			if (mimeType.equals("application/zip") || mimeType.equals("application/octet-stream")) {
				returnValue = new BitmapReturnValue(
						OpenRasterFileFormatConversion.importOraFile(resolver, uri, maxWidth, maxHeight, scaleImage), null);
			} else {
				if (scaleImage) {
					returnValue = new BitmapReturnValue(null, FileIO.getBitmapFromUri(resolver, uri, maxWidth, maxHeight));
				} else {
					returnValue = new BitmapReturnValue(null, FileIO.getBitmapFromUri(resolver, uri));
				}
			}

			return returnValue;
		} catch (IOException e) {
			Log.e(TAG, "Can't load image file", e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(BitmapReturnValue result) {
		LoadImageCallback callback = callbackRef.get();
		if (callback != null && !callback.isFinishing()) {
			callback.onLoadImagePostExecute(requestCode, uri, result);
		}
	}

	public interface LoadImageCallback {
		void onLoadImagePostExecute(int requestCode, Uri uri, BitmapReturnValue result);
		void onLoadImagePreExecute(int requestCode);
		ContentResolver getContentResolver();
		boolean isFinishing();
	}
}
