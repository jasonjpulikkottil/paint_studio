package com.jdots.paint.ui.viewholder;

import android.view.View;
import android.view.ViewGroup;

import com.jdots.paint.contract.LayerContracts;

import com.jdots.paint.R;

public class LayerMenuViewHolder implements LayerContracts.LayerMenuViewHolder {
	public final View layerAddButton;
	public final View layerDeleteButton;

	public LayerMenuViewHolder(ViewGroup layerLayout) {
		layerAddButton = layerLayout.findViewById(R.id.paint_studio_layer_side_nav_button_add);
		layerDeleteButton = layerLayout.findViewById(R.id.paint_studio_layer_side_nav_button_delete);
	}

	@Override
	public void disableAddLayerButton() {
		layerAddButton.setEnabled(false);
	}

	@Override
	public void enableAddLayerButton() {
		layerAddButton.setEnabled(true);
	}

	@Override
	public void disableRemoveLayerButton() {
		layerDeleteButton.setEnabled(false);
	}

	@Override
	public void enableRemoveLayerButton() {
		layerDeleteButton.setEnabled(true);
	}
}
