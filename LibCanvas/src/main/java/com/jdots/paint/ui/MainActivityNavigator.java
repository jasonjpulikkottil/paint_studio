package com.jdots.paint.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jdots.paint.FileIO;
import com.jdots.paint.MainActivity;
import com.jdots.paint.R;
import com.jdots.paint.WelcomeActivity;
import com.jdots.paint.colorpicker.ColorPickerDialog;
import com.jdots.paint.colorpicker.OnColorPickedListener;
import com.jdots.paint.common.Constants;
import com.jdots.paint.common.MainActivityConstants;
import com.jdots.paint.contract.MainActivityContracts;
import com.jdots.paint.dialog.AboutDialog;
import com.jdots.paint.dialog.FeedbackDialog;
import com.jdots.paint.dialog.ImportImageDialog;
import com.jdots.paint.dialog.IndeterminateProgressDialog;
import com.jdots.paint.dialog.InfoDialog;
import com.jdots.paint.dialog.JpgInfoDialog;
import com.jdots.paint.dialog.LikeUsDialog;
import com.jdots.paint.dialog.OraInfoDialog;
import com.jdots.paint.dialog.OverwriteDialog;
import com.jdots.paint.dialog.PermanentDenialPermissionInfoDialog;
import com.jdots.paint.dialog.PermissionInfoDialog;
import com.jdots.paint.dialog.PngInfoDialog;
import com.jdots.paint.dialog.RateUsDialog;
import com.jdots.paint.dialog.SaveBeforeFinishDialog;
import com.jdots.paint.dialog.SaveBeforeLoadImageDialog;
import com.jdots.paint.dialog.SaveBeforeNewImageDialog;
import com.jdots.paint.dialog.SaveInformationDialog;
import com.jdots.paint.tools.ToolReference;
import com.jdots.paint.ui.fragments.CatroidMediaGalleryFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

public class MainActivityNavigator implements MainActivityContracts.Navigator {
	private MainActivity mainActivity;
	//private MainIntroActivity mainiActivity;

	private final ToolReference toolReference;
	private AppCompatDialog progressDialog;

	public MainActivityNavigator(MainActivity mainActivity, ToolReference toolReference) {
		this.mainActivity = mainActivity;
		//this.mainiActivity=mainiActivity;
		this.toolReference = toolReference;
	}

	@Override
	public void showColorPickerDialog() {
		if (findFragmentByTag(Constants.COLOR_PICKER_DIALOG_TAG) == null) {
			ColorPickerDialog dialog = ColorPickerDialog.newInstance(toolReference.get().getDrawPaint().getColor());
			setupColorPickerDialogListeners(dialog);
			showDialogFragmentSafely(dialog, Constants.COLOR_PICKER_DIALOG_TAG);
		}
	}

	@Override
	public void showCatroidMediaGallery() {
		if (findFragmentByTag(Constants.CATROID_MEDIA_GALLERY_FRAGMENT_TAG) == null) {
			CatroidMediaGalleryFragment fragment = new CatroidMediaGalleryFragment();
			fragment.setMediaGalleryListener(new CatroidMediaGalleryFragment.MediaGalleryListener() {
				@Override
				public void bitmapLoadedFromSource(Bitmap loadedBitmap) {
					mainActivity.getPresenter().bitmapLoadedFromSource(loadedBitmap);
				}

				@Override
				public void showProgressDialog() {
					showIndeterminateProgressDialog();
				}

				@Override
				public void dissmissProgressDialog() {
					dismissIndeterminateProgressDialog();
				}
			});
			showFragment(fragment, Constants.CATROID_MEDIA_GALLERY_FRAGMENT_TAG);
		}
	}

