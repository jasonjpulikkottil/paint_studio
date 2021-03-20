package com.jdots.paint.ui;

import android.content.Context;

import com.jdots.paint.contract.LayerContracts;

public class LayerNavigator implements LayerContracts.Navigator {
	private Context context;

	public LayerNavigator(Context context) {
		this.context = context;
	}

	@Override
	public void showToast(int id, int length) {
		ToastFactory.makeText(context, id, length).show();
	}
}
