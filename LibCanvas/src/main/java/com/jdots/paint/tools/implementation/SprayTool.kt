package com.jdots.paint.tools.implementation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.*
import com.jdots.paint.command.CommandManager
import com.jdots.paint.tools.ContextCallback
import com.jdots.paint.tools.ToolPaint
import com.jdots.paint.tools.ToolType
import com.jdots.paint.tools.Workspace
import com.jdots.paint.tools.options.SprayToolOptionsView
import com.jdots.paint.tools.options.ToolOptionsVisibilityController
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class SprayTool(var stampToolOptionsView: SprayToolOptionsView,
                val contextCallback: ContextCallback,
                toolOptionsViewController: ToolOptionsVisibilityController,
                toolPaint: ToolPaint,
                workspace: Workspace,
                commandManager: CommandManager)
    : BaseTool(contextCallback, toolOptionsViewController, toolPaint, workspace, commandManager) {

    @VisibleForTesting
    var sprayToolScope = CoroutineScope(Dispatchers.Main)
    @VisibleForTesting
    var sprayedPoints = ConcurrentLinkedQueue<PointF>()
    @VisibleForTesting
    var sprayActive = false
    private var currentCoordinate: PointF? = null
    private var previewBitmap: Bitmap = Bitmap.createBitmap(workspace.width, workspace.height, Bitmap.Config.ARGB_8888)
    private val previewCanvas = Canvas(previewBitmap)

    private var sprayRadius = DEFAULT_RADIUS

    companion object {
        const val BUNDLE_RADIUS = "BUNDLE_RADIUS"
        const val DEFAULT_RADIUS = 30
    }

    init {
        toolPaint.strokeWidth = 5f

        stampToolOptionsView.setCallback(object : SprayToolOptionsView.Callback {
            override fun radiusChanged(radius: Int) {
                sprayRadius = DEFAULT_RADIUS + radius  * 2
            }
        })

        stampToolOptionsView.setCurrentPaint(toolPaint.paint)
        toolOptionsViewController.showDelayed()
    }

    override fun draw(canvas: Canvas?) {
        canvas?.run {
            save()
            drawBitmap(previewBitmap, 0.0f, 0.0f, null)
            restore()
        }
    }

    override fun handleUp(coordinate: PointF?): Boolean {
        sprayToolScope.cancel()
        currentCoordinate = coordinate
        addSprayCommand()
        return true
    }

    override fun handleMove(coordinate: PointF?): Boolean {
        currentCoordinate = coordinate
        return true
    }

    override fun handleDown(coordinate: PointF?): Boolean {
        if(sprayActive || coordinate == null) {
            return false
        }

        sprayActive = true
        currentCoordinate = coordinate
        createSprayPatternAsync()
        return true
    }

    override fun onSaveInstanceState(bundle: Bundle?) {
        super.onSaveInstanceState(bundle)
        bundle?.putInt(BUNDLE_RADIUS, sprayRadius)
        sprayToolScope.cancel()
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        super.onRestoreInstanceState(bundle)
        bundle?.getInt(BUNDLE_RADIUS)?.let { radius ->
            sprayRadius = radius
            stampToolOptionsView.setRadius(radius)
        }
    }

    private fun addSprayCommand() {
        val pointsList = mutableListOf<PointF>().apply {
            addAll(sprayedPoints)
        }

        val pointsArray = FloatArray(pointsList.size * 2)

        pointsList.forEachIndexed { index, point ->
            pointsArray[index * 2] = point.x
            pointsArray[index * 2 + 1] = point.y
        }

        val command = commandFactory.createSprayCommand(pointsArray, drawPaint)
        commandManager.addCommand(command)
    }


    private fun createSprayPatternAsync() {
        sprayToolScope = CoroutineScope(Dispatchers.Default)
        sprayToolScope.launch {
            while(true) {
                repeat((sprayRadius / DEFAULT_RADIUS)) {
                    val point = createRandomPointInCircle()
                    if (workspace.contains(point)) {
                        previewCanvas.drawPoint(point.x, point.y, drawPaint)
                        sprayedPoints.add(point)
                    }
                }

                delay(1)
            }
        }
    }

    override fun resetInternalState() {
        sprayToolScope.cancel()
        sprayActive = false
        sprayedPoints.clear()
        previewBitmap.eraseColor(Color.TRANSPARENT)
    }

    private fun createRandomPointInCircle(): PointF {
        val point = PointF()
        val radius = sprayRadius * Random.nextFloat().pow(0.5f)
        val theta = Random.nextFloat() * 2f * Math.PI

        point.x = radius * cos(theta).toFloat() + (currentCoordinate?.x ?: 0f)
        point.y = radius * sin(theta).toFloat() + (currentCoordinate?.y ?: 0f)
        return point
    }

    override fun getToolType(): ToolType? {
        return ToolType.SPRAY
    }
}