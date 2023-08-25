package com.example.asian

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class ImageViewCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mImagePosX = 0f
    private var mImagePosY = 0f
    private var mBitmap: Bitmap? = null
    private var mReduceSpeedMove = 1.4f
    private var mScaledBitmap: Bitmap? = null
    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        setImageBitmap(mBitmap)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
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

    fun getImagePosition(): Pair<Float, Float> {
        return Pair(mImagePosX, mImagePosY)
    }

    override fun onDraw(canvas: Canvas) {
        mBitmap?.let {
            if (mScaledBitmap == null) {
                val width = it.width / 8
                val height = it.height / 8
                mScaledBitmap = Bitmap.createScaledBitmap(it, width, height, true)
            }
            canvas.drawBitmap(mScaledBitmap!!, 0f, 0f, null)
        }
    }
}
