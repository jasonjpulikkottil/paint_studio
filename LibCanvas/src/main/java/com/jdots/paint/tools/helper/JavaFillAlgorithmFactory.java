package com.jdots.paint.tools.helper;

public class JavaFillAlgorithmFactory implements FillAlgorithmFactory {
	@Override
	public FillAlgorithm createFillAlgorithm() {
		return new JavaFillAlgorithm();
	}
}
