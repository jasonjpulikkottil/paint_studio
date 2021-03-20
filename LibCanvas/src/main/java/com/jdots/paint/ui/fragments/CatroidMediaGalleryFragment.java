
package com.jdots.paint.ui.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.jdots.paint.common.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import com.jdots.paint.R;
import com.jdots.paint.web.MediaGalleryWebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CatroidMediaGalleryFragment extends Fragment implements MediaGalleryWebViewClient.WebClientCallback {
	private WebView webView;
	private MediaGalleryListener listener;

	public interface MediaGalleryListener {
		void bitmapLoadedFromSource(Bitmap loadedBitmap);

		void showProgressDialog();

		void dissmissProgressDialog();
	}

	public void setMediaGalleryListener(MediaGalleryListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_paint_studio_webview, container, false);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		webView = view.findViewById(R.id.webview);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new MediaGalleryWebViewClient(this));
		webView.getSettings().setUserAgentString("JDotsLab");
		webView.loadUrl(Constants.MEDIA_GALLEY_URL);

		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
				listener.showProgressDialog();
				imageLoader.loadImage(url, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if (loadedImage != null) {
							listener.bitmapLoadedFromSource(loadedImage);
						}
						listener.dissmissProgressDialog();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						listener.dissmissProgressDialog();
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						listener.dissmissProgressDialog();
					}
				});
				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		webView.setDownloadListener(null);
		webView.destroy();
		super.onDestroy();
	}

	@Override
	public void finish() {
		getActivity().getSupportFragmentManager().popBackStack();
	}
}
