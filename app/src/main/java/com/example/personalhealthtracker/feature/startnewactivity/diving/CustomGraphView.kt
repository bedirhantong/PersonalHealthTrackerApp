package com.example.personalhealthtracker.feature.startnewactivity.diving

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint()
    private val path = Path()
    private val points = mutableListOf<Pair<Float, Float>>() // Grafik için veri noktaları
    private val graphWidth = 800f // Grafik genişliği
    private val graphHeight = 500f // Grafik yüksekliği

    init {
        // Grafik çiziminde kullanılan paint özellikleri
        paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
    }

    // Grafik için yeni bir nokta eklemek
    fun updateGraph(newPoint: Pair<Float, Float>) {
        if (points.size > 10) points.removeAt(0)
        points.add(newPoint)
        invalidate() // Yeni veri geldiğinde yeniden çizim tetikler
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Grafik verisi varsa çizim yapalım
        if (points.isNotEmpty()) {
            path.reset() // Önceki çizimi temizle
            path.moveTo(0f, graphHeight - points[0].second)

            // Veri noktaları ile grafiği çiz
            for (i in 1 until points.size) {
                val x = (graphWidth / points.size) * i
                val y = graphHeight - points[i].second // Grafik alt hizasında çizim
                path.lineTo(x, y)
            }

            canvas.drawPath(path, paint)
        }
    }

    // Grafik sıfırlanacaksa
    fun resetGraph() {
        points.clear()
        invalidate() // Çizimi sıfırla
    }
}
