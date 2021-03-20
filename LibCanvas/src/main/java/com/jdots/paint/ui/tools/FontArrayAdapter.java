package com.jdots.paint.ui.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jdots.paint.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class FontArrayAdapter extends ArrayAdapter<String> {
	private Typeface sansSerif = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
	private Typeface serif = Typeface.create(Typeface.SERIF, Typeface.NORMAL);
	private Typeface monospace = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
	private Typeface stc = ResourcesCompat.getFont(getContext(), R.font.stc_regular);
	private Typeface dubai = ResourcesCompat.getFont(getContext(), R.font.dubai);

	private final Typeface[] typeFaces = {
			sansSerif,
			monospace,
			serif,
			dubai,
			stc,
	};

	public FontArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		TextView spinnerText = (TextView) super.getDropDownView(position, convertView, parent);
		spinnerText.setTypeface(typeFaces[position]);
		return spinnerText;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		TextView spinnerText = (TextView) super.getView(position, convertView, parent);
		spinnerText.setTypeface(typeFaces[position]);
		return spinnerText;
	}
}
