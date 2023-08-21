package com.example.asian

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.asian.databinding.ActivityInsertImageToPdfBinding
import com.github.barteksc.pdfviewer.PDFView
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class InsertImageToPDF : AppCompatActivity() {

    private lateinit var mPdfViewDemo: PDFView
    private lateinit var mBinding: ActivityInsertImageToPdfBinding
    private lateinit var mSignatureBitmap: Bitmap
    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mCurrentPage: Int = 0
    private var mImagePosX = 0
    private var mImagePosY = 0
    private lateinit var mFilePdfChoosen: PDFView.Configurator
    private lateinit var mPdfReader: PdfReader
    private lateinit var mFileName: String
    private val mImageWidth = 60f
    private val mImageHeight = 60f
    private val mPdfUrl = "register-job.pdf"
    private var mFileInput: File? = null
    private var mIsGetFromAssets: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initVariable()
        configPdfView()
        receiveImageViewFromMainActivity()
        handleAction()
    }

    private fun initVariable() {
        mFileName = mPdfUrl
        mFilePdfChoosen = mPdfViewDemo.fromAsset(mPdfUrl)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleAction() {
        mBinding.imageViewSignature.setOnTouchListener { _, event ->
            handleImageViewTouch(event, mBinding.imageViewSignature)
        }

        mBinding.btnExport.setOnClickListener {
            exportPdfWithImage()
        }

        mBinding.fabOpenPdf.setOnClickListener {
            if (checkAllFilesAccessPermission()) {
                selectPdf()
            } else {
                Toast.makeText(
                    baseContext,
                    "Please grant MANAGE_EXTERNAL_STORAGE permission when using",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissions()
            }

        }
    }

    private fun receiveImageViewFromMainActivity() {
        val byteArray = intent.extras!!.getByteArray("picture")
        byteArray?.let {
            mSignatureBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            mBinding.imageViewSignature.setImageBitmap(mSignatureBitmap)
        }
    }

    private fun configPdfView() {
        mPdfViewDemo.maxZoom = 1f
        mFilePdfChoosen.onPageChange { page, pageCount ->
            mCurrentPage = page
            println("onPageChange() called with: page = [$page], pageCount = [$pageCount]")
        }.onPageScroll { page, pageCount ->
            mCurrentPage = page
            println("onPageScroll() called with: page = [$page], pageCount = [$pageCount]")
        }.spacing(10).swipeHorizontal(true).enableDoubletap(true).load()
    }

    private fun initView() {
        mBinding = ActivityInsertImageToPdfBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mPdfViewDemo = mBinding.pdfView
    }

    private fun handleImageViewTouch(event: MotionEvent, imageView: ImageView): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mOriginalX = event.x
                mOriginalY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val layoutParams = imageView.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.leftMargin = event.rawX.toInt()
                layoutParams.topMargin = event.rawY.toInt()
                imageView.layoutParams = layoutParams
            }

            MotionEvent.ACTION_UP -> {
                mImagePosX = imageView.left
                mImagePosY = imageView.top
            }
        }
        return true
    }

    private fun signatureBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun exportPdfWithImage() {
        val outputDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val time = listOf(('0'..'9'), ('a'..'z'), ('A'..'Z')).flatten().random()
        val outputFileName = "export$time-$mFileName"
        val outputFile = File(outputDirectory, outputFileName)

        try {
            val pdfDocument: PdfDocument
            val writer = PdfWriter(FileOutputStream(outputFile))
            if (mIsGetFromAssets) {
                pdfDocument = PdfDocument(PdfReader(assets.open(mPdfUrl)), writer)
            } else {
                mPdfReader = PdfReader(mFileInput)
                pdfDocument = PdfDocument(mPdfReader, writer)
            }
            val page = pdfDocument.getPage(mCurrentPage + 1)
            val image = ImageDataFactory.create(signatureBitmapToByteArray(mSignatureBitmap))
            val pdfCanvas = PdfCanvas(page)

            val pdfPageWidth = page.cropBox.width
            val pdfPageHeight = page.cropBox.height

            val pdfImageX = mImagePosX.toFloat() * pdfPageWidth / mBinding.pdfView.width
            val pdfImageY =
                (pdfPageHeight - mImagePosY.toFloat() * pdfPageHeight / mBinding.pdfView.height) - mImageHeight

            pdfCanvas.addImageWithTransformationMatrix(
                image, mImageWidth, 0f, 0f, mImageHeight, pdfImageX + 20f, pdfImageY - 30f
            )
            Toast.makeText(baseContext, "Save to: ${outputFile.absolutePath}", Toast.LENGTH_SHORT)
                .show()
            pdfDocument.close()
            mPdfReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        mResultLauncher.launch(pdfIntent)
    }

    private var mResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mIsGetFromAssets = false
                val data: Intent? = result.data
                val uri = data?.data
                if (uri != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            try {
                                val buffer = ByteArray(inputStream.available())
                                inputStream.read(buffer)
                                mFileInput =
                                    File.createTempFile("temp", null, applicationContext.cacheDir)
                                mFileInput?.writeBytes(buffer)
                                mFilePdfChoosen = mPdfViewDemo.fromUri(uri)
                                inputStream.close()
                                withContext(Dispatchers.Main) {
                                    configPdfView()
                                    getFileNameFromStorage(uri)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }

    private fun getFileNameFromStorage(uri: Uri) {
        val uriString: String = uri.toString()
        val myFile = File(uriString)

        if (uriString.startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(uri, null, null, null, null)
                val columnIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor != null && cursor.moveToFirst() && columnIndex != null && columnIndex >= 0) {
                    mFileName = cursor.getString(columnIndex)
                }
            } finally {
                cursor?.close()
            }
        } else if (uriString.startsWith("file://")) {
            mFileName = myFile.name
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun checkAllFilesAccessPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.fromParts("package", packageName, null)
            })
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
    }
}
