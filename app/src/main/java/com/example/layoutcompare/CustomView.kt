package com.example.layoutcompare

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
class CustomView(context: Context, attributeSet: AttributeSet? = null) :ViewGroup(context, attributeSet) {

    class CustomLayoutParams(x: Int, y: Int): MarginLayoutParams(x, y)

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private val margin = 16.todp()

    private val image = ImageView(context).apply {
        layoutParams = CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150.todp())
        setImageResource(R.drawable.singapore)
        scaleType = ImageView.ScaleType.CENTER_CROP
        contentDescription = context.getString(R.string.dummy)
        addView(this)
    }

    private val favorite = ImageView(context).apply {
        val size = 36.todp()
        layoutParams = CustomLayoutParams(size, size).also {
            it.rightMargin = margin
        }
        val padding = 5.todp()
        setBackgroundResource(R.drawable.info_background)
        setPadding(padding, padding, padding, padding)
        setImageResource(R.drawable.ic_star)
        contentDescription = context.getString(R.string.dummy)
        addView(this)
    }

    private val title = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = margin
            it.marginStart = margin
        }
        setText(R.string.singapore)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        addView(this)
    }

    private val cameraTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.marginStart = margin
            it.topMargin = margin / 2
        }
        setText(R.string.camera)
        addView(this)
    }

    private val cameraEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            val itemMargin = margin / 2
            it.marginStart = itemMargin
            it.topMargin = itemMargin
            it.marginEnd = itemMargin
        }
        setText(R.string.camera_value)
        setEms(10)
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
        addView(this)
    }

    private val settingsTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.marginStart = margin
            it.topMargin = margin / 2
        }
        setText(R.string.settings)
        addView(this)
    }

    private val settingsEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            val itemMargin = margin / 2
            it.marginStart = itemMargin
            it.topMargin = itemMargin
            it.marginEnd = itemMargin
        }
        setText(R.string.camera_settings)
        setEms(10)
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
        addView(this)
    }

    private val description = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
            it.topMargin = margin / 2
            it.marginStart = margin
            it.marginEnd = margin
            it.bottomMargin = margin / 2
        }
        ellipsize = TextUtils.TruncateAt.END
        isVerticalFadingEdgeEnabled = true
        setText(R.string.singapore_description)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        addView(this)
    }

    private val upload = Button(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            val margin = 16.todp()
            it.bottomMargin = margin
            it.marginEnd = 100.todp()
        }
        setText(R.string.upload)
        addView(this)
    }

    private val discard = Button(context).apply {
        val margin = 16.todp()
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
            it.bottomMargin = margin
            it.marginEnd = margin / 2
        }
        setText(R.string.discard)
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        image.autoMeasure()
        favorite.autoMeasure()
        title.autoMeasure()
        cameraTitle.autoMeasure()
        settingsTitle.autoMeasure()
        description.autoMeasure()
        upload.autoMeasure()
        discard.autoMeasure()
        val leftCamera = measuredWidth - cameraTitle.horizontalSize() - cameraEditText.horizontalMargin()
        cameraEditText.measure(leftCamera.toExactlyWidth(), cameraEditText.defaultMeasureHeight())
        val leftSettings = measuredWidth - settingsTitle.horizontalSize() - settingsEditText.horizontalMargin()
        settingsEditText.measure(leftSettings.toExactlyWidth(), settingsEditText.defaultMeasureHeight())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        image.autoLayout()
        var top = image.measuredHeight
        favorite.autoLayout(y = top - favorite.measuredHeight / 2, fromRight = true)
        title.autoLayout(y = top)
        top += title.verticalSize()
        val cameraOffset = cameraEditText.paint.fontMetrics.let { (it.descent + it.ascent) / 2 - it.descent }.toInt()
        cameraTitle.autoLayout(y = top + cameraEditText.measuredHeight / 2 + cameraOffset)
        cameraEditText.autoLayout(y = top, fromRight = true)
        top += cameraEditText.verticalSize()

        val settingOffset = settingsEditText.paint.fontMetrics.let { (it.descent + it.ascent) / 2 - it.descent }.toInt()
        settingsTitle.autoLayout(y = top + settingsEditText.measuredHeight / 2 + settingOffset)
        settingsEditText.autoLayout(y = top, fromRight = true)
        top += settingsEditText.verticalSize()
        description.autoLayout(y = top)
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

    fun View.autoLayout(x: Int = 0, y: Int = 0,
                        fromRight: Boolean = false, fromBottom: Boolean = false) {
        val layoutParams = this.layoutParams as CustomLayoutParams
        if (!fromRight) {
            val left = x + layoutParams.marginStart
            if (!fromBottom) {
                val top = y + layoutParams.topMargin
                this.layout(left, top, left + this.measuredWidth, top + this.measuredHeight)
            } else {
                val bottom = this@CustomView.measuredHeight - y - layoutParams.bottomMargin
                this.layout(left, bottom - this.measuredHeight, left + this.measuredWidth, bottom)
            }
        } else {
            val right = this@CustomView.measuredWidth - x - layoutParams.marginEnd
            if (!fromBottom) {
                val top = y + layoutParams.topMargin
                this.layout(right - this.measuredWidth, top, right, top + this.measuredHeight)
            } else {
                val bottom = this@CustomView.measuredHeight - y - layoutParams.bottomMargin
                this.layout(right - this.measuredWidth, bottom - this.measuredHeight, right, bottom)
            }
        }
    }

}
