package com.jdots.paint.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.jdots.paint.R

class LikeUsDialog : MainActivityDialogFragment() {
	@SuppressLint("InflateParams")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return AlertDialog.Builder(requireContext(), R.style.paint_studioAlertDialog)
				.setMessage(getString(R.string.paint_studio_like_us))
				.setTitle(getString(R.string.paint_studio_rate_us_title))
				.setPositiveButton(R.string.paint_studio_yes) { _, _ ->
					presenter.showRateUsDialog()
					dismiss()
				}
				.setNegativeButton(R.string.paint_studio_no) { _, _ ->
					presenter.showFeedbackDialog()
					dismiss()
				}
				.create()
	}

	companion object {
		@JvmStatic
		fun newInstance(): LikeUsDialog = LikeUsDialog()
	}
}