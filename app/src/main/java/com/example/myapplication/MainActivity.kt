package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var signaturePad: SignaturePad
    private lateinit var binding: ActivityMainBinding
    private val READ_STORAGE_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        signaturePad = binding.signaturePad
        binding.btnClear.setOnClickListener { signaturePad.clear() }

        binding.btnSubmit.setOnClickListener {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    READ_STORAGE_PERMISSION_CODE
                )
        }

        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                // Bắt đầu vẽ chữ ký
            }

            override fun onSigned() {
                // Khi chữ ký đã được vẽ
            }

            override fun onClear() {
                // Khi chữ ký bị xóa
            }
        })
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
               Log.d("ddd", "why code = " + grantResults[0])
            }
        }
    }*/



    private fun saveImageToGallery() {
        val signatureBitmap = signaturePad.signatureBitmap
        val fileName = "signature_image.png"
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "SignatureImages"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        FileOutputStream(file).use { out ->
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Thêm hình ảnh vào thư viện hình ảnh
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = FileProvider.getUriForFile(
            this@MainActivity,
            "$packageName.provider",
            file
        )
        mediaScanIntent.data = contentUri
        sendBroadcast(mediaScanIntent)
    }
}
