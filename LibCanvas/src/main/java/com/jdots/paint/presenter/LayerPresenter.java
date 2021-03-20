package com.jdots.paint.presenter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jdots.paint.command.CommandFactory;
import com.jdots.paint.command.CommandManager;
import com.jdots.paint.common.Constants;
import com.jdots.paint.contract.LayerContracts;

import com.jdots.paint.R;

import com.jdots.paint.controller.DefaultToolController;
import com.jdots.paint.tools.ToolType;
import com.jdots.paint.ui.DrawingSurface;
import com.jdots.paint.ui.dragndrop.DragAndDropPresenter;
import com.jdots.paint.ui.dragndrop.ListItemLongClickHandler;
import com.jdots.paint.ui.viewholder.BottomNavigationViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LayerPresenter implements LayerContracts.Presenter, DragAndDropPresenter {
	private static final String TAG = LayerPresenter.class.getSimpleName();
	private final CommandManager commandManager;
	private final CommandFactory commandFactory;
	private final LayerContracts.Model model;
	private ListItemLongClickHandler listItemLongClickHandler;
	private LayerContracts.LayerMenuViewHolder layerMenuViewHolder;
	private LayerContracts.Adapter adapter;
	private List<LayerContracts.Layer> layers;
	private LayerContracts.Navigator navigator;
	private DrawingSurface drawingSurface;
	private DefaultToolController defaultToolController;
	private BottomNavigationViewHolder bottomNavigationViewHolder;

	public LayerPresenter(LayerContracts.Model model, ListItemLongClickHandler listItemLongClickHandler, LayerContracts.LayerMenuViewHolder layerMenuViewHolder, CommandManager commandManager, CommandFactory commandFactory, LayerContracts.Navigator navigator) {
		this.model = model;
		this.listItemLongClickHandler = listItemLongClickHandler;
		this.layerMenuViewHolder = layerMenuViewHolder;
		this.commandManager = commandManager;
		this.commandFactory = commandFactory;
		this.layers = new ArrayList<>(model.getLayers());
		this.navigator = navigator;
	}

	@Override
	public LayerPresenter getPresenter() {
		return this;
	}

	@Override
	public void setAdapter(LayerContracts.Adapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void setDrawingSurface(DrawingSurface drawingSurface) {
		this.drawingSurface = drawingSurface;
	}

	@Override
	public void setDefaultToolController(DefaultToolController defaultToolController) {
		this.defaultToolController = defaultToolController;
	}

	@Override
	public void setbottomNavigationViewHolder(BottomNavigationViewHolder bottomNavigationViewHolder) {
		this.bottomNavigationViewHolder = bottomNavigationViewHolder;
	}

	@Override
	public void onBindLayerViewHolderAtPosition(int position, LayerContracts.LayerViewHolder viewHolder) {
		LayerContracts.Layer layer = getLayerItem(position);

		if (layer == model.getCurrentLayer()) {
			viewHolder.setSelected(position, bottomNavigationViewHolder, defaultToolController);
		} else {
			viewHolder.setDeselected();
		}
		if (!layers.get(position).getCheckBox()) {
			viewHolder.setBitmap(layer.getTransparentBitmap());
			viewHolder.setCheckBox(false);
		} else {
			viewHolder.setBitmap(layer.getBitmap());
			viewHolder.setCheckBox(true);
		}
	}

	@Override
	public void refreshLayerMenuViewHolder() {
		if (getLayerCount() < Constants.MAX_LAYERS) {
			layerMenuViewHolder.enableAddLayerButton();
		} else {
			layerMenuViewHolder.disableAddLayerButton();
		}

		if (getLayerCount() > 1) {
			layerMenuViewHolder.enableRemoveLayerButton();
		} else {
			layerMenuViewHolder.disableRemoveLayerButton();
		}
	}

	@Override
	public int getLayerCount() {
		return layers.size();
	}

	@Override
	public LayerContracts.Layer getLayerItem(int position) {
		return layers.get(position);
	}

	@Override
	public long getLayerItemId(int position) {
		return position;
	}

	@Override
	public void addLayer() {
		if (getLayerCount() < Constants.MAX_LAYERS) {
			commandManager.addCommand(commandFactory.createAddLayerCommand());
		} else {
			navigator.showToast(R.string.layer_too_many_layers, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void removeLayer() {
		if (getLayerCount() > 1) {
			LayerContracts.Layer layerToDelete = model.getCurrentLayer();
			int index = model.getLayerIndexOf(layerToDelete);
			commandManager.addCommand(commandFactory.createRemoveLayerCommand(index));
		}
	}

	@Override
	public void hideLayer(int position) {
		LayerContracts.Layer destinationLayer = model.getLayerAt(position);
		Bitmap bitmapCopy = destinationLayer.getTransparentBitmap();
		destinationLayer.switchBitmaps(false);
		destinationLayer.setBitmap(bitmapCopy);
		destinationLayer.setCheckBox(false);

		drawingSurface.refreshDrawingSurface();

		if (model.getCurrentLayer().equals(destinationLayer)) {
			defaultToolController.switchTool(ToolType.HAND, false);
			bottomNavigationViewHolder.showCurrentTool(ToolType.HAND);
		}
	}

	@Override
	public void unhideLayer(int position, LayerContracts.LayerViewHolder viewHolder) {
		LayerContracts.Layer destinationLayer = model.getLayerAt(position);
		destinationLayer.switchBitmaps(true);
		Bitmap bitmapToAdd = destinationLayer.getBitmap();
		destinationLayer.setBitmap(bitmapToAdd);
		destinationLayer.setCheckBox(true);

		viewHolder.setBitmap(bitmapToAdd);

		drawingSurface.refreshDrawingSurface();

		if (model.getCurrentLayer().equals(destinationLayer)) {
			defaultToolController.switchTool(ToolType.BRUSH, false);
			bottomNavigationViewHolder.showCurrentTool(ToolType.BRUSH);
		}
	}

	@Override
	public int swapItemsVisually(int position, int swapWith) {
		LayerContracts.Layer tempLayer = layers.get(position);
		LayerContracts.Layer swapWithLayer = layers.get(swapWith);
		layers.set(position, swapWithLayer);
		layers.set(swapWith, tempLayer);
		return swapWith;
	}

	@Override
	public void mergeItems(int position, int mergeWith) {
		LayerContracts.Layer actualLayer = layers.get(mergeWith);
		int actualPosition = model.getLayerIndexOf(actualLayer);
		if (position != actualPosition) {
			commandManager.addCommand(commandFactory.createMergeLayersCommand(position, actualPosition));

			navigator.showToast(R.string.layer_merged, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void reorderItems(int position, int targetPosition) {
		if (position != targetPosition) {
			commandManager.addCommand(commandFactory.createReorderLayersCommand(position, targetPosition));
		}
	}

	@Override
	public void markMergeable(int position, int mergeWith) {
		if (!isPositionValid(position) || !isPositionValid(mergeWith)) {
			Log.e(TAG, "onLongClickLayerAtPosition at invalid position");
			return;
		}
		adapter.getViewHolderAt(mergeWith).setMergable();
	}

	@Override
	public void onLongClickLayerAtPosition(int position, View view) {
		if (!isPositionValid(position)) {
			Log.e(TAG, "onLongClickLayerAtPosition at invalid position");
			return;
		}
		boolean isAllowedToLongclick = true;
		for (int i = 0; i < layers.size(); i++) {
			if (!layers.get(i).getCheckBox()) {
				isAllowedToLongclick = false;
			}
		}
		if (isAllowedToLongclick) {
			if (getLayerCount() > 1) {
				listItemLongClickHandler.handleOnItemLongClick(position, view);
			}
		} else {
			navigator.showToast(R.string.no_longclick_on_hidden_layer, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onClickLayerAtPosition(int position, View view) {
		if (!isPositionValid(position)) {
			Log.e(TAG, "onClickLayerAtPosition at invalid position");
			return;
		}

		if (position != model.getLayerIndexOf(model.getCurrentLayer())) {
			commandManager.addCommand(commandFactory.createSelectLayerCommand(position));
		}
	}

	private boolean isPositionValid(int position) {
		return position >= 0 && position < layers.size();
	}

	@Override
	public void invalidate() {
		synchronized (model) {
			layers.clear();
			layers.addAll(model.getLayers());
		}
		refreshLayerMenuViewHolder();
		adapter.notifyDataSetChanged();

		listItemLongClickHandler.stopDragging();
	}
}
