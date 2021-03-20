package com.jdots.paint.tools.options;

public interface TextToolOptionsView {
	void setState(boolean bold, boolean italic, boolean underlined, String text, int textSize, String font);

	void setCallback(Callback listener);

	interface Callback {
		void setText(String text);

		void setFont(String font);

		void setUnderlined(boolean underlined);

		void setItalic(boolean italic);

		void setBold(boolean bold);

		void setTextSize(int size);

		void hideToolOptions();
	}
}
