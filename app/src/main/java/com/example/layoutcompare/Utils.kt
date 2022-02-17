package com.example.layoutcompare

import android.content.res.Resources
import android.view.View
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * Created by jianqiu on 2022/2/17.
 */
object Utils {

    val format: DecimalFormat

    init {
        val text = "#,###"
        format = DecimalFormat(text)
    }

    fun formatNumber(any: Any): String = format.format(any)
}


fun Int.todp(): Int {
    return (Resources.getSystem().displayMetrics.density * this).toInt()
}

fun Int.toExactlyWidth(): Int = View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)

fun Int.toAtMostWidth(): Int = View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)

fun Long.toFormat(): String = Utils.formatNumber(this)