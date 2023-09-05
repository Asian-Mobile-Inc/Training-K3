package com.example.asian

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class ImageViewCustom(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mImagePosX = 0f
    private var mImagePosY = 0f
    private var mBitmap: Bitmap? = null
    private var mReduceSpeedMove = 1.4f
    private var mScaledBitmap: Bitmap? = null
    private var mScaleFactor = 1.0f
    private var mScaleGestureDetector: ScaleGestureDetector? = null

    init {
        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        setImageBitmap(mBitmap)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector?.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mOriginalX = event.x
                mOriginalY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - mOriginalX
                val dy = event.y - mOriginalY

                x += dx / mReduceSpeedMove
                y += dy / mReduceSpeedMove

                mOriginalX = event.x
                mOriginalY = event.y
            }

            MotionEvent.ACTION_UP -> {
                mImagePosX = x
                mImagePosY = y
                performClick()
            }
        }
        return true
    }

    fun getScale() : Float {
        return mScaleFactor
    }

    fun getImagePosition(): Pair<Float, Float> {
        return Pair(mImagePosX, mImagePosY)
    }

    private var mWidthImage : Float = 0f
    private var mHeightImage : Float = 0f

    fun getSize(): Pair<Float, Float>{
        return Pair(mWidthImage, mHeightImage)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        mBitmap?.let {
            if (mScaledBitmap == null) {
                mWidthImage = it.width / 6f
                mHeightImage = it.height / 6f
                mScaledBitmap = Bitmap.createScaledBitmap(it,
                    mWidthImage.toInt(), mHeightImage.toInt(), true)
            }

            val centerX = (width - mScaledBitmap!!.width * mScaleFactor) / 2f
            val centerY = (height - mScaledBitmap!!.height * mScaleFactor) / 2f

            val matrix = Matrix()
            matrix.postScale(mScaleFactor, mScaleFactor)
            matrix.postTranslate(centerX, centerY)

            canvas.drawBitmap(mScaledBitmap!!, matrix, null)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = mScaleFactor.coerceIn(0.5f, 1.2f)
            invalidate()
            return true
        }

        override fun onScaleBegin(p0: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector) {
        }
    }
}
