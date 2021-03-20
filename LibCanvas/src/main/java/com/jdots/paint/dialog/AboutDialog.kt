
package com.jdots.paint.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.jdots.paint.R
import com.jdots.paint.colorpicker.BuildConfig

class AboutDialog : AppCompatDialogFragment() {
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return if (showsDialog) {
			super.onCreateView(inflater, container, savedInstanceState)
		} else {
			inflater.inflate(R.layout.dialog_paint_studio_about, container, false)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val aboutVersionView = view.findViewById<TextView>(R.id.paint_studio_about_version)
		val aboutContentView = view.findViewById<TextView>(R.id.paint_studio_about_content)
		//val aboutLicenseView = view.findViewById<TextView>(R.id.paint_studio_about_license_url)
		//val aboutCatrobatView = view.findViewById<TextView>(R.id.paint_studio_about_catrobat_url)
		val aboutVersion = getString(R.string.paint_studio_about_version, BuildConfig.VERSION_NAME)
		aboutVersionView.text = aboutVersion
		val aboutContent = getString(R.string.paint_studio_about_content,
				getString(R.string.paint_studio_about_license))
		aboutContentView.text = aboutContent
		val licenseUrl = getString(R.string.paint_studio_about_url_license,
				getString(R.string.paint_studio_about_url_license_description))
		//aboutLicenseView.text = HtmlCompat.fromHtml(licenseUrl, HtmlCompat.FROM_HTML_MODE_LEGACY)
		//aboutLicenseView.movementMethod = LinkMovementMethod.getInstance()
		val catrobatUrl = getString(R.string.paint_studio_about_url_catrobat,
				getString(R.string.paint_studio_about_url_catrobat_description))
		//aboutCatrobatView.text = HtmlCompat.fromHtml(catrobatUrl, HtmlCompat.FROM_HTML_MODE_LEGACY)
		//aboutCatrobatView.movementMethod = LinkMovementMethod.getInstance()
	}

	@SuppressLint("InflateParams")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val inflater = requireActivity().layoutInflater
		val layout = inflater.inflate(R.layout.dialog_paint_studio_about, null)
		onViewCreated(layout, savedInstanceState)
		return AlertDialog.Builder(requireContext(), R.style.paint_studioAlertDialog)
				.setTitle(R.string.paint_studio_about_title)
				.setView(layout)
				.setPositiveButton(R.string.done) { _, _ -> dismiss() }
				.create()
	}

	companion object {
		@JvmStatic
		fun newInstance(): AboutDialog = AboutDialog()
	}
}