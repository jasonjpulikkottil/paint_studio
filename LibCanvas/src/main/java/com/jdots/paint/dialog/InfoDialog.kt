
package com.jdots.paint.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import com.jdots.paint.R

class InfoDialog : AppCompatDialogFragment(), DialogInterface.OnClickListener {
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(requireContext(), R.style.paint_studioAlertDialog)
		arguments?.let {
			builder.setTitle(it.getInt(TITLE_RESOURCE_KEY))
					.setIcon(it.getInt(DRAWABLE_RESOURCE_KEY))
					.setMessage(it.getInt(MESSAGE_RESOURCE_KEY))
		}
		builder.setPositiveButton(android.R.string.ok, this)
		return builder.create()
	}

	override fun onClick(dialog: DialogInterface, which: Int) {
		dialog.cancel()
	}

	enum class DialogType(@get:DrawableRes @param:DrawableRes val imageResource: Int) {
		INFO(R.drawable.ic_paint_studio_dialog_info), WARNING(R.drawable.ic_paint_studio_dialog_warning);
	}

	companion object {
		const val DRAWABLE_RESOURCE_KEY = "drawableResource"
		const val MESSAGE_RESOURCE_KEY = "messageResource"
		const val TITLE_RESOURCE_KEY = "titleResource"

		@JvmStatic
		fun newInstance(dialogType: DialogType, @StringRes messageResource: Int, @StringRes titleResource: Int): InfoDialog {
			return InfoDialog().apply {
				arguments = bundleOf(
					DRAWABLE_RESOURCE_KEY to dialogType.imageResource,
					MESSAGE_RESOURCE_KEY to messageResource,
					TITLE_RESOURCE_KEY to titleResource
				)
			}
		}
	}
}