package com.example.currentweatherview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginTop

class CurrentWeatherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) :
    ViewGroup(context, attrs, defStyleAttrs) {

    val weatherImageChild: View?
        get() = getChildAt(0) ?: null

    val currentTemperatureChild: View?
        get() = getChildAt(1) ?: null

    val currentCityChild: View?
        get() = getChildAt(2) ?: null


    override fun measureChild(
        child: View?,
        parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        val widthSpecSize = MeasureSpec.getSize(parentWidthMeasureSpec)
        val heightMeasureSize = MeasureSpec.getSize(parentHeightMeasureSpec)

        val childWidthSpec = when (MeasureSpec.getMode(parentWidthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> parentWidthMeasureSpec
            MeasureSpec.AT_MOST -> parentWidthMeasureSpec
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.AT_MOST)
            else -> error("Unreachable")
        }

        val childHeightSpec = when (MeasureSpec.getMode(parentHeightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> parentHeightMeasureSpec
            MeasureSpec.AT_MOST -> parentHeightMeasureSpec
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(
                heightMeasureSize,
                MeasureSpec.AT_MOST
            )
            else -> error("Unreachable")
        }

        child?.measure(childWidthSpec, childHeightSpec)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        checkChildCount()
        checkForImage()

        weatherImageChild?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }
        currentTemperatureChild?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }
        currentCityChild?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }

        val weatherImageChildHeight = (weatherImageChild?.measuredHeight ?: 0)

        setMeasuredDimension(widthMeasureSpec, weatherImageChildHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftImageBorder = (r - l) / 4
        val rightImageBorder = leftImageBorder + (weatherImageChild?.measuredWidth ?: 0)
        val rightTemperatureBorder = (r - l) * 3 / 4
        val leftTemperatureBorder =
            rightTemperatureBorder - (currentTemperatureChild?.measuredWidth ?: 0)

        val leftCityBorder = (rightTemperatureBorder - leftTemperatureBorder) / 2 +
                    leftTemperatureBorder - ((currentCityChild?.measuredWidth ?: 0) / 2)

        val rightCityBorder = leftCityBorder + (currentCityChild?.measuredWidth ?: 0)


        weatherImageChild?.layout(
            leftImageBorder, 0,
            rightImageBorder,
            weatherImageChild?.measuredHeight ?: 0
        )

        currentTemperatureChild?.layout(
            leftTemperatureBorder,
            0,
            rightTemperatureBorder,
            currentTemperatureChild?.measuredHeight ?: 0
        )

        currentCityChild?.layout(
            leftCityBorder,
            b - (currentCityChild?.measuredHeight ?: 0) - marginTop,
            rightCityBorder,
            weatherImageChild?.measuredHeight ?: 0 + marginTop
        )

    }

    private fun checkChildCount() {
        if (childCount > 3) error("This ViewGroup shouldn't contain more than 3 children")
    }

    private fun checkForImage() {
        if (weatherImageChild !is ImageView) error("First element must be ImageView")
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }
}