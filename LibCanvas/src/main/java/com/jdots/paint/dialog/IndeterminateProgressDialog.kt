
package com.jdots.paint.dialog

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.jdots.paint.R

object IndeterminateProgressDialog {
	@JvmStatic
	@SuppressLint("InflateParams")
	fun newInstance(context: Context?): AlertDialog {
		return AlertDialog.Builder(context!!, R.style.paint_studioProgressDialog)
			.setCancelable(false)
			.setView(R.layout.paint_studio_layout_indeterminate)
			.create()
	}
}