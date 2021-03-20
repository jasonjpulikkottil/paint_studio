package com.jdots.paint.ui.viewholder;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jdots.paint.R;
import com.jdots.paint.contract.MainActivityContracts;

import androidx.appcompat.widget.Toolbar;

public class TopBarViewHolder implements MainActivityContracts.TopBarViewHolder {
	public final Toolbar toolbar;
	public final ImageButton undoButton;
	public final ImageButton redoButton;
	public final ImageButton checkmarkButton;
	public final ViewGroup layout;

	public TopBarViewHolder(ViewGroup layout) {
		this.layout = layout;
		toolbar = layout.findViewById(R.id.paint_studio_toolbar);
		undoButton = layout.findViewById(R.id.paint_studio_btn_top_undo);
		redoButton = layout.findViewById(R.id.paint_studio_btn_top_redo);
		checkmarkButton = layout.findViewById(R.id.paint_studio_btn_top_checkmark);
	}

	@Override
	public void enableUndoButton() {
		undoButton.setEnabled(true);
	}

	@Override
	public void disableUndoButton() {
		undoButton.setEnabled(false);
	}

	@Override
	public void enableRedoButton() {
		redoButton.setEnabled(true);
	}

	@Override
	public void disableRedoButton() {
		redoButton.setEnabled(false);
	}

	@Override
	public void hide() {
		layout.setVisibility(View.GONE);
	}

	@Override
	public void show() {
		layout.setVisibility(View.VISIBLE);
	}

	@Override
	public int getHeight() {
		return layout.getHeight();
	}

	@Override
	public void removeStandaloneMenuItems(Menu menu) {
		menu.removeItem(R.id.paint_studio_options_save_image);
		menu.removeItem(R.id.paint_studio_options_save_duplicate);
		menu.removeItem(R.id.paint_studio_options_new_image);
		//menu.removeItem(R.id.paint_studio_options_rate_us);
	}

	@Override
	public void removeCatroidMenuItems(Menu menu) {
		menu.removeItem(R.id.paint_studio_options_export);
		menu.removeItem(R.id.paint_studio_options_discard_image);
	}

	@Override
	public void hideTitleIfNotStandalone() {
		toolbar.setTitle("");
	}
}
