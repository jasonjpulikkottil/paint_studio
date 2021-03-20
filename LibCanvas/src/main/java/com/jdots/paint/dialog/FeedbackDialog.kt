package com.jdots.paint.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.jdots.paint.R

class FeedbackDialog : AppCompatDialogFragment() {
	@SuppressLint("InflateParams")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(requireContext(), R.style.paint_studioAlertDialog)
				.setMessage(R.string.paint_studio_feedback)
				.setTitle(R.string.paint_studio_rate_us_title)
				.setPositiveButton(R.string.paint_studio_ok)  { _, _ -> dismiss() }
				.create()
	}

	companion object {
		@JvmStatic
		fun newInstance(): FeedbackDialog = FeedbackDialog()
	}
}