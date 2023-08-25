package com.example.asian

import android.Manifest
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import java.io.IOException

class InsertSignatureToPDFActivity : AppCompatActivity() {

    private lateinit var mPdfViewDemo: PDFView
    private lateinit var mBinding: ActivityInsertImageToPdfBinding
    private lateinit var mSignatureBitmap: Bitmap
    private var mCurrentPage: Int = 0
    private var mImagePosX = 0f
    private var mImagePosY = 0f
    private lateinit var mFilePdfChoosen: PDFView.Configurator
    private lateinit var mPdfReader: PdfReader
    private lateinit var mFileName: String
    private val mPdfUrl = "register-job.pdf"
    private var mFileInput: File? = null
    private var mIsGetFromAssets: Boolean = true
    private val mScaleImageView = 1.45f
    private var mScaleZoomPdf = 1f
    private lateinit var customImageView: ImageViewCustom

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
        customImageView = findViewById(R.id.imageViewSignature)
    }

    private fun handleAction() {
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
            customImageView.setBitmap(mSignatureBitmap)
        }
    }

    private fun configPdfView() {
        mPdfViewDemo.maxZoom = 1.6f
        mPdfViewDemo.minZoom = 1f
        mPdfViewDemo.midZoom = 1.3f
        mFilePdfChoosen.onPageChange { page, pageCount ->
            mCurrentPage = page
            println("onPageChange() called with: page = [$page], pageCount = [$pageCount]")
        }.onPageScroll { page, pageCount ->
            mCurrentPage = page
            println("onPageScroll() called with: page = [$page], pageCount = [$pageCount]")
        }.swipeHorizontal(true).load()
    }

    private fun initView() {
        mBinding = ActivityInsertImageToPdfBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mPdfViewDemo = mBinding.pdfView
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
            mScaleZoomPdf = mPdfViewDemo.zoom
            val page = pdfDocument.getPage(mCurrentPage + 1)
            val image = ImageDataFactory.create(signatureBitmapToByteArray(mSignatureBitmap))
            val pdfCanvas = PdfCanvas(page)

            val pdfPageWidth = page.pageSize.width
            val pdfPageHeight = page.pageSize.height
            val mScaleWidth = pdfPageWidth / mBinding.pdfView.width
            val mScaleHeight = pdfPageHeight / mBinding.pdfView.measuredHeight
            val mCurrentYOffset = mBinding.pdfView.currentYOffset
            val mCurrentXOffset =
                mBinding.pdfView.currentXOffset + 1080f * mCurrentPage * mScaleZoomPdf
            val mImageHeight = mBinding.imageViewSignature.measuredHeight.toFloat() * mScaleHeight
            val mImageWidth = mBinding.imageViewSignature.measuredWidth.toFloat() * mScaleWidth

            val position = customImageView.getImagePosition()
            mImagePosX = position.first
            mImagePosY = position.second

            val pdfImageX =
                (mImagePosX - mCurrentXOffset) * mScaleWidth / mScaleZoomPdf
            val pdfImageY =
                mScaleImageView * (
                        mBinding.pdfView.measuredHeight - mImagePosY - mImageHeight - mCurrentYOffset) *
                        mScaleHeight / mScaleZoomPdf

            pdfCanvas.addImageWithTransformationMatrix(
                image,
                mImageWidth,
                0f,
                0f,
                mImageHeight,
                pdfImageX,
                pdfImageY
            )
            Toast.makeText(baseContext, "Save to: ${outputFile.absolutePath}", Toast.LENGTH_SHORT)
                .show()
            pdfDocument.close()
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
                            } catch (e: IOException) {
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
