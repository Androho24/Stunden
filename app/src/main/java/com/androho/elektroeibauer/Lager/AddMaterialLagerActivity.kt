package com.androho.elektroeibauer.Lager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androho.elektroeibauer.Adapter.MaterialAdapterAddLager
import com.androho.elektroeibauer.Barcode.CaptureAct
import com.androho.elektroeibauer.Objects.Material
import com.androho.elektroeibauer.R
import com.androho.elektroeibauer.XmlTool
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class AddMaterialLagerActivity : AppCompatActivity() {
    var lastString =""
    var tableMaterial : RecyclerView? = null
    var editTextFilter : EditText? = null
    var buttonAddMaterial : Button? = null
    var editUnit : Spinner? = null
    var editMatName : EditText? = null
    var mainScrollView : ScrollView? = null
    var buttonBarcode : Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.material_lager_activity)
        tableMaterial = findViewById(R.id.recycleViewMaterialLagerAddLager)
        editTextFilter = findViewById(R.id.editTextFilterMaterialAddLager)
        tableMaterial!!.layoutManager = LinearLayoutManager(this)
        buttonAddMaterial = findViewById(R.id.buttonAddMaterialMaterialAddLager)
        editUnit = findViewById(R.id.spinnerLagerAddLager)
        editMatName = findViewById(R.id.editTextNewMaterialMaterialAddLager)
        mainScrollView = findViewById(R.id.scrollMaterialLager)
        buttonBarcode = findViewById(R.id.buttonAddMaterialBarcodeAddLager)
        Material.connectMaterial()
        var adapter = MaterialAdapterAddLager(Material.connectedMaterials,applicationContext)
        tableMaterial!!.adapter = adapter


        onTextChanged()
        onButtonClickListeners()
        setSpinnerContent()

    }

    private fun setSpinnerContent() {
        var unitList = ArrayList<String>()
        unitList.add("Stck")
        unitList.add("m")

        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        editUnit!!.adapter = dataAdapter
    }

    private fun onButtonClickListeners() {
        buttonBarcode!!.setOnClickListener {
            scanCode()
        }
        buttonAddMaterial!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material hinzufügen")
            builder.setMessage("Möchten sie folegendes Material hinzufügen?\n\n"+"Einheit: "+editUnit!!.selectedItem.toString()+"\n"+"Name: "+editMatName!!.text.toString())


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                var materialExists = false
                for (mat in Material.connectedMaterials){
                    if (editMatName!!.text.toString() == mat.material){
                        materialExists = true
                    }
                }

                if (materialExists){
                    Toast.makeText(this,"Material existiert bereits", Toast.LENGTH_SHORT).show()
                }
                else {
                    var i =0
                    for (mat in Material.ownMaterials){
                        if (mat.id > 0){
                            i = mat.id
                        }
                    }
                    var mat = Material(i+1,editMatName!!.text.toString(), editUnit!!.selectedItem!!.toString(),"",0,false)
                    Material.ownMaterials.add(mat)

                    var xmlTool = XmlTool()
                    xmlTool.saveOwnMaterialsToXml(Material.ownMaterials, applicationContext)
                    Material.connectMaterial()
                    var adapter = MaterialAdapterAddLager(Material.connectedMaterials,applicationContext)
                    tableMaterial!!.adapter = adapter
                    editTextFilter!!.setText(mat.material)
                    mainScrollView!!.fullScroll(ScrollView.FOCUS_UP)
                }
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()

        }

    }

    private fun onTextChanged() {

        editTextFilter!!.doAfterTextChanged {

            if (lastString.length < editTextFilter!!.text.toString().length) {
                var i = 0
                var listMaterial = ArrayList<Material>()
                var seperatedBySpace = false
                var splitSearch = editTextFilter!!.text.toString().split(" ")
                if (splitSearch.size >1){
                    seperatedBySpace = true
                }
                for (mat in Material.connectedMaterials) {
                    if (seperatedBySpace == false) {
                        if (mat.material.contains(
                                editTextFilter!!.text.toString(),
                                ignoreCase = true
                            )
                        ) {
                            listMaterial.add(mat)
                        }
                    }
                    else{

                        if (splitSearch.size == 2){
                            if (mat.material.contains(splitSearch[0],true) && mat.material.contains(splitSearch[1],true)){
                                listMaterial.add(mat)
                            }

                        }
                        else if (splitSearch.size == 3){
                            if (mat.material.contains(splitSearch[0],true) && mat.material.contains(splitSearch[1],true)&& mat.material.contains(splitSearch[2],true)){
                                listMaterial.add(mat)
                            }
                        }
                        else if (splitSearch.size == 4){
                            if (mat.material.contains(splitSearch[0],true) && mat.material.contains(splitSearch[1],true)&& mat.material.contains(splitSearch[2],true)&& mat.material.contains(splitSearch[3],true)){
                                listMaterial.add(mat)
                            }
                        }
                        else
                        {
                            if(mat.material.contains(editTextFilter!!.text.toString())){
                                listMaterial.add(mat)
                            }
                        }

                    }



                }
                var adapter = MaterialAdapterAddLager(listMaterial,applicationContext)
                tableMaterial!!.adapter = adapter
            }
            else{
                var adapter = MaterialAdapterAddLager(Material.connectedMaterials,applicationContext)
                tableMaterial!!.adapter = adapter
            }

        }



        editMatName!!.setOnClickListener {
            mainScrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
        }

        editTextFilter!!.setOnClickListener {
            mainScrollView!!.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LagerActivity.materialEditResult){
            var adapter = MaterialAdapterAddLager(Material.materials,applicationContext)
            tableMaterial!!.adapter = adapter
            editTextFilter!!.setText("")
        }
    }
    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(false)
        options.setOrientationLocked(true)
        options.setCaptureActivity(CaptureAct::class.java)
        barLauncher.launch(options)
    }

    var barLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult? ->


        if (result!!.contents != null) {
          /*  var builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Result").setMessage(result.contents).setPositiveButton(
                "ok"
            ) { _, _ ->*/
                var i = 0
                for (mat in Material.connectedMaterials) {
                    if (result.contents.toString() == mat.barcode) {
                        editTextFilter!!.setText(mat.material)
                    }
                }

        /*    }.show()*/
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}