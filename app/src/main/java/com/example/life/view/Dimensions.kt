package com.lifegame.view

import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.properties.Delegates

fun dipToPx(metrics: DisplayMetrics, dip: Int) : Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), metrics)
}

class Dimensions {
    var canvasWidth by Delegates.observable(0) { _, _, _ -> recalculate()}
    var canvasHeight by Delegates.observable(0) { _, _, _ -> recalculate()}
    var rows by Delegates.observable(0) { _, _, _ -> recalculate()}
    var columns by Delegates.observable(0) { _, _, _ -> recalculate()}
    var lineThick by Delegates.observable(0f) { _, _, _ -> recalculate()}

    var insetX = 0f
        private set
    var insetY = 0f
        private set
    var cellSize = 0f
        private set

    private fun recalculate() {
        cellSize = minOf(
            (canvasWidth - lineThick) / columns,
            (canvasHeight - lineThick) / rows
        ) - lineThick

        insetX = (canvasWidth - lineThick - columns * (cellSize + lineThick)) / 2
        insetY = (canvasHeight - lineThick - rows * (cellSize + lineThick)) / 2
    }
}