package com.example.layoutcompare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.*
import java.lang.StringBuilder

class TraditionActivity : AppCompatActivity() {

    var count = 0

    lateinit var group: ViewGroup

    var finalTime = 0L
    var callCount = -1

    var addTime = 0L
    var addCount = 0L

    var state = 0

    val stringBuilder = StringBuilder()

    private fun checkstate() {
        if (state == 0) {
            Log.i("LogView", "average isConstraint: ")
            stringBuilder.append("mode constraint end\n\n")
        } else if (state == 1) {
            Log.i("LogView", "average normal: ")
            stringBuilder.append("mode normal end\n\n")
        } else if (state == 2) {
            Log.i("LogView", "average isSimple: ")
            stringBuilder.append("mode simple end\n\n")
        } else {
            Log.i("LogView", "average isCustom: ")
            stringBuilder.append("mode custom end\n\n")
        }
        state ++
        reset()
        if (state <= 3) {
            start()
        } else {
            val resultIntent = Intent()
            resultIntent.putExtra("result", stringBuilder.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun reset() {
        count = 0
        finalTime = 0L
        callCount = 0
        LogViewGroup.measureCall = 0L
        LogViewGroup.measureTime = 0L
        LogViewGroup.layoutCall = 0L
        LogViewGroup.layoutTime = 0L
        addTime = 0L
        addCount = 0
    }

    // 我这边测试绘制了两帧, 第二帧其实不需要计算
    var needCount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tradition)

        group = findViewById(R.id.view_group)

        findViewById<View>(R.id.refresh).setOnClickListener {
            reset()
            start()
        }

        group.addView(getView(), 0)

        val handlerThread = HandlerThread("this")
        handlerThread.start()

        window.addOnFrameMetricsAvailableListener(object: Window.OnFrameMetricsAvailableListener{
            override fun onFrameMetricsAvailable(
                window: Window?,
                frameMetrics: FrameMetrics?,
                dropCountSinceLastInvocation: Int
            ) {
                val duration = frameMetrics?.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) ?: 0
                Log.i("LogView", "onFrameMetricsAvailable: " + duration + " is " + (needCount))
                if (needCount) {
                    finalTime += duration
                    callCount ++
                    needCount = false
                }
            }
        }, Handler(handlerThread.looper))
    }

    private fun getView(): View {
        val start = System.nanoTime()
        val view = if (state == 0) {
            LayoutInflater.from(this).inflate(R.layout.item_constraint, group, false)
        } else if (state == 1) {
            LayoutInflater.from(this).inflate(R.layout.item_tradition, group, false)
        } else if (state == 2) {
            LayoutInflater.from(this).inflate(R.layout.item_tradition_simple, group, false)
        } else {
            CustomView(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
        addTime += (System.nanoTime() - start)
        addCount ++
        // 清除缓存
        resources.flushLayoutCache()
        return view
    }

    private fun start() {
//        if (count % 2 == 0) {
//            measureWithWrapContent()
//        } else {
//            measureWithExactly()
//        }
        val view = getView()
        group.removeAllViewsInLayout()
        group.addView(view)

        needCount = true

        count++
        if (count < 100) {
            group.postDelayed( { start()}, 100)
        } else {
            val frameLog = "frameTime: " + finalTime + " frameCount: " + callCount + " average: " + (finalTime / callCount)
            Log.i("LogView", frameLog)
            stringBuilder.append("frameTime: " + (finalTime / callCount)).append("\n")

            val createLog = "createTime: " + addTime + " createCount: " + addCount + " average: " + (addTime / addCount)
            Log.i("LogView", createLog)
            stringBuilder.append("createTime: " + (addTime / addCount)).append("\n")

            val measureLog = "measureTime: " + LogViewGroup.measureTime + " measureCount: " + LogViewGroup.measureCall + " average: " + (LogViewGroup.measureTime / LogViewGroup.measureCall)
            Log.i("LogView", measureLog)
            stringBuilder.append("measureTime: " + (LogViewGroup.measureTime / LogViewGroup.measureCall)).append("\n")

            val layoutLog = "layoutTime: " + LogViewGroup.layoutTime + " layoutCount: " + LogViewGroup.layoutCall + " average: " + (LogViewGroup.layoutTime / LogViewGroup.layoutCall)
            Log.i("LogView", layoutLog)
            stringBuilder.append( "layoutTime: " + (LogViewGroup.layoutTime / LogViewGroup.layoutCall)).append("\n")

            val allTime = (addTime / addCount + LogViewGroup.measureTime / LogViewGroup.measureCall + LogViewGroup.layoutTime / LogViewGroup.layoutCall)
            val fullFlowLog = "full_flow: " + allTime + " average: " + allTime / 3
            Log.i("LogView", fullFlowLog)
            stringBuilder.append("full_flow: $allTime").append("\n")

            group.postDelayed( { checkstate() }, 1000)
        }
    }
//
//    fun measureWithWrapContent() {
//        val view = group
//        val width = View.MeasureSpec.makeMeasureSpec(resources.displayMetrics.widthPixels - 1, View.MeasureSpec.EXACTLY)
//        val height = View.MeasureSpec.makeMeasureSpec(resources.displayMetrics.heightPixels - 1, View.MeasureSpec.EXACTLY)
//        view.measure(width, height)
//        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
//    }
//
//    fun measureWithExactly() {
//        val view = group
//        val width = View.MeasureSpec.makeMeasureSpec(resources.displayMetrics.widthPixels, View.MeasureSpec.EXACTLY)
//        val height = View.MeasureSpec.makeMeasureSpec(resources.displayMetrics.heightPixels, View.MeasureSpec.EXACTLY)
//        view.measure(width, height)
//        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
//    }
}