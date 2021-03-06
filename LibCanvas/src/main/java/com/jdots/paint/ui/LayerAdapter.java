package com.jdots.paint.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jdots.paint.contract.LayerContracts;
import com.jdots.paint.controller.DefaultToolController;

import com.jdots.paint.R;

import com.jdots.paint.tools.ToolType;
import com.jdots.paint.ui.viewholder.BottomNavigationViewHolder;

public class LayerAdapter extends BaseAdapter implements LayerContracts.Adapter {
	private final LayerContracts.Presenter presenter;
	private final SparseArray<LayerContracts.LayerViewHolder> viewHolders;

	public LayerAdapter(LayerContracts.Presenter presenter) {
		this.presenter = presenter;
		viewHolders = new SparseArray<>();
	}

	@Override
	public int getCount() {
		return presenter.getLayerCount();
	}

	public LayerContracts.Presenter getPresenter() {
		return this.presenter;
	}

	@Override
	public Object getItem(int position) {
		return presenter.getLayerItem(position);
	}

	@Override
	public long getItemId(int position) {
		return presenter.getLayerItemId(position);
	}

	@Override
	public void notifyDataSetChanged() {
		viewHolders.clear();
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final LayerContracts.LayerViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.paint_studio_item_layer, parent, false);
			viewHolder = new LayerViewHolder(convertView, presenter);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (LayerContracts.LayerViewHolder) convertView.getTag();
		}
		viewHolders.put(position, viewHolder);
		presenter.onBindLayerViewHolderAtPosition(position, viewHolder);
		final CheckBox checkBox = convertView.findViewById(R.id.paint_studio_checkbox_layer);
		checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
					final boolean isChecked = checkBox.isChecked();
					if (isChecked) {
						presenter.unhideLayer(position, viewHolder);
						presenter.getLayerItem(position).setCheckBox(isChecked);
					} else {
						presenter.hideLayer(position);
						presenter.getLayerItem(position).setCheckBox(isChecked);
					}
				}
		});
		return convertView;
	}

	@Override
	public LayerContracts.LayerViewHolder getViewHolderAt(int position) {
		return viewHolders.get(position);
	}

	static class LayerViewHolder implements LayerContracts.LayerViewHolder {
		private final View itemView;
		private final LinearLayout layerBackground;
		private final ImageView imageView;
		private Bitmap bitmap;
		private CheckBox checkBox;
		private LayerContracts.Presenter layerPresenter;

		LayerViewHolder(View itemView, LayerContracts.Presenter layerPresenter) {
			this.itemView = itemView;
			layerBackground = itemView.findViewById(R.id.paint_studio_item_layer_background);
			imageView = itemView.findViewById(R.id.paint_studio_item_layer_image);
			this.checkBox = itemView.findViewById(R.id.paint_studio_checkbox_layer);
			this.layerPresenter = layerPresenter;
		}

		@Override
		public void setSelected(int position, BottomNavigationViewHolder bottomNavigationViewHolder, DefaultToolController defaultToolController) {
			if (!layerPresenter.getLayerItem(position).getCheckBox()) {
				defaultToolController.switchTool(ToolType.HAND, false);
				bottomNavigationViewHolder.showCurrentTool(ToolType.HAND);
			}
			layerBackground.setBackgroundColor(Color.BLUE);
		}

		@Override
		public void setSelected() {
			layerBackground.setBackgroundColor(Color.BLUE);
		}

		@Override
		public void setDeselected() {
			layerBackground.setBackgroundColor(Color.TRANSPARENT);
		}

		@Override
		public void setBitmap(Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
			this.bitmap = bitmap;
		}

		@Override
		public void setCheckBox(boolean setTo) {
			this.checkBox.setChecked(setTo);
		}

		@Override
		public Bitmap getBitmap() {
			return this.bitmap;
		}

		@Override
		public View getView() {
			return itemView;
		}

		@Override
		public void setMergable() {
			layerBackground.setBackgroundColor(Color.YELLOW);
		}
	}
}
