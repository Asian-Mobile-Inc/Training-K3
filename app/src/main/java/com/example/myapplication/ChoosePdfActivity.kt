package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityChoosePdfBinding
import com.shockwave.pdfium.BuildConfig
import java.io.File

@Suppress("DEPRECATION")
class ChoosePdfActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var binding: ActivityChoosePdfBinding
    private val PICK_PDF_REQUEST_CODE = 123
    private var imagePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.imageView
        imagePath = intent.getStringExtra("imagePath")


        if (!imagePath.isNullOrBlank()) {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                imageView.setImageURI(Uri.fromFile(imageFile))
            }
        }

        binding.btnBackActivity.setOnClickListener {
            onBackPressed()
        }

        binding.btnChoosePdf.setOnClickListener {
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
                openPdf(selectedPdfUri)
                // addImageToPdfAndSave(selectedPdfUri)
            }
        }
    }

    private fun openPdf(pdfUri: Uri) {

        //val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")

        val intent = Intent(this, PdfViewerActivity::class.java)
        intent.putExtra("pdfUri", pdfUri.toString())
        intent.putExtra("imagePathAdd", imagePath)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }
    }

}
