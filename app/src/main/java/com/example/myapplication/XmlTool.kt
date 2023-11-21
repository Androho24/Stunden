package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import com.example.myapplication.Objects.Customer
import com.example.myapplication.Objects.Material
import com.thoughtworks.xstream.XStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException

class XmlTool {

    @Synchronized
    fun loadSavedProfilefromXml(context: Context) {
        try {
            val xStream = XStream()
            xStream.allowTypes(arrayOf<Class<*>>(Customer::class.java))
            xStream.alias("savedCustomers", Customer::class.java)
            val pixelExistFile = File(context.cacheDir.toString() + "/" + "savedcustomers.xml")
            //File pixelExistFile = new File(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOCUMENTS+"/"+"ownpixels"+".xml");
            val fos1: InputStream
            fos1 = if (pixelExistFile.exists()) {
                FileInputStream(context.cacheDir.toString() + "/" + "savedcustomers.xml")
                // fos1 = new FileInputStream(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOCUMENTS+"/"+"ownpixels"+".xml");
            } else {
                context.resources.openRawResource(R.raw.savedcustomers)
            }
            val item = xStream.fromXML(fos1) as ArrayList<Customer>
            Customer.arrayCustomers= item
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
    @Synchronized
    fun loadMaterialsFromXml(context:Context){
        try {
            val xStream = XStream()
            xStream.allowTypes(arrayOf<Class<*>>(Material::class.java))
            xStream.alias("Material", Material::class.java)
            val pixelExistFile = File(context.cacheDir.toString() + "/" + "materials.xml")
            //File pixelExistFile = new File(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOCUMENTS+"/"+"ownpixels"+".xml");
            val fos1: InputStream
            fos1 = if (pixelExistFile.exists()) {
                FileInputStream(context.cacheDir.toString() + "/" + "materials.xml")
                // fos1 = new FileInputStream(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOCUMENTS+"/"+"ownpixels"+".xml");
            } else {
                context.resources.openRawResource(R.raw.materials)
            }
            val item = xStream.fromXML(fos1) as ArrayList<Material>
            Material.materials= item
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun saveProfilesToXml(customers: ArrayList<Customer>?, context: Context) {
        try {
            val xStream = XStream()
            xStream.allowTypes(arrayOf<Class<*>>(Customer::class.java))
            xStream.alias("savedCustomers", Customer::class.java)
            val asdfd = xStream.toXML(customers)
            val fos = FileOutputStream(context.cacheDir.toString() + "/" + "savedcustomers.xml")
            //FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOCUMENTS+"/"+"ownpixels"+".xml");
            fos.write("<?xml version=\"1.0\"?>".toByteArray(charset("UTF-8")))
            val bytes = asdfd.toByteArray(charset("UTF-8"))
            fos.write(bytes)
            fos.close()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}