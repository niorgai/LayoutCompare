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

class TestActivity : AppCompatActivity() {

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
            Log.i("LogView", "average isEmpty: ")
            stringBuilder.append("mode empty end\n\n")
        } else if (state == 1) {
            Log.i("LogView", "average isConstraint: ")
            stringBuilder.append("mode constraint end\n\n")
        } else if (state == 2) {
            Log.i("LogView", "average isNormal: ")
            stringBuilder.append("mode normal end\n\n")
        } else if (state == 3) {
            Log.i("LogView", "average isSimple: ")
            stringBuilder.append("mode simple end\n\n")
        } else if (state == 4) {
            Log.i("LogView", "average isCustom: ")
            stringBuilder.append("mode custom end\n\n")
        } else {
            Log.i("LogView", "average isTotalCustom: ")
            stringBuilder.append("mode total custom end\n\n")
        }
        state ++
        reset()
        if (state <= 5) {
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

    // 只计算需要重绘的那一帧
    var needCount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tradition)

        group = findViewById(R.id.view_group)

        findViewById<View>(R.id.refresh).setOnClickListener {
            reset()
            start()
        }

        val handlerThread = HandlerThread("this")
        handlerThread.start()

        window.addOnFrameMetricsAvailableListener(object: Window.OnFrameMetricsAvailableListener{
            override fun onFrameMetricsAvailable(
                window: Window?,
                frameMetrics: FrameMetrics?,
                dropCountSinceLastInvocation: Int
            ) {
                val duration = frameMetrics?.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) ?: 0
                if (needCount) {
                    finalTime += duration
                    callCount ++
                    needCount = false
                }
            }
        }, Handler(handlerThread.looper))
    }

    private fun getView(): View {
        // 清除缓存
        LayoutInflater.from(this).context.resources.flushLayoutCache()
        val start = System.nanoTime()
        val view = if (state == 0) {
            View(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            }
        } else if (state == 1) {
            LayoutInflater.from(this).inflate(R.layout.item_constraint, group, false)
        } else if (state == 2) {
            LayoutInflater.from(this).inflate(R.layout.item_tradition, group, false)
        } else if (state == 3) {
            LayoutInflater.from(this).inflate(R.layout.item_tradition_simple, group, false)
        } else if (state == 4) {
            CustomView(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            }
        } else {
            TotalCustomView(this).apply {
                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
        addTime += (System.nanoTime() - start)
        addCount ++
        return view
    }

    private fun start() {
        val view = getView()
        group.removeAllViewsInLayout()
        group.addView(view)

        needCount = true

        count++
        if (count < 100) {
            group.postDelayed( { start()}, 100)
        } else {
            val frameLog = "frameTime: " + finalTime.toFormat() + " frameCount: " + callCount + " average: " + (finalTime / callCount).toFormat()
            Log.i("LogView", frameLog)
            stringBuilder.append("frameTime: " + (finalTime / callCount).toFormat()).append("\n")

            val createLog = "createTime: " + addTime.toFormat() + " createCount: " + addCount + " average: " + (addTime / addCount).toFormat()
            Log.i("LogView", createLog)
            stringBuilder.append("createTime: " + (addTime / addCount).toFormat()).append("\n")

            val measureLog = "measureTime: " + LogViewGroup.measureTime.toFormat() + " measureCount: " + LogViewGroup.measureCall + " average: " + (LogViewGroup.measureTime / LogViewGroup.measureCall).toFormat()
            Log.i("LogView", measureLog)
            stringBuilder.append("measureTime: " + (LogViewGroup.measureTime / LogViewGroup.measureCall).toFormat()).append("\n")

            val layoutLog = "layoutTime: " + LogViewGroup.layoutTime.toFormat() + " layoutCount: " + LogViewGroup.layoutCall + " average: " + (LogViewGroup.layoutTime / LogViewGroup.layoutCall).toFormat()
            Log.i("LogView", layoutLog)
            stringBuilder.append( "layoutTime: " + (LogViewGroup.layoutTime / LogViewGroup.layoutCall).toFormat()).append("\n")

            val allTime = (addTime / addCount + LogViewGroup.measureTime / LogViewGroup.measureCall + LogViewGroup.layoutTime / LogViewGroup.layoutCall).toFormat()
            val fullFlowLog = "full_flow: " + allTime + " average: "
            Log.i("LogView", fullFlowLog)
            stringBuilder.append("full_flow: $allTime").append("\n")

            group.postDelayed( { checkstate() }, 1000)
        }
    }
}