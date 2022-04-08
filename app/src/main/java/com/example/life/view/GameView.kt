package com.example.life.view

import GameField
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.properties.Delegates

class GameView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    private val dims = Dimensions().also {
        it.lineThick = dipToPx(resources.displayMetrics, 2)
    }

    var field by Delegates.observable(GameField()) { _,_,newValue ->
        dims.rows = newValue.height
        dims.columns = newValue.width
        postInvalidate()
    }

    init {
        field = field //Do not delete. It calls onChange()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTileTouchListener(listener: (x: Int, y: Int)->Unit) {
        setOnTouchListener { _, event ->
            if(event.actionMasked != MotionEvent.ACTION_DOWN) {
                return@setOnTouchListener false
            }
            val tileX = ((event.x - dims.insetX - dims.lineThick) /
                    (dims.lineThick + dims.cellSize)).toInt()
            val tileY = ((event.y - dims.insetY - dims.lineThick) /
                    (dims.lineThick + dims.cellSize)).toInt()
            listener(tileX, tileY)
            return@setOnTouchListener true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        dims.canvasWidth = width
        dims.canvasHeight = height

        canvas.drawColor(Color.BLACK)
        drawGrid(canvas)
        fillGrid(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val cellsInARow = field.width
        val cellsInAColumn = field.height

        val paint = Paint().also {
            it.color = Color.GRAY
        }

        val verticalLine =
            RectF(dims.insetX, dims.insetY, dims.lineThick + dims.insetX, canvas.height - dims.insetY)
        canvas.drawRect(verticalLine, paint)
        for (i in 1..cellsInARow) {
            verticalLine.offset(dims.cellSize + dims.lineThick, 0f)
            canvas.drawRect(verticalLine, paint)
        }

        val horizontalLine = RectF(dims.insetX, dims.insetY, canvas.width - dims.insetX, dims.lineThick + dims.insetY)
        canvas.drawRect(horizontalLine, paint)
        for (i in 1..cellsInAColumn) {
            horizontalLine.offset(0f, dims.cellSize + dims.lineThick)
            canvas.drawRect(horizontalLine, paint)
        }
    }

    private fun fillGrid(canvas: Canvas) {
        val paint = Paint().also {
            it.color = Color.GREEN
        }
        for(tileX in 0 until field.width) {
            for(tileY in 0 until field.height) {
                if(field[tileX, tileY]) {
                    val x = dims.insetX + dims.lineThick + tileX * (dims.cellSize + dims.lineThick) + dims.cellSize / 2
                    val y = dims.insetY + dims.lineThick + tileY * (dims.cellSize + dims.lineThick) + dims.cellSize / 2
                    canvas.drawCircle(x, y, dims.cellSize / 2, paint)
                }
            }
        }
    }
}