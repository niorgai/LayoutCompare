package com.example.layoutcompare;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * Created by jianqiu on 2022/1/14.
 */
public class LogViewGroup extends FrameLayout {

    public static long measureTime = 0L;
    public static long measureCall = 0L;
    public static long layoutTime = 0L;
    public static long layoutCall = 0L;

    private static final String TAG = "LogViewGroup";

    public LogViewGroup(Context context) {
        super(context);
    }

    public LogViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long start = System.nanoTime();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i(TAG, "onMeasure: " + ());
        measureTime += (System.nanoTime() - start);
        measureCall ++;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        long start = System.nanoTime();
        super.onLayout(changed, l, t, r, b);
//        Log.i(TAG, "onLayout: " + (System.nanoTime() - start));
        layoutTime += (System.nanoTime() - start);
        layoutCall ++;
    }
}
