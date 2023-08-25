package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream
import java.net.URL


@Suppress("DEPRECATION")
class PdfViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var binding: ActivityPdfViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfView = binding.pdfView
        val pdfUriString = intent.getStringExtra("pdfUri")
        val pdfUri = Uri.parse(pdfUriString)

        val imagePath = intent.getStringExtra("imagePathAdd")

        binding.btnSave.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            } else {
                if (imagePath != null) {
                    val imageFile = File(imagePath)
                    if (imageFile.exists()) {
                        addImageToPdfAndSave(pdfUri, Uri.fromFile(imageFile))
                        imageFile.delete()
                    }
                }
                onBackPressed()
            }
        }


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }



        binding.btnNext.setOnClickListener {
            val currentPage = pdfView.currentPage
            val pageCount = pdfView.pageCount
            if (currentPage < pageCount - 1) {
                pdfView.jumpTo(currentPage + 1)
            }
        }

        binding.btnPrevious.setOnClickListener {
            val currentPage = pdfView.currentPage
            if (currentPage > 0) {
                pdfView.jumpTo(currentPage - 1)
            }
        }

        pdfView.fromUri(pdfUri)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableAnnotationRendering(true)
            .onPageChange(null)
            .enableDoubletap(true)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()
    }

    private fun addImageToPdfAndSave(pdfUri: Uri, imageUri: Uri) {


        val downloadsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFileName = "output.pdf"
        val pdfFile = File(downloadsDirectory, outputFileName)
        val outputStream = FileOutputStream(pdfFile)
        val writer = PdfWriter(outputStream)

        val inputStream = contentResolver.openInputStream(pdfUri)
        val pdf = PdfDocument(PdfReader(inputStream), writer)

        val document = Document(pdf)
        val image = Image(ImageDataFactory.create(imageUri.toString()))
        if (image != null) {
            image.scaleToFit(200f, 200f) // Scale the image if needed
            document.add(image)
        } else {
            Log.d("ddd", "null")
        }


        document.close()
        pdf.close()
        outputStream.close()

        updateMediaStore(pdfFile)

    }

    private fun updateMediaStore(file: File) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.TITLE, file.name)
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.DATA, file.absolutePath)
            put(MediaStore.MediaColumns.SIZE, file.length())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        }
    }


}
