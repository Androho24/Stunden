package com.example.myapplication.Pdf

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.CustomerMaterial
import com.example.myapplication.Objects.WorktimeMain
import com.example.myapplication.R
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.layout.Document

import com.itextpdf.layout.border.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import java.io.ByteArrayOutputStream

class PDFCreator {

    fun returnCreatedDocument(drawable: Drawable?, customer: Customer,date : String,arbeitsbeschreibung: String,worktimes: ArrayList<WorktimeMain>,path : String,count : Int,location:String) : Document{

        var doc : Document? = null
        // Creating a workbook object from the XSSFWorkbook() class
        //val doc = XWPFDocument(OPCPackage.open("/storage/emulated/0/documents/test/word.docx"))

        val dest = path
        val writer = PdfWriter(dest)

        // Creating a PdfDocument object

        // Creating a PdfDocument object
        val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(writer)

        // Creating a Document object

        // Creating a Document object
        doc = Document(pdfDoc)

        doc.add(firmenTable(drawable!!))

        // Creating a table
        val pointColumnWidths1 = floatArrayOf(400f,100f)
        val table = Table(pointColumnWidths1)
        val cell1 = Cell()
        cell1.setBorderTop(Border.NO_BORDER)
        cell1.setBorderRight(Border.NO_BORDER)
        cell1.setBorderLeft(Border.NO_BORDER)
        cell1.add("TAGELOHN-ARBEITSNACHWEIS")
        cell1.setFontSize(18f)
        table.addCell(cell1)

        val cell2 = Cell()
        cell2.setBorderTop(Border.NO_BORDER)
        cell2.setBorderRight(Border.NO_BORDER)
        cell2.setBorderLeft(Border.NO_BORDER)
        cell2.add("Lfd.-Nr.:"+count)
        cell2.setFontSize(10f)
        table.addCell(cell2)
        doc.add(table)

        val auftragColumns = floatArrayOf(250f,250f)
        val tableAuftrag = Table(auftragColumns)
        val cell1Auftrag = Cell()
        cell1Auftrag.setFontSize(11f)
        cell1Auftrag.add("Auftraggeber:")
        tableAuftrag.addCell(cell1Auftrag)

        val cell2Auftrag = Cell()
        cell2Auftrag.setFontSize(11f)
        cell2Auftrag.add("Bauvorhaben:")
        tableAuftrag.addCell(cell2Auftrag)

        if(customer.customerExpanded.clientName != ""){
            val cell3Auftrag = Cell()
            cell3Auftrag.setFontSize(11f)
            cell3Auftrag.add(customer.customerExpanded.clientName+" "+customer.customerExpanded.clientPreName+"\n"+customer.customerExpanded.clientStreetName+" "+customer.customerExpanded.clientStreetNumber+" \n"+customer.customerExpanded.clientPlz+" "+customer.customerExpanded.clientLocation)
            tableAuftrag.addCell(cell3Auftrag)
        }
        else{
        val cell3Auftrag = Cell()
        cell3Auftrag.setFontSize(11f)
        cell3Auftrag.add(customer.name+" "+customer.preName+"\n"+customer.streetName+" "+customer.streetNumber+" \n"+customer.plz+" "+customer.location)
        tableAuftrag.addCell(cell3Auftrag)
        }

        val cell4Auftrag = Cell()
        cell4Auftrag.setFontSize(11f)
        cell4Auftrag.add(customer.name+" "+customer.preName+"\n"+customer.streetName+" "+customer.streetNumber+" \n"+customer.plz+" "+customer.location)
        tableAuftrag.addCell(cell4Auftrag)

        val cell5Auftrag = Cell()
        cell5Auftrag.setFontSize(11f)
        if (customer.projectNumber != ""){
            cell5Auftrag.add("Projektnummer: "+customer.projectNumber)
        }
        else {
            cell5Auftrag.add("Projektnummer: ")
        }
        tableAuftrag.addCell(cell5Auftrag)

        val cell6Auftrag = Cell()
        cell6Auftrag.setFontSize(11f)
        cell6Auftrag.add("Datum: "+date)
        tableAuftrag.addCell(cell6Auftrag)






        doc.add(tableAuftrag)
        doc.add(tableZeiten(worktimes,date,arbeitsbeschreibung))
        doc.add(tableMaterial())
        doc.add(tableSigning(date,location))


     return doc
    }



