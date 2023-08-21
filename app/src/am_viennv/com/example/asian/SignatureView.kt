package com.example.asian

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup

class SignatureView(context: Context) : View(context) {

    private var mStrokeWidth = 10f
    private var mStrokeColor = Color.BLACK
    private var mBackground = Color.TRANSPARENT
    private val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f
    private var mIsClickAction = false
    private val mPaint = Paint()
    private val mPath = Path()
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mPaint.isAntiAlias = true
        mPaint.color = mStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = mStrokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        mPaint.strokeWidth = strokeWidth
        invalidate()
    }

    fun setStrokeColor(strokeColor: Int) {
        mPaint.color = strokeColor
        invalidate()
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchDownX = event.x
                mTouchDownY = event.y
                mIsClickAction = true
                mPath.moveTo(event.x, event.y)
                mLastTouchX = event.x
                mLastTouchY = event.y
                return true
            }

            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                if (mIsClickAction && isClick(event.x, event.y)) {
                    performClick()
                    mIsClickAction = false
                } else {
                    mIsClickAction = false
                    val historySize = event.historySize
                    for (i in 0 until historySize) {
                        val historicalX = event.getHistoricalX(i)
                        val historicalY = event.getHistoricalY(i)

                        mPath.lineTo(historicalX, historicalY)
                    }
                }
                mPath.lineTo(event.x, event.y)
            }

            else -> {
                return false
            }
        }

        invalidate()
        mLastTouchX = event.x
        mLastTouchY = event.y
        return true
    }

    private fun isClick(x: Float, y: Float): Boolean {
        val dx = x - mTouchDownX
        val dy = y - mTouchDownY
        return dx * dx + dy * dy < mTouchSlop * mTouchSlop
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(mBackground)
        canvas?.drawPath(mPath, mPaint)
    }

    fun clear() {
        mPath.reset()
        invalidate()
    }
}
