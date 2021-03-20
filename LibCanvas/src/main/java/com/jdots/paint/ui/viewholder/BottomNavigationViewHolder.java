package com.jdots.paint.ui.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.jdots.paint.contract.MainActivityContracts;
import com.jdots.paint.ui.BottomNavigationLandscape;
import com.jdots.paint.ui.BottomNavigationPortrait;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.jdots.paint.R;

import com.jdots.paint.tools.ToolType;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

public class BottomNavigationViewHolder implements MainActivityContracts.BottomNavigationViewHolder {
	private final View layout;
	private final BottomNavigationView bottomNavigationView;
	private int orientation;
	private MainActivityContracts.BottomNavigationAppearance bottomNavigation;
	private ImageView colorButton;

	public BottomNavigationViewHolder(View layout, int orientation, Context context) {
		this.layout = layout;
		this.bottomNavigationView = layout.findViewById(R.id.paint_studio_bottom_navigation);
		this.orientation = orientation;

		setAppearance(context);

		BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
		BottomNavigationItemView item = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(2);
		colorButton = item.findViewById(R.id.icon);

		initColorButton();
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
	public void showCurrentTool(ToolType toolType) {
		bottomNavigation.showCurrentTool(toolType);
	}

	@Override
	public void setColorButtonColor(int color) {
		colorButton.setColorFilter(color);
	}

	private void initColorButton() {
		colorButton.setScaleType(ImageView.ScaleType.FIT_XY);
		colorButton.setBackgroundColor(Color.WHITE);
		colorButton.setPadding(2, 2, 2, 2);
	}

	public BottomNavigationView getBottomNavigationView() {
		return bottomNavigationView;
	}

	private void setAppearance(Context context) {
		if (orientation == SCREEN_ORIENTATION_PORTRAIT) {
			bottomNavigation = new BottomNavigationPortrait(bottomNavigationView);
		} else {
			bottomNavigation = new BottomNavigationLandscape(context, bottomNavigationView);
		}
	}
}