    private fun tableSigning(date: String,location: String): Table {
        val pointColumnWidths1 = floatArrayOf(250f,50f,250f)
        val table = Table(pointColumnWidths1)
        val cell11 = Cell()
        cell11.setBorderTop(Border.NO_BORDER)
        cell11.setBorderRight(Border.NO_BORDER)
        cell11.setBorderLeft(Border.NO_BORDER)
        cell11.setBorderBottom(Border.NO_BORDER)
        cell11.setFontSize(10f)

        cell11.add("o.g. Angaben bestätigt:")
        table.addCell(cell11)

        val cell21 = Cell()
        cell21.setBorderTop(Border.NO_BORDER)
        cell21.setBorderRight(Border.NO_BORDER)
        cell21.setBorderLeft(Border.NO_BORDER)
        cell21.setBorderBottom(Border.NO_BORDER)
        cell21.setFontSize(10f)
        cell21.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell21)

        val cell31 = Cell()
        cell31.setBorderTop(Border.NO_BORDER)
        cell31.setBorderRight(Border.NO_BORDER)
        cell31.setBorderLeft(Border.NO_BORDER)
        cell31.setBorderBottom(Border.NO_BORDER)
        cell31.setFontSize(10f)
        cell31.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell31)





        val cell1 = Cell()
        cell1.setBorderTop(Border.NO_BORDER)
        cell1.setBorderRight(Border.NO_BORDER)
        cell1.setBorderLeft(Border.NO_BORDER)
        cell1.setBorderBottom(Border.NO_BORDER)
        cell1.setFontSize(10f)
        cell1.setHeight(70f)
        cell1.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell1)

        val cell2 = Cell()
        cell2.setBorderTop(Border.NO_BORDER)
        cell2.setBorderRight(Border.NO_BORDER)
        cell2.setBorderLeft(Border.NO_BORDER)
        cell2.setBorderBottom(Border.NO_BORDER)
        cell2.setFontSize(10f)
        cell2.setHeight(70f)
        cell2.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell2)

        val cell3 = Cell()
        cell3.setBorderTop(Border.NO_BORDER)
        cell3.setBorderRight(Border.NO_BORDER)
        cell3.setBorderLeft(Border.NO_BORDER)
        cell3.setBorderBottom(Border.NO_BORDER)
        cell3.setFontSize(10f)
        cell3.setHeight(70f)
        cell3.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell3)

        val cell131 = Cell()
        cell131.setBorderTop(Border.NO_BORDER)
        cell131.setBorderRight(Border.NO_BORDER)
        cell131.setBorderLeft(Border.NO_BORDER)
        cell131.setBorderBottom(Border.NO_BORDER)
        cell131.setFontSize(10f)
        cell131.add(location+", "+date)
        table.addCell(cell131)

        val cell231 = Cell()
        cell231.setBorderTop(Border.NO_BORDER)
        cell231.setBorderRight(Border.NO_BORDER)
        cell231.setBorderLeft(Border.NO_BORDER)
        cell231.setBorderBottom(Border.NO_BORDER)
        cell231.setFontSize(10f)
        cell231.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell231)

        val cell331 = Cell()

        cell331.setBorderTop(Border.NO_BORDER)
        cell331.setBorderRight(Border.NO_BORDER)
        cell331.setBorderLeft(Border.NO_BORDER)
        cell331.setBorderBottom(Border.NO_BORDER)
        cell331.setFontSize(10f)
        cell331.add(location+", "+date)
        table.addCell(cell331)


        val cell13 = Cell()

        cell13.setBorderRight(Border.NO_BORDER)
        cell13.setBorderLeft(Border.NO_BORDER)
        cell13.setBorderBottom(Border.NO_BORDER)
        cell13.setFontSize(10f)
        cell13.add("Ort,Datum/Unterschrift - Auftraggeber").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell13)

        val cell23 = Cell()
        cell23.setBorderTop(Border.NO_BORDER)
        cell23.setBorderRight(Border.NO_BORDER)
        cell23.setBorderLeft(Border.NO_BORDER)
        cell23.setBorderBottom(Border.NO_BORDER)
        cell23.setFontSize(10f)
        cell23.add("").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell23)

        val cell33 = Cell()

        cell33.setBorderRight(Border.NO_BORDER)
        cell33.setBorderLeft(Border.NO_BORDER)
        cell33.setBorderBottom(Border.NO_BORDER)
        cell33.setFontSize(10f)
        cell33.add("Ort,Datum/Unterschrift - Monteur").setTextAlignment(TextAlignment.CENTER)
        table.addCell(cell33)

        return  table
    }

    private fun firmenTable(drawable: Drawable): Table {



        val pointColumnWidths1 = floatArrayOf(250f,250f)
        val table = Table(pointColumnWidths1)
        val cell1 = Cell()
        cell1.setBorderTop(Border.NO_BORDER)
        cell1.setBorderRight(Border.NO_BORDER)
        cell1.setBorderLeft(Border.NO_BORDER)
        cell1.setBorderBottom(Border.NO_BORDER)
        cell1.setFontSize(10f)
        cell1.setHeight(90f)
        cell1.add("Elektro Eibauer").setTextAlignment(TextAlignment.CENTER)

        cell1.add("Oberviehmoos 15")
        cell1.add("84164 Moosthenning")
        cell1.add("08731/3256686")
        table.addCell(cell1)

        val cell2 = Cell()
        cell2.setBorderTop(Border.NO_BORDER)
        cell2.setBorderRight(Border.NO_BORDER)
        cell2.setBorderLeft(Border.NO_BORDER)
        cell2.setBorderBottom(Border.NO_BORDER)
        table.addCell(cell2)
// Load image from disk
        // Load image from disk


        val myIcon: Drawable = drawable
        val bitmap = (myIcon as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata: ByteArray = stream.toByteArray()



        var datafact = ImageDataFactory.create(bitmapdata)
// Create layout image object and provide parameters. Page number = 1
// Create layout image object and provide parameters. Page number = 1
        val image = com.itextpdf.layout.element.Image(datafact).scaleAbsolute(200f, 70f)

// This adds the image to the page
// This adds the image to the page
        cell2.add(image)


        return table

    }

    private fun tableMaterial(): Table {
        val pointColumnWidths1 = floatArrayOf(50f,50f,50f,350f)
        val table = Table(pointColumnWidths1)

        val cell1 = Cell()
        cell1.add("Pos.")
        cell1.setHeight(10f)
        cell1.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell1.setFontSize(7f)
        table.addCell(cell1)

        val cell2 = Cell()
        cell2.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell2.add("Anzahl")
        cell2.setHeight(10f)
        cell2.setFontSize(7f)
        table.addCell(cell2)

        val cell4 = Cell()
        cell4.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell4.add("Einheit")
        cell4.setHeight(10f)
        cell4.setFontSize(7f)
        table.addCell(cell4)

        val cell3 = Cell()
        cell3.setHeight(10f)
        cell3.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell3.add("Material")
        cell3.setFontSize(7f)
        table.addCell(cell3)

   /*     val cell4 = Cell()
        cell4.setBorderTop(Border.NO_BORDER)
        cell4.setHeight(10f)
        cell4.setFontSize(7f)
        table.addCell(cell4)*/

        var count = 1
        for (mat in CustomerMaterial.customerMaterials){
            var cell11 = Cell()
            cell11.add(count.toString())
            cell11.setHeight(10f)
            cell11.setFontSize(7f)
            table.addCell(cell11)

            var cell12 = Cell()
            cell12.setHeight(10f)
            cell12.add(mat.materialAmount)
            cell12.setFontSize(7f)
            table.addCell(cell12)

            var cell13 = Cell()
            cell13.setHeight(10f)
            cell13.add(mat.materialUnit)
            cell13.setFontSize(7f)
            table.addCell(cell13)

            var list = mat.materialName!!.split("\n")
            var matname = ""
            for (ma in list){
                matname = matname+ma+""
            }

            var cell14 = Cell()
            cell14.add(matname)
            cell14.setHeight(10f)
            cell14.setFontSize(7f)
            table.addCell(cell14)
            count++
        }


        for (i in count..13){
            var cell11 = Cell()
            cell11.add("")
            cell11.setHeight(10f)
            cell11.setFontSize(7f)
            table.addCell(cell11)

            var cell12 = Cell()
            cell12.setHeight(10f)
            cell12.add("")
            cell12.setFontSize(7f)
            table.addCell(cell12)

            var cell13 = Cell()
            cell13.setHeight(10f)
            cell13.add("")
            cell13.setFontSize(7f)
            table.addCell(cell13)

            var cell14 = Cell()
            cell14.add("")
            cell14.setHeight(10f)
            cell14.setFontSize(7f)
            table.addCell(cell14)
        }


        return table

    }

    private fun tableZeiten(worktimes : ArrayList<WorktimeMain>,date: String,arbeitsbeschreibung:String): Table {

        var list = arbeitsbeschreibung.split("\n")

        var workDescription : ArrayList<String> = ArrayList()

        if (list.lastIndex>=0){
            for (item in list){
                workDescription.add(item)
            }
        }
        var readyDescription = ArrayList<String>()
        var line = 0
        for (item in workDescription){
            var itemList = item.chunked(50)
           /* if (itemList.size >0){
                itemList.get(line).substringBeforeLast(" ")
            }*/
            for (chunkedItem in itemList){
                readyDescription.add(chunkedItem)
            }
        }

        if (readyDescription.size==0){
            readyDescription.add("")
        }



        val pointColumnWidths1 = floatArrayOf(48f,35f,35f,35f,30f,40f,87f,190f)
        val table = Table(pointColumnWidths1)
        val cell1 = Cell()
        cell1.add("Arbeits-\n tag")
        cell1.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell1.setFontSize(7f)
        table.addCell(cell1)

        val cell2 = Cell()
        cell2.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell2.add("Arbeits-\n beginn")
        cell2.setFontSize(7f)
        table.addCell(cell2)

        val cell3 = Cell()
        cell3.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell3.add("Arbeits-\n ende")
        cell3.setFontSize(7f)
        table.addCell(cell3)

        val cell4 = Cell()
        cell4.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell4.add("Arbeits-\n zeit in h")
        cell4.setFontSize(7f)
        table.addCell(cell4)

        val cell5 = Cell()
        cell5.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell5.add("Wege-\n zeit in h")
        cell5.setFontSize(7f)
        table.addCell(cell5)

        val cell6 = Cell()
        cell6.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell6.add("gef. km")
        cell6.setFontSize(7f)
        table.addCell(cell6)

        val cell7 = Cell()
        cell7.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell7.add("Monteure")
        cell7.setFontSize(7f)
        table.addCell(cell7)

        val cell8 = Cell()
        cell8.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
        cell8.add("Ausgeführte Arbeiten")
        cell8.setFontSize(7f)
        table.addCell(cell8)

        var positions : Int= 1

        for (worktime in worktimes){
            var cell11 = Cell()
            cell11.add("")
            cell11.setHeight(10f)
            cell11.setFontSize(7f)
            cell11.add(date)
            table.addCell(cell11)

            var cell12 = Cell()
            cell12.add("")
            cell12.setHeight(10f)
            cell12.setFontSize(7f)
            cell12.add(worktime.beginWorktime)
            table.addCell(cell12)

            var cell13 = Cell()
            cell13.add("")
            cell13.setHeight(10f)
            cell13.setFontSize(7f)
            cell13.add(worktime.endWorktime)
            table.addCell(cell13)

            var cell14 = Cell()
            cell14.add("")
            cell14.setHeight(10f)
            cell14.setFontSize(7f)
            cell14.add(worktime.workTime)
            table.addCell(cell14)

            var cell15 = Cell()
            cell15.add("")
            cell15.setHeight(10f)
            cell15.setFontSize(7f)
            cell15.add(worktime.wegeRuest)
            table.addCell(cell15)

            var cell16 = Cell()
            cell16.add("")
            cell16.setHeight(10f)
            cell16.setFontSize(7f)
            table.addCell(cell16)

            var cell17 = Cell()
            cell17.setHeight(10f)
            cell17.setFontSize(7f)
            cell17.add(worktime.workerName)
            table.addCell(cell17)

            if (positions-1 <= readyDescription.lastIndex ) {
                var cell18 = Cell()
                cell18.setHeight(10f)
                cell18.setFontSize(7f)
                cell18.add(readyDescription[positions-1])
                table.addCell(cell18)
            }
            else{
                var cell18 =  Cell()
                cell18.setHeight(10f)
                cell18.setFontSize(7f)
                cell18.add("")
                table.addCell(cell18)
            }
            positions++
        }

        for (i in positions..13){
            var cell11 = Cell()
            cell11.add("")
            cell11.setHeight(10f)
            cell11.setFontSize(7f)
            table.addCell(cell11)

            var cell12 = Cell()
            cell12.add("")
            cell12.setHeight(10f)
            cell12.setFontSize(7f)
            table.addCell(cell12)

            var cell13 = Cell()
            cell13.add("")
            cell13.setHeight(10f)
            cell13.setFontSize(7f)
            table.addCell(cell13)

            var cell14 = Cell()
            cell14.add("")
            cell14.setHeight(10f)
            cell14.setFontSize(7f)
            table.addCell(cell14)

            var cell15 = Cell()
            cell15.add("")
            cell15.setHeight(10f)
            cell15.setFontSize(7f)
            table.addCell(cell15)

            var cell16 = Cell()
            cell16.add("")
            cell16.setHeight(10f)
            cell16.setFontSize(7f)
            table.addCell(cell16)

            var cell17 = Cell()
            cell17.add("")
            cell17.setHeight(10f)
            cell17.setFontSize(7f)
            table.addCell(cell17)


            var cell18 = Cell()
            if (i-1<=readyDescription.lastIndex){
                cell18.add(readyDescription[i-1])
            }
            else {
                cell18.add("")
            }
            cell18.setHeight(10f)
            cell18.setFontSize(7f)
            table.addCell(cell18)


        }


        return table

    }

}