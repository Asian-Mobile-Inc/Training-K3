package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignatureBinding
import com.github.gcacace.signaturepad.views.SignaturePad

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream


class ActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivitySignatureBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clearButton: Button = binding.clearButton
        val saveButton: Button = binding.saveButton

        val signatureView: SignatureView = binding.signatureView

        saveButton.setOnClickListener {
            val signatureBitmap = signatureView.getSignatureBitmap()

            openChooseActivity(signatureBitmap)
        }

        clearButton.setOnClickListener {
            val signatureBitmap = signatureView.getSignatureBitmap()
            signatureView.clearSignature()
        }

        /*clearButton.setOnClickListener {
            signaturePad.clear()

        }
        saveButton.setOnClickListener {
            val signatureBitmap = signaturePad.signatureBitmap
            if (signatureBitmap.width > 0 && signatureBitmap.height > 0) {
                openChooseActivity(signatureBitmap)
            }
        }*/
    }

    private fun openChooseActivity(signatureBitmap: Bitmap) {
        val intent = Intent(this, ChoosePdfActivity::class.java)

        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val file = File(cachePath, "image.jpg")
        val outputStream = FileOutputStream(file)
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Nén hình ảnh vào tệp
        outputStream.close()

        intent.putExtra("imagePath", file.absolutePath)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.d("ddd", e.toString())
        }

    }


    /*private fun isPermissonGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExternalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
        }
    }

    val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {

    })

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val uri = Uri.Builder()
                    .scheme("package")
                    .opaquePart(packageName)
                    .build()


                var intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.setData(uri)
                startActivityForResult(intent, 101)



            } catch (e: Exception) {
                var intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, 101)
                Log.d("ddd", e.toString())
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }*/





   /* private fun addImageToPdfAndSave(pdfUri: Uri) {
        val document = Document()
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val outputFileName = "output.pdf"
        val pdfFile = File(downloadsDirectory, outputFileName)
        val outputStream = FileOutputStream(pdfFile)
        val writer = PdfWriter.geItnstance(document, outputStream)

        document.open()

        val pdfReader = PdfReader(contentResolver.openInputStream(pdfUri))
        val pageCount = pdfReader.numberOfPages

        for (i in 1..pageCount) {
            val page = writer.getImportedPage(pdfReader, i)
            runBlocking (Dispatchers.IO) {
                val image = Image.getInstance("https://i.gyazo.com/6c74b3d8b0e237a1dc049cb7c204fc01.jpg")
                image.setAbsolutePosition(100f, 100f) // Set the position of the image on the page
                image.scaleToFit(200f, 200f) // Scale the image if needed
                document.add(image)
                document.newPage()
            }


        }

        document.close()
        pdfReader.close()
        outputStream.close()

        // Now you have a new PDF file with images added
    }*/
}
