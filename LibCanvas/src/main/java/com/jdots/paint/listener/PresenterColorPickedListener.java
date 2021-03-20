package com.jdots.paint.listener;

import com.jdots.paint.colorpicker.OnColorPickedListener;
import com.jdots.paint.contract.MainActivityContracts;

public class PresenterColorPickedListener implements OnColorPickedListener {
	private final MainActivityContracts.Presenter presenter;

	public PresenterColorPickedListener(MainActivityContracts.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void colorChanged(int color) {
		presenter.setBottomNavigationColor(color);
	}
}
