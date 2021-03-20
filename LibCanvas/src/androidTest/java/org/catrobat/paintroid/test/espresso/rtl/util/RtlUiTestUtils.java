/**

 */package com.jdots.paint.test.espresso.rtl.util;

public final class RtlUiTestUtils {
	private RtlUiTestUtils() {
		throw new AssertionError();
	}

	public static boolean checkTextDirection(String string) {
		return Character.getDirectionality(string.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT
				|| Character.getDirectionality(string.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
	}
}
