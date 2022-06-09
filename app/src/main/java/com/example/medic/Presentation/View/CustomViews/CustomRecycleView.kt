package com.example.medic.Presentation.View.CustomViews

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSmoothScroller
import android.util.DisplayMetrics
import android.content.Context
import android.util.AttributeSet

class CustomRecycleView constructor(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs) {
    private var finalHeight: Int = 0
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        if (getAdapter() != null) finalHeight = heightSpec * adapter!!.itemCount
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(heightSpec, MeasureSpec.EXACTLY))
    }

    public override fun smoothScrollToPosition(position: Int) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(getContext()) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }
        linearSmoothScroller.setTargetPosition(position)

        //startSmoothScroll(linearSmoothScroller);
        super.smoothScrollToPosition(position)
    }

    companion object {
        private val MILLISECONDS_PER_INCH: Float = 10005f
    }
}