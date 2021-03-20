package com.jdots.paint.ui.tools;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jdots.paint.R;
import com.jdots.paint.tools.options.ToolOptionsViewController;

public class DefaultToolOptionsViewController implements ToolOptionsViewController {
	private final ViewGroup toolSpecificOptionsLayout;
	private final ViewGroup bottomNavigation;
	private final ViewGroup mainToolOptions;
	private final View topBarSpecificViewCheckmark;

	private boolean toolOptionsShown;
	private boolean enabled = true;
	private Callback callback;

	public DefaultToolOptionsViewController(AppCompatActivity activity) {
		bottomNavigation = activity.findViewById(R.id.paint_studio_main_bottom_navigation);
		mainToolOptions = activity.findViewById(R.id.paint_studio_main_tool_options);
		toolSpecificOptionsLayout = activity.findViewById(R.id.paint_studio_layout_tool_specific_options);
		topBarSpecificViewCheckmark = activity.findViewById(R.id.paint_studio_btn_top_checkmark);

		mainToolOptions.setVisibility(View.INVISIBLE);
	}

	@Override
	public void resetToOrigin() {
		toolOptionsShown = false;
		mainToolOptions.setVisibility(View.INVISIBLE);
		mainToolOptions.setY(bottomNavigation.getY() + bottomNavigation.getHeight());
	}

	@Override
	public void hide() {
		if (!enabled) {
			return;
		}

		toolOptionsShown = false;
		mainToolOptions.animate().y(bottomNavigation.getY() + bottomNavigation.getHeight());
		notifyHide();
	}

	@Override
	public void disable() {
		enabled = false;

		if (isVisible()) {
			resetToOrigin();
		}
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public void show() {
		if (!enabled) {
			return;
		}

		toolOptionsShown = true;
		mainToolOptions.setVisibility(View.INVISIBLE);
		mainToolOptions.post(new Runnable() {
			@Override
			public void run() {
				float yPos = bottomNavigation.getY() - mainToolOptions.getHeight();
				mainToolOptions.animate().y(yPos);
				mainToolOptions.setVisibility(View.VISIBLE);
			}
		});

		notifyShow();
	}

	@Override
	public void showDelayed() {
		toolSpecificOptionsLayout.post(new Runnable() {
			@Override
			public void run() {
				show();
			}
		});
	}

	private void notifyHide() {
		if (callback != null) {
			callback.onHide();
		}
	}

	private void notifyShow() {
		if (callback != null) {
			callback.onShow();
		}
	}

	@Override
	public void removeToolViews() {
		toolSpecificOptionsLayout.removeAllViews();
		callback = null;
	}

	@Override
	public boolean isVisible() {
		return toolOptionsShown;
	}

	@Override
	public void setCallback(@Nullable Callback callback) {
		this.callback = callback;
	}

	@Override
	public ViewGroup getToolSpecificOptionsLayout() {
		return toolSpecificOptionsLayout;
	}

	@Override
	public void showCheckmark() {
		topBarSpecificViewCheckmark.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideCheckmark() {
		topBarSpecificViewCheckmark.setVisibility(View.GONE);
	}
}
