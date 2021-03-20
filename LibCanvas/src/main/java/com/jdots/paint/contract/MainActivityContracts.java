package com.jdots.paint.contract;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Menu;

import com.jdots.paint.dialog.PermissionInfoDialog;
import com.jdots.paint.iotasks.CreateFileAsync;
import com.jdots.paint.iotasks.LoadImageAsync;
import com.jdots.paint.iotasks.SaveImageAsync;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.tools.Workspace;
import com.jdots.paint.ui.LayerAdapter;

import java.io.File;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.jdots.paint.common.MainActivityConstants;

public interface MainActivityContracts {
	interface Navigator {
		void showColorPickerDialog();

		void startLoadImageActivity(@MainActivityConstants.ActivityRequestCode int requestCode);

		void startImportImageActivity(@MainActivityConstants.ActivityRequestCode int requestCode);

		void showAboutDialog();

		void showLikeUsDialog();

		void showRateUsDialog();

		void showFeedbackDialog();

		void showOverwriteDialog(int permissionCode);

		void showPngInformationDialog();

		void showJpgInformationDialog();

		void showOraInformationDialog();

		void sendFeedback();

		void startWelcomeActivity(@MainActivityConstants.ActivityRequestCode int requestCode);

		void startShareImageActivity(Bitmap bitmap);

		void showIndeterminateProgressDialog();

		void dismissIndeterminateProgressDialog();

		void returnToPocketCode(String path);

		void showToast(@StringRes int resId, int duration);

		void showSaveErrorDialog();

		void showLoadErrorDialog();

		void showRequestPermissionRationaleDialog(PermissionInfoDialog.PermissionType permissionType, String[] permissions, int requestCode);

		void showRequestPermanentlyDeniedPermissionRationaleDialog();

		void askForPermission(String[] permissions, int requestCode);

		boolean isSdkAboveOrEqualM();

		boolean isSdkAboveOrEqualQ();

		boolean doIHavePermission(String permission);

		boolean isPermissionPermanentlyDenied(String[] permission);

		void finishActivity();

		void showSaveBeforeFinishDialog();

		void showSaveBeforeNewImageDialog();

		void showSaveBeforeLoadImageDialog();

		void showSaveImageInformationDialogWhenStandalone(int permissionCode, int imageNumber, boolean isExport);

		void restoreFragmentListeners();

		void showToolChangeToast(int offset, int idRes);

		void broadcastAddPictureToGallery(Uri uri);

		void rateUsClicked();

		void showImageImportDialog();

		void showCatroidMediaGallery();
	}

	interface MainView {
		Presenter getPresenter();

		boolean isFinishing();

		ContentResolver getContentResolver();

		DisplayMetrics getDisplayMetrics();

		void initializeActionBar(boolean isOpenedFromCatroid);

		void superHandleActivityResult(int requestCode, int resultCode, Intent data);

		void superHandleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

		Uri getUriFromFile(File file);

		void hideKeyboard();

		boolean isKeyboardShown();

		void refreshDrawingSurface();

		void enterFullscreen();

		void exitFullscreen();
	}

	interface Presenter {
		void initializeFromCleanState(String extraPicturePath, String extraPictureName);

		void restoreState(boolean isFullscreen, boolean isSaved, boolean isOpenedFromCatroid,
				boolean wasInitialAnimationPlayed, @Nullable Uri savedPictureUri, @Nullable Uri cameraImageUri);

		void finishInitialize();

		void removeMoreOptionsItems(Menu menu);

		void loadImageClicked();

		void loadNewImage();

		void newImageClicked();

		void discardImageClicked();

		void saveCopyClicked(boolean isExport);

		void saveImageClicked();

		void shareImageClicked();

		void enterFullscreenClicked();

		void exitFullscreenClicked();

		void backToPocketCodeClicked();

		void showHelpClicked();

		void showAboutClicked();

		void showRateUsDialog();

		void showFeedbackDialog();

		void showOverwriteDialog(int permissionCode);

		void showPngInformationDialog();

		void showJpgInformationDialog();

		void showOraInformationDialog();

		void sendFeedback();

		void onNewImage();

		void switchBetweenVersions(int requestCode);

		void handleActivityResult(int requestCode, int resultCode, Intent data);

		void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

		void onBackPressed();

		void saveImageConfirmClicked(int requestCode, Uri uri);

		void saveCopyConfirmClicked(int requestCode);

		void undoClicked();

		void redoClicked();

		void showColorPickerClicked();

		void showLayerMenuClicked();

		void onCommandPostExecute();

		void setBottomNavigationColor(int color);

		void onCreateTool();

		void toolClicked(ToolType toolType);

		void saveBeforeLoadImage();

		void saveBeforeNewImage();

		void saveBeforeFinish();

		void finishActivity();

		void actionToolsClicked();

		void actionCurrentToolClicked();

		void rateUsClicked();

		void importFromGalleryClicked();

		void showImportDialog();

		void importStickersClicked();

		void bitmapLoadedFromSource(Bitmap loadedImage);

		void setLayerAdapter(LayerAdapter layerAdapter);

		int getImageNumber();

		Bitmap getBitmap();
	}

	interface Model {
		Uri getCameraImageUri();

		void setCameraImageUri(Uri cameraImageUri);

		Uri getSavedPictureUri();

		void setSavedPictureUri(Uri savedPictureUri);

		boolean isSaved();

		void setSaved(boolean saved);

		boolean isFullscreen();

		void setFullscreen(boolean fullscreen);

		boolean isOpenedFromCatroid();

		void setOpenedFromCatroid(boolean openedFromCatroid);

		boolean wasInitialAnimationPlayed();

		void setInitialAnimationPlayed(boolean wasInitialAnimationPlayed);
	}

	interface Interactor {
		void saveCopy(SaveImageAsync.SaveImageCallback callback, int requestCode, Workspace workspace);

		void createFile(CreateFileAsync.CreateFileCallback callback, int requestCode, @Nullable String filename);

		void saveImage(SaveImageAsync.SaveImageCallback callback, int requestCode, Workspace workspace, Uri uri);

		void loadFile(LoadImageAsync.LoadImageCallback callback, int requestCode, Uri uri);

		void loadFile(LoadImageAsync.LoadImageCallback callback, int requestCode, int maxWidth, int maxHeight, Uri uri);
	}

	interface TopBarViewHolder {
		void enableUndoButton();

		void disableUndoButton();

		void enableRedoButton();

		void disableRedoButton();

		void hide();

		void show();

		int getHeight();

		void removeStandaloneMenuItems(Menu menu);

		void removeCatroidMenuItems(Menu menu);

		void hideTitleIfNotStandalone();
	}

	interface DrawerLayoutViewHolder {

		void closeDrawer(int gravity, boolean animate);

		boolean isDrawerOpen(int gravity);

		void openDrawer(int gravity);
	}

	interface BottomBarViewHolder {
		void show();

		void hide();

		boolean isVisible();
	}

	interface BottomNavigationViewHolder {
		void show();

		void hide();

		void showCurrentTool(ToolType toolType);

		void setColorButtonColor(@ColorInt int color);
	}

	interface BottomNavigationAppearance {
		void showCurrentTool(ToolType toolType);
	}
}
