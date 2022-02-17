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
class TotalCustomView(context: Context, attributeSet: AttributeSet? = null) :ViewGroup(context, attributeSet) {

    class CustomLayoutParams(x: Int, y: Int): MarginLayoutParams(x, y)

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private val margin8 = 8.todp()

    private val margin16 = margin8 * 2

    private val image = ImageView(context).apply {
        layoutParams = CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setImageResource(R.drawable.singapore)
        scaleType = ImageView.ScaleType.CENTER_CROP
        contentDescription = context.getString(R.string.dummy)
        addView(this)
    }

    private val favorite = ImageView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val padding = 5.todp()
        setBackgroundResource(R.drawable.info_background)
        setPadding(padding, padding, padding, padding)
        setImageResource(R.drawable.ic_star)
        contentDescription = context.getString(R.string.dummy)
        addView(this)
    }

    private val title = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.singapore)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        addView(this)
    }

    private val cameraTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.camera)
        addView(this)
    }

    private val cameraEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.camera_value)
        setEms(10)
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
        addView(this)
    }

    private val settingsTitle = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.settings)
        addView(this)
    }

    private val settingsEditText = EditText(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.camera_settings)
        setEms(10)
        inputType = EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME
        addView(this)
    }

    private val description = TextView(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        ellipsize = TextUtils.TruncateAt.END
        isVerticalFadingEdgeEnabled = true
        setText(R.string.singapore_description)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        addView(this)
    }

    private val upload = Button(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.upload)
        addView(this)
    }

    private val discard = Button(context).apply {
        layoutParams = CustomLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setText(R.string.discard)
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = 36.todp().toExactlyWidth()
        favorite.measure(size, size)
        val heightWrapContent = measuredHeight.toAtMostWidth()
        title.measure(heightWrapContent, heightWrapContent)
        cameraTitle.measure(heightWrapContent, heightWrapContent)
        settingsTitle.measure(heightWrapContent, heightWrapContent)
        var leftSize = (measuredWidth - margin16 - margin16).toAtMostWidth()
        description.measure(leftSize, heightWrapContent)
        leftSize = (measuredWidth - cameraTitle.measuredWidth - margin16 - margin16).toExactlyWidth()
        cameraEditText.measure(leftSize, heightWrapContent)
        leftSize = (measuredWidth - settingsTitle.measuredWidth - margin16 - margin16).toExactlyWidth()
        settingsEditText.measure(leftSize, heightWrapContent)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = 150.todp()
        image.layout(0, 0, measuredWidth, top)

        favorite.layout(measuredWidth - favorite.measuredWidth - margin16, top - favorite.measuredHeight / 2,
            measuredWidth - margin16, top + favorite.measuredHeight / 2)

        top += margin16
        title.layout(margin16, top, margin16 + title.measuredWidth, top + title.measuredHeight)

        top += title.measuredHeight + margin8
        var offset = cameraEditText.paint.fontMetrics.let { (it.descent + it.ascent) / 2 - it.descent }.toInt()
        cameraTitle.layout(margin16, top + offset + cameraEditText.measuredHeight / 2,
            margin16 + cameraTitle.measuredWidth, top + offset + cameraEditText.measuredHeight / 2 + cameraTitle.measuredHeight)
        cameraEditText.layout(measuredWidth - margin8 - cameraEditText.measuredWidth, top,
            measuredWidth - margin8, top + cameraEditText.measuredHeight)

        top += cameraEditText.measuredHeight + margin8
        offset = settingsEditText.paint.fontMetrics.let { (it.descent + it.ascent) / 2 - it.descent }.toInt()
        settingsTitle.layout(margin16, top + offset + settingsEditText.measuredHeight / 2,
            margin16 + settingsTitle.measuredWidth, top + offset + settingsEditText.measuredHeight / 2 + settingsTitle.measuredHeight)
        settingsEditText.layout(measuredWidth - margin8 - settingsEditText.measuredWidth, top,
            measuredWidth - margin8, top + settingsEditText.measuredHeight)

        top += (settingsEditText.measuredHeight + margin8)
        description.layout(margin16, top, measuredWidth - margin16, top + description.measuredHeight)

        val bottom = measuredHeight - margin16
        val uploadRight = measuredWidth - 100.todp()
        upload.layout(uploadRight - upload.measuredWidth, bottom - upload.measuredHeight, uploadRight, bottom)
        discard.layout(measuredWidth - margin8 - upload.measuredWidth, bottom - upload.measuredHeight, measuredWidth - margin8, bottom)
    }

}