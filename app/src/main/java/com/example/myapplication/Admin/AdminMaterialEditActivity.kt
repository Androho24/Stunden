package com.example.myapplication.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Barcode.CaptureAct
import com.example.myapplication.Database.GoogleFirebase
import com.example.myapplication.Objects.Material
import com.example.myapplication.R
import com.example.myapplication.XmlTool
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class AdminMaterialEditActivity : AppCompatActivity() {

    var textViewMaterialUnitToEdit : TextView? = null
    var textViewMaterialNameToEdit : TextView? = null
    var spinnerUnitNew : Spinner? = null
    var editTextNewMaterialName : TextView? = null
    var buttonCancel : Button? = null
    var buttonSave : Button? = null
    var buttonDelete: Button? = null
    var buttonBarcode : Button? = null
    var unit:String? = ""
    var name:String? = ""
    var textViewBarcodeToEdit : TextView? = null
    var textViewNewBarcode : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_material_edit_activity)
        textViewMaterialNameToEdit = findViewById(R.id.textViewNameToEditEditMatListAdmin)
        textViewMaterialUnitToEdit = findViewById(R.id.textViewUnitToEditMatListAdmin)
        spinnerUnitNew = findViewById(R.id.spinnerNewUnitEditMatListAdmin)
        editTextNewMaterialName = findViewById(R.id.editTextNewNameEditMatListAdmin)
        buttonCancel = findViewById(R.id.buttonCancelEditMatListAdmin)
        buttonDelete = findViewById(R.id.buttonDeleteEditMatListAdmin)
        buttonSave = findViewById(R.id.buttonSaveEditMatListAdmin)
        unit = intent.extras!!.getString("unit")
        name = intent.extras!!.getString("name")
        textViewMaterialNameToEdit!!.text = name
        textViewMaterialUnitToEdit!!.text = unit
        editTextNewMaterialName!!.text = name
        textViewBarcodeToEdit = findViewById(R.id.textViewBarcodeToEditMatAdmin)
        textViewNewBarcode = findViewById(R.id.textViewNewBarcodeMatEditAdmin)

        for (mat in Material.materials){
            if (mat.material == name && mat.unit == unit){
                textViewBarcodeToEdit!!.text = mat.barcode
                textViewNewBarcode!!.text = mat.barcode
            }
        }

        buttonBarcode = findViewById(R.id.buttonAddBarcodeMatEditAdmin)

        setButtonOnClickListeners()
        setSpinnerContent()


    }

    private fun setButtonOnClickListeners() {

        buttonBarcode!!.setOnClickListener {
            scanCode()
        }
        buttonSave!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material ändern")
            builder.setMessage("Möchten sie folegendes Material ändern?\n"+"Einheit: "+unit+"\n"+"Name: "+name)


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                for (mat in Material.materials){
                    if (mat.material == name && mat.unit == unit && mat.barcode == textViewBarcodeToEdit!!.text.toString()) {
                        mat.material = editTextNewMaterialName!!.text.toString()
                        mat.unit = spinnerUnitNew!!.selectedItem!!.toString()
                        mat.barcode = textViewNewBarcode!!.text.toString()
                    }
                }
                var xmlTool = XmlTool()
                xmlTool.saveOwnMaterialsToXml(Material.ownMaterials,applicationContext)
                xmlTool.saveMaterialsToXml(Material.materials,applicationContext)
                GoogleFirebase.updateMaterialToDatabase()
                finish()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }
        buttonDelete!!.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material löschen")
            builder.setMessage("Möchten sie folegendes Material löschen?\n"+"Einheit: "+unit+"\n"+"Name: "+name)


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->



                var materialLagerNew = ArrayList<Material>()
                for (mat in Material.materials){
                    if (mat.material == name && mat.unit == unit){

                    }
                    else{
                        materialLagerNew.add(mat)
                    }
                }
                var xmlTool = XmlTool()
                xmlTool.saveMaterialsToXml(Material.materials,applicationContext)
                xmlTool.saveOwnMaterialsToXml(Material.ownMaterials,applicationContext)
                GoogleFirebase.updateMaterialToDatabase()
                finish()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        buttonCancel!!.setOnClickListener {
            finish()
        }

    }

    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(CaptureAct::class.java)
        barLauncher.launch(options)
    }
    var barLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult? ->


        if (result!!.contents != null) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Result").setMessage(result.contents).setPositiveButton(
                "ok"
            ) { _, _ ->
                var i = 0
                textViewNewBarcode!!.text = result.contents.toString()


            }.show()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
    private fun setSpinnerContent() {
        var unitList = ArrayList<String>()
        unitList.add("Stck")
        unitList.add("m")

        val dataAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unitList)
        dataAdapter.setDropDownViewResource(R.layout.spinner_style)
        spinnerUnitNew!!.adapter = dataAdapter

        if (unit == "Stck"){
            spinnerUnitNew!!.setSelection(0)
        }
        else{
            spinnerUnitNew!!.setSelection(1)
        }
    }
}