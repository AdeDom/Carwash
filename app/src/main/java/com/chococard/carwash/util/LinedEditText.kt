package com.chococard.carwash.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getColor
import com.chococard.carwash.R

class LinedEditText(
    context: Context?,
    attrs: AttributeSet?
) : AppCompatEditText(context, attrs) {

    private val rect: Rect = Rect()

    private val paint: Paint = Paint()

    override fun onDraw(canvas: Canvas) { // get the height of the view
        val height = (this.parent as View).height
        val lineHeight: Int = lineHeight
        val numberOfLines = height / lineHeight
        val r = rect
        val paint = paint
        var baseline: Int = getLineBounds(0, r)
        for (i in 0 until numberOfLines) {
            canvas.drawLine(
                r.left.toFloat(),
                baseline + 1.toFloat(),
                r.right.toFloat(),
                baseline + 1.toFloat(),
                paint
            )
            baseline += lineHeight
        }
        super.onDraw(canvas)
    }

    // we need this constructor for LayoutInflater
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = getColor(context!!, R.color.colorTextGray) // Color of the lines on paper
    }

}
