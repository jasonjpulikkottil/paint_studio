package com.jdots.paint.ui.viewholder;

import com.jdots.paint.contract.MainActivityContracts;

import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerLayoutViewHolder implements MainActivityContracts.DrawerLayoutViewHolder {
	public final DrawerLayout drawerLayout;

	public DrawerLayoutViewHolder(DrawerLayout drawerLayout) {
		this.drawerLayout = drawerLayout;
	}

	@Override
	public void closeDrawer(int gravity, boolean animate) {
		drawerLayout.closeDrawer(gravity, animate);
	}

	@Override
	public boolean isDrawerOpen(int gravity) {
		return drawerLayout.isDrawerOpen(gravity);
	}

	@Override
	public void openDrawer(int gravity) {
		drawerLayout.openDrawer(gravity);
	}
}
