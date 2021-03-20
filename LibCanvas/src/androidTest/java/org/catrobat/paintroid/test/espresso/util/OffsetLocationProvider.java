/*

 */package com.jdots.paint.test.espresso.util;

import android.view.View;

import androidx.test.espresso.action.CoordinatesProvider;

public class OffsetLocationProvider implements CoordinatesProvider {
	private final CoordinatesProvider locationProvider;
	private final int xOffset;
	private final int yOffset;

	public OffsetLocationProvider(CoordinatesProvider locationProvider, int xOffset, int yOffset) {

		this.locationProvider = locationProvider;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public static CoordinatesProvider withOffset(CoordinatesProvider locationProvider, int xOffset, int yOffset) {
		return new OffsetLocationProvider(locationProvider, xOffset, yOffset);
	}

	@Override
	public float[] calculateCoordinates(View view) {
		float[] coordinates = locationProvider.calculateCoordinates(view);
		coordinates[0] += xOffset;
		coordinates[1] += yOffset;
		return coordinates;
	}
}