	private void showFragment(Fragment fragment, String tag) {
		FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_to_top, R.anim.slide_to_bottom, R.anim.slide_to_top, R.anim.slide_to_bottom)
				.addToBackStack(null)
				.add(R.id.fragment_container, fragment, tag)
				.commit();
	}

	private void showDialogFragmentSafely(DialogFragment dialog, String tag) {
		FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
		if (!fragmentManager.isStateSaved()) {
			dialog.show(fragmentManager, tag);
		}
	}

	private Fragment findFragmentByTag(String tag) {
		return mainActivity.getSupportFragmentManager().findFragmentByTag(tag);
	}

	private void setupColorPickerDialogListeners(ColorPickerDialog dialog) {
		dialog.addOnColorPickedListener(new OnColorPickedListener() {
			@Override
			public void colorChanged(int color) {
				toolReference.get().changePaintColor(color);
				mainActivity.getPresenter().setBottomNavigationColor(color);
			}
		});

		dialog.setBitmap(mainActivity.getPresenter().getBitmap());
	}

	private void setupCatroidMediaGalleryListeners(CatroidMediaGalleryFragment dialog) {
		dialog.setMediaGalleryListener(new CatroidMediaGalleryFragment.MediaGalleryListener() {
			@Override
			public void bitmapLoadedFromSource(Bitmap loadedBitmap) {
				mainActivity.getPresenter().bitmapLoadedFromSource(loadedBitmap);
			}

			@Override
			public void showProgressDialog() {
				showIndeterminateProgressDialog();
			}

			@Override
			public void dissmissProgressDialog() {
				dismissIndeterminateProgressDialog();
			}
		});
	}

	private void openPlayStore(String applicationId) {
		Uri uriPlayStore = Uri.parse("market://details?id=" + applicationId);
		Intent openPlayStore = new Intent(Intent.ACTION_VIEW, uriPlayStore);

		try {
			mainActivity.startActivity(openPlayStore);
		} catch (ActivityNotFoundException e) {
			Uri uriNoPlayStore = Uri.parse("http://play.google.com/store/apps/details?id=" + applicationId);
			Intent noPlayStoreInstalled = new Intent(Intent.ACTION_VIEW, uriNoPlayStore);
			mainActivity.startActivity(noPlayStoreInstalled);
		}
	}

	@Override
	public void startLoadImageActivity(@MainActivityConstants.ActivityRequestCode int requestCode) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		mainActivity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startImportImageActivity(@MainActivityConstants.ActivityRequestCode int requestCode) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		mainActivity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startWelcomeActivity(@MainActivityConstants.ActivityRequestCode int requestCode) {
		Intent intent = new Intent(mainActivity.getApplicationContext(), WelcomeActivity.class);
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		mainActivity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startShareImageActivity(Bitmap bitmap) {
		Uri uri = FileIO.saveBitmapToCache(bitmap, mainActivity);
		if (uri != null) {
			Intent shareIntent = new Intent();
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			shareIntent.setDataAndType(uri, mainActivity.getContentResolver().getType(uri));
			shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			shareIntent.setAction(Intent.ACTION_SEND);
			String chooserTitle = mainActivity.getResources().getString(R.string.share_image_via_text);
			mainActivity.startActivity(Intent.createChooser(shareIntent, chooserTitle));
		}
	}

	@Override
	public void showAboutDialog() {
		AboutDialog about = AboutDialog.newInstance();
		about.show(mainActivity.getSupportFragmentManager(), Constants.ABOUT_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showLikeUsDialog() {
		LikeUsDialog likeUsDialog = LikeUsDialog.newInstance();
		likeUsDialog.show(mainActivity.getSupportFragmentManager(), Constants.LIKE_US_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showRateUsDialog() {
		RateUsDialog rateUsDialog = RateUsDialog.newInstance();
		rateUsDialog.show(mainActivity.getSupportFragmentManager(), Constants.RATE_US_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showFeedbackDialog() {
		FeedbackDialog feedbackDialog = FeedbackDialog.newInstance();
		feedbackDialog.show(mainActivity.getSupportFragmentManager(), Constants.FEEDBACK_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showOverwriteDialog(int permissionCode) {
		OverwriteDialog overwriteDialog = OverwriteDialog.newInstance(permissionCode);
		overwriteDialog.show(mainActivity.getSupportFragmentManager(), Constants.OVERWRITE_INFORMATION_DIALOG_TAG);
	}

	@Override
	public void showPngInformationDialog() {
		PngInfoDialog pngInfoDialog = PngInfoDialog.newInstance();
		pngInfoDialog.show(mainActivity.getSupportFragmentManager(), Constants.PNG_INFORMATION_DIALOG_TAG);
	}

	@Override
	public void showJpgInformationDialog() {
		JpgInfoDialog jpgInfoDialog = JpgInfoDialog.newInstance();
		jpgInfoDialog.show(mainActivity.getSupportFragmentManager(), Constants.JPG_INFORMATION_DIALOG_TAG);
	}

	@Override
	public void showOraInformationDialog() {
		OraInfoDialog oraInfoDialog = OraInfoDialog.newInstance();
		oraInfoDialog.show(mainActivity.getSupportFragmentManager(), Constants.ORA_INFORMATION_DIALOG_TAG);
	}

	@Override
	public void sendFeedback() {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		Uri data = Uri.parse("mailto:jdotslab@gmail.com");
		intent.setData(data);
		mainActivity.startActivity(intent);
	}

	@Override
	public void showImageImportDialog() {
		ImportImageDialog importImage = ImportImageDialog.newInstance();
		importImage.show(mainActivity.getSupportFragmentManager(), Constants.ABOUT_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showIndeterminateProgressDialog() {
		if (progressDialog == null) {
			progressDialog = IndeterminateProgressDialog.newInstance(mainActivity);
		}
		progressDialog.show();
	}

	@Override
	public void dismissIndeterminateProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

	@Override
	public void returnToPocketCode(String path) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(Constants.PaintStudio_PICTURE_PATH, path);
		mainActivity.setResult(RESULT_OK, resultIntent);
		mainActivity.finish();
	}

	@Override
	public void showToast(int resId, int duration) {
		ToastFactory.makeText(mainActivity, resId, duration).show();
	}

	@Override
	public void showSaveErrorDialog() {



		AppCompatDialogFragment dialog = InfoDialog.newInstance(InfoDialog.DialogType.WARNING,
				R.string.dialog_error_sdcard_text, R.string.dialog_error_save_title);
		showDialogFragmentSafely(dialog, Constants.SAVE_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showLoadErrorDialog() {
		AppCompatDialogFragment dialog = InfoDialog.newInstance(InfoDialog.DialogType.WARNING,
				R.string.dialog_loading_image_failed_title, R.string.dialog_loading_image_failed_text);
		showDialogFragmentSafely(dialog, Constants.LOAD_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showRequestPermissionRationaleDialog(PermissionInfoDialog.PermissionType permissionType, String[] permissions, int requestCode) {
		AppCompatDialogFragment dialog = PermissionInfoDialog.newInstance(permissionType, permissions, requestCode);
		showDialogFragmentSafely(dialog, Constants.PERMISSION_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void showRequestPermanentlyDeniedPermissionRationaleDialog() {
		AppCompatDialogFragment dialog = PermanentDenialPermissionInfoDialog.newInstance(mainActivity.getName());
		showDialogFragmentSafely(dialog, Constants.PERMISSION_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void askForPermission(String[] permissions, int requestCode) {
		ActivityCompat.requestPermissions(mainActivity, permissions, requestCode);
	}

	@Override
	public boolean isSdkAboveOrEqualM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	@Override
	public boolean isSdkAboveOrEqualQ() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
	}

	@Override
	public boolean doIHavePermission(String permission) {
		return ContextCompat.checkSelfPermission(mainActivity, permission) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public boolean isPermissionPermanentlyDenied(String[] permissions) {
		return !ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, permissions[0]);
	}

	@Override
	public void finishActivity() {



		mainActivity.finish();
	}

	@Override
	public void showSaveBeforeFinishDialog() {
		AppCompatDialogFragment dialog = SaveBeforeFinishDialog.newInstance(
				SaveBeforeFinishDialog.SaveBeforeFinishDialogType.FINISH);
		showDialogFragmentSafely(dialog, Constants.SAVE_QUESTION_FRAGMENT_TAG);
	}

	@Override
	public void showSaveBeforeNewImageDialog() {
		AppCompatDialogFragment dialog = SaveBeforeNewImageDialog.newInstance();
		showDialogFragmentSafely(dialog, Constants.SAVE_QUESTION_FRAGMENT_TAG);
	}

	@Override
	public void showSaveBeforeLoadImageDialog() {
		AppCompatDialogFragment dialog = SaveBeforeLoadImageDialog.newInstance();
		showDialogFragmentSafely(dialog, Constants.SAVE_QUESTION_FRAGMENT_TAG);
	}

	@SuppressLint("VisibleForTests")
	@Override
	public void showSaveImageInformationDialogWhenStandalone(int permissionCode, int imageNumber, boolean isExport) {
		Uri uri = mainActivity.model.getSavedPictureUri();
		if (uri != null && permissionCode != MainActivityConstants.PERMISSION_EXTERNAL_STORAGE_SAVE_COPY) {
			FileIO.parseFileName(uri, mainActivity.getContentResolver());
		}

		if (!isExport && mainActivity.model.isOpenedFromCatroid()) {
			String name = getFileName(uri);
			if (name != null && (name.endsWith("jpg") || name.endsWith("jpeg"))) {
				FileIO.compressFormat = Bitmap.CompressFormat.JPEG;
				FileIO.ending = ".jpg";
			} else if (name != null && name.endsWith("png")) {
				FileIO.compressFormat = Bitmap.CompressFormat.PNG;
				FileIO.ending = ".png";
			} else {
				FileIO.compressFormat = Bitmap.CompressFormat.PNG;
				FileIO.ending = ".png";
			}
			FileIO.filename = "paint_studio_image" + imageNumber;

			FileIO.catroidFlag = true;
			FileIO.isCatrobatImage = false;
			mainActivity.getPresenter().switchBetweenVersions(permissionCode);
			return;
		}

		boolean isStandard = false;
		if (permissionCode == MainActivityConstants.PERMISSION_EXTERNAL_STORAGE_SAVE_COPY) {
			isStandard = true;
		}

		SaveInformationDialog saveInfodialog = SaveInformationDialog.newInstance(permissionCode, imageNumber, isStandard);
		saveInfodialog.show(mainActivity.getSupportFragmentManager(), Constants.SAVE_INFORMATION_DIALOG_TAG);
	}

	public String getFileName(Uri uri) {
		String result = null;
		if (Objects.equals(uri.getScheme(), "content")) {
			Cursor cursor = mainActivity.getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			assert result != null;
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}

	@Override
	public void showToolChangeToast(int offset, int idRes) {
		Toast toolNameToast = ToastFactory.makeText(mainActivity, idRes, Toast.LENGTH_SHORT);
		int gravity = Gravity.TOP | Gravity.CENTER;
		if (mainActivity.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
			offset = 0;
		}
		toolNameToast.setGravity(gravity, 0, offset);
		toolNameToast.show();
	}

	@Override
	public void broadcastAddPictureToGallery(Uri uri) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(uri);
		mainActivity.sendBroadcast(mediaScanIntent);
	}

	@Override
	public void restoreFragmentListeners() {
		Fragment fragment = findFragmentByTag(Constants.COLOR_PICKER_DIALOG_TAG);
		if (fragment != null) {
			setupColorPickerDialogListeners((ColorPickerDialog) fragment);
		}

		fragment = findFragmentByTag(Constants.CATROID_MEDIA_GALLERY_FRAGMENT_TAG);
		if (fragment != null) {
			setupCatroidMediaGalleryListeners((CatroidMediaGalleryFragment) fragment);
		}
	}

	@Override
	public void rateUsClicked() {
		openPlayStore(mainActivity.getPackageName());
	}
}
