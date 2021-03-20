package com.jdots.paint;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.jdots.paint.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Objects;

public final class FileIO {
	public static String filename = "image";
	public static String ending = ".jpg";
	public static int compressQuality = 100;
	public static Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
	public static boolean catroidFlag = false;
	public static boolean isCatrobatImage = false;

	public static String currentFileNameJpg = null;
	public static String currentFileNamePng = null;
	public static String currentFileNameOra = null;
	public static Uri uriFileJpg = null;
	public static Uri uriFilePng = null;
	public static Uri uriFileOra = null;

	private FileIO() {
		throw new AssertionError();
	}

	private static void saveBitmapToStream(OutputStream outputStream, Bitmap bitmap) throws IOException {
		if (bitmap == null || bitmap.isRecycled()) {
			throw new IllegalArgumentException("Bitmap is invalid");
		}

		if (compressFormat == Bitmap.CompressFormat.JPEG) {
			Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), bitmap.getConfig());
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawColor(Color.WHITE);
			canvas.drawBitmap(bitmap, 0F, 0F, null);

			bitmap = newBitmap;
		}

		if (!bitmap.compress(compressFormat, compressQuality, outputStream)) {
			throw new IOException("Can not write png to stream.");
		}
	}

	public static Uri saveBitmapToUri(Uri uri, ContentResolver resolver, Bitmap bitmap) throws IOException {
		OutputStream outputStream = resolver.openOutputStream(uri);

		if (outputStream == null) {
			throw new IllegalArgumentException("Can not open uri.");
		}
		try {
			saveBitmapToStream(outputStream, bitmap);
		} finally {
			outputStream.close();
		}

		return uri;
	}

	public static Uri saveBitmapToFile(String fileName, Bitmap bitmap, ContentResolver resolver) throws IOException {
		OutputStream fos;
		Uri imageUri;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
			contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
			contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

			imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


			try {
				fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));

				saveBitmapToStream(fos, bitmap);

				Objects.requireNonNull(fos, "Can't create fileoutputstream!");
				fos.close();
			} finally {
				//
			}
		} else {
			if (!(Constants.MEDIA_DIRECTORY.exists() || Constants.MEDIA_DIRECTORY.mkdirs())) {
				throw new IOException("Can not create media directory.");
			}

			File file = new File(Constants.MEDIA_DIRECTORY, "paint_studio_"+fileName);
			OutputStream outputStream = new FileOutputStream(file);

			try {
				saveBitmapToStream(outputStream, bitmap);
			} finally {
				outputStream.close();
			}

			imageUri = Uri.fromFile(file);
		}

		return imageUri;
	}

	public static Uri saveBitmapToCache(Bitmap bitmap, MainActivity mainActivity) {
		Uri uri = null;
		try {
			File cachePath = new File(mainActivity.getCacheDir(), "images");
			cachePath.mkdirs();
			FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
			saveBitmapToStream(stream, bitmap);
			stream.close();
			File imagePath = new File(mainActivity.getCacheDir(), "images");
			File newFile = new File(imagePath, "image.png");
			String fileProviderString = mainActivity.getApplicationContext().getPackageName() + ".fileprovider";
			uri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), fileProviderString, newFile);
		} catch (IOException e) {
			Log.e("Can not write", "Can not write png to stream.", e);
		}
		return uri;
	}

	public static String getDefaultFileName() {
		return filename + ending;
	}

	public static File createNewEmptyPictureFile(String filename, AppCompatActivity activity) throws NullPointerException {
		if (filename == null) {
			filename = getDefaultFileName();
		}

		if (!filename.toLowerCase(Locale.US).endsWith(ending.toLowerCase(Locale.US))) {
			filename += ending;
		}


		File saveDir =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				//+ File.separator + ""
				+"/PaintStudio/");

		if(!saveDir.exists()) {
			saveDir.mkdirs();
		}

		if(!saveDir.exists()) {
			 saveDir =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					+ File.separator + ""
					);
		}



		if (!Objects.requireNonNull(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).exists()
				&& !Objects.requireNonNull(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)).mkdirs()) {
			throw new NullPointerException("Can not create media directory.");
		}

		return new File(saveDir, "paint_studio_"+filename);
	}

	private static Bitmap decodeBitmapFromUri(ContentResolver resolver, @NonNull Uri uri, BitmapFactory.Options options) throws IOException {
		InputStream inputStream = resolver.openInputStream(uri);
		if (inputStream == null) {
			throw new IOException("Can't open input stream");
		}
		try {
			return BitmapFactory.decodeStream(inputStream, null, options);
		} finally {
			inputStream.close();
		}
	}

	public static void parseFileName(Uri uri, ContentResolver resolver) {
		String fileName = "image";

		Cursor cursor = null;
		try {
			cursor = resolver.query(uri, new String[]{
					MediaStore.Images.ImageColumns.DISPLAY_NAME
			}, null, null, null);

			if (cursor != null && cursor.moveToFirst()) {
				fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
			}
		} finally {

			if (cursor != null) {
				cursor.close();
			}
		}

		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			ending = ".jpg";
			compressFormat = Bitmap.CompressFormat.JPEG;
			filename = fileName.substring(0, fileName.length() - FileIO.ending.length());
		} else if (fileName.endsWith(".png")) {
			ending = ".png";
			compressFormat = Bitmap.CompressFormat.PNG;
			filename = fileName.substring(0, fileName.length() - FileIO.ending.length());
		}
	}

	public static int checkIfDifferentFile(String filename) {
		if (currentFileNamePng == null && currentFileNameJpg == null && currentFileNameOra == null) {
			return Constants.IS_NO_FILE;
		}

		if (currentFileNameJpg != null && currentFileNameJpg.equals(filename)) {
			return Constants.IS_JPG;
		}

		if (currentFileNamePng != null && currentFileNamePng.equals(filename)) {
			return Constants.IS_PNG;
		}

		if (currentFileNameOra != null && currentFileNameOra.equals(filename)) {
			return Constants.IS_ORA;
		}

		return Constants.IS_NO_FILE;
	}

	public static int calculateSampleSize(int width, int height, int maxWidth, int maxHeight) {
		int sampleSize = 1;
		while (width > maxWidth || height > maxHeight) {
			width /= 2;
			height /= 2;
			sampleSize *= 2;
		}
		return sampleSize;
	}

	public static Bitmap getBitmapFromUri(ContentResolver resolver, @NonNull Uri bitmapUri) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;
		return enableAlpha(decodeBitmapFromUri(resolver, bitmapUri, options));
	}

	public static Bitmap getBitmapFromUri(ContentResolver resolver, @NonNull Uri bitmapUri, int maxWidth, int maxHeight) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		decodeBitmapFromUri(resolver, bitmapUri, options);
		if (options.outHeight < 0 || options.outWidth < 0) {
			throw new IOException("Can't load bitmap from uri");
		}

		int sampleSize = calculateSampleSize(options.outWidth, options.outHeight,
				maxWidth, maxHeight);

		options.inMutable = true;
		options.inJustDecodeBounds = false;
		options.inSampleSize = sampleSize;

		return enableAlpha(decodeBitmapFromUri(resolver, bitmapUri, options));
	}

	public static Bitmap getBitmapFromFile(File bitmapFile) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inMutable = true;
		return enableAlpha(BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options));
	}

	public static Bitmap enableAlpha(Bitmap bitmap) {
		if (bitmap != null) {
			bitmap.setHasAlpha(true);
		}
		return bitmap;
	}
}
