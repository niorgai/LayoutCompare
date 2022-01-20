package com.example.layoutcompare

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class TraditionActivity : AppCompatActivity() {

    var count = 0

    lateinit var group: ViewGroup

    var finalTime = 0L
    var callCount = -1

    var isConstraint = false
    var isCustom = false
    var isEmpty = false

    var addTime = 0L
    var addCount = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tradition)
        ViewGroup.LayoutParams.WRAP_CONTENT

        isConstraint = intent.getBooleanExtra("constraint", false)
        isCustom = intent.getBooleanExtra("custom", false)
        isEmpty = intent.getBooleanExtra("empty", false)
        if (isConstraint) {
            Log.i("LogView", "isConstraint: ")
        } else if (isCustom) {
            Log.i("LogView", "isCustom: ")
        } else if (isEmpty) {
            Log.i("LogView", "isEmpty: ")
        } else {
            Log.i("LogView", "normal: ")
        }

        group = findViewById(R.id.view_group)

        findViewById<View>(R.id.refresh).setOnClickListener {
            count = 0
            finalTime = 0L
            callCount = 0
            LogViewGroup.measureCall = 0L
            LogViewGroup.measureTime = 0L
            LogViewGroup.layoutCall = 0L
            LogViewGroup.layoutTime = 0L
            addTime = 0L
            addCount = 0
            start()
        }

//        for (i in 0 until 20) {


//        }

        val handlerThread = HandlerThread("this")
        handlerThread.start()

        window.addOnFrameMetricsAvailableListener(object: Window.OnFrameMetricsAvailableListener{
            override fun onFrameMetricsAvailable(
                window: Window?,
                frameMetrics: FrameMetrics?,
                dropCountSinceLastInvocation: Int
            ) {
                val frameCopy = FrameMetrics(frameMetrics)
                val duration = frameCopy.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)
                Log.i("LogView", "onFrameMetricsAvailable: " + duration)
                if (callCount >= 0) {
                    finalTime += duration
                    callCount ++
                }
            }
        }, Handler(handlerThread.looper))
    }

    fun addView(): View {
        val start = System.nanoTime()
        val view = if (isConstraint) {
            LayoutInflater.from(this).inflate(R.layout.item_constraint, group, false)
//        } else if (isCustom) {
//            CustomView(this).apply {
//                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    resources.displayMetrics.densityDpi * 54)
//            }
        } else if (isEmpty) {
            LayoutInflater.from(this).inflate(R.layout.item_tradition_simple, group, false)
        } else {
            LayoutInflater.from(this).inflate(R.layout.item_tradition, group, false)
        }
        addTime += (System.nanoTime() - start)
        addCount ++
        return view
    }

    private fun start() {
//        if (count % 2 == 0) {
//            measureWithWrapContent()
//        } else {
//            measureWithExactly()
//        }
        group.removeAllViewsInLayout()
        group.addView(addView())
        count++
        if (count < 100) {
            group.postDelayed( { start()}, 100)
        } else {
            Log.i("LogView", "totalTime: " + finalTime + " totalCall: " + callCount + " average: " + (finalTime / callCount))
            Log.i("LogView", "addTime: " + addTime + " addCount: " + addCount + " average: " + (addTime / addCount))
            Log.i("LogView", "measureTime: " + LogViewGroup.measureTime + " measureCall: " + LogViewGroup.measureCall + " average: " + (LogViewGroup.measureTime / LogViewGroup.measureCall))
            Log.i("LogView", "layoutTime: " + LogViewGroup.layoutTime + " layoutCall: " + LogViewGroup.layoutCall + " average: " + (LogViewGroup.layoutTime / LogViewGroup.layoutCall))
            val allTime = (addTime / addCount + LogViewGroup.measureTime / LogViewGroup.measureCall + LogViewGroup.layoutTime / LogViewGroup.layoutCall)
            Log.i("LogView", "allTime: " + allTime + " average: " + allTime / 3)
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