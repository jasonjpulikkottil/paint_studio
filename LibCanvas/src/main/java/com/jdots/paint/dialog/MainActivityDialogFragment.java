package com.jdots.paint.dialog;

import android.os.Bundle;

import com.jdots.paint.contract.MainActivityContracts;

import androidx.appcompat.app.AppCompatDialogFragment;

public class MainActivityDialogFragment extends AppCompatDialogFragment {
	private MainActivityContracts.Presenter presenter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		MainActivityContracts.MainView activity = (MainActivityContracts.MainView) getActivity();
		if (activity == null) {
			throw new IllegalArgumentException("Parent activity must implement MainActivityContracts.MainView");
		}
		presenter = activity.getPresenter();
	}

	public MainActivityContracts.Presenter getPresenter() {
		return presenter;
	}
}
