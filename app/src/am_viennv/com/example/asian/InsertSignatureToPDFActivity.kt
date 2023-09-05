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
import com.github.barteksc.pdfviewer.util.FitPolicy
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
    private lateinit var customImageView: ImageViewCustom
    private lateinit var mFilePdfChoosen: PDFView.Configurator
    private lateinit var mPdfReader: PdfReader
    private lateinit var mFileName: String
    private var mFileInput: File? = null
    private var mIsGetFromAssets = true
    private val mPdfUrl = "don_xin_viec.pdf"
    private var mScaleZoomPdf = 1f
    private var mCurrentPage = 0
    private var mImagePosX = 0f
    private var mImagePosY = 0f

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
        mPdfViewDemo.midZoom = 1f
        mPdfViewDemo.minZoom = 0.5f
        mPdfViewDemo.maxZoom = 1.2f
        mFilePdfChoosen.onPageChange { page, pageCount ->
            mCurrentPage = page
            println("onPageChange() called with: page = [$page], pageCount = [$pageCount]")
        }.onPageScroll { page, pageCount ->
            mCurrentPage = page
            println("onPageScroll() called with: page = [$page], pageCount = [$pageCount]")
        }.swipeHorizontal(true).pageFitPolicy(FitPolicy.WIDTH).pageFling(true).fitEachPage(true)
            .autoSpacing(true).load()
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
        if (!checkAllFilesAccessPermission()) {
            requestPermissions()
            return
        }
        val outputDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val time = listOf(('0'..'9'), ('a'..'z'), ('A'..'Z')).flatten().random()
        val outputFileName = "export$time-$mFileName"
        val outputFile = File(outputDirectory, outputFileName)

        try {
            val pdfDocument = createPdfDocument(outputFile)
            addImageToPdf(pdfDocument)
            Toast.makeText(baseContext, "Save to: ${outputFile.absolutePath}", Toast.LENGTH_SHORT)
                .show()
            pdfDocument.close()
        } catch (e: IOException) {
            Toast.makeText(baseContext, "Save to: $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createPdfDocument(outputFile: File): PdfDocument {
        val pdfDocument: PdfDocument
        val writer = PdfWriter(FileOutputStream(outputFile))
        if (mIsGetFromAssets) {
            pdfDocument = PdfDocument(PdfReader(assets.open(mPdfUrl)), writer)
        } else {
            mPdfReader = PdfReader(mFileInput)
            pdfDocument = PdfDocument(mPdfReader, writer)
        }
        mScaleZoomPdf = mPdfViewDemo.zoom
        return pdfDocument
    }

    private fun addImageToPdf(pdfDocument: PdfDocument) {
        var page = pdfDocument.getPage(mCurrentPage + 1)
        val image = ImageDataFactory.create(signatureBitmapToByteArray(mSignatureBitmap))
        var pdfCanvas = PdfCanvas(page)
        val mScaleWidth = page.pageSize.width / mBinding.pdfView.width
        val mCurrentYOffset = mBinding.pdfView.currentYOffset
        val mScaleHeight =
            page.pageSize.height / (resources.displayMetrics.heightPixels - 2 * mCurrentYOffset)
        val mCurrentXOffset =
            mBinding.pdfView.currentXOffset + resources.displayMetrics.widthPixels * mCurrentPage * mScaleZoomPdf
        val size = customImageView.getSize()
        var mImageHeight = size.second * mScaleHeight
        var mImageWidth = size.first * mScaleWidth
        val position = customImageView.getImagePosition()
        mImagePosX = position.first
        mImagePosY = position.second
        val mScaleFactorImage = customImageView.getScale()
        var pdfImageX =
            (mImagePosX - mCurrentXOffset) * mScaleWidth / mScaleZoomPdf + mBinding.imageViewSignature.width * mScaleHeight / 2 - mImageWidth / 2

        var pdfImageY =
            (resources.displayMetrics.heightPixels - mImagePosY - mCurrentYOffset) * mScaleHeight - mBinding.imageViewSignature.height * mScaleHeight + mImageHeight / 2

        if (mScaleZoomPdf < 0.6f) {
            if (pdfImageX >= page.pageSize.width) {
                page = pdfDocument.getPage(mCurrentPage + 2)
                pdfCanvas = PdfCanvas(page)
                pdfImageX -= page.pageSize.width
            }
            if (mScaleFactorImage >= 1f) {
                mImageWidth /= mScaleZoomPdf
                pdfImageX -= mImageWidth * mScaleZoomPdf / 2
                mImageWidth /= mScaleFactorImage
            } else {
                mImageHeight *= mScaleFactorImage
                pdfImageY += mImageHeight * mScaleFactorImage
            }
        }
        if (mScaleZoomPdf > 1f) {
            mImageHeight *= mScaleFactorImage
        }
        pdfCanvas.addImageWithTransformationMatrix(
            image, mImageWidth, 0f, 0f, mImageHeight, pdfImageX, pdfImageY
        )
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
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
