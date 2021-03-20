
package com.jdots.paint.web;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jdots.paint.R;

public class MediaGalleryWebViewClient extends WebViewClient {
	private ProgressDialog webViewLoadingDialog;
	private WebClientCallback callback;

	public interface WebClientCallback {
		void finish();
	}

	public MediaGalleryWebViewClient(WebClientCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void onPageStarted(WebView view, String urlClient, Bitmap favicon) {
		if (webViewLoadingDialog == null) {
			webViewLoadingDialog = new ProgressDialog(view.getContext(), R.style.WebViewLoadingCircle);
			webViewLoadingDialog.setCancelable(true);
			webViewLoadingDialog.setCanceledOnTouchOutside(false);
			webViewLoadingDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			webViewLoadingDialog.show();
		} else {
			callback.finish();
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (webViewLoadingDialog != null) {
			webViewLoadingDialog.dismiss();
			webViewLoadingDialog = null;
		}
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return false;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		callback.finish();
	}
}
