package com.jdots.paint.command.implementation

import android.graphics.Canvas
import android.graphics.Paint
import com.jdots.paint.command.Command
import com.jdots.paint.contract.LayerContracts

class SprayCommand(
        private val sprayedPoints: FloatArray,
        private val paint: Paint
) : Command {

    override fun run(canvas: Canvas?, layerModel: LayerContracts.Model?) {
        canvas?.drawPoints(sprayedPoints, paint)
    }

    override fun freeResources() {
        //nothing to free
    }
}