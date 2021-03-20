package com.jdots.paint.command.implementation

import android.graphics.*
import com.jdots.paint.command.Command
import com.jdots.paint.contract.LayerContracts

class CutCommand(val toolPosition: Point,
                 val boxWidth: Float,
                 val boxHeight: Float,
                 val boxRotation: Float
) : Command {
    private val boxRect = RectF(-boxWidth / 2f, -boxHeight / 2f, boxWidth / 2f,boxHeight / 2f)
    private val paint: Paint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        alpha = 0
    }

    override fun run(canvas: Canvas, layerModel: LayerContracts.Model?) {
        canvas.save()
        canvas.translate(toolPosition.x.toFloat(), toolPosition.y.toFloat())
        canvas.rotate(boxRotation)
        canvas.drawRect(boxRect, paint)
        canvas.restore()
    }

    override fun freeResources() {
        //No resources to free
    }
}
