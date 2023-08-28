package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignatureBinding
import java.io.File
import java.io.FileOutputStream


@Suppress("DEPRECATION")
class ActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivitySignatureBinding
    private lateinit var signatureBitmap: Bitmap

    companion object {
        private const val PICK_PDF_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signatureView: SignatureView = binding.signatureView

        binding.btnClear.setOnClickListener {
            signatureView.clearSignature()
        }

        binding.btnChoose.setOnClickListener {
            signatureBitmap = signatureView.getSignatureBitmap()
            openPdfDocument()
        }
    }

    private fun openPdfDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        startActivityForResult(intent, PICK_PDF_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { selectedPdfUri ->
                openPdf(selectedPdfUri, signatureBitmap)
            }
        }
    }

    private fun openPdf(pdfUri: Uri, signatureBitmap: Bitmap) {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "image1.jpg")
        val outputStream = FileOutputStream(file)
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdfUri", pdfUri.toString())
        intent.putExtra("imagePathAdd", file.absolutePath)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }
    }


}
