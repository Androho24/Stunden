package com.example.myapplication.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
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
import org.apache.commons.math3.linear.ArrayRealVector
import org.w3c.dom.Text

class AdminMaterialEditActivity : AppCompatActivity() {

    var isFromCustMat = false
    var textViewMaterialUnitToEdit: TextView? = null
    var textViewMaterialNameToEdit: TextView? = null
    var spinnerUnitNew: Spinner? = null
    var editTextNewMaterialName: TextView? = null
    var buttonCancel: Button? = null
    var buttonSave: Button? = null
    var buttonDelete: Button? = null
    var buttonBarcode: Button? = null
    var unit: String? = ""
    var name: String? = ""
    var textViewBarcodeToEdit: TextView? = null
    var textViewNewBarcode: TextView? = null
    var textViewAmountToEdit: TextView? = null
    var textViewOrderToEdit: TextView? = null

    var textViewAmountNew: TextView? = null
    var checkBoxOrderNew: CheckBox? = null

    var buttonDeleteBarcode: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_material_edit_activity)
        textViewMaterialNameToEdit = findViewById(R.id.textViewNameToEditEditMatListAdmin)
        textViewMaterialUnitToEdit = findViewById(R.id.textViewUnitToEditMatListAdmin)
        textViewAmountToEdit = findViewById(R.id.textViewMatAmountToEditAdim)
        textViewOrderToEdit = findViewById(R.id.textViewMatOrderToEditAdmin)

        textViewAmountNew = findViewById(R.id.editTextAmountNewMatAdmin)
        checkBoxOrderNew = findViewById(R.id.checkBoxMatOrderAdmin)


        spinnerUnitNew = findViewById(R.id.spinnerNewUnitEditMatListAdmin)
        editTextNewMaterialName = findViewById(R.id.editTextNewNameEditMatListAdmin)
        buttonCancel = findViewById(R.id.buttonCancelEditMatListAdmin)
        buttonDelete = findViewById(R.id.buttonDeleteEditMatListAdmin)
        buttonSave = findViewById(R.id.buttonSaveEditMatListAdmin)
        buttonDeleteBarcode = findViewById(R.id.buttonDeleteBarcodeMatEditAdmin)

        unit = intent.extras!!.getString("unit")
        name = intent.extras!!.getString("name")
        isFromCustMat = intent.extras!!.getBoolean("ownMat")
        Material.connectMaterial()

        textViewMaterialNameToEdit!!.text = name
        textViewMaterialUnitToEdit!!.text = unit
        editTextNewMaterialName!!.text = name
        textViewBarcodeToEdit = findViewById(R.id.textViewBarcodeToEditMatAdmin)
        textViewNewBarcode = findViewById(R.id.textViewNewBarcodeMatEditAdmin)
        if (isFromCustMat) {
            for (mat in Material.adminCustList) {
                if (mat.material == name && mat.unit == unit) {
                    textViewBarcodeToEdit!!.text = mat.barcode
                    textViewNewBarcode!!.text = mat.barcode
                    if (mat.bestellen == true) {
                        textViewOrderToEdit!!.setText("Ja")
                    } else {
                        textViewOrderToEdit!!.setText("Nein")
                    }
                    textViewAmountToEdit!!.setText(mat.anzahl.toString())
                }
            }
        } else {
            for (mat in Material.materials) {
                if (mat.material == name && mat.unit == unit) {
                    textViewBarcodeToEdit!!.text = mat.barcode
                    textViewNewBarcode!!.text = mat.barcode
                    if (mat.bestellen == true) {
                        textViewOrderToEdit!!.setText("Ja")
                    } else {
                        textViewOrderToEdit!!.setText("Nein")
                    }
                    textViewAmountToEdit!!.setText(mat.anzahl.toString())
                }
            }
        }

        buttonBarcode = findViewById(R.id.buttonAddBarcodeMatEditAdmin)

        setButtonOnClickListeners()
        setSpinnerContent()


    }

    private fun setButtonOnClickListeners() {

        buttonDeleteBarcode!!.setOnClickListener {
            textViewNewBarcode!!.text = ""
        }
        buttonBarcode!!.setOnClickListener {
            scanCode()
        }
        buttonSave!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material ändern")
            builder.setMessage("Möchten Sie folgendes Material ändern?\n" + "Einheit: " + unit + "\n" + "Name: " + name)


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                if (isFromCustMat) {
                    for (mat in Material.adminCustList) {
                        var barcodeForMat = ""
                        if (mat.barcode == null) {
                            barcodeForMat = ""
                        } else {
                            barcodeForMat = mat.barcode!!
                        }

                        if (mat.material == name && mat.unit == unit && barcodeForMat == textViewBarcodeToEdit!!.text.toString()) {
                            mat.material = editTextNewMaterialName!!.text.toString()
                            mat.unit = spinnerUnitNew!!.selectedItem!!.toString()
                            mat.barcode = textViewNewBarcode!!.text.toString()
                            mat.bestellen = checkBoxOrderNew!!.isChecked
                            if (!textViewAmountNew!!.text.toString().equals("")) {
                                mat.anzahl = textViewAmountNew!!.text.toString().toInt()
                            }
                        }
                    }
                } else {
                    for (mat in Material.materials) {
                        var barcodeForMat = ""
                        if (mat.barcode == null) {
                            barcodeForMat = ""
                        } else {
                            barcodeForMat = mat.barcode!!
                        }
                        var tsesaf = textViewBarcodeToEdit!!.text.toString()
                        var sf = name
                        var asdf = mat.material
                        var asdfasdf = mat.unit
                        var sadfasdfsdaf = unit
                        if (mat.material == name && mat.unit == unit && barcodeForMat == textViewBarcodeToEdit!!.text.toString()) {
                            mat.material = editTextNewMaterialName!!.text.toString()
                            mat.unit = spinnerUnitNew!!.selectedItem!!.toString()
                            mat.barcode = textViewNewBarcode!!.text.toString()
                            mat.bestellen = checkBoxOrderNew!!.isChecked
                            if (!textViewAmountNew!!.text.toString().equals("")) {
                                mat.anzahl = textViewAmountNew!!.text.toString().toInt()
                            }
                        }
                    }
                    var xmlTool = XmlTool()
                    xmlTool.saveOwnMaterialsToXml(Material.ownMaterials, applicationContext)
                    xmlTool.saveMaterialsToXml(Material.materials, applicationContext)
                   // GoogleFirebase.updateMaterialToDatabase()
                }

                var intent = Intent()
                setResult(AdminUploadCustomMaterialsActivity.custEditResult, intent)
                finish()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }
        buttonDelete!!.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Material löschen")
            builder.setMessage("Möchten sie folegendes Material löschen?\n" + "Einheit: " + unit + "\n" + "Name: " + name)


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                if (isFromCustMat) {
                    var matCustList = ArrayList<Material>()
                    for (mat in Material.adminCustList) {
                        if (mat.material == name && mat.unit == unit) {

                        } else {
                            matCustList.add(mat)
                        }
                    }
                    Material.adminCustList = matCustList
                } else {
                    var materialLagerNew = ArrayList<Material>()
                    for (mat in Material.materials) {
                        if (mat.material == name && mat.unit == unit) {

                        } else {
                            materialLagerNew.add(mat)
                        }
                    }

                    Material.materials = materialLagerNew
                    var xmlTool = XmlTool()
                    xmlTool.saveMaterialsToXml(Material.materials, applicationContext)
                    xmlTool.saveOwnMaterialsToXml(Material.ownMaterials, applicationContext)
                    GoogleFirebase.updateMaterialToDatabase()
                }
                var intent = Intent()
                setResult(AdminUploadCustomMaterialsActivity.custEditResult, intent)
                finish()

            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }

            builder.show()
        }

        buttonCancel!!.setOnClickListener {
            var intent = Intent()
            setResult(AdminUploadCustomMaterialsActivity.custEditResult, intent)
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

        if (unit == "Stck") {
            spinnerUnitNew!!.setSelection(0)
        } else {
            spinnerUnitNew!!.setSelection(1)
        }
    }
}