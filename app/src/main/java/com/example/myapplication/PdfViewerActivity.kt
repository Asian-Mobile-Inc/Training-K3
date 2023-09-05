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
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
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
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class PdfViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var binding: ActivityPdfViewerBinding
    private val limitY = 1870f / 3.5f
    private val limitX = 1425f / 3.5f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var scaleGestureDetectorPdf: ScaleGestureDetector


    class ScaleImageListener(private val imageView: ImageView) :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private var scale = 1.0f
        private var scaleImage = 1f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            imageView.scaleX *= scale
            imageView.scaleY *= scale
            scaleImage = imageView.scaleX

            Log.d("ddd", "imageView: " + imageView.scaleX)
            return true
        }

        fun getScaleImageValue(): Float {
            return scaleImage
        }
    }

    private inner class ScalePDFListener(private val pdfView: PDFView) :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor = pdfView.scaleX
            Log.d("ddd", "" + scaleFactor)
            if (!(pdfView.scaleX * detector.scaleFactor > 2.0f || pdfView.scaleX * detector.scaleFactor < 0.5f)) {
                pdfView.scaleX *= detector.scaleFactor
                pdfView.scaleY *= detector.scaleFactor
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var lastPDFX = 0f
        var lastPDFY = 0f

        pdfView = binding.pdfView
        scaleGestureDetectorPdf = ScaleGestureDetector(this, ScalePDFListener(pdfView))
        pdfView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastPDFX = event.x
                    lastPDFY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val distanceX = event.x - lastPDFX
                    val distanceY = event.y - lastPDFY

                    view.x = view.x + distanceX
                    view.y = view.y + distanceY
                }

                MotionEvent.ACTION_UP -> {
                    lastPDFX = view.x
                    lastPDFY = view.y
                }
            }
            scaleGestureDetectorPdf.onTouchEvent(event)
            true

        }
        val pdfUriString = intent.getStringExtra("pdfUri")
        val pdfUri = Uri.parse(pdfUriString)
        val imageView = binding.imageView
        val imagePath = intent.getStringExtra("imagePathAdd")

        imageView.setImageURI(Uri.fromFile(imagePath?.let { File(it) }))

        var lastX = 0f
        var lastY = 0f
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 123
                )

            }
        }
        val scaleListener = ScaleImageListener(imageView)
        scaleGestureDetector = ScaleGestureDetector(this, scaleListener)


        imageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val distanceX = event.x - lastX
                    val distanceY = event.y - lastY

                    view.x = view.x + distanceX
                    view.y = view.y + distanceY
                }

                MotionEvent.ACTION_UP -> {
                    lastX = view.x
                    lastY = view.y
                }
            }
            scaleGestureDetector.onTouchEvent(event)
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
                            lastY / resources.displayMetrics.density / 0.6746f,
                            scaleListener.getScaleImageValue(),
                            lastPDFX / resources.displayMetrics.density / 0.6653f,
                            lastPDFY / resources.displayMetrics.density / 0.6746f
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
            .enableSwipe(true)
            .enableAntialiasing(true)
            .swipeHorizontal(false)
            .enableAnnotationRendering(true)
            .onPageChange(null)
            .enableDoubletap(false)
            .pageFitPolicy(FitPolicy.WIDTH).load()
    }

    private fun addImageToPdfAndSave(
        pdfUri: Uri,
        imageUri: Uri,
        pageIndex: Int,
        lastX: Float,
        lastY: Float,
        scale: Float,
        lastPDFX: Float,
        lastPDFY: Float
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

        var x = (lastX - lastPDFX - 57 * (scale - 1)) // x khi chua scale pdf
        var y =
            ((limitY / 0.6746f - lastY + lastPDFY - 120f * scale / scaleFactor) + 61 * (scale - 1)) // y khi chua scale pdf
        val h = limitY / 0.6746f
        x = (x - (limitX / 0.6653f / 2 - limitX / 0.6653f / 2 * scaleFactor)) / scaleFactor
        y =
            h - ((h / 2 - y + h / 2 * scaleFactor - 120f * scale / scaleFactor) / scaleFactor) - 120f * scale / scaleFactor

        image.scaleToFit(120f * scale / scaleFactor, 120f * scale / scaleFactor)
        image.setFixedPosition(pageIndex + 1, x, y)
        document.add(image)

        Log.d(
            "ddd",
            "" + y + " || " + (lastX - 57 * (scale - 1)) + "  --  " + ((limitY / 0.6746f - lastY - 120f * scale) + 61 * (scale - 1)) + " -- " + scaleFactor
        )

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
        } else {
            val contentUri = MediaStore.Files.getContentUri("external")
            applicationContext.contentResolver.insert(contentUri, values)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        Log.d("ddd", "ddd")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                onBackPressed()
            }
        }
    }
}
