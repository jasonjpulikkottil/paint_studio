package com.jdots.paint.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.jdots.paint.R

class ImportImageDialog : MainActivityDialogFragment() {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val importGallery = view.findViewById<LinearLayout>(R.id.paint_studio_dialog_import_gallery)
		val importStickers = view.findViewById<LinearLayout>(R.id.paint_studio_dialog_import_stickers)
		importGallery.setOnClickListener {
			presenter.importFromGalleryClicked()
			dismiss()
		}
		importStickers.setOnClickListener {
			presenter.importStickersClicked()
			dismiss()
		}
	}

	@SuppressLint("InflateParams")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val inflater = requireActivity().layoutInflater
		val layout = inflater.inflate(R.layout.dialog_paint_studio_import_image, null)
		onViewCreated(layout, savedInstanceState)
		return AlertDialog.Builder(requireContext(), R.style.paint_studioAlertDialog)
				.setTitle(R.string.dialog_import_image_title)
				.setView(layout)
				.setNegativeButton(R.string.paint_studio_cancel) { _, _ -> dismiss() }
				.create()
	}

	companion object {
		@JvmStatic
		fun newInstance(): ImportImageDialog = ImportImageDialog()
	}
}