package com.example.asian

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.asian.databinding.ActivityMainBinding
import com.example.asian.databinding.ActivityMainBinding.inflate
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mSignatureView: SignatureView
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
        handleClick()
        handleBetweenProgressBarWithSignatureView()
    }

    private fun handleBetweenProgressBarWithSignatureView() {
        mBinding.seekBarSize?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mBinding.tvValueSize?.text = progress.toString()
                    mSignatureView.setStrokeWidth(progress.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun handleClick() {
        mBinding.btnCreate?.setOnClickListener {
            mBitmap = Bitmap.createBitmap(
                mSignatureView.width,
                mSignatureView.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(mBitmap!!)
            mSignatureView.draw(canvas)
            mBinding.ivShowSignature?.setImageBitmap(mBitmap)
        }

        mBinding.fabClear?.setOnClickListener {
            mSignatureView.clear()
            mBinding.ivShowSignature?.setImageBitmap(null)
        }

        mBinding.btnInsert?.setOnClickListener {
            if (mBitmap == null) {
                Toast.makeText(baseContext, "Please draw your signature !", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val mIntent = Intent(this, InsertImageToPDF::class.java)
            val stream = ByteArrayOutputStream()
            mBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            mIntent.putExtra("picture", byteArray)
            startActivity(mIntent)
        }

        mBinding.ivColorBlack?.setOnClickListener {
            mSignatureView.setStrokeColor(ContextCompat.getColor(baseContext, R.color.colorBlack))
        }

        mBinding.ivColorBlue?.setOnClickListener {
            mSignatureView.setStrokeColor(ContextCompat.getColor(baseContext, R.color.colorBlue))
        }

        mBinding.ivColorPink?.setOnClickListener {
            mSignatureView.setStrokeColor(ContextCompat.getColor(baseContext, R.color.colorPink))
        }
    }

    private fun initVariable() {
        mBinding = inflate(layoutInflater)
        setContentView(mBinding.root)
        mSignatureView = SignatureView(this).apply {
            mBinding.flDrawSignature?.addView(this)
        }
    }
}
