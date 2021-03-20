
package com.jdots.paint.iotasks;

import android.graphics.Bitmap;

import java.util.List;

public class BitmapReturnValue {
	public List<Bitmap> bitmapList;
	public Bitmap bitmap;

	public BitmapReturnValue(List<Bitmap> list, Bitmap singleBitmap) {
		bitmapList = list;
		bitmap = singleBitmap;
	}
}
