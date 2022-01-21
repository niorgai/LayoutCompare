package com.example.layoutcompare

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop

/**
 * Created by jianqiu on 2022/1/20.
 */
class CustomView(context: Context) :ViewGroup(context) {

    class CustomLayoutParams(x: Int, y: Int): MarginLayoutParams(x, y)

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private val badge = ImageView(context).apply {
        layoutParams = CustomLayoutParams(30.todp(), 30.todp()).also {
            it.topMargin = 85.todp()
            it.rightMargin = 30.todp()
        }
        setBackgroundColor(Color.BLACK)
        addView(this)
    }

    private val image = ImageView(context).apply {
        layoutParams = CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100.todp())
        setBackgroundResource(R.color.teal_200)
        addView(this)
    }

    private val title = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = 10.todp()
            it.marginStart = 10.todp()
        }
        text = "Singapore"
        addView(this)
    }

    private val cameraTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = 10.todp()
            it.marginStart = 10.todp()
        }
        text = "Camera"
        addView(this)
    }

    private val cameraEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, 20.todp()).also {
            it.marginEnd = 10.todp()
        }
        hint = "Leica M Typ 240"
        addView(this)
    }

    private val settingsTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = 10.todp()
            it.marginStart = 10.todp()
        }
        text = "Settings"
        addView(this)
    }

    private val settingsEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            it.marginEnd = 10.todp()
        }
        hint = "f/4 16s ISO 200"
        addView(this)
    }

    private val content = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = 10.todp()
            it.marginStart = 10.todp()
            it.marginEnd = 10.todp()
        }
        text = "Singapore officially the Republic of Singapore, and often referred to as the Lion City, the Garden City, and the Red Dot, is a global city in Southeast Asia and the world"
        addView(this)
    }

    private val upload = Button(context).apply {
        layoutParams = CustomLayoutParams(100.todp(), 50.todp()).also {
            it.bottomMargin = 10.todp()
            it.marginEnd = 130.todp()
        }
        text = "UPLOAD"
        addView(this)
    }

    private val discard = Button(context).apply {
        layoutParams = CustomLayoutParams(100.todp(), 50.todp()).also {
            it.bottomMargin = 10.todp()
            it.marginEnd = 10.todp()
        }
        text = "DISCARD"
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        image.autoMeasure()
        badge.autoMeasure()
        title.autoMeasure()
        cameraTitle.autoMeasure()
        settingsTitle.autoMeasure()
        content.autoMeasure()
        upload.autoMeasure()
        discard.autoMeasure()
        val leftCamera = measuredWidth - cameraTitle.horizontalSize() + cameraEditText.horizontalMargin()
        cameraEditText.measure(leftCamera.toAtMostWidth(), cameraEditText.defaultMeasureHeight())
        val leftSettings = measuredWidth - settingsTitle.horizontalSize() + settingsEditText.horizontalMargin()
        settingsEditText.measure(leftSettings.toAtMostWidth(), settingsEditText.defaultMeasureHeight())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        image.autoLayout()
        badge.autoLayout(fromRight = true)
        var top = image.measuredHeight
        title.autoLayout(y = top)
        top += title.verticalSize()
        cameraTitle.autoLayout(y = top)
        cameraEditText.autoLayout(y = top, fromRight = true)
        top += cameraTitle.verticalSize()
        settingsTitle.autoLayout(y = top)
        settingsEditText.autoLayout(y = top, fromRight = true)
        top += settingsTitle.verticalSize()
        content.autoLayout(y = top)
        upload.autoLayout(fromRight = true, fromBottom = true)
        discard.autoLayout(fromRight = true, fromBottom = true)
    }


    fun View.autoMeasure() {
        this.measure(defaultMeasureWidth(), defaultMeasureHeight())
    }

    fun View.defaultMeasureWidth(): Int {
        val layoutParams = this.layoutParams as CustomLayoutParams
        return when(this.layoutParams.width) {
            LayoutParams.MATCH_PARENT ->
                (this@CustomView.measuredWidth - layoutParams.marginStart - layoutParams.marginEnd).toExactlyWidth()
            LayoutParams.WRAP_CONTENT ->
                (this@CustomView.measuredWidth - layoutParams.marginStart - layoutParams.marginEnd).toAtMostWidth()
            else -> this.layoutParams.width.toExactlyWidth()
        }
    }

    fun View.defaultMeasureHeight(): Int {
        return when(this.layoutParams.height) {
            LayoutParams.MATCH_PARENT -> this@CustomView.measuredHeight.toExactlyWidth()
            LayoutParams.WRAP_CONTENT -> this@CustomView.measuredHeight.toAtMostWidth()
            else -> this.layoutParams.height.toExactlyWidth()
        }
    }

    fun View.horizontalSize() = this.measuredWidth + this.horizontalMargin()

    fun View.horizontalMargin():Int {
        val layoutParams = this.layoutParams as CustomLayoutParams
        return layoutParams.marginStart + layoutParams.marginEnd
    }

    fun View.verticalSize(): Int {
        val layoutParams = this.layoutParams as CustomLayoutParams
        return this.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
    }

    fun Int.toExactlyWidth(): Int = MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)

    fun Int.toAtMostWidth(): Int = MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)

    fun View.autoLayout(x: Int = 0, y: Int = 0,
                        fromRight: Boolean = false, fromBottom: Boolean = false) {
        val layoutParams = this.layoutParams as CustomLayoutParams
        val left = if (!fromRight) x + layoutParams.marginStart else
            this@CustomView.measuredWidth - this.measuredWidth - x - layoutParams.marginEnd
        val top = if (!fromBottom) y + layoutParams.topMargin else
            this@CustomView.measuredHeight - this.measuredHeight- y - layoutParams.bottomMargin
        val right = if (!fromRight) x + this.measuredWidth + layoutParams.marginStart else
            this@CustomView.measuredWidth - x - layoutParams.marginEnd
        val bottom = if (!fromBottom) y + this.measuredHeight + layoutParams.topMargin else
            this@CustomView.measuredHeight - y - layoutParams.bottomMargin
        this.layout(left, top, right, bottom)
    }

}

fun Int.todp(): Int {
    return (Resources.getSystem().displayMetrics.density * this).toInt()
}

fun View.autoLayout(parent: View,
                    x: Int = 0, y: Int = 0,
                    fromRight: Boolean = false, fromBottom: Boolean = false) {
    val layoutParams = this.layoutParams as CustomView.CustomLayoutParams
    val left = if (!fromRight) x + layoutParams.marginStart else
        parent.measuredWidth - this.measuredWidth - x - layoutParams.marginEnd
    val top = if (!fromBottom) y + layoutParams.topMargin else
        parent.measuredHeight - this.measuredHeight- y - layoutParams.bottomMargin
    val right = if (!fromRight) x + this.measuredWidth + layoutParams.marginStart else
        parent.measuredWidth - x - layoutParams.marginEnd
    val bottom = if (!fromBottom) y + this.measuredHeight + layoutParams.topMargin else
        parent.measuredHeight - y - layoutParams.bottomMargin
    this.layout(left, top, right, bottom)
}