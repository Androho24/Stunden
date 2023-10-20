package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter


import com.ortiz.touchview.TouchImageView
import org.apache.commons.io.FileUtils.getFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Objects


class PreviewPdfActivity : AppCompatActivity() {

    var pdfView : TouchImageView? = null
    var buttonSend : Button? = null
    var path =""
    var pathToSave = ""
    var customerPrename = ""
    var customerName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_pdf_activity)
        buttonSend = findViewById(R.id.buttonSendPreview)
        pdfView = findViewById(R.id.pdf_image)

        val bundle = intent.extras
        path = bundle!!.getString("path").toString()
        pathToSave = bundle!!.getString("pathToSave").toString()
        customerName = bundle!!.getString("customerName").toString()
        customerPrename = bundle.getString("customerName").toString()
        buttonOnClickListeners()
        val file: File = File(path)
        if (!file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            val asset: InputStream = applicationContext.getAssets().open("test.pdf")
            val output = FileOutputStream(file)
            val buffer = ByteArray(2048)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        var parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        // This is the PdfRenderer we use to render the PDF.

            var pdfRenderer = PdfRenderer(parcelFileDescriptor)



        // Make sure to close the current page before opening another one.
        // Make sure to close the current page before opening another one.

        // Use `openPage` to open a specific page in PDF.
        // Use `openPage` to open a specific page in PDF.
       var currentPage = pdfRenderer.openPage(0)
        // Important: the destination bitmap must be ARGB (not RGB).
        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = Bitmap.createBitmap(
            2048,2896,
            Bitmap.Config.ARGB_8888
        )
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        // We are ready to show the Bitmap to user.
        // We are ready to show the Bitmap to user.
        pdfView!!.setImageBitmap(bitmap)
       // updateUi()
    }

    private fun buttonOnClickListeners() {
        buttonSend!!.setOnClickListener {

            var count = 0

            var pathlist = pathToSave.split("a##5")

            var newPath = pathlist[0]+count+pathlist[1]

            val reader = PdfReader(path)
            val writer = PdfWriter(newPath)
            val pdfDocument = PdfDocument(reader, writer)


            pdfDocument.close()

            var uri = File(newPath)

            var adress = arrayOf<String>("matthias.hoepfler@gmail.com")

            var photoURI = FileProvider.getUriForFile(
                Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", uri);
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, adress)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Stundenf")
            intent.putExtra(Intent.EXTRA_TEXT, "Pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.type = "message/rfc822"
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }
}