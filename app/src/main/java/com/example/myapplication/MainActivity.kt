package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var signaturePad: SignaturePad
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            saveImageToGallery()
        } else {
            // Xử lý khi quyền không được cấp
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        signaturePad = binding.signaturePad
        binding.btnClear.setOnClickListener { signaturePad.clear() }

        binding.btnSubmit.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Quyền đã được cấp, xử lý nút like
                saveImageToGallery()
            } else {
                // Quyền chưa được cấp, yêu cầu cấp quyền
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
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
