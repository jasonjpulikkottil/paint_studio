package com.jdots.paint.ui.tools

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.textfield.TextInputEditText
import com.jdots.paint.R
import com.jdots.paint.tools.options.SprayToolOptionsView

class DefaultSprayToolOptionsView(rootView: ViewGroup) : SprayToolOptionsView {

    private var callback: SprayToolOptionsView.Callback? = null
    private val radiusText: TextInputEditText
    private val radiusSeekBar: SeekBar

    companion object {
        private const val MIN_RADIUS = 1
    }

    init {
        val inflater = LayoutInflater.from(rootView.context)
        val sprayToolOptionsView: View = inflater.inflate(R.layout.dialog_paint_studio_spray_tool, rootView)
        radiusText = sprayToolOptionsView.findViewById(R.id.paint_studio_radius_text)
        radiusSeekBar = sprayToolOptionsView.findViewById(R.id.paint_studio_spray_radius_seek_bar)
        initializeListeners()
    }

    private fun initializeListeners() {
        radiusSeekBar.progress = MIN_RADIUS
        radiusText.setText(radiusSeekBar.progress.toString())
        radiusText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().toInt() > 100) {
                    radiusText.setText(s.toString().substring(0,2))
                    return
                }

                if(radiusSeekBar.progress.toString() != s.toString()) {
                    radiusSeekBar.progress = s.toString().toInt()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                if (progress < MIN_RADIUS) {
                    radiusText.setText(MIN_RADIUS.toString())
                } else {
                    radiusText.setText(progress.toString())
                }

                callback?.radiusChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun setCallback(callback: SprayToolOptionsView.Callback?) {
        this.callback = callback
    }

    override fun setRadius(radius: Int) {
        radiusSeekBar.progress = radius
    }

    override fun setCurrentPaint(paint: Paint) {
        setRadius(paint.strokeWidth.toInt())
    }
}