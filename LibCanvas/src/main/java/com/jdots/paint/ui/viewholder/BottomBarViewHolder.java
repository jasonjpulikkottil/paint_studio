package com.jdots.paint.ui.viewholder;

import android.view.View;

import com.jdots.paint.contract.MainActivityContracts;

public class BottomBarViewHolder implements MainActivityContracts.BottomBarViewHolder {
	public final View layout;

	public BottomBarViewHolder(View layout) {
		this.layout = layout;
	}

	@Override
	public void show() {
		layout.setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		layout.setVisibility(View.GONE);
	}

	@Override
	public boolean isVisible() {
		return layout.getVisibility() == View.VISIBLE;
	}
}

