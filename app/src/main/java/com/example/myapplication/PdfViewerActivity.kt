package com.example.myapplication

import android.annotation.SuppressLint
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
import android.util.TypedValue
import android.view.MotionEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.bumptech.glide.Glide
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
    private var limit = 1870f / 3.5f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfView = binding.pdfView
        val pdfUriString = intent.getStringExtra("pdfUri")
        val pdfUri = Uri.parse(pdfUriString)
        val imageView = binding.imageView
        val imagePath = intent.getStringExtra("imagePathAdd")

        imageView.setImageURI(Uri.fromFile(File(imagePath)))

        var lastX = 0f
        var lastY = 0f
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    123
                )

            }
        }

        imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    var distanceX = event.x - lastX
                    var distanceY = event.y - lastY

                    view.x = view.x + distanceX
                    view.y = view.y + distanceY
                    Log.d("ddd", "" + view.x + " || " + view.y)
                }

                MotionEvent.ACTION_UP -> {
                    lastX = view.x
                    lastY = view.y
                }
            }
            true
        }

        binding.btnSave.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            } else {
                if (imagePath != null) {
                    val imageFile = File(imagePath)
                    if (imageFile.exists()) {
                        addImageToPdfAndSave(
                            pdfUri,
                            Uri.fromFile(imageFile),
                            pdfView.currentPage,
                            lastX / resources.displayMetrics.density / 0.6653f,
                            lastY / resources.displayMetrics.density / 0.6746f
                        )

                        imageFile.delete()
                    }
                }
                onBackPressed()
            }
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
            Log.d("ddd", "" + resources.displayMetrics.density)
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
            .enableSwipe(false)
//            .swipeHorizontal(false)
            .enableAnnotationRendering(true)
            .onPageChange(null)
            .enableDoubletap(true)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()
    }

    private fun addImageToPdfAndSave(
        pdfUri: Uri,
        imageUri: Uri,
        pageIndex: Int,
        lastX: Float,
        lastY: Float
    ) {
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

        image.scaleToFit(120f, 120f)
        image.setFixedPosition(pageIndex + 1, lastX, limit / 0.6764f - lastY - 120f)
        document.add(image)


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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("ddd", "ddd")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                onBackPressed()
            }
        }
    }
}
