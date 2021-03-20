package com.jdots.paint.model;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.jdots.paint.contract.LayerContracts;

public class Layer implements LayerContracts.Layer {
	private Bitmap bitmap;
	private Bitmap transparentBitmap;
	private boolean checkBox;

	public Layer(Bitmap bitmap) {
		this.bitmap = bitmap;
		if (bitmap != null) {
			this.transparentBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Bitmap.Config.ARGB_8888);
		}
		this.checkBox = true;
	}

	public void setCheckBox(boolean setTo) {
		this.checkBox = setTo;
	}

	public boolean getCheckBox() {
		return checkBox;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public Bitmap getTransparentBitmap() {
		return transparentBitmap;
	}

	public void switchBitmaps(boolean isUnhide) {
		Bitmap tmpBitmap = transparentBitmap.copy(transparentBitmap.getConfig(), transparentBitmap.isMutable());
		this.transparentBitmap = bitmap;
		this.bitmap = tmpBitmap;
		if (isUnhide) {
			transparentBitmap.eraseColor(Color.TRANSPARENT);
		}
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
