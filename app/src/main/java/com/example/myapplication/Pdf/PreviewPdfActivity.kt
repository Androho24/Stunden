package com.example.myapplication.Pdf



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.SigningCustomerFragment
import com.example.myapplication.SigningFragment
import com.github.barteksc.pdfviewer.PDFView
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Objects


class PreviewPdfActivity : AppCompatActivity() , SigningFragment.onSignedCompleteListener,
    SigningCustomerFragment.onSignedCustomerCompleteListener {

        var webView: WebView? = null


    var buttonSend: Button? = null
    var path = ""
    var pathToSave = ""
    var customerPrename = ""
    var customerName = ""
    var buttonSign: Button? = null
    var buttonCustomerSign: Button? = null
    var isLager = false
    var pdfViewTest:PDFView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview_pdf_activity)

        buttonSend = findViewById(R.id.buttonSendPreview)

        buttonSign = findViewById(R.id.buttonSignPreview)
        buttonCustomerSign = findViewById(R.id.buttonSignCustomerPreview)
        val bundle = intent.extras
        path = bundle!!.getString("path").toString()
        pathToSave = bundle.getString("pathToSave").toString()
        customerName = bundle.getString("customerName").toString()
        customerPrename = bundle.getString("customerPrename").toString()
        var stringIsLager = bundle.getString("isLager").toString()
         pdfViewTest = findViewById(R.id.webviewTest)


        if (stringIsLager == "true"){
            isLager = true
        }
        else{
            isLager = false
        }
        buttonOnClickListeners()
        val file: File = File(path)
        if (!file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            val asset: InputStream = applicationContext.assets.open(path)
            val output = FileOutputStream(file)
            val buffer = ByteArray(2048)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        var parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        // This is the PdfRenderer we use to render the PDF.
        // This is the PdfRenderer we use to render the PDF.
        val bitmap = Bitmap.createBitmap(
            2048, 2896,
            Bitmap.Config.ARGB_8888
        )
        var pdfRenderer = PdfRenderer(parcelFileDescriptor)
        val file1 = Uri.fromFile(file)
        pdfViewTest!!.fromUri(file1).load()


      /*  var pageCount = pdfRenderer.pageCount

        for(i in 0..pageCount){
            var page = pdfRenderer.openPage(0)
            page.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

        }*/






        // Make sure to close the current page before opening another one.
        // Make sure to close the current page before opening another one.

        // Use `openPage` to open a specific page in PDF.
        // Use `openPage` to open a specific page in PDF.
      /*  var currentPage = pdfRenderer.openPage(0)

        // Important: the destination bitmap must be ARGB (not RGB).
        // Important: the destination bitmap must be ARGB (not RGB).

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)*/
        // We are ready to show the Bitmap to user.
        // We are ready to show the Bitmap to user.



       // pdfView!!.setImageBitmap(bitmap)

        // updateUi()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }


    private fun buttonOnClickListeners() {

        buttonSend!!.setOnClickListener {


            val reader = PdfReader(path)
            val writer = PdfWriter(pathToSave)
            val pdfDocument = PdfDocument(reader, writer)


            pdfDocument.close()

            if (!isLager) {

                var uri = File(pathToSave)

                var adress = arrayOf<String>("service@elektro-eibauer.de")

                var photoURI = FileProvider.getUriForFile(
                    Objects.requireNonNull(applicationContext),
                    BuildConfig.APPLICATION_ID + ".provider", uri
                )
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, adress)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Arbeitsnachweis")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Sehr geehrte Frau Mayer,\n\n anbei übersende ich Ihnen meinen Arbeitsnachweis.\n\n Mit freundlichen Grüßen\n\nIhr Arbeitnehmer"
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.type = "message/rfc822"
                intent.setType("application/pdf")
                intent.putExtra(Intent.EXTRA_STREAM, photoURI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            else{
                var uri = File(pathToSave)

                var adress = arrayOf<String>("service@elektro-eibauer.de")

                var photoURI = FileProvider.getUriForFile(
                    Objects.requireNonNull(applicationContext),
                    BuildConfig.APPLICATION_ID + ".provider", uri
                )
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, adress)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Materialschein")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Sehr geehrte Frau Mayer,\n\n anbei übersende ich Ihnen meinen Materialschein.\n\n Mit freundlichen Grüßen\n\nIhr Arbeitnehmer"
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.type = "message/rfc822"
                intent.setType("application/pdf")
                intent.putExtra(Intent.EXTRA_STREAM, photoURI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        buttonSign!!.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            val customerFragmentDialog: SigningFragment =
                SigningFragment.newInstance("Some Title")

            customerFragmentDialog.show(fm, "fragment_edit_name")
        }

        buttonCustomerSign!!.setOnClickListener {
            val fm: FragmentManager = supportFragmentManager
            val customerFragmentDialog: SigningCustomerFragment =
                SigningCustomerFragment.newInstance("Some Title")

            customerFragmentDialog.show(fm, "fragment_edit_name")
        }
    }


    override fun onSignedCompleteListener(imageView: ImageView) {
        var bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte: ByteArray = baos.toByteArray()
        val data: ImageData = ImageDataFactory.create(imageInByte)


        val pdfDoc = PdfDocument(PdfReader(path), PdfWriter("/storage/emulated/0/Documents/ElektroEibauer/test1.pdf"))
        var image = Image(data).scaleAbsolute(150f, 70f).setFixedPosition(pdfDoc.numberOfPages,350f,80f)
        var document = Document(pdfDoc)
        document.add(image)
        document.close()

        val file= File("/storage/emulated/0/Documents/ElektroEibauer/test1.pdf")

        var parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
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
         bitmap = Bitmap.createBitmap(
            2048, 2896,
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
        var orig = File(path)


        file.copyTo(orig,true)
        var file1 = Uri.fromFile(orig)
        pdfViewTest!!.removeAllViews()
        pdfViewTest!!.fromUri(file1).load()
        file.delete()
    }

    override fun onSignedCustomerCompleteListener(imageView: ImageView) {
        var bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte: ByteArray = baos.toByteArray()
        val data: ImageData = ImageDataFactory.create(imageInByte)

       // var image = Image(data).scaleAbsolute(150f, 70f).setRelativePosition(50f,660f,0f,0f)

        val pdfDoc = PdfDocument(PdfReader(path), PdfWriter("/storage/emulated/0/Documents/ElektroEibauer/test1.pdf"))
        var image = Image(data).scaleAbsolute(150f, 70f).setFixedPosition(pdfDoc.numberOfPages,50f,80f)
        var document = Document(pdfDoc)
        document.add(image)
        document.close()

        val file= File("/storage/emulated/0/Documents/ElektroEibauer/test1.pdf")

        var parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
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
        bitmap = Bitmap.createBitmap(
            2048, 2896,
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
      //  pdfView!!.setImageBitmap(bitmap)
        var orig = File(path)


        file.copyTo(orig,true)
        var file1 = Uri.fromFile(orig)
        pdfViewTest!!.removeAllViews()
        pdfViewTest!!.fromUri(file1).load()
        file.delete()
    }
}