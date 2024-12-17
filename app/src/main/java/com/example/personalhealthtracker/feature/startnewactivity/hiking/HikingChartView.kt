package com.example.personalhealthtracker.feature.startnewactivity.hiking

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class HikingChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        color = context.getColor(android.R.color.holo_blue_bright)
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val elevationData = mutableListOf<Float>() // Will hold live elevation data
    private val maxElevation = 1000f // Max elevation for scaling

    init {
        // Initially load the chart with zero values
        for (i in 0..20) {
            elevationData.add(0f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val stepX = width / (elevationData.size - 1)
        val stepY = height / maxElevation

        val path = android.graphics.Path()
        path.moveTo(0f, height - elevationData[0] * stepY) // Start point

        for (i in 1 until elevationData.size) {
            path.lineTo(i * stepX, height - elevationData[i] * stepY)
        }

        canvas.drawPath(path, paint)
    }

    fun updateChartData(newData: Float) {
        elevationData.add(newData)
        if (elevationData.size > 20) {
            elevationData.removeAt(0) // Keep chart size constant
        }
        invalidate() // Redraw chart
    }

    fun setMaxElevation(max: Float) {
        // Optionally adjust chart scaling
    }
}
