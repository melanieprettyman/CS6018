package com.example.lab2_draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

// DrawingView.kt
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var path = Path()
    private val paths = mutableListOf<Path>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint) //
    }

    fun startDrawing(x: Float, y: Float) {
        path = Path().apply {
            moveTo(x, y)
        }
        paths.add(path)
        invalidate()
    }

    fun continueDrawing(x: Float, y: Float) {
        path.lineTo(x, y)
        invalidate()
    }

    fun setPaths(savedPaths: List<Path>) {
        paths.clear()
        paths.addAll(savedPaths)
        invalidate()
    }

    fun getCurrentPath(): Path {
        return path
    }
}
